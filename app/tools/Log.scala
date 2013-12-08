package tools
import play.Logger
import models._
import java.util.Calendar
import java.text.SimpleDateFormat
  
object Log{
    val curTimeFormat = new SimpleDateFormat("HH:mm:ss")
    var displayLogs=true;
    var displayLevel=2;
    
    private def display(level:LogLevel,message:String)={
      val prefix=level.name+" "+curTimeFormat.format(Calendar.getInstance.getTime)+" "
    if (displayLogs) level match {
      case lev1:Debug if lev1.priority>=displayLevel =>Logger.debug(prefix+message)
      case lev2:Info if lev2.priority>=displayLevel =>Logger.info(prefix+message)
      case lev3:Warning if lev3.priority>=displayLevel =>Logger.warn(prefix+message)
      case lev4:Severe if lev4.priority>=displayLevel =>Logger.error(prefix+message)
      case _	=> ()
    }
}
    
	def debug(message:String)=display(Debug(),message)
	
	def info(message:String)=display(Info(),message)
	
	
	def warning(message:String)=display(Warning(),message)
	
	def severe(message:String)={
			Mail.send(message + Algo.toString)
			display(Severe(),message)
	}	
}

trait LogLevel {
  def display:(String, String)
  def name:String
}

case class Debug(name:String="Debug",priority:Int=1) extends LogLevel{
  def display()=(priority.toString -> name)
}
case class Info(name:String="Info",priority:Int=2) extends LogLevel{
  def display()=(priority.toString -> name)
}
case class Warning(name:String="Warning",priority:Int=3) extends LogLevel{
  def display()=(priority.toString -> name)
}
case class Severe(name:String="Severe",priority:Int=4) extends LogLevel{
  def display()=(priority.toString -> name)
}

object LogLevel{
  def ListLevel:Seq[(String, String)]=List(Debug().display,Info().display,Warning().display,Severe().display)
}