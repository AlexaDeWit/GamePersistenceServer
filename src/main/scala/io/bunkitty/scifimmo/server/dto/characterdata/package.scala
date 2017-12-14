package io.bunkitty.scifimmo.server.dto

import io.bunkitty.scifimmo.server.dto.characterdata.CharacterDto
import cats.effect._
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

package object characterdata {
  implicit lazy val characterDataSeqJsonEncoder:  EntityEncoder[IO, Seq[CharacterDto]] = jsonEncoderOf[IO, Seq[CharacterDto]]
}
