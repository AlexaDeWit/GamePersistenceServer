package construction.universe.server.middlewares

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.data._
import cats.effect._
import cats.implicits._
import construction.universe.db.DbUtil._
import construction.universe.server.model._
import construction.universe.throwables.InvalidAuthTokenException
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class Authentication(db: Database) extends Http4sDsl[IO] {

  def retrieveUser: Kleisli[IO, BearerToken, User] = Kleisli(tokenString => {
    val now = Timestamp.valueOf(LocalDateTime.now())
    val tokenQuery = TableQuery[AccessTokens].filter(_.token === tokenString.token).filter(_.expiry > now).take(1).result
    for {
      tokenOpt <- db.runIO[Option[AccessToken]](tokenQuery.headOption)
      token <- tokenOpt.map(o => IO(o)).getOrElse(IO.raiseError(throw InvalidAuthTokenException("Could not find a valid auth token")))
      userQuery = TableQuery[Users].filter(_.id === token.fkUserId).take(1).result
      user <- db.runIO[User](userQuery.head)
    } yield user
  })

  val authUser: Kleisli[IO, Request[IO], Either[String, User]] = Kleisli({ request =>
    val message = for {
      header <- request.headers.get(Authorization).toRight("Couldn't find an Authorization header")
      message <- BearerToken.extract(header.value)
    } yield message
    message.traverse(retrieveUser.run)
  })

  val onFailure: AuthedService[String, IO] = Kleisli((req: AuthedRequest[IO, String]) => OptionT.liftF(Forbidden(req.authInfo)))
  val authMiddleware = AuthMiddleware(authUser, onFailure)
}
