package io.bunkitty.scifimmo.model

case class BasicToken(token: String) {

}

object BasicToken {
  def extract(headerContent: String): Either[String, BasicToken] = {
    val parseOption = if(headerContent.contains("Basic")) {
      headerContent.split("Basic").lastOption.map(_.trim)
    }
    else {
      None
    }
    parseOption.map(t => BasicToken(t)).toRight("Could not parse bearer token")
  }
}
