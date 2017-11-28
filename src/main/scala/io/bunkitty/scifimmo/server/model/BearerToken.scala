package io.bunkitty.scifimmo.server.model

case class BearerToken(token: String) {

}

object BearerToken {
  def extract(headerContent: String): Either[String, BearerToken] = {
    val parseOption = if(headerContent.contains("Basic")) {
      headerContent.split("Basic").lastOption.map(_.trim)
    }
    else {
      None
    }
    parseOption.map(t => BearerToken(t)).toRight("Could not parse bearer token")
  }
}
