package tools
import play.Logger
import models._

object Log{
	def debug(message:String)=Logger.debug(message)
	
	def severe(message:String)={
			Mail.send(message + Elevator.toString)
			Logger.error(message)
	}
	
	def info(message:String)=Logger.info(message)
	  
	def warning(message:String)=Logger.warn(message)
}