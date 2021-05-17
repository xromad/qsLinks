package com.bjs.registry

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.bjs.model.{User, Users}
import com.bjs.repository.UserRepository
import org.mongodb.scala.bson.BsonDocument
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object UserRegistry {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

  def apply(): Behavior[Command] = registry

  implicit val ec: ExecutionContext = ExecutionContext.global

  private def registry: Behavior[Command] =
    Behaviors.receiveMessage {
      case GetUsers(replyTo) =>
        logger.debug("GetUsers:")
        UserRepository.findAll() onComplete {
          case Success(users) => replyTo ! Users(users)
          case Failure(t) => replyTo ! Users(Seq.empty[User])
        }
        Behaviors.same
      case CreateUser(user, replyTo) =>
        logger.debug("CreateUser: " + user)
        UserRepository.save(user)
        replyTo ! ActionPerformed(s"User ${user.name} created.")
        Behaviors.same
      case UpdateUser(user, replyTo) =>
        logger.debug("UpdateUser: " + user)
        UserRepository.findByName(user.name) onComplete {
          case Success(users) =>
            val updateList: BsonDocument = users.head.createUserUpdateList(user)
            UserRepository.update(user.name, updateList) onComplete {
              case Success(updatedUsers) => replyTo ! ActionPerformed(s"User ${updatedUsers} updated.")
              case Failure(t) => replyTo ! ActionPerformed(s"User ${user.name} update error.")
            }
          case Failure(t) => replyTo ! ActionPerformed(s"User ${user.name} not found.")
        }
        Behaviors.same
      case GetUser(name, replyTo) =>
        logger.debug("GetUser: " + name)
        UserRepository.findByName(name) onComplete {
          case Success(users) => replyTo ! GetUserResponse(Some(Users(users)))
          case Failure(t) => replyTo ! GetUserResponse(Some(Users(Seq.empty[User])))
        }
        Behaviors.same
      case DeleteUser(name, replyTo) =>
        logger.debug("DeleteUser: " + name)
        UserRepository.delete(name)
        replyTo ! ActionPerformed(s"User $name deleted.")
        Behaviors.same
    }

  // actor protocol
  sealed trait Command

  final case class GetUsers(replyTo: ActorRef[Users]) extends Command

  final case class CreateUser(user: User, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class UpdateUser(user: User, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetUser(name: String, replyTo: ActorRef[GetUserResponse]) extends Command

  final case class DeleteUser(name: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetUserResponse(maybeUser: Option[Users])

  final case class ActionPerformed(description: String)

}

