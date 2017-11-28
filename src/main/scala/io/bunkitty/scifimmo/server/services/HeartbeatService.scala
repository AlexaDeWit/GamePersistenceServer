package io.bunkitty.scifimmo.server.services

import cats.effect.{Effect, IO}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

case class HeartbeatService() extends Http4sDsl[IO] {

  def route(): HttpService[IO] = HttpService {
    case GET -> Root / "heartbeat" => {
      Ok()
    }
  }

}
