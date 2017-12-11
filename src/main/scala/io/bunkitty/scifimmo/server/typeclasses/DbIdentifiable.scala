package io.bunkitty.scifimmo.server.typeclasses

import cats.effect.IO
import simulacrum.typeclass
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext

@typeclass trait DbIdentifiable[A] {
  def withId(item: A, id: Long ): A
  def insertQuery(item: A)(implicit db: Database, ec: ExecutionContext): IO[A]
}
