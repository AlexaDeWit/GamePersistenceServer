package io.bunkitty.scifimmo.server.typeclasses

import cats.effect.IO
import io.bunkitty.scifimmo.server.model.{AccessToken, AccessTokens, Characters, Character}
import io.bunkitty.scifimmo.db.DbUtil._
import slick.jdbc.PostgresProfile.api._
import io.bunkitty.scifimmo.server.typeclasses._

import scala.concurrent.ExecutionContext

package object instances {
  implicit val accessTokenDbIdentifiable: DbIdentifiable[AccessToken] = new DbIdentifiable[AccessToken] {
    override def insertQuery(item: AccessToken)(implicit db: Database, ec: ExecutionContext): IO[AccessToken] = {
      val query = TableQuery[AccessTokens] returning TableQuery[AccessTokens].map(_.id) += item
      for {
        result <- db.runIO[Long](query)
        mapped = withId(item, result)
      } yield mapped
    }
    override def withId(item: AccessToken, id: Long): AccessToken = item.copy(id = Option(id))
  }

  implicit val characterDbIdentifiable: DbIdentifiable[Character] = new DbIdentifiable[Character] {
    override def insertQuery(item: Character)(implicit db: Database, ec: ExecutionContext): IO[Character] = {
      val query = TableQuery[Characters] returning TableQuery[Characters].map(_.id) += item
      for {
        result <- db.runIO[Long](query)
        mapped = withId(item, result)
      } yield mapped
    }
    override def withId(item: Character, id: Long): Character = item.copy(id = Option(id))
  }

}
