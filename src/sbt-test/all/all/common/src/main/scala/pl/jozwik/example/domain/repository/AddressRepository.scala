package pl.jozwik.example.domain.repository

import pl.jozwik.example.domain.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.repository.RepositoryWithGeneratedId

trait AddressRepository[F[_], UP] extends RepositoryWithGeneratedId[F, AddressId, Address, UP] {
  def setCountryIfCity(city: String, country: String): F[UP]
}
