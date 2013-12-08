package models

trait Person
case class Waiter (callLevel:Int,direction:Direction) extends Person{
  def this(callLevel:Int,direction:String)= this(callLevel,Direction.labelToDirection(direction))
  override def toString() = direction.label
}
case class Client (stop:Int) extends Person


//scoring :
/**

20+(2+ abs(start - stop)) -( waittime / 2 + traveltime)


Initial 20

before open :
-waitime/2

On go
(2+ abs(start - stop))


on travel:

-traveltime
*/
