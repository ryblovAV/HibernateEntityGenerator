package HibernateEntityGenerator

import HibernateEntityGenerator.JSONReader.DBSchemeReader
import HibernateEntityGenerator.builders.EntityBuilder
import HibernateEntityGenerator.writer.EntityWriter
import grizzled.slf4j.Logging

object GeneratorApp extends App with Logging {

  val packageName = "org.loader.new.pojo"

  val tables = DBSchemeReader.readScheme.filter((t) => t.name == "CI_SA_CHAR")


  tables.foreach((t) => EntityWriter.writeEntity(tableName = t.name,
    packageName = packageName,
    entityStr = EntityBuilder.build(t, packageName)))

  //  EntityWriter.writeEntity()


}
