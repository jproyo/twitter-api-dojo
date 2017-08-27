package edu.jproyo.dojos.twitter.api

import scala.language.postfixOps
import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import StatusCodes._
import Directives._

import com.typesafe.scalalogging.Logger
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.syntax._
import com.danielasfregola.twitter4s.exceptions.TwitterException


trait MainController extends Directives with FailFastCirceSupport with TwitterResultJsonCodec with TwitterExceptionJsonCodec{

  val logger = Logger[MainController]

  val serviceProxyApi: ActorRef

  import config._
  implicit val timeout: Timeout = Timeout(config().serviceTimeout)

  val twitterExceptionHandler = ExceptionHandler {
    case e: TwitterException =>
      extractUri { uri =>
        logger.error(s"Request to $uri could not be handled normally")
        complete(HttpResponse(e.code, entity = HttpEntity(ContentTypes.`application/json`, e.errors.asJson.toString)))
      }
    case _: Exception =>
      extractUri { uri =>
        logger.error(s"Request to $uri could not be handled normally")
        complete(HttpResponse(InternalServerError, entity = s"Unknown error for $uri"))
      }
  }

  val mainRoute =
    pathPrefix("twitter" / "api"){
      path("ping") {
        get {
          complete("PONG!")
        }
      } ~
      handleExceptions(twitterExceptionHandler) {
        path(Segment / "tweets"){ username =>
          get {
            complete { 200 -> (serviceProxyApi ? username).mapTo[TweetsResult] }
          }
        }
      } ~
      get {
        redirect("/twitter/api/ping", PermanentRedirect)
      }
    }~
    get {
      complete(index)
    }


  lazy val index = HttpEntity(
      ContentTypes.`text/html(UTF-8)`,
      <html>
        <body>
          <h1>Twitter <i>Prototype API</i>!</h1>
          <p>Defined resources:</p>
          <ul>
            <li><a href="/twitter/api/ping">/twitter/api/ping</a></li>
            <li><a href="/twitter/api/{username}/tweets">/twitter/api/&#123;username&#125;/tweets</a></li>
          </ul>
        </body>
      </html>.toString)


}
