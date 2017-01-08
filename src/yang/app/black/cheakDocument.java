package yang.app.black;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IRegion;

public class cheakDocument implements Serializable{
	static final long serialVersionUID = 42L;

	// public cheakDocument(String doc){
	//
	// }
	/**
	 * 提取两个符号间的字符串
	 * 此方法会遍历给定的字符串，提取任意两个给定的符号间的子字符串
	 * @param str
	 * @param c 分割字符串的符号，例如“_中国_”中的‘_’
	 * @param onlyChinese
	 * @param sbuname 给定的字符串所属的文件名称，可以为null
	 * @return
	 */
	public List<TextRegion> splitString(String str, char c,boolean onlyChinese,String subname) {
		List<TextRegion> lis = new ArrayList<TextRegion>();
		int startindex = -1;
	
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				if (startindex == -1)
					startindex = i;
				else if (i - startindex > 1) {
					TextRegion tr = null;
					if (onlyChinese) {
						if (wordCountStat.isAsiaString(str.substring(startindex+1,i))) {
							tr = new TextRegion(
									str.substring(startindex + 1, i),
									startindex + 1, i);
							if(subname != null)
								tr.name = subname;
						}
					} else{
						tr = new TextRegion(str.substring(startindex + 1, i),
								startindex + 1, i);
						if(subname != null)
							tr.name = subname;
					}
					if (tr != null)
						lis.add(tr);
					startindex = -1;
				} else {
					startindex = i;
				}
			}else if(str.charAt(i) == '\n' || str.charAt(i) == '\r' || str.charAt(i) == '\t'){
				startindex = -1;
			}
		}
		return lis;
	}
	/**
	 * 将给定字符串截成两个字符串并返回，截取点为特定字符
	 * @param str
	 * @param br截取点字符
	 * @return
	 */
	public static String[] subString(String str, String br){
		int index = str.indexOf(br);
		if(index != -1){
			String sub1 = str.substring(0, index);
			String sub2 = str.substring(index+1,str.length());
			return new String[]{sub1,sub2};
		}else return null;
	}
	/**
	 * 截取给定字符串str中起始索引start和字符串str1之间的子字符串
	 * @param str
	 * @param start
	 * @param str
	 * @return
	 */
	public static TextRegion subString(String str, int start, String str1){
		int index = str.indexOf(str1, start);
		if(index != -1){
			return new TextRegion(str.substring(start, index), start, index);
		}
		return null;
	}
	/**
	 * 返回给定字符串str中的字符串str1和字符串str2之间的子字符串
	 * @param str
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static TextRegion subString(String str, String str1, String str2){
		int index = str.indexOf(str1);
		if(index != -1){
			int index_str2 = str.indexOf(str2, index+str1.length());
			if(index_str2 != -1){
				return new TextRegion(str.substring(index+str1.length(), index_str2), index+str1.length(), index_str2);
			}
		}
		return null;
	}
	public void splitString(String str){
		
	}
	public static boolean findString(String str1,String str2){
		if(str1.indexOf(str2) != -1){
			return true;
		}else return false;
	}
	/**
	 * 返回给定的字符串str里指定的字符串str1后面的字符串
	 * 如果str里不存在字符串str1则返回null
	 * @param str
	 * @param str1
	 * @return
	 */
	public static TextRegion checkCommand(String str,String str1){
		int index = str.indexOf(str1);
		if(index != -1){
			String s = str.substring(index+str1.length(), str.length());
			return new TextRegion(s, index, index+str1.length()+s.length());
		}else return null;
	}
	public static TextRegion find(String str,String str1){
		int index = str.indexOf(str1);
		if(index != -1){
			return new TextRegion(str1, index, index+str1.length());
		}else return null;
	}
	/**
	 * 在给定的字符串str中逐行查找字符串str1
	 * @param str
	 * @param str1
	 * @return
	 */
	public static List<TextRegion> searchByLine(String str, String str1,String subname){
		int index = 0;
		List<TextRegion> lis = new ArrayList<TextRegion>();
		
		for(int i=0; i< str.length(); i++){
			if(str.charAt(i) == '\n'){
				String substr = str.substring(index, i);
				int index_first = substr.indexOf(str1);
				if(index_first != -1){
					TextRegion tr = new TextRegion(substr, index_first+index, str1.length());
					tr.name = subname;
					lis.add(tr);
				}
				index = i+1;
			}else if(i == str.length()-1){
				String substr = str.substring(index, str.length());
				int index_first = substr.indexOf(str1);
				if(index_first != -1){
					TextRegion tr = new TextRegion(substr, index_first+index, index_first+index+str1.length());
					tr.name = subname;
					lis.add(tr);
				}
			}
		}
		return lis;
	}
	/**
	 * 检查字符串末尾是否缺失标点
	 * @param str
	 * @return 如果末尾缺失标点符号则返回true
	 */
	static boolean cheakString(String str){
		if(str.length()>0)					
			if(!cheak(str.charAt(str.length()-1)))
				return true;
		return false;
	}
	/**
	 * 判断给定的字符是否为标点符号
	 * @param c
	 * @return 如果给定的字符是标点符号返回true，否则返回false
	 */
	static boolean cheak(char c){
		String[] str = {
				"，",
				"。",
				"、",
				"……",
				"…",
				"——",
				"—",
				"？",
				"?",
				"！",
				",",
				".",
				"?",
				"!",
				"-",
				"<",
				">",
				"~",
				"·",
				"《",
				"》",
				"”",
				"“",
				"\"",
				"(",
				")",
				"（",
				"）",
				":",
				"：",
				"[",
				"]",
				"{",
				"}",
				"『",
				"』",
				"【",
				"】",
				"@",
				"—",
				"——",
				"/"};
		for(int i=0; i<str.length; i++){
			if(c == str[i].charAt(0)){
				return true;
			}
		}
		return false;
	}
public static List<String> getAllLine(String str){
	List<String> lis_allline = new ArrayList<String>();
	int index = 0;
	for(int i=0; i<str.length(); i++){
		if(str.charAt(i) == '\n'){
			String substr = str.substring(index, i);
			lis_allline.add(substr);
			index = i+1;
		}else if(i == str.length()-1){
			String substr = str.substring(index, i+1);
			lis_allline.add(substr);
		}
	}
	return lis_allline;
}

}

class TextRegion implements Serializable{
	int start, end;
	String text,name,describe;
	static final long serialVersionUID = 42L;

	public TextRegion(String text, int start, int end) {
		this.text = text;
		this.start = start;
		this.end = end;
	}
}
