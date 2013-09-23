package models

trait Action{
  def label:String
}
case class Nothing() extends Action{
  def label="NOTHING"
}
case class Up() extends Action{
  def label="UP"
}
case class Down() extends Action{
  def label="DOWN"
}
case class Open() extends Action{
  def label="OPEN"
}
case class Close() extends Action{
  def label="CLOSE"
}

object DSL{
implicit def tUpTypeToUp(tUp:models.Up.type)=Up()
implicit def tDownTypeToDown(tUp:models.Down.type)=Down()
}