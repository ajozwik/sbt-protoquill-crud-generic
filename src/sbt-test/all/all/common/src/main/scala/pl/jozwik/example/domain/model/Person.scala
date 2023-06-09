package pl.jozwik.example.domain.model

import java.time.LocalDate

import pl.jozwik.quillgeneric.repository.WithId

object PersonId {
  val empty: PersonId = PersonId(0)
}

final case class PersonId(value: Int) extends AnyVal

final case class Person(id: PersonId, firstName: String, lastName: String, birthDate: LocalDate, addressId: Option[AddressId] = None) extends WithId[PersonId]
