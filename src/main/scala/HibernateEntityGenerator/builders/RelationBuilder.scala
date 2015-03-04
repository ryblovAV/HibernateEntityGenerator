package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{Column, Relation}


object RelationBuilder {

  def buildJoinColumn(columnName: String) = {
    s"""  @JoinColumn(name = "$columnName")""".stripMargin
  }

  def buildColumns(columnsName: List[String]) = {
    if (columnsName.size == 1)
      buildJoinColumn(columnsName(0))
    else {
      val s = columnsName.foldLeft("")((str, c) =>
          s"""|$str${EntityBuilder.addSeparator(str)}
              |${buildJoinColumn(c)}""".stripMargin)
      s"""|  @JoinColumns({$s
          |  })""".stripMargin
      }
  }

  def buildManyToOne(r: Relation) =
    s"""|  @ManyToOne(cascade = CascadeType.ALL)
        |${buildColumns(r.columnNames)}
        |public ${EntityBuilder.getEntity(r.joinTable).capitalize}Entity ${EntityBuilder.getEntity(r.joinTable)};""".stripMargin
}
