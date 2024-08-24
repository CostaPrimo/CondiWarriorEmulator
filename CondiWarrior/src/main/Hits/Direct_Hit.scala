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
  def hit(attacks: Int): Double = {
    val weaponStrength = Math.round(weapon.rollWeaponStrength)
    var damage = 0.0

    for(i <- 1 to attacks) {
      damage += single_hit(weaponStrength)
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
  def single_hit(weaponStrength: Long): Double = {
    val player = weapon.getPlayer

    triggerDoomSigil()

    if (rollCritical(player.getCriticalChance)) {
      triggerCriticalHitEffects()
      calculateDamage(weaponStrength) * player.getCriticalModifier * VULNERABILITY
    } else calculateDamage(weaponStrength)  * VULNERABILITY
  }

  /**
   * Trigger Doom Sigil if ready
   */
  private def triggerDoomSigil(): Unit = {
    if (weapon.getPlayer.getDoomReady) {
      weapon.getTarget.add_conditions(
        List[Condition](
          new Poison(doom_poison_dur * weapon.getPlayer.getConditionDuration),
          new Poison(doom_poison_dur * weapon.getPlayer.getConditionDuration),
          new Poison(doom_poison_dur * weapon.getPlayer.getConditionDuration)
        )
      )
      weapon.getPlayer.setDoomReady(false)
    }
  }

  /**
   * Check if target has bleeding condition, if so add 5% to critical chance.
   * Else calculate critical chance normally.
   * @param critical_chance
   * @return Critical boolean
   */
  private def rollCritical(critical_chance: Double): Boolean = {
    val conditions = weapon.getTarget.getConditions
    for (condition <- conditions) {
      if (condition.isInstanceOf[Bleeding]) {
        return Math.random() * 100.0 <= critical_chance + 5.0
      }
    }
    Math.random() * 100.0 <= critical_chance
  }

  /**
   * Trigger effects related to critical hits
   */
  private def triggerCriticalHitEffects(): Unit = {
    weapon.getPlayer.add_furious_stack()
    triggerKingOfFiresIcd()

    rollBloodlustBleed()
    rollFood()

    triggerEarthSigil()
    triggerTormentSigil()
  }

  /**
   * Handle King of Fires ICD
   */
  private def triggerKingOfFiresIcd(): Unit = {
    if (weapon.getPlayer.getKingOfFiresCD == 0.0) {
      weapon.getPlayer.setKingOfFiresCD()
      weapon.getPlayer.setFireAura(5.0)
    }
  }

  /**
   * Roll bloodlust bleed chance and attempt to trigger fractal relic
   */
  private def rollBloodlustBleed(): Unit = {
    if(Math.random() * 100.0 <= 33.0){
      weapon.getTarget.add_condition(
        new Bleeding(3.0 * (weapon.getPlayer.getConditionDuration + 0.33))
      )
      weapon.relic_of_the_fractal()
    }
  }

  /**
   * Roll ascended food lifesteal chance
   */
  private  def rollFood(): Unit = {
    if(Math.random() * 100.0 <= 66.0 && weapon.getPlayer.getFoodCd == 0.0) {
      weapon.getTarget.deal_strike_damage(325.0)
      weapon.getPlayer.setFoodCd()
    }
  }

  /**
   * Trigger earth sigil if on sword and sigil is off cooldown.
   * Attempt to trigger relic of the fractal when applying bleeding.
   */
  private def triggerEarthSigil(): Unit = {
    if (weapon.getPlayer.isOnSword && weapon.getPlayer.getEarthCd == 0.0) {
      weapon.getTarget.add_condition(
        new Bleeding(6.0 * (weapon.getPlayer.getConditionDuration + 0.33))
      )
      weapon.getPlayer.setEarthCd()
      weapon.relic_of_the_fractal()
    }
  }

  /**
   * Trigger Torment sigil if on longbow and sigil is off cooldown
   */
  private def triggerTormentSigil(): Unit = {
    if (!weapon.getPlayer.isOnSword && weapon.getPlayer.getTormentCd == 0.0) {
      weapon.getTarget.add_conditions(
        List[Condition](
          new Torment(5.0 * weapon.getPlayer.getConditionDuration),
          new Torment(5.0 * weapon.getPlayer.getConditionDuration)
        )
      )
      weapon.getPlayer.setTormentCd()
    }
  }

  /**
   * Calculates damage based on the formula `WeaponStrength * power * coefficient / targetArmour`
   * @param weaponStrength
   * @return Damage dealt
   */
  private def calculateDamage(weaponStrength: Double): Double = weaponStrength * weapon.getPlayer.getPower * coefficient / weapon.getTarget.getArmour

}
