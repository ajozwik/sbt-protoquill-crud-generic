package pl.jozwik.example.doobie

import io.getquill.*
import pl.jozwik.example.domain.model.{Person, PersonId}
import pl.jozwik.example.doobie.repository.PersonRepositoryGen
trait PersonRepositorySuite extends AbstractDoobieJdbcSpec {
  private implicit val personSchema: SchemaMeta[Person] = schemaMeta[Person]("Person3", _.birthDate -> "DOB")
  private lazy val repository                           = new PersonRepositoryGen(ctx)

  "PersonRepository " should {
    "Call all operations on Person with auto generated id and custom field" in {

      logger.debug("generated id with custom field")
      val person = Person(PersonId.empty, "firstName", "lastName", today)
      repository.all.runUnsafe() shouldBe Seq()
      val personId         = repository.create(person)
      val personIdProvided = personId.runUnsafe()
      val createdPatron    = repository.read(personIdProvided).runUnsafe().getOrElse(fail())
      repository.update(createdPatron).runUnsafe() shouldBe 1
      repository.all.runUnsafe() shouldBe Seq(createdPatron)
      val newBirthDate = createdPatron.birthDate.minusYears(1)
      val modified     = createdPatron.copy(birthDate = newBirthDate)
      repository.update(modified).runUnsafe() shouldBe 1
      repository.read(createdPatron.id).runUnsafe().map(_.birthDate) shouldBe Option(newBirthDate)

      repository.delete(createdPatron.id).runUnsafe() shouldBe 1
      repository.read(createdPatron.id).runUnsafe() shouldBe empty
      repository.all.runUnsafe() shouldBe Seq()

    }
  }
}
