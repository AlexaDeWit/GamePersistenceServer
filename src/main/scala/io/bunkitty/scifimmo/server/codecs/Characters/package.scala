package io.bunkitty.scifimmo.server.codecs

import cats.effect._
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.circe._
import org.http4s.dsl.io._
import io.bunkitty.scifimmo.server.model.Character
import org.http4s.EntityEncoder

package object Characters {

  implicit lazy val characterEncoder: Encoder[Character] = deriveEncoder[Character]
  implicit lazy val characterDecoder: Decoder[Character] = deriveDecoder[Character]

  implicit lazy val characterSeqEncoder: Encoder[List[Character]] = deriveEncoder[List[Character]]
  implicit lazy val characterSeqDecoder: Decoder[List[Character]] = deriveDecoder[List[Character]]

  implicit lazy val characterSeqJsonEntity: EntityEncoder[IO, List[Character]] = jsonEncoderOf[IO, List[Character]]
}
