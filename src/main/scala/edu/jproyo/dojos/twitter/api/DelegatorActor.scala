package edu.jproyo.dojos.twitter.api

import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import akka.actor._
import akka.actor.Status.Failure
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout

import com.typesafe.scalalogging.Logger

import io.circe.syntax._

import edu.jproyo.dojos.twitter.api.service._

class DelegatorActor(implicit service: TwitterService) extends Actor with TwitterResultJsonCodec{

	val logger = Logger[DelegatorActor]

	import config._
	implicit val timeout = Timeout(config().serviceTimeout)

	override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3) {
      case e: Exception =>
        logger.info(s"Producer exception caught. Restarting. ${e.getMessage}")
        SupervisorStrategy.Restart
      case _ =>
        SupervisorStrategy.Escalate
    }

	def receive = {

		case username: String => {
			logger.info(s"Tweets for user $username")
			service.tweetsFor(username).map(TweetsResult(username,_)) pipeTo sender
		}

	}


}
