import HibernateEntityGenerator.builders.RelationBuilder
import HibernateEntityGenerator.models.{RefOneToMany, RefManyToOne}
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
      val s = RelationBuilder.buildManyToOne(RefManyToOne(columnName = "CHAR_PREM_ID", tableName = "CI_PREM")).replace(" ","")
      s should be (s"""|  @ManyToOne(cascade = CascadeType.ALL)
                       |  @JoinColumn(name = "CHAR_PREM_ID")
                       |  public PremEntity prem;""".stripMargin.replace(" ",""))
    }

    it("build one to many") {
      val s = RelationBuilder.buildOneToMany(RefOneToMany(oneTableName = "CI_ACCT", manyTableName = "CI_SA"))
      info(s)
      s should be (
        s"""|  @OneToMany(mappedBy = "acct", cascade = CascadeType.ALL)
            |  public Set<SaEntity> saEntitySet = new HashSet<>;""".stripMargin)
    }



  }

}
