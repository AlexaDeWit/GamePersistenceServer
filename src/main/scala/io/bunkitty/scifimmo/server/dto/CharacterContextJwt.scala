package io.bunkitty.scifimmo.server.dto

import java.sql.Timestamp

case class CharacterContextJwt(userData: UserInfo, expiry: Timestamp, characterId: String)
