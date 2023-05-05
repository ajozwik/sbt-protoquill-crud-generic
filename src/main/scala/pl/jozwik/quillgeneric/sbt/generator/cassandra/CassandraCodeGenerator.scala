package pl.jozwik.quillgeneric.sbt.generator.cassandra

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithTry }

object CassandraCodeGenerator extends AbstractCodeGenerator with WithCassandra with WithTry {
  protected def genericPackage = "pl.jozwik.quillgeneric.cassandra"
  protected def aliasName      = "CassandraContextDateQuotes"

  override protected def customImports: String =
    """import scala.util.Try
      |import pl.jozwik.quillgeneric.cassandra.*
      |""".stripMargin

  override protected def domainRepository: String = "CassandraRepositoryTry"

  override protected def domainRepositoryWithGenerated: String = "CassandraRepositoryTry"
}
