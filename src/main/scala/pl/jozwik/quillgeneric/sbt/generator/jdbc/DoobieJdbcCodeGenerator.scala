package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithDoobie, WithJdbc }

object DoobieJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithDoobie {
  protected val genericPackage                = "pl.jozwik.quillgeneric.doobie"
  protected val aliasName                     = "DoobieJdbcContextWithDateQuotes"
  protected val domainRepository: String      = "DoobieRepository "
  protected val domainRepositoryWithGenerated = "DoobieRepositoryWithTransactionWithGeneratedId"

  protected def customImports: String =
    """import _root_.doobie.ConnectionIO
      |import pl.jozwik.quillgeneric.doobie.*
      |
      |""".stripMargin
}
