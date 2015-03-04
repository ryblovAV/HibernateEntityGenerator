package HibernateEntityGenerator.models

case class Column(name: String,
                  dataType: String,
                  defaultValue: Option[String],
                  dataLength: Option[Int],
                  dataPrecision: Option[Int],
                  dataScale: Option[Int],
                  isPrimary: Boolean)

case object Column {

  def stringColumn(name: String,
                   dataType: String,
                   defaultValue: String,
                   dataLength: Int,
                   isPrimary: Boolean = false) = {
    Column(
      name = name,
      dataType = dataType,
      defaultValue = Option(defaultValue),
      dataLength = Option(dataLength),
      dataPrecision = None,
      dataScale = None,
      isPrimary = isPrimary)
  }

  def numberColumn(name: String,
                   defaultValue: String,
                   dataLength: Int,
                   dataPrecision: Int,
                   dataScale: Int,
                   isPrimary: Boolean = false) = {
    Column(
      name = name,
      dataType = "NUMBER",
      defaultValue = Option(defaultValue),
      dataLength = Option(dataLength),
      dataPrecision = Option(dataPrecision),
      dataScale = Option(dataScale),
      isPrimary = isPrimary)
  }

  def dateColumn(name: String,
                 defaultValue: String,
                 isPrimary: Boolean = false) = {
    Column(
      name = name,
      dataType = "DATE",
      defaultValue = Option(defaultValue),
      dataLength = None,
      dataPrecision = None,
      dataScale = None,
      isPrimary = isPrimary)
  }
}

case class Relation(joinTable: String, columnNames: List[String])

case class Table(name: String,
                 owner: String,
                 columns: List[Column],
                 pKeys: List[Column],
                 embeddableTables: List[Table],
                 isEmbeddable: Boolean,
                 isWithKey: Boolean) {
  require((!pKeys.isEmpty) || isEmbeddable)
  require(!columns.isEmpty)
}

object Table {

  def apply(name: String,
            owner: String,
            columns: List[Column],
            pKeys: List[Column]):Table =
    Table(name = name,
      owner = owner,
      columns = columns,
      pKeys = pKeys,
      embeddableTables = List.empty,
      isEmbeddable = false,
      isWithKey = false)

}