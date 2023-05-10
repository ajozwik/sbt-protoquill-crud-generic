package pl.jozwik.example.zio.impl

import java.time.LocalDate
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository
import pl.jozwik.quillgeneric.zio.*
import io.getquill.*
import io.getquill.extras.LocalDateOps
import zio.Task
trait PersonRepositoryImpl[+Dialect <: SqlIdiom, +Naming <: NamingStrategy, C <: ZioJdbcContextWithDateQuotes[Dialect, Naming]]
  extends ZioJdbcRepositoryWithGeneratedId[PersonId, Person, C, Dialect, Naming]
  with PersonRepository[Task, Long] {
  import context.*
  def searchByFirstName(name: String)(offset: Int, limit: Int): Task[Seq[Person]] = {
    val q = quoteQuery.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(lift(offset)).take(lift(limit))
    run(q)
  }

  def maxBirthDate: Task[Option[LocalDate]] = {
    val r = quoteQuery.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Task[Seq[Person]] = {
    val q = quoteQuery.filter(_.birthDate > lift(date)).drop(lift(offset)).take(lift(limit))
    run(q)
  }

  def count: Task[Long] =
    val q = quoteQuery.size
    run(q)

}
