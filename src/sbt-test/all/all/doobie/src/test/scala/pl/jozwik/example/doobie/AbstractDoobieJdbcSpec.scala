package pl.jozwik.example.doobie

import cats.effect.IO
import cats.effect.kernel.Resource
import cats.effect.unsafe.implicits.global
import doobie.h2.*
import doobie.implicits.*
import doobie.{ ConnectionIO, HC, Transactor }
import io.getquill.H2JdbcContext
import io.getquill.doobie.DoobieContext
import org.scalatest.{ BeforeAndAfterAll, TryValues }
import pl.jozwik.example.AbstractSpec

import scala.concurrent.ExecutionContext
import scala.util.Try

trait AbstractDoobieJdbcSpec extends AbstractSpec with BeforeAndAfterAll {

  import cats.effect.unsafe.implicits.global

  extension [T](task: ConnectionIO[T])
    def runUnsafe(): T = transactor
      .use { xa =>
        task.transact(xa)
      }
      .unsafeRunSync()

  private lazy val transactor: Resource[IO, H2Transactor[IO]] =
    H2Transactor.newH2Transactor[IO](
      "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:scripts/create.sql'",
      "sa",
      "",
      ExecutionContext.global
    )

  lazy protected val ctx = new DoobieContext.H2(strategy)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
