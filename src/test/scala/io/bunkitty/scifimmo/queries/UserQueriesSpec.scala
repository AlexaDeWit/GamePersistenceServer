package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import doobie.scalatest._
import io.bunkitty.scifimmo.common.TestResources
import io.bunkitty.scifimmo.model.User
import io.bunkitty.scifimmo.queries.UserQueries._
import org.scalatest._

class UserQueriesSpec extends FunSuite with Matchers with IOChecker {

  val transactor = TestResources.postgresTransactor
  val yolo = transactor.yolo
  import yolo._

  test("user from tokens") {
    check(findUserQuery(1))
  }

}
