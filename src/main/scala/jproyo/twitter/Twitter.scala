package jproyo
package twitter
package logic

import scala.language.higherKinds
import algebra._
import scalaz._

package object program {


  def userTimeLine[F[_]: Monad: TimeLine: CacheTL](username: String): F[IList[UserTimeLine]] =
    OptionT(CacheTL.get(username)).getOrElseF(fromTwitter(username))

  private def fromTwitter[F[_]: Monad: TimeLine: CacheTL](username: String): F[IList[UserTimeLine]] =
    for{
      result <- TimeLine.get(username)
      _      <- CacheTL.set(username, result)
    } yield result

}

