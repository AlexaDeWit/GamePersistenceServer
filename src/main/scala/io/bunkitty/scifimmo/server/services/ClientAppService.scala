package construction.universe.server.services

import cats.effect.{Effect, IO}
import org.http4s.{HttpService, Request, StaticFile}
import org.http4s.dsl.Http4sDsl

import scala.io.Source

case class ClientAppService() extends Http4sDsl[IO] {

  def static(file: String, request: Request[IO]) =
    StaticFile.fromResource("/dist/" + file, Some(request)).getOrElseF(NotFound())

  def route(): HttpService[IO] = HttpService {
    case request @ GET -> Root / "build.js" =>
      static("build.js", request)
    case request @ GET -> Root =>
      static("index.html", request)
  }

}
