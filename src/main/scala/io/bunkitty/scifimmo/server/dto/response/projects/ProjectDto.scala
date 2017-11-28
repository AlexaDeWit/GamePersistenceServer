package construction.universe.server.dto.response.projects

import cats.effect.IO
import construction.universe.server.model.Project
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s.EntityEncoder
import org.http4s.circe._

@JsonCodec case class ProjectDto(uUID: String, name: String, description: Option[String]) {

}

object ProjectDto {
  def apply(project: Project): ProjectDto = ProjectDto(project.uUID, project.name, project.description)
  implicit lazy val projectDtoJsonEncoder: EntityEncoder[IO, ProjectDto] = jsonEncoderOf
  implicit lazy val projectDtoSeqJsonEncoder: EntityEncoder[IO, Seq[ProjectDto]] = jsonEncoderOf
}
