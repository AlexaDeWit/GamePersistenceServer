package io.bunkitty.scifimmo.server.services

import org.http4s._
import org.http4s.dsl.io._
import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.queries.CharacterQueries
import io.bunkitty.scifimmo.server.codecs.Characters._
import io.bunkitty.scifimmo.server.dto.UserInfo
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest._
import org.http4s.AuthedService

case class CharactersService(transactor: Transactor[IO]) {

  def route(): AuthedService[UserInfo, IO] = AuthedService {
    case GET -> Root as user => {
      for {
        chars <- CharacterQueries.findCharactersForUser(user.id).transact(transactor)
        response <- Ok(chars)
      } yield response
    }
    case request @ POST -> Root / "create" as user => {
      request.req.decode[CreateCharacterRequest] { character =>
        val characterToInser = Character(None, user.id, character.name, character.species)
        for {
          createdId <- CharacterQueries.insertCharacter(characterToInser).transact(transactor)
          response <- Ok(characterToInser.copy(id = Some(createdId)))
        } yield response
      }
    }
  }
}
