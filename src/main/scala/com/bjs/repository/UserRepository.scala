package com.bjs.repository

import com.bjs.model.User
import com.bjs.mongo.UserMongo
import org.mongodb.scala._
import org.mongodb.scala.bson.{BsonDocument, BsonString}
import org.mongodb.scala.model.UpdateOptions
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

object UserRepository {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def findByName(name: String): Future[Seq[User]] = {
    UserMongo.userCollection.find(Document("name" -> new BsonString(name))).collect.head
  }

  def findAll(): Future[Seq[User]] = {
    UserMongo.userCollection.find().collect.head
  }

  def save(user: User): Future[String] =
    UserMongo.userCollection.insertOne(user).head.map { _ => user.name }

  def update(name: String, updateList: BsonDocument): Future[String] = {
    val updateOptions: UpdateOptions = new UpdateOptions().upsert(false)
    UserMongo.userCollection.updateOne(Document("name" -> BsonString(name)), updateList, updateOptions)
      .head
      .map { _ => name }
  }

  def delete(name: String): Future[Option[result.DeleteResult]] =
    UserMongo.userCollection.deleteOne(Document("name" -> new BsonString(name))).head().map(Option(_))
}
