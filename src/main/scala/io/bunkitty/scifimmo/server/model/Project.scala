package construction.universe.server.model

import java.util.UUID

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Project(id: Option[Long], fkUserId: Long, fkDefaultPageId: Option[Long],  name: String, uUID: String, description: Option[String]) {

}

class Projects(tag: Tag) extends Table[Project](tag, "projects") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def fkUserId = column[Long]("FK_USER_ID")
  def fkDefaultPageId = column[Option[Long]]("FK_DEFAULT_PAGE_ID")
  def name = column[String]("NAME")
  def uUID = column[String]("UUID")
  def description = column[Option[String]]("DESCRIPTION")
  def * = (id.?, fkUserId, fkDefaultPageId, name, uUID, description) <> (Project.tupled, Project.unapply)
}

object Projects {
  def apply(userId: Long, name: String, description: Option[String] = None): Project = {
    Project(None, userId, None, name, UUID.randomUUID().toString, description)
  }
}
