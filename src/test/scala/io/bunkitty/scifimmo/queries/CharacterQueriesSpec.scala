package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import doobie.scalatest._
import org.scalatest._
import io.bunkitty.scifimmo.model.Character
import io.bunkitty.scifimmo.common.TestResources

class CharacterQueriesSpec extends FunSuite with Matchers with IOChecker {

  val transactor = TestResources.postgresTransactor

  test("character from id") {
    check(CharacterQueries.findCharacterQuery(1))
  }

  test("characters from user id"){
    check(CharacterQueries.findCharactersForUserQuery(1))
  }

  test("insert character to characters table"){
    check(CharacterQueries.insertCharacterQuery(Character(None, 1, "Dog", "Cat")))
  }

  test("update character already present in characters table"){
    check(CharacterQueries.insertCharacterQuery(Character(None, 1, "Dog", "Cat")))
    check(CharacterQueries.insertCharacterQuery(Character(Some(1), 1, "Dog", "Cat")))
  }

}
