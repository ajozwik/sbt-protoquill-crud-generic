package pl.jozwik.quillgeneric.sbt.generator

trait WithTry {

  protected def monad: String       = "Try"
  protected def tryStart: String    = "Try {"
  protected def tryEnd: String      = "}"
}
