package io.bunkitty.scifimmo.server


import io.bunkitty.scifimmo.server.config.{ApplicationConfigurationException, Config}
import io.bunkitty.scifimmo.server.middlewares.Authentication
import io.bunkitty.scifimmo.server.services._
import fs2._
import cats.effect._
import cats.syntax.either._
import doobie.util.transactor.Transactor
import fs2.StreamApp.ExitCode
import org.http4s.server.blaze.BlazeBuilder
import slick.jdbc.PostgresProfile.api._


object App extends StreamApp[IO] {
  private val config = Config.config
  val applicationPrereqs: Either[String, ApplicationPrerequisites] = for {
    conf <- config.leftMap(t => s"Could not load Config: Error was $t")
    dbConf = conf.db
    db = Database.forURL(dbConf.url, dbConf.user, dbConf.password, null, dbConf.driver)
    transactor = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver", s"jdbc:postgresql:${dbConf.schema}", dbConf.user, dbConf.password
    )
    authMiddleware = new Authentication(transactor).authMiddleware
  } yield ApplicationPrerequisites(db, authMiddleware, transactor)

  def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    applicationPrereqs match {
      case Right(prereqs) => {
        val heartbeatService = HeartbeatService().route()
        val accountService = AccountService(prereqs.transactor).route()
        val sessionsService = SessionsService(prereqs.db).route()
        val charactersService = prereqs.authMiddleware(CharactersService(prereqs.db).route())
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