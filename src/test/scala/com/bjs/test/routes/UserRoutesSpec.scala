package com.bjs.test.routes

import java.util.Date
import spray.json._
import org.slf4j._

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshalling.{GenericMarshallers, Marshal}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.bjs.model.{Catalog, Category, Link, User}
import com.bjs.registry.UserRegistry
import com.bjs.routes.UserRoutes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable

class UserRoutesSpec extends AnyWordSpec
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

  // the Akka HTTP route testkit does not yet support a typed actor system (https://github.com/akka/akka-http/issues/2036)
  // so we have to adapt for now
  lazy val testKit = ActorTestKit()

  implicit def typedSystem: ActorSystem[Nothing] = testKit.system
  lazy val routes: Route = new UserRoutes(userRegistry).userRoutes
  // Here we need to implement all the abstract members of UserRoutes.
  // We use the real UserRegistryActor to test it while we hit the Routes,
  // but we could "mock" it by implementing it in-place or by using a TestProbe
  // created with testKit.createTestProbe()
  val userRegistry: ActorRef[UserRegistry.Command] = testKit.spawn(UserRegistry())

  override def createActorSystem(): akka.actor.ActorSystem =
    testKit.system.classicSystem

  // use the json formats to marshal and unmarshall objects in the test
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import com.bjs.model.JsonFormats._

  "UserRoutes" should {
    "return no users if no present (GET /users)" in {
      // note that there's no need for the host part in the uri:
      val request = HttpRequest(uri = "/users")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and no entries should be in the list:
        entityAs[String] should ===("""{"users":[]}""")
      }
    }

    "be able to add users (POST /users)" in {
      val link = Link(
        name = "FunLink",
        url = "Http://www.google.com",
        stars = 4,
        dateAdded = new Date(),
        dateModified = new  Date(),
        public = true)
      val category = Category(
        name = "utility",
        linkList = immutable.Seq[Link](link),
        dateAdded = new Date(),
        dateModified = new Date(),
        public = true
      )
      val catalog = Catalog(
        name = "Home",
        categoryList = immutable.Seq[Category](category),
        dateAdded = new Date(),
        dateModified = new Date(),
        public = true
      )
      val user = User(
        name = "Jenny",
        age = 24,
        countryOfResidence = "USA",
        catalogList = immutable.Seq[Catalog](catalog)
      )

      logger.debug("user Json: " + user.toJson.prettyPrint)

      val userEntity = Marshal(user).to[MessageEntity].futureValue // futureValue is from ScalaFutures

      // using the RequestBuilding DSL:
      val request = Post("/users").withEntity(userEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.Created)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and we know what message we're expecting back:
        entityAs[String] should ===("""{"description":"User Jenny created."}""")
      }
    }

    "be able to get specific users (GET /users/Jenny" in {
      // user the RequestBuilding DSL provided by ScalatestRouteSpec:
      val request = Get(uri = "/users/Jenny")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and no entries should be in the list:
        entityAs[String] contains "\"name\":\"Jenny\"" shouldBe true
      }
    }

    "be able to get all users (GET /users/Jenny" in {
      // user the RequestBuilding DSL provided by ScalatestRouteSpec:
      val request = Get(uri = "/users")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and no entries should be in the list:
        entityAs[String] contains "\"name\":\"Jenny\"" shouldBe true
      }
    }

    "be able to remove users (DELETE /users)" in {
      // user the RequestBuilding DSL provided by ScalatestRouteSpec:
      val request = Delete(uri = "/users/Jenny")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)

        // and no entries should be in the list:
        entityAs[String] should ===("""{"description":"User Jenny deleted."}""")
      }
    }
  }
}
