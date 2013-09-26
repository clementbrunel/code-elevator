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
    Logger.debug("call End Waiters " +BuildingWaiters.levels) 
    Ok("")
  }
  
  def go(floorToGo:Int) = Action {
    Logger.debug("floorToGo" + floorToGo)
    BuildingClients.++(floorToGo, 1)
    Logger.debug("go Clients" +BuildingClients.levels) 
    Ok("")
  }
  
  def userHasEntered() = Action {
    Logger.debug("userHasEntered")
    BuildingWaiters.++(State.level,-1)
    Logger.debug("userHasEntered End" +BuildingWaiters.levels) 
    Ok("")
  }
  
  def userHasExited() = Action {
    Logger.debug("userHasExited")
    BuildingClients.++(State.level,-1)
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
       State.update(Nothing())
    }
    else { 
	    val currentLevel=State.level
	    val happyWaiters= BuildingWaiters.levels.get(currentLevel).getOrElse("0")
	    val happyClients = BuildingClients.levels.get(currentLevel).getOrElse("0")
	    if (happyClients==0 && happyWaiters==0){
	      State.door match {
		       case _:Opened => 
		           {//Close the door before Start!
		            Logger.debug("CLOSE");
		            State.update(Close())
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
	        case other  		=>{Logger.debug("Doors already opened, Come in but closed then Reopen for Server");  
	        					   State.update(Close())	        					   
	        					  }
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


