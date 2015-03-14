package HibernateEntityGenerator.JSONReader

import HibernateEntityGenerator.models.{Table,Column,RefManyToOne,RefOneToMany}
import grizzled.slf4j.Logging
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

object TableParser extends Logging{


  implicit val columnReaders: Reads[Column] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "dataType").read[String] and
      (JsPath \ "defaultValue").readNullable[String] and
      (JsPath \ "dataLength").read[Int] and
      (JsPath \ "dataPrecision").readNullable[Int] and
      (JsPath \ "dataScale").readNullable[Int] and
      (JsPath \ "pkPosition").readNullable[Int]
    )(Column.apply _)

  //ManyToOne
  implicit val refManyToOneReaders: Reads[RefManyToOne] = (
    (JsPath \ "columnName").read[String] and
      (JsPath \ "tableName").read[String]
    )(RefManyToOne.apply _)

  //OneToMany
  implicit val refOneToManyReaders: Reads[RefOneToMany] = (
    (JsPath \ "oneTableName").read[String] and
      (JsPath \ "manyTableName").read[String]
    )(RefOneToMany.apply _)

  //Table
  implicit val tableReaders: Reads[Table] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "owner").read[String] and
      (JsPath \ "embeddable").read[Int] and
      (JsPath \ "columns").read[List[Column]] and
      (JsPath \ "pkColumns").read[List[Column]] and
      (JsPath \ "embeddedTables").read[List[String]] and
      (JsPath \ "manyToOne").read[List[RefManyToOne]] and
      (JsPath \ "oneToMany").read[List[RefOneToMany]]
    )(Table.apply _)

  def strToJson(json: JsValue) =
    json.validate[List[Table]] match {
      case s: JsSuccess[List[Table]] => {
        s.get
      }
      case e: JsError => {
        info(e)
        throw new Exception(e.toString)
      }
    }



}
