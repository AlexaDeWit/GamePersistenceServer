package io.bunkitty.scifimmo.model

import cats.effect.IO
import io.bunkitty.scifimmo.argon2.HashedPassword
import io.bunkitty.scifimmo.throwables.OptionWithoutContentsException

case class User(id: Option[Long], email: String, password: HashedPassword, username: String, validatedEmail: Boolean = false) {
  def ioWithId[A](f: Long => IO[A]): IO[A] = {
    id.map(f).getOrElse(IO.raiseError(OptionWithoutContentsException("Could not retrieve user ID.")))
  }
}