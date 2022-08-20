package yang.app.qt.black;

import java.io.Serializable;

class markstat
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  public String text;
  public int count;
  public boolean visible;
  
  public markstat(String text, int count)
  {
    this.text = text;
    this.count = count;
  }
}
