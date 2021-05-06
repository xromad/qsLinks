package com.bjs.model

import java.util.Date

import scala.collection.immutable

final case class Catalog(
                          name: String,
                          categoryList: immutable.Seq[Category],
                          dateAdded: Date,
                          dateModified: Date,
                          public: Boolean
                        )


