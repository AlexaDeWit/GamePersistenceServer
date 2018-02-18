package io.bunkitty.scifimmo.server.services

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.argon2.ArgonScala
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.queries.UserQueries
import io.bunkitty.scifimmo.server.dto.{JwtPayload, UserInfo}
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest
import io.bunkitty.scifimmo.server.dto.request.accounts.RegistrationRequest._
import io.bunkitty.scifimmo.server.codecs.JwtPayloads._
import io.circe.syntax._
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

case class AccountService(transactor: Transactor[IO], private val jwtService: JwtService) extends Http4sDsl[IO]  {

  def route(): HttpService[IO] = HttpService {
    case request @ POST -> Root / "register" => {
      request.decode[RegistrationRequest] { registrationRequest =>
        val pass = ArgonScala.hashPassword(registrationRequest.rawPassword)
        val userToMake = User(None, registrationRequest.email, pass, registrationRequest.username)
        for {
          userId <- UserQueries.insertUser(userToMake).transact(transactor)
          jwt = JwtPayload(UserInfo(userId,userToMake.email, userToMake.username),Timestamp.valueOf(LocalDateTime.now().plusDays(7)))
          token <- jwtService.sign(jwt.asJson.toString)
          response <- Ok(token)
        } yield response
      }
    }
  }

}
