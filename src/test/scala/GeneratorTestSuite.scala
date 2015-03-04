import HibernateEntityGenerator.builders.{RelationBuilder, MethodBuilder, FieldBuilder, EntityBuilder}
import HibernateEntityGenerator.models._
import org.scalatest.{FunSpec, ShouldMatchers, FunSuite}

class GeneratorTestSuite extends FunSpec with ShouldMatchers {

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

  val embeddableTable = Table("CI_PER_NAME", "STGADM", columns, pKeys)

  val embeddableTables = List(embeddableTable,
                              Table("CI_PER_ID","STGADM",columns,pKeys),
                              Table("CI_PER_K","STGADM",columns,pKeys))

  val table = Table("CI_PER","STGADM",columns,pKeys,embeddableTables,false,true)

  describe("Create TableInfo") {
    table.name should be ("CI_PER")
  }

  describe("define entity name") {
    EntityBuilder.transformEntityName("CI_PER") should be ("perEntity")
  }

  describe("create table with not primaryKey") {
    an [IllegalArgumentException] should be thrownBy { Table("name","owner",columns,List.empty,List.empty, false, false) }
  }

  describe("create table with not columns") {
    an [IllegalArgumentException] should be thrownBy { Table("name","owner",List.empty,pKeys,List.empty, false, false) }
  }

  describe("create head class") {
    it("embeddable") {
      val s = EntityBuilder.buildClass("CI_PER_CHAR","STGADM",true)
      s should be (s"""|@Embeddable
                       |public class PerCharEntity""".stripMargin)
    }

    it("entity") {
      val s = EntityBuilder.buildClass("CI_PER","STGADM",false)
      s should be (s"""|@Entity
                       |@Table(name = "CI_PER", schema = "STGADM")
                       |public class PerEntity""".stripMargin)
    }

  }

  describe("Join column") {

    it("join one column") {
      val s = EntityBuilder.buildJoinColumnBlock(pKeys)
      s should be( s"""joinColumns = @JoinColumn(name = "PER_ID")""")
    }

    it("join many columns") {
      val pKeys = List(perIdColumn, envIdColumn)
      val s = EntityBuilder.buildJoinColumnBlock(pKeys)
      //      embeddableTable(s)
      s should be( s"""joinColumns = {@JoinColumn(name = "PER_ID"), @JoinColumn(name = "ENV_ID")}""")
    }

  }

  describe("create block") {
    it("embeddable collection") {
      val s = EntityBuilder.buildEmbeddableCollection(embeddableTable)
      s should be("public Set<PerNameEntity> perNameEntitySet = new HashSet<>();")

    }

    it("embeddable collection annotation") {
      val s = EntityBuilder.buildEmbeddableCollectionA(embeddableTable)
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

  describe("create methods") {
    it("equal for one field") {
      val s = MethodBuilder.createEqualRow("perId","CHAR")
      s should be (s"""|  if (!this.perId.equals(other.perId)) {
                       |    return false;
                       |  }""".stripMargin)
    }

    it("equal method") {
      val s = MethodBuilder.buildEqualMethod("CI_PER",pKeys)
      s should be (s"""|@Override
                       |public boolean equals(Object object) {
                       |
                       |  if (this == object)
                       |    return true;
                       |
                       |  if (!(object instanceof PerEntity))
                       |    return false;
                       |
                       |  PerEntity other = (PerEntity) object;
                       |
                       |  if (!this.perId.equals(other.perId)) {
                       |    return false;
                       |  }
                       |
                       |  return true;
                       |}""".stripMargin)
    }

    it("hashCode method(one primaryKeys)") {
      val s = MethodBuilder.buildHashCodeMethod(pKeys)
      s should be (s"""|@Override
                       |public int hashCode() {
                       |  int hash = 0;
                       |  hash = 31 * hash + perId.hashCode();
                       |
                       |  return hash;
                       |}""".stripMargin)
    }

    it("hashCode method(many primaryKeys)") {
      val s = MethodBuilder.buildHashCodeMethod(pKeys:+envIdColumn)
      s should be (s"""|@Override
                       |public int hashCode() {
                       |  int hash = 0;
                       |  hash = 31 * hash + perId.hashCode();
                       |  hash = 31 * hash + envId;
                       |
                       |  return hash;
                       |}""".stripMargin)
    }







  }



}
