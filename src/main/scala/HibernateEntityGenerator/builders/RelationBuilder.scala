package HibernateEntityGenerator.builders

import HibernateEntityGenerator.models.{RefOneToMany, RefManyToOne}

object RelationBuilder {

  def buildOneToMany(r: RefOneToMany) =
    s"""|  @OneToMany(mappedBy = "${EntityBuilder.getEntity(r.oneTableName)}", cascade = CascadeType.ALL)
        |  public Set<${EntityBuilder.transformEntityName(r.manyTableName).capitalize}> ${EntityBuilder.transformEntityName(r.manyTableName)}Set = new HashSet<>;""".stripMargin

  def buildOneToManyAll(lr: List[RefOneToMany]) =
    lr.foldLeft("")((str, r) => s"$str\n${buildOneToMany(r)}\n")

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

  def buildManyToOne(r: RefManyToOne) =
    s"""|  @ManyToOne(cascade = CascadeType.ALL)
        |${buildColumns(List(r.columnName))}
        |  public ${EntityBuilder.getEntity(r.tableName).capitalize}Entity ${EntityBuilder.getEntity(r.tableName)};""".stripMargin

  def buildManyToOneAll(lr: List[RefManyToOne]) =
    lr.foldLeft("")((str, r) => s"$str\n${buildManyToOne(r)}\n")

}
