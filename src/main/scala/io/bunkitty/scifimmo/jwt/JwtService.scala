package io.bunkitty.scifimmo.jwt



import cats._
import cats.effect.IO
import cats.syntax._
import io.circe._
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

  def decode[R](message: String, decoder: Decoder[R]): IO[R] = ???

}
