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



case class TableInfo(name: String, 
                     owner: String, 
                     columns: List[Column],
                     pKeys: List[Column],
                     embeddableTables: List[TableInfo]) {
  require(!pKeys.isEmpty)
  require(!columns.isEmpty)
}

object TableInfo {

  def apply(name: String,
            owner: String,
            columns: List[Column],
            pKeys: List[Column]):TableInfo =
    TableInfo(name = name,
      owner = owner,
      columns = columns,
      pKeys = pKeys,
      embeddableTables = List.empty)

}