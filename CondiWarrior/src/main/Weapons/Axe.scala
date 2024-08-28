package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Hits.Direct_Hit

class Axe extends Weapon(1000, 100) {
  //CONFIG ----------------------------
  private val chop_coef = 0.7
  private val chop_attacks = 1

  private val double_chop_coeff = 0.75
  private val double_chop_attacks = 2

  private val triple_chop_coeff = 0.75
  private val triple_chop_attacks = 2
  private val triple_chop_final_coeff = 1.6
  private val triple_chop_final_attacks = 1

  private val cyclone_axe_coeff = 0.88
  private val cyclone_axe_attacks = 2
  private val cyclone_axe_finisher_chance = 100

  private val throw_axe_coeff = 0.85
  private val throw_axe_attacks = 1
  private val throw_axe_finisher_chance = 100

  private val dual_strike_coeff = 1.175
  private val dual_strike_attacks = 2

  private val whirling_axe_coeff = 0.5592
  private val whirling_axe_attacks = 15
  private val whirling_axe_finisher_chance = 100

  private val decapitate_coeff = 3.0
  private val decapitate_attacks = 1
  //-----------------------------------

  def chop(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, chop_coef).hit(chop_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def double_chop(cast_time: Double): Unit = {
    val weapon_strength = Math.round(this.rollWeaponStrength)

    for(i <- 1 to double_chop_attacks) {
      tick_time(cast_time / double_chop_attacks)
      val damage = new Direct_Hit(this, double_chop_coeff).single_hit(weapon_strength)
      getTarget.deal_strike_damage(damage)
    }
  }

  def triple_chop(cast_time: Double): Unit = {
    val weapon_strength = Math.round(this.rollWeaponStrength)

    for(i <- 1 to triple_chop_attacks) {
      tick_time(cast_time / (triple_chop_attacks + triple_chop_final_attacks))
      val damage = new Direct_Hit(this, triple_chop_coeff).single_hit(weapon_strength)
      getTarget.deal_strike_damage(damage)
    }
    tick_time(cast_time / (triple_chop_attacks + triple_chop_final_attacks))
    val damage = new Direct_Hit(this, triple_chop_final_coeff).single_hit(weapon_strength)
    getTarget.deal_strike_damage(damage)
  }

  def cyclone_axe(cast_time: Double): Unit = {
    val weapon_strength = Math.round(this.rollWeaponStrength)

    for (i <- 1 to cyclone_axe_attacks) {
      tick_time(cast_time / cyclone_axe_attacks)
      val damage = new Direct_Hit(this, cyclone_axe_coeff).single_hit(weapon_strength)
      getTarget.deal_strike_damage(damage)
      projectile_finisher(cyclone_axe_finisher_chance)
    }
  }

  def throw_axe(cast_time: Double): Unit = {
    tick_time(cast_time)

    var damage = new Direct_Hit(this, throw_axe_coeff).hit(throw_axe_attacks)
    if(getTarget.getHealth * 0.75 <=  getTarget.getDamageTaken) {
      damage *= 2.0
    } else if (getTarget.getHealth * 0.5 <= getTarget.getDamageTaken) {
      damage *= 1.5
    }
    getTarget.deal_strike_damage(damage)

    projectile_finisher(throw_axe_finisher_chance)
  }

  def dual_strike(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, dual_strike_coeff).hit(dual_strike_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def whirling_axe(cast_time: Double): Unit = {
    val weapon_strength = Math.round(this.rollWeaponStrength)

    for(i <- 1 to whirling_axe_attacks) {
      tick_time(cast_time / whirling_axe_attacks)
      val damage = new Direct_Hit(this, whirling_axe_coeff).single_hit(weapon_strength)
      getTarget.deal_strike_damage(damage)
      projectile_finisher(whirling_axe_finisher_chance)
    }
  }

  def decapitate(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, decapitate_coeff).hit(decapitate_attacks)
    getTarget.deal_strike_damage(damage)
  }
}
