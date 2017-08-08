package edu.jproyo.dojos.twitter.api.config

import scala.language.postfixOps
import scala.concurrent.duration._

import org.scalatest._

class ConfigurationTest extends WordSpec with Matchers {

  "Configuration" when {
    "has correctvalues" should {
      "return 1 second configured on test resources application.conf" in {
        Configuration().serviceTimeout shouldEqual (1 seconds)
      }
    }
    "default values" should {
      "return 3 second configured on by params" in {
        Configuration(Some("""twitter-app { service-timeout: 3 seconds }""")).serviceTimeout shouldEqual (3 seconds)
      }
      "return 1 second configured with None" in {
        Configuration(None).serviceTimeout shouldEqual (1 seconds)
      }
    }
  }

}
