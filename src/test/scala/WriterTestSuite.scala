import HibernateEntityGenerator.writer.EntityWriter
import org.scalatest._

class WriterTestSuite extends FunSpec with ShouldMatchers {

  describe("write entity") {

    val pathFolder = EntityWriter.buildPathFolder("HibernateEntityGenerator.builders")

    it("package to folder name") {
      EntityWriter.packageToFolderName("HibernateEntityGenerator.builders") should be ("HibernateEntityGenerator/builders")
    }

    it("build path folder") {
      info(s"pathFolder = $pathFolder")
      pathFolder should be ("src/main/resources/gen/HibernateEntityGenerator/builders")
    }

    it("build full path") {
      EntityWriter.buildFullPath("org.loader.pojo.sa", "CI_SA_CHAR") should be ("src/main/resources/gen/org/loader/pojo/sa/SaCharEntity.java")
    }
  }

}
