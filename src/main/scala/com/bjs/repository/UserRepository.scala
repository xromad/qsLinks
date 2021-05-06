package com.bjs.repository

import com.bjs.model.User
import com.bjs.mongo.UserMongo
import org.mongodb.scala._
import org.mongodb.scala.bson.BsonString
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

object UserRepository {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val userMongo = new UserMongo

  def findByName(name: String): Future[Seq[User]] = {
    userMongo.userCollection.find(Document("name" -> new BsonString(name))).collect.head
  }

  def findAll(): Future[Seq[User]] = {
    userMongo.userCollection.find().collect.head
  }

  def save(user: User): Future[String] =
    userMongo.userCollection.insertOne(user).head.map { _ => user.name }

  //todo: update this to use updateOne later
  def update(user: User): Future[String] =
    userMongo.userCollection.replaceOne(Document("name" -> new BsonString(user.name)), user).head.map { _ => user.name }

  def delete(name: String): Future[Option[result.DeleteResult]] =
    userMongo.userCollection.deleteOne(Document("name" -> new BsonString(name))).head().map(Option(_))
}
