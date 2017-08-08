package edu.jproyo.dojos.twitter.api.config

import scala.language.postfixOps
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory
import configs.Configs
import configs.syntax._

object Configuration {

  private lazy val _config = ConfigFactory.load()

  def config = _config.getOrElse[TwitterApiConfig]("twitter-app", TwitterApiConfig(5 seconds)).value

}

case class TwitterApiConfig(serviceTimeout: FiniteDuration)
