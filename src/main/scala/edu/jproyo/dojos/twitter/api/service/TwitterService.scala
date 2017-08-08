package edu.jproyo.dojos.twitter.api.service

import scala.language.postfixOps
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.collection.immutable._

import edu.jproyo.dojos.twitter.api.TweetsResult
import edu.jproyo.dojos.twitter.api.adapter.Adapter

trait TwitterService {
  val adapter: Adapter
  def tweetsFor(username: String): Future[List[String]]
}

object TwitterService{

  implicit object TwitterServiceWithCache extends TwitterService {

    import edu.jproyo.dojos.twitter.api.adapter.TwitterAdapter
    import scalacache._
    import guava._

    val adapter = TwitterAdapter
    implicit val scalaCache = ScalaCache(GuavaCache())

    def tweetsFor(username: String): Future[List[String]] = {
      cachingWithTTL(username)(30 seconds) {
        adapter.tweetsFor(username)
      }
    }

  }

}
