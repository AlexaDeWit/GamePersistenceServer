package io.bunkitty.scifimmo.server.model

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Shard(id: Option[Long],
                 fkClientId: Long,
                 name: String,
                 region: String,
                 population: Long,
                 acceptingConnections: Boolean,
                 currentIp: String
                )

class Shards(tag: Tag) extends Table[Shard](tag, "SHARDS"){
  def id = column[Long]("ID", O.PrimaryKey,O.AutoInc)
  def fkClientId = column[Long]("FK_CLIENT_ID")
  def name = column[String]("NAME")
  def region = column[String]("REGION")
  def population = column[Long]("POPULATION")
  def acceptingConnections = column[Boolean]("ACCEPTING_CONNECTIONS")
  def currentIp = column[String]("CURRENT_IP")
  def * = (id.?, fkClientId, name, region, population, acceptingConnections, currentIp) <> (Shard.tupled, Shard.unapply)
}