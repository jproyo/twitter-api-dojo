package edu.jproyo.dojos.twitter.api

import scala.io.StdIn
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor._
import akka.routing.RoundRobinPool
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import com.typesafe.scalalogging.Logger

import io.circe.syntax._
import io.circe.parser.decode

object WebApp extends MainController with TwitterResultJsonCodec{

  val logger = Logger(this.getClass.getPackage.getName)

  implicit val system = ActorSystem("twitter-api-system")

  val serviceProxyApi = system.actorOf(RoundRobinPool(10).props(Props(new TwitterActorService)), name = "twitter-proxy-service")

  def main(args: Array[String]) {

    implicit val materializer = ActorMaterializer()

    val srvAddrr = "0.0.0.0"
    val bindingFuture = Http().bindAndHandle(mainRoute, s"$srvAddrr", 8080)
    logger.info(s"Server online at http://$srvAddrr:8080/\nPress Ctrl+D to stop...")
    while(StdIn.readLine() != null){}
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => {
        system.terminate()
        System.exit(0)
      })
  }
}
