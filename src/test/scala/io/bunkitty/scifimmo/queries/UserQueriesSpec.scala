package io.bunkitty.scifimmo.queries

import doobie.scalatest._
import io.bunkitty.scifimmo.argon2.HashedPassword
import io.bunkitty.scifimmo.common.TestResources
import io.bunkitty.scifimmo.model.User
import io.bunkitty.scifimmo.queries.UserQueries._
import org.scalatest._

class UserQueriesSpec extends FunSuite with Matchers with IOChecker {

  val transactor = TestResources.postgresTransactor

  test("user from id") {
    check(findUserQuery("sdfasdjkfs"))
  }

  test("insert user query") {
    check(insertUserQuery(User("dfasdhfjs", "Bah", HashedPassword.unsafeWrapString("foo"), "Cats")))
  }

  test("user from email string"){
    check(findUserByEmailQuery("what"))
  }
  
}
