package yang.demo.allPurpose;

import java.io.File;
import java.io.Serializable;

public class isYourNeedFile
  implements Serializable
{
  static final long serialVersionUID = 42L;
  private String Filter_rule = ".*.jar";
  private File youNeedFile;
  
  public isYourNeedFile(File dirFile, String filter_rule)
  {
    this.youNeedFile = dirFile;
    this.Filter_rule = filter_rule;
  }
  
  public File getYouNeedFiles()
  {
    return this.youNeedFile;
  }
  
  public String[] Filter()
  {
    String[] s = this.youNeedFile.list(new MyFileFilter(this.Filter_rule));
    return s;
  }
}
