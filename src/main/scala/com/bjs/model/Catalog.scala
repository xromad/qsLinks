package com.bjs.model

import java.util.Date

import scala.collection.immutable
import scala.collection.mutable.ListBuffer

/**
  * A user has a catalog of links that are broken into separate Categories
  *
  * @param name
  * @param categoryList
  * @param dateAdded
  * @param dateModified
  * @param public
  */
final case class Catalog(
                          name: String,
                          categoryList: immutable.Seq[Category],
                          dateAdded: Date,
                          dateModified: Date,
                          public: Boolean
                        ) {

  def createCatalogUpdates(oldCatalog: Catalog, newCatalog: Catalog): String = {
    val updates: ListBuffer[String] = ListBuffer.empty

    if (oldCatalog.name != newCatalog.name) updates += s"""\"name\": \"${newCatalog.name}\""""
    //Note: category knows how to update itself
    //updates += createCategoryUpdateList(oldCatalog.categoryList, newCatalog.categoryList)
    updates += s"""\"dateModified\": ${new Date()}"""
    if (oldCatalog.public != newCatalog.public) updates += s"""\"public\": ${newCatalog.public}"""

    //todo: this will need formatting
    updates.mkString(", ")
  }
}


