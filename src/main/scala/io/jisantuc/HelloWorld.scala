package io.jisantuc

import cats.effect.IO

object HelloWorld {

  def say(): IO[String] = IO.delay("Hello Cats!")
}
