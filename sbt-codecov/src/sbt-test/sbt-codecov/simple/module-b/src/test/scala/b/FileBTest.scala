package b

import org.specs2.mutable._

class FileBTest extends Specification {

  "test" >> {
    FileB.add(1, 2) must be equalTo 3
  }

}