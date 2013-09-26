package models
import play.Logger


trait State{
    def level:Int
    def action:ElevatorAction
    def door:DoorState
}
case class InitialState(level:Int=0,action:ElevatorAction=Nothing(),door:DoorState=Closed()) extends State
case class CurrentState(currentLevel:Int,currentaction:ElevatorAction,doorState:DoorState) extends State{
  def level=currentLevel
  def action=currentaction
  def door=doorState
  def clone (state:State)=new CurrentState(state.level,state.action,state.door)
}
object State{
  var level:Int=0
  def ++()={level=level+1}
  def --()={level=level-1}
  var action:ElevatorAction=Nothing()
  var door:DoorState=Closed()
  val start=InitialState()
  def reset={level=0;door=Closed()}

  def update(processAction:ElevatorAction):Unit={processAction match {
    case open:Open => {door=Opened(); Logger.debug("Update State open, Open the door")}
    case close:Close => {door=Closed(); Logger.debug("Update State close, Close the door")}
    case up:Up =>  {State++; Logger.debug("Update State Up, level++")}
    case down:Down => {State--; Logger.debug("Update State Down, level--")}
    case nothing:Nothing => Logger.debug("Update State Nothing, No changement")
  }
  Logger.debug("State after Update =  L" + State.level+ " A"+State.action.label+ "D"+ State.door) 
  action=processAction
  }
  
  
  def calculDirection():Unit={
    //todo integrer la direction des waiters dans la prise de decision....
    
    val map = BuildingClients.levels ++ BuildingWaiters.levels .map{ case (k,v) => k -> (v + BuildingClients.levels.getOrElse(k,0)) } 
    Logger.debug("map (level,pond" + map)
    //TODO Changer la ponderation...
    val distances:List[(Int,Int,Int)]=map.map (x => (Math.abs(level-x._1),x._1,x._2)).toList.sortBy(diff => diff._3).reverse
    Logger.debug("distances (diff,level,pond" + distances)
    (distances,State.level) match {
      case (head::tail,level) if head._2>level 				=>{	Logger.debug("calculDirection Up" + distances.head._1)    	  																   	
      																		State.update(Up())   //doors must be closed!
    	  																   }
      case (head::tail,level) if head._2<level 				=>{ Logger.debug("calculDirection down" + distances.head._1)	  																	
      																		State.update(Down())
      																	   }
      case others     										=>{ Logger.error("calculDirection ERROR Nothing in nextCommand") 
        														State.update(Nothing())
   															 }
    }  
  }
}


