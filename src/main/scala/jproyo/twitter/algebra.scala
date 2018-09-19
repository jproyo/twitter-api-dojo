package jproyo
package twitter
package algebra

import scala.language.higherKinds
import scalaz._

final case class UserTimeLine(val date: String, val text: String)

object TimeLine {
  def apply[F[_]](implicit F: TimeLine[F]): TimeLine[F] = F

  def get[F[_]](username: String)(implicit F: TimeLine[F]): F[IList[UserTimeLine]] = TimeLine[F].get(username)
}


trait TimeLine[F[_]] {
  def get(username: String): F[IList[UserTimeLine]]
}

object CacheTL {
  def apply[F[_]](implicit F: CacheTL[F]): CacheTL[F] = F

  def get[F[_]](username: String)(implicit F: CacheTL[F]): F[Option[IList[UserTimeLine]]] =
    CacheTL[F].get(username)

  def set[F[_]](username: String, result: IList[UserTimeLine])(implicit F: CacheTL[F]): F[Unit] =
    CacheTL[F].set(username, result)
}

trait CacheTL[F[_]]{
  def get(username: String): F[Option[IList[UserTimeLine]]]
  def set(username: String, result: IList[UserTimeLine]): F[Unit]
}



