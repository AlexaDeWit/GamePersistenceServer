package io.bunkitty.scifimmo.server.codecs

import io.bunkitty.scifimmo.server.codecs.UsersDto._
import io.bunkitty.scifimmo.server.codecs.TimeCodecs._ //Intellij lies, this is absolutely needed for the token codec.
import io.bunkitty.scifimmo.server.dto.JwtPayload
import io.circe._
import io.circe.generic.semiauto._

package object JwtPayloads {

  implicit lazy val jwtPayloadDecoder: Decoder[JwtPayload] = deriveDecoder
  implicit lazy val jwtPayloadEncoder: Encoder[JwtPayload] = deriveEncoder

}
