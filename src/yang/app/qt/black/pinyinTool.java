package yang.app.qt.black;

import java.io.File;
import java.util.List;

import yang.demo.allPurpose.isYourNeedFile;

public class pinyinTool {
	 File dir;
	 List<String> doublePinyin;
	 List<String> pinyinLib;
	 char fuZhuMa; //双拼辅助按键，微软双拼是o键
	  public pinyinTool(File dir)
	  {
	    this.dir = dir;
	    if (!dir.exists()) {
	      dir.mkdir();
	    }
	    getallfiles();
	  }
	  void getallfiles() {
	        File doublePinyinFile = new File(this.dir.getAbsolutePath() + File.separator + "doublePinyin.txt");
	       doublePinyin = cheakDocument.getAllLine(io.readTextFile(doublePinyinFile, "gb2312"));
	       File pinyinLibFile = new File(this.dir.getAbsolutePath() + File.separator + "pinyinLib.txt");
	       pinyinLib = cheakDocument.getAllLine(io.readTextFile(pinyinLibFile, "gb2312"));
	      fuZhuMa = doublePinyin.get(0).charAt(0);
	  }
	  boolean isPinyin(String str) {
		  for(String s:pinyinLib) {
			  if(s.equals(str)) return true;
		  }
		  return false;
	  }
	  String getShengMu(char key) {
		  String sm = "";
		  for(int i=1;i<doublePinyin.size();i++) {
			  String s = doublePinyin.get(i);
			  char charAt = s.charAt(0);
			  if(charAt == key) {
				  TextRegion shengmu = cheakDocument.subString(s, "S", "E");
				  if(shengmu != null)
				  sm = shengmu.text;
				  break;
			  }
		  }
		  if(sm.isEmpty() && key != fuZhuMa) sm = key+"";
		  return sm;
	  }
	 
	  String[] getYunMu(char key) {
		  String yunmu1 = null,yunmu2 = null;
		  for(int i=1;i<doublePinyin.size();i++) {
			  String s = doublePinyin.get(i);
			  char charAt = s.charAt(0);
			  if(charAt == key) {
				  TextRegion yunmu = cheakDocument.subString(s, "Y", "E");
				  if(yunmu != null)
					  yunmu1 = yunmu.text;
				  TextRegion yunmu_ = cheakDocument.subString(s, "A", "E");
				  if(yunmu_ != null)
					  yunmu2 = yunmu_.text;
				  break;
			  }
		  }
		  String[] s = null;
		  if(yunmu1!= null && yunmu2!= null)s = new String[] {yunmu1,yunmu2};
		  else if(yunmu1 != null)s = new String[] {yunmu1};
		  return s; 
	  }
	  String isPiPie(String[] allyunmu,String shengmu) {
		  String pipie = null;
		  if(allyunmu != null)
		  for(String s:pinyinLib) {
			  if(allyunmu.length > 1) {
				  String pinyin1 = shengmu+allyunmu[0];
				  if(s.equals(pinyin1)) {
					pipie = allyunmu[0];
					break;
				  }
				  String pinyin2 = shengmu+allyunmu[1]; 
				  if(s.equals(pinyin2)) {
					pipie = allyunmu[1];
					break;
				  }
			  }else {
				  String pinyin = shengmu+allyunmu[0];
				  if(s.equals(pinyin)) {
					  pipie = allyunmu[0];
					  break;
				  }
			  }
		  }
		  return pipie;
	  }
	  public String toFullPinyin(String doublePinyin,String spilChar){
		  if(doublePinyin.isEmpty())return "";
			int a = 1; //0为声母，1为韵母
			String lastChar = null;
			String quanPin = "";
			for(int i=0;i<doublePinyin.length();i++){
				char c = doublePinyin.charAt(i);
				if(a==0){ //上一个字符为声母
					String[] allYunMu = getYunMu(c);//获取所有韵母
					String isPiPie = isPiPie(allYunMu,lastChar);//在韵母列表里检索，并返回与给定的声母相匹配的韵母
//					System.out.println("test: "+isPiPie);
					if(isPiPie != null){
						quanPin = quanPin+isPiPie+spilChar;
						a = 1;	
						lastChar = isPiPie;
					}else{
						String shengMu = getShengMu(c);
						quanPin = quanPin+spilChar+shengMu;
						a = 0;
						lastChar = shengMu;
					}
					
				}else{//上一个字符为韵母，或当前字符就是第一个字符
					String shengMu = getShengMu(c);
					quanPin = quanPin+shengMu;
					a = 0;
					lastChar = shengMu;
				}
			}
			if(quanPin.isEmpty())return "";
			if(quanPin.lastIndexOf(spilChar) == quanPin.length()-spilChar.length()) {
				return quanPin.substring(0, quanPin.length()-spilChar.length());
			}else return quanPin;
		}
	  public static void main(String args[]) {
		  pinyinTool pinyinTool = new pinyinTool(new File("./PinyinTool"));
//		  String shengMu = pinyinTool.getShengMu('v');
//		  String[] yunMu = pinyinTool.getYunMu('f');
//		  String yun = null;
//		  if(yunMu != null) {
//			  if(yunMu.length > 1) {
//				  yun = yunMu[0]+" "+yunMu[1];
//			  }else {
//				  yun = yunMu[0];
//			  }
//		  }
//		  System.out.println("声母："+shengMu+"\n韵母："+yun);

		  String dp = "oax;";
		  System.out.println(pinyinTool.toFullPinyin(dp,"'"));
		  
//		  String[] s = new String[]{"en"};
//		  System.out.println(pinyinTool.isPiPie(s, "zh"));
	  }
	  
}
