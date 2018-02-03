package io.bunkitty.scifimmo.jwt

import java.nio.charset.Charset
import javax.crypto
import javax.crypto.spec.SecretKeySpec

import cats.effect.IO

case class HmacSha256(private val key: String) {
  def sign(message: String): IO[String] = {
    val mac = crypto.Mac.getInstance(algorithm)
    val secret = new SecretKeySpec(bytes(key), algorithm)
    mac.init(secret)
    IO{new String(mac.doFinal(bytes(message)), charset)}
  }

  val algorithm: String = "HMacSha256"
  val charset: Charset = Charset.forName("UTF-8")
  def bytes(s: String): Array[Byte] = s.getBytes(charset)

}
