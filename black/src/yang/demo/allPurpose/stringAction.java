package yang.demo.allPurpose;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

public class stringAction
  implements Serializable
{
  static final long serialVersionUID = 42L;
  
  public static String spiltFileName(String filename, String fileextensionsname)
  {
    int in = filename.lastIndexOf(fileextensionsname);
    return filename.substring(0, in);
  }
  
  public static String[] getGBcode(char c)
  {
    Charset gb = (Charset)Charset.availableCharsets().subMap("gb18030", "gb2312").values().iterator().next();
    ByteBuffer bb = gb.encode(CharBuffer.wrap(new char[] { c }));
    byte[] b = bb.array();
    String newb0 = Integer.toString(b[0] + 256, 16);
    String newb1 = Integer.toString(b[1] + 256, 16);
    return new String[] { newb0, newb1 };
  }
  
  public static List<String> getGBcode(String str)
  {
    List<String> lis = new ArrayList();
    Charset gb = (Charset)Charset.availableCharsets().subMap("gb18030", "gb2312").values().iterator().next();
    ByteBuffer bb = gb.encode(CharBuffer.wrap(str));
    byte[] b = bb.array();
    String[] newb_array = new String[b.length / 2];
    for (int i = 0; i < b.length; i++) {
      lis.add(Integer.toString(b[i] + 256, 16));
    }
    return lis;
  }
}
