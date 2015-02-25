package HibernateEntityGenerator.models

case class ColumnInfo(name:String)

case class TableInfo(name:String, columns: List[ColumnInfo])
