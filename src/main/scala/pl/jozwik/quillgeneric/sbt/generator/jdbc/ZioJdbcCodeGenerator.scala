package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithZio }

object ZioJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithZio {
  protected def genericPackage                = "pl.jozwik.quillgeneric.zio"
  protected def aliasName                     = "ZioJdbcContextWithDateQuotes"
  protected def domainRepository: String      = "ZioJdbcRepository"
  protected def repositoryCompositeKey        = "ZioJdbcRepositoryCompositeKey"
  protected def domainRepositoryWithGenerated = "ZioJdbcRepositoryWithTransactionWithGeneratedId"

  protected def customImports: String = """import io.getquill.context.qzio.ZioJdbcContext
                                |import pl.jozwik.quillgeneric.zio.*
                                |import zio.interop.catz.*""".stripMargin
}
