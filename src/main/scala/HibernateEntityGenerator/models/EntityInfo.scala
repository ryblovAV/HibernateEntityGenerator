package HibernateEntityGenerator.models

case class FieldInfo(name:String, column: Column)

case class EntityInfo(name: String, table: TableInfo, embeddableEntities: List[EntityInfo])
