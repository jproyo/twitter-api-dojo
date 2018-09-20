package jproyo.twitter

import logic._
import scalaz.Id._

object MyApp extends App {

  import derivation.TimeLineIO._

  val module = new TwitterModule[Id](timeLineWithTwitterLib, cacheWithGuava)
  module.userTimeLine("juanpabloroyo").toList.foreach(println(_))


}
