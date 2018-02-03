package io.bunkitty.scifimmo.server.services

import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.io.IOUtil._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.queries.{AccessTokenQueries, UserQueries}
import io.bunkitty.scifimmo.server.dto.request.accounts.LoginRequest
import io.bunkitty.scifimmo.jwt.HmacSha256
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

case class SessionsService(transactor: Transactor[IO], private val hmacService: HmacSha256) extends Http4sDsl[IO] {

  def route(): HttpService[IO] = HttpService {
    case request @ POST -> Root / "login" => {
      request.decode[LoginRequest] { loginRequest =>
        for {
          userOpt <- UserQueries.findUserByEmail(loginRequest.email).transact(transactor)
          user <- userOpt.ioResult(s"Could not retrieve user with email ${loginRequest.email}")
          passwordValidation <- IO(user.password.verify(loginRequest.rawPassword))
          response <- if(passwordValidation) {
            user.id match {
              case Some(id) => {
                val tokenToInsert = AccessTokens.generateFor(id)
                val resultantTokenId =  AccessTokenQueries.insertAccessToken(tokenToInsert).transact(transactor)
                resultantTokenId.flatMap(tokenId => Ok(AccessTokenDto.fromTokenDao(tokenToInsert.copy(id = Some(tokenId)))))
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