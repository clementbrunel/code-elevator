package models
import play.Logger

trait Command{
  def label:String
}
object Command{
  def labelToAction(label:String):Command={
    label match {
      case "UP" => Up()
      case "DOWN" => Down()
      case "OPEN" => Open()
      case "CLOSE" => Close()
      case "RESET" => Reset()
      case other => Nothing()
    }
    
  }
}
case class Nothing() extends Command{
  def label="NOTHING"
}
case class Up() extends Command{
  def label="UP"
}
case class Down() extends Command{
  def label="DOWN"
}
case class Open() extends Command{
  def label="OPEN"
}
case class Close() extends Command{
  def label="CLOSE"
}
case class Reset() extends Command{
  def label="RESET"
}

trait Direction{
  def label:String
}
object Direction{
  def labelToDirection(label:String):Direction={
    label match {
      case "UP" => UpDirection()
      case _ => DownDirection()
    }
  }
}
case class UpDirection() extends Direction{
  def label="UP"
}
case class DownDirection() extends Direction{
  def label="DOWN"
}

trait DoorState
case class Opened() extends DoorState
case class Closed() extends DoorState

object DSL{
implicit def StringToAction(label:String)=Command.labelToAction(label)
implicit def tUpTypeToUp(tUp:models.Up.type)=Up()
implicit def tDownTypeToDown(tUp:models.Down.type)=Down()
implicit def tNothingTypeToNothing(tUp:models.Nothing.type)=Nothing()
implicit def tOpenTypeToOpen(tUp:models.Open.type)=Open()
implicit def tCloseTypeToClose(tUp:models.Close.type)=Close()
implicit def tClosedTypeToClosed(tUp:models.Close.type)=Closed()
implicit def tOpenedTypeToOpened(tUp:models.Close.type)=Opened()

}