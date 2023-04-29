package pl.jozwik.quillgeneric.sbt

import sbt.{ Def, settingKey }

trait PluginKeys {
  val generateZioRepositories  = settingKey[Seq[RepositoryDescription]]("Zio repositories descriptions")
  val generateTryRepositories  = settingKey[Seq[RepositoryDescription]]("Try repositories descriptions")
  val protoQuillGenericVersion = settingKey[String]("Quill generic version")

  val defaultSettings: Seq[Def.Setting[?]] =
    Seq(
      generateZioRepositories  := Seq.empty[RepositoryDescription],
      generateTryRepositories  := Seq.empty[RepositoryDescription],
      protoQuillGenericVersion := "0.4"
    )
}
