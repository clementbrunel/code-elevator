package models
import play.Logger

trait ElevatorAction{
  def label:String
}
case class Nothing() extends ElevatorAction{
  def label="NOTHING"
}
case class Up() extends ElevatorAction{
  def label="UP"
}
case class Down() extends ElevatorAction{
  def label="DOWN"
}
case class Open() extends ElevatorAction{
  def label="OPEN"
}
case class Close() extends ElevatorAction{
  def label="CLOSE"
}




object DSL{
implicit def tUpTypeToUp(tUp:models.Up.type)=Up()
implicit def tDownTypeToDown(tUp:models.Down.type)=Down()
implicit def tNothingTypeToNothing(tUp:models.Nothing.type)=Nothing()
implicit def tOpenTypeToOpen(tUp:models.Open.type)=Open()
implicit def tCloseTypeToClose(tUp:models.Close.type)=Close()
}