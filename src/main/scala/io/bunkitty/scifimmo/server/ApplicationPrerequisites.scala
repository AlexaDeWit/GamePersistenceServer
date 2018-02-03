package io.bunkitty.scifimmo.server

import cats.effect.IO
import doobie.util.transactor.Transactor
import io.bunkitty.scifimmo.jwt.HmacSha256
import io.bunkitty.scifimmo.server.ApplicationPrerequisites.HeaderAuthMiddleware
import io.bunkitty.scifimmo.model.User
import org.http4s.server.AuthMiddleware

case class ApplicationPrerequisites(authMiddleware: HeaderAuthMiddleware, transactor: Transactor[IO], hmacService: HmacSha256) {
}

object ApplicationPrerequisites {
  type HeaderAuthMiddleware = AuthMiddleware[IO, User]
}
