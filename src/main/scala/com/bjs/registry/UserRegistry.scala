package com.bjs.registry

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.bjs.model.{User, Users}
import org.slf4j.{Logger, LoggerFactory}

object UserRegistry {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)
  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(users: Set[User]): Behavior[Command] =
    Behaviors.receiveMessage {
      //todo: this is where mongo needs to go...
      case GetUsers(replyTo) =>
        logger.debug("GetUsers:")
        replyTo ! Users(users.toSeq)
        Behaviors.same
      case CreateUser(user, replyTo) =>
        logger.debug("CreateUser: " + user)
        replyTo ! ActionPerformed(s"User ${user.name} created.")
        registry(users + user)
      case GetUser(name, replyTo) =>
        logger.debug("GetUser: " + name)
        replyTo ! GetUserResponse(users.find(_.name == name))
        Behaviors.same
      case DeleteUser(name, replyTo) =>
        logger.debug("DeleteUser: " + name)
        replyTo ! ActionPerformed(s"User $name deleted.")
        registry(users.filterNot(_.name == name))
    }

  // actor protocol
  sealed trait Command

  final case class GetUsers(replyTo: ActorRef[Users]) extends Command

  final case class CreateUser(user: User, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetUser(name: String, replyTo: ActorRef[GetUserResponse]) extends Command

  final case class DeleteUser(name: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetUserResponse(maybeUser: Option[User])

  final case class ActionPerformed(description: String)
}
