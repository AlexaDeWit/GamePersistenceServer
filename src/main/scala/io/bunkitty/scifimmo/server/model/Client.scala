package io.bunkitty.scifimmo.server.model

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Client(id: Option[Long], clientId: String, clientSecret: String )

class Clients(tag: Tag)extends Table[Client](tag,"CLIENTS"){
  def id = column[Long]("ID", O.PrimaryKey,O.AutoInc)
  def clientId = column[String]("CLIENT_ID")
  def clientSecret = column[String]("CLIENT_SECRET")

  def * = (id.?, clientId, clientSecret) <> (Client.tupled, Client.unapply)

}

