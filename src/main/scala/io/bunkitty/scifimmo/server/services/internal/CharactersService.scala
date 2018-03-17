package io.bunkitty.scifimmo.server.services.internal

import cats.effect.IO
import doobie._
import doobie.implicits._
import io.bunkitty.scifimmo.io.IOUtil._
import io.bunkitty.scifimmo.jwt.JwtService
import io.bunkitty.scifimmo.queries.CharacterQueries
import io.bunkitty.scifimmo.server.codecs.Characters._
import io.bunkitty.scifimmo.server.dto.GameClientClaims
import org.http4s.AuthedService
import org.http4s.dsl.io._

case class CharactersService(transactor: Transactor[IO], private val jwtService: JwtService) {

  def route(): AuthedService[GameClientClaims, IO] = AuthedService {
    case GET -> Root / "characters" / id as _ => {
      for {
        maybeCharacter <- CharacterQueries.findCharacter(id).transact(transactor)
        character <- maybeCharacter.ioResult(s"Failed to retrieve character $id")
        response <- Ok(character)
      } yield response
    }
  }
}
