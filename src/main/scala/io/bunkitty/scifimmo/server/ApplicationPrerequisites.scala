package io.bunkitty.scifimmo.server

import cats.effect.IO
import doobie.util.transactor.Transactor
import io.bunkitty.scifimmo.server.ApplicationPrerequisites.HeaderAuthMiddleware
import io.bunkitty.scifimmo.model.User
import org.http4s.server.AuthMiddleware
import slick.jdbc.PostgresProfile.api._



case class ApplicationPrerequisites(db: Database, authMiddleware: HeaderAuthMiddleware, transactor: Transactor[IO]) {
}

object ApplicationPrerequisites {
  type HeaderAuthMiddleware = AuthMiddleware[IO, User]
}
