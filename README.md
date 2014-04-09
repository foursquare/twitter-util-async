# Scala Async for Twitter Util Library

twitter-util-async adapts the [scala-async library](https://github.com/scala/async), which only operates with
the Scala implementation of `Future`, to operate with `com.twitter.util.Future`
in the [Twitter util library](https://github.com/twitter/util).

## Quick Start

Just add the following dependency to your SBT configuration:

    libraryDependencies += "com.foursquare" %% "twitter-util-async" % "0.4-SNAPSHOT"

See the documentation for [scala-async](https://github.com/scala/async) for information on how to use `async`
and `await`. The main difference is that you must import `com.foursquare.common.async.Async.{async, await}`
instead of `scala.async.Async.{async, await}`. (And obviously, `com.twitter.util.Future` will be returned
(and expected) instead of `scala.concurrent.Future`.
