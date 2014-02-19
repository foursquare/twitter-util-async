// Copyright 2011 Foursquare Labs Inc. All Rights Reserved.

package com.foursquare.common.async

import com.foursquare.common.async.Async.{async, await}
import org.junit.{Assert => T, Test}
import com.twitter.util.{Await, Duration, Future}
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global

class TwitterFutureSystemTest {
  @Test
  def testBasicAsync() {
    val f1: Future[Int] = Future.value(5)
    val f2: Future[Int] = Future.value(10)
    val f3 = async {
      val v1 = await(f1)
      val v2 = await(f2)
      v1 + v2
    }
    T.assertEquals(Await.result(f3, Duration(2, TimeUnit.SECONDS)), 15)
  }

  @Test
  def testExplicitAsync() {
    val f1: Future[Int] = Future.value(5)
    val f2: Future[Int] = Future.value(10)
    val f3 = async(global) {
      val v1 = await(f1)
      val v2 = await(f2)
      v1 + v2
    }
    T.assertEquals(Await.result(f3, Duration(2, TimeUnit.SECONDS)), 15)
  }

  @Test
  def testExceptions() {
    val f1: Future[Int] = Future.exception(new IllegalArgumentException("foo"))
    val f2 = async {
      val v = await(f1)
      2 * v
    }
    try {
      Await.result(f2, Duration(2, TimeUnit.SECONDS))
      T.fail("should have thrown an exception")
    } catch {
      case _: IllegalArgumentException => // pass
      case _: Exception => T.fail("unexpected exception")
    }
  }
}
