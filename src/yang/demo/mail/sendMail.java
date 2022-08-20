package yang.demo.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class sendMail
{
  public static void sendMail(String title, String message)
  {
    Properties prop = new Properties();
    prop.setProperty("mail.transport.protocol", "smtp");
    prop.setProperty("mail.smtp.host", "smtp.163.com");
    prop.setProperty("mail.smtp.auth", "true");
    prop.setProperty("mail.debug", "true");
    
    Session session = Session.getInstance(prop);
    Message msg = new MimeMessage(session);
    try
    {
      msg.setFrom(new InternetAddress("yangisboy@163.com"));
      msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("aliceasmud@gmail.com"));
      msg.setSubject(title);
      msg.setText(message);
    }
    catch (AddressException e)
    {
      e.printStackTrace();
    }
    catch (MessagingException e)
    {
      e.printStackTrace();
    }
    Transport tran = null;
    try
    {
      tran = session.getTransport();
    }
    catch (NoSuchProviderException e)
    {
      e.printStackTrace();
    }
    try
    {
      tran.connect("yangisboy", "dxy13633528994");
    }
    catch (MessagingException e)
    {
      e.printStackTrace();
    }
    try
    {
      tran.sendMessage(msg, msg.getAllRecipients());
    }
    catch (MessagingException e)
    {
      e.printStackTrace();
    }
  }
}
