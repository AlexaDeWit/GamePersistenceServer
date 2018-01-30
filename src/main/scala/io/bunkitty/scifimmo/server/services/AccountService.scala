package io.bunkitty.scifimmo.server.services

import cats.effect.{Effect, IO}
import doobie._, doobie.implicits._
import io.bunkitty.scifimmo.argon2.ArgonScala
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest._
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.queries.UserQueries
import io.bunkitty.scifimmo.server.typeclasses._
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class AccountService(db: Database, transactor: Transactor[IO]) extends Http4sDsl[IO]  {

  implicit lazy val databaseInstance: Database = db

  def route(): HttpService[IO] = HttpService {
    case request @ POST -> Root / "register" => {
      request.decode[RegistrationRequest] { registrationRequest =>
        val pass = ArgonScala.hashPassword(registrationRequest.rawPassword)
        val userToMake = User(None, registrationRequest.email, pass, registrationRequest.username)
        for {
          user <- UserQueries.insertUser(userToMake).transact(transactor)
          tokenToInsert =  AccessTokens.generateFor(user)
          resultantToken <- DbIdentifiable.insertQuery[AccessToken, AccessTokens](tokenToInsert)
          response <- Ok(AccessTokenDto.fromTokenDao(resultantToken))
        } yield response
      }
    }
  }

}
