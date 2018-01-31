package io.bunkitty.scifimmo.queries

import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model.Character

object CharacterQueries {

  def findCharacterQuery(id: Long): Query0[Character] =
    sql"""SELECT * FROM "CHARACTERS" WHERE "ID" = $id""".query[Character]

}
