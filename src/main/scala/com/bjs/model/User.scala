package com.bjs.model

import org.mongodb.scala.bson.BsonDocument

import scala.collection.immutable
import scala.collection.mutable.ListBuffer

/**
  * User is the base object of the system.  It is who logs in and who manages their account
  *
  * @param name
  * @param age
  * @param countryOfResidence
  * @param catalogList
  */
final case class User(
                       name: String,
                       age: Int, //todo: remove age and add birth date (age should be derived)
                       countryOfResidence: String,
                       //todo: add date created and date modified
                       catalogList: immutable.Seq[Catalog]
                       //todo: add permisson level
                     ) {

  //todo: really need to figure out how to get this into the individual objects/case classes
  //todo: find a way to do this directly into the objects and not as string generations
  def createUserUpdateList(newUser: User): BsonDocument = {
    val oldUser = this
    val updates: ListBuffer[String] = ListBuffer.empty

    //todo: switch the api to use primary id keys so you can update name
    //if (oldUser.name != newUser.name) updates += s"""\"name\": \"${newUser.name}\""""

    if (oldUser.age != newUser.age) updates += s"""\"age\": ${newUser.age}"""
    if (oldUser.countryOfResidence != newUser.countryOfResidence) updates += s"""\"countryOfResidence\": \"${newUser.countryOfResidence}\""""

    //Note catalog knows how to update iteself
    //todo add date created
    //todo add date updated
    //todo add PermissionLevel enum
    val jsonUpdateString: String = s"{$$set: {${updates.mkString(", ")}}}"

    BsonDocument(jsonUpdateString)
  }

}

