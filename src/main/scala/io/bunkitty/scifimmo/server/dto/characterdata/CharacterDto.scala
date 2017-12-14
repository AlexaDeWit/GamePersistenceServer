package io.bunkitty.scifimmo.server.dto.characterdata

import io.circe.generic.JsonCodec

@JsonCodec case class CharacterDto(name: String,
                                speciesName: String,
                                locationX: Option[Float],
                                locationY: Option[Float],
                                locationZ: Option[Float],
                                //Stats
                                currentHealth: Option[Float],
                                currentToughness: Option[Float],
                                currentConstitution: Option[Float],
                                currentStamina: Option[Float],
                                currentQuickness: Option[Float],
                                currentEndurance: Option[Float],
                                currentFocus: Option[Float],
                                currentClarity: Option[Float],
                                currentWillpower: Option[Float],
                               )