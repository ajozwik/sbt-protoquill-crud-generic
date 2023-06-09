package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithDoobie, WithJdbc, WithNoTask }

object DoobieJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithDoobie with WithNoTask {
  protected val genericPackage                = "pl.jozwik.quillgeneric.doobie"
  protected val aliasName                     = "DoobieJdbcContextWithDateQuotes"
  protected val domainRepository: String      = "DoobieRepository "
  protected val domainRepositoryWithGenerated = "DoobieRepositoryWithTransactionWithGeneratedId"

  protected override def customImports: String =
    s"""import _root_.doobie.ConnectionIO
      |import pl.jozwik.quillgeneric.doobie.*
      |${super.customImports}
      |""".stripMargin
}
