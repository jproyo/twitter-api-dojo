package jproyo
package twitter
package derivation

import scala.language.higherKinds
import algebra._
import scalaz._
import scalaz.zio._
import scalacache._
import guava._
import scala.language.postfixOps
import scala.concurrent.duration._
import edu.jproyo.dojos.twitter.api.adapter.TwitterAdapter._

object TimeLineIO{

  implicit val guavaCache = GuavaCache()

  implicit val timeLineWithTwitterLib = new TimeLine[IO[Nothing, ?]] {
    override def get(username: String): IO[Nothing, IList[UserTimeLine]] = IO.now(IList.empty)
  }

  implicit val cacheWithGuava = new CacheTL[IO[Nothing, ?]] {

    override def get(username: String): IO[Nothing, Option[IList[UserTimeLine]]] =
      IO.now(None)

    override def set(username: String, result: IList[UserTimeLine]): IO[Nothing, Unit] =
      IO.now(guavaCache.put[IList[UserTimeLine]](username, result, Some(5 seconds)))

  }

}
