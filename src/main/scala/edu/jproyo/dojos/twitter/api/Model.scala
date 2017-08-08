package edu.jproyo.dojos.twitter.api

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

case class TweetsResult(val username: String, val tweets: List[String])

trait TwitterResultJsonCodec {
  implicit val messageDecoder: Decoder[TweetsResult] = deriveDecoder
  implicit val messageEncoder: Encoder[TweetsResult] = deriveEncoder
}
