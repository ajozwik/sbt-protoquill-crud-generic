package pl.jozwik.quillgeneric.sbt.generator

import java.io.File

import pl.jozwik.quillgeneric.sbt.RepositoryDescription

trait Generator {
  protected def aliasGenericDeclaration: String
  protected def aliasName: String
  protected def customImports: String
  protected def domainRepository: String
  protected def domainRepositoryWithGenerated: String
  protected def genericDeclaration: String
  protected def genericPackage: String
  protected def importDomainTraitRepository: String
  protected def monad: String
  protected def sqlIdiomImport: String
  protected def tryEnd: String
  protected def tryStart: String

  def generate(rootPath: File)(description: RepositoryDescription): (File, String)
}
