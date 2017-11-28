package construction.universe.server.dto.response.pages

import cats.effect.IO
import construction.universe.server.model.{Page, PageText}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import org.http4s.EntityEncoder
import org.http4s.circe._


@JsonCodec case class PageDto(name: String, uuid: String, description: Option[String], pageText: Option[String]) {

}

object PageDto {
  def apply(page: Page): PageDto = PageDto(page.name, page.uUID, page.description, None)
  def apply(page: Page, pageText: PageText): PageDto = PageDto(page.name, page.uUID, page.description, Option(pageText.textMarkdown))

  implicit lazy val pageDtoJsonEncoder: EntityEncoder[IO, PageDto] = jsonEncoderOf
  implicit lazy val pageDtoSeqJsonEncoder: EntityEncoder[IO, Seq[PageDto]] = jsonEncoderOf
}
