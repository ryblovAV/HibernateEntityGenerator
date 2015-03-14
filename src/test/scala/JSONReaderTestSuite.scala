import HibernateEntityGenerator.JSONReader.DBSchemeReader
import org.scalatest._

class JSONReaderTestSuite extends FunSpec with ShouldMatchers {

  describe("read data base scheme from json") {
    it("read table collection") {
      val tables = DBSchemeReader.readScheme
      tables.isEmpty should be (false)
    }
  }

}
