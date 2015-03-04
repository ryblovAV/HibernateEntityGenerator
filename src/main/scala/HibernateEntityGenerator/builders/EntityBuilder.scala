package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{Column, Table}

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

  def getEntity(tableName: String) = tableName match {
    case entityRegExpr(name) => transformToCamelCase(name)
  }

  def transformEntityName(tableName: String) = s"${getEntity(tableName)}Entity"

  def addSeparator(str: String):String = if (str.length > 0) ", " else ""

  def buildClass(tableName: String, owner: String, isEmbeddable: Boolean) = {
    if (isEmbeddable)
      s"""|@Embeddable
          |public class ${transformEntityName(tableName).capitalize}""".stripMargin
    else
      s"""|@Entity
          |@Table(name = "$tableName", schema = "$owner")
          |public class ${transformEntityName(tableName).capitalize}""".stripMargin

  }

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

  def buildEmbeddableCollection(table: Table) =
    s"public Set<${transformEntityName(table.name).capitalize}> ${transformEntityName(table.name)}Set = new HashSet<>();"

  def buildEmbeddableCollectionA(table: Table)  =
    s"""|@ElementCollection
        |@CollectionTable(name = "${table.name}", schema = "${table.owner}", ${buildJoinColumnBlock(table.pKeys)})""".stripMargin

  def buildEmbeddableBlock(table: Table) =
    s"""|${buildEmbeddableCollectionA(table)}
        |${buildEmbeddableCollection(table)}
     """.stripMargin

  def buildEmbeddableCollectionBlock(embeddableTables: List[Table]) =
    embeddableTables.foldLeft[String]("")((str, e) => s"str + ${buildEmbeddableBlock(e)}")


  def build(table: Table) = {
    s"""|${EntityBuilder.buildClass(tableName = table.name, owner = table.owner, isEmbeddable = table.isEmbeddable)} {
        |${MethodBuilder.createConstructor(tableName = table.name)}
        |${if (table.isWithKey) MethodBuilder.createConstructorWithEnvId(table.name) else ""}
        |${buildEmbeddableCollectionBlock(table.embeddableTables)}
        |}""".stripMargin
  }

}
