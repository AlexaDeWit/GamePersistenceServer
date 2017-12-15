package io.bunkitty.scifimmo.server.services

import org.http4s._
import org.http4s.dsl.io._
import cats.effect.{Effect, IO}
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.server.model._
import io.bunkitty.scifimmo.server.codecs.Characters._
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest._
import io.bunkitty.scifimmo.server.typeclasses._
import io.bunkitty.scifimmo.server.typeclasses.instances._
import io.bunkitty.scifimmo.throwables.OptionWithoutContentsException
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
        response <- Ok(chars.toList)
      } yield response
    }
    case request @ POST -> Root / "create" as user => {
      request.req.decode[CreateCharacterRequest] { character =>
        for {
          userId <- user.id.map(id => IO(id)).getOrElse(IO.raiseError(OptionWithoutContentsException("Could not retrieve user ID.")))
          characterToInsert = Character(None, userId, character.name, character.species)
          created <- DbIdentifiable[Character, Characters].insertQuery(characterToInsert)
          response <- Ok(created)
        } yield response
      }
    }
  }
}
