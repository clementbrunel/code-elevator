package models

trait Person
case class Waiter (callLevel:Level,direction:ElevatorAction) extends Person
case class Client (stop:Level) extends Person


object Person {
 def waiter(level:Level,direction:ElevatorAction)= Waiter(level,direction)
 def client(stop:Level)= Client(stop)
}