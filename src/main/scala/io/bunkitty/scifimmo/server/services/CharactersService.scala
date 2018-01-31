package io.bunkitty.scifimmo.server.services

import org.http4s._
import org.http4s.dsl.io._
import cats.effect.IO
import io.bunkitty.scifimmo.db.DbUtil._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.server.codecs.Characters._
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest._
import io.bunkitty.scifimmo.server.typeclasses.DbIdentifiable
import io.bunkitty.scifimmo.server.typeclasses.DbIdentifiable._
import org.http4s.AuthedService
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

case class CharactersService(db: Database) {

  private implicit lazy val dbInstance: Database = db
  private lazy val characters = TableQuery[Characters]

  lazy val charsQuery = Compiled(rawCharsQuery _)
  private def rawCharsQuery(userId: Rep[Long]) = characters.filter(_.fkUserId === userId)

  def route(): AuthedService[User, IO] = AuthedService {
    case GET -> Root as user => {
      for {
        chars <- user.ioWithId(id => db.runIO[Seq[Character]](charsQuery(id).result))
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
