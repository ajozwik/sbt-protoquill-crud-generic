package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithTry }

object TryJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithTry {
  protected def genericPackage                = "pl.jozwik.quillgeneric.monad"
  protected def aliasName                     = "TryJdbcContextWithDateQuotes"
  protected def domainRepository: String      = "TryJdbcRepository"
  protected def domainRepositoryWithGenerated = "TryJdbcRepositoryWithGeneratedId"

  protected def customImports: String = """import scala.util.Try
                                |import pl.jozwik.quillgeneric.monad.*
                                |import cats.implicits.*""".stripMargin
}
