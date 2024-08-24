package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Entities.{CondiWarrior, Target}
import CondiWarrior.src.main.Hits.Direct_Hit

class Unarmed extends Weapon (690.5, 34.5){

  val sword = new Sword()
  val longbow = new Longbow()

  //CONFIG ----------------------------
  private val LAST_BLAZE_BURNING_DUR = 4.0
  private val LAST_BLAZE_BURNING_AMOUNT = 1

  private val blood_reckoning_berserk_increase = 2.0

  private val shattering_blow_berserk_increase = 3.0
  private val shattering_blow_coeff = 1.5
  private val shattering_blow_attacks = 1
  private val shattering_blow_bleeding_dur = 10.0
  private val shattering_blow_bleeding_amount = 4

  private val sundering_leap_berserk_increase = 3.0
  private val sundering_leap_coeff = 2.5
  private val sundering_leap_attacks = 1

  private val outrage_berserk_increase = 3.0

  private val head_butt_berserk_increase = 2.0
  private val head_butt_coeff = 4.5
  private val head_butt_attacks = 1

  private val geomancy_bleeding_duration = 8.0
  private val geomancy_bleeding_amount = 3
  private val geomancy_coeff = 0.25
  private val geomancy_attacks = 1
  //-----------------------------------

  override def setPlayer(condiWarrior: CondiWarrior): Unit = {
    sword.setPlayer(condiWarrior)
    longbow.setPlayer(condiWarrior)
    super.setPlayer(condiWarrior)
  }

  override def setTarget(target: Target): Unit = {
    sword.setTarget(target)
    longbow.setTarget(target)
    super.setTarget(target)
  }

  def berserk(): Unit = {
    val LAST_BLAZE = createBurning(LAST_BLAZE_BURNING_DUR, LAST_BLAZE_BURNING_AMOUNT)
    getTarget.add_conditions(LAST_BLAZE)
    getPlayer.startBerserk()
  }

  def blood_reckoning(cast_time: Double): Unit = {
    val LAST_BLAZE = createBurning(LAST_BLAZE_BURNING_DUR, LAST_BLAZE_BURNING_AMOUNT)

    tick_time(cast_time)

    getTarget.add_conditions(LAST_BLAZE)

    if(getPlayer.getFireAura > 0.0){ unarmed_king_of_fires()}

    if(getPlayer.getInBerserk) { getPlayer.add_berserk(blood_reckoning_berserk_increase) }
  }

  def shattering_blow(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, shattering_blow_coeff).hit(shattering_blow_attacks)
    getTarget.deal_strike_damage(damage)

    val LAST_BLAZE = createBurning(LAST_BLAZE_BURNING_DUR, LAST_BLAZE_BURNING_AMOUNT)
    val BLEEDING = createBleeding(shattering_blow_bleeding_dur, shattering_blow_bleeding_amount)
    getTarget.add_conditions(LAST_BLAZE ::: BLEEDING)
    relic_of_the_fractal()

    if(getPlayer.getFireAura > 0.0){ unarmed_king_of_fires()}

    if(getPlayer.getInBerserk) { getPlayer.add_berserk(shattering_blow_berserk_increase) }
  }

  def sundering_leap(cast_time: Double): Unit = {
    tick_time(cast_time)

    if(getPlayer.hasActiveFireField) { getPlayer.setFireAura(5.0) }

    val damage = new Direct_Hit(this, sundering_leap_coeff).hit(sundering_leap_attacks)
    getTarget.deal_strike_damage(damage)

    val LAST_BLAZE = createBurning(LAST_BLAZE_BURNING_DUR, LAST_BLAZE_BURNING_AMOUNT)
    if(getPlayer.getFireAura>0.0){ unarmed_king_of_fires()}
    getTarget.add_conditions(LAST_BLAZE)

    if(getPlayer.getInBerserk) { getPlayer.add_berserk(sundering_leap_berserk_increase) }
  }

  def outrage(): Unit = {
    val LAST_BLAZE = createBurning(LAST_BLAZE_BURNING_DUR, LAST_BLAZE_BURNING_AMOUNT)
    getTarget.add_conditions(LAST_BLAZE)

    if(getPlayer.getFireAura > 0.0){ unarmed_king_of_fires()}

    if(getPlayer.getInBerserk) { getPlayer.add_berserk(outrage_berserk_increase) }
  }

  def head_butt(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, head_butt_coeff).hit(head_butt_attacks)
    getTarget.deal_strike_damage(damage)

    val LAST_BLAZE = createBurning(LAST_BLAZE_BURNING_DUR, LAST_BLAZE_BURNING_AMOUNT)
    getTarget.add_conditions(LAST_BLAZE)

    if (getPlayer.getFireAura > 0.0) { unarmed_king_of_fires() }

    if(getPlayer.getInBerserk) { getPlayer.add_berserk(head_butt_berserk_increase) }
  }

  private def unarmed_king_of_fires(): Unit = {
    if(getPlayer.isOnSword){
      sword.king_of_fires()
    } else {
      longbow.king_of_fires()
    }
  }

  def swapWeapon(cast_time: Double): Unit = {
    tick_time(cast_time)
    if (!getPlayer.isOnSword) {
      geomancy()
      getPlayer.weapon_swap()
    } else {
      getPlayer.setDoomReady(true)
      getPlayer.weapon_swap()
    }
  }

  def geomancy(): Unit = {
    val CONDITIONS = createBleeding(geomancy_bleeding_duration, geomancy_bleeding_amount)
    getTarget.add_conditions(CONDITIONS)
    relic_of_the_fractal()

    val damage = new Direct_Hit(this, geomancy_coeff).hit(geomancy_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def wait(duration: Double): Unit = {
    tick_time(duration)
  }

}
