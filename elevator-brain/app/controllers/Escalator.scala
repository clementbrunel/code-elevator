package controllers
import play.api._
import play.api.mvc._
import play.Logger

object  Escalator  extends Controller {

  def call(atFloor:Int, to:String) = Action {
    Logger.debug("call atfloor" + atFloor + "To"+ to)
    Ok("")
  }
  
  def go(floorToGo:Int) = Action {
    Logger.debug("floorToGo" + floorToGo)
    Ok("")
  }
  
  def userHasEntered() = Action {
    Logger.debug("userHasEntered")
    Ok("")
  }
  
  def userHasExited() = Action {
    Logger.debug("userHasExited")
    Ok("")
  }
  
  def reset(message:String) = Action {
    Logger.debug("userHasExited")
    Ok("")
  }
  
  def nextCommand()=Action{
    Logger.debug("nextCommand")
    Ok("UP")
  }
}


