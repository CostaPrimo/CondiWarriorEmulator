package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Conditions.Condition
import CondiWarrior.src.main.Hits.pulsing_hits.Scorched_Earth
import CondiWarrior.src.main.Hits.Direct_Hit

class Longbow extends Weapon(1050, 84){

  //CONFIG ----------------------------
  private val dual_shot_coeff = 0.525
  private val dual_shot_attacks = 2
  private val dual_shot_burning_dur = 1.0
  private val dual_shot_burning_amount= 2
  private val dual_shot_finisher_chance = 20

  private val fan_of_fire_coeff = 0.44
  private val fan_of_fire_attacks = 3
  private val fan_of_fire_burning_dur = 3.0
  private val fan_of_fire_burning_amount = 3

  private val arcing_arrow_coeff = 2.5
  private val arcing_arrow_attacks = 1
  private val arcing_arrow_burning_dur = 5.0
  private val arcing_arrow_burning_amount = 1

  private val pin_down_coeff = 0.44
  private val pin_down_attacks = 1
  private val pin_down_bleeding_dur = 12.0
  private val pin_down_bleeding_amount = 6
  private val pin_down_finisher_chance = 100
  //-----------------------------------

  def dual_shot(cast_time: Double): Unit = {
    //val CONDITIONS = createBurning(dual_shot_burning_dur, dual_shot_burning_amount)
    val CONDITIONS = List[Condition]()
    tick_time(cast_time)

    val damage = new Direct_Hit(this, dual_shot_coeff).hit(dual_shot_attacks)
    getTarget.deal_strike_damage(damage)

    for(i <- 1 to dual_shot_attacks){
      projectile_finisher(dual_shot_finisher_chance)
    }

    getTarget.add_conditions(CONDITIONS)
  }

  def fan_of_fire(cast_time: Double): Unit = {
    val CONDITIONS = createBurning(fan_of_fire_burning_dur, fan_of_fire_burning_amount)

    tick_time(cast_time)

    val damage = new Direct_Hit(this, fan_of_fire_coeff).hit(fan_of_fire_attacks)
    getTarget.deal_strike_damage(damage)

    getTarget.add_conditions(CONDITIONS)
  }

  def arcing_arrow(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, arcing_arrow_coeff).hit(arcing_arrow_attacks)
    getTarget.deal_strike_damage(damage)

    getTarget.add_conditions(createBurning(arcing_arrow_burning_dur, arcing_arrow_burning_amount))
  }

  def pin_down(cast_time: Double): Unit = {
    var CONDITIONS = createBleeding(pin_down_bleeding_dur, pin_down_bleeding_amount)

    tick_time(cast_time)

    val damage = new Direct_Hit(this, pin_down_coeff).hit(pin_down_attacks)
    getTarget.deal_strike_damage(damage)

    projectile_finisher(pin_down_finisher_chance)

    getTarget.add_conditions(CONDITIONS)
    relic_of_the_fractal()
  }

  def scorched_earth(cast_time: Double): Unit = {
    tick_time(cast_time)

    val pulsing_Hit = new Scorched_Earth(this, getTarget)
    pulsing_Hit.initialHit()
    getPlayer.addPulsingHit(pulsing_Hit)

    if(getPlayer.getFireAura > 0.0) {
      king_of_fires()
    }
  }

}
