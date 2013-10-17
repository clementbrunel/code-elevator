package models
import play.Logger
import models.DSL._

trait State{
    def level:Int
    def action:Command
    def door:DoorState
}
case class InitialState(level:Int=0,action:Command=Nothing(),door:DoorState=Closed()) extends State
case class CurrentState(currentLevel:Int,currentaction:Command,doorState:DoorState) extends State{
  def level=currentLevel
  def action=currentaction
  def door=doorState
  def clone (state:State)=new CurrentState(state.level,state.action,state.door)
}
object State{
  var level:Int=0
  def ++()={level=level+1}
  def --()={level=level-1}
  var action:Command=Nothing()
  var door:DoorState=Closed()
  val start=InitialState()
  def reset={level=0;door=Closed()}
  
  override def toString()={
    "State { level:" + level + " action: " + action.label + " doors: " + door.label+"}"
  }

  def update(processAction:Command):Command={processAction match {
    case open:Open => {door=Opened(); Logger.debug("Update State open, Open the door")}
    case close:Close => {door=Closed(); Logger.debug("Update State close, Close the door")}
    case up:Up =>  {State++; Logger.debug("Update State Up, level++")}
    case down:Down => {State--; Logger.debug("Update State Down, level--")}
    case nothing:Nothing => Logger.debug("Update State Nothing, No changement")
  }
  Logger.debug("State after Update =  Level: " + State.level+ " Action : "+State.action.label+ " Doors : "+ State.door) 
  action=processAction
  processAction
  }
  
  
  def calculDirection():Command={
    //todo integrer la direction des waiters dans la prise de decision.... chiant et ca arrive jamais 1 fois en 10 min...
    
//    val map = BuildingClients.levels ++ BuildingWaiters.levels .map{ case (k,v) => k -> (v.size + (BuildingClients.levels.get(level) match {case None => 0.toInt; case Some(list) => list.size})) } 
    val map = (0 to 5).map (i=> (i,BuildingClients.levels.getOrElse(i,List()).size + BuildingWaiters.levels.getOrElse(i,List()).size)).toMap
    Logger.debug("map (level,pond" + map)
    val distances:List[(Int,Int,Int)]=doPonderation(map)
//    val distancesinf = distances.filter(level => level._2 < State.level)
//    val distancessup = distances.filter(level => level._2 > State.level)
    Logger.debug("distances (diff,level,pond" + distances)
//    val inf = distancesinf.map(tuple => tuple._1).sum
//    Logger.info("inf" + inf)
//    val sup = distancessup.map(tuple => tuple._1).sum
//    Logger.info("sup" + sup)

//    TODO eviter les demi tour pour un waiter.... par contre faire la direction sur la somme de la direction?
    (distances,State.level) match {
      case (head::tail,level) if head._2>level 				=>{	Logger.debug("calculDirection Up" + distances.head._2)    	  																   	
      																		State.update(Up)   //doors must be closed!
    	  																   }
      case (head::tail,level) if head._2<level 				=>{ Logger.debug("calculDirection down" + distances.head._2)	  																	
      																		State.update(Down)
      																	   }
      case (others,level)									=>{ Logger.error("calculDirection ERROR Last Case Nothing") 
        														State.update(Nothing())
   															 }
    }  
//    if (inf>sup){
//      Logger.debug("calculDirection down")
//          State.update(Down)
//    }
//    else {
//      Logger.debug("calculDirection up")
//          State.update(Up)
//    }
  }
  
  def doPonderation(levels:Map[Int,Int]):List[(Int,Int,Int)]={
    //on filtre les donnees sans ponderation car inutiles pour le calcul... -> evite le bloquage au niveau actuel vide. 
    val etap1= levels.filter(elem => elem._2!=0)
    Logger.debug("levels without useless levels" + etap1)
    //On calcul la difference avec le niveau actuel pour la ponderation
    val etap2 = etap1.map (x => (Math.abs(level-x._1),x._1,x._2)).toList
    Logger.debug("levels with difference" + etap2)
    
    //On change la ponderation avec la difference de level
    val etap3= etap2.map(etap => ((etap._1/etap._3),etap._2,etap._3))
    Logger.debug("levels with difference and pond" + etap3)
     //On tri par poids puis par nb de passage, vaut mieux deux mouvement pour 2 personnes que 1 pour 1.
    val etap4= etap3.sortBy(diff => diff._1 -> diff._3)
    Logger.info("levels with difference and pond and sorted " + etap4)
    etap4
  }
}


