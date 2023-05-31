package pl.jozwik.example.doobie

import io.getquill.*
import pl.jozwik.example.domain.model.{ Configuration, ConfigurationId }
import pl.jozwik.example.doobie.repository.ConfigurationRepositoryGen
trait ConfigurationSuite extends AbstractDoobieJdbcSpec {
  private implicit val configurationSchema: SchemaMeta[Configuration] = schemaMeta("CONFIGURATION", _.id -> "`KEY`", _.value -> "`VALUE`")

  private lazy val repository = new ConfigurationRepositoryGen(ctx)

  "Configuration " should {
    "Call all operation " in {
      logger.debug("configuration")
      val entity = Configuration(ConfigurationId("firstName"), "lastName")
      repository.all.runUnsafe() shouldBe Seq()
      val entityId         = repository.create(entity).runUnsafe()
      val entityIdProvided = entityId
      val createdEntity    = repository.read(entityIdProvided).runUnsafe().getOrElse(fail())
      repository.update(createdEntity).runUnsafe() shouldBe 1
      repository.all.runUnsafe() shouldBe Seq(createdEntity)
      val newValue = "newValue"
      val modified = createdEntity.copy(value = newValue)
      repository.update(modified).runUnsafe() shouldBe 1
      repository.read(createdEntity.id).runUnsafe().map(_.value) shouldBe Option(newValue)
      repository.delete(createdEntity.id).runUnsafe() shouldBe 1
      repository.read(createdEntity.id).runUnsafe() shouldBe empty
      repository.all.runUnsafe() shouldBe Seq()
    }
  }
}
