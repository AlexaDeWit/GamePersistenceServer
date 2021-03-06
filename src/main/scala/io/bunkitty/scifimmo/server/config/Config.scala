package io.bunkitty.scifimmo.server.config

import pureconfig._
import pureconfig.error.ConfigReaderFailures

object Config {
  val config: Either[ConfigReaderFailures,Config] = loadConfig[Config]
}

case class DBConfig(user: String, password: String, schema: String, hostname: String)

case class JWTConfig(secret: String)

case class Config(db: DBConfig, jwt: JWTConfig)