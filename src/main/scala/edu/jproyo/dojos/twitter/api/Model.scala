package edu.jproyo.dojos.twitter.api

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

case class CustomMessage(val key: Option[String], val delay: Option[Int], val content: String)

trait CustomMessageJsonCodec {
  implicit val messageDecoder: Decoder[CustomMessage] = deriveDecoder
  implicit val messageEncoder: Encoder[CustomMessage] = deriveEncoder
}
