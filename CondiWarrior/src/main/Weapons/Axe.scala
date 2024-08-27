package CondiWarrior.src.main.Weapons

class Axe extends Weapon(1000, 100) {
  //CONFIG ----------------------------
  private val chop_coef = 0.7
  private val chop_attacks = 1

  private val double_chop_coeff = 0.75
  private val double_chop_attacks = 2

  // TODO: Figure out how TF weapon strength is rolled for this.
  private val triple_chop_coeff = 0.75
  private val triple_chop_attacks = 2
  private val triple_chop_final_coeff = 1.6
  private val triple_chop_final_attacks = 1

  private val cyclone_axe_coeff = 0.88
  private val cyclone_axe_attacks = 2
  private val cyclone_axe_finisher_chance = 100

  private val throw_axe_coeff = 0.85
  private val throw_axe_attacks = 1
  private val throw_axe_finisher_chance = 100

  private val dual_strike_coeff = 1.175
  private val dual_strike_attacks = 2

  private val whirling_axe_coeff = 0.5592
  private val whirling_axe_attacks = 15

  private val decapitate_coeff = 3.0
  private val decapitate_attacks = 1
  //-----------------------------------
}
