package pl.jozwik.example.zio.impl

import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.quillgeneric.zio.{ QIO, ZioJdbcContextWithDateQuotes }
import pl.jozwik.quillgeneric.zio.ZioJdbcRepositoryWithTransactionWithGeneratedId

import java.time.LocalDateTime
trait AddressRepositoryImpl[+Dialect <: SqlIdiom, +Naming <: NamingStrategy, C <: ZioJdbcContextWithDateQuotes[Dialect, Naming]]
  extends ZioJdbcRepositoryWithTransactionWithGeneratedId[AddressId, Address, C, Dialect, Naming]
  with AddressRepository[QIO] {
  import context.*
  override def setCountryIfCity(city: String, country: String): QIO[Long] = {
    val now = LocalDateTime.now
    for {
      r <- run {
        query[Address]
          .filter(_.city == lift(city))
          .update(_.country -> lift(country), _.updated -> lift(Option(now)))
      }
    } yield {
      r
    }
  }

}
