package pl.jozwik.quillgeneric.sbt.generator

trait WithZio {
  protected val monad: String       = "Task"
  protected val monadImport: String = s"import pl.jozwik.quillgeneric.zio.ZioJdbcRepository.$monad"
  protected val tryStart: String    = ""
  protected val tryEnd: String      = ""

  protected val toTask: String =
    """
      |      toTask {""".stripMargin

  protected val toTaskEnd: String =
    """ }
      |    """.stripMargin

}
