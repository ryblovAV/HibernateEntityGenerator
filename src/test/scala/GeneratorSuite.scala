import HibernateEntityGenerator.models.TableInfo
import org.scalatest.{FunSpec, ShouldMatchers, FunSuite}

class GeneratorSuite extends FunSpec with ShouldMatchers {

  describe("Create TableInfo") {
    val tableInfo = TableInfo("CI_PER")
    tableInfo.name should be ("CI_PER")
  }


}
