package construction.universe.server.dto.response.sessions

import java.sql.Timestamp

import cats.effect.IO
import construction.universe.server.model.AccessToken
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s.EntityEncoder
import org.http4s.circe._
import construction.universe.server.codecs.TimeCodecs._

@JsonCodec case class AccessTokenDto(tokenString: String, expiry: Timestamp) {

}

object AccessTokenDto {
  def fromTokenDao(token: AccessToken) = {
    AccessTokenDto(token.token, token.expiry)
  }

  implicit lazy val accessTokenDtoEntityEncoder: EntityEncoder[IO, AccessTokenDto] = jsonEncoderOf
}
