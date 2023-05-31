package pl.jozwik.example.doobie

import io.getquill.*
import pl.jozwik.example.domain.model.{Cell4d, Cell4dId}
import pl.jozwik.example.doobie.repository.Cell4dRepositoryGen
trait Cell4dSuite extends AbstractDoobieJdbcSpec {
  private implicit def schema: SchemaMeta[Cell4d] = schemaMeta("Cell4d", _.id.fk1 -> "X", _.id.fk2 -> "Y", _.id.fk3 -> "Z", _.id.fk4 -> "T")

  private lazy val repository = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
    "Call crud operations " in {
      val id     = Cell4dId(0, 1, 0, 1)
      val entity = Cell4d(id, false)
      repository.createOrUpdateAndRead(entity).runUnsafe() shouldBe entity
      repository.delete(id).runUnsafe() shouldBe 1

    }
  }
}
