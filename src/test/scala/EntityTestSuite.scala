import HibernateEntityGenerator.builders.EntityBuilder
import HibernateEntityGenerator.models.{Column, Table}
import org.scalatest._

class EntityTestSuite extends FunSpec with ShouldMatchers {

  val acctId = Column.stringColumn(name = "ACCT_ID",dataType = "char",defaultValue = null,dataLength = 10,isPrimary = true)
  val envId = Column.numberColumn(name = "ENV_ID", defaultValue = null, dataLength = 22,dataPrecision = 6,dataScale = 0,isPrimary = false)

  val columns = List(
    acctId
  )

  val pKeys = List(acctId)

  val acctK = Table(
    name = "CI_ACCT_K",
    owner = "STGADM",
    columns = columns,
    pKeys = List(),
    embeddableTables = List(),
    isEmbeddable = true,
    isWithKey = false)

  val acct = Table(
    name = "CI_ACCT",
    owner = "STGADM",
    columns = columns,
    pKeys = pKeys,
    embeddableTables = List(),
    isEmbeddable = false,
    isWithKey = true)


  ignore("entity class"){
    it("build class for entity"){
      val s = EntityBuilder.build(acct)
      info(s)
      s should be ("YYY2")
    }
  }
}
