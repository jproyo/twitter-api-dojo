package edu.jproyo.dojos.twitter.api.service

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import org.scalamock.scalatest.MockFactory
import org.scalatest._

import edu.jproyo.dojos.twitter.api.TweetSimpl

class ServiceTest extends WordSpec with Matchers with MockFactory {

  "TwitterService" when {
    "is called without cache" should {
      "called underlying implementation" in {
        val twitterStub = stub[edu.jproyo.dojos.twitter.api.adapter.Adapter]
        (twitterStub.tweetsFor _).when("someuser").returns(Future{List(TweetSimpl("", "one"), TweetSimpl("", "two"))})
        implicitly[TwitterService].tweetsFor("someuser")
      }
    }
    "is called with cache" should {
      "not call underlying implementation" in {
        val twitterStub = stub[edu.jproyo.dojos.twitter.api.adapter.Adapter]
        (twitterStub.tweetsFor _).when("somecacheduser").never
        import scalacache._
        import guava._
        implicit val scalaCache = ScalaCache(GuavaCache())
        put("somecacheduser")(Future[List[TweetSimpl]]{
          List(TweetSimpl("12/12/2012", "my new tweet"))
        })
        implicitly[TwitterService].tweetsFor("somecacheduser")
      }
    }
  }

}
