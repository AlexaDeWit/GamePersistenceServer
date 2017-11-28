package construction.universe.server.model

import construction.universe.argon2.HashedPassword
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import construction.universe.server.db.mappings.HashedPasswordMapping._

case class User(id: Option[Long], email: String, password: HashedPassword, displayName: String) {

}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def email = column[String]("EMAIL")
  def password = column[HashedPassword]("PASSWORD")
  def displayName = column[String]("DISPLAY_NAME")
  def * = (id.?, email, password, displayName) <> (User.tupled, User.unapply)
}