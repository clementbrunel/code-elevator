package models
import models._
import models.DSL._
import tools.Log
import scala.collection.mutable.ListBuffer

trait Algo {
  def nextCommand(cabine:BuildingClients):Command
  def name:String
}

object Algo{
  var currentAlgo:Algo=MultiTower()
  var history:List[List[Int]]=List();
  var ResetManuAsked:Reset = NoReset();
  var cabines=List[BuildingClients]()
  
  
  def getCabineByIndex(index:Int):BuildingClients={
    if (cabines.isEmpty){
      Algo.resetAll
    }
    cabines.lift(index) match {
      case Some(cab) 	=> cab
      case None 		=> Log.severe("Recuperation d'une cabine non existente" +index +" vs size" + cabines.size);null
    }
  }
  
  def addWaiterOrNot(atFloor:Int,waiter:Waiter)={
	  val cabsAtGoodLevelAndOpened = cabines.filter(cab => cab.state.level==atFloor && cab.state.door==Opened())  
	    if (!cabsAtGoodLevelAndOpened.isEmpty){
	    	Log.warning("Waiter not added because of currentState")
	    }
	    else{
	    	BuildingWaiters.add(atFloor, waiter)   
	    }
  }
    
  //res case class pour les differents resets
  def resetManuDone= {
    ResetManuAsked=NoReset();
    }
  def resetManuAsked(ResetType:Reset)= {
    ResetManuAsked=ResetType;
    }
  def resetManuIsAsked():Boolean= {
    ResetManuAsked match {
      case reset @NoReset() => false;
      case otherReset 		=> true
    }
   }
  

  def changeAlgo(name:String)={
    name match{
      case "BigTower" => currentAlgo=BigTower()
      case "SmallTower" => currentAlgo=SmallTower()
      case "MultiCabine" => currentAlgo=MultiTower()
    }
  }
   def nextCommands():List[Command]={

//     val lastAction=State.action
//    	CrashDetection.addHelp("Lastaction " + State.action.label)
     
    	var listbuf = new ListBuffer[Command]() 
    	for (cabine <- cabines){
    	   var newCommand= currentAlgo.nextCommand(cabine)
    	   currentAlgo match {
    	     case multi @MultiTower(_) => {
    	       //special improvement for multi
    	     }
    	     case _	=> ()
    	   }
    	   cabine.state.update(newCommand)
    	   listbuf +=newCommand
    	}
    	val nextCommands=listbuf.toList
    	CrashDetection.addHelp(" nextCommand calculated " + nextCommands.map(com => com.label).mkString(","))
    	if (BuildingWaiters.size()>(Specs.bestCapacity*(Specs.maxLevel- Specs.minLevel)) && Specs.autoReset){
    		Log.warning("Size Waiters" +BuildingWaiters.size() + "vs "+ Specs.bestCapacity*(Specs.maxLevel- Specs.minLevel))
    		CrashDetection.addHelp("autoReset asked " +BuildingWaiters.size() + "vs "+ Specs.bestCapacity*(Specs.maxLevel- Specs.minLevel))
    		Algo.resetManuAsked(CrashIsBetter())
    	}
    	nextCommands

//    	history=CrashDetection.add(history, State.level)
    	
//    	 if (CrashDetection.isLooped(history)) {
//    	   CrashDetection.addHelp("loop detected" +history.mkString(" "))
//    	   lastAction match {
//    	      case up:Up if State.level< Specs.maxLevel			=> State.update(Up); //on continue les actions pour eviter le bouclage!
//		      case down:Down if State.level>Specs.minLevel 		=>State.update(Down)
//		      case downElse if State.level>Specs.minLevel 		=>State.update(Down)
//		      case upElse if State.level< Specs.maxLevel		=>State.update(Up)
//    	   }
//    	 }
//    	 else{
//    	   State.update(nextCommand)
//    	 }   	
   }
   
