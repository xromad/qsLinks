package com.bjs.model

/**
  * ADMIN = see/access anything
  * USER = see/access what you own
  * GUEST = see/access only public
  */

object PermissionLevel extends Enumeration {
  type PermissionLevel = Value
  val ADMIN, USER, GUEST = Value
}

