/*import java.nio.file.{Path, Paths}

import org.specs2.mutable._

class NaiveJoinSpec extends Specification {
  "NaiveJoin" should {
    "output lines with matching keys from two different files" in {

      val reader:SimpleReader = new SimpleReader
      val testPath: Path = Paths.get("C:/Users/dennis.koehn/Uni/DWH/files/")

      val file1 = reader.read(testPath.resolve("test1"))
      val file2 = reader.read(testPath.resolve("test2"))

      val expectedResult = Set("A3094364306", "F3094364306", "C3069349063")

      val naiveJoin: NaiveJoin = new NaiveJoin
      naiveJoin.join(file1,file2) === expectedResult
    }
  }

}
*/