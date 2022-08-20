package yang.app.qt.black;

import java.io.Serializable;

public class TextRegion
  implements Serializable
{
  int start;
  int end;
  String text;
  String showname;
  String filename;
  String describe;
  Object data;
  static final long serialVersionUID = 42L;
  
  public TextRegion(String text, int start, int end)
  {
    this.text = text;
    this.start = start;
    this.end = end;
  }
  public void setData(Object data) {
	  this.data = data;
  }
  Object Data() {
	  return data;
  }
}
