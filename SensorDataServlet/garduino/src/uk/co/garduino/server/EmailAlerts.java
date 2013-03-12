package uk.co.garduino.server;
import java.security.Security;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.smtp.SMTPTransport;


public class EmailAlerts {
	private String to;
	private String from;
	private String mailServer; 
	private String user;
	private String pass;
	private String messageBody;
	
	public EmailAlerts(String to, String from, String mailServer, String user, String pass) {
		this.to = to;
		this.from = from;
		this.mailServer = mailServer;
		this.user = user;
		this.pass = pass;
	}
	
	public void setMessage(String message) {
		this.messageBody = message;
	}
	
	/**
	 * 
	 * Almost all of this is lightly modified from the example on stackoverflow by "Mohit Bansal"
	 * http://stackoverflow.com/questions/3649014/send-email-using-java
	 */
	public void send() {  
      Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
      final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
     
      Properties props = System.getProperties();
      props.setProperty("mail.smtps.host", "smtp.gmail.com");
      props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
      props.setProperty("mail.smtp.socketFactory.fallback", "false");
      props.setProperty("mail.smtp.port", "465");
      props.setProperty("mail.smtp.socketFactory.port", "465");
      props.setProperty("mail.smtps.auth", "true");
      props.put("mail.smtps.quitwait", "false");
      
      Session session = Session.getInstance(props, null);

      try{
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         message.setSubject("Your plants need attention!");
         message.setText(messageBody);
         SMTPTransport t = (SMTPTransport)session.getTransport("smtps");
         t.connect(mailServer, user, pass);
         t.sendMessage(message, message.getAllRecipients());      
         t.close();
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
	}
}
