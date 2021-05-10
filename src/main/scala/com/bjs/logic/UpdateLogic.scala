package com.bjs.logic

import com.bjs.model.User
import org.mongodb.scala.bson.BsonDocument
import org.slf4j.{Logger, LoggerFactory}

object UpdateLogic {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

  //todo: this now updates the age only, need to make this generic
  def createUpdateList(user: User): BsonDocument = {
    logger.debug(s"Creating update for: ${user.name}")
    val jsonAgeString = s"{ $$set: { age: ${user.age} }}"
    BsonDocument(jsonAgeString)
  }

}
