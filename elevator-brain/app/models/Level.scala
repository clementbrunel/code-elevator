package models


case class BuildingFile(levels:Map[Int,List[Person]]){
  def this (rdc:Int,top:Int)=this(((rdc to top).map(number => (number,List()))).toMap)
}
object BuildingFile{
	def initialState= new BuildingFile(0,5)
	def reset(building:Map[Int,List[Person]])=building.map(level => (level._1,List()))
	def isEmpty(building:Map[Int,List[Person]])=building.filter( map => !map._2.isEmpty).isEmpty
	def ++(level:Int,person:Person,building:Map[Int,List[Person]])=building.updated(level, building.apply(level).+:(person))
	def --(level:Int,building:Map[Int,List[Person]])=building.updated(level, building.apply(level) match {case head::tail => tail; case others => Nil})
}
object BuildingWaiters{
	var initialTowerWaiters=BuildingFile.initialState
	var levels=initialTowerWaiters.levels
	def isEmpty=BuildingFile.isEmpty(levels)
	def reset=levels=BuildingFile.reset(levels)
	def ++(level:Int,person:Waiter)=levels=BuildingFile.++(level, person, levels)
	def --(level:Int)=levels=BuildingFile.--(level,levels)
}
object BuildingClients{
	var initialTowerClients=BuildingFile.initialState
	var levels=initialTowerClients.levels
	def isEmpty=BuildingFile.isEmpty(levels)
	def reset=levels=BuildingFile.reset(levels)
	def ++(level:Int,person:Client)=levels=BuildingFile.++(level, person, levels)
	def --(level:Int)=levels=BuildingFile.--(level,levels)
  
}


//Backup Code 
/*case class Level(floor:Int){
  val max=5;
  val min=0;
  def ++ =Level(floor+1)
  def -- =Level(floor-1)
  def +(level:Level):Level=Level(Math.abs(floor+level.floor))
  def -(level:Level):Level=Level(Math.abs(floor-level.floor))
  def topPossible=floor+1<=max
  def bottomPossible=floor-1>=min
}
trait Person
case class Waiter (callLevel:Level,direction:ElevatorAction) extends Person
case class Client (stop:Level) extends Person


object Person {
 def waiter(level:Level,direction:ElevatorAction)= Waiter(level,direction)
 def client(stop:Level)= Client(stop)
}  */