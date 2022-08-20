package yang.demo.allPurpose;

import java.io.Serializable;

public class TextRegion
  implements Serializable
{
  public int start;
  public int end;
  public String text;
  public String name;
  public String describe;
  static final long serialVersionUID = 42L;
  
  public TextRegion(String text, int start, int end)
  {
    this.text = text;
    this.start = start;
    this.end = end;
  }
}
