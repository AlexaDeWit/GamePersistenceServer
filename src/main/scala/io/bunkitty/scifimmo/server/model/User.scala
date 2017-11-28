package io.bunkitty.scifimmo.server.model

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import io.bunkitty.scifimmo.server.db.mappings.HashedPasswordMapping._
import io.bunkitty.scifimmo.argon2.HashedPassword

case class User(id: Option[Long], email: String, password: HashedPassword, displayName: String) {

}

class Users(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def email = column[String]("EMAIL")
  def password = column[HashedPassword]("PASSWORD")
  def displayName = column[String]("DISPLAY_NAME")
  def * = (id.?, email, password, displayName) <> (User.tupled, User.unapply)
}