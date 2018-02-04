package io.bunkitty.scifimmo.server.codecs

import io.bunkitty.scifimmo.server.dto.UserInfo
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.circe._


package object UsersDto {
  implicit lazy val userDtoEncoder: Encoder[UserInfo] = deriveEncoder
  implicit lazy val userDtoDecoder: Decoder[UserInfo] = deriveDecoder
}
