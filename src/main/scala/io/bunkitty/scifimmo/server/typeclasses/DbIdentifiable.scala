package io.bunkitty.scifimmo.server.typeclasses

import cats.effect.IO
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.model.{AccessToken, AccessTokens, Character, Characters}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

trait DbIdentifiable[A, B <: Table[A]] {
  def table: TableQuery[B]
  def getDbId(b: B): Rep[Long]
  def withItemId(item: A, id: Long): A

  def insertQuery(item: A)(implicit db: Database, ec: ExecutionContext, dbIdentifiable: DbIdentifiable[A, B]): IO[A] = {
    val query = dbIdentifiable.table returning dbIdentifiable.table.map(dbIdentifiable.getDbId) += item
    for {
      result <- db.runIO[Long](query)
      mapped = dbIdentifiable.withItemId(item, result)
    } yield mapped
  }
}

object DbIdentifiable {
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

  def insertQuery[A, B <: Table[A]](item: A)(implicit instance: DbIdentifiable[A,B], db: Database, executionContext: ExecutionContext): IO[A] = instance.insertQuery(item)

}
