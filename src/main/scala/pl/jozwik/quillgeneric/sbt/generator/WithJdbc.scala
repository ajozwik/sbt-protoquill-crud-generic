package pl.jozwik.quillgeneric.sbt.generator

import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates.*

trait WithJdbc {
  protected def update                  = "Long"
  protected def contextTransactionStart = "context.transaction {"
  protected def contextTransactionEnd   = "}"
  protected def sqlIdiomImport          = "import io.getquill.context.sql.idiom.SqlIdiom"

  protected def aliasGenericDeclaration =
    s"+${DialectTemplate} <: SqlIdiom, +${NamingTemplate} <: NamingStrategy, C <: ${ContextAlias}[${CodeGenerationTemplates.DialectTemplate}, ${CodeGenerationTemplates.NamingTemplate}]"
  protected def genericDeclaration    = s"C, ${DialectTemplate}, ${NamingTemplate}"
  protected def createOrUpdate        = "createOrUpdate"
  protected def createOrUpdateAndRead = "createOrUpdateAndRead"
}
