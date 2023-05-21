package pl.jozwik.example

import java.time.{ LocalDate, LocalDateTime }
import java.time.temporal.ChronoUnit
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.TryValues
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.wordspec.AnyWordSpecLike
import pl.jozwik.example.domain.model.AddressId

import scala.util.{ Failure, Success, Try }

trait AbstractSpec extends AnyWordSpecLike with TimeLimitedTests with Matchers with StrictLogging with TryValues {
  val TIMEOUT_SECONDS              = 60
  val timeLimit                    = Span(TIMEOUT_SECONDS, Seconds)
  protected val now: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
  protected val today: LocalDate   = now.toLocalDate
  protected val strategy           = Strategy.namingStrategy

  protected val (offset, limit) = (0, 100)
  protected val generateId      = true
  protected val addressId       = AddressId(1)

  extension [T](task: Try[T])
    def runUnsafe() = task match {
      case Success(s) =>
        s
      case Failure(th) =>
        throw th
    }
}
