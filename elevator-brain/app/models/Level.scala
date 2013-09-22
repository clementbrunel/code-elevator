package models

case class Level(floor:Int){
  def label:Int=floor;
  val max=5;
  val min=0;
  def topPossible=floor+1<=max
  def bottomPossible=floor-1>=min
//  def isPermited=floor>=min && floor<=max
}