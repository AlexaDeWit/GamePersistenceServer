package io.bunkitty.scifimmo.server.services

import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.argon2.ArgonScala
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.queries.{AccessTokenQueries, UserQueries}
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest._
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto._
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

case class AccountService(transactor: Transactor[IO]) extends Http4sDsl[IO]  {

  def route(): HttpService[IO] = HttpService {
    case request @ POST -> Root / "register" => {
      request.decode[RegistrationRequest] { registrationRequest =>
        val pass = ArgonScala.hashPassword(registrationRequest.rawPassword)
        val userToMake = User(None, registrationRequest.email, pass, registrationRequest.username)
        for {
          user <- UserQueries.insertUser(userToMake).transact(transactor)
          tokenToInsert =  AccessTokens.generateFor(user)
          resultantTokenId <- AccessTokenQueries.insertAccessToken(tokenToInsert).transact(transactor)
          response <- Ok(AccessTokenDto.fromTokenDao(tokenToInsert.copy(id = Some(resultantTokenId))))
        } yield response
      }
    }
  }

}
