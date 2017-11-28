package construction.universe.server.db.mappings

import construction.universe.argon2.HashedPassword
import slick.jdbc.PostgresProfile.api._

object HashedPasswordMapping {
  implicit val HashedPasswordColumnType = MappedColumnType.base[HashedPassword, String](
    hashed => hashed.underlying,
    str => HashedPassword.unsafeWrapString(str)
  )
}