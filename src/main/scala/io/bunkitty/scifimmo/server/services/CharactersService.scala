package io.bunkitty.scifimmo.server.services

import org.http4s._
import org.http4s.dsl.io._
import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.queries.CharacterQueries
import io.bunkitty.scifimmo.server.codecs.Characters._
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest._
import org.http4s.AuthedService

case class CharactersService(transactor: Transactor[IO]) {

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
          createdId <- CharacterQueries.insertCharacter(characterToInsert).transact(transactor)
          response <- Ok(characterToInsert.copy(id = Some(createdId)))
        } yield response
      }
    }
  }
}
