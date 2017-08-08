package edu.jproyo.dojos.twitter.api.config

import scala.language.postfixOps
import scala.concurrent.duration._

import org.scalatest._

class ConfigurationTest extends WordSpec with Matchers {

  "Configuration" when {
    "has correctvalues" should {
      "return 1 second configured on test resources application.conf" in {
        Configuration.config.serviceTimeout shouldEqual (1 seconds)
      }
    }
  }

}
