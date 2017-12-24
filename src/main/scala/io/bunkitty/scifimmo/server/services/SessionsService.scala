package io.bunkitty.scifimmo.server.services

import cats.effect.{Effect, IO}
import io.bunkitty.scifimmo.argon2.{ArgonScala, HashedPassword}
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.server.dto.request.accounts.LoginRequest
import io.bunkitty.scifimmo.server.dto.response.sessions.AccessTokenDto
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.server.typeclasses._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class SessionsService(db: Database) extends Http4sDsl[IO] {

  private implicit lazy val databaseInstance: Database = db

  lazy val compiledUserQuery = Compiled(rawUserQuery _)

  private def rawUserQuery(email: Rep[String]) = {
    val baseQuery = for {
      users <- TableQuery[Users] if users.email === email
    } yield users
    baseQuery.take(1)
  }

  def route(): HttpService[IO] = HttpService {
    case request @ POST -> Root / "login" => {
      request.decode[LoginRequest] { loginRequest =>
        val tokens = TableQuery[AccessTokens]
        for {
          user <- db.runIO[User](compiledUserQuery(loginRequest.email).result.head)
          passwordValidation <- IO(user.password.verify(loginRequest.rawPassword))
          response <- if(passwordValidation) {
            user.id match {
              case Some(id) => {
                val tokenToInsert = AccessTokens.generateFor(id)
                val resultantToken = DbIdentifiable.insertQuery[AccessToken, AccessTokens](tokenToInsert)
                resultantToken.flatMap(token => Ok(AccessTokenDto.fromTokenDao(token)))
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