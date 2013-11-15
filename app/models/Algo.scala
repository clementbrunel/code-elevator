package models
import models._
import models.DSL._
import tools.Log

trait Algo {
  def nextCommand:Command
  def name:String
}

object Algo{
  var currentAlgo:Algo=SmallTower()

  def changeAlgo(name:String)={
    name match{
      case "BigTower" => currentAlgo=BigTower()
      case "SmallTower" => currentAlgo=SmallTower()
    }
  }
  def ListAlgo:Seq[(String, String)]=List(SmallTower().display,BigTower().display)
}
case class BigTower(name:String="BigTower") extends Algo{ 
  def display = (name -> name) 
  
  override def nextCommand={
    //TODO The improvements for big tower
    val calculatedNextCommand = SmallTower().nextCommand
    if (BuildingWaiters.size()>(Specs.bestCapacity*(Specs.maxLevel- Specs.minLevel))){
      Log.warning("Size Waiters" +BuildingWaiters.size() + "vs "+ Specs.bestCapacity*(Specs.maxLevel- Specs.minLevel))
      Elevator.resetManuAsked(3)
    }
    calculatedNextCommand
  }
}
case class SmallTower(name:String="SmallTower") extends Algo{
  def display = (name -> name) 
  
  override def nextCommand={
    	if (BuildingWaiters.isEmpty && BuildingClients.isEmpty){
	       Nothing
	    }
	    else { 
	    	CrashDetection.incrementCounter
		    val currentLevel=State.level
		    val happyWaiters= BuildingWaiters.levels.getOrElse(currentLevel, Nil)
		    val happyClients = BuildingClients.levels.getOrElse(currentLevel, Nil)
		   Log.info("BuildingClients.size" + BuildingClients.size)
		   (happyClients,happyWaiters,State.door) match {
		      case(clients,_,Closed()) if clients.size>0=>{
						        	Log.debug("OPEN");  
						            Open
		      							}
		      case(_,waiters,Closed()) if waiters.size>0 && BuildingClients.size<Specs.bestCapacity =>{
		      					if (!waiters.filter(waiter => (waiter.asInstanceOf[Waiter].direction.label==State.action.label)).isEmpty || BuildingClients.isEmpty
		      					    ||State.level==Specs.minLevel ||State.level==Specs.maxLevel) {
		    	  				Log.debug("Enter in Optimisation with direction :" + State.action.label +" BuildingClients.isEmpty " + BuildingClients.isEmpty);  
	    	  					Open
		      					}
		      					else{
		      					  Log.debug("list of waiter no stop because of direction" + happyWaiters + " vs " +State.action )
		      					  State.action match {
		      					    case up:Up if State.level<Specs.maxLevel		=> Up //pour eviter le blocage initial si appel au niveau zero et etape actuel nothing
		      					    case down:Down if State.level>Specs.minLevel	=> Down
		      					    case others => State.calculDirection()  
		      					    }
		      					}
		      }
		  
		      case (_,_,Opened()) => {
		    	  				//BUG, les users ne rentrent pas si la porte est deja ouverte au bon etage quand ils arrivent
		    	  				Log.debug("Doors already opened, Come in but so do Nothing");  
	    					    Close	        					   
		        					  }
		      case (_,_,Closed()) => {
		    	  				Log.debug("Ferme et pas de client ou waiter a satisfaire")
	    	  					State.calculDirection()
		      }
		    }
	    }
  }
}


