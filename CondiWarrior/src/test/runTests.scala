package CondiWarrior.src.test

import CondiWarrior.src.test.CondiWarriorTest.runWarriorTests
import CondiWarrior.src.test.WeaponTest.runWeaponTests

object runTests {
  def main(args: Array[String]): Unit = {
    runWarriorTests()
    runWeaponTests()
  }
}
