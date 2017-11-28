package io.bunkitty.scifimmo.server.db.mappings

import io.bunkitty.scifimmo.argon2.HashedPassword
import slick.jdbc.PostgresProfile.api._

object HashedPasswordMapping {
  implicit val HashedPasswordColumnType = MappedColumnType.base[HashedPassword, String](
    hashed => hashed.underlying,
    str => HashedPassword.unsafeWrapString(str)
  )
}