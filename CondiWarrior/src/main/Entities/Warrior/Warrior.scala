package CondiWarrior.src.main.Entities.Warrior

import CondiWarrior.src.main.Hits.pulsing_hits.Pulsing_Hit
import CondiWarrior.src.main.Util.FIELD
import CondiWarrior.src.main.Weapons.Weapon

abstract class Warrior(precision: Int, ferocity: Int, conditionDamage: Int, expertise: Int, power: Int) {
  private val BASE_CRITICAL_MODIFIER: Double = 1.5
  private val BASE_CRITICAL_CHANCE = 5

  private var pulsing_hits: List[Pulsing_Hit] = List()
  private var food_cd: Double = 0.0

  def getCriticalChance: Double
  def getCriticalModifier: Double
  def getConditionDuration: Double
  def getConditionDamage: Int
  def getPower: Int
  def weapon_swap(): Unit
  def getField: FIELD

  def tick_warrior(time: Double): Unit

  def addPulsingHit(hit: Pulsing_Hit): Unit = pulsing_hits = hit :: pulsing_hits
  def tick_pulsing_hits(time: Double): Unit = {
    var templist = List[Pulsing_Hit]()
    for (hit <- pulsing_hits) {
      hit.tick_effect(time)
      if (hit.getPulses > 0) {
        templist = hit :: templist
      }
    }
    pulsing_hits = templist
  }

  def getFoodCd: Double = this.food_cd
  def setFoodCd(): Unit = this.food_cd = 2.0
  def tick_food(time: Double): Unit = {
    if (food_cd > time) food_cd -= time
    else food_cd = 0.0
  }
}
