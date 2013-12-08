package models
import play.Logger
trait withLabel{
  def label:String
}
trait Command extends withLabel

object Command{
  def labelToAction(label:String):Command={
    label match {
      case "UP" 	=> Up()
      case "DOWN" 	=> Down()
      case "OPEN" 	=> Open()
      case "CLOSE" 	=> Close()
      case "OPEN_UP" 	=> OpenUp()
      case "OPEN_DOWN" 	=> OpenDown()
      case other 	=> Nothing()
    }
    
  }
}
case class Nothing() extends Command{
  def label = "NOTHING"
}
case class Up() extends Command{
  def label = "UP"
}
case class Down() extends Command{
  def label = "DOWN"
}
case class Open() extends Command{
  def label = "OPEN"
}
case class OpenUp() extends Command{
  def label = "OPEN_UP"
}
case class OpenDown() extends Command{
  def label = "OPEN_DOWN"
}
case class Close() extends Command{
  def label = "CLOSE"
}

trait Reset extends withLabel{
  def code:Int
}

case class NoReset() extends Reset{
  def label = "NoReset"
  def code = 0
}

case class ResetManu() extends Reset{
  def label = "ResetManu"
  def code = 1
}

case class ResetCrash() extends Reset{
  def label = "ResetCrash"
  def code = 2
}

case class CrashIsBetter() extends Reset{
  def label = "CrashIsBetter"
  def code = 3
}





trait Direction extends withLabel

object Direction{
  def labelToDirection(label:String):Direction={
    label match {
      case 	"UP" 	=> UpDirection()
      case 	_ 		=> DownDirection()
    }
  }
}
case class UpDirection() extends Direction{
  def label = "UP"
}
case class DownDirection() extends Direction{
  def label = "DOWN"
}

trait DoorState extends withLabel
case class Opened() extends DoorState{
  def label = "Opened"
}
case class Closed() extends DoorState{
  def label = "Closed"
}

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