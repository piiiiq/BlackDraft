package yang.demo.allPurpose;

public class WebFormatter
{
  public static String html2text(String html)
  {
    StringBuffer sb = new StringBuffer(html.length());
    char[] data = html.toCharArray();
    int start = 0;
    boolean previousIsPre = false;
    Token token = null;
    for (;;)
    {
      token = parse(data, start, previousIsPre);
      if (token == null) {
        break;
      }
      previousIsPre = token.isPreTag();
      sb = sb.append(token.getText());
      start += token.getLength();
    }
    return sb.toString();
  }
  
  private static Token parse(char[] data, int start, boolean previousIsPre)
  {
    if (start >= data.length) {
      return null;
    }
    char c = data[start];
    if (c == '<')
    {
      int end_index = indexOf(data, start + 1, '>');
      if (end_index == -1) {
        return new Token(0, data, start, data.length, previousIsPre);
      }
      String s = new String(data, start, end_index - start + 1);
      if (s.startsWith("<!--"))
      {
        int end_comment_index = indexOf(data, start + 1, "-->");
        if (end_comment_index == -1) {
          return new Token(1, data, start, data.length, previousIsPre);
        }
        return new Token(1, data, start, end_comment_index + 3, previousIsPre);
      }
      String s_lowerCase = s.toLowerCase();
      if (s_lowerCase.startsWith("<script"))
      {
        int end_script_index = indexOf(data, start + 1, "</script>");
        if (end_script_index == -1) {
          return new Token(3, data, start, data.length, previousIsPre);
        }
        return new Token(3, data, start, end_script_index + 9, previousIsPre);
      }
      return new Token(2, data, start, start + s.length(), previousIsPre);
    }
    int next_tag_index = indexOf(data, start + 1, '<');
    if (next_tag_index == -1) {
      return new Token(0, data, start, data.length, previousIsPre);
    }
    return new Token(0, data, start, next_tag_index, previousIsPre);
  }
  
  private static int indexOf(char[] data, int start, String s)
  {
    char[] ss = s.toCharArray();
    for (int i = start; i < data.length - ss.length; i++)
    {
      boolean match = true;
      for (int j = 0; j < ss.length; j++) {
        if (data[(i + j)] != ss[j])
        {
          match = false;
          break;
        }
      }
      if (match) {
        return i;
      }
    }
    return -1;
  }
  
  private static int indexOf(char[] data, int start, char c)
  {
    for (int i = start; i < data.length; i++) {
      if (data[i] == c) {
        return i;
      }
    }
    return -1;
  }
}
