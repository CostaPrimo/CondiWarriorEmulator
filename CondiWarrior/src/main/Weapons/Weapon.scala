package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Entities.{CondiWarrior, Target}
import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Conditions.{Bleeding, Burning, Condition, Torment}

class Weapon(weapon_strength_midpoint: Double, variance: Double) {
  private var player: CondiWarrior = null
  private var target: Target = null

  //CONFIG ----------------------------
  private val king_of_fires_coeff = 0.44
  private val king_of_fires_attacks = 1
  private val king_of_fires_burning_dur = 3.0
  private val king_of_fires_burning_amount = 3

  private val fractal_bleeding_threshold = 6
  private val fractal_burning_dur = 8.0
  private val fractal_burning_amount = 2
  private val fractal_torment_dur = 8.0

  private val burning_finisher_dur = 1.0
  private val burning_finisher_amount = 1
  //-----------------------------------

  def setPlayer(condiWarrior: CondiWarrior): Unit= {
    this.player = condiWarrior
  }

  def setTarget(target: Target): Unit = {
    this.target = target
  }

  def getPlayer: CondiWarrior = this.player
  def getTarget: Target = this.target

  def rollWeaponStrength: Double = {
    weapon_strength_midpoint - variance + (Math.random() * variance * 2)
  }

  def tick_time(time: Double): Unit = {
    target.tick_conditions(getPlayer.getConditionDamage, time)
    target.addTime(time)
    getPlayer.tick_warrior(time)
  }

  def createBurning(duration: Double, amount: Int): List[Condition] = {
    var conditions = List[Condition]()
    for (i <- 1 to amount) {
      conditions = new Burning(duration * burningDuration) :: conditions
    }
    conditions
  }

  def createBleeding(duration: Double, amount: Int): List[Condition] = {
    var conditions = List[Condition]()
    for (i <- 1 to amount) {
      conditions = new Bleeding(duration * bleedingDuration) :: conditions
    }
    conditions
  }

  def king_of_fires(): Unit = {
    val CONDITIONS = createBurning(king_of_fires_burning_dur, king_of_fires_burning_amount)
    getTarget.add_conditions(CONDITIONS)
    val damage = new Direct_Hit(this, king_of_fires_coeff).hit(king_of_fires_attacks)
    getPlayer.setFireAura(0.0)

    getTarget.deal_strike_damage(damage)
  }

  /**
   * Trigger Relic of the Fractal if the relic is off cooldown as sufficient bleeding is applied to the target.
   */
  def relic_of_the_fractal(): Unit = {
    if(getPlayer.getFractalCd == 0.0) {

      var bleeding_count = 0
      for (condition <- getTarget.getConditions) {
        if (condition.isInstanceOf[Bleeding]) bleeding_count += 1
      }

      if(fractal_bleeding_threshold <= bleeding_count) {
        val conditions = createBurning(fractal_burning_dur, fractal_burning_amount)
        getTarget.add_conditions(
          List[Condition](
            new Torment(fractal_torment_dur * condiDuration),
            new Torment(fractal_torment_dur * condiDuration),
            new Torment(fractal_torment_dur * condiDuration)
          ) ::: conditions
        )
        getPlayer.setFractalCd()
      }
    }
  }

  def projectile_finisher(finisher_chance: Int): Unit = {
    if(getPlayer.hasActiveFireField) {
      if(roll_projectile_finisher(finisher_chance)) {
        getTarget.add_conditions(createBurning(burning_finisher_dur, burning_finisher_amount))
      }
    }
  }

  private def bleedingDuration(): Double = {
    burningDuration();
  }

  private def burningDuration(): Double = {
    getPlayer.getConditionDuration + 0.33
  }

  private def condiDuration: Double = {
    getPlayer.getConditionDuration
  }

  private def roll_projectile_finisher(chance: Int): Boolean = {
    chance >= Math.random() * 100.0
  }

}
