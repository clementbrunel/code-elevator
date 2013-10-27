package models

trait Person
case class Waiter (callLevel:Int,direction:Direction) extends Person{
  def this(callLevel:Int,direction:String)= this(callLevel,Direction.labelToDirection(direction))
  override def toString() = direction.label
}
case class Client (stop:Int) extends Person
