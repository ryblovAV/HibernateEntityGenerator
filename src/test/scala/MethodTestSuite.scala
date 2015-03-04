import HibernateEntityGenerator.builders.MethodBuilder
import org.scalatest._

class MethodTestSuite extends FunSpec with ShouldMatchers {

  describe("constructors") {

    it("constructor with envId") {
      val s = MethodBuilder.createConstructorWithEnvId("CI_PER")
      s should be (s"""|  public PerEntity(int envId) {
                       |    perKEntitySet.add(new PerKEntity(envId));
                       |  }""".stripMargin)
    }

    it("constructor") {
      val s = MethodBuilder.createConstructor("CI_PER")
      s should be (s"""|  public PerEntity() {
                       |  }""".stripMargin)
    }
  }

}
