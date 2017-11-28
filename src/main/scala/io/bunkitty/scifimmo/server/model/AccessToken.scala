package io.bunkitty.scifimmo.server.model

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import io.bunkitty.scifimmo.argon2.HashedPassword
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class AccessToken(id: Option[Long], expiry: Timestamp, token: String, fkUserId: Long) {

}

class AccessTokens(tag: Tag) extends Table[AccessToken](tag, "access_tokens") {
  val UsersQuery = TableQuery[Users]

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def expiry = column[Timestamp]("EXPIRY")
  def token = column[String]("TOKEN")
  def fkUserId = column[Long]("FK_USER_ID")
  def user = foreignKey("FK_USER_ID", fkUserId, UsersQuery)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  def * = (id.?, expiry, token, fkUserId) <> (AccessToken.tupled, AccessToken.unapply)
}

object AccessTokens {
  def generateFor(userId: Long): AccessToken = {
    AccessToken(None, Timestamp.valueOf(LocalDateTime.now().plusDays(7)), UUID.randomUUID().toString, userId)
  }
}