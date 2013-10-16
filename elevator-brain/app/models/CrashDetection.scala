package models

case class CrashDetection(history:List[Int]) {
  
  val detectionSeuil=3
  var countAction=0;
  
  def isLooped():Boolean={
    if (history.size == detectionSeuil){
      if ((history.head== history.tail.tail) && (history.head!= history.tail.head)){
        return true;
      }
      else return false;
    }
    else {
      return false;
    }
  }
  
  def add(floor:Int):List[Int]={
    return List(floor)++history.take(detectionSeuil)
  }
  
  def lastUserExcited{
    countAction+=1
  }
  def reset(){
    countAction=0
  }
  def isCrashed():Boolean= {
        return countAction > 2 * (Specs.minLevel - Specs.maxLevel);
    }

}