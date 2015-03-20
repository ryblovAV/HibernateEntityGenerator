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

    it("protected constructor") {
      val s = MethodBuilder.createConstructor("CI_PER",true)
      s should be (s"""|  protected PerEntity() {
                       |  }""".stripMargin)
    }
    it("public constructor") {
      val s = MethodBuilder.createConstructor("CI_PER_CHAR",false)
      s should be (s"""|  public PerCharEntity() {
                      |  }""".stripMargin)
    }

  }

}
