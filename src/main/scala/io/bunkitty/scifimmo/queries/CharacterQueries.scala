package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.Character

object CharacterQueries {

  def findCharacterQuery(id: Long): Query0[Character] =
    sql"""SELECT
             "ID", "FK_USER_ID",
             "NAME","SPECIES_NAME",
             "LOCATION_X","LOCATION_Y","LOCATION_Z",
             "CURRENT_HEALTH","CURRENT_TOUGHNESS","CURRENT_CONSTITUTION",
             "CURRENT_STAMINA","CURRENT_ENDURANCE","CURRENT_QUICKNESS",
             "CURRENT_FOCUS","CURRENT_CLARITY", "CURRENT_WILLPOWER"
         FROM "CHARACTERS"
         WHERE "ID" = $id""".query[Character]

  def findCharacter(id: Long): ConnectionIO[Option[Character]] = findCharacterQuery(id).option

  def deleteCharacterQuery(id: Long): Query0[Long] =
    sql"""DELETE
          FROM "CHARACTERS"
          WHERE "ID" = $id
          RETURNING "ID"""".query[Long]

  def deleteCharacter(id: Long): ConnectionIO[Long] = deleteCharacterQuery(id).unique

  def findCharactersForUserQuery(userId: Long): Query0[Character] =
    sql"""SELECT
            "ID", "FK_USER_ID",
            "NAME","SPECIES_NAME",
            "LOCATION_X","LOCATION_Y","LOCATION_Z",
            "CURRENT_HEALTH","CURRENT_TOUGHNESS","CURRENT_CONSTITUTION",
            "CURRENT_STAMINA","CURRENT_ENDURANCE","CURRENT_QUICKNESS",
            "CURRENT_FOCUS","CURRENT_CLARITY", "CURRENT_WILLPOWER"
        FROM "CHARACTERS"
        WHERE "FK_USER_ID" = $userId""".query[Character]

  def findCharactersForUser(userId: Long): ConnectionIO[List[Character]] = findCharactersForUserQuery(userId).to[List]

  def insertCharacterQuery(character: Character): Query0[Long] =
    sql"""
          INSERT INTO "CHARACTERS"
          (
              "FK_USER_ID",
              "NAME","SPECIES_NAME",
              "LOCATION_X","LOCATION_Y","LOCATION_Z",
              "CURRENT_HEALTH","CURRENT_TOUGHNESS","CURRENT_CONSTITUTION",
              "CURRENT_STAMINA","CURRENT_ENDURANCE","CURRENT_QUICKNESS",
              "CURRENT_FOCUS","CURRENT_CLARITY", "CURRENT_WILLPOWER"
          )
          VALUES
          (
              ${character.fkUserId},
              ${character.name}, ${character.speciesName},
              ${character.locationX}, ${character.locationY}, ${character.locationZ},
              ${character.currentHealth}, ${character.currentToughness}, ${character.currentConstitution},
              ${character.currentStamina}, ${character.currentEndurance}, ${character.currentQuickness},
              ${character.currentFocus}, ${character.currentClarity}, ${character.currentWillpower}
          )
          RETURNING "ID"
      """.query[Long]

  def insertCharacter(character: Character): ConnectionIO[Long] =
    insertCharacterQuery(character).unique

  def updateCharacterQuery(character: Character): Query0[Long] =
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
      """.query[Long]

  def updateCharacter(character: Character): ConnectionIO[Long] =
    updateCharacterQuery(character).unique

  def insertOrUpdateCharacter(character: Character): ConnectionIO[Long] = {
    character.id.map(n => updateCharacterQuery(character)).getOrElse(insertCharacterQuery(character)).unique
  }

}
