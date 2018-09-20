package edu.jproyo.dojos.twitter.api.adapter

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, RatedData, Tweet}
import edu.jproyo.dojos.twitter.api.TweetSimpl

trait Adapter {
  def tweetsFor(username: String): Future[List[TweetSimpl]]
}

object TwitterAdapter extends Adapter{

  lazy val restClient = TwitterRestClient(ConsumerToken("z1M5MjcLwrc2FUyFe0aTPmnpd", "qa9Qis0VNtthW9E8OGUzkttuWu6dOvMoUOJ8NvRfwlL1xjxso5"), AccessToken("75272994-eq6KgSTAijzhwATBrxGv2TeiaR6kLqhSUMlmki2B7", "TIC7h7N7r0X9ekU7Qq3FSCmXjB11TSigtxPunQcGyenjn"))

  def tweetsFor(username: String): Future[List[TweetSimpl]] = {
     _tweets(username).map{ rated => rated.data.map(r => TweetSimpl(r.created_at.toString,r.text)).toList }
  }

  def _tweets(username: String): Future[RatedData[Seq[Tweet]]] = {
    restClient.userTimelineForUser(screen_name = username, count = 10)
  }

}
