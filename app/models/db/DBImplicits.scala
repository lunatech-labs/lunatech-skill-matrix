package models.db

import org.joda.time.DateTime
import slick.driver.PostgresDriver.api._

object DBImplicits {
  implicit val dateTimeMappedColumn = MappedColumnType.base[DateTime, String](
    e => e.toString,
    s => DateTime.parse(s)
  )
}
