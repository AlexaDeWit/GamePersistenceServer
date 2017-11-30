package io.bunkitty.scifimmo.server.config

import pureconfig._
import pureconfig.error.ConfigReaderFailures

object Config {
  val config: Either[ConfigReaderFailures,Config] = loadConfig[Config]
}

case class DBConfig(url: String, driver: String, user: String, password: String)


case class Config(db: DBConfig) {

}
