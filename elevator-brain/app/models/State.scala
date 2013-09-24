package models


trait State{
    def level:Level
    def action:ElevatorAction
}
case class InitialState(level:Level=Level(0),action:ElevatorAction=Nothing()) extends State
case class CurrentState(currentLevel:Level,currentaction:ElevatorAction) extends State{
  def level=currentLevel
  def action=currentaction
}

