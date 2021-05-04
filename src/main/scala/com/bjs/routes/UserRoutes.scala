package com.bjs.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route

import com.bjs.registry.UserRegistry
import com.bjs.registry.UserRegistry._
import com.bjs.model.{User, Users}
import scala.concurrent.Future
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout



class UserRoutes(userRegistry: ActorRef[UserRegistry.Command])(implicit val system: ActorSystem[_]) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import com.bjs.model.JsonFormats._

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getUsers(): Future[Users] =
    userRegistry.ask(GetUsers)
  def getUser(name: String): Future[GetUserResponse] =
    userRegistry.ask(GetUser(name, _))
  def createUser(user: User): Future[ActionPerformed] =
    userRegistry.ask(CreateUser(user, _))
  def deleteUser(name: String): Future[ActionPerformed] =
    userRegistry.ask(DeleteUser(name, _))

  val userRoutes: Route =
  pathPrefix("users") {
    concat(
      pathEnd {
        concat(
          // get All Users (/users)
          get {
            complete(getUsers())
          },
          // post Create User (/users <body>)
          post {
            entity(as[User]) { user =>
              onSuccess(createUser(user)) { performed =>
                complete((StatusCodes.Created, performed))
              }
            }
          })
      },
      path(Segment) { name =>
        concat(
          // get User by Name (/users/"name")
          get {
            // retrieve-user-info
            rejectEmptyResponse {
              onSuccess(getUser(name)) { response =>
                complete(response.maybeUser)
              }
            }
          },
          // delete User by Name (/users/"name)
          delete {
            //users-delete-logic
            onSuccess(deleteUser(name)) { performed =>
              complete((StatusCodes.OK, performed))
            }
          })
      })
  }
}

