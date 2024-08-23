package CondiWarrior.src.main.Conditions

class Condition(base: Double, coefficient: Double, duration: Double) {
  private var internal_duration = duration

  /**
   *
   * @return The remaining duration of the condition
   */
  def getDuration: Double = this.internal_duration

  /**
   * Function for calculating condition damage done in 1 second.
   * Primarily used for testing and is thus unused in the code.
   * @param conditionDamage Condition damage to use for damage calculation
   * @return Damage dealt over 1 second
   */
  def tickCondition(conditionDamage: Int): Double = {
    tickCondition(conditionDamage, 1)
  }

  /**
   * Function for calculating the damage done from a condition over a set period of time
   * @param conditionDamage Condition damage to use for damage calculation
   * @param time The amount of time used for the damage calculation
   * @return Damage dealt over the specified amount of time
   */
  def tickCondition(conditionDamage: Int, time: Double): Double = {
    if(time <= internal_duration){
      calculateDamage(conditionDamage, time)
    }
    else{
      calculateRemainderDamage(conditionDamage)
    }
  }

  private def calculateDamage(conditionDamage: Int, time: Double): Double = {
    this.internal_duration = internal_duration - time
    ((coefficient * conditionDamage) + base) * time
  }

  private def calculateRemainderDamage(conditionDamage: Int): Double = {
    val damage = ((coefficient * conditionDamage) + base) * internal_duration
    this.internal_duration = 0.0
    damage
  }
}
