package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import doobie.scalatest._
import org.scalatest._
import io.bunkitty.scifimmo.common.TestResources

class CharacterQueriesSpec extends FunSuite with Matchers with IOChecker {

  val transactor = TestResources.postgresTransactor

  test("character from id") {
    check(CharacterQueries.findCharacterQuery(1))
  }

  test("characters from user id"){
    check(CharacterQueries.findCharactersForUserQuery(1))
  }

}
