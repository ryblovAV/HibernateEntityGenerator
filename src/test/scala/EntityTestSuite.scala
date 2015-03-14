import HibernateEntityGenerator.builders.EntityBuilder
import HibernateEntityGenerator.models.{Column, Table}
import org.scalatest._

class EntityTestSuite extends FunSpec with ShouldMatchers {

  val acctId = Column.stringColumn(name = "ACCT_ID",dataType = "char",defaultValue = null, dataLength = 10, pkPosition = Some(1))
  val envId = Column.numberColumn(name = "ENV_ID", defaultValue = null, dataLength = 22,dataPrecision = 6,dataScale = 0,pkPosition = None)

  val columns = List(
    acctId
  )

  val pKeys = List(acctId)

  val acctK = Table(
    name = "CI_ACCT_K",
    owner = "STGADM",
    embeddable = 1,
    columns = columns,
    pkColumns = List(),
    embeddedTables = List())

  val acct = Table(
    name = "CI_ACCT",
    owner = "STGADM",
    embeddable = 0,
    columns = columns,
    pkColumns = pKeys,
    embeddedTables = List())

}
