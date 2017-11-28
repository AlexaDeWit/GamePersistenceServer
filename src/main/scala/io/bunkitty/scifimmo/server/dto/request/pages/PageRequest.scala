package construction.universe.server.dto.request.pages

import cats.effect._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._

@JsonCodec case class PageRequest(name: String, description: Option[String], markdownText: String) {
}

object PageRequest {
  implicit lazy val pageCreateRequestJsonDecoder: EntityDecoder[IO, PageRequest] = jsonOf
}


