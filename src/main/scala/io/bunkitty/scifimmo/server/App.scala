package construction.universe.server


import fs2._
import cats.effect._
import cats.syntax.either._
import com.typesafe.config.ConfigException
import construction.universe.server.config.{ApplicationConfigurationException, Config}
import construction.universe.server.middlewares.Authentication
import construction.universe.server.services._
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.middleware.GZip
import org.http4s.util.{ExitCode, StreamApp}
import slick.jdbc.PostgresProfile.api._

import scala.util.{Failure, Success, Try}

object App extends StreamApp[IO] {
  val config: Try[Config] = Config.loadConfig
  val clientAppService: ClientAppService = ClientAppService()
  val applicationPrereqs: Either[String, ApplicationPrerequisites] = for {
    conf <- config.toEither.leftMap(t => s"Could not load Config: Error was ${t.getMessage}")
    dbConf <- conf.db.toRight("DB Config Not Found")
    db = Database.forURL(dbConf.url, dbConf.username, dbConf.password, null, dbConf.driver)
    authMiddleware = new Authentication(db).authMiddleware
  } yield ApplicationPrerequisites(db, authMiddleware)

  def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    applicationPrereqs match {
      case Right(prereqs) => {
        val heartbeatService = HeartbeatService().route()
        val clientAppService = ClientAppService().route()
        val accountService = AccountService(prereqs.db).route()
        val sessionsService = SessionsService(prereqs.db).route()
        val projectsService = prereqs.authMiddleware(ProjectsService(prereqs.db).route())
        val pagesService = prereqs.authMiddleware(PagesService(prereqs.db).route())
        BlazeBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .mountService(heartbeatService,"/heartbeats")
          .mountService(accountService, "/api/accounts")
          .mountService(sessionsService, "/api/sessions")
          .mountService(projectsService, "/api/projects")
          .mountService(pagesService, "/api/pages")
          .mountService(GZip(clientAppService), "/") //Must be last?
          .serve
      }
      case Left(message) => Stream.fail(new ApplicationConfigurationException(message))
    }

  }
}