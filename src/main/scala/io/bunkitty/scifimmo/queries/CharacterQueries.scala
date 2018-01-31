package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.{Character, User}

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

  def findCharactersForUser(userId: Long): ConnectionIO[List[Character]] = findCharacterQuery(userId).list


}
