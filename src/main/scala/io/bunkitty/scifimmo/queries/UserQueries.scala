package io.bunkitty.scifimmo.queries

import java.sql.Timestamp

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.User

object UserQueries {

  def findUser(id: Long): ConnectionIO[Option[User]] = findUserQuery(id).option

  def findUserQuery(id: Long): Query0[User] =
    sql"""SELECT * FROM "USERS" WHERE "ID" = $id;""".query[User]

  def findUserFromToken(token: String, timeToCheckValidity: Timestamp): ConnectionIO[Option[User]] = findUserFromTokenQuery(token, timeToCheckValidity).option

  def findUserFromTokenQuery(token: String, timeToCheckValidity: Timestamp): Query0[User] =
    sql"""SELECT U."ID", U."EMAIL", U."PASSWORD_DIGEST", U."USERNAME", U."VALIDATED_EMAIL" FROM "USERS" U INNER JOIN "SESSION_TOKENS" T ON ( U."ID" = T."FK_USER_ID" ) WHERE T."TOKEN" = $token AND T."EXPIRATION" > $timeToCheckValidity;""".query[User]

  def insertUserQuery(toInsert: User): Query0[Long] =
    sql"""insert into "USERS" ("EMAIL", "PASSWORD_DIGEST", "USERNAME", "VALIDATED_EMAIL") values (${toInsert.email}, ${toInsert.password}, ${toInsert.username}, ${toInsert.validatedEmail}) returning "ID"""".query[Long]

  def insertUser(toInsert: User): ConnectionIO[Long] =
    insertUserQuery(toInsert).unique
}
