// package edu.jproyo.dojos.twitter.api
//
// import java.util.UUID
//
// import scala.language.postfixOps
// import scala.util.Properties
// import scala.concurrent.Await
// import scala.concurrent.duration._
//
// import akka.actor._
// import akka.actor.Status.Failure
// import akka.pattern.ask
// import akka.util.Timeout
//
// import com.typesafe.scalalogging.Logger
//
// import io.circe.syntax._
//
// import com.spingo.op_rabbit.{RabbitControl,Message}
// import com.spingo.op_rabbit.Message._
// import com.spingo.op_rabbit.CirceSupport._
// import com.spingo.op_rabbit.properties._
//
//
// class EnQueueMessageActor extends Actor with CustomMessageJsonCodec{
//
// 	val logger = Logger[EnQueueMessageActor]
//
// 	val producerActor = context.actorOf(Props[RabbitControl])
//
// 	implicit val timeout = Timeout(5 seconds)
//
// 	override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3) {
//       case e: Exception =>
//         logger.info(s"Producer exception caught. Restarting consumer. ${e.getMessage}")
//         SupervisorStrategy.Restart
//       case _ =>
//         SupervisorStrategy.Escalate
//     }
//
// 	def receive = {
//
// 		case message: CustomMessage => {
// 			val newMessage = message.copy(key = Some(UUID.randomUUID.toString))
// 			val received = (producerActor ? Message.queue(newMessage, "test-queue")).mapTo[ConfirmResponse]
// 			val result = Await.result(received, timeout.duration)
// 			logger.info(s"Response $result")
// 		}
//
// 	}
//
//
// }
