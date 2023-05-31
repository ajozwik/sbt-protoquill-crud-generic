package pl.jozwik.quillgeneric.sbt.generator

import java.io.File
import java.nio.file.Paths
import pl.jozwik.quillgeneric.sbt.RepositoryDescription
import sbt.*

import scala.io.{ Codec, Source }
import scala.util.Using
abstract class AbstractCodeGenerator extends Generator with CodeGenerationTemplates {
  private val Dialect                            = "Dialect"
  private val Naming                             = "Naming"
  protected def template                         = "$template$.txt"
  protected def templateWithGeneratedId          = "$template_generate_id$.txt"
  protected def importDomainTraitRepository      = s"import $genericPackage.$domainRepository.$ContextAlias"
  private def repositoryWithGeneratedWithGeneric = s"$domainRepositoryWithGenerated[$BeanIdTemplate, $BeanTemplate, C, $DialectTemplate, $NamingTemplate]"
  private def repositoryWithGeneratedImport      = s"import $genericPackage.$domainRepositoryWithGenerated"

  private val headerFile = "$header$.txt"

  private val header: String = readTemplate(headerFile)

  def generate(rootPath: File)(description: RepositoryDescription): (File, String) = {
    import description.*
    val templateFile = chooseTemplate(generateId)
    val content      = readTemplate(templateFile)
    val path         = Paths.get(rootPath.getAbsolutePath, packageName*)
    val dir          = path.toFile
    mkdirs(dir)
    val pName = toPackageName(packageName)
    val file  = dir / s"$repositorySimpleClassName.scala"
    val (repositoryTraitSimpleClassName, repositoryImport, defaultRepositoryImport) =
      toRepositoryTraitImport(repositoryTrait, packageName, repositoryPackageName, repositoryTraitSimpleClassNameOpt, generateId)
    val genericContent = toGenericContent(content)
    val findByKey      = toFindByKey(description.beanIdClass.keyLength)
    val result = genericContent
      .replace(RepositoryTraitSimpleClassName, repositoryTraitSimpleClassName)
      .replace(RepositoryTraitImport, repositoryImport)
      .replace(PackageTemplate, pName)
      .replace(RepositoryClassTemplate, repositorySimpleClassName)
      .replace(BeanTemplate, beanSimpleClassName)
      .replace(BeanClassImport, createImport(packageName, beanPackageName, beanClass))
      .replace(BeanIdTemplate, beanIdSimpleClassName)
      .replace(BeanIdClassImport, createImport(packageName, beanIdPackageName, beanIdClass.name))
      .replace(ContextTransactionStart, contextTransactionStart)
      .replace(ContextTransactionEnd, contextTransactionEnd)
      .replace(RepositoryImport, defaultRepositoryImport)
      .replace(UpdateResult, updateResult)
      .replace(DialectTemplate, Dialect)
      .replace(Monad, monad)
      .replace(NamingTemplate, Naming)
      .replace(ContextAlias, aliasName)
      .replace(SqlIdiomImport, sqlIdiomImport)
      .replace(CustomImports, customImports)
      .replace(FindByKey, findByKey)
      .replace(TryStart, tryStart)
      .replace(TryEnd, tryEnd)
    (file, s"$header\n$result")
  }

  private def toGenericContent(content: String) =
    content
      .replace(Update, update)
      .replace(CreateOrUpdate, createOrUpdate)
      .replace(ToTask, toTask)
      .replace(ToTaskEnd, toTaskEnd)
      .replace(AliasGenericDeclaration, aliasGenericDeclaration)
      .replace(RepositoryDomainTraitImport, importDomainTraitRepository)

  private def toPackageName(packageName: Seq[String]): String =
    packageName match {
      case Seq() =>
        ""
      case _ =>
        s"""package ${packageName.mkString(".")}"""
    }

  private def toRepositoryTraitImport(
      repositoryTrait: Option[String],
      packageName: Seq[String],
      repositoryPackageName: Seq[String],
      repositoryTraitSimpleClassNameOpt: String,
      generateId: Boolean
  ) =
    if (repositoryTraitSimpleClassNameOpt.isEmpty) {
      val (repository, repositoryImport) = if (generateId) {
        (repositoryWithGeneratedWithGeneric, repositoryWithGeneratedImport)
      } else {
        repositoryWithGeneric
      }
      (s"$repository", "", repositoryImport)

    } else {
      val clazzName = repositoryTrait.getOrElse("")
      val index     = clazzName.indexOf('[')
      val withoutGeneric = index match {
        case -1 =>
          clazzName
        case _ =>
          clazzName.substring(0, index)
      }
      val imp = createImport(packageName, repositoryPackageName, withoutGeneric)
      (s"$repositoryTraitSimpleClassNameOpt", imp, "")
    }

  private def createImport(packageNameSeq: Seq[String], packageNameBean: Seq[String], className: String) =
    if (packageNameSeq.sameElements(packageNameBean)) { "" }
    else { s"import $className" }

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private def readTemplate(templateResource: String): String = {
    val input = Option(getClass.getClassLoader.getResourceAsStream(templateResource))
      .getOrElse(getClass.getClassLoader.getResourceAsStream(s"/$templateResource"))
    Using.resource(input) { i =>
      Source.fromInputStream(i)(Codec.UTF8).mkString
    }
  }

  private def mkdirs(dir: File): Unit =
    if (!dir.mkdirs() && !dir.isDirectory) {
      sys.error(s"${dir.getAbsolutePath} can not be created")
    }

  private def toFindByKey(keyLength: Option[Byte]) = {
    keyLength match {
      case Some(l) if l > 1 =>
        createFindByKey(l)
      case _ =>
        "filter(_.id == lift(id))"
    }
  }

  private def createFindByKey(l: Byte) =
    (1 to l)
      .map { i =>
        findBy(i)
      }
      .mkString(".")

  private def findBy(key: Int) = s"filter(_.id.fk$key == lift(id.fk$key))"

  private def repositoryWithGeneric =
    (s"$domainRepository[$BeanIdTemplate, $BeanTemplate, $genericDeclaration]", s"import $genericPackage.$domainRepository")

  private def chooseTemplate(generateId: Boolean): String =
    if (generateId) {
      templateWithGeneratedId
    } else {
      template
    }
}
