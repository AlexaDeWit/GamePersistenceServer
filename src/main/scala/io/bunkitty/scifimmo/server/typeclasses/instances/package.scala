package io.bunkitty.scifimmo.server.typeclasses

import cats.effect.IO
import io.bunkitty.scifimmo.server.model.{AccessToken, AccessTokens, Characters, Character}
import slick.jdbc.PostgresProfile.api._
import io.bunkitty.scifimmo.server.typeclasses._


package object instances {
  implicit val accessTokenDbIdentifiable: DbIdentifiable[AccessToken, AccessTokens] =
    new DbIdentifiable[AccessToken, AccessTokens] {
      val table = TableQuery[AccessTokens]
      def getDbId(accessToken: AccessTokens): Rep[Long] = accessToken.id
      def withItemId(item: AccessToken, id: Long): AccessToken = item.copy(id = Option(id))
    }

  implicit val characterDbIdentifiable: DbIdentifiable[Character, Characters] =
    new DbIdentifiable[Character, Characters] {
      val table = TableQuery[Characters]
      def getDbId(characters: Characters): Rep[Long] = characters.id
      def withItemId(item: Character, id: Long): Character = item.copy(id = Option(id))
    }
}