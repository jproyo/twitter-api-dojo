package edu.jproyo.dojos.twitter.api

import org.scalatest.{ Matchers, WordSpec }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import akka.http.scaladsl.model.{HttpEntity, ContentTypes, HttpMethods}
import Directives._

class RouteIntegrationTest extends WordSpec with Matchers with ScalatestRouteTest with MainController{

  "API Endpoin" should {

    "return HTML documentation on root path" in {
      Get() ~> mainRoute ~> check {
        status shouldEqual StatusCodes.OK
        contentType shouldEqual ContentTypes.`text/html(UTF-8)`
      }
    }

    "redirect on not found /twitter/api or /twitter/api/unknown" in {
      Get("/twitter/api") ~> mainRoute ~> check {
        status shouldEqual StatusCodes.PermanentRedirect
      }
      Get("/twitter/api/something") ~> mainRoute ~> check {
        status shouldEqual StatusCodes.PermanentRedirect
      }
      Get("/twitter/api/something/other/and/other") ~> mainRoute ~> check {
        status shouldEqual StatusCodes.PermanentRedirect
      }
    }

    "response PONG! on ping" in {
      Get("/twitter/api/ping") ~> mainRoute ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual "PONG!"
      }
    }

    "response username mirror on /twitter/api/{username}/tweets" in {
      Get("/twitter/api/someuser/tweets") ~> mainRoute ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[String] shouldEqual "someuser"
      }
    }

    "method not allowed on other than Get" in {
      Post("/twitter/api") ~> mainRoute ~> check {
        rejection shouldEqual MethodRejection(HttpMethods.GET)
      }
      Put("/twitter/api") ~> mainRoute ~> check {
        rejection shouldEqual MethodRejection(HttpMethods.GET)
      }
      Delete("/twitter/api") ~> mainRoute ~> check {
        rejection shouldEqual MethodRejection(HttpMethods.GET)
      }
    }

  }

}
