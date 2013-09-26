package models


case class BuildingFile(levels:Map[Int,Int]){
  def this (rdc:Int,top:Int)=this(((rdc to top).map(number => (number,0))).toMap)
}
object BuildingFile{
	def initialState= new BuildingFile(0,5)
	def reset(building:Map[Int,Int])=building.map(level => (level._1,0))
	def isEmpty(building:Map[Int,Int])=building.filter( map => map._2!=0).isEmpty
	def ++(level:Int,number:Int,building:Map[Int,Int])=building.updated(level, building.apply(level)+number)
}
object BuildingWaiters{
	var towerWaiters=BuildingFile.initialState
	var levels=towerWaiters.levels
	def isEmpty=BuildingFile.isEmpty(levels)
	def reset=levels=BuildingFile.reset(levels)
	def ++(level:Int,number:Int)=levels=BuildingFile.++(level, number, levels)
}
object BuildingClients{
	var towerClients=BuildingFile.initialState
	var levels=towerClients.levels
	def isEmpty=BuildingFile.isEmpty(levels)
	def reset=levels=BuildingFile.reset(levels)
	def ++(level:Int,number:Int)=levels=BuildingFile.++(level, number, levels)
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