package pl.jozwik.quillgeneric.sbt

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.wordspec.AnyWordSpecLike

trait AbstractSpec extends AnyWordSpecLike with TimeLimitedTests with Matchers with StrictLogging {
  private val timeoutSeconds = 360
  val timeLimit: Span        = Span(timeoutSeconds, Seconds)
}
