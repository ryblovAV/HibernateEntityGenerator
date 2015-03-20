package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.Column


object FieldBuilder {

  def buildFieldAnnotation(column: Column) = {

    def buildColumnDefinition(dataType: String) = if (dataType == "CHAR") s""", columnDefinition = "char"""" else ""

    def buildLength(column: Column) =
      if (column.dataType == "CHAR" || column.dataType == "VARCHAR2")
        s", length = ${column.dataLength}" else ""


    def buildColumnAnnotation(column: Column) =
    s"""@Column(name = "${column.name}"${buildColumnDefinition(column.dataType)}${buildLength(column)})"""

    if (column.dataType == "DATE")
      s"""|${buildColumnAnnotation(column)}
          |  @Temporal(TemporalType.TIMESTAMP)""".stripMargin
    else
      buildColumnAnnotation(column)
  }

  def buildFiledAnnotationPK(column: Column, isEmbeddable: Boolean) = {
    if ((column.pkPosition.isEmpty == false) && (isEmbeddable == false))
      s"""|@Id
          |  ${buildFieldAnnotation(column)}""".stripMargin
    else buildFieldAnnotation(column)
  }

  def buildFieldName(columnName: String) = EntityBuilder.transformToCamelCase(columnName)

  def buildFieldJavaCode(column: Column) = {

    def buildFieldJavaCode(columnName: String, javaType: String, defaultValue: Option[String]) = {

      def buildeDefaultValueJavaCode(javaType: String, defaultValue: Option[String]) = defaultValue match {
        case Some(v) =>
          javaType match {
            case "String" => s""" = "${v.trim.replace("'","")}""""
            case ("int" | "double") => s" = ${v.trim}"
          }
        case _ => ""
      }

      s"public $javaType ${buildFieldName(columnName)}${buildeDefaultValueJavaCode(javaType, defaultValue)};"
    }

    column match {
      case Column(name, "NUMBER", defaultValue, _, _, Some(0), _) =>
        buildFieldJavaCode(name, "int", defaultValue)
      case Column(name, "NUMBER", defaultValue, _, _, _, _) =>
        buildFieldJavaCode(name, "double", defaultValue)
      case Column(name, "VARCHAR2" | "CHAR", defaultValue, _, _, _, _) =>
        buildFieldJavaCode(name, "String", defaultValue)
      case Column(name, "DATE", defaultValue, _, _, _, _) =>
        buildFieldJavaCode(name, "Date", defaultValue)
    }
  }

  def buildFieldCode(column: Column, isEmbeddable: Boolean) = {
    s"""|  ${buildFiledAnnotationPK(column, isEmbeddable)}
        |  ${buildFieldJavaCode(column)}""".stripMargin
  }

  def buildFieldCodeAll(columns: List[Column], isEmbeddable: Boolean) =
    columns.filter(_.dataType != "CLOB").foldLeft("")((str, c) => s"$str\n${buildFieldCode(c,isEmbeddable)}\n")

}
