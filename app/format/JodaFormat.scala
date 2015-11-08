package format

import org.joda.time._
import org.joda.time.format._
import anorm._
import play.api.libs.json._

/**
 * @author Ziguang Xu
 */

object JodaFormat {
  val dateFormatGeneration: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmssSS")

  /* Import this to extract DateTime objects from the DB */
  implicit def rowToDateTime: Column[DateTime] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case ts: java.sql.Timestamp => Right(new DateTime(ts.getTime))
      case d: java.sql.Date => Right(new DateTime(d.getTime))
      case str: java.lang.String => Right(dateFormatGeneration.parseDateTime(str))
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass))
    }
  }

  /* Import this to save DateTime objects into the DB */
  implicit val dateTimeToStatement = new ToStatement[DateTime] {
    def set(s: java.sql.PreparedStatement, index: Int, aValue: DateTime): Unit = {
      s.setTimestamp(index, new java.sql.Timestamp(aValue.getMillis()))
    }
  }

  implicit object JodaDateTimeFormat extends Format[DateTime] {
    override def writes(o: DateTime): JsValue = JsNumber(o.getMillis)
    override def reads(json: JsValue): JsResult[DateTime] = JsSuccess(new DateTime(json.as[Long]))
  }
}
