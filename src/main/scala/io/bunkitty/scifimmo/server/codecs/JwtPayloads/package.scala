package io.bunkitty.scifimmo.server.codecs

import io.bunkitty.scifimmo.server.codecs.UsersDto._
import io.bunkitty.scifimmo.server.codecs.TimeCodecs._
import io.bunkitty.scifimmo.server.dto.{CharacterContextJwt, UserContextJwt}
import io.circe._
import io.circe.generic.semiauto._

package object JwtPayloads {

  implicit lazy val jwtUserContextPayloadDecoder: Decoder[UserContextJwt] = deriveDecoder
  implicit lazy val jwtUserContextPayloadEncoder: Encoder[UserContextJwt] = deriveEncoder

  implicit lazy val jwtCharacterContextPayloadDecoder: Decoder[CharacterContextJwt] = deriveDecoder
  implicit lazy val jwtCharacterContextPayloadEncoder: Encoder[CharacterContextJwt] = deriveEncoder

}
