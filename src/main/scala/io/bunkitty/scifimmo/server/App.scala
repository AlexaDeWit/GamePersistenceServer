package io.bunkitty.scifimmo.server


import io.bunkitty.scifimmo.server.config.{ApplicationConfigurationException, Config}
import io.bunkitty.scifimmo.server.middlewares.Authentication
import io.bunkitty.scifimmo.server.services._
import io.bunkitty.scifimmo.jwt.JwtService
import fs2._
import cats.effect._
import cats.syntax.either._
import doobie.util.transactor.Transactor
import fs2.StreamApp.ExitCode
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global


object App extends StreamApp[IO] {
  private val config = Config.config
  val applicationPrereqs: Either[String, ApplicationPrerequisites] = for {
    conf <- config.leftMap(t => s"Could not load Config: Error was $t")
    dbConf = conf.db
    jwtConf = conf.jwt
    transactor = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver", s"jdbc:postgresql://${dbConf.hostname}/${dbConf.schema}", dbConf.user, dbConf.password
    )
    hmacService = JwtService(jwtConf.secret)
    authMiddleware = Authentication(transactor, hmacService).authMiddleware
  } yield ApplicationPrerequisites(authMiddleware, transactor, hmacService)

  def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    applicationPrereqs match {
      case Right(prereqs) => {
        val heartbeatService = HeartbeatService().route()
        val accountService = AccountService(prereqs.transactor, prereqs.hmacService).route()
        val sessionsService = SessionsService(prereqs.transactor, prereqs.hmacService).route()
        val charactersService = prereqs.authMiddleware(CharactersService(prereqs.transactor, prereqs.hmacService).route())
        BlazeBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .mountService(heartbeatService,"/heartbeats")
          .mountService(accountService, "/api/accounts")
          .mountService(sessionsService, "/api/sessions")
          .mountService(charactersService, "/api/characters")
          .serve
      }
      case Left(message) => Stream.raiseError(new ApplicationConfigurationException(message))
    }

  }
}