package io.bunkitty.scifimmo.server.config

import com.typesafe.config.ConfigFactory

import scala.util.Try

object Config {

  private lazy val config = ConfigFactory.load()
  private lazy val applyDbConfig = (conf: Config) => {
    val pass = config.getString("db.password")
    val username = config.getString("db.user")
    val driver = config.getString("db.driver")
    val url = config.getString("db.url")
    val dbCon = DBConfig(url, driver, username, pass)
    conf.copy(db = Some(dbCon))
  }

  private lazy val applyApplicationConfig = (conf: Config) => {
    val appCon = ApplicationConfig()
    conf.copy(application = Some(appCon))
  }

  private lazy val configLoaders: Seq[Config => Config] = Seq(
    applyDbConfig,
    applyApplicationConfig
  )

  def loadConfig: Try[Config] = {
    val conf = Config()
    Try(configLoaders.foldRight(conf)((f, c) => f(c)))
  }
}

case class DBConfig(url: String, driver: String, username: String, password: String)

case class ApplicationConfig()

case class Config(db: Option[DBConfig] = None, application: Option[ApplicationConfig] = None) {

}
