package models


case class BuildingFile(floors:Map[Int,List[Person]]){
	def this (rdc:Int,top:Int)=this(((rdc to top).map(number => (number,List()))).toMap)
	def this()= this(Specs.minLevel,Specs.maxLevel)
	def subPerson(level:Int)=floors.updated(level, floors.apply(level) match {case head::tail => tail; case others => Nil})
	def addPerson(level:Int,person:Person)=floors.updated(level, floors.apply(level).+:(person))
	def isEmpty():Boolean=floors.filter( map => !map._2.isEmpty).isEmpty
	def reset()=floors.map(level => (level._1,List()))
}
object BuildingFile{
	def initialState= new BuildingFile(Specs.minLevel,Specs.maxLevel)
  	def reset(building:Map[Int,List[Person]])=building.map(level => (level._1,List()))
	def isEmpty(building:Map[Int,List[Person]]):Boolean=building.filter( map => !map._2.isEmpty).isEmpty	
	def addPerson(level:Int,person:Person,building:Map[Int,List[Person]])=building.updated(level, building.apply(level).+:(person))	
	def subPerson(level:Int,building:Map[Int,List[Person]])=building.updated(level, building.apply(level) match {case head::tail => tail; case others => Nil})
	
}
object BuildingWaiters{
	var initialTowerWaiters=BuildingFile.initialState
	var levels=initialTowerWaiters.floors
	def isEmpty:Boolean=BuildingFile.isEmpty(levels)
	def reset:Unit=levels=BuildingFile.reset(levels)
	def add(level:Int,person:Waiter)=levels=BuildingFile.addPerson(level, person,levels)
	def minus(level:Int)=levels=BuildingFile.subPerson(level,levels)
	override def toString():String="BuildingWaiters" + levels.mkString(",")
}
//case class BuildingClients(clients:List[Client])
object BuildingClients{
	var initialTowerClients=BuildingFile.initialState
	var levels=initialTowerClients.floors
	def isEmpty:Boolean=BuildingFile.isEmpty(levels)
	def reset:Unit=levels=BuildingFile.reset(levels)
	def add(level:Int,person:Client)=levels=BuildingFile.addPerson(level, person, levels)
	def minus(level:Int)=levels=BuildingFile.subPerson(level,levels)
	override def toString():String="BuildingClients" + levels.mkString(",")
}


object Specs{
  val minLevel=0
  val maxLevel=5
  
  val detectionSeuil=3
}