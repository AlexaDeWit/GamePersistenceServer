package construction.universe.server.services

import cats.effect.{Effect, IO}
import construction.universe.server.config.Config
import construction.universe.server.dto.request.accounts.LoginRequest
import construction.universe.server.model.Users
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

case class HeartbeatService() extends Http4sDsl[IO] {

  def route(): HttpService[IO] = HttpService {
    case GET -> Root / "heartbeat" => {
      Ok()
    }
  }

}
