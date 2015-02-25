import HibernateEntityGenerator.builders.EntityBuilder
import HibernateEntityGenerator.models.TableInfo
import org.scalatest.{FunSpec, ShouldMatchers, FunSuite}

class GeneratorSuite extends FunSpec with ShouldMatchers {

  val table = TableInfo("CI_PER",List.empty)

  describe("Create TableInfo") {
    table.name should be ("CI_PER")
  }

  describe("define entity name") {
    val entity = EntityBuilder.build(table)
    entity.name should be ("PerEntity")
  }

}
