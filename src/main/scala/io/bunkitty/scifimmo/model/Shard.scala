package io.bunkitty.scifimmo.model

case class Shard(id: Option[Long],
                 fkClientId: Long,
                 name: String,
                 region: Option[String],
                 population: Option[Long],
                 acceptingConnections: Boolean,
                 currentIp: String
                )
