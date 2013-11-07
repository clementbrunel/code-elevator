package models
import play.Logger
import models._
import models.DSL._
import tools.Log

case class Elevator(state:State,waiters:BuildingFile,client:BuildingFile){
}


object Elevator{
  var ResetManuAsked = 0;
  var history:List[Int]=List();
  
  
  def resetAll= {  
    BuildingClients.reset
	BuildingWaiters.reset
	State.reset
	resetManuDone
	CrashDetection.resetCounter
	history=List();
  	}
  def resetManuDone= {
    ResetManuAsked=0;
    }
  def resetManuAsked(command:Int)= {
    ResetManuAsked=command;
    }
  override def toString():String={
    " ElevatorProblem { Clients :  "+BuildingClients.levels.map (level => "Level: "+level._1+","+level._2.size).mkString("/")+"\n"+
    " Waiters " + BuildingWaiters.levels.map (level => "Level: "+level._1+level._2.mkString(",",",","")).mkString("/")+"\n"+
    " State" + State.toString + " ResetManuAsked " + ResetManuAsked+" CrashDetectionCounter " + CrashDetection.countAction+"\n"+
    " History" +history.mkString(",")+"}"
  }
  
  
  def addWaiterOrNot(atFloor:Int,waiter:Waiter)={
    if (atFloor==State.level && State.door==Opened()){
    	Log.warning("Waiter not added because of currentState")
    }
    else{
    	BuildingWaiters.add(atFloor, waiter)   
    }
  }
  
  def nextCommand:Command={
    	val lastAction=State.action
    	
    	val nextCommand =  Algo.currentAlgo.nextCommand
    	
    	history=CrashDetection.add(history, State.level)
    	
    	 if (CrashDetection.isLooped(history)) {
    	   lastAction match {
    	      case up:Up if State.level< Specs.maxLevel			=> State.update(Up); //on continue les actions pour eviter le bouclage!
		      case down:Down if State.level>Specs.minLevel 		=>State.update(Down)
		      case downElse if State.level>Specs.minLevel 		=>State.update(Down)
		      case upElse if State.level< Specs.maxLevel		=>State.update(Up)
    	   }
    	 }
    	 else{
    	   nextCommand
    	 }   	
  }
}