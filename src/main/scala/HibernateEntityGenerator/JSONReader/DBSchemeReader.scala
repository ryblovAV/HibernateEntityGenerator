package HibernateEntityGenerator.JSONReader

import HibernateEntityGenerator.models.Table
import play.api.libs.json.Json

import scala.io.Source

object DBSchemeReader {

  val pathTables = "src/main/resources/data/Scheme.json"

  def readScheme: List[Table] = {
    val bufferedSource = Source.fromFile(pathTables)
    val jsonStr:String = bufferedSource.getLines.mkString("")
    bufferedSource.close

    jsonToDBTable(jsonStr)
  }

  def jsonToDBTable(str: String): List[Table] =
    TableParser.strToJson(Json.parse(str))



}
