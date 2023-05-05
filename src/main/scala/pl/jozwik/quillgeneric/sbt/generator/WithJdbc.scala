package pl.jozwik.quillgeneric.sbt.generator

import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates.*

trait WithJdbc {
  protected def update         = "Long"
  protected def sqlIdiomImport = "import io.getquill.context.sql.idiom.SqlIdiom"

  protected def aliasGenericDeclaration =
    s"+${DialectTemplate} <: SqlIdiom, +${NamingTemplate} <: NamingStrategy, C <: ${ContextAlias}[${CodeGenerationTemplates.DialectTemplate}, ${CodeGenerationTemplates.NamingTemplate}]"
  protected def genericDeclaration = s"C, ${DialectTemplate}, ${NamingTemplate}"

  protected def contextTransactionStart = "inTransaction {"

  protected def contextTransactionEnd = "}"

  protected def createOrUpdate: String = s"""  override def createOrUpdate(entity: $BeanTemplate): $Monad[$BeanIdTemplate] =
                                   |    $ContextTransactionStart
                                   |      for {
                                   |        el <- ${TryStart}run(find(entity.id).updateValue(lift(entity)))$TryEnd
                                   |        id <- el match
                                   |          case 0 =>
                                   |            create(entity)
                                   |          case _ =>
                                   |            pure(entity.id)
                                   |      } yield {
                                   |        id
                                   |      }
                                   |   $ContextTransactionEnd""".stripMargin
}
