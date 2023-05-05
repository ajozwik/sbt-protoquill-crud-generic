package pl.jozwik.example.doobie.impl

import doobie.ConnectionIO
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.quillgeneric.doobie.*

import java.time.LocalDateTime
trait AddressRepositoryImpl[+Dialect <: SqlIdiom, +Naming <: NamingStrategy, C <: DoobieJdbcContextWithDateQuotes[Dialect, Naming]]
  extends DoobieRepositoryWithTransactionWithGeneratedId[AddressId, Address, C, Dialect, Naming]
  with AddressRepository[ConnectionIO, Long] {
  import context.*
  override def setCountryIfCity(city: String, country: String): ConnectionIO[Long] = {
    val now = LocalDateTime.now
    for {
      r <-
        run {
          query[Address]
            .filter(_.city == lift(city))
            .update(_.country -> lift(country), _.updated -> lift(Option(now)))
        }

    } yield {
      r
    }
  }

}
