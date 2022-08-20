package yang.app.qt.black;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import yang.demo.allPurpose.DesUtils;

public class io {
	public static boolean writeBlackFile(File file, String text, Properties styles) {
		try {
			if (text.indexOf("$bc 加密") == 0) {
				text = black.des_doc.encrypt(text,"utf-8");
				black.p("加密保存文档"+file.getName());
			}
//			System.out.println(text);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		}
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		out.setLevel(9);
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(out, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		BufferedWriter bw = new BufferedWriter(osw);
		try {
			ZipEntry word = new ZipEntry("black_word");
			out.putNextEntry(word);
			bw.write(text);
			bw.flush();
			if (styles != null) {
				ZipEntry styles_entry = new ZipEntry("styles");
				out.putNextEntry(styles_entry);
				styles.storeToXML(out, null);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String readBlackFileByLine(File file) {
		ArrayList<String> lines = new ArrayList<>();
		ZipInputStream in = null;
		String fileName = null;
		String doc = null;
		try {
			in = new ZipInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fileName = in.getNextEntry().getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(isr);
		try {
			if (fileName.equals("black_word")) {
				String str;
				while ((str = br.readLine()) != null) {
					lines.add(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(lines.size() == 0) return "";
		boolean noPass = false;
		try {
			if(lines.size() == 1) {
				String dec = black.des_doc.decrypt(lines.get(0),"utf-8");
				black.p("解密读取文档"+file.getName());
				return dec;
			}else noPass = true;
		} catch (Exception e) {
			noPass = true;
		}
		if(noPass) {
			StringBuilder strBuilder = new StringBuilder();
			
			for(String s:lines) {
				strBuilder.append(s+"\n");
			}
			String string = strBuilder.toString();
			return string;
		}
		return "-1";
	}
	/**
	 * 读取black文件，会自动判断文档是否被加密
	 * @param file
	 * @param passwd 解密文档所用的密钥
	 * @return 文档已被加密但无法使用给定的密钥解密时返回一个包含-1的字符串，其他情况均返回一个包含文档内容的字符串
	 */
	public static String readBlackFileByLine(File file,String passwd) {
		ArrayList<String> lines = new ArrayList<>();
		ZipInputStream in = null;
		String fileName = null;
		String doc = null;
		try {
			in = new ZipInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fileName = in.getNextEntry().getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(isr);
		try {
			if (fileName.equals("black_word")) {
				String str;
				while ((str = br.readLine()) != null) {
					lines.add(str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(lines.size() == 0) return "";
		
		try {
//			System.out.println(MD5.getMD5Checksum(file.getAbsolutePath()));
			DesUtils des = new DesUtils(passwd);
			String dec = des.decrypt(lines.get(0),"utf-8");
			return dec;
		} catch (Exception e) {
			String simpleName = e.getClass().getSimpleName();
			if(simpleName.equals("BadPaddingException")) {
				return "-1";
			}
			StringBuilder strBuilder = new StringBuilder();
			
			for(String s:lines) {
				strBuilder.append(s+"\n");
			}
			String string = strBuilder.toString();
			return string;
		}
	}

	public static String readBlackFileByByte(File file) {
		StringBuilder strBuilder = new StringBuilder();
		ZipInputStream in = null;
		String fileName = null;
		try {
			in = new ZipInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fileName = in.getNextEntry().getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] b = new byte[(int) file.length()];
		if (fileName.equals("black_word")) {
			BufferedInputStream bis = new BufferedInputStream(in);
			try {
				int i = 0;
				while ((i = bis.read(b)) != -1) {
					strBuilder.append(new String(b, 0, i, "utf-8"));
				}
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String string = strBuilder.toString();
		return string;
	}

	public static Object readObjFile(File file) {
		FileInputStream fis = null;
		Object oo = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				oo = ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
//			e.printStackTrace();
			black.p("读取文件出错："+file.getAbsolutePath().toString());
			return null;
		}
		try {
			fis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return oo;
	}

	public static void writeObjFile(File file, Object o) {
		if (o != null) {
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(o);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean writeTextFile(File file, String text, String fileencode) {
		OutputStreamWriter osr = null;
		try {
			osr = new OutputStreamWriter(new FileOutputStream(file), fileencode);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			BufferedWriter out = new BufferedWriter(osr);
			String newtext = text.replaceAll("\n", System.getProperty("line.separator", "\n"));
			out.write(newtext);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static String readTextFile(File file, String fileencode) {
		StringBuilder strBuilder = new StringBuilder();

		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(new FileInputStream(file), fileencode);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			BufferedReader in = new BufferedReader(isr, 10000);
			String str;
			while ((str = in.readLine()) != null) {
				strBuilder.append(str + "\n");
			}
			in.close();
		} catch (IOException localIOException) {
		}
		return strBuilder.toString();
	}
}
