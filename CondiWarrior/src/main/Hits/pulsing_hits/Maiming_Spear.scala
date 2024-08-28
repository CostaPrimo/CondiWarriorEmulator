package CondiWarrior.src.main.Hits.pulsing_hits

import CondiWarrior.src.main.Entities.Target
import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Weapons.Weapon

case class Maiming_Spear(weapon: Weapon, target: Target) extends Pulsing_Hit(weapon, 1, 0.5) {

  private val maiming_spear_coeff = 1.2
  private val maiming_spear_aftershock_coeff = 1.2
  private val maiming_spear_aftershock_modifier = 1.5

  override def tick_effect(duration: Double): Unit = {
    time += duration

    if(getInterval <= time) {
      trigger()
      time -= getInterval
      pulses -= 1
    }
  }

  override def trigger(): Unit = {
    val damage = new Direct_Hit(weapon, maiming_spear_aftershock_coeff).single_hit(weapon_strength) * maiming_spear_aftershock_modifier
    target.deal_strike_damage(damage)
  }

  def initialHit(): Unit = {
    val damage = new Direct_Hit(weapon, maiming_spear_coeff).single_hit(weapon_strength)
    target.deal_strike_damage(damage)
  }
}
