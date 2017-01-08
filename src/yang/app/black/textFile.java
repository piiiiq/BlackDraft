package yang.app.black;

import java.io.File;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;

public class textFile {
	private File file;
	private StyledTextContent textContent;
	private int isSave;
	private StyledText styledText;
	private String tempName;
	public textFile()
	{
		
	}
	public void setTempName(String name)
	{
		this.tempName = name;
	}
	
	public void setStyledText(StyledText styledText)
	{
		this.styledText = styledText;
	}
	public StyledText getStyleText()
	{
		return styledText;
	}
	public textFile(StyledText styledText, File file, int isSave)
	{
		this.styledText = styledText;
		this.file = file;
		this.isSave = isSave;
	}
	
	public void setTextContent(StyledTextContent textContent)
	{
		this.textContent = textContent;
	}
	
	public void setFile(File file)
	{
		this.file = file;
	}
	
	public void setIsSave(int isSave)
	{
		this.isSave = isSave;
	}
	
	public StyledTextContent getTextContent()
	{
		return textContent;
	}
	
	public File getFile()
	{
		if(file == null)
			return null;
		else
			return file;
	}
	public String getFileString()
	{
		if(file == null)
			return "null";
		else
			return file.toString();
	}
	
	public int isSave()
	{
		return isSave;
	}
	public String getFileName()
	{
		if(file != null)
		{
			if(isSave == 0)
				return file.getName() + " " + "(Î´±£´æ)";
			else
				return file.getName();
		}
			
		else
		{
			if(isSave == 0)
				return tempName + " " + "(Î´±£´æ)";
			else
				return tempName;
		}
			
	}
	public int getLineCount()
	{
		return this.styledText.getLineCount();
	}
	
}
