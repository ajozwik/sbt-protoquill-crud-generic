package pl.jozwik.example.monad

import io.getquill.H2JdbcContext
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.AbstractSpec

trait AbstractTryJdbcSpec extends AbstractSpec with BeforeAndAfterAll {

  lazy protected val ctx = new H2JdbcContext(strategy, TryHelperSpec.pool)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
