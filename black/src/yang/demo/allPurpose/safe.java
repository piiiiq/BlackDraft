package yang.demo.allPurpose;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class safe
  implements Serializable
{
  Properties pro;
  static final int tryDays = 60;
  static final String dirname = "BradypusPygmaeus";
  static final String filename = "info";
  public static int last = 1;
  public static int first = 0;
  static final long serialVersionUID = 42L;
  
  public long tryDay(int day)
  {
    long m = 86400000L * day;
    return m;
  }
  
  public boolean isSafe()
  {
    if (readSafeFile(first) == 0L) {
      writeSafeFile(null, first);
    }
    boolean issafe;
    if ((readSafeFile(first) > 0L) && 
      (System.currentTimeMillis() - readSafeFile(first) < tryDay(60)) && 
      (readSafeFile(last) == 0L))
    {
      issafe = true;
    }
    else
    {
      writeSafeFile(getSafeFile(), last);
      issafe = false;
    }
    return issafe;
  }
  
  public int getTryedDays()
  {
    long milofdays = readSafeFile(first) + tryDay(60) - System.currentTimeMillis();
    if (milofdays > 0L) {
      return time.getDays(milofdays);
    }
    return 0;
  }
  
  public void writeSafeFile(Properties properties, int value)
  {
    if (properties != null) {
      this.pro = properties;
    } else {
      this.pro = new Properties();
    }
    String userdir = System.getProperty("user.home");
    if (value == first) {
      this.pro.setProperty("first", String.valueOf(System.currentTimeMillis()));
    } else if (value == last) {
      this.pro.setProperty("last", String.valueOf(System.currentTimeMillis()));
    }
    if (userdir != null)
    {
      File appdata = new File(userdir + "\\" + "AppData\\Local");
      if (appdata.exists())
      {
        File f = new File(appdata + "\\" + "BradypusPygmaeus");
        if ((!f.exists()) && 
          (!f.mkdirs())) {
          System.exit(0);
        }
        try
        {
          FileOutputStream fos = new FileOutputStream(new File(f + "\\" + "info"));
          try
          {
            this.pro.storeToXML(fos, null);
            fos.flush();
            fos.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
          return;
        }
        catch (FileNotFoundException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
  
  public long readSafeFile(int value)
  {
//    this.pro = new Properties();
//    String userdir = System.getProperty("user.home");
//    File blackfile = new File(userdir + "\\" + "AppData\\Local\\" + "BradypusPygmaeus" + "\\" + "info");
//    String date = null;
//    if (blackfile.exists()) {
//      try
//      {
//        FileInputStream fis = new FileInputStream(blackfile);
//        try
//        {
//          this.pro.loadFromXML(fis);
//          if (value == first) {
//            date = this.pro.getProperty("first");
//          } else {
//            date = this.pro.getProperty("last");
//          }
//        }
//        catch (InvalidPropertiesFormatException e)
//        {
//          e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//          e.printStackTrace();
//        }
//        if (date == null) {
//          break label169;
//        }
//      }
//      catch (FileNotFoundException e)
//      {
//        e.printStackTrace();
//      }
//    }
//    return Long.valueOf(date).longValue();
//    label169:
    return 0L;
  }
  
  public Properties getSafeFile()
  {
    return this.pro;
  }
}
