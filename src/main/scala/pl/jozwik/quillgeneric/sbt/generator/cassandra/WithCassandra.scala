package pl.jozwik.quillgeneric.sbt.generator.cassandra

import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates.*
trait WithCassandra {
  protected def update                     = "Unit"
  protected def contextTransactionStart    = ""
  protected def contextTransactionEnd      = ""
  protected def sqlIdiomImport             = ""
  protected def genericDeclaration: String = s"C, $NamingTemplate"
  protected def aliasGenericDeclaration =
    s"+$NamingTemplate <: NamingStrategy, C <: CassandraContextWithDateQuotes[$NamingTemplate]"

  protected def createOrUpdate: String =
    s"""  override def createOrUpdate(entity: $BeanTemplate): $Monad[$BeanIdTemplate] =
       |      for {
       |        el <- read(entity.id)
       |        id <- el match {
       |            case None =>
       |              create(entity)
       |            case _ =>
       |              update(entity)
       |            }
       |      } yield {
       |        entity.id
       |      }
       |""".stripMargin



}
