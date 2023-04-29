package pl.jozwik.example.monad

import io.getquill.*
import pl.jozwik.example.domain.model.{ Configuration, ConfigurationId }
import pl.jozwik.example.monad.repository.ConfigurationRepositoryGen

trait ConfigurationSuite extends AbstractTryJdbcSpec {

  private implicit val meta: SchemaMeta[Configuration] = schemaMeta[Configuration]("CONFIGURATION", _.id -> "`KEY`", _.value -> "`VALUE`")
  private lazy val repository                          = new ConfigurationRepositoryGen(ctx)

  "ConfigurationRepository " should {
    "All is empty" in {
      repository.all.runUnsafe() shouldBe empty
    }

    "Call all operations on Configuration" in {

      val configuration = Configuration(ConfigurationId("key"), "value")
      val task = repository
        .inTransaction {
          for {
            _      <- repository.create(configuration)
            actual <- repository.readUnsafe(configuration.id)
          } yield {
            actual shouldBe configuration
          }

        }
      task.runUnsafe()

      repository.createOrUpdateAndRead(configuration).runUnsafe() shouldBe configuration
    }
  }
}
