package models
import play.Logger


object CrashDetection{
  val detectionSeuil=Specs.detectionSeuil
  var countAction=0;
  
  def isLooped(history:List[Int]):Boolean={
    history.size match {
      case less if less<detectionSeuil 		=> false
      case more if more>detectionSeuil 		=> isLooped(history.tail)
      case equals if equals>detectionSeuil	=> 	if ((history.head== history.tail.tail) && (history.head!= history.tail.head)){
    	  											Logger.warn("Cycle detected! ");
    	  											return true;
      												}
      											else return false;
    }
  }
  
  def add(floor:Int,history:List[Int]):List[Int]={
    return List(floor)++history.take(detectionSeuil)
  }
  
  def lastUserExcited{
    countAction+=1
  }
  def reset(){
    countAction=0
  }
  def isCrashed():Boolean= {
       val crashed =  countAction > 2 * (Specs.maxLevel - Specs.minLevel);
       if (crashed){
         Logger.warn("Application is Crashed!!! ");
       }
       crashed
    }
  
  def isKO(history:List[Int]):Boolean={
    val isKo = isCrashed || isLooped(history)
    if (isKo) Mail.send("Application has crashed   Loop : " +isLooped(history) + "Crash" + isCrashed); 
    isKo
  }
  
  def clean(history:List[Int]):List[Int]=history.take(detectionSeuil)
  def add(history:List[Int],level:Int):List[Int]=clean(level+:history)
}