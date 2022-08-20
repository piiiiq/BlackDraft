package yang.demo.allPurpose;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class io
  implements Serializable
{
  static final long serialVersionUID = 42L;
  
  public static void copyFile(File infile, File outFile)
  {
    FileInputStream fis = null;
    BufferedOutputStream bos = null;
    try
    {
      fis = new FileInputStream(infile);
    }
    catch (FileNotFoundException e2)
    {
      e2.printStackTrace();
    }
    BufferedInputStream bis = new BufferedInputStream(fis);
    try
    {
      FileOutputStream fos = new FileOutputStream(outFile);
      bos = new BufferedOutputStream(fos);
    }
    catch (FileNotFoundException e1)
    {
      e1.printStackTrace();
    }
    byte[] a = new byte[100];
    try
    {
      while (bis.read(a) != -1) {
        bos.write(a);
      }
      bos.flush();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
