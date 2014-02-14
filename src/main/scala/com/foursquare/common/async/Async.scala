/* Copyright (C) 2012-2014 EPFL
 * Copyright (C) 2012-2014 Typesafe, Inc.
 * Copyright (C) 2014 Foursquare Labs, Inc.
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

import com.twitter.util.Future
import scala.concurrent.ExecutionContext
import scala.language.experimental.macros
import scala.reflect.internal.annotations.compileTimeOnly

object Async {

  /**
   * Run the block of code `body` asynchronously. `body` may contain calls to `await` when the results of
   * a `Future` are needed; this is translated into non-blocking code.
   */
  def async[T](body: T)(implicit execContext: ExecutionContext): Future[T] = macro TwitterAsyncImpl.asyncImpl[T]

  /**
   * Run the block of code `body` asynchronously. `body` may contain calls to `await` when the results of
   * a `Future` are needed; this is translated into non-blocking code.
   */
  def async[T](execContext: ExecutionContext)(body: T): Future[T] = macro TwitterAsyncImpl.asyncImplExplicit[T]

  /**
   * Non-blocking await on the result of `awaitable`. This may only be used directly within an enclosing `async` block.
   *
   * Internally, this will register the remainder of the code in enclosing `async` block as a callback
   * in the `onComplete` handler of `awaitable`, and will *not* block a thread.
   */
  @compileTimeOnly("`await` must be enclosed in an `async` block")
  def await[T](awaitable: Future[T]): T = ??? // No implementation here, as calls to this are translated to `onComplete` by the macro.
}
