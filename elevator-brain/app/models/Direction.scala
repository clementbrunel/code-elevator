package models


trait Action{
  def label:String
}
case class Nothing() extends Action{
  def label="NOTHING"
}
case class Up() extends Action{
  def label="UP"
}
case class Down() extends Action{
  def label="DOWN"
}
case class Open() extends Action{
  def label="OPEN"
}
case class Close() extends Action{
  def label="CLOSE"
}


trait State{
    def countPassagers:Int
    def level:Level
    def direction:Direction
}
case class InitialState(countPassagers:Int=0,level:Level=Level(0),direction:Direction=null) extends State
case class CurrentState(count:Int,currentLevel:Level,currentdirection:Direction) extends State{
  def countPassagers=count
  def level=currentLevel
  def direction=currentdirection
}
case class NextState(currentState:State){
  def countPassagers=currentState.countPassagers
  def level = currentState.direction match {
    case _:UP 	=> 	if (currentState.level.top) Level(currentState.level.label+1) 
    				else Level (currentState.level.label-1)
    case _:DOWN => 	if (currentState.level.bottom) Level(currentState.level.label-1) 
    				else Level (currentState.level.label+1)
  	}
  
}

case class Level(floor:Int){
  def label:Int=floor;
  val max=5;
  val min=0;
  def top=floor+1<=max
  def bottom=floor-1>=min
//  def isPermited=floor>=min && floor<=max
}

trait Direction
case class UP() extends Direction
case class DOWN() extends Direction
