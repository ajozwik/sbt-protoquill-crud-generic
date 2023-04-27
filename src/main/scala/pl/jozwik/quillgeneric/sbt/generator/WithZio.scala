package pl.jozwik.quillgeneric.sbt.generator

trait WithZio {
  protected def monad: String       = "QIO"
  protected def monadImport: String = s"import pl.jozwik.quillgeneric.zio.ZioJdbcRepository.$monad"
  protected def tryStart: String    = ""
  protected def tryEnd: String      = ""
}
