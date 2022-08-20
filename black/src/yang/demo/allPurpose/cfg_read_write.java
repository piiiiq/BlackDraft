package yang.demo.allPurpose;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class cfg_read_write
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private FileOutputStream fos;
  private FileInputStream fis;
  private String CFGFile_dir = "./CFG/";
  private File cfg_file;
  private File cfg_dir;
  
  public cfg_read_write(String CFG_fileName)
  {
    this.cfg_file = new File(this.CFGFile_dir + CFG_fileName);
    this.cfg_dir = new File(this.CFGFile_dir);
  }
  
  public void cfg_write(Properties properties)
  {
    try
    {
      properties.storeToXML(new FileOutputStream(this.cfg_file), "CFG File");
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public void cfg_read(Properties properties)
  {
    if (!this.cfg_dir.exists())
    {
      this.cfg_dir.mkdir();
      try
      {
        this.cfg_file.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      try
      {
        if (!this.cfg_file.createNewFile()) {
          try
          {
            properties.loadFromXML(new FileInputStream(this.cfg_file));
          }
          catch (IOException localIOException1) {}
        }
        return;
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public static Properties cfg_read(File f)
  {
    Properties pro = new Properties();
    try
    {
      pro.loadFromXML(new FileInputStream(f));
    }
    catch (InvalidPropertiesFormatException e)
    {
      e.printStackTrace();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return pro;
  }
  
  public static void cfg_write(Properties pro, File f)
  {
    try
    {
      if (!f.exists()) {
        f.createNewFile();
      }
      pro.storeToXML(new FileOutputStream(f), null);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
