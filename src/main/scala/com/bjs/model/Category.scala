package com.bjs.model

import java.util.Date

import scala.collection.immutable
import scala.collection.mutable.ListBuffer

/**
  * A Catalog has a set of categories that contain links
  *
  * @param name
  * @param linkList
  * @param dateAdded
  * @param dateModified
  * @param public
  */
case class Category(
                     name: String,
                     linkList: immutable.Seq[Link],
                     dateAdded: Date,
                     dateModified: Date,
                     public: Boolean
                   ) {

  def createCategoryUpdates(oldCategory: Category, newCategory: Category): String = {
    val updates: ListBuffer[String] = ListBuffer.empty
    //Note: link knows how to update itself
    if (oldCategory.name != newCategory.name) updates += s"""\"name\": \"${newCategory.name}\""""
    updates += s"""\"dateModified\": ${new Date()}"""
    if (oldCategory.public != newCategory.public) updates += s"""\"public\": ${newCategory.public}"""

    updates.mkString(", ")
  }
}

