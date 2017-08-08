package edu.jproyo.dojos.twitter.api

import akka.actor._
import akka.pattern.ask
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, ContentTypes}
import akka.http.scaladsl.model.StatusCodes._

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import io.circe.syntax._

trait MainController extends Directives with FailFastCirceSupport with CustomMessageJsonCodec{

  val serviceProxyApi: ActorRef

  val mainRoute =
    pathPrefix("twitter" / "api"){
      path("ping") {
        get {
          complete("PONG!")
        }
      } ~
      path(Segment / "tweets"){ username =>
        get {
          complete {
            200 -> (serviceProxyApi ? username).mapTo[TweetsResult]
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
