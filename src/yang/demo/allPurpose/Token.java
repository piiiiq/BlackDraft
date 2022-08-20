package yang.demo.allPurpose;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

class Token
{
  public static final int TOKEN_TEXT = 0;
  public static final int TOKEN_COMMENT = 1;
  public static final int TOKEN_TAG = 2;
  public static final int TOKEN_SCRIPT = 3;
  private static final char[] TAG_BR = "<br".toCharArray();
  private static final char[] TAG_P = "<p".toCharArray();
  private static final char[] TAG_LI = "<li".toCharArray();
  private static final char[] TAG_PRE = "<pre".toCharArray();
  private static final char[] TAG_HR = "<hr".toCharArray();
  private static final char[] END_TAG_TD = "</td>".toCharArray();
  private static final char[] END_TAG_TR = "</tr>".toCharArray();
  private static final char[] END_TAG_LI = "</li>".toCharArray();
  private static final Map SPECIAL_CHARS = new HashMap();
  private int type;
  private String html;
  private String text = null;
  private int length = 0;
  private boolean isPre = false;
  
  static
  {
    SPECIAL_CHARS.put("\"", "\"");
    SPECIAL_CHARS.put("<", "<");
    SPECIAL_CHARS.put(">", ">");
    SPECIAL_CHARS.put("&", "&");
    SPECIAL_CHARS.put("®", "(r)");
    SPECIAL_CHARS.put("©", "(c)");
    SPECIAL_CHARS.put(" ", " ");
    SPECIAL_CHARS.put("£", "?");
  }
  
  public Token(int type, char[] data, int start, int end, boolean previousIsPre)
  {
    this.type = type;
    this.length = (end - start);
    this.html = new String(data, start, this.length);
    System.out.println("[Token] html=" + this.html + ".");
    parseText(previousIsPre);
    System.out.println("[Token] text=" + this.text + ".");
  }
  
  public int getLength()
  {
    return this.length;
  }
  
  public boolean isPreTag()
  {
    return this.isPre;
  }
  
  private void parseText(boolean previousIsPre)
  {
    if (this.type == 2)
    {
      char[] cs = this.html.toCharArray();
      if ((compareTag(TAG_BR, cs)) || (compareTag(TAG_P, cs))) {
        this.text = "\n";
      } else if (compareTag(TAG_LI, cs)) {
        this.text = "\n* ";
      } else if (compareTag(TAG_PRE, cs)) {
        this.isPre = true;
      } else if (compareTag(TAG_HR, cs)) {
        this.text = "\n--------\n";
      } else if (compareString(END_TAG_TD, cs)) {
        this.text = "\t";
      } else if ((compareString(END_TAG_TR, cs)) || (compareString(END_TAG_LI, cs))) {
        this.text = "\n";
      }
    }
    else if (this.type == 0)
    {
      this.text = toText(this.html, previousIsPre);
    }
  }
  
  public String getText()
  {
    return this.text == null ? "" : this.text;
  }
  
  private String toText(String html, boolean isPre)
  {
    char[] cs = html.toCharArray();
    StringBuffer buffer = new StringBuffer(cs.length);
    int start = 0;
    boolean continueSpace = false;
    while (start < cs.length)
    {
      char current = cs[start];
      char next;
      if (start + 1 < cs.length) {
        next = cs[(start + 1)];
      } else {
        next = '\000';
      }
      if (current == ' ')
      {
        if ((isPre) || (!continueSpace)) {
          buffer = buffer.append(' ');
        }
        continueSpace = true;
        
        start++;
      }
      else if ((current == '\r') && (next == '\n'))
      {
        if (isPre) {
          buffer = buffer.append('\n');
        }
        start += 2;
      }
      else if ((current == '\n') || (current == '\r'))
      {
        if (isPre) {
          buffer = buffer.append('\n');
        }
        start++;
      }
      else
      {
        continueSpace = false;
        if (current == '&')
        {
          int length = readUtil(cs, start, ';', 10);
          if (length == -1)
          {
            buffer = buffer.append('&');
            
            start++;
          }
          else
          {
            String spec = new String(cs, start, length);
            String specChar = (String)SPECIAL_CHARS.get(spec);
            if (specChar != null)
            {
              buffer = buffer.append(specChar);
              
              start += length;
            }
            else if (next == '#')
            {
              String num = new String(cs, start + 2, length - 3);
              try
              {
                int code = Integer.parseInt(num);
                if ((code > 0) && (code < 65536))
                {
                  buffer = buffer.append((char)code);
                  
                  start++;
                }
              }
              catch (Exception localException)
              {
                buffer = buffer.append("");
                
                start += 2;
              }
            }
            else
            {
              buffer = buffer.append('&');
              
              start++;
            }
          }
        }
        else
        {
          buffer = buffer.append(current);
          
          start++;
        }
      }
    }
    return buffer.toString();
  }
  
  private int readUtil(char[] cs, int start, char util, int maxLength)
  {
    int end = start + maxLength;
    if (end > cs.length) {
      end = cs.length;
    }
    for (int i = start; i < start + maxLength; i++) {
      if (cs[i] == util) {
        return i - start + 1;
      }
    }
    return -1;
  }
  
  private boolean compareTag(char[] ori_tag, char[] tag)
  {
    if (ori_tag.length >= tag.length) {
      return false;
    }
    for (int i = 0; i < ori_tag.length; i++) {
      if (Character.toLowerCase(tag[i]) != ori_tag[i]) {
        return false;
      }
    }
    if (tag.length > ori_tag.length)
    {
      char c = Character.toLowerCase(tag[ori_tag.length]);
      if ((c < 'a') || (c > 'z')) {
        return true;
      }
      return false;
    }
    return true;
  }
  
  private boolean compareString(char[] ori, char[] comp)
  {
    if (ori.length > comp.length) {
      return false;
    }
    for (int i = 0; i < ori.length; i++) {
      if (Character.toLowerCase(comp[i]) != ori[i]) {
        return false;
      }
    }
    return true;
  }
  
  public String toString()
  {
    return this.html;
  }
}
