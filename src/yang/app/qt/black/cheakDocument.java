package yang.app.qt.black;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import yang.demo.allPurpose.wordCountStat;

public class cheakDocument implements Serializable {
	static final long serialVersionUID = 42L;
	static String[] syl = { "，", "。", "、", "……", "…", "——", "—", "？", "?", "！", ",", ".", "?", "!",

			"<", ">", "~", "·", "《", "》", "”", "“", "\"", "(", ")", "（", "）", ":", "：", "[", "]", "{", "}", "『", "』",
			"【", "】", "@", "—", "——", "/" };

	public static int[] wordStat(String text) {
		int spaceCount = 0;
		int chineseCount = 0;
		int enterCount = 0;
		for (int i = 0; i < text.length(); i++) {
			int c = text.charAt(i);
			if ((c >= 19968) && (c <= 40869)) {
				chineseCount++;
			} else if ((c >= 12288) && (c <= 12311)) {
				chineseCount++;
			} else if ((c >= 65072) && (c <= 65374)) {
				chineseCount++;
			} else if (c == 32) {
				spaceCount++;
			} else if (c == 10) {
				enterCount++;
			}
		}
		return new int[] { spaceCount, chineseCount, enterCount };
	}

	public List<TextRegion> splitString(String str, char c, boolean onlyChinese, String subname) {
		List<TextRegion> lis = new ArrayList();
		int startindex = -1;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				if (startindex == -1) {
					startindex = i;
				} else if (i - startindex > 1) {
					TextRegion tr = null;
					if (onlyChinese) {
						if (wordCountStat.isAsiaString(str.substring(startindex + 1, i))) {
							tr = new TextRegion(str.substring(startindex + 1, i), startindex + 1, i);
							if (subname != null) {
								tr.showname = subname;
							}
						}
					} else {
						tr = new TextRegion(str.substring(startindex + 1, i), startindex + 1, i);
						if (subname != null) {
							tr.showname = subname;
						}
					}
					if (tr != null) {
						lis.add(tr);
					}
					startindex = -1;
				} else {
					startindex = i;
				}
			} else if ((str.charAt(i) == '\n') || (str.charAt(i) == '\r') || (str.charAt(i) == '\t')) {
				startindex = -1;
			}
		}
		return lis;
	}
	/**
	 * 截取两个字符串之间的字符串
	 * 此方法从后面的标记字符串（str0）的位置开始向前检索，截取str0与第一个和str1匹配的字符串之间的字符串
	 * @param text要截取的字符串
	 * @param str0后面的标记字符串
	 * @param str1前面的标记字符串
	 *
	 */
	static String subStringByClose(String text,String str0,String str1) {
		String s = null;
		int indexOf = text.indexOf(str0);
		if(indexOf == -1)return null;
		int lastIndexOf = text.lastIndexOf(str1, indexOf);
		if(lastIndexOf == -1)return null;
		s = text.substring(lastIndexOf+str1.length(),indexOf);
		return s;
	}
	public static String[] subString(String str, String br) {
		int index = str.indexOf(br);
		if (index != -1) {
			String sub1 = str.substring(0, index);
			String sub2 = str.substring(index + 1, str.length());
			return new String[] { sub1, sub2 };
		}
		return new String[] { str, "" };
	}

	public static TextRegion subString(String str, int start, String str1) {
		int index = str.indexOf(str1, start);
		if (index != -1) {
			return new TextRegion(str.substring(start, index), start, index);
		}
		return null;
	}

	public static TextRegion subString(String str, String str1, String str2) {
		int index = str.indexOf(str1);
		if (index != -1) {
			int index_str2 = str.indexOf(str2, index + str1.length());
			if (index_str2 != -1) {
				return new TextRegion(str.substring(index + str1.length(), index_str2), index + str1.length(),
						index_str2);
			}
		}
		return null;
	}

	public void splitString(String str) {
	}

	public static boolean findString(String str1, String str2) {
		if (str1.indexOf(str2) != -1) {
			return true;
		}
		return false;
	}
	/**
	 * 截取str中str1后面的字符串
	 * @param str
	 * @param str1
	 * @return
	 */
	public static TextRegion checkCommand(String str, String str1) {
		int index = str.indexOf(str1);
		if (index != -1) {
			String s = str.substring(index + str1.length(), str.length());
			return new TextRegion(s, index, index + str1.length() + s.length());
		}
		return null;
	}

	public static String[] checkCommandArgs(String str) {
		ArrayList<String> args = new ArrayList();
		char last = ' ';
		String arg = "";
		for (int i = 0; i < str.length(); i++) {
			char curr = str.charAt(i);
			if (curr != ' ') {
				if (last == ' ') {
					arg = "";
					arg = arg + curr;
				} else {
					arg = arg + curr;
				}
			} else if (!arg.equals("")) {
				args.add(arg);
				arg = "";
			}
			last = curr;
		}
		if (!arg.equals("")) {
			args.add(arg);
			arg = "";
		}
		return (String[]) args.toArray(new String[args.size()]);
	}
	
	public static ArrayList<String> checkCommandArgs(String str, char c) {
		ArrayList<String> args = new ArrayList();
		char last = c;
		String arg = "";
		for (int i = 0; i < str.length(); i++) {
			char curr = str.charAt(i);
			if (curr != c) {
				if (last == c) {
					arg = "";
					arg = arg + curr;
				} else {
					arg = arg + curr;
				}
			} else if (!arg.equals("")) {
				args.add(arg);
				arg = "";
			}
			last = curr;
		}
		if (!arg.equals("")) {
			args.add(arg);
			arg = "";
		}
		return args;
	}

	public static TextRegion find(String str, String str1) {
		int index = str.indexOf(str1);
		if (index != -1) {
			return new TextRegion(str1, index, index + str1.length());
		}
		return null;
	}

	public static List<TextRegion> searchByLine(String str, String str1, String showname, String filename) {
		int index = 0;
		List<TextRegion> lis = new ArrayList();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				String substr = str.substring(index, i);
				int index_first = substr.indexOf(str1);
				if (index_first != -1) {
					TextRegion tr = new TextRegion(substr, index_first + index, str1.length());
					tr.showname = showname;
					tr.filename = filename;
					lis.add(tr);
				}
				index = i + 1;
			} else if (i == str.length() - 1) {
				String substr = str.substring(index, str.length());
				int index_first = substr.indexOf(str1);
				if (index_first != -1) {
					TextRegion tr = new TextRegion(substr, index_first + index, str1.length());
					tr.showname = showname;
					tr.filename = filename;
					lis.add(tr);
				}
			}
		}
		return lis;
	}

	public List<TextRegion> searchByLastOfLine(String str, String str1, String showname, String filename) {
		int index = str.length();
		List<TextRegion> lis = new ArrayList();
		for (int i = str.length(); i >= 0; i--) {
			if (str.charAt(i) == '\n') {
				String substr = str.substring(i + 1, index);
				int index_first = substr.indexOf(str1);
				if (index_first != -1) {
					TextRegion tr = new TextRegion(substr, index_first + index, str1.length());
					tr.showname = showname;
					tr.filename = filename;
					lis.add(tr);
				}
				index = i + 1;
			} else if (i == str.length() - 1) {
				String substr = str.substring(index, str.length());
				int index_first = substr.indexOf(str1);
				if (index_first != -1) {
					TextRegion tr = new TextRegion(substr, index_first + index, index_first + index + str1.length());
					tr.showname = showname;
					tr.filename = filename;
					lis.add(tr);
				}
			}
		}
		return lis;
	}

	static boolean cheakString(String str) {
		if ((str.length() > 0) && (!cheak(str.charAt(str.length() - 1)))) {
			return true;
		}
		return false;
	}
	static boolean cheakStringOnly(String str,String[] syls) {
		if ((str.length() > 0) && (!cheakOnly(str.charAt(str.length() - 1),syls))) {
			return true;
		}
		return false;
	}
	/**
	 * 只检查字符是否匹配给定的标点数组
	 * @param c
	 * @param syls
	 * @return
	 */
	static boolean cheakOnly(char c,String[] syls) {
		for (int i = 0; i < syls.length; i++) {
			if (c == syls[i].charAt(0)) {
				return true;
			}
		}
		return false;
	}
	/**检查全部标点，但排除指定的标点
	 * 
	 * @param c
	 * @param without
	 * @return
	 */
	static boolean cheak(char c, String[] without) {
		for (int i = 0; i < syl.length; i++) {
			boolean not = false;
			for(String s:without) {
				if(s.equals(syl[i])) {
					not = true;
				}
			}
			if (!not && c == syl[i].charAt(0)) {
				return true;
			}
		}
		return false;
	}

	static boolean cheak(char c) {
		for (int i = 0; i < syl.length; i++) {
			if (c == syl[i].charAt(0)) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<String> getAllLine(String str) {
		ArrayList<String> lis_allline = new ArrayList();
		int index = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				String substr = str.substring(index, i);
				lis_allline.add(substr);
				index = i + 1;
			} else if (i == str.length() - 1) {
				String substr = str.substring(index, i + 1);
				lis_allline.add(substr);
			}
		}
		return lis_allline;
	}

	public static boolean isAsia(int c) {
		if ((c >= 19968) && (c <= 40869)) {
			return true;
		}
		if ((c >= 12288) && (c <= 12311)) {
			return true;
		}
		if ((c >= 65072) && (c <= 65374)) {
			return true;
		}
		return false;
	}

	public static boolean isEnglishString(String str) {
		boolean is = true;
		for (int i = 0; i < str.length(); i++) {
			int charAt = str.charAt(i);
			if ((charAt < 97) || (charAt > 122)) {
				if ((charAt < 65) || (charAt > 90)) {
					is = false;
					break;
				}
			}
		}
		return is;
	}

	/**
	 * 检查给定的字符串里所有的字符是否都是中文字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAsiaString(String str) {
		boolean allischinese = true;
		for (int i = 0; i < str.length(); i++) {
			if (!isAsia(str.charAt(i))) {
				allischinese = false;
				break;
			}
		}
		return allischinese;
	}

	/**
	 * 检查给定的字符串中的字符是否都不是中文字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAllNoChineseString(String str) {
		boolean allAreNochinese = true;
		for (int i = 0; i < str.length(); i++) {
			if (isAsia(str.charAt(i))) {
				allAreNochinese = false;
				break;
			}
		}
		return allAreNochinese;
	}
	/**
	 * 去除重复的文本条目
	 */
	public static ArrayList<String> withOutSame(ArrayList<String> in) {
		ArrayList<String> al = new ArrayList<>();
		for(String s:in) {
			boolean ishas = false;
			for(String ss:al) {
				if(ss.equals(s)) {
					ishas = true;
					break;
				}
			}
			if(!ishas)al.add(s);
		}
		return al;
	}
}
