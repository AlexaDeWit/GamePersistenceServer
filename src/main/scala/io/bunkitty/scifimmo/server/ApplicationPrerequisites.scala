package io.bunkitty.scifimmo.server

import cats.effect.IO
import doobie.util.transactor.Transactor
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.server.ApplicationPrerequisites.{GameClientAuthMiddleware, HeaderAuthMiddleware}
import io.bunkitty.scifimmo.server.dto.{GameClientClaims, UserInfo}
import org.http4s.server.AuthMiddleware

case class ApplicationPrerequisites(authMiddleware: HeaderAuthMiddleware, transactor: Transactor[IO], hmacService: JwtService, gameClientMiddleware: GameClientAuthMiddleware) {
}

object ApplicationPrerequisites {
  type HeaderAuthMiddleware = AuthMiddleware[IO, UserInfo]
  type GameClientAuthMiddleware = AuthMiddleware[IO, GameClientClaims]
}
