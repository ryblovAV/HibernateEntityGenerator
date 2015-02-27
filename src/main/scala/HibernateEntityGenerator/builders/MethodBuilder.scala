package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{TableInfo, Column}

object MethodBuilder {

  def createEqualRowWithoutCheck(column:Column) = column match {
      case Column(name, "NUMBER" , _, _, _, _, _) =>
        s"""|  if (this.${FieldBuilder.buildFieldName(name)} != other.${FieldBuilder.buildFieldName(name)}) {
            |    return false;
            |  }""".stripMargin
      case Column(name, _, _, _, _, _, _) =>
        s"""|  if (!this.${FieldBuilder.buildFieldName(name)}.equals(other.${FieldBuilder.buildFieldName(name)})) {
            |    return false;
            |  }""".stripMargin
  }

  def buildEqualMethod(table: TableInfo) = {

    val s = table.pKeys.foldLeft[String]("")(
      (str, column) => str + s"""|${createEqualRowWithoutCheck(column)}
                                 |""".stripMargin
    )

    val entityName = EntityBuilder.transformEntityName(table.name).capitalize

    s"""|@Override
        |public boolean equals(Object object) {
        |
        |  if (this == object)
        |    return true;
        |
        |  if (!(object instanceof ${entityName}))
        |    return false;
        |
        |  ${entityName} other = (${entityName}) object;
        |
        |${s}
        |  return true;
        |}""".stripMargin

  }

}
