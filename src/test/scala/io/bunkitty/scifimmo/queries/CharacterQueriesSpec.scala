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
    check(CharacterQueries.findCharacterQuery("sdfasd"))
  }

  test("characters from user id"){
    check(CharacterQueries.findCharactersForUserQuery("asdfsd"))
  }

  test("insert character to characters table"){
    check(CharacterQueries.insertCharacterQuery(Character("sdfasdf", "sdfasd", "Dog", "Cat")))
  }

  test("update character already present in characters table"){
    check(CharacterQueries.insertCharacterQuery(Character("Stsdfsdfs", "dsfasdfs", "Dog", "Cat")))
    check(CharacterQueries.insertCharacterQuery(Character("sdfasdkl", "sdfasdfs", "Dog", "Cat")))
  }

}
