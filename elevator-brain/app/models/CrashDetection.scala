package models
import play.Logger


object CrashDetection{
  val detectionSeuil=3
  var countAction=0;
  
  def isLooped(history:List[Int]):Boolean={
    if (history.size == detectionSeuil){
      if ((history.head== history.tail.tail) && (history.head!= history.tail.head)){
        Logger.warn("Cycle detected! ");
        return true;
      }
      else return false;
    }
    else {
      return false;
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
       val crashed =  countAction > 2 * (Specs.minLevel - Specs.maxLevel);
       if (crashed){
         Logger.warn("Application is Crashed!!! ");
       }
       crashed
    }
  
  def isKO(history:List[Int]):Boolean= isCrashed || isLooped(history)
}