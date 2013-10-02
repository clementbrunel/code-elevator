package models

trait Person
case class Waiter (callLevel:Int,direction:ElevatorAction) extends Person{
   def this(callLevel:Int,direction:String)= this(callLevel,ElevatorAction.labelToAction(direction))
}
case class Client (stop:Int) extends Person


//object Person {
// def waiter(level:Int,direction:ElevatorAction)= Waiter(level,direction)
// def client(stop:Int)= Client(stop)
//}