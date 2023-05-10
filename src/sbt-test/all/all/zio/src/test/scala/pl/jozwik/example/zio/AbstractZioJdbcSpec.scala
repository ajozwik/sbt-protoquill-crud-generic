package pl.jozwik.example.zio

import io.getquill.H2ZioJdbcContext
import io.getquill.jdbczio.Quill
import org.scalatest.BeforeAndAfterAll
import zio.Task
import pl.jozwik.example.{AbstractSpec, PoolHelper}
import zio.{Tag, Unsafe, ZEnvironment, ZLayer}

import javax.sql.DataSource

object ZioHelperSpec {
  val pool: DataSource = runLayerUnsafe(Quill.DataSource.fromPrefix(PoolHelper.PoolName))

  def runLayerUnsafe[T: Tag](layer: ZLayer[Any, Throwable, T]): T =
    zio.Unsafe.unsafe { implicit unsafe =>
      zio.Runtime.default.unsafe.run(zio.Scope.global.extend(layer.build)).getOrThrow()
    }.get
}

private trait AbstractZioJdbcSpec extends AbstractSpec with BeforeAndAfterAll {

  sys.props.put("quill.macro.log", false.toString)
  sys.props.put("quill.binds.log", true.toString)
  extension [T](qzio: Task[T]) def runSyncUnsafe() = unsafe(qzio)

  protected def unsafe[T](qzio: Task[T]): T =
    Unsafe.unsafe { implicit unsafe =>
      val io = qzio.provideEnvironment(ZEnvironment(ZioHelperSpec.pool))
      zio.Runtime.default.unsafe.run(io).getOrThrow()
    }

  lazy protected val ctx = new H2ZioJdbcContext(strategy)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
