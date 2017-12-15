package io.bunkitty.scifimmo.server.typeclasses

import cats.effect.IO
import io.bunkitty.scifimmo.db.DbUtil._
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
  
}
