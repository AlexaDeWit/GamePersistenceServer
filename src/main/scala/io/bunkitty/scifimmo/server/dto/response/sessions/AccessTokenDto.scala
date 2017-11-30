package io.bunkitty.scifimmo.server.dto.response.sessions

import java.sql.Timestamp
import io.bunkitty.scifimmo.server.codecs.TimeCodecs._ //Intellij lies, this is absolutely needed for the token codec.
import cats.effect.IO
import io.bunkitty.scifimmo.server.model.AccessToken
import io.bunkitty.scifimmo.server.codecs.rules._ //Actually needed
import io.circe.generic.extras._
import io.circe.generic.auto._
import org.http4s.EntityEncoder
import org.http4s.circe._

@ConfiguredJsonCodec case class AccessTokenDto(tokenString: String, expiry: Timestamp) {

}

object AccessTokenDto {
  def fromTokenDao(token: AccessToken) = {
    AccessTokenDto(token.token, token.expiry)
  }

  implicit lazy val accessTokenDtoEntityEncoder: EntityEncoder[IO, AccessTokenDto] = jsonEncoderOf
}
