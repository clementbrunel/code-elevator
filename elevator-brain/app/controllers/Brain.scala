package controllers
import play.api._
import play.api.mvc._
import play.Logger
import models._
import models.DSL._
import tools._
import play.api.cache.Cache
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._




object Brain  extends Controller {
  
   val ResetForm = Form(
	    	"cause" -> nonEmptyText
   )
	
  def CalcResponse(action:String="")= {
	    Action {
	      Logger.info("Brain Response"+ action)
		    Elevator.ResetManuAsked match {
		      case 0 => action match {
//		        case "" 	=>InternalServerError("Invalid Calculated Response")
		        case others =>Ok(action)
		      }
		      case 1 => {
		        Logger.info("*****************resetManuDone*************************" )
		        Elevator.resetAll
		        BadRequest("OopsResetAsked")
		      }
		    }
	    }
  }
  def index() = Action {
    val message = Elevator.ResetManuAsked match {
      case 1 	=> "Reset in Progress"
      case 0 	=> "Application is ready"
    }
    Ok(views.html.index(message,ResetForm))
  }
  
  def resetManu(message:String) = Action {implicit request =>
    ResetForm.bindFromRequest.fold(
				// Cas d erreurs du formulaire
				errors => {
				    Redirect(routes.Brain.index())
				},
				// Cas de reussite du formulaire
				success => {
					Logger.info("resetManu" + message)
				    Elevator.resetManuAsked
				    Redirect(routes.Brain.index())    
				}
    		)
  }
  
  def call(atFloor:Int, to:String) ={
    Logger.info("call atfloor" + atFloor + "To"+ to)
    BuildingWaiters.add(atFloor, Waiter(atFloor,Direction.labelToDirection(to)))
    Logger.debug("call End Waiters " +Elevator.toString) 
    CalcResponse()
  }
  
  def go(floorToGo:Int) =  {
    Logger.info("floorToGo" + floorToGo)
    BuildingClients.add(floorToGo,Client(floorToGo))
    Logger.debug("go Clients" +Elevator.toString) 
    CalcResponse()
  }
  
  def userHasEntered() =  {
    Logger.info("userHasEntered")
    BuildingWaiters.minus(State.level)
    Logger.debug("userHasEntered End" +Elevator.toString) 
    CalcResponse()
  }
  
  def userHasExited() =  {
    Logger.info("userHasExited")
    BuildingClients.minus(State.level)
    Logger.debug("userHasExited End" + Elevator.toString) 
    CalcResponse()
  }
  
  def reset(message:String) =  {
    Logger.info("reset" + message)
    Elevator.resetAll
    Logger.debug("reset Clients " +Elevator.toString) 
    CalcResponse()
  }
  
  def nextCommand()={Timer.chrono {
	    Logger.info("nextCommand")
	    val action = Elevator.nextCommand
	    CalcResponse(action.label) 
	  }
  }
}


