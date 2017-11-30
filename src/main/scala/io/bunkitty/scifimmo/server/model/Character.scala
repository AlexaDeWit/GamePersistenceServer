package io.bunkitty.scifimmo.server.model

case class Character(id: Option[Long],
                     fkUserId: Long,
                     name: String,
                     speciesName: String,
                     locationX: Float,
                     locationY: Float,
                     locationZ: Float,
                    //Stats
                     currentHealth: Float,
                     currentToughness: Float,
                     currentConstitution: Float,
                     currentStamina: Float,
                     currentSpeed: Float,
                     currentEndurance: Float,
                     currentFocus: Float,
                     currentClarity: Float,
                     currentWillpower: Float,

                    ) {

}
