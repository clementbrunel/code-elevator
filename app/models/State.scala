package models
import play.Logger
import models.DSL._
import tools.Log

trait State{
    def level:Int
    def action:Command
    def door:DoorState
}

object State{
  var level:Int=0
  def ++()={level=level+1}
  def --()={level=level-1}
  var action:Command=Nothing()
  var door:DoorState=Closed()
  
  
  def reset={level=0;door=Closed()}
  
  override def toString()={
    "State { level:" + level + " action: " + action.label + " doors: " + door.label+"}"
  }

  def update(processAction:Command):Command={processAction match {
    case open:Open => {door=Opened(); Log.debug("Update State open, Open the door")}
    case close:Close => {door=Closed(); Log.debug("Update State close, Close the door")}
    case up:Up =>  {State++; Log.debug("Update State Up, level++")}
    case down:Down => {State--; Log.debug("Update State Down, level--")}
    case nothing:Nothing => Log.debug("Update State Nothing, No changement")
  }
  action=processAction
  processAction
  }
  
  def computeDistanceTo(toFloor:Int):Int= {
	  Math.abs(Specs.maxLevel-Specs.minLevel)-Math.abs(State.level-toFloor);
    }
  def countGoTo(direction:Direction):Int={
    direction match {
      case up:UpDirection => (BuildingClients.levels.filter(level => (level._1 > State.level)).map( level => level._2.size*computeDistanceTo(level._1))).sum
      case down:DownDirection => (BuildingClients.levels.filter(level => (level._1 < State.level)).map( level => level._2.size*computeDistanceTo(level._1))).sum
    }
  }
  
  def countCallFrom(direction:Direction):Int={
    direction match {
      case up:UpDirection => (BuildingWaiters.levels.filter(level => (level._1 > State.level)).map( level => level._2.size*computeDistanceTo(level._1))).sum
      case down:DownDirection => (BuildingWaiters.levels.filter(level => (level._1 < State.level)).map( level => level._2.size*computeDistanceTo(level._1))).sum
    }
  }
  
  def calculPonderation():(Int,Int)={
    val tupleCoefs=
      if(BuildingClients.size<Specs.bestCapacity){
        CrashDetection.addHelp("Waiters are in calcul")
    (Specs.clientPond * countGoTo(UpDirection()) + Specs.waiterPond * countCallFrom(UpDirection()),
     Specs.clientPond * countGoTo(DownDirection()) + Specs.waiterPond * countCallFrom(DownDirection()))
	}
    else{
       CrashDetection.addHelp("Waiters are NOT in calcul")
      (Specs.clientPond * countGoTo(UpDirection()),
       Specs.clientPond * countGoTo(DownDirection()))
    }
    CrashDetection.addHelp("countToTop" +tupleCoefs._1 + "countToDown" +tupleCoefs._2)
    Log.debug("countToTop" +tupleCoefs._1 + "countToDown" +tupleCoefs._2)
    tupleCoefs
  }
  
  def calculDirection():Command={
   val CoefsTopAndDown=calculPonderation()
     CoefsTopAndDown match {
		      case (countToTop,countToDown) if (countToTop > countToDown) =>  Up
		      case (countToTop,countToDown) if (countToTop < countToDown) =>  Down
		      case (countToTop,countToDown) if (State.level < Specs.maxLevel) =>  Up
		      case (_,_)  =>  Down
		    }
	}
}


