package io.bunkitty.scifimmo.queries

import java.sql.Timestamp

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.User

object UserQueries {

  def findUser(id: Long): ConnectionIO[Option[User]] = findUserQuery(id).option

  def findUserQuery(id: Long): Query0[User] =
    sql"""SELECT * FROM "USERS" WHERE "ID" = $id;""".query[User]

  def findUserFromToken(token: String, timeToCheckValidity: Timestamp) =
    sql"""SELECT U.ID, U.EMAIL, U.PASSWORD_DIGEST, U.USERNAME, U.VALIDATED_EMAIL FROM USERS U INNER JOIN SESSION_TOKENS T ON ( USERS.ID = SESSION_TOKENS.FK_USER_ID ) WHERE T.TOKEN = $token AND T.EXPIRATION > $timeToCheckValidity;""".query[User].option
}
