package CondiWarrior.src.main.Hits.pulsing_hits

import CondiWarrior.src.main.Conditions.Burning
import CondiWarrior.src.main.Entities.Target
import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Weapons.Weapon

class Scorched_Earth(weapon: Weapon, target: Target) extends Pulsing_Hit(weapon, 3, 2.0) {

  //CONFIG ----------------------------
  val scorched_earth_coeff = 0.5
  val scorched_earth_burning_dur = 2.5

  val burst_mastery = 1.07
  //-----------------------------------

  override def tick_effect(duration: Double): Unit = {
    if (getInterval <= time + duration) {
      trigger()
      time = time + duration - getInterval
      pulses = pulses - 1
    } else {
      time += duration
    }
  }

  override def trigger(): Unit = {
    val damage = new Direct_Hit(weapon, scorched_earth_coeff).single_hit(weapon_strength) * burst_mastery
    target.deal_strike_damage(damage)
    target.add_condition(
      new Burning(scorched_earth_burning_dur * (weapon.getPlayer.getConditionDuration + 0.33))
    )
  }

  def initialHit(): Unit = {
    trigger()
    pulses -= 1
  }

}
