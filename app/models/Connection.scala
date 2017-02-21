package models

import slick.driver.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import play.api.Play

object Connection{

  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db
}