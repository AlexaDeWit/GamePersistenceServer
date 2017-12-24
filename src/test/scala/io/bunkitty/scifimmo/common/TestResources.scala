package io.bunkitty.scifimmo.common

import cats.effect.IO
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor
import io.bunkitty.scifimmo.server.config.Config

object TestResources {

  lazy val config = Config.config.right.get
  lazy val dbConf = config.db

  lazy val postgresTransactor: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", s"jdbc:postgresql://${dbConf.hostname}/${dbConf.schema}", dbConf.user, dbConf.password
  )

}
