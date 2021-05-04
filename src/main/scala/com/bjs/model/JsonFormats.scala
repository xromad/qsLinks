package com.bjs.model

import java.text.SimpleDateFormat
import java.util.Date

import com.bjs.registry.UserRegistry.ActionPerformed
import com.typesafe.config.{Config, ConfigFactory}
import spray.json.{DefaultJsonProtocol, _}

import scala.util.Try

object JsonFormats extends DefaultJsonProtocol {

  // order is important: think small to large
  implicit val enumJsonFormat: EnumJsonConverter[PermissionLevel.type] = new EnumJsonConverter(PermissionLevel)
  implicit val dateJsonFormat: DateJsonConverter = new DateJsonConverter
  implicit val linkJsonFormat: RootJsonFormat[Link] = jsonFormat6(Link)
  implicit val categoryJsonFormat: RootJsonFormat[Category] = jsonFormat5(Category)
  implicit val catalogJsonFormat: RootJsonFormat[Catalog] = jsonFormat5(Catalog)
  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat4(User)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users)
  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)
}

// spray does not have a date formatter
class DateJsonConverter extends RootJsonFormat[Date] {

  lazy val dateFormatString = ConfigFactory.load().getString("my-app.conversions.dateFormatString")

  private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
    override def initialValue() = new SimpleDateFormat(dateFormatString)
  }

  def write(date: Date) = JsString(dateToIsoString(date))

  private def dateToIsoString(date: Date): String =
    localIsoDateFormatter.get().format(date)

  def read(json: JsValue): Date = json match {
    case JsString(rawDate) =>
      parseIsoDateString(rawDate)
        .fold(deserializationError(s"Expected ISO Date format, got $rawDate"))(identity)
    case error => deserializationError(s"Expected JsString, got $error")
  }

  private def parseIsoDateString(date: String): Option[Date] =
    Try {
      localIsoDateFormatter.get().parse(date)
    }.toOption
}

// spray does not have a generic enum formatter
class EnumJsonConverter[T <: scala.Enumeration](enu: T) extends RootJsonFormat[T#Value] {
  override def write(obj: T#Value): JsValue = JsString(obj.toString)

  override def read(json: JsValue): T#Value = {
    json match {
      case JsString(txt) => enu.withName(txt)
      case somethingElse => throw DeserializationException(s"Expected a value from enum $enu instead of $somethingElse")
    }
  }
}
