package models
import play.Logger

case class Elevator (clients:List[Client],waiters:List[Waiter],state:State){
  def this(elev:Elevator)= this(elev.clients,elev.waiters,elev.state)
  def this()=this(List(),List(),InitialState())
  
  
  def changeWaiters(newWaiters:List[Waiter]):List[Waiter]=waiters++newWaiters
  def changeClients(newClients:List[Client]):List[Client]=clients++newClients
  def changeState(state:State):State=state
 }
 
 object Elevator{
   def calculDirection(clients:List[Client],waiters:List[Waiter],state:State):State={
    //todo integrer la direction des waiters dans la prise de decision....
    val levelPonderationclients = clients.map (x => (x.stop, clients.count(p => p.stop ==x.stop)))
    Logger.debug("levelPonderationclients" + levelPonderationclients)
    
    val levelPonderationwaiters =  waiters.map (x => (x.callLevel, waiters.count(p => p.callLevel ==x.callLevel)))
    Logger.debug("levelPonderationwaiters" + levelPonderationwaiters)
    
    val levelPonderation = levelPonderationclients ++ levelPonderationwaiters
    val map:Map[Level,Int]= (levelPonderation map(x => (x._1,levelPonderation.count(_ == x)))).toMap
    val distances:List[(Int,Level,Int)]=map.map (x => (( state.level-x._1).label,x._1,x._2)).toList.sortBy(diff => diff._1)
    Logger.debug("distances (diff,level,pond" + distances)
    
    if (!distances.isEmpty && distances.head._2.label> state.level.label){
      Logger.debug("calculDirection Up" + distances.head._1)
      CurrentState(state.level++,Up())
    }
    if (!distances.isEmpty && distances.head._2.label< state.level.label){
      Logger.debug("calculDirection down" + distances.head._1)
      CurrentState(state.level--,Down())
    }
    else {InitialState()}
  }
   
   def reset()=new Elevator()
 }