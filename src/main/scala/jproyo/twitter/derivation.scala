package jproyo
package twitter
package derivation

import scala.language.higherKinds
import algebra._
import com.danielasfregola.twitter4s.http.clients.rest.statuses.parameters.UserTimelineParameters
import scalaz._
import scalaz.Id._
import scalacache._
import guava._

import scala.language.postfixOps
import scala.concurrent.duration._
import edu.jproyo.dojos.twitter.api.adapter.TwitterAdapter._

import scala.concurrent.Await

object TimeLineIO{

  implicit val guavaCache = GuavaCache()


  implicit val timeLineWithTwitterLib = new TimeLine[Id] {
    override def get(username: String): Id[IList[UserTimeLine]] =
      Id.id.point(IList.fromList(Await.result(tweetsFor(username), 5 seconds).map(t => UserTimeLine(t.date, t.text))))
  }

  implicit val cacheWithGuava = new CacheTL[Id] {

    override def get(username: String): Id[Option[IList[UserTimeLine]]] =
      Id.id.point(Await.result(guavaCache.get(username), 5 seconds))

    override def set(username: String, result: IList[UserTimeLine]): Id[Unit] =
      Id.id.point(guavaCache.put[IList[UserTimeLine]](username, result, Some(5 seconds)))

  }

}
