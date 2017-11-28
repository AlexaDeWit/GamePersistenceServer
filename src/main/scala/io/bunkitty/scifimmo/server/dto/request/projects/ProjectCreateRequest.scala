package construction.universe.server.dto.request.projects

import cats.effect._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._


@JsonCodec case class ProjectCreateRequest(name: String, description: Option[String]) {
}

object ProjectCreateRequest {
  implicit lazy val projectCreateReqJsonDecoder: EntityDecoder[IO, ProjectCreateRequest] = jsonOf
}
