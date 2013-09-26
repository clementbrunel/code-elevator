package models
import play.Logger
//import play.api.cache.Cache
//import play.api.Play.current


//case class Elevator (clients:List[Client],waiters:List[Waiter],state:State){
//  def this(elev:Elevator)= this(elev.clients,elev.waiters,elev.state)
//  def this()=this(List(),List(),InitialState())
//  
// }

 object Elevator{
//   var clients:List[Client]=List()
//   var waiters:BuildingWaiters=List()
//   var state:State=InitialState()
   
//   def changeClients(newClients:List[Client])={clients=newClients}
//   def changeClients(changement:Int)={BuildingClients.levels.updated(State.level, BuildingClients.levels.apply(State.level)+changement)}
//   def changeWaiters(newWaiters:List[Waiter])={waiters=newWaiters}
//   def changeWaiters(changement:Int)={BuildingWaiters.levels.updated(State.level, BuildingWaiters.levels.apply(State.level)+changement)}
//   def changeState(newState:State)={state=newState}
   
//   def saveProblemToCache(newClients:List[Client],newWaiters:List[Waiter],newState:State):Elevator={ 
//    Elevator.changeClients(newClients)
//    Elevator.changeWaiters(newWaiters)
//    Elevator.changeState(newState)
//    val newElevator=Elevator(clients,waiters,state)
//    Logger.debug("Problem save to cache clients" + newElevator)
//    Elevator(clients,waiters,state)
//  }
 
//  def getProblemFromCache():Elevator={
//  val elevRecup=Elevator(clients,waiters,state)
//  Logger.debug("Problem recup from cache clients" +elevRecup)
//  elevRecup
//  }
   
//   def calculDirection(clients:List[Client],waiters:List[Waiter],state:State):State={
//    //todo integrer la direction des waiters dans la prise de decision....
//    val levelPonderationclients = clients.map (x => (x.stop, clients.count(p => p.stop ==x.stop)))
//    Logger.debug("levelPonderationclients" + levelPonderationclients)
//    
//    val levelPonderationwaiters =  waiters.map (x => (x.callLevel, waiters.count(p => p.callLevel ==x.callLevel)))
//    Logger.debug("levelPonderationwaiters" + levelPonderationwaiters)
//    
//    val levelPonderation = levelPonderationclients ++ levelPonderationwaiters
//    val map:Map[Level,Int]= (levelPonderation map(x => (x._1,levelPonderation.count(_ == x)))).toMap
//    val distances:List[(Int,Level,Int)]=map.map (x => (( State.level-x._1).floor,x._1,x._2)).toList.sortBy(diff => diff._1)
//    Logger.debug("distances (diff,level,pond" + distances)
//    (distances,state.level) match {
//      case (head::tail,level) if head._2.floor>level.floor 				=>{	Logger.debug("calculDirection Up" + distances.head._1)
//    	  																   	CurrentState(state.level++,Up(),state.door)   //doors must be closed!
//    	  																   }
//      case (head::tail,level) if head._2.floor<level.floor 				=>{ Logger.debug("calculDirection down" + distances.head._1)
//    	  																	CurrentState(state.level--,Down(),state.door)
//      																	   }
//      case others     													=> InitialState()
//    }
//  
//
//  }

   
//   def reset()=new Elevator()
 }
 