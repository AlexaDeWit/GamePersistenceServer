package io.bunkitty.scifimmo.server.services

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import org.http4s._
import org.http4s.dsl.io._
import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.model._
import io.bunkitty.scifimmo.io.IOUtil._
import io.bunkitty.scifimmo.queries.CharacterQueries
import io.bunkitty.scifimmo.server.codecs.Characters._
import io.bunkitty.scifimmo.server.dto.{CharacterContextJwt, UserInfo}
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest
import io.bunkitty.scifimmo.server.dto.requests.characters.CreateCharacterRequest._
import io.bunkitty.scifimmo.server.codecs.JwtPayloads._
import io.circe.syntax._
import org.http4s.AuthedService

case class CharactersService(transactor: Transactor[IO], private val jwtService: JwtService) {

  def route(): AuthedService[UserInfo, IO] = AuthedService {
    case GET -> Root as user => {
      for {
        chars <- CharacterQueries.findCharactersForUser(user.id).transact(transactor)
        response <- Ok(chars)
      } yield response
    }

    case request @ POST -> Root / "create" as user => {
      request.req.decode[CreateCharacterRequest] { character =>
        val characterToInsert = Character(UUID.randomUUID().toString, user.id, character.name, character.species)
        for {
          createdId <- CharacterQueries.insertCharacter(characterToInsert).transact(transactor)
          response <- Ok(characterToInsert.copy(id = createdId))
        } yield response
      }
    }

    case GET -> Root / "validateCharacterContext" / id as user => {
      for {
        chars <- CharacterQueries.findCharactersForUser(user.id).transact(transactor)
        charId <- chars.find(_.id == id).ioResult().map(_.id)
        jwt = CharacterContextJwt(UserInfo(user.id, user.email, user.username),Timestamp.valueOf(LocalDateTime.now().plusDays(7)), charId)
        token <- jwtService.sign(jwt.asJson.toString)
        response <- Ok(token.toString)
      } yield response
    }

    case DELETE -> Root / id as user => {
      for {
        ownedCharacter <- CharacterQueries.findCharacter(id).transact(transactor)
        charId <- ownedCharacter.filter(_.fkUserId == user.id).map(_.id).ioResult("Could not retrieve owned character id")
        deletedId <- CharacterQueries.deleteCharacter(charId).transact(transactor)
        response <- Ok(deletedId.toString)
      } yield response
    }

  }
}
