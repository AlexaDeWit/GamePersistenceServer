package io.bunkitty.scifimmo.model

import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

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
                     currentQuickness: Option[Float] = None,
                     currentEndurance: Option[Float] = None,
                     currentFocus: Option[Float] = None,
                     currentClarity: Option[Float] = None,
                     currentWillpower: Option[Float] = None,
                    )

class Characters(tag: Tag) extends Table[Character](tag, "CHARACTERS") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def fkUserId = column[Long]("FK_USER_ID")
  def name = column[String]("NAME")
  def speciesName = column[String]("SPECIES_NAME")
  def locationX = column[Option[Float]]("LOCATION_X")
  def locationY = column[Option[Float]]("LOCATION_Y")
  def locationZ = column[Option[Float]]("LOCATION_Z")
  //Stats
  def currentHealth = column[Option[Float]]("CURRENT_HEALTH")
  def currentToughness = column[Option[Float]]("CURRENT_TOUGHNESS")
  def currentConstitution = column[Option[Float]]("CURRENT_CONSTITUTION")
  def currentStamina = column[Option[Float]]("CURRENT_STAMINA")
  def currentQuickness = column[Option[Float]]("CURRENT_QUICKNESS")
  def currentEndurance = column[Option[Float]]("CURRENT_ENDURANCE")
  def currentFocus = column[Option[Float]]("CURRENT_FOCUS")
  def currentClarity = column[Option[Float]]("CURRENT_CLARITY")
  def currentWillpower = column[Option[Float]]("CURRENT_WILLPOWER")

  def * = (id.?,
        fkUserId,
        name,
        speciesName,
        locationX,
        locationY,
        locationZ,
        currentHealth,
        currentToughness,
        currentConstitution,
        currentStamina,
        currentQuickness,
        currentEndurance,
        currentFocus,
        currentClarity,
        currentWillpower) <> (Character.tupled, Character.unapply)
}