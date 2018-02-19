package io.bunkitty.scifimmo.server.dto

import java.sql.Timestamp

case class UserContextJwt(userData: UserInfo, expiry: Timestamp)
