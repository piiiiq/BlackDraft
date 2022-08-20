package yang.demo.allPurpose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class cheakDocument
  implements Serializable
{
  static final long serialVersionUID = 42L;
  
  public List<TextRegion> splitString(String str, char c, boolean onlyChinese, String subname)
  {
    List<TextRegion> lis = new ArrayList();
    int startindex = -1;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == c)
      {
        if (startindex == -1)
        {
          startindex = i;
        }
        else if (i - startindex > 1)
        {
          TextRegion tr = null;
          if (onlyChinese)
          {
            if (wordCountStat.isAsiaString(str.substring(startindex + 1, i)))
            {
              tr = new TextRegion(
                str.substring(startindex + 1, i), 
                startindex + 1, i);
              if (subname != null) {
                tr.name = subname;
              }
            }
          }
          else
          {
            tr = new TextRegion(str.substring(startindex + 1, i), 
              startindex + 1, i);
            if (subname != null) {
              tr.name = subname;
            }
          }
          if (tr != null) {
            lis.add(tr);
          }
          startindex = -1;
        }
        else
        {
          startindex = i;
        }
      }
      else if ((str.charAt(i) == '\n') || (str.charAt(i) == '\r') || (str.charAt(i) == '\t')) {
        startindex = -1;
      }
    }
    return lis;
  }
  
  public static TextRegion subString(String str, int start, String str1)
  {
    int index = str.indexOf(str1, start);
    if (index != -1) {
      return new TextRegion(str.substring(start, index), start, index);
    }
    return null;
  }
  
  public static TextRegion subString(String str, String str1, String str2)
  {
    int index = str.indexOf(str1);
    if (index != -1)
    {
      int index_str2 = str.indexOf(str2, index + str1.length());
      if (index_str2 != -1) {
        return new TextRegion(str.substring(index + str1.length(), index_str2), index + str1.length(), index_str2);
      }
    }
    return null;
  }
  
  public static boolean findString(String str1, String str2)
  {
    if (str1.indexOf(str2) != -1) {
      return true;
    }
    return false;
  }
  
  public static TextRegion checkCommand(String str, String str1)
  {
    int index = str.indexOf(str1);
    if (index != -1)
    {
      String s = str.substring(index + str1.length(), str.length());
      return new TextRegion(s, index, index + str1.length() + s.length());
    }
    return null;
  }
  
  public static TextRegion find(String str, String str1)
  {
    int index = str.indexOf(str1);
    if (index != -1) {
      return new TextRegion(str1, index, index + str1.length());
    }
    return null;
  }
  
  public static List<TextRegion> searchByLine(String str, String str1, String subname)
  {
    int index = 0;
    List<TextRegion> lis = new ArrayList();
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '\n')
      {
        String substr = str.substring(index, i);
        int index_first = substr.indexOf(str1);
        if (index_first != -1)
        {
          TextRegion tr = new TextRegion(substr, index_first + index, str1.length());
          tr.name = subname;
          lis.add(tr);
        }
        index = i + 1;
      }
      else if (i == str.length() - 1)
      {
        String substr = str.substring(index, str.length());
        int index_first = substr.indexOf(str1);
        if (index_first != -1)
        {
          TextRegion tr = new TextRegion(substr, index_first + index, index_first + index + str1.length());
          tr.name = subname;
          lis.add(tr);
        }
      }
    }
    return lis;
  }
  
  public static boolean cheakString(String str)
  {
    if ((str.length() > 0) && 
      (!cheak(str.charAt(str.length() - 1)))) {
      return true;
    }
    return false;
  }
  
  public static boolean cheak(char c)
  {
    String[] str = { "，", "。", "、", "……", "…", "——", "—", "？", "?", "！", ",", ".", "?", "!", "-", "<", ">", "~", "·", "《", "》", "”", "“", "\"", "(", ")", "（", "）", 
      ":", "：", "[", "]", "{", "}", "『", "』", "【", "】", "@", "—", "——", "/" };
    for (int i = 0; i < str.length; i++) {
      if (c == str[i].charAt(0)) {
        return true;
      }
    }
    return false;
  }
  
  public static List<String> getAllLine(String str)
  {
    List<String> lis_allline = new ArrayList();
    int index = 0;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == '\n')
      {
        String substr = str.substring(index, i + 1);
        lis_allline.add(substr);
        index = i + 1;
      }
      else if (i == str.length() - 1)
      {
        String substr = str.substring(index, i);
        lis_allline.add(substr);
      }
    }
    return lis_allline;
  }
}
