package com.bjs.test

import com.bjs.model.User
import com.bjs.logic.UpdateLogic
import org.mongodb.scala.bson.BsonDocument
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spray.json._
import com.bjs.model.JsonFormats._

class UpdateLogicTest extends AnyFlatSpec
  with Matchers {

  "sending a user" should "generate an updateList" in {
     val myUser: User =
     """| {
        | "age": 42,
        | "catalogList": [{
        | "categoryList": [{
        | "dateAdded": "2021-05-04T14:29:48.279-0400",
        | "dateModified": "2021-05-04T14:29:48.279-0400",
        | "linkList": [{
        | "dateAdded": "2021-05-04T14:29:48.278-0400",
        | "dateModified": "2021-05-04T14:29:48.278-0400",
        | "name": "FunLink",
        | "public": true,
        | "stars": 4,
        | "url": "Http://www.google.com"
        | }],
        | "name": "utility",
        | "public": true
        | }],
        | "dateAdded": "2021-05-04T14:29:48.279-0400",
        | "dateModified": "2021-05-04T14:29:48.279-0400",
        | "name": "Home",
        | "public": true
        | }],
        | "countryOfResidence": "USA",
        | "name": "Jenny"
        | }""".stripMargin.parseJson.convertTo[User]

    val updateList: String = s"""{\"$$set\": {\"age\": ${myUser.age}}}"""

    val changeList: BsonDocument = UpdateLogic.createUpdateList(user = myUser)
    changeList.entrySet().isEmpty shouldBe false
    changeList.toString shouldBe updateList
  }
}
