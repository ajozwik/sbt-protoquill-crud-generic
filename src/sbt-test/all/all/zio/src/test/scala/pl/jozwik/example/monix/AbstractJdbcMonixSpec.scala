package pl.jozwik.example.monix

import io.getquill.H2ZioJdbcContext
import monix.execution.Scheduler
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.example.AbstractSpec
import pl.jozwik.example.monix.repository.AddressRepositoryGen
import pl.jozwik.quillgeneric.quillmacro.monix.ZioWithContextDateQuotes.ZioWithContextLong

trait AbstractJdbcZioSpec extends AbstractSpec with BeforeAndAfterAll {
  protected implicit val scheduler: Scheduler = monix.execution.Scheduler.Implicits.global

  lazy protected val ctx               = new H2ZioJdbcContext(strategy, "h2Zio") with ZioWithContextLong
  protected lazy val addressRepository = new AddressRepositoryGen(ctx)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }
}
