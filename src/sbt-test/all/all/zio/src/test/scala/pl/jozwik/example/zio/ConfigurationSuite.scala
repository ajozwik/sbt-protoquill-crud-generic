package pl.jozwik.example.zio

import io.getquill.*
import pl.jozwik.example.domain.model.{ Configuration, ConfigurationId }
import pl.jozwik.example.zio.repository.ConfigurationRepositoryGen
import pl.jozwik.quillgeneric.repository.Repository
import pl.jozwik.quillgeneric.zio.QIO
trait ConfigurationSuite extends AbstractZioJdbcSpec {
  private implicit val configurationSchema: SchemaMeta[Configuration] = schemaMeta("CONFIGURATION", _.id -> "`KEY`", _.value -> "`VALUE`")

  private lazy val repository: Repository[QIO, ConfigurationId, Configuration, Long] = new ConfigurationRepositoryGen(ctx)

  "Configuration " should {
    "Call all operation " in {
      logger.debug("configuration")
      val entity = Configuration(ConfigurationId("firstName"), "lastName")
      repository.all.runSyncUnsafe() shouldBe Seq()
      val entityId         = repository.create(entity).runSyncUnsafe()
      val entityIdProvided = entityId
      val createdEntity    = repository.read(entityIdProvided).runSyncUnsafe().getOrElse(fail())
      repository.update(createdEntity).runSyncUnsafe() shouldBe 1
      repository.all.runSyncUnsafe() shouldBe Seq(createdEntity)
      val newValue = "newValue"
      val modified = createdEntity.copy(value = newValue)
      repository.update(modified).runSyncUnsafe() shouldBe 1
      repository.read(createdEntity.id).runSyncUnsafe().map(_.value) shouldBe Option(newValue)
      repository.delete(createdEntity.id).runSyncUnsafe() shouldBe 1
      repository.read(createdEntity.id).runSyncUnsafe() shouldBe empty
      repository.all.runSyncUnsafe() shouldBe Seq()
    }
  }
}
