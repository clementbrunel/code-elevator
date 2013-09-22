package models

case class Elevator (clients:List[Person],state:State){
  def this(elev:Elevator)= this(elev.clients,elev.state)
  def this()=this(List(),InitialState())
}
