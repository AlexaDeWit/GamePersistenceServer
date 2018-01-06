package io.bunkitty.scifimmo.jwt

import java.nio.charset.Charset
import javax.crypto
import javax.crypto.spec.SecretKeySpec

case class HmacSha256(private val key: String) {
  def sign(message: String): String = {
    val mac = crypto.Mac.getInstance(algorithm)
    val secret = new SecretKeySpec(bytes(key), algorithm)
    mac.init(secret)
    new String(mac.doFinal(bytes(message)), charset)
  }

  private val algorithm: String = "HMacSha256"
  private val charset: Charset = Charset.forName("UTF-8")

  private def bytes(s: String): Array[Byte] = s.getBytes(charset)

}
