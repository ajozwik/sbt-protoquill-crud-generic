package pl.jozwik.example.monad.impl

import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.example.domain.repository.AddressRepository
import pl.jozwik.quillgeneric.monad.*
import java.time.LocalDateTime
import scala.util.Try
trait AddressRepositoryImpl[+Dialect <: SqlIdiom, +Naming <: NamingStrategy, C <: TryJdbcContextWithDateQuotes[Dialect, Naming]]
  extends TryJdbcRepositoryWithGeneratedId[AddressId, Address, C, Dialect, Naming]
  with AddressRepository[Try, Long] {
  import context.*
  override def setCountryIfCity(city: String, country: String): Try[Long] = {
    val now = LocalDateTime.now
    for {
      r <- Try {
        run {
          quoteQuery
            .filter(_.city == lift(city))
            .update(_.country -> lift(country), _.updated -> lift(Option(now)))
        }
      }
    } yield {
      r
    }
  }

}
