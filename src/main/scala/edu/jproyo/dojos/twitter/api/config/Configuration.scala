package edu.jproyo.dojos.twitter.api.config

import scala.language.postfixOps
import scala.concurrent.duration._

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import configs.Configs
import configs.syntax._

case class TwitterApiConfig(val serviceTimeout: FiniteDuration = 5 seconds, val cacheTweetsByUserTTL: FiniteDuration = 30 seconds)

object config {

  def apply(configStr: Option[String] = None): TwitterApiConfig = {
    configStr.fold(ConfigFactory.load())(ConfigFactory.parseString(_)).getOrElse[TwitterApiConfig]("twitter-app", TwitterApiConfig()).value
  }

}
