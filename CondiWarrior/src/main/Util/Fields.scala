package CondiWarrior.src.main.Util

sealed trait FIELD

object Fields extends {
  case object EMPTY extends FIELD
  case object FIRE extends FIELD
  case object LIGHTING extends FIELD
}
