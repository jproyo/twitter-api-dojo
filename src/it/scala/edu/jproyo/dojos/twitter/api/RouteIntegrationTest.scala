package edu.jproyo.dojos.twitter.api

import org.scalatest.{ Matchers, WordSpec, BeforeAndAfterAll }
import akka.actor.{ActorSystem, Actor, Props}
import akka.testkit.{ ImplicitSender, TestActors, TestKit }
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import akka.http.scaladsl.model.{HttpEntity, ContentTypes, HttpMethods}
import StatusCodes._
import Directives._

import com.danielasfregola.twitter4s.exceptions._

import edu.jproyo.dojos.twitter.api.config._

class RouteIntegrationTest extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfterAll with MainController {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  class TwitterProxyApiMock extends Actor {
    def receive = {
      case msg: String if msg.endsWith("invalid") => sender ! akka.actor.Status.Failure(new TwitterException(NotFound, Errors(TwitterError("not found", 404))))
      case msg: String if msg.endsWith("valid") => sender ! akka.actor.Status.Success(TweetsResult(msg, List("one")))
      case _           => None
    }
  }

  val serviceProxyApi = system.actorOf(Props(new TwitterProxyApiMock))

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

    "response username TweetsResult on /twitter/api/{username}/tweets" in {
      Get("/twitter/api/someuservalid/tweets") ~> mainRoute ~> check {
        status shouldEqual StatusCodes.OK
        entityAs[TweetsResult] shouldEqual TweetsResult("someuservalid", List("one"))
      }
    }

    "response TwitterExeption on /twitter/api/{invalidusername}/tweets" in {
      Get("/twitter/api/someuserinvalid/tweets") ~> mainRoute ~> check {
        status shouldEqual StatusCodes.NotFound
        entityAs[Errors] shouldEqual Errors(TwitterError("not found", 404))
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
