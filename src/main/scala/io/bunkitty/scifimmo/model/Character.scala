package io.bunkitty.scifimmo.model

case class Character(id: Option[Long],
                     fkUserId: Long,
                     name: String,
                     speciesName: String,
                     locationX: Option[Float] = None,
                     locationY: Option[Float] = None,
                     locationZ: Option[Float] = None,
                     //Stats
                     currentHealth: Option[Float] = None,
                     currentToughness: Option[Float] = None,
                     currentConstitution: Option[Float] = None,
                     currentStamina: Option[Float] = None,
                     currentEndurance: Option[Float] = None,
                     currentQuickness: Option[Float] = None,
                     currentFocus: Option[Float] = None,
                     currentClarity: Option[Float] = None,
                     currentWillpower: Option[Float] = None,
                    )
