package yang.app.qt.black;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class createJRE {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//通过jdeps命令得到的所有已分析jar文件的Dot文本文件的存放目录
		File dotDir = new File("d:\\hello");
		//模块来源路径，一般是jdk目录中的jmods文件夹，路径中包含空格要加引号
		String modulePath = "\"C:\\Program Files\\Java\\jdk-9\\jmods\"";
		//产生的jre存放路径
		String outPath = "d:\\jre";
		
		ArrayList<String> moduleNames = new ArrayList<>();
		File[] lf = dotDir.listFiles();
		StringBuffer sb = new StringBuffer();
		for(File f:lf) {
			sb.append(io.readTextFile(f, "gbk"));
		}
		List<String> allLine = cheakDocument.getAllLine(sb.toString());
		for(String s:allLine) {
			TextRegion moduleName = cheakDocument.subString(s,"(", ")");
			if(moduleName != null) {
				if(!moduleName.text.equals("找不到") && moduleName.text.lastIndexOf(".jar")==-1)
				moduleNames.add(moduleName.text);
			}
		}
		//对得到的模块名称去重
		ArrayList<String> withOutSame = cheakDocument.withOutSame(moduleNames);
		StringBuffer cmd = new StringBuffer("jlink"+" --output "+outPath+" --module-path "+modulePath+" --add-modules ");
		for(String s:withOutSame) {
			System.out.println(s);
			cmd.append(s+",");
		}
		//追加一个字符集模块
		cmd.append("jdk.charsets");
		
		System.out.println(cmd);
		
		try {
			Process exec = Runtime.getRuntime().exec(cmd.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
