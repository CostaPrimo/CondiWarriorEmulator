package CondiWarrior.src.test

import CondiWarrior.src.main.Entities.{CondiWarrior, Target}
import _root_.CondiWarrior.src.main.Conditions.{Bleeding, Burning, Torment}
import _root_.CondiWarrior.src.main.Weapons.Weapon

object WeaponTest {

  val uut = new Weapon(0, 0)
  var target: Target = null
  var player: CondiWarrior = null

  def runWeaponTests(): Unit = {
    testFractalRelic()
    testKingOfFires()
    testCreateConditions()
  }

  private def testCreateConditions(): Unit = {
    println("Running Create Conditions Test")
    initWeapon()
    val amount = 5
    val duration = 1.0

    val bleeding = uut.createBleeding(duration, amount)
    assert(bleeding.size == amount)
    for(condition <- bleeding) {
      assert(condition.getDuration == duration * (player.getConditionDuration + 0.33))
    }

    val burning = uut.createBleeding(duration, amount)
    assert(burning.size == amount)
    for (condition <- burning) {
      assert(condition.getDuration == duration * (player.getConditionDuration + 0.33))
    }
  }

  private def testFractalRelic(): Unit ={
    println("Running Fractal Test")
    initWeapon()
    val fractalThreshold = 6
    val fractalBurning = 2
    val fractalTorment = 3

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
    initWeapon()
    val kingOfFiresBurning = 3
    uut.king_of_fires()
    assert(target.getConditions.size == kingOfFiresBurning)
  }

  private def initWeapon(): Unit = {
    target = new Target(1000000.0)
    player = new CondiWarrior(0, 0, 0, 0, 0)
    uut.setTarget(target)
    uut.setPlayer(player)
  }
}
