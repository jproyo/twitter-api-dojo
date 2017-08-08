package edu.jproyo.dojos.twitter.api

import scala.language.postfixOps
import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor._
import akka.actor.Status.Failure
import akka.pattern.ask
import akka.util.Timeout

import com.typesafe.scalalogging.Logger

import io.circe.syntax._

import edu.jproyo.dojos.twitter.api.config._
import edu.jproyo.dojos.twitter.api.service._

class TwitterActorService(implicit twitterService: TwitterService) extends Actor with TwitterResultJsonCodec{

	val logger = Logger[TwitterActorService]

	implicit val timeout = Timeout(Configuration().serviceTimeout)

	override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3) {
      case e: Exception =>
        logger.info(s"Producer exception caught. Restarting. ${e.getMessage}")
        SupervisorStrategy.Restart
      case _ =>
        SupervisorStrategy.Escalate
    }

	def receive = {

		case username: String => {
			// val newMessage = message.copy(key = Some(UUID.randomUUID.toString))
			// val received = (producerActor ? Message.queue(newMessage, "test-queue")).mapTo[ConfirmResponse]
			// val result = Await.result(received, timeout.duration)
			logger.info(s"Tweets for user $username")
			sender ! twitterService.tweetsFor(username)
		}

	}


}
