package tools
import com.typesafe.plugin._
import play.api.Play.current

object Mail {
   var isActivated=false; 
   def changeState={isActivated=(!isActivated) }
   def send(message:String){
	val mail = use[MailerPlugin].email
	mail.setSubject("[Elevator-Brain] Error");
	mail.setRecipient("Brain-Elevator <zemize78@randomail.net>");
	mail.setFrom("Brain-Elevator <noreply@email.com>");
	//sends html
	if (isActivated) {
	  mail.sendHtml("<html>"+message+"</html>" );
	  Log.debug("******************A mail has bee sent********************")
	}
	//sends text/text
//	mail.send(message);
	//sends both text and html
//	mail.send( "text", "<html>html</html>");
	
   }
}