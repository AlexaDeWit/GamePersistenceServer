package io.bunkitty.scifimmo.model

case class JsonWebToken(token: String)

object JsonWebToken {
  def extract(headerContent: String): Either[String, JsonWebToken] = {
    val parseOption = if(headerContent.contains("Bearer")) {
      headerContent.split("Bearer").lastOption.map(_.trim)
    }
    else {
      None
    }
    parseOption.map(t => JsonWebToken(t)).toRight("Could not parse bearer token")
  }
}
