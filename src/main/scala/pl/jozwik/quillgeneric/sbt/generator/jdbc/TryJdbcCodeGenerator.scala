package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithNoTask, WithTry }

object TryJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithTry with WithNoTask {
  protected def genericPackage                = "pl.jozwik.quillgeneric.monad"
  protected def aliasName                     = "TryJdbcContextWithDateQuotes"
  protected def domainRepository: String      = "TryJdbcRepository"
  protected def domainRepositoryWithGenerated = "TryJdbcRepositoryWithGeneratedId"

  protected override def customImports: String = s"""import scala.util.Try
                                |import pl.jozwik.quillgeneric.monad.*
                                |import cats.implicits.*
                                |${super.customImports}""".stripMargin
}
