package jproyo.twitter

import logic.program._
import scalaz.zio._

object Main extends App {

  override def main(args: Array[String]): Unit = {

    import derivation.TimeLineIO._

    for {
      result <- userTimeLine("juanpabloroyo")
      _ <- println(result)
    } yield ()
  }

}
