package pl.jozwik.example.monad

import io.getquill.H2JdbcContext
import org.scalatest.{ BeforeAndAfterAll, TryValues }
import pl.jozwik.example.AbstractSpec

import scala.util.{ Failure, Success, Try }

trait AbstractTryJdbcSpec extends AbstractSpec with BeforeAndAfterAll with TryValues {

  extension [T](task: Try[T])
    def runUnsafe(): T =
      task match {
        case Success(value) =>
          value
        case Failure(exception) =>
          logger.error("", exception)
          fail(exception)
      }

  lazy protected val ctx = new H2JdbcContext(strategy, TryHelperSpec.pool)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
