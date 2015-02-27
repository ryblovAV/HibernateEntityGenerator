package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{FieldInfo, Column}


object FieldBuilder {

  def buildFieldAnnotation(column: Column) = {

    def buildColumnDefinition(dataType: String) = if (dataType == "CHAR") s""", columnDefinition = "char"""" else ""



    def buildLength(column: Column) = column.dataLength match {
      case Some(n) if column.dataType == "CHAR" || column.dataType == "VARCHAR2" => s", length = $n"
      case _ => ""
    }

    def buildColumnAnnotation(column: Column) =
    s"""@Column(name = "${column.name}"${buildColumnDefinition(column.dataType)}${buildLength(column)})"""

    if (column.dataType == "DATE")
      s"""|${buildColumnAnnotation(column)}
          |@Temporal(TemporalType.TIMESTAMP)""".stripMargin
    else
      buildColumnAnnotation(column)
  }

  def buildFieldName(columnName: String) = EntityBuilder.transformToCamelCase(columnName)

//  def buildFieldAnottation(column: Column) = column match {
//    case Column(_, "NUMBER", defaultValue, dataLength, _, 0, isPrimary) =>
//
//
//  }
//
//  def buildField(column: Column):FieldInfo = {



/*
    column match {
      case Column(name, "NUMBER", _, _, 0, defaultValue, isPrimary) =>
        createFieldInfo(name, "int", defaultValue, defineAnnotationForColumn, isPrimary)
      case ColumnInfo(name, "NUMBER", _, _, _, defaultValue, isPrimary) =>
        createFieldInfo(name, "int", defaultValue, defineAnnotationForColumn, isPrimary)
      case ColumnInfo(name, "VARCHAR2", dataLength, _, _, defaultValue, isPrimary) =>
        createFieldInfo(name, "String", defaultValue, defineAnnotationForVarcharColumn(dataLength), isPrimary)
      case ColumnInfo(name, "CHAR", dataLength, _, _, defaultValue, isPrimary) =>
        createFieldInfo(name, "String", defaultValue, defineAnnotationForCharColumn(dataLength), isPrimary)
      case ColumnInfo(name, "DATE", _, _, _, defaultValue, isPrimary) =>
        createFieldInfo(name, "Date", defaultValue, defineAnnotationForDateColumn, isPrimary)
      case _ => FieldInfo(column.name, column.dataType, column.defaultValue, "????????", false)
    }
*/

//    FieldInfo(EntityBuilder.transformToCamelCase(column.name),column)
//  }

}
