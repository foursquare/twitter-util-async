# Scala Async for Twitter Util Library

twitter-util-async adapts the [scala-async library](https://github.com/scala/async), which only operates with
`scala.concurrent.Future`, to operate with `com.twitter.util.Future` in the
[Twitter util library](https://github.com/twitter/util).

## Quick Start

Just add the following dependency to your SBT configuration:

    libraryDependencies += "com.foursquare" %% "twitter-util-async" % "1.0.0"

See the documentation for [scala-async](https://github.com/scala/async) for information on how to use `async`
and `await`. The main difference is that you must import `com.foursquare.common.async.Async.{async, await}`
instead of `scala.async.Async.{async, await}`. (Also, obviously, `async` returns, and `await` expects, a
`com.twitter.util.Future` instead of a `scala.concurrent.Future`.
