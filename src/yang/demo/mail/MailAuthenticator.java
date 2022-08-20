package yang.demo.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class MailAuthenticator
  extends Authenticator
{
  private String userName;
  private String password;
  
  public MailAuthenticator(String userName, String password)
  {
    this.userName = userName;
    this.password = password;
  }
  
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(this.userName, this.password);
  }
}
