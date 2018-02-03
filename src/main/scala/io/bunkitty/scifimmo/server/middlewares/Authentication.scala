package io.bunkitty.scifimmo.server.middlewares

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.data._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.jwt.HmacSha256
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.queries.UserQueries
import io.bunkitty.scifimmo.throwables.InvalidAuthTokenException
import io.bunkitty.scifimmo.jwt.HmacSha256
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware

case class Authentication(transactor: Transactor[IO], private val hmacService: HmacSha256) extends Http4sDsl[IO] {

  def retrieveUser: Kleisli[IO, BasicToken, Either[String, User]] = Kleisli(tokenString => {
    val now = Timestamp.valueOf(LocalDateTime.now())
    for {
      user <- UserQueries.findUserFromToken(tokenString.token, now).transact(transactor)
    } yield Either.fromOption(user, "No user was found for the provided auth token.")
  })

  val authUser: Kleisli[IO, Request[IO], Either[String, User]] = Kleisli({ request =>
    val message = for {
      header <- request.headers.get(Authorization).toRight("Couldn't find an Authorization header")
      message <- BasicToken.extract(header.value)
    } yield message
    message.fold(s => IO(Left(s)), t => retrieveUser(t))
  })

  val onFailure: AuthedService[String, IO] = Kleisli((req: AuthedRequest[IO, String]) => OptionT.liftF(Forbidden(req.authInfo)))
  val authMiddleware: AuthMiddleware[IO, User] = AuthMiddleware(authUser, onFailure)
}
