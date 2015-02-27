import HibernateEntityGenerator.builders.{FieldBuilder, EntityBuilder}
import HibernateEntityGenerator.models._
import org.scalatest.{FunSpec, ShouldMatchers, FunSuite}

class GeneratorSuite extends FunSpec with ShouldMatchers {

  val perIdColumn = Column.stringColumn(name = "PER_ID",
                                        dataType = "CHAR",
                                        dataLength = 10,
                                        defaultValue = null,
                                        isPrimary = true)
  
  val languageCdColumn = Column.stringColumn(name = "LANGUAGE_CD",
                                       dataType = "CHAR",
                                       dataLength = 3,
                                       defaultValue = null)

  val envIdColumn = Column.numberColumn(name = "ENV_ID",
                                        dataLength = 22,
                                        dataPrecision = 6,
                                        dataScale = 0,
                                        defaultValue = null)

  val totToBillAmt = Column.numberColumn(name = "TOT_TO_BILL_AMT",
                                        dataLength = 22,
                                        dataPrecision = 15,
                                        dataScale = 2,
                                        defaultValue = null)

  val addressColumn = Column.stringColumn(name = "ADDRESS1",
                                          dataType = "VARCHAR2",
                                          dataLength = 254,
                                          defaultValue = null)

  val effDtColumn = Column.dateColumn(name = "EFFDT",
                                      defaultValue = null)



  val columns = List(perIdColumn)

  val pKeys = List(perIdColumn)

  val embeddableTable = TableInfo("CI_PER_NAME", "STGADM", columns, pKeys)
  val embeddableEntity = EntityInfo("perNameEntity", embeddableTable, List.empty)

  val embeddableTables = List(embeddableTable,
                              TableInfo("CI_PER_ID","STGADM",columns,pKeys),
                              TableInfo("CI_PER_K","STGADM",columns,pKeys))
  val table = TableInfo("CI_PER","STGADM",columns,pKeys,embeddableTables)

  describe("Create TableInfo") {
    table.name should be ("CI_PER")
  }

  describe("define entity name") {
    val entity = EntityBuilder.build(table)
    entity.name should be ("perEntity")
  }

  describe("create table with not primaryKey") {
    an [IllegalArgumentException] should be thrownBy { TableInfo("name","owner",columns,List.empty,List.empty) }
  }

  describe("create table with not columns") {
    an [IllegalArgumentException] should be thrownBy { TableInfo("name","owner",List.empty,pKeys,List.empty) }
  }

  describe("create block") {
    it("join one column") {
      val s = EntityBuilder.buildJoinColumnBlock(pKeys)
      //      embeddableTable(s)
      s should be( s"""joinColumns = @JoinColumn(name = "PER_ID")""")
    }

    it("join many columns") {
      val pKeys = List(perIdColumn, envIdColumn)
      val s = EntityBuilder.buildJoinColumnBlock(pKeys)
      //      embeddableTable(s)
      s should be( s"""joinColumns = {@JoinColumn(name = "PER_ID"), @JoinColumn(name = "ENV_ID")}""")
    }

    it("embeddable collection") {
      val s = EntityBuilder.buildEmbeddableCollection(embeddableEntity)
      s should be("public Set<PerNameEntity> perNameEntitySet = new HashSet<>();")

    }

    it("embeddable collection annotation") {
      val s = EntityBuilder.buildEmbeddableCollectionA(embeddableTable)
      info(s)
      s should be(s"""|@ElementCollection
                      |@CollectionTable(name = "CI_PER_NAME", schema = "STGADM", joinColumns = @JoinColumn(name = "PER_ID"))""".stripMargin)
    }
  }

  describe("build field annotation") {

    it("build string field annotation(char)") {
      val s = FieldBuilder.buildFieldAnnotation(languageCdColumn)
      s should be(s"""@Column(name = "LANGUAGE_CD", columnDefinition = "char", length = 3)""")
    }

    it("build string field annotation(varchar2)") {
      val s = FieldBuilder.buildFieldAnnotation(addressColumn)
      s should be(s"""@Column(name = "ADDRESS1", length = 254)""")
    }

    it("build number field annotation") {
      val s = FieldBuilder.buildFieldAnnotation(envIdColumn)
      s should be(s"""@Column(name = "ENV_ID")""")
    }

    it("build date field annotation") {
      val s = FieldBuilder.buildFieldAnnotation(effDtColumn)
      info(s)
      s should be(s"""|@Column(name = "EFFDT")
                      |@Temporal(TemporalType.TIMESTAMP)""".stripMargin)
    }

  }

  describe("build java field") {
    it("check field name") {
      val name = FieldBuilder.buildFieldName("PER_ID")
      name should be("perId")
    }

    def addDefV(column: Column) = column.copy(defaultValue = Some("0"))

    it("check int java") {
      FieldBuilder.buildFieldJavaCode(envIdColumn) should be ("public int envId;")
      FieldBuilder.buildFieldJavaCode(addDefV(envIdColumn)) should be ("public int envId = 0;")
    }

    it("check double java") {
      FieldBuilder.buildFieldJavaCode(totToBillAmt) should be ("public double totToBillAmt;")
      FieldBuilder.buildFieldJavaCode(addDefV(totToBillAmt)) should be ("public double totToBillAmt = 0;")
    }

    it("check string (varchar2) java") {
      FieldBuilder.buildFieldJavaCode(addressColumn) should be ("public String address1;")
      FieldBuilder.buildFieldJavaCode(addDefV(addressColumn)) should be (s"""public String address1 = "0";""")
    }

    it("check string (char) java") {
      FieldBuilder.buildFieldJavaCode(languageCdColumn) should be ("public String languageCd;")
      FieldBuilder.buildFieldJavaCode(addDefV(languageCdColumn)) should be (s"""public String languageCd = "0";""")
    }

    it("check date (char) java") {
      val s = FieldBuilder.buildFieldJavaCode(effDtColumn)
      s should be ("public Date effdt;")
    }




  }

}
