package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithDoobie, WithJdbc }

object DoobieJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithDoobie {
  protected def genericPackage                = "pl.jozwik.quillgeneric.doobie"
  protected def aliasName                     = "DoobieJdbcContextWithDateQuotes"
  protected def domainRepository: String      = "DoobieJdbcRepository"
  protected def domainRepositoryWithGenerated = "DoobieJdbcRepositoryWithTransactionWithGeneratedId"

  protected def customImports: String =
    """import _root_.doobie.ConnectionIO
      |import pl.jozwik.quillgeneric.doobie.*
      |
      |""".stripMargin
}
