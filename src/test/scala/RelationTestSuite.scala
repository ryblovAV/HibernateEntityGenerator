import HibernateEntityGenerator.builders.RelationBuilder
import HibernateEntityGenerator.models.{Column, Relation}
import org.scalatest._

class RelationTestSuite extends FunSpec with ShouldMatchers {

  describe("many to one") {

    it("Join column (one)") {
      val s = RelationBuilder.buildColumns(List("CHAR_PREM_ID"))
      s should be (s"""  @JoinColumn(name = "CHAR_PREM_ID")""")
    }

    it("Join column (many)") {
      val s = RelationBuilder.buildColumns(List("ADDR_ID","ADDR_ZIP"))
      val s2 = s"""|  @JoinColumns({
                   |  @JoinColumn(name = "ADDR_ID"),
                   |  @JoinColumn(name = "ADDR_ZIP")
                   |  })""".stripMargin
      s.replace(" ","") should be (s2.replace(" ",""))
    }

    it("build many to one") {
      val s = RelationBuilder.buildManyToOne(Relation("CI_PREM",List("CHAR_PREM_ID"))).replace(" ","")
      s should be (s"""|  @ManyToOne(cascade = CascadeType.ALL)
                       |  @JoinColumn(name = "CHAR_PREM_ID")
                       |  public PremEntity prem;""".stripMargin.replace(" ",""))
    }

  }

}
