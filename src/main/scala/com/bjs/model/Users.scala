package com.bjs.model

import scala.collection.immutable

/**
  * Users is really just a holder for user when the database has multiple returns
  *
  * @param users
  */
final case class Users(users: immutable.Seq[User])

