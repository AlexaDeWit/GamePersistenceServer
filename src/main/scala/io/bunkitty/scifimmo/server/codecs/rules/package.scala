package io.bunkitty.scifimmo.server.codecs

import io.circe.generic.extras._

package object rules {
  implicit val config: Configuration = Configuration.default.copy(
    transformMemberNames = {
      name =>
        val first = name.headOption
        first.map(_.toUpper).getOrElse("") + name.tail
    }
  )
}
