package controllers
import play.api._
import play.api.mvc._
import play.Logger
import models.State
import models.InitialState
import models.Action
import models.Level
import models.Person
import models.Waiter
import models.Client
import models.Elevator
import models.Up
import models.Down
import models.Open
import models.Close
import models.Nothing
import models.DSL._




object Brain  extends Controller {
  val elevator:Elevator=new Elevator()
  val listAction:List[Action]=List()
  
  
  def call(atFloor:Int, to:String) = Action {
    Logger.debug("call atfloor" + atFloor + "To"+ to)
    elevator.waiters.++(List(Waiter(Level(atFloor),Up)))
    Logger.debug("Actual Size of waiters" +elevator.waiters.size)
    Ok("")
  }
  
  def go(floorToGo:Int) = Action {
    Logger.debug("floorToGo" + floorToGo)
    elevator.clients.++(List(Client(Level(floorToGo))))
    Logger.debug("Actual Size of clients" +elevator.clients.size)
    Ok("")
  }
  
  def userHasEntered() = Action {
    Logger.debug("userHasEntered")
    //Todo... A ammeliorer. Super lourd....
    val waitersAtThisLevel = elevator.waiters.filter(waiter => waiter.callLevel==elevator.state.level).tail
    elevator.waiters.filter(waiter => waiter.callLevel!=elevator.state.level)++waitersAtThisLevel
    Logger.debug("Actual Size of waiters" +elevator.waiters.size)
    Ok("")
  }
  
  def userHasExited() = Action {
    Logger.debug("userHasExited")
     //Todo... A ammeliorer. Super lourd....
    elevator.clients.filter(client => client.stop==elevator.state.level).tail++elevator.clients.filter(client => client.stop!=elevator.state.level)
    Logger.debug("Actual Size of clients" +elevator.clients.size)
    Ok("")
  }
  
  def reset(message:String) = Action {
    Logger.debug("reset" + message)
    elevator.reset()
    Ok("")
  }
  
  def nextCommand()={
    Logger.debug("nextCommand")
    if (elevator.waiters.isEmpty && elevator.clients.isEmpty){
      ActionAndListAction(Nothing())
    }
    val currentLevel=elevator.state.level
    val happyWaiters = elevator.waiters.filter(waiter => waiter.callLevel==currentLevel)
    val happyClients = elevator.clients.filter(client => client.stop==currentLevel)
    if (happyClients.isEmpty && happyWaiters.isEmpty){
      if (listAction.head == Open()){
          ActionAndListAction(Close())
      }
      else {
    	  ActionAndListAction(elevator.calculDirection())
      } 
    }
    else {
    	  ActionAndListAction(Open())
    }
  }
  
  
  def ActionAndListAction(action:Action)=Action{
    action :: listAction
    Ok(action.label) 
  }
}


