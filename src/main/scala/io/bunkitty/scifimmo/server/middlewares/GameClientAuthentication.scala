package io.bunkitty.scifimmo.server.middlewares

import cats.data._
import cats.effect._
import doobie._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.server.codecs.JwtPayloads
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.server.dto.GameClientClaims
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware

case class GameClientAuthentication(transactor: Transactor[IO], private val jwtService: JwtService) extends Http4sDsl[IO] {

  val authUser: Kleisli[IO, Request[IO], Either[String, GameClientClaims]] = Kleisli({ request =>
    val header = request.headers.get(Authorization).toRight("Couldn't find an Authorization header")
    val message = header.fold(s => Left(s), h => JsonWebToken.extract(h.value))
    message.fold(
      s => IO(Left(s)),
      token => jwtService.decode[GameClientClaims](token.token, JwtPayloads.gameClientClaimsDecoder)
        .map[Either[String, GameClientClaims]](Right.apply).map{
        case Left(err) => Left(err)
        case Right(claims) if claims.isAuthoritativeGameServer => Right(claims)
        case _ => Left("Insufficient authority to access this resource.")
      }
    )
  })

  val onFailure: AuthedService[String, IO] = Kleisli((req: AuthedRequest[IO, String]) => OptionT.liftF(Forbidden(req.authInfo)))
  val authMiddleware: AuthMiddleware[IO, GameClientClaims] = AuthMiddleware(authUser, onFailure)

}
