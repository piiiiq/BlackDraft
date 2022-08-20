package yang.demo.allPurpose;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class fileTool
{
	  /**
	   * 判断两个文件的内容是否相同，文件名要用绝对路径
	   * @param fileName1 ：文件1的绝对路径
	   * @param fileName2 ：文件2的绝对路径
	   * @return 相同返回true，不相同返回false
	   */
	public static boolean isSameFile(String fileName1,String fileName2){
		FileInputStream fis1 = null;
		FileInputStream fis2 = null;
		try {
			fis1 = new FileInputStream(fileName1);
			fis2 = new FileInputStream(fileName2);
			
			int len1 = fis1.available();//返回总的字节数
			int len2 = fis2.available();
			
			if (len1 == len2) {//长度相同，则比较具体内容
				//建立两个字节缓冲区
				byte[] data1 = new byte[len1];
				byte[] data2 = new byte[len2];
				
				//分别将两个文件的内容读入缓冲区
				fis1.read(data1);
				fis2.read(data2);
				
				//依次比较文件中的每一个字节
				for (int i=0; i<len1; i++) {
					//只要有一个字节不同，两个文件就不一样
					if (data1[i] != data2[i]) {
//						System.out.println("文件内容不一样");
						return false;
					}
				}
//				System.out.println("两个文件完全相同");
				return true;
			} else {
				//长度不一样，文件肯定不同
				return false;
			}
		} catch (FileNotFoundException e) {
//			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
		} finally {//关闭文件流，防止内存泄漏
			if (fis1 != null) {
				try {
					fis1.close();
				} catch (IOException e) {
					//忽略
//					e.printStackTrace();
				}
			}
			if (fis2 != null) {
				try {
					fis2.close();
				} catch (IOException e) {
					//忽略
//					e.printStackTrace();
				}
			}
		}
		return false;
	}
	public static boolean copy(String source, String dest,ArrayList<File> al) {
		File file = new File(source);
		File parentfile = file.getParentFile();
		if ((file.exists()) && (file.isDirectory())) {
			Object[] files = listFiles(source).toArray();
			Object[] arrayOfObject1;
			int j = (arrayOfObject1 = files).length;
			for (int i = 0; i < j; i++) {
				Object f = arrayOfObject1[i];
				try {
					File fi = (File) f;
					String path = fi.getCanonicalPath().replace(parentfile.getCanonicalPath(), "");
					File destfile = new File(dest + path);
					if (fi.isDirectory()) {
						destfile.mkdirs();
					} else {
						boolean write = true;
						if (destfile.exists()) {
							if(!isSameFile(destfile.getAbsolutePath(), fi.getAbsolutePath())) {
								destfile.delete();
								destfile.createNewFile();
								al.add(destfile);
							}else {
								write = false;
							}
						}else {
							if (!destfile.getParentFile().exists()) {
								destfile.getParentFile().mkdirs();
								destfile.createNewFile();
								al.add(destfile);
							} else {
								destfile.createNewFile();
								al.add(destfile);
							}
						}
						
						if(write) {
							FileOutputStream fos = new FileOutputStream(destfile);
							BufferedOutputStream bos = new BufferedOutputStream(fos);

							FileInputStream fis = new FileInputStream(fi);
							BufferedInputStream bis = new BufferedInputStream(fis);

							int a = 0;
							while ((a = bis.read()) != -1) {
								bos.write(a);
							}
							bos.flush();
							bis.close();
							bos.close();
						}
					}
					
				} catch (IOException e) {
//					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
  
  public static boolean deleteDir(String dir)
  {
    ArrayList<File> al = listFiles(dir);
    for (File f : al) {
      f.delete();
    }
    File dirfile = new File(dir);
    return dirfile.delete();
  }
  
  public static ArrayList<File> listFiles(String dir)
  {
    File dirfile = new File(dir);
    ArrayList<File> al = new ArrayList();
    if (dirfile.exists())
    {
      File[] files = dirfile.listFiles();
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = files).length;
      for (int i = 0; i < j; i++)
      {
        File f = arrayOfFile1[i];
        if (f.isDirectory()) {
          try
          {
            ArrayList<File> al_ = listFiles(f.getCanonicalPath());
            Iterator<File> it = al_.iterator();
            while (it.hasNext()) {
              al.add((File)it.next());
            }
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
        al.add(f);
      }
    }
    else
    {
      try
      {
        throw new IOException();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return al;
  }
}
