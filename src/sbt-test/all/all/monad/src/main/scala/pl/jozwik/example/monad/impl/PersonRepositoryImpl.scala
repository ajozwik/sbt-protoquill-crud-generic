package pl.jozwik.example.monad.impl

import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.extras.LocalDateOps
import pl.jozwik.quillgeneric.monad.*
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository

import java.time.LocalDate
import scala.util.Try
trait PersonRepositoryImpl[+Dialect <: SqlIdiom, +Naming <: NamingStrategy, C <: TryJdbcContextWithDateQuotes[Dialect, Naming]]
  extends TryJdbcRepositoryWithGeneratedId[PersonId, Person, C, Dialect, Naming]
  with PersonRepository[Try, Long] {
  import context.*
  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]] = {
    val q = quoteQuery.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(lift(offset)).take(lift(limit))
    Try { run(q) }
  }

  def maxBirthDate: Try[Option[LocalDate]] = {
    val r = quoteQuery.map(p => p.birthDate)
    Try { run(r.max) }
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Try[Seq[Person]] = {
    val q = quoteQuery.filter(_.birthDate > lift(date)).drop(lift(offset)).take(lift(limit))
    Try { run(q) }
  }

  def count: Try[Long] =
    val q = quoteQuery.size
    Try { run(q) }

}
