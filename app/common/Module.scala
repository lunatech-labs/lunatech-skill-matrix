package common

import com.google.inject.AbstractModule
import com.typesafe.config.{Config, ConfigFactory}

class Module extends AbstractModule {
  def configure() = {
    bind(classOf[Config]).toInstance(ConfigFactory.load())
  }
}
