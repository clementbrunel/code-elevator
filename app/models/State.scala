package models
import play.Logger
import models.DSL._
import tools.Log



class State(){
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
    case openup:OpenUp=>{door=Opened(); Log.debug("Update State openup, Open the door")}
    case opendown:OpenDown=>{door=Opened(); Log.debug("Update State OpenDown, Open the door")}
    case close:Close => {door=Closed(); Log.debug("Update State close, Close the door")}
    case up:Up =>  {this.++; Log.debug("Update State Up, level++")}
    case down:Down => {this.--; Log.debug("Update State Down, level--")}
    case nothing:Nothing => Log.debug("Update State Nothing, No changement")
  }
  action=processAction
  processAction
  }


  def computeDistanceTo(toFloor:Int):Int= {
	  Math.abs(Specs.maxLevel-Specs.minLevel)-Math.abs(level-toFloor);
    }
  
  def countGoTo(cabine:BuildingClients,direction:Direction):Int={
    direction match {
      case up:UpDirection => (cabine.levels.filter(lev => (lev._1 > level)).map( lev => lev._2.size*computeDistanceTo(lev._1))).sum
      case down:DownDirection => (cabine.levels.filter(lev => (lev._1 < level)).map( lev => lev._2.size*computeDistanceTo(lev._1))).sum
    }
  }
  
  def countCallFrom(direction:Direction):Int={
    direction match {
      case up:UpDirection => (BuildingWaiters.levels.filter(lev => (lev._1 > level)).map( lev => lev._2.size*computeDistanceTo(lev._1))).sum
      case down:DownDirection => (BuildingWaiters.levels.filter(lev => (lev._1 < level)).map( lev => lev._2.size*computeDistanceTo(lev._1))).sum
    }
  }
  
  def nearestCabs(level:Int,cabine:BuildingClients,othersCabines:List[BuildingClients]):Boolean={
    val mydiff =Math.abs(cabine.state.level-level)
    val avgOthersDiff=average(othersCabines.map(cab => Math.abs(cab.state.level-level)))
    mydiff<=avgOthersDiff
  }
  
  //use for bigger list, only half of cab will regard the waiters
  def average[T]( ts: Iterable[T] )( implicit num: Numeric[T] ) :Double= {
  num.toDouble( ts.sum ) / ts.size
  }
  
  //only use on multielevator
  def countCallFromNearest(direction:Direction,cabine:BuildingClients,othersCabines:List[BuildingClients]):Int={
    direction match {
      case up:UpDirection => 
        (BuildingWaiters.levels.filter(lev => (lev._1 > level && nearestCabs(level,cabine,othersCabines)))
          .map( lev => lev._2.size*computeDistanceTo(lev._1))).sum
      case down:DownDirection => 
        (BuildingWaiters.levels.filter(lev => (lev._1 < level && nearestCabs(level,cabine,othersCabines)))
          .map( lev => lev._2.size*computeDistanceTo(lev._1))).sum
    }
  }
  
  def calculPonderation(cabine:BuildingClients):(Int,Int)={
    val tupleCoefs=
      if(cabine.size<Specs.bestCapacity){
        CrashDetection.addHelp("Waiters are in calcul")
    (Specs.clientPond * countGoTo(cabine,UpDirection()) + Specs.waiterPond * countCallFrom(UpDirection()),
     Specs.clientPond * countGoTo(cabine,DownDirection()) + Specs.waiterPond * countCallFrom(DownDirection()))
	}
    else{
       CrashDetection.addHelp("Waiters are NOT in calcul")
      (Specs.clientPond * countGoTo(cabine,UpDirection()),
       Specs.clientPond * countGoTo(cabine,DownDirection()))
    }
    CrashDetection.addHelp("calculPonderation countToTop" +tupleCoefs._1 + "countToDown" +tupleCoefs._2)
    Log.debug("calculPonderation countToTop" +tupleCoefs._1 + "countToDown" +tupleCoefs._2)
    tupleCoefs
  }
  
  def calculPonderationForList(cabine:BuildingClients,othersCabines:List[BuildingClients]):(Int,Int)={
    val tupleCoefs=
      if(cabine.size<Specs.bestCapacity){
        CrashDetection.addHelp("Waiters are in calcul")
    (Specs.clientPond * countGoTo(cabine,UpDirection()) + Specs.waiterPond * countCallFromNearest(UpDirection(),cabine,othersCabines),
     Specs.clientPond * countGoTo(cabine,DownDirection()) + Specs.waiterPond * countCallFromNearest(DownDirection(),cabine,othersCabines))
	}
    else{
       CrashDetection.addHelp("Waiters are NOT in calcul")
      (Specs.clientPond * countGoTo(cabine,UpDirection()),
       Specs.clientPond * countGoTo(cabine,DownDirection()))
    }
    CrashDetection.addHelp("calculPonderationForList countToTop" +tupleCoefs._1 + "countToDown" +tupleCoefs._2)
    Log.debug("calculPonderationForList countToTop" +tupleCoefs._1 + "countToDown" +tupleCoefs._2)
    tupleCoefs
  }
  
  
  def calculDirection(cabine:BuildingClients,othersCabines:List[BuildingClients]=Nil):Command={
   val others=othersCabines.filter(cab => cab != cabine)
   Log.debug("Size of other cabine in State " + others.size)
   val CoefsTopAndDown=if (others.isEmpty) {
	   	calculPonderation(cabine)
   	}else{
	   	calculPonderationForList(cabine,others)
   }
     CoefsTopAndDown match {
		      case (countToTop,countToDown) if (countToTop > countToDown) =>  Up
		      case (countToTop,countToDown) if (countToTop < countToDown) =>  Down
		      case (countToTop,countToDown) if (level < Specs.maxLevel) =>  Up
		      case (_,_)  =>  Down
		    }
	}
}


