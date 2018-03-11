package io.bunkitty.scifimmo.model

import cats.effect.IO
import io.bunkitty.scifimmo.argon2.HashedPassword
import io.bunkitty.scifimmo.throwables.OptionWithoutContentsException

case class User(id: String, email: String, password: HashedPassword, username: String, validatedEmail: Boolean = false)