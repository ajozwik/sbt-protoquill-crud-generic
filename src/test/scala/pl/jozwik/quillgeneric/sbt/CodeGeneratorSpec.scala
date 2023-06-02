package pl.jozwik.quillgeneric.sbt

import pl.jozwik.quillgeneric.sbt.generator.Generator
import pl.jozwik.quillgeneric.sbt.generator.cassandra.CassandraCodeGenerator
import pl.jozwik.quillgeneric.sbt.generator.jdbc.{ TryJdbcCodeGenerator, ZioJdbcCodeGenerator }

import java.io.File

class ZioGeneratorCodeSpec extends AbstractCodeGeneratorSpec(ZioJdbcCodeGenerator)

class TryGeneratorCodeSpec extends AbstractCodeGeneratorSpec(TryJdbcCodeGenerator)

class CassandraGeneratorCodeSpec extends AbstractCodeGeneratorSpec(CassandraCodeGenerator, false)

abstract class AbstractCodeGeneratorSpec(generator: Generator, generatedId: Boolean = true) extends AbstractSpec {

  private val generic: String = "[Dialect, Naming]"
  private def baseTempPath    = new File("target")
  private val modelPackage    = "pl.jozwik.quillgeneric.model"
  private val implPackage     = "pl.jozwik.quillgeneric.zio.repository"
  private val genPackage      = s"$implPackage.gen"
  "Generator " should {
    "Generate code for Person" in {
      val description =
        RepositoryDescription(s"${modelPackage}.Person", BeanIdClass(s"${modelPackage}.PersonId"), s"${implPackage}.PersonRepositoryJdbc", generatedId)
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
    }
    "Custom mapping " in {

      val description = RepositoryDescription(
        "pl.jozwik.model.Person",
        BeanIdClass("pl.jozwik.model.PersonId"),
        "pl.jozwik.repository.PersonRepository",
        generatedId,
        Option(s"pl.jozwik.quillgeneric.sbt.MyPersonRepository$generic")
      )
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)

    }

    "Generate code for Configuration" in {
      val description = RepositoryDescription(
        s"${modelPackage}.Configuration",
        BeanIdClass(s"${modelPackage}.ConfigurationId"),
        s"$genPackage.ConfigurationRepositoryJdbc",
        !generatedId,
        Option(s"$implPackage.MyConfigurationRepositoryJdbc")
      )
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
    }

    "Generate code for Sale" in {
      val description =
        RepositoryDescription(s"${modelPackage}.Sale", BeanIdClass(s"${modelPackage}.SaleId", Option(2)), s"${implPackage}.SaleRepository")
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
    }

    "Without package" in {
      val description =
        RepositoryDescription(s"${modelPackage}.Sale", BeanIdClass(s"${modelPackage}.SaleId", Option(2)), "SaleRepository")
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
    }
  }

  private def generateAndLog(description: RepositoryDescription) = {
    val (file, content) = generator.generate(baseTempPath)(description)
    logger.debug(s"$file")
    logger.debug(s"\n$content")
    (file, content)
  }
}
