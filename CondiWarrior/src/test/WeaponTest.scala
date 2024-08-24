package CondiWarrior.src.test

import CondiWarrior.src.main.Entities.{CondiWarrior, Target}
import _root_.CondiWarrior.src.main.Conditions.{Bleeding, Burning, Torment}
import _root_.CondiWarrior.src.main.Weapons.Sword

object WeaponTest {

  val uut = new Sword

  def runWeaponTests(): Unit = {
    testFractalRelic()
    testKingOfFires()
  }

  private def testFractalRelic(): Unit ={
    println("Running Fractal Test")
    val fractalThreshold = 6
    val fractalBurning = 2
    val fractalTorment = 3
    val target = new Target(1000000.0)
    val player = new CondiWarrior(0, 0, 0, 0, 0)
    uut.setTarget(target)
    uut.setPlayer(player)

    // Don't trigger relic when below threshold
    target.add_conditions(uut.createBleeding(1.0, fractalThreshold - 1))
    uut.relic_of_the_fractal()
    assert(target.getConditions.size == fractalThreshold - 1)
    assert(player.getFractalCd == 0.0)

    //Trigger relic when at or above threshold
    target.add_condition(new Bleeding(1.0))
    uut.relic_of_the_fractal()

    var burnCount = 0
    var tormentCount = 0
    for (condition <- target.getConditions) {
      condition match {
        case _: Burning => burnCount += 1
        case _: Torment => tormentCount += 1
        case _ => "nothing"
      }
    }

    assert(burnCount == fractalBurning)
    assert(tormentCount == fractalTorment)
    assert(target.getConditions.size == fractalThreshold + fractalBurning + fractalTorment)
  }

  private def testKingOfFires(): Unit = {
    println("Running King of Fires Test")
    val kingOfFiresBurning = 3
    val target = new Target(1000000.0)
    val player = new CondiWarrior(0, 0, 0, 0, 0)
    uut.setTarget(target)
    uut.setPlayer(player)

    uut.king_of_fires()
    assert(target.getConditions.size == kingOfFiresBurning)
  }

}
