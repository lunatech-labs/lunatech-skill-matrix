package common

import slick.jdbc.{JdbcBackend, JdbcProfile}

trait DBConnectionProvider {
  def db: JdbcBackend#DatabaseDef
  def profile: JdbcProfile
}
