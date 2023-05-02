package pl.jozwik.example.doobie

import io.getquill.*
import pl.jozwik.example.domain.model.{Address, AddressId}
import pl.jozwik.example.doobie.repository.AddressRepositoryGen
trait AddressSuite extends AbstractDoobieJdbcSpec {
  private lazy val addressRepository = new AddressRepositoryGen(ctx)(schemaMeta[Address]("ADDRESS"))

  "Address " should {
      "Batch update address " in {
        val city = "Rakow"
        val address = Address(AddressId.empty, "Lechia", city, Option("Listopadowa"))
        val id      = addressRepository.create(address).runUnsafe()
        id should not be AddressId.empty
        addressRepository.setCountryIfCity(city, "Poland").runUnsafe() shouldBe 1
        addressRepository.delete(id).runUnsafe() shouldBe 1
      }
    }
}
