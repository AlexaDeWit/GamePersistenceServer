package io.bunkitty.scifimmo.server.services

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.io.IOUtil._
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.queries.UserQueries
import io.bunkitty.scifimmo.server.dto.request.accounts.LoginRequest
import io.bunkitty.scifimmo.server.dto.{UserContextJwt, UserInfo}
import io.bunkitty.scifimmo.server.codecs.JwtPayloads._
import io.circe.syntax._
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

case class SessionsService(transactor: Transactor[IO], private val jwtService: JwtService) extends Http4sDsl[IO] {

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
                val jwt = UserContextJwt(UserInfo(id, user.email, user.username),Timestamp.valueOf(LocalDateTime.now().plusDays(7)))
                val token = jwtService.sign(jwt.asJson.toString)
                token.flatMap(t => Ok(t))
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