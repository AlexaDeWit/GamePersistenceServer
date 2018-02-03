package io.bunkitty.scifimmo.jwt



import cats._
import cats.effect.IO
import cats.syntax._
import io.circe._
import io.circe.syntax._
import io.circe.parser._
import pdi.jwt.{Jwt, JwtAlgorithm}

case class JwtService(private val key: String) {
  def sign(message: String): IO[String] = {
    IO{Jwt.encode(message, key, JwtAlgorithm.HS256)}
  }

  def verify(message: String): IO[Boolean] = {
    for {
      result <- IO(Jwt.decodeRawAll(message, key, Seq(JwtAlgorithm.HS256)))
    } yield result.isSuccess
  }

  def decodeRaw(message: String): IO[(String, String, String)] = {
    IO(Jwt.decodeRawAll(message, key, Seq(JwtAlgorithm.HS256))).flatMap( result => result.fold(IO.raiseError, result => IO(result)))
  }

  def decode[R](message: String, decoder: Decoder[R]): IO[R] = {
    val raw = decodeRaw(message).map{ case (_, body, _) => body }.flatMap( raw => parse(raw).fold(IO.raiseError, json => IO(json)))
    raw.flatMap(json => json.as[R](decoder).fold(IO.raiseError, decoded => IO(decoded)))
  }

}
