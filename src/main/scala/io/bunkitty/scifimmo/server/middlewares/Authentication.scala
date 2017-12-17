package io.bunkitty.scifimmo.server.middlewares

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.data._
import cats.effect._
import cats.implicits._
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.server.model._
import io.bunkitty.scifimmo.throwables.InvalidAuthTokenException
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class Authentication(db: Database) extends Http4sDsl[IO] {

  def retrieveUser: Kleisli[IO, BasicToken, User] = Kleisli(tokenString => {
    val now = Timestamp.valueOf(LocalDateTime.now())
    val userFetchQuery = for {
      tokens <- TableQuery[AccessTokens] if tokens.token === tokenString.token && tokens.expiry > now
      users <- TableQuery[Users] if tokens.fkUserId === users.id
    } yield users
    for {
      user <- db.runIO[User](userFetchQuery.take(1).result.head)
    } yield user
  })

  val authUser: Kleisli[IO, Request[IO], Either[String, User]] = Kleisli({ request =>
    val message = for {
      header <- request.headers.get(Authorization).toRight("Couldn't find an Authorization header")
      message <- BasicToken.extract(header.value)
    } yield message
    message.traverse(retrieveUser.run)
  })

  val onFailure: AuthedService[String, IO] = Kleisli((req: AuthedRequest[IO, String]) => OptionT.liftF(Forbidden(req.authInfo)))
  val authMiddleware = AuthMiddleware(authUser, onFailure)
}
