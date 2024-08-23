package CondiWarrior.src.main.Hits

import CondiWarrior.src.main.Conditions.{Bleeding, Condition, Poison, Torment}
import CondiWarrior.src.main.Entities.Target
import CondiWarrior.src.main.Weapons.Weapon

/**
 * Object for emulating direct hits
 * @param weapon The weapon the hit originates from
 * @param coefficient The coefficient of the skill used
 */
class Direct_Hit(weapon: Weapon, coefficient: Double) {

  private val VULNERABILITY = 1.25

  private val doom_poison_dur = 8.0

  /**
   * Function for simulating an attack that hits 1 or more times at the same time.
   * It rolls critical hits and applies on hit effects separately for every hit.
   * @param target target of the attack
   * @param attacks number of attacks
   * @return total damage done as a double
   */
  def hit(target: Target, attacks: Int): Double = {
    val weaponStrength = Math.round(weapon.rollWeaponStrength)
    var damage = 0.0

    for(i <- 1 to attacks) {
      damage += single_hit(target, weaponStrength)
    }
    damage
  }

  /**
   * Function for calculate the damage to deal to a target as well as apply effects related to the attack.
   * fx. sigils, on critical effects, on hit effects, etc.
   * @param target Target of the attack
   * @param weaponStrength Weapon strength for the damage calculation
   * @return Damage done as a double
   */
  def single_hit(target: Target, weaponStrength: Long): Double = {
    val player = weapon.getPlayer

    triggerDoomSigil(target)

    if (rollCritical(player.getCriticalChance)) {
      triggerCriticalHitEffects(target)
      calculateDamage(weaponStrength, target) * player.getCriticalModifier * VULNERABILITY
    } else calculateDamage(weaponStrength, target)  * VULNERABILITY
  }

  private def triggerDoomSigil(target: Target): Unit = {
    if (weapon.getPlayer.getDoomReady) {
      target.add_conditions(
        List[Condition](
          new Poison(doom_poison_dur * weapon.getPlayer.getConditionDuration),
          new Poison(doom_poison_dur * weapon.getPlayer.getConditionDuration),
          new Poison(doom_poison_dur * weapon.getPlayer.getConditionDuration)
        )
      )
      weapon.getPlayer.setDoomReady(false)
    }
  }

  private def rollCritical(critical_chance: Double): Boolean = {
    val conditions = weapon.getTarget.getConditions
    for (condition <- conditions) {
      if (condition.isInstanceOf[Bleeding]) {
        return Math.random() * 100.0 <= critical_chance + 5.0
      }
    }
    Math.random() * 100.0 <= critical_chance
  }

  private def triggerCriticalHitEffects(target: Target): Unit = {
    weapon.getPlayer.add_furious_stack()
    triggerKingOfFiresIcd()

    rollBloodlustBleed(target)
    rollFood(target)

    triggerEarthSigil(target)
    triggerTormentSigil(target)
  }

  private def triggerKingOfFiresIcd(): Unit = {
    if (weapon.getPlayer.getKingOfFiresCD == 0.0) {
      weapon.getPlayer.setKingOfFiresCD()
      weapon.getPlayer.setFireAura(5.0)
    }
  }

  private def rollBloodlustBleed(target: Target): Unit = {
    if(Math.random() * 100.0 <= 33.0){
      target.add_condition(
        new Bleeding(3.0 * (weapon.getPlayer.getConditionDuration + 0.33))
      )
      weapon.relic_of_the_fractal()
    }
  }

  private  def rollFood(target: Target): Unit = {
    if(Math.random() * 100.0 <= 66.0 && weapon.getPlayer.getFoodCd == 0.0) {
      target.deal_strike_damage(325.0)
      weapon.getPlayer.setFoodCd()
    }
  }

  private def triggerEarthSigil(target: Target): Unit = {
    if (weapon.getPlayer.getOnSword && weapon.getPlayer.getEarthCd == 0.0) {
      target.add_condition(
        new Bleeding(6.0 * (weapon.getPlayer.getConditionDuration + 0.33))
      )
      weapon.getPlayer.setEarthCd()
      weapon.relic_of_the_fractal()
    }
  }

  private def triggerTormentSigil(target: Target): Unit = {
    if (!weapon.getPlayer.getOnSword && weapon.getPlayer.getTormentCd == 0.0) {
      target.add_conditions(
        List[Condition](
          new Torment(5.0 * weapon.getPlayer.getConditionDuration),
          new Torment(5.0 * weapon.getPlayer.getConditionDuration)
        )
      )
      weapon.getPlayer.setTormentCd()
    }
  }

  private def calculateDamage(weaponStrength: Double, target: Target): Double = weaponStrength * weapon.getPlayer.getPower * coefficient / target.getArmour

}
