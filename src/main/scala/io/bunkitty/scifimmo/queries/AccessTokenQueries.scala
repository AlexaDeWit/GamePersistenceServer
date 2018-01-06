package io.bunkitty.scifimmo.queries

import java.sql.Timestamp

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.AccessToken

object AccessTokenQueries {

  def createAccessTokenQuery(token: AccessToken): Update0 =
    sql"""insert into "SESSION_TOKENS" ("FK_USER_ID", "TOKEN", "EXPIRATION") values (${token.fkUserId}, ${token.token}, ${token.expiry})""".update
}
