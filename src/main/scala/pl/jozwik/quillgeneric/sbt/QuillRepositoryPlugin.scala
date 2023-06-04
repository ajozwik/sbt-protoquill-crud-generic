package pl.jozwik.quillgeneric.sbt

import sbt.Keys.*
import sbt.*
import sbt.plugins.JvmPlugin
import PluginHelper.*
import pl.jozwik.quillgeneric.sbt.generator.jdbc.*
import pl.jozwik.quillgeneric.sbt.generator.Generator
import pl.jozwik.quillgeneric.sbt.generator.cassandra.CassandraCodeGenerator

object QuillRepositoryPlugin extends AutoPlugin {

  override def trigger: sbt.PluginTrigger = allRequirements

  override def requires: sbt.Plugins = JvmPlugin

  object autoImport extends PluginKeys

  import autoImport.*

  private def generate(descriptions: Seq[RepositoryDescription], rootPath: File, generator: Generator): Seq[File] =
    descriptions.map { d =>
      val (file, content) = generator.generate(rootPath)(d)
      IO.write(file, content)
      file
    }

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.Option2Iterable"))
  override lazy val projectSettings: Seq[Def.Setting[?]] = {
    defaultSettings ++ Seq[Def.Setting[?]](
      Compile / sourceGenerators += Def.task {
        val rootPath = (Compile / sourceManaged).value
        generate(generateZioRepositories.value, rootPath, ZioJdbcCodeGenerator) ++
          generate(generateTryRepositories.value, rootPath, TryJdbcCodeGenerator) ++
          generate(generateDoobieRepositories.value, rootPath, DoobieJdbcCodeGenerator) ++
          generate(generateCassandraRepositories.value, rootPath, CassandraCodeGenerator)
      }.taskValue,
      libraryDependencies ++= Seq(
        addImport(true, "repository", protoQuillGenericVersion.value),
        addImport(generateTryRepositories.value.nonEmpty, "repository-jdbc-monad", protoQuillGenericVersion.value),
        addImport(generateZioRepositories.value.nonEmpty, "quill-jdbc-zio", protoQuillGenericVersion.value),
        addImport(generateDoobieRepositories.value.nonEmpty, "repository-doobie", protoQuillGenericVersion.value),
        addImport(generateCassandraRepositories.value.nonEmpty, "repository-cassandra", protoQuillGenericVersion.value)
      ).flatten
    )
  }

}
