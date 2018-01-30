package io.bunkitty.scifimmo.model

import cats.effect.IO
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import io.bunkitty.scifimmo.server.db.mappings.HashedPasswordMapping._
import io.bunkitty.scifimmo.argon2.HashedPassword
import io.bunkitty.scifimmo.throwables.OptionWithoutContentsException

case class User(id: Option[Long], email: String, password: HashedPassword, username: String, validatedEmail: Boolean = false) {
  def ioWithId[A](f: Long => IO[A]): IO[A] = {
    id.map(f).getOrElse(IO.raiseError(OptionWithoutContentsException("Could not retrieve user ID.")))
  }
}

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def email = column[String]("EMAIL")
  def password = column[HashedPassword]("PASSWORD_DIGEST")
  def username = column[String]("USERNAME")
  def validatedEmail = column[Boolean]("VALIDATED_EMAIL")
  def * = (id.?, email, password, username, validatedEmail) <> (User.tupled, User.unapply)
}