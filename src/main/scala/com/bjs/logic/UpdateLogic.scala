package com.bjs.logic

import com.bjs.model._
import org.slf4j.{Logger, LoggerFactory}

object UpdateLogic {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

  //todo: I still have a fair amount of work to do here...
  /*{ $set: { item: "ABC123", "ratings.1": { by: "xyz", rating: 3 } } }*/

  def createUpdate(oldUsers: Users, newUsers: Users): String = {
    (for {
      user <- newUsers.users
      catalog <- user.catalogList
      category <- catalog.categoryList
      newLink <- category.linkList
      update <- oldUsers.users.find(_.name == user.name).get //actually might need find with index...
        .catalogList.find(_.name == catalog.name).get
        .categoryList.find(_.name == category.name).get
        .linkList.find(_.name == newLink.name).get
        .createLinkUpdates(newLink).get
    } yield "{" + List(user.name, catalog.name, category.name, newLink.name, update).mkString(".") + "}")
      .mkString(", ")
  }
}
