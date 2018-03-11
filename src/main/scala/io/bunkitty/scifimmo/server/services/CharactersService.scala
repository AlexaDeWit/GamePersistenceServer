package io.bunkitty.scifimmo.server.services

import java.sql.Timestamp
import java.time.LocalDateTime

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
        val characterToInser = Character(None, user.id, character.name, character.species)
        for {
          createdId <- CharacterQueries.insertCharacter(characterToInser).transact(transactor)
          response <- Ok(characterToInser.copy(id = Some(createdId)))
        } yield response
      }
    }

    case GET -> Root / "validateCharacterContext" / LongVar(id) as user => {
      for {
        chars <- CharacterQueries.findCharactersForUser(user.id).transact(transactor)
        charId <- chars.find(_.id.contains(id)).ioResult().flatMap(_.id.ioResult())
        jwt = CharacterContextJwt(UserInfo(user.id, user.email, user.username),Timestamp.valueOf(LocalDateTime.now().plusDays(7)), charId)
        token <- jwtService.sign(jwt.asJson.toString)
        response <- Ok(token.toString)
      } yield response
    }

    case DELETE -> Root / LongVar(id) as user => {
      for {
        ownedCharacter <- CharacterQueries.findCharacter(id).transact(transactor)
        charId <- ownedCharacter.flatMap(_.id).ioResult("Could not retrieve owned character id")
        deletedId <- CharacterQueries.deleteCharacter(charId).transact(transactor)
        response <- Ok(deletedId.toString)
      } yield response
    }

  }
}
