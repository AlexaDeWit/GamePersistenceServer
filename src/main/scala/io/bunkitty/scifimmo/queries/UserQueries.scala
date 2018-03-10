package io.bunkitty.scifimmo.queries

import java.sql.Timestamp

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.User

object UserQueries {

  def findUser(id: Long): ConnectionIO[Option[User]] = findUserQuery(id).option

  def findUserQuery(id: Long): Query0[User] =
    sql"""SELECT * FROM "USERS" WHERE "ID" = $id;""".query[User]

  def findUserByEmail(email: String) =
    findUserByEmailQuery(email).option

  def findUserByEmailQuery(email: String): Query0[User] =
    sql"""SELECT * FROM "USERS" WHERE "EMAIL" = $email LIMIT 1""".query[User]
  
  def insertUserQuery(toInsert: User): Query0[Long] =
    sql"""insert into "USERS" ("EMAIL", "PASSWORD_DIGEST", "USERNAME", "VALIDATED_EMAIL") values (${toInsert.email}, ${toInsert.password}, ${toInsert.username}, ${toInsert.validatedEmail}) returning "ID"""".query[Long]

  def insertUser(toInsert: User): ConnectionIO[Long] =
    insertUserQuery(toInsert).unique
}
