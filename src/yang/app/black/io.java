package yang.app.black;

import java.io.File;
import java.io.Serializable;

import org.eclipse.jface.text.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import yang.app.black.starter.blackMain;

public class io implements Serializable{
	static final long serialVersionUID = 42L;
	private black black;
	private String saveAsDir;
	private boolean fileOpened = false;
	// private String[] filterExtensions = { "*.black", "*.txt", "*.docx" };
	String[] filterExtensionsForSaveAs = { "*.black" };
	// 同时显示多个扩展名的文件要在各个扩展名之间加分号';'
	String[] filterExtensionsForOpen = { "*.black;*.txt;*.docx;*.doc",
			"*.black", "*.txt", "*.docx" ,"*.doc"};

	public io(black appShell) {
		this.black = appShell;

	}

	public void New(File newfile)// 新建文件的统一入口
	{
		if (black.getEditer() == null) {
			black.createTextArea(null);
			black.setCurrentEditFile(newfile);
			black.tv.setDocument(new Document());
			black.setFileIsSave(1);
			black.applySetting();
			save(newfile);
		} else {
			black.closeCurrentFile(true);
			black.setCurrentEditFile(newfile);
			black.createTextArea(null);
			black.tv.setDocument(new Document());
			black.setFileIsSave(1);
			black.applySetting();
			save(newfile);
		}
	}

	public boolean open(File file)// 打开文件的统一入口
	{
		
		if (file.exists()) {		
				black.createTextArea(null);
				inputAndOutput(file, 2);
				if(black.fileIsSave == 0)
					black.setFileIsSave(1);
				black.setCurrentEditFile(file);
				black.applySetting();
				return true;
		} else {
			MessageBox mess = new MessageBox(black, SWT.OK | SWT.ICON_ERROR);
			mess.setMessage("文件未找到！" + "\n" + "(" + file.toString() + ")");
			mess.open();
			return false;
		}
	}

	public boolean save(File file)// 保存文件的统一入口
	{
		this.inputAndOutput(file, 1);
		black.setFileIsSave(1);
		return true;
	}


	

	

	public File directoryDialog()// 弹出一个目录选择框，将选取的目录作为文件返回，否则返回null
	{
		DirectoryDialog dirDialog = new DirectoryDialog(black);
		dirDialog.open();
		if (dirDialog.getFilterPath().length() > 0) {
			return new File(dirDialog.getFilterPath());
		} else
			return null;
	}

	public File dialogForOtherFile(String[] filter, int style)// 弹出一个文件选择框，将选取的文件路径和文件名作为文件返回，否则返回null
	{
		FileDialog saveDialog = new FileDialog(black, style);
		if (filter != null)
			saveDialog.setFilterExtensions(filter);
		saveDialog.open();

		if (saveDialog.getFileName().length() > 0)
			return (new File(saveDialog.getFilterPath() + "/"
					+ saveDialog.getFileName()));
		else
			return null;

	}



	// ---------------------------------------------------------------------------------------

	public boolean write(StyledText textArea, File file)// 此方法用于在文件切换器中快速保存文件
	{
		File f = null;
		if (file == null) {

			FileDialog saveDialog = new FileDialog(black, SWT.SAVE);

			saveDialog.setFilterExtensions(filterExtensionsForSaveAs);

			saveDialog.open();
			if (saveDialog.getFileName().length() > 0) {
				f = new File(saveDialog.getFilterPath() + "/"
						+ saveDialog.getFileName());
				// .setCurrentFileSaveLocation(fullScreenWord.getCurrentEditFile().getAbsolutePath());
			} else {
				return false;
			}

		} else
			f = file;

		inputAndOutput(f, 1);

		return true;
	}

	public void inputAndOutput(File file, int writerOrRead)// 写为1，读为2
	{
		Runnable threadForIO = new ioThread(black, file, writerOrRead,
				black.tv.getDocument(), black.tv,
				black.ba.getAllStyleRange(black.text));
		Display.getDefault().syncExec(threadForIO);
		black.ba.showCharCount();
	}
}
