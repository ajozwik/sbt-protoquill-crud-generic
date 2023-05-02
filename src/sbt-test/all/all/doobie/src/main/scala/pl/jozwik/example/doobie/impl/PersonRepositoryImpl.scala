package pl.jozwik.example.doobie.impl

import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.extras.LocalDateOps
import pl.jozwik.example.domain.model.{ Person, PersonId }
import pl.jozwik.example.domain.repository.PersonRepository
import _root_.doobie.ConnectionIO
import pl.jozwik.quillgeneric.doobie.*

import java.time.LocalDate
trait PersonRepositoryImpl[+Dialect <: SqlIdiom, +Naming <: NamingStrategy, C <: DoobieJdbcContextWithDateQuotes[Dialect, Naming]]
  extends DoobieJdbcRepositoryWithTransactionWithGeneratedId[PersonId, Person, C, Dialect, Naming]
  with PersonRepository[ConnectionIO] {
  import context.*
  def searchByFirstName(name: String)(offset: Int, limit: Int): ConnectionIO[Seq[Person]] = {
    val q = quoteQuery.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(lift(offset)).take(lift(limit))
    for {
      l <- run(q)
    } yield {
      l
    }
  }

  def maxBirthDate: ConnectionIO[Option[LocalDate]] = {
    val r = quoteQuery.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): ConnectionIO[Seq[Person]] = {
    val q = quoteQuery.filter(_.birthDate > lift(date)).drop(lift(offset)).take(lift(limit))
    for {
      l <- run(q)
    } yield {
      l
    }
  }

  def count: ConnectionIO[Long] =
    val q = quoteQuery.size
    run(q)

}
