package io.bunkitty.scifimmo.server.dto.requests.characters

import cats.effect._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._

@JsonCodec case class CreateCharacterRequest(name: String, species: String)

object LoginRequest {
  implicit lazy val loginRequestJsonDecoder: EntityDecoder[IO, CreateCharacterRequest] = jsonOf
}