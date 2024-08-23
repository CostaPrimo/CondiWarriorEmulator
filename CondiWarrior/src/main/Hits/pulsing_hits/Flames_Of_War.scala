package CondiWarrior.src.main.Hits.pulsing_hits

import CondiWarrior.src.main.Conditions.{Burning, Condition}
import CondiWarrior.src.main.Entities.Target
import CondiWarrior.src.main.Hits.Direct_Hit
import CondiWarrior.src.main.Weapons.Weapon

class Flames_Of_War(weapon: Weapon, target: Target) extends Pulsing_Hit(weapon, 5, 1.0) {


  //CONFIG ----------------------------
  private val flames_of_war_tick_burning_dur = 2.0
  private val flames_of_war_final_burning_dur = 6.0
  private val flames_of_War_coeff = 1.0
  //-----------------------------------

  override def tick_effect(duration: Double): Unit = {
    if (getInterval <= time + duration) {
      pulses = pulses - 1
      if(pulses>0) {
        trigger()
        time = time + duration - getInterval
      } else {
        finalHit()
      }

    } else {
      time += duration
    }
  }

  override def trigger(): Unit = {
    target.add_conditions(
      List[Condition](
        new Burning(flames_of_war_tick_burning_dur * conditionDuration())
      )
    )
  }

  def initialHit(): Unit = {
    trigger()
    pulses -= 1
  }

  def finalHit(): Unit = {
    val damage = new Direct_Hit(weapon, flames_of_War_coeff).single_hit(target, weapon_strength)
    target.deal_strike_damage(damage)

    target.add_conditions(
      List[Condition](
        new Burning(flames_of_war_final_burning_dur * conditionDuration()),
        new Burning(flames_of_war_final_burning_dur * conditionDuration())
      )
    )
  }

  private def conditionDuration(): Double = {
    weapon.getPlayer.getConditionDuration + 0.33
  }
}
