package construction.universe.server.services

import cats.effect.{Effect, IO}
import construction.universe.argon2.{ArgonScala, HashedPassword}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import construction.universe.db.DbUtil._
import construction.universe.server.dto.request.accounts.LoginRequest
import construction.universe.server.dto.response.sessions.AccessTokenDto
import construction.universe.server.model._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class SessionsService(db: Database) extends Http4sDsl[IO] {

  def route(): HttpService[IO] = HttpService {
    case request @ POST -> Root / "login" => {
      request.decode[LoginRequest] { loginRequest =>
        val users = TableQuery[Users]
        val tokens = TableQuery[AccessTokens]
        for {
          user <- db.runIO[User](users.filter(_.email === loginRequest.email).take(1).result.head)
          passwordValidation <- IO(user.password.verify(loginRequest.rawPassword))
          response <- if(passwordValidation) {
            user.id match {
              case Some(id) => {
                val tokenQuery = (tokens returning tokens.map(_.id))  += AccessTokens.generateFor(id)
                for {
                  tokenId <- db.runIO(tokenQuery)
                  token <- db.runIO[AccessToken](tokens.filter(_.id === tokenId).result.head)
                  response <- Ok(AccessTokenDto.fromTokenDao(token))
                } yield response
              }
              case _ => Forbidden()
            }
          } else {
            Forbidden()
          }
        } yield response
      }
    }
  }
}