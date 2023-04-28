package pl.jozwik.quillgeneric.sbt

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import DependencyHelper._
import pl.jozwik.quillgeneric.sbt.generator.jdbc.ZioJdbcCodeGenerator
import pl.jozwik.quillgeneric.sbt.generator.Generator

object QuillRepositoryPlugin extends AutoPlugin {

  override def trigger: sbt.PluginTrigger = allRequirements

  override def requires: sbt.Plugins = JvmPlugin

  object autoImport extends PluginKeys

  import autoImport._

  private def generate(descriptions: Seq[RepositoryDescription], rootPath: File, generator: Generator): Seq[File] =
    descriptions.map { d =>
      val (file, content) = generator.generate(rootPath)(d)
      IO.write(file, content)
      file
    }

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  override lazy val projectSettings: Seq[Def.Setting[_]] = {
    defaultSettings ++ Seq[Def.Setting[_]](
      Compile / sourceGenerators += Def.task {
        val rootPath = (Compile / sourceManaged).value
        generate(generateZioRepositories.value, rootPath, ZioJdbcCodeGenerator)
      }.taskValue,
      libraryDependencies ++= Seq.empty ++
        addImport(true, "repository", protoQuillGenericVersion.value) ++
        addImport(generateZioRepositories.value.nonEmpty, "quill-jdbc-zio", protoQuillGenericVersion.value)
    )
  }

}
