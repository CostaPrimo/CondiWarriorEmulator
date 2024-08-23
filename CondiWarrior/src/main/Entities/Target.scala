package CondiWarrior.src.main.Entities

import CondiWarrior.src.main.Conditions.Condition

class Target(health: Double) {
  private var conditions = List[Condition]()
  private var damage_taken = 0.0
  private var time = 0.0
  private val armour = 2597.0

  private val VULNERABILITY = 1.25

  def getDamageTaken: Double = damage_taken
  def getArmour: Double = armour
  def getTime: Double = time
  def getHealth: Double = health
  def getConditions: List[Condition] = conditions

  def tick_conditions(conditionDamage: Int): Unit = {
    tick_conditions(conditionDamage, 1)
  }

  def tick_conditions(conditionDamage: Int, time: Double): Unit = {
    var tempList = List[Condition]()
    for (stack <- conditions) {
      val damage_to_take = stack.tickCondition(conditionDamage, time) * VULNERABILITY
      this.damage_taken = this.damage_taken + damage_to_take
      if (stack.getDuration > 0.0) tempList = stack :: tempList
    }

    this.conditions = tempList
  }

  def add_condition(condi: Condition): Unit = {
    add_conditions(List(condi))
  }

  def add_conditions(conditions: List[Condition]): Unit = {
    this.conditions = this.conditions ::: conditions
  }

  def deal_strike_damage(damage: Double): Unit = {
    this.damage_taken = damage_taken + (damage * 1.1)
  }

  def addTime(duration: Double): Unit = {
    this.time += duration
  }

}
