package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Hits.pulsing_hits.Flames_Of_War
import CondiWarrior.src.main.Hits.Direct_Hit

class Torch extends Weapon(900, 72){

  //CONFIG ----------------------------
  val blaze_breaker_coeff = 0.4
  val blaze_breaker_attacks = 1
  val blaze_breaker_burning_dur = 6.0
  val blaze_breaker_burning_amount = 1
  //-----------------------------------

  def blaze_breaker(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, blaze_breaker_coeff).hit(blaze_breaker_attacks)
    getTarget.deal_strike_damage(damage)

    getTarget.add_conditions(createBurning(blaze_breaker_burning_dur, blaze_breaker_burning_amount))

    if(getPlayer.getFireAura > 0.0) {
      king_of_fires()
    }

  }

  def flames_of_war(cast_time: Double): Unit = {
    tick_time(cast_time)

    val pulsing_Hit = new Flames_Of_War(this, getTarget)
    pulsing_Hit.initialHit()
    getPlayer.addPulsingHit(pulsing_Hit)

    if(getPlayer.getFireAura > 0.0) {
      king_of_fires()
    }
  }

}
