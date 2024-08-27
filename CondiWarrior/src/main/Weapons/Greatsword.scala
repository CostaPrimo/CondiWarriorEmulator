package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Hits.Direct_Hit

class Greatsword extends Weapon(1100, 55) {
  //CONFIG ----------------------------
  private val greatsword_swing_coeff = 0.8
  private val greatsword_swing_attacks = 1

  private val greatsword_slice_coeff = 1.05
  private val greatsword_slice_attacks = 1

  private val brutal_strike_coeff = 1.5
  private val brutal_strike_attacks = 1

  // TODO: Figure out how tf weaponstrength works with this
  private val hundred_blades_coeff = 0.5775
  private val hundreds_blades_attacks = 8
  private val hundred_blades_final_coeff = 1.21
  private val hundred_blades_final_attacks = 1

  private val whirlwind_attack_coeff = 0.665
  private val whirlwind_attack_attacks = 4
  private val whirlwind_attack_finisher_chance = 100

  private val bladetrail_coeff = 1.5
  private val bladetrail_attacks = 2
  private val bladetrail_finisher_chance = 100

  private val rush_coeff = 2.5
  private val rush_attacks = 1

  private val arc_divider_coeff = 3.5
  private val arc_divider_attacks = 1

  private val burning_finisher_dur = 1.0
  private val burning_finisher_amount = 1
  //-----------------------------------

  def greatsword_swing(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, greatsword_swing_coeff).hit(greatsword_swing_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def greatsword_slice(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, greatsword_slice_coeff).hit(greatsword_slice_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def brutal_strike(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, brutal_strike_coeff).hit(brutal_strike_attacks)
    getTarget.deal_strike_damage(damage)
  }

  // TODO
  def hundred_blades(cast_time: Double): Unit = {
    tick_time(cast_time)
  }

  def whirlwind_attack(cast_time: Double): Unit = {
    val weapon_strength = Math.round(this.rollWeaponStrength)
    for (i <- 1 to whirlwind_attack_attacks) {
      val toTick = cast_time / whirlwind_attack_attacks
      tick_time(toTick)
      val damage = new Direct_Hit(this, whirlwind_attack_coeff).single_hit(weapon_strength)
      getTarget.deal_strike_damage(damage)

      projectile_finisher(whirlwind_attack_finisher_chance)
    }
  }

  // TODO
  def bladetrail(cast_time: Double): Unit = {
    tick_time(cast_time)
    val weapon_strength = Math.round(this.rollWeaponStrength)

    val damage = new Direct_Hit(this, bladetrail_coeff).single_hit(weapon_strength)
    projectile_finisher(bladetrail_finisher_chance)

    //val pulsing_hit = new BladeTrail(this, getTarget, weaponStrength)
    //getPlayer.addPulsingHit(pulsing_hit)
  }

  def rush(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, rush_coeff).hit(rush_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def arc_divider(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, arc_divider_coeff).hit(arc_divider_attacks)
    getTarget.deal_strike_damage(damage)
  }

}
