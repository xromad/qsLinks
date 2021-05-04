import java.text.SimpleDateFormat

import com.bjs.model.JsonFormats._
import com.bjs.model.PermissionLevel
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.slf4j.{Logger, LoggerFactory}
import spray.json.{JsString, JsValue, _}

class JsonFormatsTest extends AnyFlatSpec
  with Matchers
  with DefaultJsonProtocol {
  val logger: Logger = LoggerFactory.getLogger(this.getClass.getName)

  "DateJsonConverter.read" should "parse json" in {
    val dateString = "2021-05-04T08:25:26.618-0400"
    val jsonDateString = dateString.toJson
    val myDate = dateJsonFormat.read(jsonDateString)
    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    myDate == sdf.parse("2021-05-04T08:25:26.618-0400") shouldBe true
  }

  "DateJsonConverter.write" should "create json" in {
    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val myDate = sdf.parse("2021-05-04T08:25:26.618-0400")
    val jsonDate: JsString = dateJsonFormat.write(myDate)
    jsonDate.toString shouldBe "\"2021-05-04T08:25:26.618-0400\""

  }

  "EnumJsonConverter.read" should "parse json" in {

  }

  "EnumJsonConverter.write" should "create json" in {
    val myEnum = PermissionLevel.ADMIN
    val jsonPermissionLevel: JsValue = enumJsonFormat.write(myEnum)
    logger.debug("BJS *** " + jsonPermissionLevel)
    jsonPermissionLevel.toString shouldBe "\"ADMIN\""
  }

}
