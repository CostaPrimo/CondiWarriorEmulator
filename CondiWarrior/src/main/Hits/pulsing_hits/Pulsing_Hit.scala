package CondiWarrior.src.main.Hits.pulsing_hits

import CondiWarrior.src.main.Weapons.Weapon

abstract class Pulsing_Hit(weapon: Weapon, var pulses: Int, interval: Double) {

  var time = 0.0
  val weapon_strength: Long = Math.round(weapon.rollWeaponStrength)

  def getPulses: Int = this.pulses
  def getInterval: Double = this.interval

  def tick_effect(duration: Double): Unit

  def trigger(): Unit
}
