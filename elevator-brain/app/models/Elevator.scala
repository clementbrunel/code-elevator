package models
import play.Logger
import models._
import models.DSL._

case class Elevator(state:State,waiters:BuildingFile,client:BuildingFile){
}


object Elevator{
  var ResetManuAsked = 0;
  var history:List[Int]=List();
  def resetAll= {  
    BuildingClients.reset
	BuildingWaiters.reset
	State.reset
	resetManuDone
	CrashDetection.resetCounter
	history=List();
  	}
  def resetManuDone= {
    ResetManuAsked=0;
    }
  def resetManuAsked= {
    ResetManuAsked=1;
    }
  override def toString():String={
    "ElevatorProblem {"+BuildingClients.levels.map (level => "Level: "+level._1+" Clients"+level._2.mkString(","))+"\n"+
    BuildingWaiters.levels.map (level => "Level: "+level._1+" Waiters"+level._2.mkString(","))+"\n"+
    " State" + State.toString + " ResetManuAsked " + ResetManuAsked+" CrashDetectionCounter " + CrashDetection.countAction+"}"
  }
//  def 
  
  
  def nextCommand:Command={
    	val lastAction=State.action
    	val nextCommand =  
    	if (BuildingWaiters.isEmpty && BuildingClients.isEmpty){
	       State.update(Nothing)
	    }
	    else { 
		    val currentLevel=State.level
		    val happyWaiters= BuildingWaiters.levels.getOrElse(currentLevel, Nil)
		    val happyClients = BuildingClients.levels.getOrElse(currentLevel, Nil)
		   (happyClients,happyWaiters,State.door) match {
		      case(clients,_,Closed()) if clients.size>0=>{
						        	Log.debug("OPEN");  
						            State.update(Open)
		      							}
		      case(_,waiters,Closed()) if waiters.size>0 =>{
		      					if (!waiters.filter(waiter => (waiter.asInstanceOf[Waiter].direction.label==State.action.label)).isEmpty || BuildingClients.isEmpty) {
		    	  				Log.debug("Enter in Optimisation with direction :" + State.action.label +" BuildingClients.isEmpty " + BuildingClients.isEmpty);  
	    	  					State.update(Open)
	//	            			State.update(State.action)
		      					}
		      					else{
		      					  Log.debug("list of waiter no stop because of direction" + happyWaiters + " vs " +State.action )
		      					  State.action match {
		      					    case up:Up if State.level<Specs.minLevel		=> State.update(Up); //pour eviter le blocage initial si appel au niveau zero et etape actuel nothing
		      					    case down:Down if State.level>Specs.maxLevel	=> State.update(Down)
		      					    case others => State.door match {
		      					     	case opened:Opened => State.update(Nothing)
		      					     	case closed:Closed => State.update(Open)
		      					        }
		      					    }
		      					}
		      }
		      case (_,_,Closed()) => {
		    	  				Log.debug("Ferme et pas de client ou waiter a satisfaire")
	    	  					State.calculDirection()
		      }
		      case (_,_,Opened()) => {
		    	  				//BUG, les users ne rentrent pas si la porte est deja ouverte au bon etage quand ils arrivent
		    	  				Log.debug("Doors already opened, Come in but closed then Reopen for Server");  
	    					    State.update(Close)	        					   
		        					  }
		    }
	    }
    	CrashDetection.add(history, State.level)
    	CrashDetection.incrementCounter
    	 if (CrashDetection.isKO(history)) {
    	   lastAction match {
    	     //check last direction et
    	      case up:Up if State.level< Specs.maxLevel			=> State.update(Up); //on continue les actions pour eviter le bouclage!
		      case down:Down if State.level>Specs.minLevel 		=>State.update(Down)
		      case downElse if State.level>Specs.minLevel 		=>State.update(Down)
		      case upElse if State.level< Specs.maxLevel		=>State.update(Up)
    	   }
    	 }
    	 else{
    	   nextCommand
    	 }   	
  }
}