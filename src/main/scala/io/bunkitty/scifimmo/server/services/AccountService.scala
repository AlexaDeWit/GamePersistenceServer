package construction.universe.server.services

import cats.effect.{Effect, IO}
import construction.universe.argon2.ArgonScala
import construction.universe.server.dto.request.accounts.RegistrationRequest
import construction.universe.server.dto.request.accounts.RegistrationRequest._
import construction.universe.server.dto.response.sessions.AccessTokenDto
import construction.universe.server.dto.response.sessions.AccessTokenDto._
import construction.universe.server.model._
import construction.universe.db.DbUtil._
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class AccountService(db: Database) extends Http4sDsl[IO]  {

  def route(): HttpService[IO] = HttpService {
    case request @ POST -> Root / "register" => {
      request.decode[RegistrationRequest] { registrationRequest =>
        val pass = ArgonScala.hashPassword(registrationRequest.rawPassword)
        val userToMake = User(None, registrationRequest.email, pass, registrationRequest.displayName)
        val users = TableQuery[Users]
        val tokens = TableQuery[AccessTokens]
        val userQuery = (users returning users.map(_.id)) += userToMake
        for {
          user <- db.runIO(userQuery)
          tokenQuery = (tokens returning tokens.map(_.id)) += AccessTokens.generateFor(user)
          tokenId <- db.runIO(tokenQuery)
          token <- db.runIO[AccessToken](tokens.filter(_.id === tokenId).result.head) //TODO: Order by date
          response <- Ok(AccessTokenDto.fromTokenDao(token))
        } yield response
      }
    }
  }

}
