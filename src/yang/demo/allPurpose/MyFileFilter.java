package yang.demo.allPurpose;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyFileFilter
  implements FilenameFilter
{
  private Pattern p;
  
  public MyFileFilter(String name)
  {
    this.p = Pattern.compile(name);
  }
  
  public boolean accept(File file, String string)
  {
    return this.p.matcher(string).matches();
  }
}
