package tools
import play.Logger
import tools.Mail

object Log{
	def debug(message:String)=Logger.debug(message)
	
	def severe(message:String)={Mail.send(message)
								Logger.error(message)
								}
	
	def info(message:String)=Logger.info(message)
	  
	def warning(message:String)=Logger.warn(message)
}