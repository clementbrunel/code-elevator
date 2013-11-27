package models
import tools.Log

class BuildingFile(private var floors:Map[Int,List[Person]]){
    def getFloors()=floors
	def this (rdc:Int,top:Int)=this(((rdc to top).map(number => (number,List()))).toMap)
	def this()= this(Specs.minLevel,Specs.maxLevel)
	def subPerson(level:Int)={floors=floors.updated(level, floors.apply(level) match {case head::tail => tail; case others => Nil});floors}
	def addPerson(level:Int,person:Person)={floors=floors.updated(level, floors.apply(level).+:(person));floors}
	def isEmpty():Boolean=floors.filter( map => !map._2.isEmpty).isEmpty
	def reset()={floors=floors.map(level => (level._1,List()))}
	def size():Int=floors.toList.map(level => level._2.size).sum
}
object BuildingFile{
	def initialState= new BuildingFile(Specs.minLevel,Specs.maxLevel)
  	def reset(building:Map[Int,List[Person]])=new BuildingFile(Specs.minLevel,Specs.maxLevel).floors
	def isEmpty(building:Map[Int,List[Person]]):Boolean=building.filter( map => !map._2.isEmpty).isEmpty	
	def addPerson(level:Int,person:Person,building:Map[Int,List[Person]])=building.updated(level, building.apply(level).+:(person))	
	def subPerson(level:Int,building:Map[Int,List[Person]])=building.updated(level, building.apply(level) match {case head::tail => tail; case others => Nil})
	def size(building:Map[Int,List[Person]]):Int=building.toList.map(level => level._2.size).sum
}

object BuildingWaiters{
	var initialTowerWaiters=BuildingFile.initialState
	var levels=initialTowerWaiters.getFloors
	
	def isEmpty:Boolean=BuildingFile.isEmpty(levels)
	
	def reset:Unit=levels=BuildingFile.reset(levels)
	
	def add(level:Int,person:Waiter)=levels=BuildingFile.addPerson(level, person,levels)
	
	def minus(level:Int)=levels=BuildingFile.subPerson(level,levels)
	
	override def toString():String="BuildingWaiters" + levels.mkString(",")
	
	def size():Int=BuildingFile.size(levels)
	
	
}


//TODO check if delete work after refacto
//object BuildingClients{
//	var initialTowerClients=BuildingFile.initialState
//	var levels=initialTowerClients.getFloors
//	def isEmpty:Boolean=BuildingFile.isEmpty(levels)
//	def reset:Unit=levels=BuildingFile.reset(levels)
//	def add(level:Int,person:Client)=levels=BuildingFile.addPerson(level, person, levels)
//	def minus(level:Int)=levels=BuildingFile.subPerson(level,levels)
//	override def toString():String="BuildingClients" + levels.mkString(",")
//	def size():Int=BuildingFile.size(levels)
//}
class BuildingClients{
    var state=new State()
  	var initialTowerClients=BuildingFile.initialState
	var levels=initialTowerClients.getFloors
	def isEmpty:Boolean=BuildingFile.isEmpty(levels)
	def reset:Unit=levels=BuildingFile.reset(levels)
	def add(level:Int,person:Client)=levels=BuildingFile.addPerson(level, person, levels)
	def minus(level:Int)=levels=BuildingFile.subPerson(level,levels)
	def size():Int=BuildingFile.size(levels)
	
	override def toString():String=levels.map (level => "Level: "+level._1+","+level._2.size).mkString("/")+"\n"
}


object Specs{
  var clientPond=2
  var waiterPond=1
  var minLevel=(0)
  var maxLevel=5 
  var bestCapacity=10
  var maxCapacity=60
  var cabinCount=1
  val detectionSeuil=3
  
}