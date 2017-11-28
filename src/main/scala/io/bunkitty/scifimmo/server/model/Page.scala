package construction.universe.server.model

import java.util.UUID

import construction.universe.server.dto.request.pages.PageRequest
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Page(id: Option[Long], fkUserId: Long, fkProjectId: Long, fkPageTextId: Long, name: String, uUID: String, description: Option[String]) {

}

class Pages(tag: Tag) extends Table[Page](tag, "projects") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def fkUserId = column[Long]("FK_USER_ID")
  def fkProjectId = column[Long]("FK_PROJECT_ID")
  def fkPageTextId = column[Long]("FK_PAGE_TEXT_ID")
  def name = column[String]("NAME")
  def uUID = column[String]("UUID")
  def description = column[Option[String]]("DESCRIPTION")
  def * = (id.?, fkUserId, fkProjectId, fkPageTextId, name, uUID, description) <> (Page.tupled, Page.unapply)
}

object Pages {
  def apply(fkUserId: Long, fkProjectId: Long, fkPageTextId: Long, pageCreateRequest: PageRequest): Page =
    Page(None, fkUserId, fkProjectId, fkPageTextId, pageCreateRequest.name, UUID.randomUUID().toString, pageCreateRequest.description)
}
