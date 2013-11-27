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
import tools.Log




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
	    	"levelLogs"	-> number,
	    	"algo"	-> nonEmptyText,
	    	"pondclient" -> number,
	    	"pondwaiter" -> number,
	    	"minlevel"-> number,
	    	"maxlevel" -> number,
	    	"cabinCount" -> number,
	    	"bestcapacity" -> number,
	    	"maxCapacity" -> number
	    )(ConfigForm.apply)(ConfigForm.unapply)
   )
	
  def CalcResponse(action:String="")= {
	    Action {
	      if (!action.isEmpty()){
	    	  Log.info("Brain Response "+ action)
	      }
		  Algo.ResetManuAsked match {
		      case 0 => Ok(action)
		      case 1 => {
		        Log.info("*****************resetManuDone*************************" )
		        Log.severe("Application has manually reseted (resetManuDone)")        
		        BadRequest("OopsResetAsked")
		      }
		      case 2 => {
		        Log.info("*****************CrashDetected*************************" )
		         Log.severe("Application has Crashed (CrashDetected)")        
		        BadRequest("OopsApplicationCrashed")
		      }
		      case 3 => {
		        Log.info("*****************CrashIsBetter*************************" )
		         Log.severe("Application has Crashed (CrashIsBetter)")        
		        BadRequest("OopsApplicationMustCrashed")
		      }
		    }
	    }
  }
   
  def config() = Action {implicit request =>
         FormConfig.bindFromRequest.fold(
				// Cas d erreurs du formulaire
				errors => {
				    Ok(views.html.index("Bad Config",FormReset,errors,LogLevel.ListLevel,Algo.ListAlgo))
				},
				// Cas de reussite du formulaire
				success => {
					Log.info("displayLogs " + success.displayLogs + "EmailSender " + success.emailSend + " LogsLevel " + success.levelLogs+" Algo : "+success.algo 
					    +"\n PondClient : "+ success.pondclient +" PondWaiter : "+ success.pondwaiter +" MaxLevel : " +success.maxLevel)
					    
					Mail.isActivated=success.emailSend
					Log.displayLogs=success.displayLogs
					Log.displayLevel=success.levelLogs
					Algo.changeAlgo(success.algo)
					Specs.clientPond=success.pondclient
					Specs.waiterPond=success.pondwaiter
					Specs.minLevel=success.minLevel
					Specs.maxLevel=success.maxLevel
					Specs.cabinCount=success.cabinCount
					Specs.bestCapacity=success.bestCapacity
					Specs.maxCapacity=success.maxCapacity
					val filledForm = FormConfig.fill(ConfigForm(success.emailSend,success.displayLogs,success.levelLogs,success.algo,
					    Specs.clientPond,Specs.waiterPond,Specs.minLevel,Specs.maxLevel,Specs.cabinCount,Specs.bestCapacity,Specs.maxCapacity))
				    Ok(views.html.index("Good Config",FormReset,filledForm,LogLevel.ListLevel,Algo.ListAlgo))
				}
    		)
  }
  
  
  //TODO rajouter eframe avec vue + liste concurrent a affronter
  def index() = Action {
    val message = Algo.ResetManuAsked match {
      case 1 	=> "Reset in Progress"
      case 0 	=> "Application is ready"
    }
    val filledConfigForm = FormConfig.fill(ConfigForm(Mail.isActivated,Log.displayLogs,Log.displayLevel,Algo.currentAlgo.name,
        Specs.clientPond,Specs.waiterPond,Specs.minLevel,Specs.maxLevel,Specs.cabinCount,Specs.bestCapacity,Specs.maxCapacity))
    Ok(views.html.index(message,FormReset,filledConfigForm,LogLevel.ListLevel,Algo.ListAlgo))
  }
  
  def resetManu(message:String) = Action {implicit request =>
    FormReset.bindFromRequest.fold(
				// Cas d erreurs du formulaire
				errors => {
				    Ok(views.html.index("Bad Reset",errors,FormConfig,LogLevel.ListLevel,Algo.ListAlgo))
				},
				// Cas de reussite du formulaire
				success => {
					Log.info("resetManu" + message)
				    Algo.resetManuAsked(1)
				    Redirect(routes.Brain.index())    
				}
    		)
  }
  
  def call(atFloor:Int, to:String) ={
    Log.info("call atfloor" + atFloor + "To"+ to)
//Verifier si la modif annule le bug avec l arrive d un waiter au bon level -> OK
    Algo.addWaiterOrNot(atFloor, Waiter(atFloor,Direction.labelToDirection(to)))
    Log.debug("call End Waiters " +Algo.toString) 
    CalcResponse()
  }
  
  def go(floorToGo:Int,cabin:Int) =  {
    Log.info("floorToGo" + floorToGo)
    val cabineConcerned =Algo.getCabineByIndex(cabin)
    cabineConcerned.add(floorToGo,Client(floorToGo))
    Log.debug("go Clients" +Algo.toString) 
    CalcResponse()
  }
  
  def userHasEntered(cabin:Int) =  {
    Log.info("userHasEntered")
    val cabineConcerned =Algo.getCabineByIndex(cabin)
    BuildingWaiters.minus(cabineConcerned.state.level)
//    Log.debug("userHasEntered End" +Algo.toString) 
    CalcResponse()
  }
  
  def userHasExited(cabin:Int) =  {
    Log.info("userHasExited")
    val cabineConcerned =Algo.getCabineByIndex(cabin)
    cabineConcerned.minus(cabineConcerned.state.level)
    CrashDetection.resetCounter
//    Log.debug("userHasExited End" + Algo.toString) 
    CalcResponse()
  }
  
  def reset(message:String, lowerFloor:Int, higherFloor:Int, cabinSize:Int,cabinCount:Int) =  {
    Log.info("reset" + message)
    Log.severe("Application has automatically reseted :"+ message+ " lowerFloor : " +lowerFloor+" higherFloor : " +higherFloor+" cabinSize : " + cabinSize   )
    if (lowerFloor.!=(-1)) {Specs.minLevel=lowerFloor}
    if (higherFloor.!=(-1)) {Specs.maxLevel=higherFloor}
    if (cabinSize.!=(-1)) {Specs.maxCapacity=cabinSize}
    if (cabinCount.!=(-1)) {Specs.cabinCount=cabinCount}
    Algo.resetAll
    Log.debug("reset Clients " +Algo.toString) 
    CalcResponse()
  }
    
  def nextCommand()={Timer.chrono {
	    Log.info("nextCommand")
	    CrashDetection.resetHelp
	    val action = Algo.nextCommands().head
	    if	(CrashDetection.isCrashed()){
	      Log.warning("Application is going to restart")
	    }
	    Log.debug("nextCommand passed " + Algo.toString) 
	    Log.info("nextCommand Response " + action.label) 
	    CalcResponse(action.label)
  	}
  }
  
   def nextCommands()={Timer.chrono {
	    Log.info("nextCommandS for " + Specs.cabinCount)
	    CrashDetection.resetHelp
	    val actions:List[Command] = Algo.nextCommands()
	    if	(CrashDetection.isCrashed()){
	      Log.warning("Application is going to restart")
	    }
	    Log.debug("nextCommands passed " + Algo.toString) 
	    val stringActions = actions.map(act => act.label).mkString("","\n","\n")
	    Log.info("nextCommands Responses " + stringActions ) 
	    CalcResponse(stringActions)
  	}
  }
}


