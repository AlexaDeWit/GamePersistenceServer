package io.bunkitty.scifimmo.server

import cats.effect.IO
import doobie.util.transactor.Transactor
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.server.ApplicationPrerequisites.HeaderAuthMiddleware
import io.bunkitty.scifimmo.model.User
import io.bunkitty.scifimmo.server.dto.UserInfo
import org.http4s.server.AuthMiddleware

case class ApplicationPrerequisites(authMiddleware: HeaderAuthMiddleware, transactor: Transactor[IO], hmacService: JwtService) {
}

object ApplicationPrerequisites {
  type HeaderAuthMiddleware = AuthMiddleware[IO, UserInfo]
}
