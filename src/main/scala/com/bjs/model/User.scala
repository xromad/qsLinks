package com.bjs.model

import scala.collection.immutable

final case class User(
                       name: String,
                       age: Int,
                       countryOfResidence: String,
                       catalogList: immutable.Seq[Catalog]
                     )
