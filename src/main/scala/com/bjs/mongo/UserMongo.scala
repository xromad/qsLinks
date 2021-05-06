package com.bjs.mongo

import com.bjs.model._
import com.typesafe.config.{Config, ConfigFactory}
import org.bson.codecs.configuration.CodecRegistries._
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._

class UserMongo {
  lazy val config: Config = ConfigFactory.load()
  lazy val mongoClient: MongoClient = MongoClient(config.getString("my-app.mongo.uri"))

  lazy val codecRegistry: CodecRegistry = fromRegistries(
    fromProviders(
      classOf[Users],
      classOf[User],
      classOf[Catalog],
      classOf[Category],
      classOf[Link]),
    MongoClient.DEFAULT_CODEC_REGISTRY
  )

  lazy val database: MongoDatabase = mongoClient.getDatabase(config.getString("my-app.mongo.database"))
    .withCodecRegistry(codecRegistry)

  lazy val userCollection: MongoCollection[User] = database.getCollection[User]("users")
}

