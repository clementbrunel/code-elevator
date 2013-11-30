package models
import play.Logger
import tools.Log
import tools.Mail


object CrashDetection{
  val detectionSeuil=Specs.detectionSeuil
  var countAction=0;
  var helDebug = ""
  
  def isLooped(history:List[Int]):Boolean={
    history.size match {
      case less if less<detectionSeuil 		=> false
      case more if more>detectionSeuil 		=> isLooped(history.tail)
      case equals							=> 	if ((history.head== history.tail.tail.head) && (history.head!= history.tail.head)){
    	  											return true;
      												}
      											else return false;
    }
  }
  
  def addHelp(add:String)=helDebug+=add+"\n"
  
  def resetHelp()=helDebug=""
  
  def getHelpAndReset():String={
    val message= helDebug
    resetHelp()
    message
  }  
  def incrementCounter{
    countAction+=1
  }
  def resetCounter(){
    countAction=0
  }
  def isCrashed():Boolean= {
       val crashed =  countAction > 5 * (Specs.maxLevel - Specs.minLevel);
       if (crashed){
         Log.warning("Application is Crashed!!! ");
         Algo.resetManuAsked(ResetCrash())
       }
       crashed
    }
  
  def isKO(history:List[Int]):Boolean={
    val isKo = isCrashed || isLooped(history)
    if (isKo) Log.severe("Application has crashed   Loop : " +isLooped(history) + " Crash " + isCrashed); 
    isKo
  }
  
  def clean(history:List[Int]):List[Int]=history.take(detectionSeuil)
  def add(history:List[Int],level:Int):List[Int]=clean(level+:history)
}