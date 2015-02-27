package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{Column, EntityInfo, TableInfo}

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
      case entityRegExpr(name) => transformToCamelCase(name) + "Entity"
    }
  }

  def build(table:TableInfo):EntityInfo = {
    EntityInfo(transformEntityName(table.name),table,List.empty)
  }

  def addSeparator(str: String):String = if (str.length > 0) ", " else ""

  def buildJoinColumnBlock(pKeys: List[Column]) = {

    def buildOne(column: Column) =
      s"""@JoinColumn(name = "${column.name}")"""

    val str =
      if (pKeys.length == 1)
        s"${buildOne(pKeys.head)}"
      else
        s"{${pKeys.foldLeft("")((str,column)=>s"""$str${addSeparator(str)}${buildOne(column)}""")}}"

    s"joinColumns = $str"
  }

  def buildEmbeddableCollection(entity: EntityInfo) =
    s"public Set<${entity.name.capitalize}> ${entity.name}Set = new HashSet<>();"

  def buildEmbeddableCollectionA(table: TableInfo)  =
    s"""|@ElementCollection
        |@CollectionTable(name = "${table.name}", schema = "${table.owner}", ${buildJoinColumnBlock(table.pKeys)})""".stripMargin

  def buildEmbeddableBlock(entity: EntityInfo) =
    s"""|${buildEmbeddableCollectionA(entity.table)}
        |${buildEmbeddableCollection(entity)}
     """.stripMargin

  def buildEmbeddableCollectionBlock(embeddableEntities: List[EntityInfo]) =
    embeddableEntities.foldLeft[String]("")((str, e) => s"str + ${buildEmbeddableBlock(e)}")


}
