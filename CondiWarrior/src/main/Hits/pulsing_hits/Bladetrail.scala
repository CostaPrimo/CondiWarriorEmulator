package CondiWarrior.src.main.Hits.pulsing_hits

import CondiWarrior.src.main.Entities.Target
import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Weapons.Weapon

// TODO: Find actual delay for 2nd hit
class Bladetrail(weapon: Weapon, target: Target) extends Pulsing_Hit(weapon, 1, 2.0){
  private val bladetrail_coeff = 1.5
  private val bladetrail_finisher_chance = 100

  override def tick_effect(duration: Double): Unit = {
    time += duration

    if (getInterval <= time) {
      time -= getInterval
      trigger()
      pulses -= 1
    }
  }

  override def trigger(): Unit = {
    val damage = new Direct_Hit(weapon, bladetrail_coeff).single_hit(weapon_strength)
    target.deal_strike_damage(damage)
    weapon.projectile_finisher(bladetrail_finisher_chance)
  }
}
