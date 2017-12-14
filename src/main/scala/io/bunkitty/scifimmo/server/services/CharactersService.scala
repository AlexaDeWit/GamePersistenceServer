package io.bunkitty.scifimmo.server.services

import org.http4s._
import org.http4s.dsl.io._
import cats.effect.{Effect, IO}
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.server.dto.characterdata._
import io.bunkitty.scifimmo.server.model._
import org.http4s.AuthedService
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class CharactersService(db: Database) {

  private implicit lazy val dbInstance: Database = db
  private lazy val characters = TableQuery[Characters]

  def route(): AuthedService[User, IO] = AuthedService {
    case GET -> Root as user => {
      for {
        chars <- db.runIO[Seq[Character]](characters.filter(_.fkUserId === user.id).result)
        response <- Ok(chars.map(_.toDto))
      } yield response
    }
  }


}
