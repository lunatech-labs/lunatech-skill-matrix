package models

import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

object Connection {

  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db
}