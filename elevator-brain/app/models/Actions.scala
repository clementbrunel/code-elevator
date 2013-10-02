package models
import play.Logger

trait ElevatorAction{
  def label:String
}
object ElevatorAction{
  def labelToAction(label:String):ElevatorAction={
    label match {
      case "UP" => Up()
      case "DOWN" => Down()
      case "OPEN" => Open()
      case "CLOSE" => Close()
      case other => Nothing()
    }
    
  }
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


trait DoorState
case class Opened() extends DoorState
case class Closed() extends DoorState

object DSL{
implicit def StringToAction(label:String)=ElevatorAction.labelToAction(label)
implicit def tUpTypeToUp(tUp:models.Up.type)=Up()
implicit def tDownTypeToDown(tUp:models.Down.type)=Down()
implicit def tNothingTypeToNothing(tUp:models.Nothing.type)=Nothing()
implicit def tOpenTypeToOpen(tUp:models.Open.type)=Open()
implicit def tCloseTypeToClose(tUp:models.Close.type)=Close()
implicit def tClosedTypeToClosed(tUp:models.Close.type)=Closed()
implicit def tOpenedTypeToOpened(tUp:models.Close.type)=Opened()

}