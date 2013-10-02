package controllers
import play.api._
import play.api.mvc._
import play.Logger
import models._
import models.DSL._
import play.api.cache.Cache
import play.api.Play.current




object Brain  extends Controller {
  
  
  def call(atFloor:Int, to:String) = Action {
    Logger.debug("call atfloor" + atFloor + "To"+ to)
    BuildingWaiters.++(atFloor, Waiter(atFloor,to))
    Logger.debug("call End Waiters " +BuildingWaiters.levels) 
    Ok("")
  }
  
  def go(floorToGo:Int) = Action {
    Logger.debug("floorToGo" + floorToGo)
    BuildingClients.++(floorToGo,Client(floorToGo))
    Logger.debug("go Clients" +BuildingClients.levels) 
    Ok("")
  }
  
  def userHasEntered() = Action {
    Logger.debug("userHasEntered")
    BuildingWaiters.--(State.level)
    Logger.debug("userHasEntered End" +BuildingWaiters.levels) 
    Ok("")
  }
  
  def userHasExited() = Action {
    Logger.debug("userHasExited")
    BuildingClients.--(State.level)
    Logger.debug("userHasExited End" +BuildingClients.levels) 
    Ok("")
  }
  
  def reset(message:String) = Action {
    Logger.debug("reset" + message)
    BuildingClients.reset
    BuildingWaiters.reset
    State.reset
    Logger.debug("reset Clients " +BuildingClients.levels + " Waiters " + BuildingWaiters.levels + " L " + State.level+ " A "+State.action.label+ "D "+ State.door) 
    Ok("")
  }
  
  def nextCommand()={
    Logger.debug("nextCommand")
    Logger.debug("Client" +BuildingClients.levels)
    Logger.debug("Waiters" +BuildingWaiters.levels)
    if (BuildingWaiters.isEmpty && BuildingClients.isEmpty){
       State.update(Nothing)
    }
    else { 
	    val currentLevel=State.level
	    val happyWaiters= BuildingWaiters.levels.getOrElse(currentLevel, Nil)
	    val happyClients = BuildingClients.levels.getOrElse(currentLevel, Nil)
	    (happyClients,happyWaiters,State.door) match {
	      case(clients,_,Closed()) if clients.size>0=>{
					        	Logger.debug("OPEN");  
					            State.update(Open)
	      							}
	      case(_,waiters,Closed()) if waiters.size>0 =>{
	      					if (!waiters.filter(waiter => (waiter.asInstanceOf[Waiter].direction.label==State.action.label)).isEmpty || BuildingClients.isEmpty) {
	    	  				Logger.debug("Enter in Optimisation with direction :" + State.action.label +" BuildingClients.isEmpty " + BuildingClients.isEmpty);  
    	  					State.update(Open)
//	            			State.update(State.action)
	      					}
	      					else{
	      					  Logger.debug("list of waiter no stop because of direction" + happyWaiters + " vs " +State.action )
	      					  State.action match {
	      					    case up:Up if State.level<5		=> State.update(Up); //pour eviter le blocage initial si appel au niveau zero et etape actuel nothing
	      					    case down:Down if State.level>0	=> State.update(Down)
	      					    case others => State.door match {
	      					     	case opened:Opened => State.update(Nothing)
	      					     	case closed:Closed => State.update(Open)
	      					        }
	      					    }
	      					}
	      }
	      case (_,_,Closed()) => {
	    	  				Logger.debug("Ferme et pas de client ou waiter a satisfaire")
    	  					State.calculDirection()
	      }
	      case (_,_,Opened()) => {
	    	  				//BUG, les users ne rentrent pas si la porte est deja ouverte au bon etage quand ils arrivent
	    	  				Logger.debug("Doors already opened, Come in but closed then Reopen for Server");  
    					    State.update(Close)	        					   
	        					  }
	    }
    }
    ActionResponse
  }
  
  
  def ActionResponse()=Action{
    Logger.debug("nextCommand ActionAndListAction" + State.action.label)
    Ok(State.action.label) 
  }
  


  

}


