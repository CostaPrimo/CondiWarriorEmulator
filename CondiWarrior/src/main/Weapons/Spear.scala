package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Hits.pulsing_hits.{Maiming_Spear, Spearmarshals_Support}

class Spear extends Weapon(0, 0){
  //CONFIG ----------------------------
  private val mighty_throw_coeff = 1.2
  private val mighty_throw_secondary_coef = 0.9
  private val mighty_throw_attacks = 1

  private val disrupting_throw_coeff = 2.0
  private val disrupting_throw_attacks = 1
  private val disrupting_throw_finisher_chance = 100

  private val spear_swipe_coeff = 2.0
  private val spear_swipe_attacks = 1

  private val wild_throw_coeff = 0.75
  private val wild_throw_attacks = 7
  private val wild_throw_primary_modifier = 1.1
  //-----------------------------------

  def mighty_throw(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, mighty_throw_coeff).hit(mighty_throw_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def maiming_spear(cast_time: Double): Unit = {
    tick_time(cast_time)

    val pulsing_hit = new Maiming_Spear(this, getTarget)
    pulsing_hit.initialHit()
    getPlayer.addPulsingHit(pulsing_hit)
  }

  def disrupting_throw(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, disrupting_throw_coeff).hit(disrupting_throw_attacks)
    getTarget.deal_strike_damage(damage)

    projectile_finisher(disrupting_throw_finisher_chance)
  }

  def spearmarshals_support(cast_time: Double): Unit = {
    val pulsing_hit = new Spearmarshals_Support(this, getTarget)
    getPlayer.addPulsingHit(pulsing_hit)
    tick_time(cast_time)
  }

  def spear_swipe(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage =  new Direct_Hit(this, spear_swipe_coeff).hit(spear_swipe_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def wild_throw(cast_time: Double): Unit = {
    val weapon_strength = Math.round(this.rollWeaponStrength)
    for (i <- 1 to wild_throw_attacks) {
      val toTick = cast_time / wild_throw_attacks
      tick_time(toTick)
      val damage = new Direct_Hit(this, wild_throw_coeff).single_hit(weapon_strength) * wild_throw_primary_modifier
      getTarget.deal_strike_damage(damage)
    }
  }

}
