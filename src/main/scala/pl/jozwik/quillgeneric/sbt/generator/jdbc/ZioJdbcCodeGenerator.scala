package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithZio }

object ZioJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithZio {
  protected def genericPackage                = "pl.jozwik.quillgeneric.zio"
  protected def aliasName                     = "ZioJdbcContextWithDateQuotes"
  protected def domainRepository: String      = "ZioJdbcRepository"
  protected def domainRepositoryWithGenerated = "ZioJdbcRepositoryWithGeneratedId"

  protected override def customImports: String = s"""import pl.jozwik.quillgeneric.zio.*
                                |import zio.interop.catz.*
                                |import zio.Task
                                |${super.customImports}""".stripMargin
}
