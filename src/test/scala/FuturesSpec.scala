import java.util.concurrent.TimeUnit

import org.specs2.mutable.Specification

import scala.concurrent.{Await, Future}

class FuturesSpec extends Specification {

  "A Single Future" should {
    "execute" in {

      // Create a future
      val future = Future {
        "My " + "First " + "Future."
      }

      future onSuccess {
        case s  => println(s)
      }

      val res =
        Await.result(future, scala.concurrent.duration.Duration(1, TimeUnit.SECONDS))

      res mustEqual "My First Future."
    }
  }

  "Futures fa, fb, and fc" should {
    "execute sequentially one after the other" in {

      val futures = for {
        a <- Future { 10 }
        b <- Future(a * 2)
        c <- Future(a + 2)
        if c > 10
      } yield c
      futures foreach println

      val res =
        Await.result(futures, scala.concurrent.duration.Duration(1, TimeUnit.SECONDS))

      res mustEqual 12
    }
  }

  "Futures fb and fc" should {
    "execute currently after fa complete" in {

      val fa = Future { 10 }
      val futures = fa.flatMap { a =>
        val fb = Future(a * 2)
        val fc = Future(a + 2)
        for {
          b <- fb
          c <- fc
          if c > 10
        } yield c
      }
      futures foreach println

      val res =
        Await.result(futures, scala.concurrent.duration.Duration(1, TimeUnit.SECONDS))

      res mustEqual 12
    }
  }
}
