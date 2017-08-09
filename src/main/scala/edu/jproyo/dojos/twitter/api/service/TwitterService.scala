package edu.jproyo.dojos.twitter.api.service

import scala.language.postfixOps
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.collection.immutable._

import edu.jproyo.dojos.twitter.api.TweetSimpl

trait TwitterService {
  def tweetsFor(username: String): Future[List[TweetSimpl]]
}

object TwitterService{

  implicit object TwitterServiceWithCache extends TwitterService {

    import edu.jproyo.dojos.twitter.api.adapter._
    import scalacache._
    import guava._

    implicit val scalaCache = ScalaCache(GuavaCache())
    lazy val twitter = TwitterAdapter

    def tweetsFor(username: String): Future[List[TweetSimpl]] = {
      cachingWithTTL(username)(30 seconds) {
        twitter.tweetsFor(username)
      }
    }

  }

}
