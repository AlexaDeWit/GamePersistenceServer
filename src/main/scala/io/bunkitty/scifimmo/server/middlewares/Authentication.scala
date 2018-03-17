package io.bunkitty.scifimmo.server.middlewares

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.data._
import cats.effect._
import doobie._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.server.codecs.JwtPayloads
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.server.dto.{UserContextJwt, UserInfo}
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware

case class Authentication(transactor: Transactor[IO], private val jwtService: JwtService) extends Http4sDsl[IO] {

  val authUser: Kleisli[IO, Request[IO], Either[String, UserInfo]] = Kleisli({ request =>
    val header = request.headers.get(Authorization).toRight("Couldn't find an Authorization header")
    val message = header.fold(s => Left(s), h => JsonWebToken.extract(h.value))
    val payload =  message.fold(
    s => IO(Left(s)),
    token => jwtService.decode[UserContextJwt](token.token, JwtPayloads.jwtUserContextPayloadDecoder)
            .map[Either[String, UserContextJwt]](Right.apply)
    )
    payload.map{
      case Left(err) => Left(err)
      case Right(contents) if contents.expiry.after(Timestamp.valueOf(LocalDateTime.now())) => Right(contents.userData)
      case _ => Left("JWT was expired.")
    }
  })

  val onFailure: AuthedService[String, IO] = Kleisli((req: AuthedRequest[IO, String]) => OptionT.liftF(Forbidden(req.authInfo)))
  val authMiddleware: AuthMiddleware[IO, UserInfo] = AuthMiddleware(authUser, onFailure)
}
