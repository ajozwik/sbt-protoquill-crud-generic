package pl.jozwik.quillgeneric.sbt

import java.io.File

class HelperSpec extends AbstractSpec {
  "PluginHelper" should {
    "Throw exception" in {
      val file = new File(baseTempPath, "fake")
      file.createNewFile()
      intercept[RuntimeException] {
        PluginHelper.mkdirs(file)
      }
    }
  }
}
