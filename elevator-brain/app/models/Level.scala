package models
//TODO now. Rajouter le nb de gens au level
case class Level(floor:Int){
  val max=5;
  val min=0;
  def ++ =Level(floor+1)
  def -- =Level(floor-1)
  def +(level:Level):Level=Level(Math.abs(floor+level.floor))
  def -(level:Level):Level=Level(Math.abs(floor-level.floor))
  def topPossible=floor+1<=max
  def bottomPossible=floor-1>=min
}

case class BuildingFile(levels:Map[Int,Int]){
  def this (rdc:Int,top:Int)=this(((rdc to top).map(number => (number,0))).toMap) 
}

object BuildingWaiters{
  var towerWaiters=new BuildingFile(0,5)
  var levels=towerWaiters.levels
//  var level:Int=0
  def ++(level:Int,number:Int)=levels=levels.updated(level, levels.apply(level)+number)
//  def ++(number:Int)=towerWaiters.levels.updated(level, towerWaiters.levels.apply(level)+number)
  def reset=levels=levels.map(level => (level._1,0))
  def isEmpty=levels.filter( map => map._2!=0).isEmpty
}
object BuildingClients{
  var towerClients=new BuildingFile(0,5)
//  var level:Int=0
  var levels=towerClients.levels
  def ++(level:Int,number:Int)=levels=levels.updated(level, levels.apply(level)+number)
//  def ++(number:Int)=towerClients.levels.updated(level, towerClients.levels.apply(level)+number)
  def reset=levels=levels.map(level => (level._1,0))
  def isEmpty=levels.filter( map => map._2!=0).isEmpty
}