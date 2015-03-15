package HibernateEntityGenerator.writer

import java.io.{FileWriter, BufferedWriter, File}

import HibernateEntityGenerator.builders.EntityBuilder

object EntityWriter {

  def packageToFolderName(packageName: String) = packageName.replace(".","/")

  def buildPathFolder(packageName: String) = s"src/main/resources/gen/${packageToFolderName(packageName)}"
  
  def buildFullPath(packageName: String, tableName: String) =
    s"${buildPathFolder(packageName)}/${EntityBuilder.buildEntityClassName(tableName)}.java"

  def writeEntity(tableName: String, packageName:String, entityStr: String) = {
    val file = new File(buildFullPath(packageName,tableName))
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(entityStr)
    bw.close()
  }

}
