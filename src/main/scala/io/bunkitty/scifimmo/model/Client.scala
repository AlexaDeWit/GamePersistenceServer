package io.bunkitty.scifimmo.model

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Client(id: Option[Long], clientId: String, clientSecret: String )

