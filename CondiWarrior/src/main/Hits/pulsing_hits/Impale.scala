package CondiWarrior.src.main.Hits.pulsing_hits

import CondiWarrior.src.main.Conditions.Torment
import CondiWarrior.src.main.Entities.Target
import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Weapons.Weapon

class Impale(weapon: Weapon, target: Target) extends Pulsing_Hit(weapon, 5, 1.0) {

  //CONFIG ----------------------------
  private val impale_torment_duration = 8.0
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
    target.add_condition(
      new Torment(impale_torment_duration * weapon.getPlayer.getConditionDuration)
    )
  }

  def initialHit(): Unit = {
    trigger()
    pulses -= 1
  }
}
