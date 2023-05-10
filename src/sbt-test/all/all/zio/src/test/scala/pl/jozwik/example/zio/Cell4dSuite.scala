package pl.jozwik.example.zio

import pl.jozwik.example.domain.model.{ Cell4d, Cell4dId }
import pl.jozwik.example.zio.repository.Cell4dRepositoryGen
import pl.jozwik.quillgeneric.repository.Repository
import zio.Task
import io.getquill.*
trait Cell4dSuite extends AbstractZioJdbcSpec {
  private implicit def schema: SchemaMeta[Cell4d] = schemaMeta("Cell4d", _.id.fk1 -> "X", _.id.fk2 -> "Y", _.id.fk3 -> "Z", _.id.fk4 -> "T")

  private lazy val repository: Repository[Task, Cell4dId, Cell4d, Long] = new Cell4dRepositoryGen(ctx)

  "Cell4dSuite " should {
    "Call crud operations " in {
      val id     = Cell4dId(0, 1, 0, 1)
      val entity = Cell4d(id, false)
      repository.createOrUpdateAndRead(entity).runSyncUnsafe() shouldBe entity
      repository.delete(id).runSyncUnsafe() shouldBe 1

    }
  }
}
