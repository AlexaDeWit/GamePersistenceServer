package io.bunkitty.scifimmo.server.services

import org.http4s._
import org.http4s.dsl.io._
import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.queries.CharacterQueries
import io.bunkitty.scifimmo.server.codecs.Characters._
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest._
import io.bunkitty.scifimmo.server.typeclasses.DbIdentifiable
import io.bunkitty.scifimmo.server.typeclasses.DbIdentifiable._
import org.http4s.AuthedService
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class CharactersService(db: Database, transactor: Transactor[IO]) {

  private implicit lazy val dbInstance: Database = db

  def route(): AuthedService[User, IO] = AuthedService {
    case GET -> Root as user => {
      for {
        chars <- user.ioWithId(id => CharacterQueries.findCharactersForUser(id).transact(transactor))
        response <- Ok(chars)
      } yield response
    }
    case request @ POST -> Root / "create" as user => {
      request.req.decode[CreateCharacterRequest] { character =>
        for {
          userId <- user.ioWithId(id => IO(id))
          characterToInsert = Character(None, userId, character.name, character.species)
          created <- DbIdentifiable.insertQuery[Character, Characters](characterToInsert)
          response <- Ok(created)
        } yield response
      }
    }
  }
}
