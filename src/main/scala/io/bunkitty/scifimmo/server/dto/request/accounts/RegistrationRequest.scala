package construction.universe.server.dto.request.accounts

import cats.effect._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._

@JsonCodec case class RegistrationRequest(displayName: String, email: String, rawPassword: String) {

}

object RegistrationRequest {
  implicit lazy val registrationRequestJsonDecoder: EntityDecoder[IO, RegistrationRequest] = jsonOf
}
