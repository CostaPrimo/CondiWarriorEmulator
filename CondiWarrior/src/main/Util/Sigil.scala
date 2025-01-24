package CondiWarrior.src.main.Util

abstract class Sigil (cooldown: Double){
  private var cd = 0.0

  def tick_time(time: Double): Unit = {
    if(this.cd - time < 0.0) this.cd = 0.0
    else this.cd -= time
  }

  def setCd(): Unit = {
    this.cd = cooldown
  }
}
