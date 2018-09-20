package jproyo
package twitter

import scala.language.higherKinds
import scalaz._


package object algebra{

  final case class UserTimeLine(val date: String, val text: String)

  trait TimeLine[F[_]] {
    def get(username: String): F[IList[UserTimeLine]]
  }

  trait CacheTL[F[_]]{
    def get(username: String): F[Option[IList[UserTimeLine]]]
    def set(username: String, result: IList[UserTimeLine]): F[Unit]
  }
}






