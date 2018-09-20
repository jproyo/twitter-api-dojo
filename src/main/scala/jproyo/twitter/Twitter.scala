package jproyo
package twitter
package logic

import scala.language.higherKinds
import algebra._
import scalaz._
import scalaz.syntax.bind._

trait Twitter[F[_]]{
  def userTimeLine(username: String): F[IList[UserTimeLine]]
}

final class TwitterModule[F[_]: Monad](T: TimeLine[F], C: CacheTL[F])
  extends Twitter[F] {

  def userTimeLine(username: String): F[IList[UserTimeLine]] =
    OptionT(C.get(username)).getOrElseF(fromTwitter(username))

  private def fromTwitter(username: String): F[IList[UserTimeLine]] =
    for{
      result <- T.get(username)
      _      <- C.set(username, result)
    } yield result

}

