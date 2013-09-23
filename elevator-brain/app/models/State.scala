package models


trait State{
//    def countPassagers:Int
    def level:Level
    def action:Action
}
case class InitialState(level:Level=Level(0),action:Action=null) extends State
case class CurrentState(currentLevel:Level,currentaction:Action) extends State{
  def level=currentLevel
  def action=currentaction
}
//case class NextState(currentState:State){
//  def level = currentState.action match {
//    case _:Up 	=> 	if (currentState.level.topPossible) Level(currentState.level.label+1) 
//    				else Level (currentState.level.label-1)
//    case _:Down => 	if (currentState.level.bottomPossible) Level(currentState.level.label-1) 
//    				else Level (currentState.level.label+1)
//  	}
// }


trait PersonState
case class In()
case class Out()
case class Waiting()