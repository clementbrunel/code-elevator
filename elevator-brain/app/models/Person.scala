package models

trait Person
case class Waiter (callLevel:Level,direction:Action) extends Person
case class Client (stop:Level) extends Person