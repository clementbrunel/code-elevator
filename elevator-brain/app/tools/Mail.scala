package tools
import com.typesafe.plugin._

object Mail {
   def send(message:String){
	val mail = use[MailerPlugin].email
	mail.setSubject("[Elevator-Brain] Error");
	mail.setRecipient("Brain-Elevator <clem.brunel@gmail.com>");
	mail.setFrom("Brain-Elevator <noreply@email.com>");
	//sends html
	mail.sendHtml("<html>"+message+"</html>" );
	//sends text/text
//	mail.send(message);
	//sends both text and html
//	mail.send( "text", "<html>html</html>");
	Log.debug("******************A mail has bee sent********************")
   }
}