   override def toString():String={
    " ElevatorProblem { Clients [ "+cabines.mkString("|")+"]"+
    " Waiters " + BuildingWaiters.levels.map (level => "Level: "+level._1+level._2.mkString(",",",","")).mkString("/")+"\n"+
    " States" + cabines.map(cab => cab.state).mkString("|").toString + " ResetManuAsked " + ResetManuAsked.label+" CrashDetectionCounter " + CrashDetection.countAction+"\n"+
    " History" +history.mkString(",")+" CabinesCount " +cabines.size+"}"
   }
   
   
   def resetAll= {  
    //reinstance BuildingClient if number of cabin change
    var listbufCab = new ListBuffer[BuildingClients]() 
    (1 to Specs.cabinCount)map(integer => listbufCab += new BuildingClients())
    cabines=listbufCab.toList
	BuildingWaiters.reset
	resetManuDone
	CrashDetection.resetCounter
	history=List();
  	}
     
  def ListAlgo:Seq[(String, String)]=List(SmallTower().display,BigTower().display,MultiTower().display)
}
/*  Define in a new class
case class MultiTowerc(name:String="MultiTower") extends Algo
* */

case class BigTower(name:String="BigTower") extends Algo{ 
  def display = (name -> name) 
  
  override def nextCommand(cabine:BuildingClients)={

    CrashDetection.addHelp("in BigTower")
    val calculatedNextCommand = SmallTower().nextCommand(cabine)
//	 options...
    calculatedNextCommand
  }
}
case class SmallTower(name:String="SmallTower") extends Algo{
  def display = (name -> name) 
  
  override def nextCommand(cabine:BuildingClients)={
	  	 CrashDetection.addHelp("in SmallTower")
	  	 
    	if (BuildingWaiters.isEmpty && cabine.isEmpty){
	       cabine.state.door match {
	         case Closed()	=>Nothing
	         case Opened()	=>Close
	       }
	    }
	    else {

	    	//CrashDetection.incrementCounter
		    val currentLevel=cabine.state.level
		    val happyWaiters= BuildingWaiters.levels.getOrElse(currentLevel, Nil)
		    val happyClients = cabine.levels.getOrElse(currentLevel, Nil)
		   (happyClients,happyWaiters,cabine.state.door) match {
		      case(clients,_,Closed()) if clients.size>0=>{
		    	  					CrashDetection.addHelp(" Clients a cet etage ")
						        	Log.debug("OPEN");  
						            Open
		      							}
		      case(_,waiters,Closed()) if waiters.size>0 && cabine.size<Specs.bestCapacity =>{
		      					if (!waiters.filter(waiter => (waiter.asInstanceOf[Waiter].direction.label==cabine.state.action.label)).isEmpty || cabine.isEmpty
		      					    ||cabine.state.level==Specs.minLevel ||cabine.state.level==Specs.maxLevel) {
		    	  				Log.debug("Enter in Optimisation with direction :" + cabine.state.action.label +" cabine.isEmpty " + cabine.isEmpty);  
	    	  					CrashDetection.addHelp(" Waiters in good direction so open ")
		    	  				Open
		      					}
		      					else{
		      					  Log.debug("list of waiter no stop because of direction" + happyWaiters + " vs " +cabine.state.action )
		      					  CrashDetection.addHelp(" in SmallTower no stop because of direction ")
		      					  cabine.state.action match {
		      					    case up:Up if cabine.state.level<Specs.maxLevel		=> Up //pour eviter le blocage initial si appel au niveau zero et etape actuel nothing
		      					    case down:Down if cabine.state.level>Specs.minLevel	=> Down
		      					    case others => cabine.state.calculDirection(cabine)  
		      					    }
		      					}
		      }
		  
		      case (_,_,Opened()) => {
		    	  				//BUG, les users ne rentrent pas si la porte est deja ouverte au bon etage quand ils arrivent
		    	  				Log.debug("Doors already opened, Come in but so do Nothing");  
		    	  				CrashDetection.addHelp("Close because was open")
	    					    Close	        					   
		        					  }
		      case (_,_,Closed()) => {
		    	  				Log.debug("Ferme et pas de client ou waiter a satisfaire")
		    	  				CrashDetection.addHelp("Pas de client ou waiter a satisfaire")
	    	  					cabine.state.calculDirection(cabine)
		      }
		    }
	    }
  }
}


