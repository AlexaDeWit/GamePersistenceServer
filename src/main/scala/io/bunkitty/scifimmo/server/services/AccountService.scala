package io.bunkitty.scifimmo.server.services

import cats.effect.{Effect, IO}
import io.bunkitty.scifimmo.argon2.ArgonScala
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest._
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto._
import io.bunkitty.scifimmo.server.model._
import io.bunkitty.scifimmo.db.DbUtil._
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
