package pl.jozwik.quillgeneric.sbt

import sbt.*

object PluginHelper {

  def addImport(add: Boolean, module: String, version: String): Option[ModuleID] =
    if (add) {
      Option("com.github.ajozwik" %% module % version)
    } else {
      Option.empty[ModuleID]
    }

  def mkdirs(dir: File): Unit = {
    dir.mkdirs()
    if (!dir.isDirectory) {
      sys.error(s"${dir.getAbsolutePath} is not a directory")
    }
  }
}
