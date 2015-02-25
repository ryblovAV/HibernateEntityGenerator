package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{EntityInfo, TableInfo}

import scala.util.matching.Regex

object EntityBuilder {

  val entityRegExpr = """CI_{1}(.+)""".r

  def transformToCamelCase(name: String) = {
    "_([a-z\\d])".r.replaceAllIn(
    name.toLowerCase(), {
      m => m.group(1).toUpperCase()
    }
    )
  }

  def transformEntityName(tableName: String) = {
    tableName match {
      case entityRegExpr(name) => transformToCamelCase(name).capitalize + "Entity"
    }
  }

  def build(table:TableInfo):EntityInfo = {
    EntityInfo(transformEntityName(table.name))
  }

}
