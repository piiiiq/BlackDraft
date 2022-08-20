package yang.demo.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailBySSL
{
  private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
  private String smtpServer;
  private String port;
  private String username;
  private String password;
  private List<String> recipients = new ArrayList();
  private String subject;
  private String content;
  private List<String> attachmentNames = new ArrayList();
  
  public SendMailBySSL() {}
  
  public SendMailBySSL(String smtpServer, String port, String username, String password, List<String> recipients, String subject, String content, List<String> attachmentNames)
  {
    this.smtpServer = smtpServer;
    this.port = port;
    this.username = username;
    this.password = password;
    this.recipients = recipients;
    this.subject = subject;
    this.content = content;
    this.attachmentNames = attachmentNames;
  }
  
  public void setSmtpServer(String smtpServer)
  {
    this.smtpServer = smtpServer;
  }
  
  public void setPort(String port)
  {
    this.port = port;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public void setRecipients(List<String> recipients)
  {
    this.recipients = recipients;
  }
  
  public void setSubject(String subject)
  {
    this.subject = subject;
  }
  
  public void setContent(String content)
  {
    this.content = content;
  }
  
  public void setAttachmentNames(List<String> attachmentNames)
  {
    this.attachmentNames = attachmentNames;
  }
  
  public String changeEncode(String str)
  {
    return str;
  }
  
  public boolean sendMail()
  {
    Properties properties = new Properties();
    properties.put("mail.smtp.host", this.smtpServer);
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.put("mail.smtp.socketFactory.fallback", "false");
    
    properties.put("mail.smtp.port", this.port);
    properties.put("mail.smtp.socketFactory.port", this.port);
    
    Session session = Session.getInstance(properties);
    
    MimeMessage message = new MimeMessage(session);
    try
    {
      Address address = new InternetAddress(this.username);
      message.setFrom(address);
      for (String recipient : this.recipients)
      {
        Address toAddress = new InternetAddress(recipient);
        message.setRecipient(MimeMessage.RecipientType.TO, toAddress);
      }
      message.setSubject(changeEncode(this.subject));
      
      message.setSentDate(new Date());
      
      Multipart multipart = new MimeMultipart();
      
      Object text = new MimeBodyPart();
      ((BodyPart)text).setText(this.content);
      multipart.addBodyPart((BodyPart)text);
      for (String fileName : this.attachmentNames)
      {
        BodyPart adjunct = new MimeBodyPart();
        FileDataSource fileDataSource = new FileDataSource(fileName);
        adjunct.setDataHandler(new DataHandler(fileDataSource));
        adjunct.setFileName(changeEncode(fileDataSource.getName()));
        multipart.addBodyPart(adjunct);
      }
      this.recipients.clear();
      this.attachmentNames.clear();
      
      message.setContent(multipart);
      message.saveChanges();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
    try
    {
      Transport transport = session.getTransport("smtp");
      transport.connect(this.smtpServer, this.username, this.password);
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
