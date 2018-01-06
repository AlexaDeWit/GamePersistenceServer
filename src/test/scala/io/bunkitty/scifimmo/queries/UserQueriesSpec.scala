package io.bunkitty.scifimmo.queries

import java.sql.Timestamp
import java.time.LocalDateTime

import doobie._
import doobie.implicits._
import doobie.scalatest._
import io.bunkitty.scifimmo.common.TestResources
import io.bunkitty.scifimmo.model.User
import io.bunkitty.scifimmo.queries.UserQueries._
import org.scalatest._

class UserQueriesSpec extends FunSuite with Matchers with IOChecker {

  val transactor = TestResources.postgresTransactor

  test("user from id") {
    check(findUserQuery(1))
  }

  test("user from token string"){
    check(findUserFromTokenQuery("fdasdfas", Timestamp.valueOf(LocalDateTime.now())))
  }


}
