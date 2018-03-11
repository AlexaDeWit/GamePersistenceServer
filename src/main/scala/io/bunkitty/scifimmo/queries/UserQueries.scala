package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.User

object UserQueries {

  def findUser(id: String): ConnectionIO[Option[User]] = findUserQuery(id).option

  def findUserQuery(id: String): Query0[User] =
    sql"""SELECT *
          FROM "USERS"
          WHERE "ID" = $id;""".query[User]

  def findUserByEmail(email: String) =
    findUserByEmailQuery(email).option

  def findUserByEmailQuery(email: String): Query0[User] =
    sql"""SELECT *
          FROM "USERS"
          WHERE "EMAIL" = $email
          LIMIT 1""".query[User]

  def insertUserQuery(toInsert: User): Query0[String] =
    sql"""INSERT into "USERS"
          (
            "ID",
            "EMAIL",
            "PASSWORD_DIGEST",
            "USERNAME",
            "VALIDATED_EMAIL"
          ) values (
            ${toInsert.id},
            ${toInsert.email},
            ${toInsert.password},
            ${toInsert.username},
            ${toInsert.validatedEmail}
          ) returning "ID"""".query[String]

  def insertUser(toInsert: User): ConnectionIO[String] =
    insertUserQuery(toInsert).unique
}
