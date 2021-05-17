package com.bjs.test

import com.bjs.logic.UpdateLogic._
import com.bjs.model.JsonFormats._
import com.bjs.model.{Link, User, Users}
import org.mongodb.scala.bson.BsonDocument
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spray.json._

class UpdateLogicTest extends AnyFlatSpec with Matchers {

  def makeTestLink(name: String = "FunLink",
                   public: Boolean = false,
                   stars: Int = 3,
                   url: String = "http://www.google.com"): Link = {
    s"""|{
        | "dateAdded": "2021-05-04T14:29:48.278-0400",
        | "dateModified": "2021-05-04T14:29:48.278-0400",
        | "name": "$name",
        | "public": $public,
        | "stars": $stars,
        | "url": "$url"
        | }""".stripMargin.parseJson.convertTo[Link]
  }

  def makeTestUser(name: String = "Jenny",
                   linkStars: String = "0",
                   age: Int = 24,
                   countryOfResidence: String = "USA"
                  ): User = {
    s"""| {
        | "age": $age,
        | "catalogList": [{
        | "categoryList": [{
        | "dateAdded": "2021-05-04T14:29:48.279-0400",
        | "dateModified": "2021-05-04T14:29:48.279-0400",
        | "linkList": [{
        | "dateAdded": "2021-05-04T14:29:48.278-0400",
        | "dateModified": "2021-05-04T14:29:48.278-0400",
        | "name": "FunLink",
        | "public": true,
        | "stars": $linkStars,
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
        | "countryOfResidence": "$countryOfResidence",
        | "name": "$name"
        | }""".stripMargin.parseJson.convertTo[User]
  }

  "Users creaeUpdate from two users lists " should "generate an updateList" in {
    val newUser1 = makeTestUser(name = "Ann", linkStars = "1")
    val newUser2 = makeTestUser(name = "Brenda", linkStars = "2")
    val newUser3 = makeTestUser(name = "Cindy", linkStars = "3")
    val newUser4 = makeTestUser(name = "Ann", linkStars = "4")
    val newUser5 = makeTestUser(name = "Brenda", linkStars = "5")
    val newUser6 = makeTestUser(name = "Cindy", linkStars = "6")

    val oldUsers: Users = Users(Seq(newUser1, newUser2, newUser3))
    val newUsers: Users = Users(Seq(newUser4, newUser5, newUser6))

    println("BJS map: " + createUpdate(oldUsers, newUsers))
    //todo: add test here
  }

  "sending a user" should "generate an updateList" in {
    val oldUser = makeTestUser()
    val newUser = makeTestUser(name = "Ann", age = 42, countryOfResidence = "Canada")
    val expected: String = s"""{\"$$set\": {\"age\": ${newUser.age}, \"countryOfResidence\": \"${newUser.countryOfResidence}\"}}"""

    val changeList: BsonDocument = oldUser.createUserUpdateList(newUser)
    changeList.entrySet().isEmpty shouldBe false
    changeList.toJson shouldBe expected
    changeList.toJson.contains("name") shouldBe false
    changeList.toJson.contains("age") shouldBe true
    changeList.toJson.contains("countryOfResidence") shouldBe true
  }

  "sending a link" should "generate a link update list" in {
    val oldLink = makeTestLink()
    val newLink = makeTestLink(name = "FunnerLink", public = true, stars = 4, url = "http://googleygoop.com")

    val changeList: String = oldLink.createLinkUpdates(newLink).get.mkString(", ")
    changeList.contains("name") shouldBe true
    changeList.contains("public") shouldBe true
    changeList.contains("dateModified") shouldBe true
    changeList.contains("stars") shouldBe true
    changeList.contains("url") shouldBe true
    changeList.contains("dateAdded") shouldBe false
  }
}

