package io.bunkitty.scifimmo.server.model

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import io.bunkitty.scifimmo.server.codecs.Characters
import io.bunkitty.scifimmo.server.db.mappings.HashedPasswordMapping._
import io.bunkitty.scifimmo.argon2.HashedPassword

case class User(id: Option[Long], email: String, password: HashedPassword, username: String, validatedEmail: Boolean = false)

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def email = column[String]("EMAIL")
  def password = column[HashedPassword]("PASSWORD_DIGEST")
  def username = column[String]("USERNAME")
  def validatedEmail = column[Boolean]("VALIDATED_EMAIL")
  def * = (id.?, email, password, username, validatedEmail) <> (User.tupled, User.unapply)
}