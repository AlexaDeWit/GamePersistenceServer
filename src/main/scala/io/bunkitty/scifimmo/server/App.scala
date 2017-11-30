package io.bunkitty.scifimmo.server


import io.bunkitty.scifimmo.server.config.{ApplicationConfigurationException, Config}
import io.bunkitty.scifimmo.server.middlewares.Authentication
import io.bunkitty.scifimmo.server.services._
import fs2._
import cats.effect._
import cats.syntax.either._
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.middleware.GZip
import org.http4s.util.{ExitCode, StreamApp}
import slick.jdbc.PostgresProfile.api._

import scala.util.{Failure, Success, Try}

object App extends StreamApp[IO] {
  private val config = Config.config
  val applicationPrereqs: Either[String, ApplicationPrerequisites] = for {
    conf <- config.leftMap(t => s"Could not load Config: Error was ${t}")
    dbConf = conf.db
    db = Database.forURL(dbConf.url, dbConf.user, dbConf.password, null, dbConf.driver)
    authMiddleware = new Authentication(db).authMiddleware
  } yield ApplicationPrerequisites(db, authMiddleware)

  def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    applicationPrereqs match {
      case Right(prereqs) => {
        val heartbeatService = HeartbeatService().route()
        val accountService = AccountService(prereqs.db).route()
        val sessionsService = SessionsService(prereqs.db).route()
        BlazeBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .mountService(heartbeatService,"/heartbeats")
          .mountService(accountService, "/api/accounts")
          .mountService(sessionsService, "/api/sessions")
          .serve
      }
      case Left(message) => Stream.fail(new ApplicationConfigurationException(message))
    }

  }
}