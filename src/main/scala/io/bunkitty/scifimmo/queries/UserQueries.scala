package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._

import io.bunkitty.scifimmo.model.User

object UserQueries {

  def find(id: Long): ConnectionIO[Option[User]] =
    sql"SELECT ID, EMAIL, PASSWORD_DIGEST, USERNAME, VALIDATED_EMAIL FROM USERS WHERE ID = $id".query[User].option
}
