package CondiWarrior.src.main.Entities

import CondiWarrior.src.main.Hits.pulsing_hits.{Flames_Of_War, Pulsing_Hit, Scorched_Earth}

class CondiWarrior(precision: Int, ferocity: Int, conditionDamage: Int, expertise: Int, power: Int) {
  //Constants
  private val SWORD_BONUS = 120
  private val BERSERK_BONUS = 300
  private val FURIOUS_SURGE_BONUS = 15
  private val BASE_CRITICAL_CHANCE = 5
  private val FURY = 30
  private val BASE_CRITICAL_MODIFIER = 1.5

  //Warrior Specific buffs
  private var furious_surge_stacks = List[Double]()
  private var onSword = true
  private var inBerserk = false
  private var berserkDuration = 0.0
  private var fire_aura = 0.0
  private var king_of_fires_cd = 0.0

  //Sigils
  private var earth_cd = 0.0
  private var torment_cd = 0.0
  private var doom_ready = false
  private var food_cd = 0.0
  private var fractal_cd = 0.0

  //Rune
  private val TRAPPER_BONUS = 0.15

  //Pulsing Effects
  private var pulsing_hits = List[Pulsing_Hit]()

  def startBerserk(): Unit = {
    this.inBerserk = true
    this.berserkDuration = 20.0
  }

  def getOnSword: Boolean = onSword

  def getFuriousSurge: List[Double] = furious_surge_stacks

  def getDoomReady: Boolean = doom_ready

  def setDoomReady(bool: Boolean): Unit = doom_ready = bool

  def weapon_swap(): Unit = { this.onSword = !this.onSword}

  def getCriticalChance: Double = BASE_CRITICAL_CHANCE + FURY + ((this.precision - 1000.0 ) / 21.0)

  def getCriticalModifier: Double = {
    if(inBerserk){
      BASE_CRITICAL_MODIFIER + (((this.ferocity + addBloodReaction("POWER"))/ 15.0)/100)
    }else BASE_CRITICAL_MODIFIER + ((this.ferocity / 15.0) / 100.0)
  }

  def getConditionDuration: Double = 1 + ((this.expertise / 15.0) / 100.0) + TRAPPER_BONUS

  def getConditionDamage: Int = {
    var toAdd = 0
    if (inBerserk) toAdd = toAdd + BERSERK_BONUS + addBloodReaction("CONDI")
    if (onSword) toAdd += SWORD_BONUS
    toAdd += (FURIOUS_SURGE_BONUS * furious_surge_stacks.size)
    this.conditionDamage + toAdd
  }

  def getPower: Int = {
    if (inBerserk) this.power + BERSERK_BONUS
    else this.power
  }

  def setFireAura(duration: Double): Unit = { this.fire_aura = duration }

  def getInBerserk: Boolean = this.inBerserk

  def getFireAura: Double = this.fire_aura

  def getKingOfFiresCD: Double = this.king_of_fires_cd

  def setKingOfFiresCD(): Unit = this.king_of_fires_cd = 15.0

  def getEarthCd: Double = this.earth_cd;

  def setEarthCd(): Unit = this.earth_cd = 2.0

  def getTormentCd: Double = this.torment_cd

  def setTormentCd(): Unit = this.torment_cd = 5.0

  def getFoodCd: Double = this.food_cd

  def setFoodCd(): Unit = this.food_cd = 2.0

  def getFractalCd: Double = this.fractal_cd

  def setFractalCd(): Unit = this.fractal_cd = 20.0

  def addPulsingHit(hit: Pulsing_Hit): Unit = pulsing_hits = hit :: pulsing_hits

  /**
   * Determine if a fire field is currently present.
   * @return Fire Field boolean
   */
  def hasActiveFireField: Boolean = {
    for (hit <- pulsing_hits) {
      if (hit.isInstanceOf[Scorched_Earth] || hit.isInstanceOf[Flames_Of_War]) {
        return true
      }
    }
    false
  }

  /**
   * Method for ticking all the various effects on warrior
   * @param time Duration to use for ticking effects
   */
  def tick_warrior(time :Double): Unit = {
    tick_KingOfFiresCD(time)
    tick_furious_stacks(time)
    tick_berserk(time)
    tick_sigils(time)
    tick_pulsing_hits(time)
  }

  def tick_berserk(time: Double): Unit = {
    if(this.inBerserk) {
      if(this.berserkDuration > time) {
        this.berserkDuration -= time
      } else {
        this.berserkDuration = 0.0
        this.inBerserk = false
        println("BERSERKER MODE OFF")
      }
    }
  }

  def tick_KingOfFiresCD(time: Double): Unit = {
    if(this.king_of_fires_cd > 0.0) {
      if(this.king_of_fires_cd>time) {
        this.king_of_fires_cd -= time
      }
      else this.king_of_fires_cd = 0.0
    }
  }

  def tick_sigils(time: Double): Unit = {
    if (earth_cd > time) {
      earth_cd -= time
    } else {
      earth_cd = 0.0
    }

    if (torment_cd > time) {
      torment_cd -= time
    } else {
      torment_cd = 0.0
    }

    if (food_cd > time) {
      food_cd -= time
     } else {
      food_cd = 0.0
    }

    if(fractal_cd > time) {
      fractal_cd -= time
    } else {
      fractal_cd = 0.0
    }
  }

  def tick_pulsing_hits(time: Double): Unit = {
    var templist = List[Pulsing_Hit]()
    for(hit <- pulsing_hits) {
      hit.tick_effect(time)
      if(hit.getPulses > 0){
        templist = hit :: templist
      }
    }
    pulsing_hits = templist
  }

  /**
   * Add Furious Surge Stack.
   * Handles max size of 25 stacks and replaces the the stack with lowest duration when trying to add a new stack at max size
   */
  def add_furious_stack(): Unit = {
    if(furious_surge_stacks.size == 25) this.furious_surge_stacks = replace_furious_stack()
    else furious_surge_stacks = 10 :: furious_surge_stacks
  }

  /**
   * Reduce the duration for all Furious Surge stacks by the given duration.
   * Remove stacks which are reduced to a duration at or below 0.0 seconds
   * @param time Time to be removed from stacks in seconds
   */
  def tick_furious_stacks(time: Double): Unit = {
    var newList = List[Double]()
    for (furious_surge_stack_duration <- furious_surge_stacks) {
      val newDuration = furious_surge_stack_duration - time
      if (newDuration > 0.0) newList = newDuration :: newList
    }
    this.furious_surge_stacks = newList
  }

  /**
   * Find the furious surge stack which is currently the closest to running out and replace it with a fresh stack
   * @return List of Furious Surge stack durations
   */
  def replace_furious_stack(): List[Double] = {
    var newList = List[Double]()

    var counter = 0
    val minIndex = furious_surge_stacks.indexOf(furious_surge_stacks.min)

    for(stack <- furious_surge_stacks){
      if (counter == minIndex){
        newList = 10 :: newList
      }
      else newList = stack :: newList
      counter+=1
    }
    newList
  }

  /**
   * Increment berserkDuration by the given duration.
   * @param duration The duration to be added to berserkDuration.
   */
  def add_berserk(duration: Double): Unit = {
    this.berserkDuration = this.berserkDuration + duration
  }

  /**
   * Calculate Blood Reaction in berserk bonus.
   * @param _type The type of bonus to be calculated (POWER || CONDI)
   * @return Berserk Bonus
   */
  private def addBloodReaction(_type: String): Int = {
    if(_type == "POWER"){
      Math.round(precision * 0.12).toInt + 1
    } else {
      Math.round(2119 * 0.15).toInt
    }
  }
}
