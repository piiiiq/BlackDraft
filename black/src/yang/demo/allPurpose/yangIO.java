package yang.demo.allPurpose;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class yangIO
  implements Serializable
{
  static final long serialVersionUID = 42L;
  FileReader fr;
  FileWriter fw;
  BufferedReader br;
  BufferedWriter bw;
  
  public String readLineFromFile(File file)
  {
    try
    {
      if (this.fr == null) {
        this.fr = new FileReader(file);
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    if (this.br == null) {
      this.br = new BufferedReader(this.fr);
    }
    try
    {
      String str;
      if ((str = this.br.readLine()) != null) {
        return str;
      }
      this.br.close();
      return null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public int writeStringToFile(File file, String str)
  {
    try
    {
      if (this.fw == null) {
        this.fw = new FileWriter(file);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    if (this.bw == null) {
      this.bw = new BufferedWriter(this.fw);
    }
    try
    {
      this.bw.write(str);
      this.bw.close();
      return 1;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return 0;
  }
}
