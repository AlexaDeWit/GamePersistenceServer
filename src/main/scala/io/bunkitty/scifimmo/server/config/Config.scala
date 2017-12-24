package io.bunkitty.scifimmo.server.config

import pureconfig._
import pureconfig.error.ConfigReaderFailures

object Config {
  val config: Either[ConfigReaderFailures,Config] = loadConfig[Config]
}

case class DBConfig(url: String, driver: String, user: String, password: String, schema: String, hostname: String, port: String)


case class Config(db: DBConfig) {

}
