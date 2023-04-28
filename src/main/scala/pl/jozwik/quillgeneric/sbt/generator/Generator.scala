package pl.jozwik.quillgeneric.sbt.generator

import java.io.File

import pl.jozwik.quillgeneric.sbt.RepositoryDescription

trait Generator {
  protected def aliasName: String
  protected def domainRepository: String
  protected def domainRepositoryWithGenerated: String
  protected def genericPackage: String

  protected def genericDeclaration: String
  protected def importDomainTraitRepository: String
  protected def sqlIdiomImport: String

  protected def aliasGenericDeclaration: String

  protected def customImports: String

  def generate(rootPath: File)(description: RepositoryDescription): (File, String)
}
