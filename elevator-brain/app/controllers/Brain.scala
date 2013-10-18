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
	      if (!action.isEmpty()){
	      Log.info("Brain Response "+ action)
	      }
		    Elevator.ResetManuAsked match {
		      case 0 => action match {
//		        case "" 	=>InternalServerError("Invalid Calculated Response")
		        case others =>Ok(action)
		      }
		      case 1 => {
		        Log.info("*****************resetManuDone*************************" )
		        Elevator.resetAll
		        Mail.send("Application has manually reseted ")
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
					Log.info("resetManu" + message)
				    Elevator.resetManuAsked
				    Redirect(routes.Brain.index())    
				}
    		)
  }
  
  def call(atFloor:Int, to:String) ={
    Log.info("call atfloor" + atFloor + "To"+ to)
    BuildingWaiters.add(atFloor, Waiter(atFloor,Direction.labelToDirection(to)))
    Log.debug("call End Waiters " +Elevator.toString) 
    CalcResponse()
  }
  
  def go(floorToGo:Int) =  {
    Log.info("floorToGo" + floorToGo)
    BuildingClients.add(floorToGo,Client(floorToGo))
    Log.debug("go Clients" +Elevator.toString) 
    CalcResponse()
  }
  
  def userHasEntered() =  {
    Log.info("userHasEntered")
    BuildingWaiters.minus(State.level)
    Log.debug("userHasEntered End" +Elevator.toString) 
    CalcResponse()
  }
  
  def userHasExited() =  {
    Log.info("userHasExited")
    BuildingClients.minus(State.level)
    CrashDetection.resetCounter
    Log.debug("userHasExited End" + Elevator.toString) 
    CalcResponse()
  }
  
  def reset(message:String) =  {
    Log.info("reset" + message)
    Elevator.resetAll
    Log.debug("reset Clients " +Elevator.toString) 
    CalcResponse()
  }
  
  def nextCommand()={Timer.chrono {
	    Log.info("nextCommand")
	    val action = Elevator.nextCommand
	    Log.info("nextCommand passed " + Elevator.toString) 
	    CalcResponse(action.label) 
	  }
  }
}


