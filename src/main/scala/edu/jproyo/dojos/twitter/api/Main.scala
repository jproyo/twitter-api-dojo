package edu.jproyo.dojos.twitter.api

import scala.language.postfixOps
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor._
import akka.routing.RoundRobinPool
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import com.typesafe.scalalogging.Logger

object WebApp extends MainController with TwitterResultJsonCodec{

  override val logger = Logger(this.getClass.getPackage.getName)

  implicit val system = ActorSystem("twitter-api-system")

  val serviceProxyApi = system.actorOf(RoundRobinPool(10).props(Props(new DelegatorActor)), name = "twitter-proxy-service")

  def main(args: Array[String]) {

    implicit val materializer = ActorMaterializer()

    val srvAddrr = "0.0.0.0"
    val bindingFuture = Http().bindAndHandle(mainRoute, s"$srvAddrr", 8080)
    logger.info(s"Server online at http://$srvAddrr:8080/\nPress Ctrl+D to stop...")
    scala.sys.addShutdownHook {
      logger.info("Finishing Actor System")
      system.terminate()
      Await.result(system.whenTerminated, 30 seconds)
      logger.info("Actor System Finished")
    }
  }
}
