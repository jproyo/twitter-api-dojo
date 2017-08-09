package edu.jproyo.dojos.twitter.api

import scala.util.{Success, Failure}
import scala.language.postfixOps
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import akka.http.scaladsl.model.StatusCodes

import org.scalatest.{ Matchers, WordSpec, ParallelTestExecution }

import com.danielasfregola.twitter4s.exceptions._

import edu.jproyo.dojos.twitter.api.adapter._

class AdapterIntegrationTest extends WordSpec with Matchers with ParallelTestExecution {

  "Twitter Adapter Integration Test" should {

    "return 10 results on known user" in {
      Await.result(TwitterAdapter.tweetsFor("juanpabloroyo"), 5 seconds) should have size 10
    }

    "return Failure for unknown user" in {
      try{
        Await.result(TwitterAdapter.tweetsFor("fdfasdf324234"), 5 seconds)
        fail
      } catch {
        case e: TwitterException => e.code shouldEqual StatusCodes.NotFound
      }
    }

  }

}
