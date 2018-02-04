package io.bunkitty.scifimmo.server.dto

import java.sql.Timestamp

case class JwtPayload(userData: UserInfo, expiry: Timestamp)
