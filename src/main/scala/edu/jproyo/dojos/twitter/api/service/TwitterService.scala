package edu.jproyo.dojos.twitter.api.service

import scala.language.postfixOps
import scala.collection.immutable._

import edu.jproyo.dojos.twitter.api.TweetsResult

trait TwitterService {
  def tweetsFor(username: String): TweetsResult
}

object TwitterService{

  implicit object TwitterServiceDefault extends TwitterService {

    def tweetsFor(username: String): TweetsResult = {
      TweetsResult(username, (0 until 10).foldRight(List.empty[String]){ (acc, elem) => s"Message $acc" :: elem } )
    }

  }
  
}
