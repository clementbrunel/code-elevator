package models
import play.Logger

case class Elevator (clients:List[Client],waiters:List[Waiter],state:State){
  def this(elev:Elevator)= this(elev.clients,elev.waiters,elev.state)
  def this()=this(List(),List(),InitialState())
  def reset()=new Elevator()
  
  def calculDirection():Action={
    //todo integrer la direction des waiters dans la prise de decision....
    val levelPonderationclients = clients.map (x => (x.stop,clients.count(p => p.stop ==x.stop)))
    Logger.debug("levelPonderationclients" + levelPonderationclients.toString())
    val levelPonderationwaiters = waiters.map (x => (x.callLevel,waiters.count(p => p.callLevel ==x.callLevel)))
    Logger.debug("levelPonderationwaiters" + levelPonderationwaiters.toString())
    val levelPonderation = levelPonderationclients ++ levelPonderationwaiters
    val map:Map[Level,Int]= (levelPonderation map(x => (x._1,levelPonderation.count(_ == x)))).toMap
    val distances:List[(Int,Level,Int)]=map.map (x => ((state.level-x._1).label,x._1,x._2)).toList.sortBy(diff => diff._1)
    if (distances.head._2.label>state.level.label){
      Logger.debug("calculDirection Up" + distances.head._1)
      Up()
    }
    else{
      Logger.debug("calculDirection down" + distances.head._1)
      Down()
    }
  }
}
