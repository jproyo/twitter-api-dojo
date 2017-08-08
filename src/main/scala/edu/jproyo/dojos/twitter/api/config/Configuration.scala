package edu.jproyo.dojos.twitter.api.config

import scala.language.postfixOps
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import configs.Configs
import configs.syntax._

case class TwitterApiConfig(serviceTimeout: FiniteDuration = 5 seconds)

object Configuration {

  def apply(configStr: Option[String] = None): TwitterApiConfig = {
    configStr.fold(ConfigFactory.load())(ConfigFactory.parseString(_)).getOrElse[TwitterApiConfig]("twitter-app", TwitterApiConfig()).value
  }

}
