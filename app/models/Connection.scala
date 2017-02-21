package models

import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider

object Connection{
  val db = Database.forURL("jdbc:postgresql://localhost:5432/skillmatrix?user=postgres&password=root", driver="org.postgresql.Driver")
}