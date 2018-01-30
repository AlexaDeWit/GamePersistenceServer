package io.bunkitty.scifimmo.queries

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import doobie._
import doobie.implicits._
import doobie.scalatest._
import io.bunkitty.scifimmo.common.TestResources
import io.bunkitty.scifimmo.model.AccessToken
import io.bunkitty.scifimmo.queries.AccessTokenQueries._
import org.scalatest._

class AccessTokenQueriesSpec extends FunSuite with Matchers with IOChecker {
  val transactor = TestResources.postgresTransactor

  test("Access Token Insertion Query") {
    val candidate = AccessToken(None, 1, Timestamp.valueOf(LocalDateTime.now().plusDays(7)), UUID.randomUUID().toString)
    check(insertAccessTokenQuery(candidate))
  }
}
