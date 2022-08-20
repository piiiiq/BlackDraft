package yang.app.qt.black;

import java.io.File;
import java.util.List;
import java.util.Random;
import yang.demo.allPurpose.isYourNeedFile;

public class nameCreator
{
  File dir;
  black b;
  List<String> italiana;
  List<String> italiana_m;
  List<String> italiana_f;
  List<String> english;
  List<String> english_m;
  List<String> english_f;
  List<String> chinese_all;
  
  public nameCreator(black b, File dir)
  {
    this.dir = dir;
    this.b = b;
    if (!dir.exists()) {
      dir.mkdir();
    }
    getallfiles();
  }
  
  public void getallfiles()
  {
    isYourNeedFile needfiles = new isYourNeedFile(this.dir, ".*.black");
    String[] files = needfiles.Filter();
    for (int i = 0; i < files.length; i++)
    {
      String s = files[i];
      if (s.equals("i.black"))
      {
        File first = new File(this.dir.getAbsolutePath() + File.separator + s);
        this.italiana = cheakDocument.getAllLine(io.readBlackFileByLine(first));
      }
      else if (s.equals("if.black"))
      {
        File first = new File(this.dir.getAbsolutePath() + File.separator + s);
        this.italiana_f = cheakDocument.getAllLine(io.readBlackFileByLine(first));
      }
      else if (s.equals("im.black"))
      {
        File first = new File(this.dir.getAbsolutePath() + File.separator + s);
        this.italiana_m = cheakDocument.getAllLine(io.readBlackFileByLine(first));
      }
      else if (s.equals("e.black"))
      {
        File first = new File(this.dir.getAbsolutePath() + File.separator + s);
        this.english = cheakDocument.getAllLine(io.readBlackFileByLine(first));
      }
      else if (s.equals("ef.black"))
      {
        File first = new File(this.dir.getAbsolutePath() + File.separator + s);
        this.english_f = cheakDocument.getAllLine(io.readBlackFileByLine(first));
      }
      else if (s.equals("em.black"))
      {
        File first = new File(this.dir.getAbsolutePath() + File.separator + s);
        this.english_m = cheakDocument.getAllLine(io.readBlackFileByLine(first));
      }
      else if (s.equals("call.black"))
      {
        File first = new File(this.dir.getAbsolutePath() + File.separator + s);
        this.chinese_all = cheakDocument.getAllLine(io.readBlackFileByLine(first));
      }
    }
  }
  
  public boolean isHasItalinaNameData()
  {
    if ((this.italiana != null) && (this.italiana_f != null) && (this.italiana_m != null)) {
      return true;
    }
    return false;
  }
  
  public String[] getItalinaName(char gender)
  {
    Random ran = new Random();
    if (gender == 'f')
    {
      int first = ran.nextInt(this.italiana.size());
      int last = ran.nextInt(this.italiana_f.size());
      return new String[] { (String)this.italiana.get(first), (String)this.italiana_f.get(last) };
    }
    int first = ran.nextInt(this.italiana.size());
    int last = ran.nextInt(this.italiana_m.size());
    return new String[] { (String)this.italiana.get(first), (String)this.italiana_m.get(last) };
  }
  
  public boolean isHasEnglishNameData()
  {
    if ((this.english != null) && (this.english_f != null) && (this.english_m != null)) {
      return true;
    }
    return false;
  }
  
  public String[] getEnglishName(char gender)
  {
    Random ran = new Random();
    if (gender == 'f')
    {
      int first = ran.nextInt(this.english.size());
      int last = ran.nextInt(this.english_f.size());
      return new String[] { (String)this.english.get(first), (String)this.english_f.get(last) };
    }
    int first = ran.nextInt(this.english.size());
    int last = ran.nextInt(this.english_m.size());
    return new String[] { (String)this.english.get(first), (String)this.english_m.get(last) };
  }
  
  public boolean isHasChineseNameData()
  {
    if (this.chinese_all != null) {
      return true;
    }
    return false;
  }
  
  public String[] getChineseNames(int count)
  {
    Random ran = new Random();
    String[] names = new String[count];
    String name = null;
    for (int i = 0; i < count; i++)
    {
      do
      {
        int all = ran.nextInt(this.chinese_all.size());
        name = (String)this.chinese_all.get(all);
      } while (ishas(name, names));
      names[i] = name;
    }
    return names;
  }
  
  public boolean ishas(String name, String[] names)
  {
    boolean ishas = false;
    for (int a = 0; a < names.length; a++) {
      if (name.equals(names[a]))
      {
        ishas = true;
        break;
      }
    }
    return ishas;
  }
}
