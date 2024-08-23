package CondiWarrior.src.test

import CondiWarrior.src.main.Entities.CondiWarrior

object CondiWarriorTest {

  var uut: CondiWarrior = null

  def main(args: Array[String]): Unit = {
    testFurious()
  }

  def testFurious(): Unit = {
    uut = new CondiWarrior(0, 0, 0, 0, 0)
    uut.add_furious_stack()
    uut.tick_warrior(1.0)
    uut.add_furious_stack()
    uut.tick_warrior(1.0)
    while (uut.getFuriousSurge.size < 25) {
      uut.add_furious_stack()
    }
    val minValue = uut.getFuriousSurge.min

    uut.add_furious_stack()
    assert(!uut.getFuriousSurge.contains(minValue))
  }


}
