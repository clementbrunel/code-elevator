package models


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

object BuildingClients{
	var initialTowerClients=BuildingFile.initialState
	var levels=initialTowerClients.getFloors
	def isEmpty:Boolean=BuildingFile.isEmpty(levels)
	def reset:Unit=levels=BuildingFile.reset(levels)
	def add(level:Int,person:Client)=levels=BuildingFile.addPerson(level, person, levels)
	def minus(level:Int)=levels=BuildingFile.subPerson(level,levels)
	override def toString():String="BuildingClients" + levels.mkString(",")
	def size():Int=BuildingFile.size(levels)
}


object Specs{
  var clientPond=2
  var waiterPond=1
  var minLevel=0
  var maxLevel=19
  var bestCapacity=10
  var maxCapacity=42
  val detectionSeuil=3
  
}