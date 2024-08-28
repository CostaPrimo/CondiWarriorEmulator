package CondiWarrior.src.main.Weapons

import CondiWarrior.src.main.Hits.Direct_Hit

class Dagger extends Weapon (1000, 30){

  //CONFIG ----------------------------
  private val precise_cut_coeff = 0.6
  private val precise_cut_coeff_crit = 1.15
  private val precise_cut_attacks = 1

  private val focused_slash_coeff = 0.65
  private val focused_slash_coeff_crit = 1.15
  private val focused_slash_attacks = 1

  private val keen_strike_coeff = 1.05
  private val keen_strike_attacks = 1

  private val aura_slicer_coeff = 1.8
  private val aura_slicer_attacks = 1

  private val disrupting_stab_coeff = 1.2
  private val disrupting_stab_attacks = 1

  private val wastrels_ruin_coeff = 1.5
  private val wastrels_ruin_attacks = 1
  private val wastrels_ruin_coeff_enemynotusingskill = 2.0

  private val bladestorm_coeff = 0.5
  private val bladestorm_attacks = 7

  private val breaching_strike_coeff = 2.5
  private val breaching_strike_attacks = 1
  private val breaching_strike_coeff_ifnoenemyboons = 1.5

  private val slicing_maelstrom_coeff = 2.5
  private val slicing_maelstrom_attacks = 1
  private val slicing_maelstrom_coeff_ifnoenemyboons = 1.5

  //----------------------------------

  def precise_cut(cast_time: Double): Unit = {
    tick_time(cast_time)

    TODO: // Needs precise_cut_coeff_crit multiplied if crit
    val damage = new Direct_Hit(this, precise_cut_coeff).hit(precise_cut_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def focused_slash(cast_time: Double): Unit = {
    tick_time(cast_time)

    TODO: // Needs focused_slash_coeff_crit multiplied if crit
    val damage = new Direct_Hit(this, focused_slash_coeff).hit(focused_slash_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def keen_strike(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, keen_strike_coeff).hit(keen_strike_attacks)
    getTarget.deal_strike_damage(damage)
  }

  def aura_slicer(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, aura_slicer_coeff).hit(aura_slicer_attacks)
    getTarget.deal_strike_damage(damage)

    if(getPlayer.hasActiveFireField) {
      this.getPlayer.setFireAura(5.0)
  }

  def disrupting_stab(cast_time: Double): Unit = {
    tick_time(cast_time)

    TODO: // Add a CC trigger
    val damage = new Direct_Hit(this, disrupting_stab_coeff).hit(disrupting_stab_attacks)
    getTarget.deal_strike_damage(damage)
  }
  
  def wastrels_ruin(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, wastrels_ruin_coeff * wastrels_ruin_coeff_enemynotusingskill).hit(arcing_arrow_attacks)
    getTarget.deal_strike_damage(damage)

  }
  
  def bladestorm(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, bladestorm_coeff).hit(bladestorm_attacks)
    getTarget.deal_strike_damage(damage)
  }
  
  def breaching_strike(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, breaching_strike_coeff * breaching_strike_coeff_ifnoenemyboons).hit(breaching_strike_attacks)
    getTarget.deal_strike_damage(damage)

    if(getPlayer.hasActiveFireField) {
      this.getPlayer.setFireAura(5.0)
    }
  }
    
  def slicing_maelstrom(cast_time: Double): Unit = {
    tick_time(cast_time)

    val damage = new Direct_Hit(this, slicing_maelstrom_coeff * slicing_maelstrom_coeff_ifnoenemyboons).hit(slicing_maelstrom_attacks)
    getTarget.deal_strike_damage(damage)
  }
}