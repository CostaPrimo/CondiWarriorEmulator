package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Hits.pulsing_hits.Impale

class Sword extends Weapon (1000, 50){

  //CONFIG ----------------------------
  private val sever_artery_bleeding_dur = 6.0
  private val sever_artery_bleeding_amount = 1
  private val sever_artery_coeff = 0.8
  private val sever_artery_attacks = 1

  private val gash_bleeding_dur = 6.0
  private val gash_bleeding_amount = 1
  private val gash_coeff = 0.8
  private val gash_attacks = 1

  private val hamstring_bleeding_dur = 6.0
  private val hamstring_bleeding_amount = 1
  private val hamstring_coeff = 1.2
  private val hamstring_attacks = 1

  private val savage_leap_coeff = 2.0
  private val savage_leap_attacks = 1

  private val final_thrust_bleeding_dur = 8.0
  private val final_thrust_bleeding_amount = 3
  private val final_thrust_bleeding_amount_sub50 = 6
  private val final_thrust_coeff = 1.5
  private val final_thrust_coeff_sub50 = 3.0
  private val final_thrust_attacks = 1

  private val impale_coeff = 1.5
  private val impale_attacks = 1
  private val impale_finisher_chance = 100

  private val riposte_bleeding_dur = 8.0
  private val riposte_bleeding_amount = 8
  private val riposte_coeff = 1.0
  private val riposte_attacks = 1

  private val flaming_flurry_attacks = 6
  private val flaming_flurry_coeff = 1.4
  private val flaming_flurry_burning_dur = 2.5
  private val flaming_flurry_burning_amount = 1

  private val burst_mastery = 1.07
  //-----------------------------------

  def sever_artery(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, sever_artery_coeff).hit(sever_artery_attacks)
    getTarget.deal_strike_damage(damage)

    getTarget.add_conditions(createBleeding(sever_artery_bleeding_dur, sever_artery_bleeding_amount))
    relic_of_the_fractal()
  }

  def gash(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, gash_coeff).hit(gash_attacks)
    getTarget.deal_strike_damage(damage)

    getTarget.add_conditions(createBleeding(gash_bleeding_dur, gash_bleeding_amount))
    relic_of_the_fractal()
  }

  def hamstring(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, hamstring_coeff).hit(hamstring_attacks)
    getTarget.deal_strike_damage(damage)

    getTarget.add_conditions(createBleeding(hamstring_bleeding_dur, hamstring_bleeding_amount))
    relic_of_the_fractal()
  }

  def savage_leap(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, savage_leap_coeff).hit(savage_leap_attacks)
    getTarget.deal_strike_damage(damage)

    if(getPlayer.hasActiveFireField) {
      this.getPlayer.setFireAura(5.0)
    }
  }

  def final_thrust(cast_time: Double): Unit = {
    tick_time(cast_time)

    if(getTarget.getDamageTaken <= getTarget.getHealth / 2) {
      val damage = new Direct_Hit(this, final_thrust_coeff).hit(final_thrust_attacks)
      getTarget.deal_strike_damage(damage)

      getTarget.add_conditions(createBleeding(final_thrust_bleeding_dur, final_thrust_bleeding_amount))
    } else {
      val damage = new Direct_Hit(this, final_thrust_coeff_sub50).hit(final_thrust_attacks)
      getTarget.deal_strike_damage(damage)

      getTarget.add_conditions(createBleeding(final_thrust_bleeding_dur, final_thrust_bleeding_amount_sub50))
    }

    relic_of_the_fractal()
  }

  def impale(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, impale_coeff).hit(impale_attacks)
    getTarget.deal_strike_damage(damage)

    val pulsing_Hit = new Impale(this, getTarget)
    pulsing_Hit.initialHit()
    getPlayer.addPulsingHit(pulsing_Hit)

    projectile_finisher(impale_finisher_chance)
  }

  def riposte(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, riposte_coeff).hit(riposte_attacks)
    getTarget.deal_strike_damage(damage)

    getTarget.add_conditions(createBleeding(riposte_bleeding_dur, riposte_bleeding_amount))
    relic_of_the_fractal()
  }

  def flaming_flurry(duration: Double): Unit = {
    val weapon_strength = Math.round(this.rollWeaponStrength)
    for(i <- 1 to flaming_flurry_attacks){
      val toTick = duration/ flaming_flurry_attacks
      tick_time(toTick)
      val damage = new Direct_Hit(this, flaming_flurry_coeff / flaming_flurry_attacks).single_hit(weapon_strength) * burst_mastery
      getTarget.deal_strike_damage(damage)
      getTarget.add_conditions(createBurning(flaming_flurry_burning_dur, flaming_flurry_burning_amount))
    }

    if (getPlayer.getFireAura > 0.0) {
      king_of_fires()
    }

  }

}
