/* Copyright (C) 2014 Foursquare Labs, Inc.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the EPFL nor the names of its contributors
 *      may be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.foursquare.common.async

import scala.async.internal.FutureSystem
import scala.concurrent.ExecutionContext
import scala.reflect.internal.SymbolTable

/**
 * Implementation of [[scala.async.internal.FutureSystem]] for twitter-util Future's.
 * For using scala-async with twitter-util/Finagle.
 */
object TwitterFutureSystem extends FutureSystem {
  type Prom[A] = com.twitter.util.Promise[A]
  type Fut[A] = com.twitter.util.Future[A]
  type ExecContext = ExecutionContext
  type Tryy[A] = com.twitter.util.Try[A]

  def mkOps(c: SymbolTable): Ops { val universe: c.type } = new Ops {
    val universe: c.type = c

    import universe._
    override def Expr[T: WeakTypeTag](tree: Tree): Expr[T] = universe.Expr[T](rootMirror, universe.FixedMirrorTreeCreator(rootMirror, tree))

    def promType[A: WeakTypeTag]: Type = weakTypeOf[com.twitter.util.Promise[A]]
    def tryType[A: WeakTypeTag]: Type = weakTypeOf[com.twitter.util.Try[A]]
    def execContextType: Type = weakTypeOf[ExecutionContext]

    def createProm[A: WeakTypeTag]: Expr[Prom[A]] = reify {
      com.twitter.util.Promise[A]()
    }

    def promiseToFuture[A: WeakTypeTag](prom: Expr[Prom[A]]): Expr[Fut[A]] = reify {
      prom.splice
    }

    def future[A: WeakTypeTag](a: Expr[A])(execContext: Expr[ExecContext]): Expr[Fut[A]] = reify {
      val promise = com.twitter.util.Promise[A]() 
      execContext.splice.prepare().execute(new Runnable {
        def run() {
          try {
            promise.setValue(a.splice)
          } catch {
            case ex: Exception =>
              promise.setException(ex)
          }
        }
      })
      promise
    }

    def onComplete[A, U](
        future: Expr[Fut[A]],
        fun: Expr[com.twitter.util.Try[A] => U],
        execContext: Expr[ExecContext]
    ): Expr[Unit] = reify {
      val responder = fun.splice
      future.splice.respond(tryValue => {
        execContext.splice.prepare().execute(new Runnable {
          def run() {
            responder(tryValue)
          }
        })
      })
      Expr[Unit](Literal(Constant(()))).splice
    }

    def completeProm[A](prom: Expr[Prom[A]], value: Expr[Tryy[A]]): Expr[Unit] = reify {
      prom.splice.update(value.splice)
      Expr[Unit](Literal(Constant(()))).splice
    }

    def tryyIsFailure[A](tryy: Expr[Tryy[A]]): Expr[Boolean] = reify {
      tryy.splice.isThrow
    }

    def tryyGet[A](tryy: Expr[Tryy[A]]): Expr[A] = reify {
      tryy.splice.get()
    }

    def tryySuccess[A: WeakTypeTag](a: Expr[A]): Expr[Tryy[A]] = reify {
      com.twitter.util.Return[A](a.splice)
    }

    def tryyFailure[A: WeakTypeTag](a: Expr[Throwable]): Expr[Tryy[A]] = reify {
      com.twitter.util.Throw[A](a.splice)
    }
  }
}

