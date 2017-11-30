package io.bunkitty.scifimmo.server.model
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

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
                     currentQuickness: Float,
                     currentEndurance: Float,
                     currentFocus: Float,
                     currentClarity: Float,
                     currentWillpower: Float,
                    )

class Characters(tag: Tag) extends Table[Character](tag, "POSTGRES") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def fkUserId = column[Long]("FK_USER_ID")
  def name = column[String]("NAME")
  def speciesName = column[String]("SPECIES_NAME")
  def locationX = column[Float]("LOCATION_X")
  def locationY = column[Float]("LOCATION_Y")
  def locationZ = column[Float]("LOCATION_Z")
  //Stats
  def currentHealth = column[Float]("CURRENT_HEALTH")
  def currentToughness = column[Float]("CURRENT_TOUGHNESS")
  def currentConstitution = column[Float]("CURRENT_CONSTITUTION")
  def currentStamina = column[Float]("CURRENT_STAMINA")
  def currentQuickness = column[Float]("CURRENT_QUICKNESS")
  def currentEndurance = column[Float]("CURRENT_ENDURANCE")
  def currentFocus = column[Float]("CURRENT_FOCUS")
  def currentClarity = column[Float]("CURRENT_CLARITY")
  def currentWillpower = column[Float]("CURRENT_WILLPOWER")

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