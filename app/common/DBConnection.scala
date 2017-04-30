package common

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend

@Singleton
class DBConnection @Inject() (dbConfigProvider: DatabaseConfigProvider) {
  val db: JdbcBackend#DatabaseDef = dbConfigProvider.get[JdbcProfile].db
}