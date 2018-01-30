package io.bunkitty.scifimmo.model

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

case class AccessToken(id: Option[Long], fkUserId: Long, expiry: Timestamp, token: String) {
}

object AccessTokens {
  def generateFor(userId: Long): AccessToken = {
    AccessToken(None, userId, Timestamp.valueOf(LocalDateTime.now().plusDays(7)), UUID.randomUUID().toString)
  }
}