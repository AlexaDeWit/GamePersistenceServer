package io.bunkitty.scifimmo.server.dto.request.accounts

import cats.effect._
import io.bunkitty.scifimmo.server.codecs.rules._ //Actually needed
import io.circe.generic.extras._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._

@ConfiguredJsonCodec case class RegistrationRequest(username: String, email: String, rawPassword: String) {

}

object RegistrationRequest {
  implicit lazy val registrationRequestJsonDecoder: EntityDecoder[IO, RegistrationRequest] = jsonOf
}
