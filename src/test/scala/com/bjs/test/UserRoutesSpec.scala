package com.bjs.test

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

/**
  * Example json for safe keeping
  * {
  * "age": 24,
  * "catalogList": [{
  * "categoryList": [{
  * "dateAdded": "2021-05-04T14:29:48.279-0400",
  * "dateModified": "2021-05-04T14:29:48.279-0400",
  * "linkList": [{
  * "dateAdded": "2021-05-04T14:29:48.278-0400",
  * "dateModified": "2021-05-04T14:29:48.278-0400",
  * "name": "FunLink",
  * "public": true,
  * "stars": 4,
  * "url": "Http://www.google.com"
  * }],
  * "name": "utility",
  * "public": true
  * }],
  * "dateAdded": "2021-05-04T14:29:48.279-0400",
  * "dateModified": "2021-05-04T14:29:48.279-0400",
  * "name": "Home",
  * "public": true
  * }],
  * "countryOfResidence": "USA",
  * "name": "Jenny"
  * }
  */

class UserRoutesSpec extends AnyWordSpec
  with Matchers
  with ScalaFutures
  with ScalatestRouteTest {
  //todo: this will become the routes test when I have mockito or somethign similiar set up

  /*  // the Akka HTTP route testkit does not yet support a typed actor system (https://github.com/akka/akka-http/issues/2036)
    // so we have to adapt for now
    lazy val testKit = ActorTestKit()
    lazy val routes: Route = new UserRoutes(userRegistry).userRoutes

    //val repository = new UserRepository(MongoCollection(db.getCollection("col", classOf[User])))
    val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

    implicit def typedSystem: ActorSystem[Nothing] = testKit.system

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
          dateModified = new Date(),
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
    }*/
}
