package pl.jozwik.quillgeneric.sbt.generator

trait WithDoobie {
  protected def monad: String    = "ConnectionIO"
  protected def tryStart: String = ""
  protected def tryEnd: String   = ""
}
