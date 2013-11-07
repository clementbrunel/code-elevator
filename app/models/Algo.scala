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
    SmallTower().nextCommand
  }
}
case class SmallTower(name:String="SmallTower") extends Algo{
  def display = (name -> name) 
  
  override def nextCommand={
    	if (BuildingWaiters.isEmpty && BuildingClients.isEmpty){
	       State.update(Nothing)
	    }
	    else { 
	    	CrashDetection.incrementCounter
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
		      					    case up:Up if State.level<Specs.maxLevel		=> State.update(Up); //pour eviter le blocage initial si appel au niveau zero et etape actuel nothing
		      					    case down:Down if State.level>Specs.minLevel	=> State.update(Down)
		      					    case others => State.calculDirection()  
		      					    }
		      					}
		      }
		  
		      case (_,_,Opened()) => {
		    	  				//BUG, les users ne rentrent pas si la porte est deja ouverte au bon etage quand ils arrivent
		    	  				Log.debug("Doors already opened, Come in but so do Nothing");  
	    					    State.update(Close)	        					   
		        					  }
		      case (_,_,Closed()) => {
		    	  				Log.debug("Ferme et pas de client ou waiter a satisfaire")
	    	  					State.calculDirection()
		      }
		    }
	    }
  }
}


