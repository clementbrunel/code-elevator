package models

case class Level(floor:Int){
  def label:Int=floor;
  val max=5;
  val min=0;
  def ++ =Level(floor+1)
  def -- =Level(floor-1)
  def +(level:Level):Level=Level(Math.abs(floor+level.label))
  def -(level:Level):Level=Level(Math.abs(floor-level.label))
  def topPossible=floor+1<=max
  def bottomPossible=floor-1>=min
//  def isPermited=floor>=min && floor<=max
}