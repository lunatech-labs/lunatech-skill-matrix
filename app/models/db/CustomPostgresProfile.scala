package models.db

import com.github.tminglei.slickpg._
import models.AccessLevel

trait CustomPostgresProfile extends ExPostgresProfile with PgArraySupport with PgPlayJsonSupport with PgDateSupportJoda {
  def pgjson = "json"
  override val api = MyAPI

  object MyAPI extends API with ArrayImplicits with JsonImplicits with JodaDateTimeImplicits
  {
   // implicit val strListTypeMapper: DriverJdbcType[List[String]] = new SimpleArrayJdbcType[String]("text").to(_.toList)

    implicit val accessLevelMappedColumnSeq = MappedColumnType.base[List[AccessLevel], List[String]](
      e => e.map{_.toString},
      s => s.map{AccessLevel(_)}
    )
  }
}

object CustomPostgresProfile extends CustomPostgresProfile