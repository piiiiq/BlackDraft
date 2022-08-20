package yang.demo.allPurpose;

import java.io.Serializable;

public class wordCountStat
  implements Serializable
{
  static final long serialVersionUID = 42L;
  
  public static boolean isAsia(int c)
  {
    if ((c >= 19968) && (c <= 40869)) {
      return true;
    }
    if ((c >= 12288) && (c <= 12311)) {
      return true;
    }
    if ((c >= 65072) && (c <= 65374)) {
      return true;
    }
    return false;
  }
  
  public static boolean isAsiaString(String str)
  {
    boolean allischinese = true;
    for (int i = 0; i < str.length(); i++) {
      if (!isAsia(str.charAt(i)))
      {
        allischinese = false;
        break;
      }
    }
    return allischinese;
  }
}
