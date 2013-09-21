package controllers
import play.api._
import play.api.mvc._

object  Escalator  extends Controller {

  def call(atFloor:Int, to:String) = Action {
    Ok(views.html.index("Your new application is ready : call atFloor" + atFloor + "to" + to))
  }
  
  def go(floorToGo:Int) = Action {
    Ok(views.html.index("Your new application is ready : floorToGo" + floorToGo))
  }
  
  def userHasEntered() = Action {
    Ok(views.html.index("Your new application is ready : userHasEntered"))
  }
  
  def userHasExited() = Action {
    Ok(views.html.index("Your new application is ready :userHasExited"))
  }
  
  def reset(message:String) = Action {
    Ok(views.html.index("Your new application is ready :reset message : " + message))
  }
  

}


