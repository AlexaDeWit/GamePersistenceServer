package io.bunkitty.scifimmo.model

case class Shard(id: String,
                 fkClientId: String,
                 name: String,
                 region: Option[String],
                 population: Option[Long],
                 acceptingConnections: Boolean,
                 currentIp: String
                )
