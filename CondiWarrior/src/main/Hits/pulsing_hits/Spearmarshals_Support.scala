package CondiWarrior.src.main.Hits.pulsing_hits

import CondiWarrior.src.main.Entities.Target
import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Weapons.Weapon

class Spearmarshals_Support(weapon: Weapon, target: Target) extends Pulsing_Hit(weapon, 7, 0.12) {
  private var fuse = 1.0
  private val spearmarshals_support_coeff = 0.6
  private val spearmarshals_support_primary_modifier = 1.25

  override def tick_effect(duration: Double): Unit = {
    time += duration
    if(fuse != 0 && fuse <= time) {
      time -= fuse
      fuse = 0
    }
    if(fuse == 0) {
      while(getInterval <= time) {
        trigger()
        time -= getInterval
        pulses -= 1
      }
    }
  }

  override def trigger(): Unit = {
    val damage = new Direct_Hit(weapon, spearmarshals_support_coeff).single_hit(weapon_strength) * spearmarshals_support_primary_modifier
    target.deal_strike_damage(damage)
  }
}
