package a

import org.specs2.mutable._

class FileATest extends Specification {

  "test" >> {
    FileA.add(1, 2) must be equalTo 3
  }

}