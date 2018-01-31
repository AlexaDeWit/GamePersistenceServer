package io.bunkitty.scifimmo.server.codecs

import cats.effect._
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.circe._
import org.http4s.dsl.io._
import io.bunkitty.scifimmo.model.Character
import org.http4s.EntityEncoder

package object Characters {

  implicit lazy val characterEncoder: Encoder[Character] = deriveEncoder[Character]
  implicit lazy val characterDecoder: Decoder[Character] = deriveDecoder[Character]

  implicit lazy val characterJsonEntity: EntityEncoder[IO, Character] = jsonEncoderOf[IO, Character]
  implicit lazy val characterListJsonEntity: EntityEncoder[IO, List[Character]] = jsonEncoderOf[IO, List[Character]]
}
