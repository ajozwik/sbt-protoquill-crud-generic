package pl.jozwik.example.zio

import pl.jozwik.example.domain.model.*
import pl.jozwik.example.zio.repository.*
import io.getquill.*
trait SaleRepositorySuite extends AbstractZioJdbcSpec {
  private implicit val saleSchema: SchemaMeta[Sale]     = schemaMeta[Sale]("SALE", _.id.fk1 -> "PRODUCT_ID", _.id.fk2 -> "PERSON_ID")
  private lazy val saleRepository                       = new SaleRepositoryGen(ctx)
  private implicit val personSchema: SchemaMeta[Person] = schemaMeta[Person]("PERSON2")
  private lazy val personRepository                     = new PersonRepositoryGen(ctx)
  private lazy val productRepository                    = new ProductRepositoryGen(ctx)(schemaMeta("PRODUCT"))
  "Sale Repository " should {
    "Call all operations on Sale" in {
      saleRepository.all.runSyncUnsafe() shouldBe Seq()
      val personWithoutId  = Person(PersonId.empty, "firstName", "lastName", today)
      val person           = personRepository.createAndRead(personWithoutId).runSyncUnsafe()
      val productWithoutId = Product(ProductId.empty, "productName")
      val product          = productRepository.createAndRead(productWithoutId).runSyncUnsafe()
      val saleId           = SaleId(product.id, person.id)
      val sale             = Sale(saleId, now)
      saleRepository.createAndRead(sale).runSyncUnsafe() shouldBe sale

      saleRepository.createOrUpdateAndRead(sale).runSyncUnsafe() shouldBe sale

      saleRepository.read(saleId).runSyncUnsafe() shouldBe Option(sale)
      saleRepository.delete(saleId).runSyncUnsafe() shouldBe 1
      productRepository.delete(product.id).runSyncUnsafe() shouldBe 1
      personRepository.delete(person.id).runSyncUnsafe() shouldBe 1
    }
  }

}
