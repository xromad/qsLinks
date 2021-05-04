package com.bjs.model

import java.util.Date

import scala.collection.immutable

case class Category(
                     name: String,
                     linkList: immutable.Seq[Link],
                     dateAdded: Date,
                     dateModified: Date,
                     public: Boolean
                   )

