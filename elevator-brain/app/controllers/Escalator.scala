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
    val currentProblem =getProblemFromCache
    val waiterList = currentProblem.waiters++(List(Waiter(Level(atFloor),Up)))
    saveProblemToCache(currentProblem.clients,waiterList,currentProblem.state)
    Logger.debug("Actual Size of waiters" +waiterList.size)
    Ok("")
  }
  
  def go(floorToGo:Int) = Action {
    Logger.debug("floorToGo" + floorToGo)
    val currentProblem =getProblemFromCache
    val clientList = currentProblem.clients++(List(Client(Level(floorToGo))))
	saveProblemToCache(clientList,currentProblem.waiters,currentProblem.state)
    Logger.debug("Actual Size of clients" +clientList.size)
    Ok("")
  }
  
  def userHasEntered() = Action {
    Logger.debug("userHasEntered")
    //Todo... A ammeliorer. Super lourd....
    val currentProblem =getProblemFromCache
    val waitersAtThisLevel = currentProblem.waiters.filter(waiter => waiter.callLevel==currentProblem.state.level).tail
    val remainingWaiters = currentProblem.waiters.filter(waiter => waiter.callLevel!=currentProblem.state.level)++waitersAtThisLevel
	saveProblemToCache(currentProblem.clients,remainingWaiters,currentProblem.state)
    Logger.debug("Actual Size of waiters" +remainingWaiters.size)
    Ok("")
  }
  
  def userHasExited() = Action {
    Logger.debug("userHasExited")
     //Todo... A ammeliorer. Super lourd....
    val currentProblem =getProblemFromCache
    val remainingClients=currentProblem.clients.filter(client => client.stop==currentProblem.state.level).tail++currentProblem.clients.filter(client => client.stop!=currentProblem.state.level)
    saveProblemToCache(remainingClients,currentProblem.waiters,currentProblem.state)
    Logger.debug("Actual Size of clients" +remainingClients.size)
    Ok("")
  }
  
  def reset(message:String) = Action {
    Logger.debug("reset" + message)
    saveProblemToCache(List(),List(),InitialState())
    Ok("")
  }
  
  def nextCommand()={
    Logger.debug("nextCommand")
    val currentProblem =getProblemFromCache()
    if (currentProblem.waiters.isEmpty && currentProblem.clients.isEmpty){
      ActionAndListAction(CurrentState(currentProblem.state.level,Nothing()))
    }
    else { 
	    val currentLevel=currentProblem.state.level
	    val happyWaiters = currentProblem.waiters.filter(waiter => waiter.callLevel==currentLevel)
	    val happyClients = currentProblem.clients.filter(client => client.stop==currentLevel)
	    if (happyClients.isEmpty && happyWaiters.isEmpty){
	      currentProblem.state.action match {
		       case _:Open => 
		           {
		            Logger.debug("CLOSE");
		             ActionAndListAction(CurrentState(currentProblem.state.level,Close()))
		            }
		        case _ => ActionAndListAction(Elevator.calculDirection(currentProblem.clients,currentProblem.waiters,currentProblem.state))
	        	}
	    }
	    else {
	      Logger.debug("OPEN");  
	      ActionAndListAction(CurrentState(currentProblem.state.level,Open()))
	    }
    }
  }
  
  
  def ActionAndListAction(state:State)=Action{
    val currentProblem =getProblemFromCache
    saveProblemToCache(currentProblem.clients,currentProblem.waiters,state)
    Logger.debug("nextCommand ActionAndListAction" + state.action.label)
    Ok(state.action.label) 
  }
  
  def getProblemFromCache():Elevator={
    val elevRecup = Cache.getAs[Elevator]("ElevatorProblem") match {
      case Some(elev)  => Some(elev)
      case None 	   => None
    }
    Logger.debug("Problem recup from cache clients" +elevRecup)
    optionElevarToElevator(elevRecup)
  }
  
  def optionElevarToElevator(option:Option[Elevator]):Elevator={
    option.getOrElse(new Elevator())
  }
  
  def saveProblemToCache(clients:List[Client],waiters:List[Waiter],state:State):Elevator={
    val newElevator= new Elevator(clients,waiters,state)
    Cache.set("ElevatorProblem",newElevator)
    Logger.debug("Problem save to cache clients" +newElevator)
    newElevator
  }
}


