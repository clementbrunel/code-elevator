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
import tools.Log
import tools.Mail
import models.ResetForm
import models.ConfigForm
import tools.LogLevel




object Brain  extends Controller {
  
   
   val FormReset = Form(
       mapping(
	    	"cause" -> nonEmptyText
	    	)(ResetForm.apply)(ResetForm.unapply)
   )
   
   
   val FormConfig = Form(
        mapping(
	    	"emailSender" 	-> boolean,
	    	"displayLogs"	-> boolean,
	    	"levelLogs"	-> number
	    )(ConfigForm.apply)(ConfigForm.unapply)
   )
	
  def CalcResponse(action:String="")= {
	    Action {
	      if (!action.isEmpty()){
	      Log.info("Brain Response "+ action)
	      }
		    Elevator.ResetManuAsked match {
		      case 0 => Ok(action)
		      case 1 => {
		        Log.info("*****************resetManuDone*************************" )
		         Log.severe("Application has manually reseted ")
		        Elevator.resetAll	        
		        BadRequest("OopsResetAsked")
		      }
		      case 2 => {
		        Log.info("*****************CrashDetected*************************" )
		         Log.severe("Application has Crased ")
		        Elevator.resetAll	        
		        BadRequest("OopsApplicationCrased")
		      }
		    }
	    }
  }
   
  def config() = Action {implicit request =>
         FormConfig.bindFromRequest.fold(
				// Cas d erreurs du formulaire
				errors => {
				    Ok(views.html.index("Bad Config",FormReset,errors,LogLevel.ListLevel))
				},
				// Cas de reussite du formulaire
				success => {
					Log.info("displayLogs " + success.displayLogs + "EmailSender " + success.emailSend + " LogsLevel " + success.levelLogs)
					Mail.isActivated=success.emailSend
					Log.displayLogs=success.displayLogs
					Log.displayLevel=success.levelLogs
					val filledForm = FormConfig.fill(ConfigForm(success.emailSend,success.displayLogs,success.levelLogs))
				    Ok(views.html.index("Good Config",FormReset,filledForm,LogLevel.ListLevel))
				}
    		)
  }
  
  def index() = Action {
    val message = Elevator.ResetManuAsked match {
      case 1 	=> "Reset in Progress"
      case 0 	=> "Application is ready"
    }
    val filledConfigForm = FormConfig.fill(ConfigForm(Mail.isActivated,Log.displayLogs,Log.displayLevel))
    Ok(views.html.index(message,FormReset,filledConfigForm,LogLevel.ListLevel))
  }
  
  def resetManu(message:String) = Action {implicit request =>
    FormReset.bindFromRequest.fold(
				// Cas d erreurs du formulaire
				errors => {
				    Ok(views.html.index("Bad Reset",errors,FormConfig,LogLevel.ListLevel))
				},
				// Cas de reussite du formulaire
				success => {
					Log.info("resetManu" + message)
				    Elevator.resetManuAsked(1)
				    Redirect(routes.Brain.index())    
				}
    		)
  }
  
  def call(atFloor:Int, to:String) ={
    Log.info("call atfloor" + atFloor + "To"+ to)
//TODO Verifier si la modif annule le bug avec l arrive d un waiter au bon level
    Elevator.addWaiterOrNot(atFloor, Waiter(atFloor,Direction.labelToDirection(to)))
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
//    Log.debug("userHasEntered End" +Elevator.toString) 
    CalcResponse()
  }
  
  def userHasExited() =  {
    Log.info("userHasExited")
    BuildingClients.minus(State.level)
    CrashDetection.resetCounter
//    Log.debug("userHasExited End" + Elevator.toString) 
    CalcResponse()
  }
  
  def reset(message:String) =  {
    Log.info("reset" + message)
    Log.severe("Application has automatically reseted :"+ message)
    Elevator.resetAll
    Log.debug("reset Clients " +Elevator.toString) 
    CalcResponse()
  }
  
  def nextCommand()={Timer.chrono {
	    Log.info("nextCommand")
	    val action = Elevator.nextCommand
	    if (CrashDetection.isCrashed){
	    Elevator.resetManuAsked(2) 
	    }
	    Log.info("nextCommand passed " + Elevator.toString) 
	    CalcResponse(action.label)
  	}
  }
}


