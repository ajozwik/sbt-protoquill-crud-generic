package pl.jozwik.quillgeneric.sbt.generator

import java.io.File

import pl.jozwik.quillgeneric.sbt.RepositoryDescription

trait Generator {
  protected def aliasGenericDeclaration: String
  protected def aliasName: String

  protected def createOrUpdate: String
  protected def contextTransactionEnd: String
  protected def contextTransactionStart: String
  protected def customImports: String
  protected def domainRepository: String
  protected def domainRepositoryWithGenerated: String
  protected def genericDeclaration: String
  protected def genericPackage: String
  protected def importDomainTraitRepository: String
  protected def monad: String
  protected def sqlIdiomImport: String
  protected def toTask: String
  protected def toTaskEnd: String
  protected def tryEnd: String
  protected def tryStart: String

  protected def update: String
  protected def updateResult: String

  def generate(rootPath: File)(description: RepositoryDescription): (File, String)
}
