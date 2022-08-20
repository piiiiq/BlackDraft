package yang.demo.mail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.util.MailSSLSocketFactory;

public class mailTool
{
	/**
	 * 
	 * @param username
	 * @param password
	 * @param host
	 * @param port-1时不更改端口
	 * @return
	 * @throws Exception
	 */
  public static Message[] receive(String username, String password, String host,String port)
    throws Exception
  {
    Properties props = new Properties();
    if(!port.equals("-1")) {
	    props.setProperty("mail.pop3.port", port); 
    } 
    props.setProperty("mail.popStore.protocol", "pop3");       // 使用pop3协议
    props.put("mail.pop3.ssl.enable",true);
    Session session = Session.getInstance(props);    
    Store store = session.getStore("pop3");
    store.connect(host, username, password);
    
    Folder folder = store.getFolder("INBOX");
    folder.open(1);
    Message[] message = folder.getMessages();
    return message;
  }
  /**
   * 
   * @param num
 * @throws MessagingException
   * @throws IOException
   */
  public static Folder getFolder_INBOX(String username, String password, String host,String port) throws MessagingException {  
	  Properties props = new Properties();
	  if(!port.equals("-1"))
		    props.setProperty("mail.pop.port", port);
	    Session session = Session.getInstance(props);
	    
	    Store store = session.getStore("pop3");
	    store.connect(host, username, password);
	    
	    Folder folder = store.getFolder("INBOX");
	    folder.open(Folder.READ_WRITE);
      return folder;
  } 
}
