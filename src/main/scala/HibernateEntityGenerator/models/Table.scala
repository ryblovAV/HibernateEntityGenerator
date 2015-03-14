package HibernateEntityGenerator.models

case class Column(name: String,
                  dataType: String,
                  defaultValue: Option[String],
                  dataLength: Int,
                  dataPrecision: Option[Int],
                  dataScale: Option[Int],
                  pkPosition: Option[Int])

case object Column {

  def stringColumn(name: String,
                   dataType: String,
                   defaultValue: String,
                   dataLength: Int,
                   pkPosition: Option[Int] = None) = {
    Column(
      name = name,
      dataType = dataType,
      defaultValue = Option(defaultValue),
      dataLength = dataLength,
      dataPrecision = None,
      dataScale = None,
      pkPosition = pkPosition)
  }

  def numberColumn(name: String,
                   defaultValue: String,
                   dataLength: Int,
                   dataPrecision: Int,
                   dataScale: Int,
                   pkPosition: Option[Int] = None) = {
    Column(
      name = name,
      dataType = "NUMBER",
      defaultValue = Option(defaultValue),
      dataLength = dataLength,
      dataPrecision = Option(dataPrecision),
      dataScale = Option(dataScale),
      pkPosition = pkPosition)
  }

  def dateColumn(name: String,
                 defaultValue: String,
                 pkPosition: Option[Int] = None) = {
    Column(
      name = name,
      dataType = "DATE",
      defaultValue = Option(defaultValue),
      dataLength = 7,
      dataPrecision = None,
      dataScale = None,
      pkPosition = pkPosition)
  }
}

//case class Relation(joinTable: String, columnNames: List[String])

case class RefManyToOne(columnName: String, tableName: String)

case class RefOneToMany(oneTableName: String, manyTableName: String)

case class Table(name: String,
                 owner: String,
                 embeddable: Int,
                 columns: List[Column],
                 pkColumns: List[Column],
                 embeddedTables: List[String],
                 manyToOne: List[RefManyToOne] = Nil,
                 oneToMany: List[RefOneToMany] = Nil) {

//  require((!pkColumns.isEmpty) || (embeddable == 1))
//  require(!columns.isEmpty)

  def isWithKey = embeddedTables.isEmpty == false
}

object Table {

  def createTable(name: String,
            owner: String,
            columns: List[Column],
            pKeys: List[Column]):Table =
    Table(name = name,
      owner = owner,
      embeddable = 0,
      columns = columns,
      pkColumns = pKeys,
      embeddedTables = List.empty,
      manyToOne = Nil,
      oneToMany = Nil)

  def createEmbeddableTable(name: String,
                  owner: String,
                  columns: List[Column],
                  pKeys: List[Column]):Table =
    Table(name = name,
      owner = owner,
      embeddable = 1,
      columns = columns,
      pkColumns = pKeys,
      embeddedTables = List.empty,
      manyToOne = Nil,
      oneToMany = Nil)


}