package construction.universe.server.model

import construction.universe.server.dto.request.pages.PageRequest
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class PageText(id: Option[Long], textMarkdown: String) {

}

class PageTexts(tag: Tag) extends Table[PageText](tag, "page_texts") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def textMarkdown = column[String]("TEXT_MARKDOWN")
  def * = (id.?, textMarkdown) <> (PageText.tupled, PageText.unapply)
}

object PageTexts {
  def apply(pageCreateRequest: PageRequest): PageText = PageText(None, pageCreateRequest.markdownText)
}
