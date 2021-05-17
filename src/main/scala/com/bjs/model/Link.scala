package com.bjs.model

import java.util.Date

import scala.collection.mutable.ListBuffer

final case class Link(
                       name: String,
                       url: String,
                       stars: Int,
                       dateAdded: Date,
                       dateModified: Date,
                       public: Boolean
                     ) {

  def createLinkUpdates(newLink: Link): Option[Seq[String]] = {
    val oldLink = this
    val updates: ListBuffer[String] = ListBuffer.empty

    if (oldLink.name != newLink.name) updates += s"""\"name\": \"${newLink.name}\""""
    if (oldLink.url != newLink.url) updates += s"""\"url\": \"${newLink.url}\""""
    if (oldLink.stars != newLink.stars) updates += s"""\"stars\": ${newLink.stars}"""
    updates += s"""\"dateModified\": ${new Date()}"""
    if (oldLink.public != newLink.public) updates += s"""\"public\": ${newLink.public}"""

    updates.length match {
      case n if n > 0 => Some(updates.toSeq)
      case _ => None
    }
  }
}


