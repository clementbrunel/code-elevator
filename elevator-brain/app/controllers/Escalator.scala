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
    BuildingWaiters.++(atFloor, 1)
    Logger.debug("call Waiters " +BuildingWaiters.levels) 
    Ok("")
  }
  
  def go(floorToGo:Int) = Action {
    Logger.debug("floorToGo" + floorToGo)
//    val currentProblem =Elevator.getProblemFromCache
    BuildingClients.++(floorToGo, 1)
//    val clientList = currentProblem.clients++(List(Client(Level(floorToGo))))
//	Elevator.saveProblemToCache(clientList,currentProblem.waiters,currentProblem.state)
    Logger.debug("go Clients" +BuildingClients.levels) 
    Ok("")
  }
  
  def userHasEntered() = Action {
    Logger.debug("userHasEntered")
    //Todo... A ammeliorer. Super lourd....
    BuildingWaiters.++(State.level,-1)
//    val currentProblem =Elevator.getProblemFromCache
//    val waitersAtThisLevel = currentProblem.waiters.filter(waiter => waiter.callLevel==currentProblem.state.level).tail
//    val remainingWaiters = currentProblem.waiters.filter(waiter => waiter.callLevel!=currentProblem.state.level)++waitersAtThisLevel
//	Elevator.saveProblemToCache(currentProblem.clients,remainingWaiters,currentProblem.state)
    Logger.debug("userHasEntered" +BuildingWaiters.levels) 
    Ok("")
  }
  
  def userHasExited() = Action {
    Logger.debug("userHasExited")
     //Todo... A ammeliorer. Super lourd....
    BuildingClients.++(State.level,-1)
//    val currentProblem =Elevator.getProblemFromCache
//    val remainingClients=currentProblem.clients.filter(client => client.stop==currentProblem.state.level).tail++currentProblem.clients.filter(client => client.stop!=currentProblem.state.level)
//    Elevator.saveProblemToCache(remainingClients,currentProblem.waiters,currentProblem.state)
    Logger.debug("userHasExited" +BuildingClients.levels) 
    Ok("")
  }
  
  def reset(message:String) = Action {
    Logger.debug("reset" + message)
    BuildingClients.reset
    BuildingWaiters.reset
    State.reset
//    Elevator.saveProblemToCache(List(),List(),InitialState())
    Logger.debug("reset Clients " +BuildingClients.levels + " Waiters " + BuildingWaiters.levels + " L " + State.level+ " A "+State.action.label+ "D "+ State.door) 
    Ok("")
  }
  
  def nextCommand()={
    Logger.debug("nextCommand")
//    val currentProblem =Elevator.getProblemFromCache()
    Logger.debug("Client" +BuildingClients.levels)
    Logger.debug("Waiters" +BuildingWaiters.levels)
    if (BuildingWaiters.isEmpty && BuildingClients.isEmpty){
       State.update(Nothing())
    }
    else { 
	    val currentLevel=State.level
//	    val happyWaiters = currentProblem.waiters.filter(waiter => waiter.callLevel==currentLevel)
	    val happyWaiters= BuildingWaiters.levels.get(currentLevel).getOrElse("0")
//	    val happyClients = currentProblem.clients.filter(client => client.stop==currentLevel)
	    val happyClients = BuildingClients.levels.get(currentLevel).getOrElse("0")
	    if (happyClients==0 && happyWaiters==0){
	      State.door match {
		       case _:Opened => 
		           {//fermer la porte avant de partir!
		            Logger.debug("CLOSE");
		            State.update(Close())
//		             ActionAndListAction(CurrentState(State.level,Close(),Closed()))
		            }
		        case _ => State.calculDirection()
	        	}
	    }
	    else{
	      State.door match {
	        case _:Closed  		=>{	Logger.debug("OPEN");  
	        						State.update(Open())
	        					  }
	        //BUG, les users ne rentrent pas si la porte est deja ouverte au bon etage quand ils arrivent
	        case other  		=>{Logger.debug("Doors already opened, Come in!");  
	        					   State.update(Nothing())	        					   
	        					  }
	      	}
	      
	    }
    }
    ActionResponse
  }
  
  
  def ActionResponse()=Action{
//    val currentProblem =Elevator.getProblemFromCache
//    Elevator.saveProblemToCache(currentProblem.clients,currentProblem.waiters,state)
    Logger.debug("nextCommand ActionAndListAction" + State.action.label)
    Ok(State.action.label) 
  }
  


  

}


