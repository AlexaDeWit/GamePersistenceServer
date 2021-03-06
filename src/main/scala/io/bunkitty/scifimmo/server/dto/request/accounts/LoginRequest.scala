package io.bunkitty.scifimmo.server.dto.request.accounts

import cats.effect._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._


@JsonCodec case class LoginRequest(email: String, rawPassword: String)
object LoginRequest {
  implicit lazy val loginRequestJsonDecoder: EntityDecoder[IO, LoginRequest] = jsonOf
}
