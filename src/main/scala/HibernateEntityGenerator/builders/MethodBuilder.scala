package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{Table$, Column}

object MethodBuilder {

  def createEqualRow(fieldName:String, dbType:String) = dbType match {
      case "NUMBER" =>
        s"""|  if (this.$fieldName != other.$fieldName) {
            |    return false;
            |  }""".stripMargin
      case _ =>
        s"""|  if (!this.$fieldName.equals(other.$fieldName)) {
            |    return false;
            |  }""".stripMargin
  }

  def buildEqualMethod(tableName: String, pKeys: List[Column]) = {

    val s = pKeys.foldLeft[String]("")(
      (str, column) => str + s"""|${createEqualRow(FieldBuilder.buildFieldName(column.name),column.dataType)}
                                 |""".stripMargin
    )

    val entityName = EntityBuilder.transformEntityName(tableName).capitalize

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

  def buildHashCodeMethod(pKeys: List[Column]) = {

    def getCalcHashRow(fieldName: String, dbType:String):String = {
      dbType match {
        case "NUMBER" =>
          s"${fieldName};"
        case _ =>
          s"${fieldName}.hashCode();"
      }
    }

    val fieldBlock = pKeys.foldLeft[String]("")(
      (str, c) => str + s"""|  hash = 31 * hash + ${getCalcHashRow(FieldBuilder.buildFieldName(c.name),c.dataType)}
          |""".stripMargin
    )

    s"""|@Override
        |public int hashCode() {
        |  int hash = 0;
        |$fieldBlock
        |  return hash;
        |}""".stripMargin
  }

  def createConstructorWithEnvId(tableName: String) = {
    s"""|  public ${EntityBuilder.transformEntityName(tableName).capitalize}(int envId) {
        |    ${EntityBuilder.getEntity(tableName)}KEntitySet.add(new ${EntityBuilder.getEntity(tableName).capitalize}KEntity(envId));
        |  }""".stripMargin
  }

  def createConstructor(tableName: String) = {
    s"""|  public ${EntityBuilder.transformEntityName(tableName).capitalize}() {
        |  }""".stripMargin
  }


}
