package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.Character

object CharacterQueries {

  def findCharacterQuery(id: String): Query0[Character] =
    sql"""SELECT
             "ID", "FK_USER_ID",
             "NAME","SPECIES_NAME",
             "LOCATION_X","LOCATION_Y","LOCATION_Z",
             "CURRENT_HEALTH","CURRENT_TOUGHNESS","CURRENT_CONSTITUTION",
             "CURRENT_STAMINA","CURRENT_ENDURANCE","CURRENT_QUICKNESS",
             "CURRENT_FOCUS","CURRENT_CLARITY", "CURRENT_WILLPOWER"
         FROM "CHARACTERS"
         WHERE "ID" = $id""".query[Character]

  def findCharacter(id: String): ConnectionIO[Option[Character]] = findCharacterQuery(id).option

  def deleteCharacterQuery(id: String): Query0[String] =
    sql"""DELETE
          FROM "CHARACTERS"
          WHERE "ID" = $id
          RETURNING "ID"""".query[String]

  def deleteCharacter(id: String): ConnectionIO[String] = deleteCharacterQuery(id).unique

  def findCharactersForUserQuery(userId: String): Query0[Character] =
    sql"""SELECT
            "ID", "FK_USER_ID",
            "NAME","SPECIES_NAME",
            "LOCATION_X","LOCATION_Y","LOCATION_Z",
            "CURRENT_HEALTH","CURRENT_TOUGHNESS","CURRENT_CONSTITUTION",
            "CURRENT_STAMINA","CURRENT_ENDURANCE","CURRENT_QUICKNESS",
            "CURRENT_FOCUS","CURRENT_CLARITY", "CURRENT_WILLPOWER"
        FROM "CHARACTERS"
        WHERE "FK_USER_ID" = $userId""".query[Character]

  def findCharactersForUser(userId: String): ConnectionIO[List[Character]] = findCharactersForUserQuery(userId).to[List]

  def insertCharacterQuery(character: Character): Query0[String] =
    sql"""
          INSERT INTO "CHARACTERS"
          (
              "ID",
              "FK_USER_ID",
              "NAME","SPECIES_NAME",
              "LOCATION_X","LOCATION_Y","LOCATION_Z",
              "CURRENT_HEALTH","CURRENT_TOUGHNESS","CURRENT_CONSTITUTION",
              "CURRENT_STAMINA","CURRENT_ENDURANCE","CURRENT_QUICKNESS",
              "CURRENT_FOCUS","CURRENT_CLARITY", "CURRENT_WILLPOWER"
          )
          VALUES
          (
              ${character.id},
              ${character.fkUserId},
              ${character.name}, ${character.speciesName},
              ${character.locationX}, ${character.locationY}, ${character.locationZ},
              ${character.currentHealth}, ${character.currentToughness}, ${character.currentConstitution},
              ${character.currentStamina}, ${character.currentEndurance}, ${character.currentQuickness},
              ${character.currentFocus}, ${character.currentClarity}, ${character.currentWillpower}
          )
          RETURNING "ID"
      """.query[String]

  def insertCharacter(character: Character): ConnectionIO[String] =
    insertCharacterQuery(character).unique

  def updateCharacterQuery(character: Character): Query0[String] =
    sql"""
          UPDATE "CHARACTERS" SET
          (
              "NAME","SPECIES_NAME",
              "LOCATION_X","LOCATION_Y","LOCATION_Z",
              "CURRENT_HEALTH","CURRENT_TOUGHNESS","CURRENT_CONSTITUTION",
              "CURRENT_STAMINA","CURRENT_ENDURANCE","CURRENT_QUICKNESS",
              "CURRENT_FOCUS","CURRENT_CLARITY", "CURRENT_WILLPOWER"
          )
          VALUES
          (
              ${character.name}, ${character.speciesName},
              ${character.locationX}, ${character.locationY}, ${character.locationZ},
              ${character.currentHealth}, ${character.currentToughness}, ${character.currentConstitution},
              ${character.currentStamina}, ${character.currentEndurance}, ${character.currentQuickness},
              ${character.currentFocus}, ${character.currentClarity}, ${character.currentWillpower}
          )
          WHERE "ID" = ${character.id}
          RETURNING "ID"
      """.query[String]

  def updateCharacter(character: Character): ConnectionIO[String] =
    updateCharacterQuery(character).unique


}
