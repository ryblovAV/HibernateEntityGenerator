package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{Column, Table}

import scala.util.matching.Regex

object EntityBuilder {

  val entityRegExpr = """CI_{1}(.+)""".r

  val columnRegExpr = """(.+)_ID{1}""".r

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

  def buildEntityClassName(tableName: String) = transformEntityName(tableName).capitalize

  def buildEntityNameByColumn(columnName: String) = columnName match {
    case columnRegExpr(name) => transformToCamelCase(name)
  }

  def buildEntityCollectionByColumn(tableName: String, columnName: String) =
    getEntity(tableName) +  buildEntityNameByColumn(columnName).capitalize


  def addSeparator(str: String):String = if (str.length > 0) ", " else ""

  def buildClass(tableName: String, owner: String, isEmbeddable: Boolean) = {
    if (isEmbeddable)
      s"""|@Embeddable
          |public class ${buildEntityClassName(tableName)}""".stripMargin
    else
      s"""|@Entity
          |@Table(name = "$tableName", schema = "$owner")
          |public class ${buildEntityClassName(tableName)}""".stripMargin

  }

  def buildJoinColumnBlock(pKeys: List[String]) = {

    def buildOne(columnName: String) =
      s"""@JoinColumn(name = "${columnName}")"""

    val str =
      if (pKeys.length == 1)
        s"${buildOne(pKeys.head)}"
      else
        s"{${pKeys.foldLeft("")((str,column)=>s"""$str${addSeparator(str)}${buildOne(column)}""")}}"

    s"joinColumns = $str"
  }

  def buildEmbeddableCollection(tableName: String) =
    s"  public Set<${buildEntityClassName(tableName)}> ${transformEntityName(tableName)}Set = new HashSet<>();"

  def buildEmbeddableCollectionA(owner: String, tableName: String, pkColumnName: String)  =
    s"""|  @ElementCollection
        |  @CollectionTable(name = "$tableName", schema = "$owner", ${buildJoinColumnBlock(List(pkColumnName))})""".stripMargin

  def buildEmbeddableBlock(owner: String, tableName: String, pkColumnName: String) =
    s"""|${buildEmbeddableCollectionA(owner, tableName, pkColumnName)}
        |${buildEmbeddableCollection(tableName)}
     """.stripMargin

  def buildEmbeddableCollectionBlock(owner: String, embeddableTables: List[String], pkColumn: String) =
    embeddableTables.foldLeft[String]("")((str, e) => s"$str\n${buildEmbeddableBlock(owner, e, pkColumn)}")

  def buildImportBlock(packageName: String) =
    s"""|import $packageName.*;
        |
        |import javax.persistence.*;
        |import java.util.Date;
        |import java.util.HashSet;
        |import java.util.Set;""".stripMargin

  def isAddConstructorWithEnvId(table: Table) = (table.isWithKey) || (table.embeddable == 1)

  //TODO correct table.pkColumns(0) (if count pkColumns != 1)
  def build(table: Table, packageName: String) = {
    s"""|package $packageName;
        |
        |${buildImportBlock(packageName)}
        |
        |${EntityBuilder.buildClass(tableName = table.name, owner = table.owner, isEmbeddable = table.embeddable == 1)} {
        |${MethodBuilder.createConstructor(tableName = table.name,isProtected = isAddConstructorWithEnvId(table))}
        |${if (isAddConstructorWithEnvId(table)) MethodBuilder.createConstructorWithEnvId(table.name) else ""}
        |${MethodBuilder.buildEqualMethod(table.name,table.pkColumns)}
        |${MethodBuilder.buildHashCodeMethod(table.pkColumns)}
        |${if (table.pkColumns.size == 1) buildEmbeddableCollectionBlock(table.owner, table.embeddedTables, table.pkColumns(0).name) else ""}
        |${RelationBuilder.buildOneToManyAll(table.oneToMany)}
        |${RelationBuilder.buildManyToOneAll(table.manyToOne)}
        |${FieldBuilder.buildFieldCodeAll(table.pkColumns.sortBy(_.name),table.embeddable == 1)}
        |${FieldBuilder.buildFieldCodeAll(table.columns.sortBy(_.name),false)}
        |
        |}""".stripMargin
  }

}
