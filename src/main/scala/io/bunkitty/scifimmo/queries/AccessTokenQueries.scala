package io.bunkitty.scifimmo.queries

import java.sql.Timestamp

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.AccessToken

object AccessTokenQueries {

  def insertAccessTokenQuery(token: AccessToken): Query0[Long] =
    sql"""insert into "SESSION_TOKENS" ("FK_USER_ID", "TOKEN", "EXPIRATION") values (${token.fkUserId}, ${token.token}, ${token.expiry}) returning "ID"""".query[Long]

  def insertAccessToken(token: AccessToken): ConnectionIO[Long] =
    insertAccessTokenQuery(token).unique
}
