package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.custom.StyledText;

public class wordCountStat implements Serializable{
	static final long serialVersionUID = 42L;
	public int spaceCount,chineseCount,enterCount;
	public void wordStat(String text){
		spaceCount = chineseCount = enterCount = 0;
		for(int i=0; i<text.length(); i++)
		{
			int c = text.charAt(i);
			
			if(c >= 19968 && c <= 40869)
				chineseCount++;
			else if(c >= 12288 && c <= 12311)
				chineseCount++;
			else if(c >= 65072 && c <= 65374)
				chineseCount++;
			else if(c == 32) 
				spaceCount++;
			else if(c == 10)
				enterCount++;
		}
	}
	/**
	 * 返回给定的Styledtext组件中的中文字符数量
	 * @param text
	 * @return
	 */
	public static int chineseWordCount(StyledText text)
	{
		int chineseWordCount = 0;
		for(int i=0; i<text.getCharCount(); i++)
		{
			int c = (text.getText(i, i).charAt(0));
			
//			if(c == 32 || c == 13 || c == 10 )
//			{
//				otherWordCount++;
//			}
			if(c >= 19968 && c <= 40869)
			{
				chineseWordCount++;
			}
			else if(c >= 12288 && c <= 12311)
			{
				chineseWordCount++;
			}
			else if(c >= 65072 && c <= 65374)
			{
				chineseWordCount++;
			}
			
		}
		return chineseWordCount;
		
	}
	/**
	 * 判断给定整型值是否为中文字符
	 * @param c
	 * @return
	 */
	public static boolean isAsia(int c)
	{
			
//			if(c == 32 || c == 13 || c == 10 )
//			{
//				otherWordCount++;
//			}
			if(c >= 19968 && c <= 40869)
			{
				return true;
			}
			else if(c >= 12288 && c <= 12311)
			{
				return true;
			}
			else if(c >= 65072 && c <= 65374)
			{
				return true;
			}
			
		return false;
		
	}
	public static boolean isAsiaString(String str){
		boolean allischinese = true;
		for(int i=0; i<str.length(); i++){
			if(!isAsia(str.charAt(i))) {
				allischinese = false;
				break;
			}
		}
		return allischinese;
	}

}
