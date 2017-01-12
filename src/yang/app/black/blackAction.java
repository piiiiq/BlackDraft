/**
 * 该类提供black编辑器所有可自由调用的操作
 */
package yang.app.black;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFRun.FontCharRange;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.app.black.starter.blackMain;
import yang.demo.allPurpose.cfg_read_write;
import yang.demo.allPurpose.fileTool;
import yang.demo.allPurpose.gitTool;
import yang.demo.allPurpose.io;
import yang.demo.allPurpose.stringAction;
import yang.demo.allPurpose.time;
import yang.demo.mail.SendMailBySSL;
import yang.demo.mail.receiveMail;
import yang.demo.mail.showMail;

public class blackAction implements Serializable {
	static final long serialVersionUID = 42L;
	black b;
	Point caretSize;
	static int RENAME = 0, SETNAME = 1;
	KeyListener numberlist;
	int listindex = 1;
	public findinfo findi;
	findinfo_result findinfoResult;
	boolean includeRecycle;
	public Listener sliderListener;
	List<TextRegion> markTextData;
	findWord find;
	public String marktext = "";
	boolean marktextIsChanged,markstatIsChanged;
	public Shell fullscreen;
	boolean fullscreen_writingview;
	Color lastForeColor, lastBackColor;
	public int posForTypeMode = 2;
	PrintStream errorStream, sysErr;
	ArrayList<markstat> markstat = new ArrayList<>();
	int ProgressValue;
	String progressMessage = "";
	StringBuilder logsmessage = new StringBuilder();

	public blackAction(black b) {
		this.b = b;
	}

	/**
	 * 新建文件
	 */
	public void newFile(File f) {
		b.getIO().New(f);
	}

	/**
	 * 保存当前编辑的文件 该方法会调用black里的saveCurrentFile方法
	 */
	public void saveFile(boolean saveFileView, boolean saveCharCount) {
		b.saveCurrentFile(saveFileView, saveCharCount);
		b.changeSaveLabelState();
	}

	/**
	 * 显示设置
	 */
	public void showSettings() {
		new settings(b, SWT.None).open();
	}

	/**
	 * 查找与替换
	 * 
	 */
	public void findReplaceWord() {
		if (b.getEditer() != null) {
			if (find == null || find.shell.isDisposed()) {
				find = new findWord(b, SWT.None);
				find.open();
				b.blackTextArea.clearSelection();
			}
		}
	}

	/**
	 * 缩小编辑器字体
	 */
	public void zoomIn() {
		if (b.getEditorZoomFromCFG() > 100)
			b.setEditorZoom(b.getEditorZoomFromCFG() - 10);
	}

	/**
	 * 放大编辑器字体
	 */
	public void zoomOut() {
		if (b.getEditorZoomFromCFG() < 400)
			b.setEditorZoom(b.getEditorZoomFromCFG() + 10);
	}

	public void selectTextOfline() {
		b.text.setSelection(currentLineOffest());
	}

	/**
	 * 返回当前编辑的段落的首尾偏移值
	 * 
	 * @return
	 */
	public Point currentLineOffest() {
		int lineindex = b.text.getLineAtOffset(b.text.getCaretOffset());
		String str = b.text.getLine(lineindex);
		int linestartoffset = b.text.getOffsetAtLine(lineindex);
		int lineendoffset = linestartoffset + str.length();
		return new Point(linestartoffset, lineendoffset);
	}

	/**
	 * 选择编辑器所有的文本
	 */
	public void selectAllText(StyledText text) {
		if (text != null) {

			if (text.getSelection().equals(currentLineOffest())) {
				int oldTopPixes = text.getTopPixel();
				selectAll(text);
				text.setTopPixel(oldTopPixes);
			} else
				selectTextOfline();
		}
	}

	/**
	 * 快速切换到上次打开的文件
	 */
	public void fastOpenFile() {
		if (b.lastOpenedFile != null && !b.lastOpenedFile.equals(b.getCurrentEditFile())) {
			openFile(b.lastOpenedFile, true);
		}
	}

	/**
	 * 如果当前的全屏写作视图属性取反后写入应用程序属性里
	 */
	public void setWritingView() {
		if (b.appProperties.getProperty("FullScreen_WritingView", "0").equals("0"))
			b.appProperties.setProperty("FullScreen_WritingView", "1");
		else
			b.appProperties.setProperty("FullScreen_WritingView", "0");
	}

	public boolean isFullScreenWritingView() {
		if (b.appProperties.getProperty("FullScreen_WritingView", "0").equals("0"))
			return false;
		else
			return true;
	}

	public void openWritingView() {
		if (b.wv == null || b.wv.isDisposed()) {
			if (b.getCurrentEditFile() != null && b.text != null) {
				if (b.appProperties.getProperty("FullScreen_WritingView", "0").equals("1")) {
					fullScreenwritingview();
				} else {
					b.wv = new writingView(b, SWT.SHELL_TRIM);
				}

			}
			// fullScreenwritingview();
			// b.wv = new writingView(b, SWT.SHELL_TRIM);
		}
	}

	void fullScreenwritingview() {
		fullscreen = new Shell(SWT.NO_TRIM) {
			public void dispose() {
				super.dispose();
			};

			protected void checkSubclass() {

			}
		};
		// shell.setFullScreen(true);
		fullscreen.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				b.text.setFocus();
			}
		});
		fullscreen.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.x > fullscreen.getSize().x - 50 && arg0.y < 50) {
					b.wv.dispose();
				}
			}

			@Override
			public void mouseExit(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEnter(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		fullscreen.setMaximized(true);
		fullscreen.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		fullscreen.setAlpha(150);
		fullscreen.setImage(b.getImage());
		fullscreen.setText("写作视图");
		fullscreen.setVisible(true);
		b.wv = new writingView(fullscreen, b, SWT.APPLICATION_MODAL);

		if (lastBackColor != null)
			changeColor(lastForeColor, lastBackColor);
		else
			changeColor(SWTResourceManager.getColor(SWT.COLOR_BLACK),
					SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		b.wv.layout();
		b.wv.setVisible(true);
		b.text.setFocus();
	}

	public void restoreWritingView() {
		openWritingView();
		b.wv.resetView();
	}

	public boolean isWritingView() {
		if (b.getAppProperties("isWritingView") != null && b.getAppProperties("isWritingView").equals("true"))
			return true;
		else
			return false;
	}

	public String getSysteminfo() {
		StringBuilder sb = new StringBuilder();
		if (b.projectProperties != null) {
			String proname = b.projectProperties.getProperty("projectName");
			String prodate = b.projectProperties.getProperty("createDate");
			sb.append("项目名称：" + proname + "\n" + "项目创建时间：" + prodate);
		}
		try {
			InetAddress ia = InetAddress.getLocalHost();
			sb.append("\n主机名称：" + ia.getHostName() + "\n主机地址：" + ia.getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String getHostName() {
		String hostname = "";
		try {
			InetAddress ia = InetAddress.getLocalHost();
			hostname = ia.getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hostname;
	}

	static String getMyIP() throws IOException {
		InputStream ins = null;
		try {
			URL url = new URL("http://www.baidu.com");
			URLConnection con = url.openConnection();
			ins = con.getInputStream();
			InputStreamReader isReader = new InputStreamReader(ins, "GB2312");
			BufferedReader bReader = new BufferedReader(isReader);
			StringBuffer webContent = new StringBuffer();
			String str = null;
			while ((str = bReader.readLine()) != null) {
				webContent.append(str);
			}
			int start = webContent.indexOf("[") + 1;
			int end = webContent.indexOf("]");
			return webContent.substring(start, end);
		} finally {
			if (ins != null) {
				ins.close();
			}
		}
	}

	public void getSystemInfo() {
		StringBuffer sb = new StringBuffer();
		Properties syspro = System.getProperties();
		Enumeration<Object> en = syspro.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			sb.append(key + ": " + syspro.getProperty(key, "null") + "\n");
		}
		getBMessageBox("系统信息", sb.toString());
	}

	/**
	 * 
	 * @param title
	 *            文本信息对话框的标题
	 * @param text
	 *            要显示的文本
	 * @param readOnly
	 *            是否为只读模式
	 */
	public void getBMessageBox(String title, String text) {
		bMessageBox bmb = new bMessageBox(b, SWT.None, false) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void saveAction() {
				// TODO Auto-generated method stub

			}
		};
		bmb.setTitle(title);
		bmb.setText(text);
		bmb.open();
	}

	public void getBMessageBox(String title, String text, int fontstyle, int fontsize) {
		bMessageBox bmb = new bMessageBox(b, SWT.None, false) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void saveAction() {
				// TODO Auto-generated method stub

			}
		};
		bmb.setTitle(title);
		bmb.setText(text);
		Font font = SWTResourceManager.getFont(bmb.text.getFont().getFontData()[0].getName(), fontsize, fontstyle);
		bmb.text.setFont(font);
		bmb.open();
	}

	/**
	 * 退出程序
	 */
	public void exitProgram() {
		b.dispose();
	}

	/**
	 * 显示关于对话框
	 */
	public void aboutBlack() {
		new aboutBlack(b, SWT.None).open();
	}

	// /**
	// * 编辑器设置
	// */
	// public void editerSettings() {
	// new editerSettings(b, SWT.None).open();
	// }
	public int getEndOffest(StyledText st) {
		int end = 0;
		int endStringLength = 0;
		for (int i = st.getLineCount() - 1; i >= 0; i--) {
			String s = st.getLine(i);
			if (!s.equals("")) {
				end = i;
				endStringLength = s.length();
				break;
			}
		}
		return st.getOffsetAtLine(end) + endStringLength;
	}

	public void selectAll(StyledText st) {
		st.setSelection(0, getEndOffest(st));
	}

	/**
	 * 显示菜单栏
	 * 
	 * @param isShow
	 */
	public void showMenuBar(boolean isShow) {
		if (!isShow) {
			if (b.getMenuBar() != null)
				b.getMenuBar().dispose();
		} else
			b.createMenuBar();
		b.appProperties.setProperty("MenuBar", String.valueOf(isShow));
	}

	/**
	 * 是否显示菜单栏
	 * 
	 * @return
	 */
	public boolean isShowMenuBar() {
		return Boolean.valueOf(b.appProperties.getProperty("MenuBar", "true"));
	}

	/**
	 * 显示字数统计对话框
	 */
	public void wordCountStat(Shell shell) {
		int allChar;
		int chineseChar;
		if (b.getEditer() != null) {
			allChar = b.getEditer().getCharCount();
			chineseChar = wordCountStat.chineseWordCount(b.getEditer());
			MessageBox mb = new MessageBox(shell);
			mb.setText("字数统计");
			mb.setMessage("字符数（不计换行符和空格）：" + getCharsCount() + "\n-----------------\n" + "中日韩字数：" + chineseChar
					+ "\n-----------------\n" + "字符总数：" + allChar);
			mb.open();
			b.text.setFocus();
		}
	}

	/**
	 * 设置在启动后打开最后使用的文件
	 * 
	 * @param isreopen
	 */
	public void setReOpenLastFileOnStarted(boolean isreopen) {
		b.appProperties.setProperty("ReOpenOnStarted", String.valueOf(isreopen));
	}

	/**
	 * 启动后是否打开最后使用的文件
	 * 
	 * @return
	 */
	public boolean IsReOpenLastProjectOnStarted() {
		return Boolean.valueOf(b.appProperties.getProperty("ReOpenOnStarted", "true"));
	}

	public void setColorForLabel(final Label label) {
		label.addMouseTrackListener(new MouseTrackListener() {

			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(b.color_label_exit);
			}

			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(b.color_labelDown);
			}
		});
	}

	/**
	 * 撤销编辑器所做的更改
	 */
	public void Undo() {
		if (b.tv != null) {
			b.tv.doOperation(b.tv.UNDO);
		}
	}

	/**
	 * 重做编辑器所做的更改
	 */
	public void Redo() {
		if (b.tv != null)
			b.tv.doOperation(b.tv.REDO);

	}

	/**
	 * 向日志对话框添加日志
	 * 
	 * @param log
	 */
	public void addLog(Object log, Point location, boolean clearlog) {
		if (b.log == null)
			b.log = new logShell(b);
		if (log != null) {
			b.log.appendLog(log.toString(), location, clearlog);
			b.log.setVisible(true);
		}
	}

	public MenuItem getMenuItem(Menu menu, String text, int style) {
		MenuItem mi = new MenuItem(menu, style);
		mi.setText(text);
		return mi;
	}

	public void cutText(StyledText text) {
		if (text != null)
			text.cut();
	}

	public void pasteText(StyledText text) {
		if (text != null)
			text.paste();
	}

	public void copyText(StyledText text) {
		if (text != null)
			text.copy();
		;
	}

	/**
	 * 为当前选择的文本设置对齐方式
	 * 
	 * @param alignment
	 */
	public void setCurrentSelectedTextAlignment(int alignment) {
		Point range = b.getEditer().getSelectionRange();
		if (range != null) {
			int startline = b.getEditer().getLineAtOffset(range.x);
			int endline = b.getEditer().getLineAtOffset(range.x + range.y);
			if (startline != endline)
				b.getEditer().setLineAlignment(startline, endline - startline + 1, alignment);
			else
				b.getEditer().setLineAlignment(startline, 1, alignment);
		} else {
			b.getEditer().setLineAlignment(b.getEditer().getLineAtOffset(b.getEditer().getCaretOffset()), 1, alignment);
		}
		b.setFileIsSave(0);
	}

	/**
	 * 为当前选择的文本设置字体属性
	 * 
	 * @param fontName
	 * @param size
	 * @param style
	 * 
	 */
	public void setTextFontStyle(String fontname, int fontsize, int fontstyle, int strikeout, int underline) {
		StyleRange[] sr = b.text.getStyleRanges(b.text.getSelectionRange().x, b.text.getSelectionRange().y);
		StyleRange newstyle = null;
		TextStyle ts = null;
		if (fontname != null && fontsize != -1 && fontstyle != -1) {
			Font newfont = SWTResourceManager.getFont(fontname, b.getZoomedFontSize(fontsize), fontstyle);
			ts = new TextStyle(newfont, null, null);

		} else if (fontname != null) {
			int oldstyle = SWT.None;
			int oldfontsize = getEditerDefaultFontSize();
			Font newfont = SWTResourceManager.getFont(fontname, b.getZoomedFontSize(oldfontsize), oldstyle);
			ts = new TextStyle(newfont, null, null);
		} else if (fontsize != -1) {
			int oldstyle = SWT.None;
			String oldfontname = b.getEditorDefaultFontFromCFG();
			Font newfont = SWTResourceManager.getFont(oldfontname, b.getZoomedFontSize(fontsize), oldstyle);
			ts = new TextStyle(newfont, null, null);
		} else if (fontstyle != -1) {
			int oldfontsize = getEditerDefaultFontSize();
			String oldfontname = b.getEditorDefaultFontFromCFG();
			Font newfont = SWTResourceManager.getFont(oldfontname, b.getZoomedFontSize(oldfontsize), fontstyle);
			ts = new TextStyle(newfont, null, null);
		} else if (strikeout != -1) {
			int oldfontsize = getEditerDefaultFontSize();
			String oldfontname = b.getEditorDefaultFontFromCFG();
			Font newfont = SWTResourceManager.getFont(oldfontname, b.getZoomedFontSize(oldfontsize), SWT.None);
			ts = new TextStyle(newfont, null, null);
			if (strikeout == 0)
				ts.strikeout = false;
			else
				ts.strikeout = true;
		} else if (underline != -1) {
			int oldfontsize = getEditerDefaultFontSize();
			String oldfontname = b.getEditorDefaultFontFromCFG();
			Font newfont = SWTResourceManager.getFont(oldfontname, b.getZoomedFontSize(oldfontsize), SWT.None);
			ts = new TextStyle(newfont, null, null);
			if (underline == 0)
				ts.underline = false;
			else
				ts.underline = true;
		}

		if (ts != null) {
			if (b.text.getSelectionCount() > 0) {
				newstyle = new StyleRange(ts);
				newstyle.start = b.text.getSelectionRange().x;
				newstyle.length = b.text.getSelectionRange().y;
				b.text.setStyleRange(newstyle);
			} else if (b.text.getCaretOffset() == b.text.getCharCount()) {
				b.blackTextArea.defaultStyle = ts;
			}
		}

		for (StyleRange s : sr) {
			if (s.font != null) {
				if (fontname != null && fontsize != -1 && fontstyle != -1) {
					Font newfont = SWTResourceManager.getFont(fontname, b.getZoomedFontSize(fontsize), fontstyle);
					s.font = newfont;
				} else if (fontname != null) {
					int oldstyle = s.font.getFontData()[0].getStyle();
					int oldfontsize = s.font.getFontData()[0].getHeight();
					Font newfont = SWTResourceManager.getFont(fontname, oldfontsize, oldstyle);
					s.font = newfont;
				} else if (fontsize != -1) {
					int oldstyle = s.font.getFontData()[0].getStyle();
					String oldfontname = s.font.getFontData()[0].getName();
					Font newfont = SWTResourceManager.getFont(oldfontname, b.getZoomedFontSize(fontsize), oldstyle);
					s.font = newfont;
				} else if (fontstyle != -1) {
					int oldfontsize = s.font.getFontData()[0].getHeight();
					String oldfontname = s.font.getFontData()[0].getName();
					Font newfont = SWTResourceManager.getFont(oldfontname, oldfontsize, fontstyle);
					s.font = newfont;
				} else if (strikeout != -1) {
					if (strikeout == 0)
						s.strikeout = false;
					else
						s.strikeout = true;
				} else if (underline != -1) {
					if (underline == 0)
						s.underline = false;
					else
						s.underline = true;
				}
			} else {
				if (fontname != null && fontsize != -1 && fontstyle != -1) {
					Font newfont = SWTResourceManager.getFont(fontname, b.getZoomedFontSize(fontsize), fontstyle);
					s.font = newfont;
				} else if (fontname != null) {
					int oldstyle = SWT.None;
					int oldfontsize = getEditerDefaultFontSize();
					Font newfont = SWTResourceManager.getFont(fontname, b.getZoomedFontSize(oldfontsize), oldstyle);
					s.font = newfont;
				} else if (fontsize != -1) {
					int oldstyle = SWT.None;
					String oldfontname = b.getEditorDefaultFontFromCFG();
					Font newfont = SWTResourceManager.getFont(oldfontname, b.getZoomedFontSize(fontsize), oldstyle);
					s.font = newfont;
				} else if (fontstyle != -1) {
					int oldfontsize = getEditerDefaultFontSize();
					String oldfontname = b.getEditorDefaultFontFromCFG();
					Font newfont = SWTResourceManager.getFont(oldfontname, b.getZoomedFontSize(oldfontsize), fontstyle);
					s.font = newfont;
				} else if (strikeout != -1) {
					if (strikeout == 0)
						s.strikeout = false;
					else
						s.strikeout = true;
				} else if (underline != -1) {
					if (underline == 0)
						s.underline = false;
					else
						s.underline = true;
				}
			}
			b.text.setStyleRange(s);
		}
		// b.text.setStyleRanges(sr);

		b.setFileIsSave(0);
	}

	/**
	 * 调用字体选择对话框
	 * 
	 * @return
	 */
	public FontData getMoreFont() {
		FontDialog fd = new FontDialog(b);
		FontData font = fd.open();
		return font;
	}

	/**
	 * 获取当前选择的文本的字体名称、字号、字体风格
	 * 
	 * @return textInfo,被封装过的属性，如果所选择的文本包含多个字体，则返回null，否则返回某个字体名称，
	 *         如果所选择的文本包含多个字号，则字号返回-1，如果所选择的文本包含多个字体样式，则返回-1
	 */
	public textInfo getCurrentSelectTextFontStyle() {
		StyleRange[] sr = b.getEditer().getStyleRanges(b.getEditer().getSelectionRange().x,
				b.getEditer().getSelectionRange().y);
		String fontname = null;
		int fontsize = 0, fontstyle = 0, alignments = 0;
		textInfo info = null;
		boolean scrikeout = false, underline = false;
		// 如果返回的风格区域数组长度为0，则返回编辑器默认字体的属性
		if (sr.length == 0) {
			info = new textInfo(getEditerDefaultFontName(), getEditerDefaultFontSize(), -1, false, false,
					b.getEditer().getAlignment());
		} else {
			// 遍历风格数组
			for (StyleRange s : sr) {
				if (s.font != null) {
					FontData[] fd = s.font.getFontData();
					for (FontData f : fd) {
						if (fontname == null)
							fontname = f.getName();
						else {
							if (!fontname.equals(f.getName()))
								fontname = null;
						}
						if (fontsize == 0) {
							fontsize = b.getFontRealSize(f.getHeight());
							// System.out.println(f.getHeight());
						} else {
							if (fontsize != f.getHeight())
								fontsize = -1;
						}
						if (fontstyle == 0)
							fontstyle = f.getStyle();
						else {
							if (fontstyle != f.getStyle())
								fontstyle = -1;
						}
					}
					scrikeout = s.strikeout;
					underline = s.underline;
				} else {
					fontname = b.getEditorDefaultFontFromCFG();
					fontsize = getEditerDefaultFontSize();
					fontstyle = SWT.None;
					scrikeout = false;
					underline = false;
				}
			}
			// 判断所选择的行对齐方式是否相同，如果不同则将alignments设置为-1
			Point range = b.getEditer().getSelectionRange();
			int startline = b.getEditer().getLineAtOffset(range.x);
			int endline = b.getEditer().getLineAtOffset(range.x + range.y);
			if (startline != endline) {
				for (int i = startline; i <= endline; ++i) {
					if (alignments == 0)
						alignments = b.getEditer().getLineAlignment(i);
					else if (alignments != b.getEditer().getLineAlignment(i)) {
						alignments = -1;
					}
				}
			} else
				alignments = b.getEditer().getLineAlignment(startline);
		}
		if (info == null)
			info = new textInfo(fontname, fontsize, fontstyle, scrikeout, underline, alignments);
		return info;
	}

	/**
	 * 将StyledText组件的内容样式储存至属性文件
	 * 
	 * @param text包含内容样式的StyledText组件
	 * @return 包含内容样式的属性文件
	 */
	public Properties getAllStyleRange(StyledText text) {
		StyleRange[] sr = text.getStyleRanges();
		Properties styles = new Properties();
		for (int i = 0; i < sr.length; i++) {
			styles.setProperty(i + "start", String.valueOf(sr[i].start));
			styles.setProperty(i + "length", String.valueOf(sr[i].length));
			// styles.setProperty(i + "background",
			// String.valueOf(sr[i].background));
			// styles.setProperty(i + "foreground",
			// String.valueOf(sr[i].foreground));
			if (sr[i].font != null) {
				styles.setProperty(i + "font", String.valueOf(sr[i].font.getFontData()[0].getName()));
				styles.setProperty(i + "fontheight",
						String.valueOf(b.getFontRealSize(sr[i].font.getFontData()[0].getHeight())));
				styles.setProperty(i + "fontstyle", String.valueOf(sr[i].font.getFontData()[0].getStyle()));
			} else {
				styles.setProperty(i + "font", String.valueOf(text.getFont().getFontData()[0].getName()));
				styles.setProperty(i + "fontheight",
						String.valueOf(b.getFontRealSize(text.getFont().getFontData()[0].getHeight())));
				styles.setProperty(i + "fontstyle",
						String.valueOf(b.getFontRealSize(text.getFont().getFontData()[0].getStyle())));
			}
			styles.setProperty(i + "strikeout", String.valueOf(sr[i].strikeout));
			styles.setProperty(i + "underline", String.valueOf(sr[i].underline));
		}
		int count = 0;
		for (int a = 0; a < text.getLineCount(); ++a) {
			int align = text.getLineAlignment(a);
			if (align != text.getAlignment()) {
				styles.setProperty(count + "alignmentstartline", String.valueOf(a));
				styles.setProperty(count + "alignment", String.valueOf(align));
				++count;
			}
		}
		return styles;
	}

	/**
	 * 从属性文件还原内容样式到text
	 * 
	 * @param p包含内容样式的属性文件
	 * @param text
	 *            要应用内容样式的StyledText组件
	 */
	public void getStylesFromProperties(Properties p, StyledText text) {
		String start, length, font, fontstyle, strikeout, underline, fontheight;
		for (int i = 0; i > -1; i++) {
			start = p.getProperty(i + "start");
			if (start != null) {
				length = p.getProperty(i + "length");
				font = p.getProperty(i + "font");
				fontheight = p.getProperty(i + "fontheight");
				fontstyle = p.getProperty(i + "fontstyle");
				strikeout = p.getProperty(i + "strikeout");
				underline = p.getProperty(i + "underline");
				if (Integer.valueOf(fontheight) >= 0) {
					Font f = SWTResourceManager.getFont(font, b.getZoomedFontSize(Integer.valueOf(fontheight)),
							Integer.valueOf(fontstyle));
					TextStyle ts = new TextStyle(f, null, null);
					ts.strikeout = Boolean.valueOf(strikeout);
					ts.underline = Boolean.valueOf(underline);
					StyleRange sr = new StyleRange(ts);
					sr.start = Integer.valueOf(start);
					sr.length = Integer.valueOf(length);
					if (sr.start >= 0 && sr.length + sr.start <= text.getCharCount())
						text.setStyleRange(sr);
				} else
					getMessageBox("读取文件时出错！", "black文件中的样式属性包含错误！字体高度不能为负值！");
			} else
				break;
		}
		for (int a = 0; a > -1; ++a) {
			String startline = p.getProperty(a + "alignmentstartline");
			if (startline != null) {
				text.setLineAlignment(Integer.valueOf(startline), 1, Integer.valueOf(p.getProperty(a + "alignment")));
			} else
				break;
		}
	}

	/**
	 * 保存text的内容样式到docx格式文件
	 */
	@SuppressWarnings("deprecation")
	public void saveStylesToDocxFile(StyledText text, XWPFDocument document) {

		for (int i = 0; i < text.getLineCount(); ++i) {
			int lineOffset = text.getOffsetAtLine(i);
			String lineText = text.getLine(i);
			XWPFParagraph paragraph = document.createParagraph();
			paragraph.setFirstLineIndent(text.getIndent());
			int lineAlignment = text.getLineAlignment(i);
			switch (lineAlignment) {
			case SWT.RIGHT:
				paragraph.setAlignment(ParagraphAlignment.RIGHT);
				break;
			case SWT.CENTER:
				paragraph.setAlignment(ParagraphAlignment.CENTER);
				break;
			}
			// 遍历每一行里的每个字符
			for (int a = lineOffset; a < lineOffset + lineText.length(); ++a) {
				StyleRange charStyle = text.getStyleRangeAtOffset(a);
				String charText = text.getText(a, a);
				XWPFRun run = paragraph.createRun();
				run.setText(charText);
				String fontname = null;
				int fontsize = 0;
				if (charStyle != null) {
					// Color background = charStyle.background;
					// Color foreground = charStyle.foreground;
					if (charStyle.font != null) {
						fontname = charStyle.font.getFontData()[0].getName();
						if (b.text != null && text.equals(b.text))
							fontsize = b.getFontRealSize(charStyle.font.getFontData()[0].getHeight());
						else
							fontsize = text.getFont().getFontData()[0].getHeight();
					}
					int fontstyle = charStyle.fontStyle;
					switch (fontstyle) {
					case SWT.BOLD:
						run.setBold(true);
						break;
					case SWT.ITALIC:
						run.setItalic(true);
						break;
					case SWT.BOLD | SWT.ITALIC:
						run.setBold(true);
						run.setItalic(true);
						break;
					}
					run.setStrike(charStyle.strikeout);
					if (charStyle.underline)
						run.setUnderline(UnderlinePatterns.SINGLE);

					run.setFontFamily(fontname, FontCharRange.eastAsia);
					run.setFontSize(fontsize);
				}

			}

		}

	}

	public void getContentFromDocxFile(XWPFDocument document, StyledText text, TextViewer tv) {
		StringBuilder sb = new StringBuilder();
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		Iterator<XWPFParagraph> itp = paragraphs.iterator();
		List<StyleRange> styles = new ArrayList<StyleRange>();
		List<alignmentInfo> aligInfo = new ArrayList<alignmentInfo>();
		alignmentInfo info = null;
		while (itp.hasNext()) {
			XWPFParagraph paragraph = itp.next();
			ParagraphAlignment pa = paragraph.getAlignment();
			if (pa != ParagraphAlignment.LEFT) {
				if (pa == ParagraphAlignment.CENTER)
					info = new alignmentInfo(sb.length(), SWT.CENTER);
				else if (pa == ParagraphAlignment.RIGHT)
					info = new alignmentInfo(sb.length(), SWT.RIGHT);
				aligInfo.add(info);
			}

			List<XWPFRun> runs = paragraph.getRuns();
			Iterator<XWPFRun> itr = runs.iterator();
			while (itr.hasNext()) {
				XWPFRun run = itr.next();

				String runText = run.getText(0);
				if (runText != null) {
					StyleRange sr = null;
					int fontstyle = 0;
					boolean underline = false, strikeout = false;
					Font font = null;

					if (run.isBold())
						fontstyle = SWT.BOLD;
					if (run.isItalic())
						fontstyle = SWT.ITALIC;
					if (run.isBold() && run.isItalic())
						fontstyle = SWT.BOLD | SWT.ITALIC;
					if (run.getUnderline() != UnderlinePatterns.NONE)
						underline = true;
					if (run.isStrike())
						strikeout = true;

					if (run.getFontFamily(FontCharRange.eastAsia) != null) {
						if (run.getFontSize() > 0) {
							if (b.text != null && text.equals(b.text))
								font = SWTResourceManager.getFont(run.getFontFamily(FontCharRange.eastAsia),
										b.getZoomedFontSize(run.getFontSize()), fontstyle);
							else
								font = SWTResourceManager.getFont(run.getFontFamily(FontCharRange.eastAsia),
										run.getFontSize(), fontstyle);
						} else {
							if (b.text != null && text.equals(b.text))
								font = SWTResourceManager.getFont(run.getFontFamily(FontCharRange.eastAsia),
										b.getZoomedFontSize(getEditerDefaultFontSize()), fontstyle);
							else
								font = SWTResourceManager.getFont(run.getFontFamily(FontCharRange.eastAsia),
										text.getFont().getFontData()[0].getHeight(), fontstyle);
						}

					}
					if (fontstyle != 0 || underline || strikeout || font != null) {
						sr = new StyleRange();
						sr.start = sb.length();
						sr.length = runText.length();
						if (font != null)
							sr.font = font;
						sr.underline = underline;
						sr.strikeout = strikeout;
						if (styles.size() > 0) {
							if (!sr.similarTo(styles.get(styles.size() - 1)))
								styles.add(sr);
							else {
								styles.get(styles.size() - 1).length += run.getText(0).length();
							}
						} else
							styles.add(sr);
					}
					sb.append(run.getText(0));
				}

			}
			sb.append("\n");
		}
		tv.setDocument(new Document(sb.toString()));
		Iterator<StyleRange> itstyle = styles.iterator();
		while (itstyle.hasNext()) {
			StyleRange sr = itstyle.next();
			text.setStyleRange(sr);
		}
		Iterator<alignmentInfo> italign = aligInfo.iterator();
		while (italign.hasNext()) {
			alignmentInfo aligninfo = italign.next();
			if (aligninfo != null)
				text.setLineAlignment(text.getLineAtOffset(aligninfo.linestartoffset), 1, aligninfo.alignment);
		}
	}

	public void setEditerDefaultFontSize(int size) {
		b.appProperties.setProperty("EditerDefaultFontSize", String.valueOf(size));
	}

	public int getEditerDefaultFontSize() {
		return Integer.valueOf(b.appProperties.getProperty("EditerDefaultFontSize", "10"));
	}

	public String getEditerDefaultFontName() {
		return b.getEditorDefaultFontFromCFG();
	}

	public void setEditerDefaultFontName(String fontname) {
		b.setEditorFont(fontname);
	}

	/**
	 * 呼出消息对话框
	 * 
	 * @param message
	 */
	public void getMessageBox(String title, String message) {
		MessageBox mb = new MessageBox(b, SWT.ICON_INFORMATION);
		mb.setText(title);
		mb.setMessage(message);
		mb.open();
		if (b.wv != null && !b.wv.isDisposed())
			b.wv.setFocus();
		else if (b.text != null)
			b.text.setFocus();
	}

	public void createProject(String projectname, String dir) {

		File projectdir = new File(dir + "\\" + projectname);
		if (projectdir.exists()) {
			getMessageBox("", "该名称已被使用，请指定其他名称");
		} else {
			projectdir.mkdir();
			new File(projectdir + "\\Files").mkdir();
			new File(projectdir + "\\Settings").mkdir();
			Properties pro = new Properties();
			pro.setProperty("blackVersion", black.getAppVersion());
			pro.setProperty("createDate", time.getCurrentDate("") + " " + time.getCurrentTime());
			pro.setProperty("projectName", projectname);
			pro.setProperty("fileCount", "0");

			File projectFile = new File(projectdir + "\\project.bpro");
			File fileinfoFile = new File(projectdir + "\\Settings\\fileinfo");
			File recyclefile = new File(projectdir + "\\Settings\\recycle");
			File indexfile = new File(projectdir + "\\Settings\\fileindex.blaobj");
			Properties fileinfo = new Properties();
			Properties recycle = new Properties();

			ArrayList<String> al = new ArrayList<String>();
			ioThread io = new ioThread(b, al, indexfile, 1);
			b.getDisplay().syncExec(io);
			cfg_read_write.cfg_write(pro, projectFile);
			cfg_read_write.cfg_write(fileinfo, fileinfoFile);
			cfg_read_write.cfg_write(recycle, recyclefile);
			b.setProject(pro, projectFile);
		}
	}

	public void openProject(File projectProperties) {
		if (projectProperties.exists()) {
			Properties project;
			project = cfg_read_write.cfg_read(projectProperties);
			b.setProject(project, projectProperties);
			marktext = null;
			markTextData = null;
			readMarkFile();
		} else {
			getMessageBox("", "项目不存在");
			// b.tree.setEnabled(false);
			newProject np = new newProject(b, SWT.None);
			boolean result = (boolean) np.open();
			if (!result)
				b.dispose();
		}
	}

	/**
	 * 打开项目树所选择的文件 此方法支持外部调用
	 */
	public void openFile() {
		if (b.tree.getSelectionCount() == 1) {
			TreeItem[] tis = b.tree.getSelection();
			for (TreeItem ti : tis) {
				if (!ti.equals(b.draft) && !ti.equals(b.treeItem_1)) {
					File f = new File(b.projectFile.getParent() + "\\Files\\" + ti.getData("realname"));
					if (b.getCurrentEditFile() == null || !b.getCurrentEditFile().equals(f))
						openFile(f, true);
				}
				if (b.findresult != null && ti.getParentItem() != null)
					if (ti.getParentItem().equals(b.findresult)) {
						showFindResult(ti);
					}
			}

		}
	}

	/**
	 * 向项目里添加新文件 此方法支持外部调用
	 */
	public void addFileToProject(String showname) {
		File newfile = new File(b.projectFile.getParent() + "\\Files\\" + b.FileCountOfProject + ".black");
		// b.fileindex.add(b.FileCountOfProject+".black");
		// b.fileInfo.setProperty(b.FileCountOfProject + ".black",
		// String.valueOf(b.FileCountOfProject));
		if (showname == null) {
			Object result = new rename(b, SETNAME).open();
			if (result != null) {
				b.fileInfo.setProperty(b.FileCountOfProject + ".black", (String) result);
			} else
				b.fileInfo.setProperty(b.FileCountOfProject + ".black", "未命名");
		} else
			b.fileInfo.setProperty(b.FileCountOfProject + ".black", showname);

		if (b.fileindex != null) {
			b.fileindex.add(b.FileCountOfProject + ".black");
			saveFileindex();
		}
		newFile(newfile);
		++b.FileCountOfProject;
		b.projectProperties.setProperty("fileCount", String.valueOf(b.FileCountOfProject));
		saveFileInfoCFG();
		saveProjectCFG();
		b.listProjectFile();
		// addLog(b.projectProperties.getProperty("fileCount"));

	}

	/**
	 * 删除文件 如果文件在草稿箱，此方法将文件从草稿箱移动到垃圾箱 如果文件在垃圾箱，则会永久删除 此方法供外部调用
	 */
	public void moveFileToRecycle() {
		TreeItem[] tis = b.tree.getSelection();
		for (TreeItem ti : tis) {
			if (!ti.equals(b.draft) && !ti.equals(b.treeItem_1)) {
				if (ti.getParentItem().equals(b.draft)) {
					moveFileToRecycle((String) ti.getData("realname"), true);
					// ti.dispose();
				} else if (ti.getParentItem().equals(b.treeItem_1)) {
					MessageBox mb = new MessageBox(b, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
					mb.setMessage("确实要删除文件 - " + ti.getText() + "?\n此操作将无法撤销");
					int result = mb.open();
					if (result == SWT.OK) {
						deleteFile((String) ti.getData("realname"));
						// ti.dispose();
					}
				}
			}
		}

		b.listProjectFile();
	}

	/**
	 * 将草稿箱的文件移动至垃圾箱,此方法不支持外部调用
	 * 
	 * @param realname
	 * @param saveCFG
	 */
	protected void moveFileToRecycle(String realname, boolean saveCFG) {
		b.recycle.setProperty(realname, "true");
		if (saveCFG)
			saveRecycleCFG();

	}

	/**
	 * 彻底删除垃圾箱中的文件,此方法不支持外部调用
	 * 
	 * @param realname
	 */
	protected void deleteFile(String realname) {
		File f = new File(b.projectFile.getParent() + "\\Files\\" + realname);
		if (f.exists()) {
			if (b.getCurrentEditFile() != null)
				if (b.getCurrentEditFile().equals(f)) {
					b.disposeTextArea();
					b.setCurrentEditFile(null);
				}
			deleteKeyFromFileInfo(realname, true);
			b.fileindex.remove(realname);
			saveFileindex();
			b.recycle.remove(realname);
			f.delete();
			b.projectProperties.setProperty("fileCount", String.valueOf(b.FileCountOfProject));
			saveProjectCFG();
			saveRecycleCFG();
		}
	}

	/**
	 * 将垃圾箱的文件移到草稿箱,此方法可供外部调用
	 */
	public void restoreFile() {
		TreeItem[] tis = b.tree.getSelection();
		for (TreeItem ti : tis) {
			if (!ti.equals(b.draft) && !ti.equals(b.treeItem_1)) {
				if (!ti.getParentItem().equals(b.draft)) {
					restoreFile((String) ti.getData("realname"));
					ti.dispose();
				}
			}
		}
	}

	/**
	 * 将指定的文件从垃圾箱移到草稿箱
	 * 
	 * @param realname
	 */
	void restoreFile(String realname) {
		b.recycle.remove(realname);
		TreeItem ti = b.getDocTreeItem(b.draft, b.fileInfo.getProperty(realname, realname));
		ti.setData("realname", realname);
		saveRecycleCFG();
	}

	/**
	 * 删除fileinfo里的条目，该方法会自动删除与给定的realname所关联的所有条目
	 * 
	 * @param realname
	 * @param saveCFG
	 */
	protected void deleteKeyFromFileInfo(String realname, boolean saveCFG) {
		b.fileInfo.remove(realname);
		b.fileInfo.remove(realname + "TopPixel");
		b.fileInfo.remove(realname + "CaretOffset");

		if (saveCFG)
			saveFileInfoCFG();
	}

	/**
	 * 重命名文档
	 * 
	 * @param realname文档的真实名称,例如1.black
	 * @param newname文档在项目树里显示的新名称
	 */
	void rename(String realname, String newname) {
		b.fileInfo.setProperty(realname, newname);
		saveFileInfoCFG();
		b.applySetting();
	}

	protected void saveFileInfoCFG() {
		File fileinfoFile = new File(b.projectFile.getParent() + "\\Settings\\fileinfo");
		cfg_read_write.cfg_write(b.fileInfo, fileinfoFile);
	}

	protected void saveRecycleCFG() {
		File recyclefile = new File(b.projectFile.getParent() + "\\Settings\\recycle");
		cfg_read_write.cfg_write(b.recycle, recyclefile);
	}

	protected void saveProjectCFG() {
		cfg_read_write.cfg_write(b.projectProperties, b.projectFile);
	}

	protected void saveFileindex() {
		if (b.fileindex != null) {
			File fileindexfile = new File(b.projectFile.getParent() + "\\Settings\\fileindex.blaobj");
			ioThread io = new ioThread(b, b.fileindex, fileindexfile, 1);
			b.getDisplay().syncExec(io);
		}
	}

	/**
	 * 获取当前编辑的文档在项目树上显示的名称
	 * 
	 * @return项目树上显示的名称
	 */
	public String getShowNameByTreeSelection() {
		if (b.tree.getSelectionCount() == 1) {
			if (!b.tree.getSelection()[0].equals(b.draft) && !b.tree.getSelection()[0].equals(b.treeItem_1)) {
				return b.tree.getSelection()[0].getText();
			}
		}
		return "";
	}

	// public void is
	/**
	 * 设置备份目录
	 */
	public boolean setBackupDir() {
		DirectoryDialog dd = null;
		if (!isWritingView())
			dd = new DirectoryDialog(b);
		else
			dd = new DirectoryDialog(b.wv);

		dd.setText("设置备份目录");

		String path = dd.open();
		if (path != null) {
			b.setAppProperties("BackupDir", path);
			return true;
		} else
			return false;
	}

	/**
	 * 获取备份目录
	 * 
	 * @return
	 */
	public String getBackupDir() {
		String backupdir = b.getAppProperties("BackupDir");
		return backupdir;
	}

	/**
	 * 开始备份
	 */
	public void startBackup() {
		String backupDir = getBackupDir();
		String sourceDir = b.projectFile.getParent();
		if (backupDir != null && new File(backupDir).exists()) {
			String projectName = b.projectFile.getParentFile().getName();
			File backupfile = new File(backupDir + "\\" + projectName);
			if (backupfile.exists()) {
				getMessageBox("备份信息", "发现以前的备份文件，将删除旧的备份文件");
				fileTool.deleteDir(backupfile.toString());
			}
			if (fileTool.copy(sourceDir, backupDir)) {
				getMessageBox("备份信息", "备份成功！");
			} else
				getMessageBox("备份信息", "备份失败！\n" + "复制文件时出错！");
			showinExplorer(backupfile.toString(), true);
		} else {
			if (setBackupDir())
				startBackup();
		}
	}

	/**
	 * 向编辑器插入文本 此方法会自动处理光标偏移量 并会判断程序是否存在编辑器 插入文本后光标会自动移至插入的文本后
	 * 
	 * @param text
	 */
	public void insertText(String text, StyledText st) {
		if (st != null) {
			int caretoffset = st.getCaretOffset();
			st.insert(text);
			st.setCaretOffset(caretoffset + text.length());
		}
	}

	public void insertTextAndSelsction(String text, StyledText st) {
		insertText(text, st);
		st.setSelection(st.getCaretOffset() - text.length(), st.getCaretOffset());
	}

	/**
	 * 打开新建/打开项目对话框
	 */
	public void openNewProjectDialog() {
		newProject np = new newProject(b, SWT.None);
		boolean result = (boolean) np.open();
		if (!result) {
			if (b.projectFile == null)
				b.dispose();
		}
	}

	public void clearRecycle() {
		Enumeration<Object> enu = b.recycle.keys();
		if (enu.hasMoreElements()) {
			MessageBox mb = new MessageBox(b, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
			mb.setMessage("确认要清空垃圾箱？\n垃圾箱内的所有文件都将被删除");
			int i = mb.open();
			if (i == SWT.OK && b.recycle != null) {
				while (enu.hasMoreElements()) {
					String s = (String) enu.nextElement();
					File f = new File(b.projectFile.getParent() + "\\Files\\" + s);
					if (f.exists()) {
						if (b.getCurrentEditFile() != null && b.getCurrentEditFile().equals(f))
							b.closeCurrentFile(false);
						f.delete();
						b.fileindex.remove(f.getName());
						deleteKeyFromFileInfo(f.getName(), false);
					}
				}
				b.projectProperties.setProperty("fileCount", String.valueOf(b.FileCountOfProject));
				b.recycle.clear();
				b.listProjectFile();
				saveRecycleCFG();
				saveProjectCFG();
				saveFileInfoCFG();
				saveFileindex();
			}
		}
	}

	public void deleteSpaceAtLineEnd() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.text.getLineCount(); i++) {
			String line = b.text.getLine(i);
			if (line.length() > 1) {
				// 删除行开头的空格
				if (line.charAt(0) == ' ') {
					line = line.substring(1, line.length());
				}
				// 删除行末尾的空格
				char lastSecChar = line.charAt(line.length() - 2);
				if (cheakDocument.cheak(lastSecChar)) {
					char lastChar = line.charAt(line.length() - 1);
					if (lastChar == ' ') {
						line = line.substring(0, line.length() - 1);
					}
				}
			}
			sb.append(line + "\n");

		}
		b.text.setText(sb.toString());
	}

	/**
	 * 用于打开项目外的文件
	 */
	public void openFileWithoutProject() {
		FileDialog fd = getFileDialog("打开文件", "", b, SWT.OPEN, new String[] { "*.black" });
		String file = fd.getFileName();
		if (!file.equals("")) {
			File filepath = new File(fd.getFilterPath() + "\\" + file);
			if (filepath.exists()) {
				// 准备打开的文件位于项目路径内
				if (filepath.getParent().equals(b.projectFile.getParent() + "\\Files")) {
					openFile(filepath, true);
				} else {// 当前所编辑的是项目内的文件
					if (b.getCurrentEditFile() != null) {
						if (b.getCurrentEditFile().getParent().equals(b.projectFile.getParent() + "\\Files")) {
							b.closeCurrentFile(true);
						} else
							b.closeCurrentFile(false);
					}

					if (filepath.getName().lastIndexOf(".black") != -1) {
						System.out.println(filepath);
						ioThread io = new ioThread(b);
						b.createTextArea(new TextViewerUndoManager(100));
						IDocument doc = io.readBlackFile(filepath, b.tv);
						b.setCurrentEditFile(filepath);
						b.applySetting();
					}
				}

			}
		}
	}

	public void getTitleList() {
		if (b.text == null)
			return;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.text.getLineCount(); i++) {
			String line = b.text.getLine(i);
			if (cheakDocument.cheakString(line))
				sb.append(line + "\n");
		}
		bMessageBox bes = new bMessageBox(b, SWT.None, false) {

			@Override
			public void saveAction() {
				// TODO Auto-generated method stub

			}
		};
		bes.setTextFontinfo(12, SWT.ITALIC);
		bes.setText(sb.toString());
		bes.setTitle("标题列表");
		bes.text.addCaretListener(new CaretListener() {

			@Override
			public void caretMoved(CaretEvent arg0) {
				// TODO Auto-generated method stub
				StyledText st = bes.text;
				int index = st.getLineAtOffset(st.getCaretOffset());
				String str = st.getLine(index);
				String text = b.text.getText();
				int indexoftext = text.indexOf(str);
				b.text.setTopIndex(b.text.getLineAtOffset(indexoftext));
			}
		});
		bes.open();
	}

	public void importFiles(boolean onlytext, String textfileencode) {
		TextViewer tv = new TextViewer(b, SWT.None);
		tv.getTextWidget().setVisible(false);
		FileDialog fd = new FileDialog(b, SWT.MULTI);
		fd.setFilterExtensions(b.getIO().filterExtensionsForOpen);
		fd.setText("导入");
		fd.open();
		int filecount = 0;
		String[] filenames = fd.getFileNames();

		if (filenames.length > 0) {
			for (String filename : filenames) {
				File f = new File(fd.getFilterPath() + "\\" + filename);

				if (f.getName().matches(".*.docx")) {
					ioThread io = new ioThread(b);

					if (onlytext) {
						Document doc = new Document(io.readDocxFile(f));
						io.writeBlackFile(
								new File(b.projectFile.getParent() + "\\Files\\" + b.FileCountOfProject + ".black"),
								doc, null);
					} else {
						io.readDocxFile(f, tv.getTextWidget(), tv);
						io.writeBlackFile(
								new File(b.projectFile.getParent() + "\\Files\\" + b.FileCountOfProject + ".black"),
								tv.getDocument(), getAllStyleRange(tv.getTextWidget()));
					}
					b.fileindex.add(b.FileCountOfProject + ".black");
					b.fileInfo.setProperty(b.FileCountOfProject + ".black",
							stringAction.spiltFileName(f.getName(), ".docx"));
					saveFileInfoCFG();
					b.FileCountOfProject++;
					b.projectProperties.setProperty("fileCount", b.FileCountOfProject + "");
					saveProjectCFG();
					saveFileindex();
					filecount++;
					b.listProjectFile();
				} else if (f.getName().matches(".*.black")) {
					io.copyFile(f, new File(b.projectFile.getParent() + "\\Files\\" + b.FileCountOfProject + ".black"));
					b.fileindex.add(b.FileCountOfProject + ".black");
					b.fileInfo.setProperty(b.FileCountOfProject + ".black",
							stringAction.spiltFileName(f.getName(), ".black"));
					saveFileInfoCFG();
					b.FileCountOfProject++;
					b.projectProperties.setProperty("fileCount", b.FileCountOfProject + "");
					saveProjectCFG();
					saveFileindex();
					filecount++;
					b.listProjectFile();
				} else if (f.getName().matches(".*.txt")) {
					ioThread io = new ioThread(b);
					Document doc = new Document(io.readTextFile(f, textfileencode));
					io.writeBlackFile(
							new File(b.projectFile.getParent() + "\\Files\\" + b.FileCountOfProject + ".black"), doc,
							null);
					b.fileindex.add(b.FileCountOfProject + ".black");
					b.fileInfo.setProperty(b.FileCountOfProject + ".black",
							stringAction.spiltFileName(f.getName(), ".txt"));
					saveFileInfoCFG();
					b.FileCountOfProject++;
					b.projectProperties.setProperty("fileCount", b.FileCountOfProject + "");
					saveProjectCFG();
					saveFileindex();
					filecount++;
					b.listProjectFile();
				} else if (f.getName().matches(".*.doc")) {
					ioThread io = new ioThread(b);
					Document doc = new Document(io.readDocFile(f));
					io.writeBlackFile(
							new File(b.projectFile.getParent() + "\\Files\\" + b.FileCountOfProject + ".black"), doc,
							null);
					b.fileindex.add(b.FileCountOfProject + ".black");
					b.fileInfo.setProperty(b.FileCountOfProject + ".black",
							stringAction.spiltFileName(f.getName(), ".doc"));
					saveFileInfoCFG();
					b.FileCountOfProject++;
					b.projectProperties.setProperty("fileCount", b.FileCountOfProject + "");
					saveProjectCFG();
					saveFileindex();
					filecount++;
					b.listProjectFile();
				}
			}
		}
		if (filenames.length > 0)
			getMessageBox("", "导入了" + filecount + "个文件");
	}

	/**
	 * 获取一个文件对话框
	 * 
	 * @param text文件对话框的标题
	 * @param parent父Shel
	 * @param style样式，例如保存、打开、单选、多选
	 * @param extensions扩展名过滤器，如*.*.txt
	 * @return
	 */
	public FileDialog getFileDialog(String text, String filename, Shell parent, int style, String[] extensions) {
		FileDialog fd = new FileDialog(parent, style);
		fd.setText(text);
		fd.setFilterExtensions(extensions);
		fd.setFileName(filename);
		fd.open();
		return fd;
	}

	public void restartBlack() {
		blackMain.restart();
	}

	public boolean saveCurrentFileAsPDF() {
		boolean issave = false;
		if (b.text != null) {
			b.text.print();
			issave = true;
		}
		return issave;
	}

	public boolean saveCurrentFileAsTXT(File f, String encode) {
		boolean issave = false;
		if (b.text != null) {
			if (!encode.equals("")) {
				if (Charset.isSupported(encode)) {
					ioThread io = new ioThread(b);
					io.writeTextFile(f, b.text.getText(), encode);
					issave = true;
				} else
					getMessageBox("", "不被支持的字符集");
			} else {
				ioThread io = new ioThread(b);
				io.writeTextFile(f, b.text.getText(), Charset.defaultCharset().name());
				issave = true;
			}
		}
		return issave;
	}

	public void exportFiles(ArrayList<String> al, File outputfile) {
		if (b.fileindex != null) {
			TextViewer tv = new TextViewer(b, SWT.V_SCROLL | SWT.WRAP);
			StyledText styledText = tv.getTextWidget();
			// blackTextArea blackTextArea = new blackTextArea(styledText, b);
			styledText.setVisible(false);
			XWPFDocument doc = new XWPFDocument();
			ioThread io = new ioThread(b);
			Iterator<String> it = al.iterator();
			while (it.hasNext()) {
				String filename = it.next();
				File file = new File(b.projectFile.getParent() + "\\Files\\" + filename);
				if (file.exists()) {
					io.readBlackFile(file, tv);
					saveStylesToDocxFile(styledText, doc);
					// doc.createParagraph().set
				}
			}
			BufferedOutputStream bos = io.getBufferedFileOutputStream(outputfile);
			try {
				doc.write(bos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tv.getTextWidget().dispose();
			getMessageBox("", "导出了" + al.size() + "个文件");
		}
	}

	public void showCharCount() {
		if (b.text != null) {
			b.label_4.setText(getCharsCount() + "");
		}
	}

	/**
	 * 在项目里的所有文件中查找给定的关键词
	 */
	public void findInAllFiles(String word, boolean forwardSearch, boolean caseSensitive, boolean wholeWord,
			boolean showAll, boolean regularExpressions) {
		if (b.projectFile != null) {
			File dir = new File(b.projectFile.getParent() + "\\Files");
			String[] files = dir.list();
			for (String file : files) {
				File f = new File(dir + "\\" + file);
				ioThread io = new ioThread(b, f, 0, null, null, null);
				b.getDisplay().syncExec(io);
				ArrayList<IRegion> al = findwordInAllFiles(io.doc, word, forwardSearch, caseSensitive, wholeWord,
						regularExpressions);
				if (al != null && al.size() > 0) {
					TreeItem ti = b.addFindResultToTree(
							b.fileInfo.getProperty(f.getName(), f.getName()) + " " + al.size() + "个结果");
					ti.setData("file", f);
					ti.setData("iregions", al);
				}
			}
		}
	}

	public void replaceInAllFile(String word1, String word2, boolean forwardSearch, boolean caseSensitive,
			boolean wholeWord, boolean regularExpressions) {
		int i = 0;
		if (b.projectFile != null) {
			File dir = new File(b.projectFile.getParent() + "\\Files");
			String[] files = dir.list();
			TextViewer tv = new TextViewer(b, SWT.None);
			// 替换开始前保存并关闭当前正在使用的文件
			File oldfile = b.getCurrentEditFile();
			b.closeCurrentFile(true);
			for (String file : files) {
				File f = new File(dir + "\\" + file);
				ioThread io = new ioThread(b, f, 0, null, tv, null);
				b.getDisplay().syncExec(io);

				int a = b.blackTextArea.replace(io.doc, word1, word2, forwardSearch, caseSensitive, wholeWord, false,
						regularExpressions);
				i += a;
				if (a > 0) {
					ioThread ioi = new ioThread(b, f, 1, io.doc, null, getAllStyleRange(tv.getTextWidget()));
					b.getDisplay().asyncExec(ioi);
				}
			}
			tv.getTextWidget().dispose();
			getMessageBox("查找/替换消息", "替换完成！\n共替换了" + i + "处");
			openFile(oldfile, true);
		}
	}

	public void showFindResult(TreeItem ti) {
		if (ti != null) {
			File f = (File) ti.getData("file");
			if (f != null) {
				openFile(f, false);
				ArrayList<IRegion> al = (ArrayList<IRegion>) ti.getData("iregions");
				if (al != null) {
					Iterator<IRegion> it = al.iterator();
					while (it.hasNext()) {
						IRegion ir = it.next();
						b.blackTextArea.styles = new ArrayList<StyleRange[]>();
						StyleRange sr = new StyleRange(ir.getOffset(), ir.getLength(), b.color_textBackground,
								org.eclipse.wb.swt.SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
						StyleRange[] s = b.getEditer().getStyleRanges(ir.getOffset(), ir.getLength());
						if (s != null)
							b.blackTextArea.styles.add(s);
						b.getEditer().setStyleRange(sr);
					}
				}

			}
		}
	}

	public void hideFindResult() {
		if (b.findresult != null && !b.findresult.isDisposed()) {
			b.findresult.dispose();
		}
	}

	public ArrayList<IRegion> findwordInAllFiles(IDocument doc, String word, boolean forwardSearch,
			boolean caseSensitive, boolean wholeWord, boolean regularExpressions) {
		ArrayList<IRegion> al = null;
		if (doc != null) {
			al = new ArrayList<>();
			FindReplaceDocumentAdapter frda = new FindReplaceDocumentAdapter(doc);
			int start = 0;
			IRegion ir;
			try {
				while ((ir = frda.find(start, word, forwardSearch, caseSensitive, wholeWord,
						regularExpressions)) != null) {
					al.add(ir);
					start = ir.getOffset() + ir.getLength();
				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return al;
	}

	/**
	 * 打开文件 自动判断File是否为空且是否存在 并在文件打开后将编辑器视图重设到上一次打开时的位置
	 * 
	 * @param file
	 */
	public void openFile(File file, boolean resetView) {
		if (file != null) {
			if (file.exists()) {
				if (b.getCurrentEditFile() != null) {
					if (!b.getCurrentEditFile().equals(file)) {
						b.donotChangeTitleBar = true;
						b.closeCurrentFile(true);
						b.donotChangeTitleBar = false;
						b.getIO().open(file);
					}
				} else {
					b.getIO().open(file);
				}
				if (resetView) {
					b.resetStyledTextTopPixelAndCaretOffset(file.getName(), b.text);
					b.text.setFocus();
				}
			}
		}
	}

	public void changeColorForAllChildren(Color foreground, Color background, Composite com) {
		Control[] cons = com.getChildren();
		for (Control con : cons) {
			con.setForeground(foreground);
			con.setBackground(background);
			if (con.getClass().equals(Composite.class)) {
				changeColorForAllChildren(foreground, background, (Composite) con);
			}
		}
	}

	public void changeColorForAllChildren(Color foreground, Color background, Shell shell) {
		Control[] cons = shell.getChildren();
		shell.setBackground(background);
		shell.setForeground(foreground);
		for (Control con : cons) {
			con.setForeground(foreground);
			con.setBackground(background);
			if (con.getClass().equals(Composite.class)) {
				changeColorForAllChildren(foreground, background, (Composite) con);
			}
		}
		lastForeColor = foreground;
		lastBackColor = background;
	}

	public void changeColor(Color foreground, Color background) {
		if (b.wv != null && !b.wv.isDisposed())
			changeColorForAllChildren(foreground, background, b.wv);
		else
			changeColorForAllChildren(foreground, background, b);
		// b.color_label_exit = background;
	}

	void findinAllText(String str) {
		if (b.wv == null || b.wv.isDisposed()) {

			findi = new findinfo(b, b, SWT.None);
		} else {
			findi = new findinfo(b.wv, b, SWT.None);
		}
		initFindinfoResult();
		List<List<TextRegion>> lis = new ArrayList<List<TextRegion>>();
		if (b.text != null) {
			lis.add(cheakDocument.searchByLine(b.text.getText(), str,
					b.fileInfo.getProperty(b.getCurrentEditFile().getName(), b.getCurrentEditFile().getName())));
		}
		for (findinfo_ findi : findinfoResult.findin) {
			List<TextRegion> list = cheakDocument.searchByLine(findi.stringbuilder.toString(), str, findi.subname);
			lis.add(list);
		}
		int i = 0;
		Iterator<List<TextRegion>> it = lis.iterator();
		while (it.hasNext()) {
			List<TextRegion> lis_tr = it.next();
			Iterator<TextRegion> it_tr = lis_tr.iterator();
			while (it_tr.hasNext()) {
				TextRegion tr = it_tr.next();
				TreeItem ti = new TreeItem(findi.tree, SWT.None);
				ti.setText(tr.name + " - " + tr.text);
				ti.setData("textregion", tr);
				ti.setData("index", i);
				i++;
			}
		}
		if (findi.tree.getItemCount() > 0)
			findi.setVisible(true);
		else
			closeFindInfoDialog();
	}

	public void initFindinfoResult() {
		if (findinfoResult == null || !b.getCurrentEditFile().equals(findinfoResult.currenteditfile)) {
			findinfo_[] alltext = getAllTextFromProject(false, includeRecycle);
			findinfoResult = new findinfo_result(b.getCurrentEditFile(), alltext);
		}
	}

	/**
	 * 获取文件的真实名称
	 * 
	 * @param showname文件在项目面板里显示的名称
	 * @return文件的真实名称，文件的真实名称类似这种格式：1.black
	 */
	String getFileRealName(String showname) {
		Set<String> s = b.fileInfo.stringPropertyNames();
		Iterator<String> it = s.iterator();
		String realname = null;
		while (it.hasNext()) {
			String key = it.next();
			if (b.fileInfo.getProperty(key).equals(showname))
				realname = key;
		}
		return realname;
	}

	/**
	 * 根据给定的显示名称返回包含全路径的文件
	 * 
	 * @param showname文件在项目面板中的显示名称
	 * @return 包含磁盘路径的File文件
	 */
	File getRealFile(String showname) {
		File file = new File(b.projectFile.getParent() + "\\Files\\" + getFileRealName(showname));
		if (file.exists())
			return file;
		else
			return null;
	}

	/**
	 * 在windows系统资源管理器中显示当前所编辑的文件
	 */
	void showFileInExplorer() {
		if (b.getCurrentEditFile() != null)
			showinExplorer(b.getCurrentEditFile().toString(), true);
	}

	/**
	 * 在系统资源管理器显示指定的路径
	 * 
	 * @param path
	 *            文件或文件夹的路径
	 * @param select
	 *            是否在资源管理器中选择该文件或文件夹
	 */
	void showinExplorer(String path, boolean select) {
		String isselect = "";
		if (select)
			isselect = "/select, ";
		try {
			Runtime.getRuntime().exec("explorer " + isselect + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getShowNameByRealName(String realname) {
		return b.fileInfo.getProperty(realname, "");
	}

	/**
	 * 全屏写作视图
	 */
	void fullScreen() {
		// if (b.wv != null && !b.wv.isDisposed()) {
		// if (!b.wv.getFullScreen()) {
		// b.wv.exit();
		// if (b.getCurrentEditFile() != null && b.text != null) {
		// b.wv = new writingView(b, SWT.NO_TRIM);
		// b.wv.setFullScreen(true);
		// }
		// } else {
		// b.wv.exit();
		// b.ba.openWritingView();
		// }
		// }
	}

	/**
	 * 暗色
	 */
	void drak() {

	}

	void setDocumentTitleBySelectionText() {
		if (!b.text.getSelectionText().equals("")) {
			String oldname = b.getCurrentEditFile().getName();
			b.ba.rename(oldname, b.text.getSelectionText());
			b.listProjectFile();
		}
	}

	void readMarkFile() {
		File markfile = getRealFile("预定义");
		if (markfile != null && markfile.exists()) {
			ioThread io = new ioThread(b);
			IDocument doc = io.readBlackFile(markfile, null);
			marktext = doc.get();
			File statfile = new File(b.projectFile.getParentFile().getAbsolutePath()+"\\Settings\\markstat");
			if(statfile.exists()){
				markstat = (ArrayList<markstat>)io.readObjFile(statfile);
			}
		} else
			b.log.appendLog("预定义文件不存在！", null, false);
	}

	void writeToMarkFile() {
		File markfile = getRealFile("预定义");
		if (markfile != null && markfile.exists()) {
			ioThread io = new ioThread(b);
			if(marktextIsChanged){
				Document docu = new Document();
				docu.set(marktext);
				io.writeBlackFile(markfile, docu, null);
				marktextIsChanged = false;
			}
			File statfile = new File(b.projectFile.getParentFile().getAbsolutePath()+"\\Settings\\markstat");
			if(!statfile.exists()){
				if(!statfile.getParentFile().exists()) statfile.getParentFile().mkdir();
				try {
					statfile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					getMessageBox("保存调频数据", "保存预定义文件调频数据时出错！");
				}
			}
			if(markstat.size() > 0 && markstatIsChanged){
				io.writeObjFile(statfile, markstat);
			}
			
		}
	}

	public void editMarkFile() {
		File file = getRealFile("预定义");
		if (file != null && !b.getCurrentEditFile().equals(file)) {
			bMessageBox bmss = null;
			bmss = new bMessageBox(b, SWT.NONE, true) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void saveAction() {
					// TODO Auto-generated method stub
					changeMarktext(this.getText());
					b.ba.getMarkFileText();
				}
			};
			bmss.setTitle("编辑预定义文件");
			bmss.setText(marktext);
			bmss.open();
		}
	}

	void changeMarktext(String newtext) {
		marktext = newtext.replace("\n\n", "\n");
		marktextIsChanged = true;
	}

	/**
	 * 将给定的文本写入预定义文件
	 */
	void addTextToMarkFile(String str) {
		// 如果预定义文件中已存在给定的字符串
		if (marktext.indexOf(str) != -1) {
			changeMarktext(marktext.replace(str, ""));
			getMessageBox("从预定义文件中删除词汇", "已将所选词汇从预定义文件中删除！");
		} else {
			changeMarktext(marktext + "\n" + str);
			getMessageBox("添加词汇到预定义文件", "已将所选词汇加入预定义文件！");
		}
		getMarkFileText();
	}

	/**
	 * 为预定义文件建立索引
	 */
	void getMarkFileText() {
		List<TextRegion> list = new ArrayList<TextRegion>();
		if (marktext == null)
			return;
		List<String> lis = cheakDocument.getAllLine(marktext);
		for (String st : lis) {
			TextRegion tr = new TextRegion(st, 0, 0);
			list.add(tr);
		}

		markTextData = list;
		//校验预定义频率arraylist里的条目是否在预定义文件中存在，如果不存在则将条目删除
		ishasInMarkdata();
	}
	/**
	 * 检查预定义数据里是否存在调频数据里的条目，如果不存在则将调频数据里的条目删除
	 */
	void ishasInMarkdata(){
		Iterator<markstat> it_stat = markstat.iterator();
		while(it_stat.hasNext()){
			String str = it_stat.next().text;
			boolean ishas = false;
			Iterator<TextRegion> it_mark = markTextData.iterator();
			while(it_mark.hasNext()){
				String text = it_mark.next().text;
				if(text.equals(str)){
					ishas = true;
					break;
				}
			}
			if(!ishas){
				it_stat.remove();
			}
		}
		
		
	}
	/**
	 * 判断给定的字符串是否在频率统计arraylist中存在
	 * @param text
	 */
	hasinfo markstatIshas(String text){		
		boolean ishas = false;
		int index = 0;		
		for(int i=0;i<markstat.size();i++){
			if(text.equals(markstat.get(i).text)){
				ishas = true;
				index = i;
				break;
			}
		}
		return new hasinfo(ishas,index);
	}
	/**
	 * 将一个已存在的条目置顶（放到markstat arraylist的末尾）
	 * @param text
	 */
	void setTopOnMarkstat(String text){
		for(int i=0;i<markstat.size();i++){
			if(markstat.get(i).text.equals(text)){
				int stat = markstat.get(i).count;
				markstat.remove(i);
				markstat.add(new markstat(text, stat));
			}
		}
	}
	/**
	 * 依照频率对统计arraylist中的条目进行排序
	 */
	void setindexOfMarkstat(){
		markstatIsChanged = true;
		ArrayList<markstat> al = new ArrayList<>();
		while(markstat.size() > 0){
			int maxindex = 0;
			int max = 0;
			for(int i=0;i<markstat.size();i++){
				if(markstat.get(i).count > max){
					max = markstat.get(i).count;
					maxindex = i;
				}
			}
			
			al.add(markstat.get(maxindex));
			markstat.remove(maxindex);
			

		}
		markstat = al;
	}
	List<TextRegion> setindexOfMarkstatBy(){
		if(b.text.getCaretOffset() > 0){
			String text = b.text.getText(0, b.text.getCaretOffset()-1);
			List<TextRegion> al = markTextData;
			List<TextRegion> al_new = new ArrayList<TextRegion>();
			while(al.size() > 0){
				int max = 0;
				int maxindex = 0;
				for(int i=0;i<al.size();i++){
					String str = al.get(i).text;
					int index = text.lastIndexOf(str);
					if(index > max){
						max = index;
						maxindex = i;
					}
				}
				al_new.add(al.get(maxindex));
				al.remove(maxindex);
			}
			return al_new;
		}else return null;
	}
	void findinMark() {
		if (marktext == null || marktext.equals(""))
			readMarkFile();
		if (markTextData == null || markTextData.size() == 0)
			getMarkFileText();
		if (markTextData != null && markTextData.size() > 0) {
			if (b.wv == null || b.wv.isDisposed())
				findi = new findinfo(b, b, SWT.None);
			else
				findi = new findinfo(b.wv, b, SWT.None);
			findi.insertAction = findi.none;
			findi.drawstrAction(findi.insertAction);
			findi.tree.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					TreeItem ti = (TreeItem)arg0.item;
					ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			//对预定义文件中的条目的顺序进行分组
			int index = 0;
			List<TextRegion> tr_no = new ArrayList<TextRegion>();// 完全不匹配的条目
			List<TextRegion> tr_line = new ArrayList<>();// 当前段落中包含的条目
			List<TextRegion> tr_doc = new ArrayList<>();// 当前文档中包含的条目

			String lastchar = null;
			if (b.text.getCaretOffset() > 0)
				lastchar = b.text.getText(b.text.getCaretOffset() - 1, b.text.getCaretOffset() - 1);
			String line = b.text.getLine(b.text.getLineAtOffset(b.text.getCaretOffset()));
			String doc = b.text.getText();
			hasinfo info = null;
			List<TextRegion> al = setindexOfMarkstatBy();
			Iterator<TextRegion> it_tr = null;
			if(al != null) it_tr = al.iterator();
			else it_tr = markTextData.iterator();
			
			while (it_tr.hasNext()) {
				TextRegion tr = it_tr.next();
				// 如果当前预定义条目中包含编辑器光标前一个字符，就将其放到前面
				if (lastchar != null && cheakDocument.findString(tr.text, lastchar)) {
					TreeItem ti = new TreeItem(findi.tree, SWT.None);
					// ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					info = markstatIshas(tr.text);
					if(!info.ishas)ti.setText(tr.text+" ("+lastchar+")");
					else{
						markstat stat = markstat.get(info.index);
						if(info.index == markstat.size()-1)
							ti.setText(tr.text+" ("+lastchar+")"+"(上次所选)"+"("+stat.count+")");
						else ti.setText(tr.text+" ("+lastchar+")"+"("+stat.count+")");
						stat.visible = false;
					}
					ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					ti.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
					ti.setData("textregion", tr);
					ti.setData("index", index);
					index++;
				}else if((info=markstatIshas(tr.text)).ishas){
					markstat.get(info.index).visible = true;
				}else if (cheakDocument.findString(line, tr.text)) {
					tr_line.add(tr);
				} else if (cheakDocument.findString(doc, tr.text)) {
					tr_doc.add(tr);
				} else {
					tr_no.add(tr);
				}
			}
			
			//将频率arraylist中最后的条目最先呈现出来
			if(markstat.size() > 0){
				markstat stat = markstat.get(markstat.size()-1);
				if(stat.visible){
					TextRegion trstat = new TextRegion(stat.text, 0, 0);
					TreeItem ti = new TreeItem(findi.tree, SWT.None);
					if(stat.count > 1 && stat.count <= 5){
						ti.setBackground(SWTResourceManager.getColor(250,254,90));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					else if(stat.count > 5 && stat.count <= 10){
						ti.setBackground(SWTResourceManager.getColor(249,254,56));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					else if(stat.count > 10 && stat.count <= 20){
						ti.setBackground(SWTResourceManager.getColor(248,254,10));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					else if(stat.count > 20){
						ti.setBackground(SWTResourceManager.getColor(191,197,1));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}else{
						ti.setBackground(SWTResourceManager.getColor(251,254,126));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					ti.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
					ti.setText(trstat.text+" (上次所选)"+"("+markstat.get(markstat.size()-1).count+")");
					ti.setData("textregion", trstat);
					ti.setData("index", index);
					index++;
				}	
			}
			
			for(int i=0;i<markstat.size()-1;i++){
				markstat stat = markstat.get(i);
				if(stat.visible){
					TextRegion trstat = new TextRegion(markstat.get(i).text, 0, 0);
					TreeItem ti = new TreeItem(findi.tree, SWT.None);
					if(stat.count > 1 && stat.count <= 5){
						ti.setBackground(SWTResourceManager.getColor(250,254,90));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					else if(stat.count > 5 && stat.count <= 10){
						ti.setBackground(SWTResourceManager.getColor(249,254,56));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					else if(stat.count > 10 && stat.count <= 20){
						ti.setBackground(SWTResourceManager.getColor(248,254,10));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					else if(stat.count > 20){
						ti.setBackground(SWTResourceManager.getColor(191,197,1));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}else{
						ti.setBackground(SWTResourceManager.getColor(251,254,126));
						ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					}
					ti.setFont(SWTResourceManager.getFont("微软雅黑", 11, SWT.BOLD));
					ti.setText(trstat.text+"("+markstat.get(i).count+")");
					ti.setData("textregion", trstat);
					ti.setData("index", index);
					index++;
				}
			}
			
			Iterator<TextRegion> it_line = tr_line.iterator();
			while(it_line.hasNext()){
				TextRegion trline = it_line.next();
				TreeItem ti = new TreeItem(findi.tree, SWT.None);
				ti.setFont(SWTResourceManager.getFont("微软雅黑", 10, SWT.BOLD));
				ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
				ti.setText(trline.text+" (当前段落)");
				ti.setData("textregion", trline);
				ti.setData("index", index);
				index++;
			}
			
			Iterator<TextRegion> it_doc = tr_doc.iterator();
			while(it_doc.hasNext()){
				TextRegion trdoc = it_doc.next();
				TreeItem ti = new TreeItem(findi.tree, SWT.None);
				ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
				ti.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.BOLD));
				ti.setText(trdoc.text+" (当前文档)");
				ti.setData("textregion", trdoc);
				ti.setData("index", index);
				index++;
			}
			
			Iterator<TextRegion> it_no = tr_no.iterator();
			while (it_no.hasNext()) {
				TextRegion trno = it_no.next();
				TreeItem ti = new TreeItem(findi.tree, SWT.None);
				// ti.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				ti.setText(trno.text);
				ti.setData("textregion", trno);
				ti.setData("index", index);
				index++;
			}
			findi.findInMark = true;
			findi.setVisible(true);
		}
	}
	public void saveGitInfo(String host,String username,String password){
		b.projectProperties.setProperty("GitHost", host);
		b.projectProperties.setProperty("GitUsername", username);
		b.projectProperties.setProperty("GitPassword", password);
		saveProjectCFG();
	}
	/**
	 * 为当前编辑的项目设置git目录
	 */
	public void setGitRespositoryPath(){
		try {
			gitTool.createGitRepository(b.projectFile.getParent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch(IllegalStateException e){
			addlogs(e.getMessage());
		}
		try {
			gitTool.createNewBranch(b.projectProperties.getProperty("projectName"), b.projectFile.getParent());
		} catch (RefAlreadyExistsException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
			addlogs(e.getMessage());
		} catch (RefNotFoundException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (InvalidRefNameException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		}
	}
	public void changeBranch(){
		try {
			gitTool.changeBranch( b.projectFile.getParent(), b.projectProperties.getProperty("projectName"));
		} catch (RefAlreadyExistsException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (RefNotFoundException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (InvalidRefNameException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (CheckoutConflictException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		}
	}
	public List<DiffEntry> commit(String message,boolean quiet){
		setGitRespositoryPath();
		changeBranch();
		String mess = null;
		List<DiffEntry> commit = null;
		if(message == null)
			mess = time.getCurrentDate("-")+"("+time.getCurrentTime().replace(":", "点")+"分)"+"项目备份";
		else mess = message;
		try {
			commit = gitTool.commit(b.projectFile.getParent(), mess);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		}
		if(!quiet && commit != null){
			StringBuilder sb = new StringBuilder();
			for(DiffEntry diff:commit){
				sb.append(diff.getNewPath()+"\n");
			}
			getBMessageBox("更改的文件", sb.toString());
		}
		return commit;
	}
	public boolean gitSetUp(){
		String host = b.projectProperties.getProperty("GitHost");
		String username = b.projectProperties.getProperty("GitUsername");
		String password = b.projectProperties.getProperty("GitPassword");
		if(host == null || username == null || password == null) return false;
		else return true;
	}
	public Iterable<PushResult> push(boolean quiet){
		Iterable<PushResult> pushToRemote = null;
		String host = b.projectProperties.getProperty("GitHost");
		String username = b.projectProperties.getProperty("GitUsername");
		String password = b.projectProperties.getProperty("GitPassword");
		if(host == null || username == null || password == null) return pushToRemote;
		try {
			pushToRemote = gitTool.pushToRemote(b.projectFile.getParent(),host, username, password, true);
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		}
		if(!quiet && pushToRemote != null){
			StringBuilder sb = new StringBuilder();
			for(PushResult pr:pushToRemote){
				Collection<RemoteRefUpdate> remoteUpdates = pr.getRemoteUpdates();
				for(RemoteRefUpdate rru:remoteUpdates){
					sb.append(rru+"\n");
				}
			}
			getBMessageBox("上传结果", sb.toString());
		}	
		return pushToRemote;
	}
	public Collection<Ref> getAllBranchFromRemote(){
		String host = b.projectProperties.getProperty("GitHost");
		String username = b.projectProperties.getProperty("GitUsername");
		String password = b.projectProperties.getProperty("GitPassword");
		if(host == null || username == null || password == null) return null;
		Collection<Ref> allBranchFromRemote = null;
		try {
			 allBranchFromRemote = gitTool.getAllBranchFromRemote(host, username, password);
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		}
		return allBranchFromRemote;
	}
	public void addlogs(String log){
		logsmessage.append("["+time.getCurrentDate("-")+" "+time.getCurrentTime()+"] "+log+"\n");
	}
	public void deleteBranchFromRemote(String branchName,boolean local){
		String host = b.projectProperties.getProperty("GitHost");
		String username = b.projectProperties.getProperty("GitUsername");
		String password = b.projectProperties.getProperty("GitPassword");
		if(host == null || username == null || password == null) return;
		
		try {
			gitTool.deleteBranchFromRemote(b.projectFile.getParent(), host, branchName, local,username, password);
		} catch (InvalidRemoteException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		}
	}
	public void copyBranchFromeRemote(String dirpath,String branchName,Collection<String> branchs, boolean all){
		String host = b.projectProperties.getProperty("GitHost");
		String username = b.projectProperties.getProperty("GitUsername");
		String password = b.projectProperties.getProperty("GitPassword");
		if(host == null || username == null || password == null) return;
		
		try {
			gitTool.CloneFromRemote(dirpath, host, all, branchs,branchName, username, password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
		} catch (JGitInternalException e){
			addlogs(e.getMessage());
		}
	}
	public void createBranch(String branchName){
		String host = b.projectProperties.getProperty("GitHost");
		String username = b.projectProperties.getProperty("GitUsername");
		String password = b.projectProperties.getProperty("GitPassword");
		if(host == null || username == null || password == null) return;
		try {
			gitTool.createNewBranch(branchName, b.projectFile.getParent());
		} catch (RefAlreadyExistsException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
			return;
		} catch (RefNotFoundException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
			return;
		} catch (InvalidRefNameException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
			return;
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			addlogs(e.getMessage());
			return;
		}
		push(true);
	}
	public void gitWorking(){
		String host = b.projectProperties.getProperty("GitHost");
		String username = b.projectProperties.getProperty("GitUsername");
		String password = b.projectProperties.getProperty("GitPassword");
		if(host == null || username == null || password == null) return;
		StringBuilder sb = new StringBuilder();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				b.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress showPro = new showProgress(b){
							@Override
							void actionInOtherThread() {
								// TODO Auto-generated method stub
								getBMessageBox("同步结果", sb.toString());
							}

							@Override
							void actionWhenOKButtonSelected() {
								// TODO Auto-generated method stub
							}
						};
						showPro.setTitle("同步至远程仓库");
						showPro.open();
					}
				});
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<DiffEntry> commit = commit(null,true);
				progressMessage = "已将项目提交至本地仓库";
				ProgressValue = 10;
				progressMessage = "正在将本地仓库更改同步到远程仓库";
				ProgressValue = 30;
				Iterable<PushResult> push = push(true);
				progressMessage = "已将更改同步至远程仓库，开始加载同步结果...";
				ProgressValue = 70;
				sb.append("项目中发生更改的文件：\n");
				if(commit != null)
					for(DiffEntry diff:commit){
						sb.append(diff.getNewPath()+"\n");
					}
				progressMessage = "已获取文件更改信息，开始获取服务器返回结果...";
				ProgressValue = 80;
				sb.append("-------------------\n远程仓库返回值：\n");
				ArrayList<String> al = new ArrayList<>();
				int count = 0;
				if(push != null)
					for(PushResult pr:push){
						Collection<RemoteRefUpdate> remoteUpdates = pr.getRemoteUpdates();
						for(RemoteRefUpdate rru:remoteUpdates){
							sb.append("远程分支名： "+rru.getRemoteName()+"\n");
							sb.append("对象ID： "+rru.getNewObjectId()+"\n");
							sb.append("更改备注： "+rru.getMessage()+"\n");
							sb.append("是否删除： "+rru.isDelete()+"\n");
							sb.append("强制更新： "+rru.isForceUpdate()+"\n");
							sb.append("状态： "+rru.getStatus()+"\n");
							if(rru.getStatus().toString().equals("OK")){
								count++;
								al.add(rru.getRemoteName());
							}
						}
					}
				sb.append("\n================\n同步的分支：\n");
				for(String str:al){
					sb.append(str+"\n");
				}
				
				progressMessage = "同步成功！";
				ProgressValue = 100;
			}
		}).start();
	}
	
	public void findInfo() {
		if (b.text.getSelectionCount() == 0) {
			Runnable runn = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (getShowNameByRealName(b.currentEditFile.getName()).equals("预定义"))
						saveFile(false, false);
					findinMark();
				}
			};
			b.getDisplay().asyncExec(runn);
		} else {
			Runnable runn = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					findinAllText(b.text.getSelectionText());
				}
			};
			b.getDisplay().asyncExec(runn);
		}

	}


	public String[] getALLFonts() {
		FontData[] fonts = b.getDisplay().getFontList(null, true);
		List<String> asiafontslist = new ArrayList();
		List<String> fontslist = new ArrayList();
		for (int i = 0; i < fonts.length; i++) {
			String name = fonts[i].getName();
			boolean isasia = false;
			for (int x = 0; x < name.length(); x++) {
				char c = name.charAt(x);
				if (wordCountStat.isAsia(c)) {
					if (name.indexOf('@') == -1)
						asiafontslist.add(name);
					isasia = true;
					break;
				}
			}
			if (!isasia)
				fontslist.add(name);
		}
		String[] fontnames = new String[asiafontslist.size() + fontslist.size()];
		for (int i = 0; i < asiafontslist.size(); i++) {
			fontnames[i] = asiafontslist.get(i);
		}
		for (int i = 0; i < fontslist.size(); i++) {
			fontnames[i + asiafontslist.size()] = fontslist.get(i);
		}
		return fontnames;
	}

	public void openQtextedit() {
		b.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// QApplication.initialize(new String[]{});
				// testqt testtestqt = new testqt(b);
				// testtestqt.show();
				// QApplication.execStatic();
			}
		});
	}

	/**
	 * 将中文逗号替换句号
	 * 
	 * @param st
	 * @param p
	 *            鼠标指针在StyleText中的坐标
	 */
	public void replaceMode(StyledText st, Point p) {

		int offset = -1;
		try {
			offset = st.getOffsetAtLocation(p);
		} catch (IllegalArgumentException e) {

		}
		if (offset != -1 && offset != st.getCharCount()) {
			String text = st.getText(offset, offset);
			if (text.equals("，")) {
				st.replaceTextRange(offset, 1, "。");
				st.setSelection(offset, offset + 1);
			}
		}

	}
	// public void showMessage(String message) {
	// String text = b.getText();
	// b.setText(message);
	// autoDO a = new autoDO(2) {
	//
	// @Override
	// public void action() {
	// // TODO Auto-generated method stub
	// b.getDisplay().asyncExec(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// if (!b.isDisposed())
	// b.setText(text);
	// }
	// });
	// this.stop();
	// }
	// };
	// a.start();
	// }

	public void closeFindInfoDialog() {
		if (findi != null && !findi.isDisposed()) {
			findi.dispose();
			findi = null;
		}
	}

	/**
	 * 获取垃圾箱文件数
	 * 
	 * @return
	 */
	public int getRecycleFileCount() {
		int filecount = 0;
		Enumeration<Object> coll = b.recycle.keys();
		while (coll.hasMoreElements()) {
			String filename = (String) coll.nextElement();
			File f = new File(b.projectFile.getParent() + "\\Files\\" + filename);
			if (f.exists()) {
				filecount++;
			}
		}
		return filecount;
	}

	/**
	 * 获取项目里所有文件的文本
	 * 
	 * @return
	 */
	public findinfo_[] getAllTextFromProject(boolean isgetcurrenteditfile, boolean includerecycle) {
		findinfo_[] findin = null;
		if (b.projectFile != null) {
			File dir = new File(b.projectFile.getParent() + "\\Files");
			String[] files = dir.list();
			if (isgetcurrenteditfile && includerecycle)
				findin = new findinfo_[files.length];
			else if (!isgetcurrenteditfile && !includerecycle) {
				if (b.getCurrentEditFile() != null)
					findin = new findinfo_[files.length - 1 - getRecycleFileCount()];
				else
					findin = new findinfo_[files.length - getRecycleFileCount()];
			} else if (!isgetcurrenteditfile) {
				if (b.getCurrentEditFile() != null)
					findin = new findinfo_[files.length - 1];
				else
					findin = new findinfo_[files.length];
			} else {
				findin = new findinfo_[files.length - getRecycleFileCount()];
			}

			int i = 0;
			for (String file : files) {
				StringBuilder sb = new StringBuilder();
				File f = new File(dir + "\\" + file);
				if (b.getCurrentEditFile() != null) {
					if (!b.getCurrentEditFile().equals(f)) {
						String value = b.recycle.getProperty(f.getName());
						if (!includerecycle) {
							if (value == null) {
								ioThread io = new ioThread(b);
								sb.append(io.readBlackFile(f, null).get());
								findin[i] = new findinfo_(sb, b.fileInfo.getProperty(f.getName(), f.getName()));
								if (i < findin.length - 1)
									i++;
							}
						} else {
							ioThread io = new ioThread(b);
							sb.append(io.readBlackFile(f, null).get());
							findin[i] = new findinfo_(sb, b.fileInfo.getProperty(f.getName(), f.getName()));
							if (i < findin.length - 1)
								i++;
						}
					} else {
						if (isgetcurrenteditfile) {
							sb.append(b.text.getText());
							findin[i] = new findinfo_(sb, b.fileInfo.getProperty(f.getName(), f.getName()));
							if (i < findin.length - 1)
								i++;
						}
					}
				} else {
					ioThread io = new ioThread(b);
					sb.append(io.readBlackFile(f, null).get());
					i++;
					findin[i] = new findinfo_(sb, b.fileInfo.getProperty(f.getName(), f.getName()));
				}

				// if(!isgetcurrenteditfile){
				// if(b.getCurrentEditFile() != null){
				// if(!b.getCurrentEditFile().equals(f)){
				// if(i+1 < files.length-1)
				// i++;
				// }
				// }else{
				// if(i+1 < files.length-1)
				// i++;
				// }
				// }else{
				// if(i+1 < files.length)
				// i++;
				// }
			}
		}
		return findin;
	}

	public void howtoUseExpressions() {
		// ioThread io = new ioThread(b);

	}

	public void resetCaret(int style) {
		b.text.setCaret(new mycaret(b.text, style));
		if (style == SWT.VERTICAL)
			b.appProperties.setProperty("CaretV", "true");
		else
			b.appProperties.setProperty("CaretV", "false");
	}

	/**
	 * 将给定窗口的大小保存至black对象的app属性对象，该app属性对象会在black对象销毁前将内容写入磁盘
	 * 
	 * @param shell
	 */
	public void saveWindowSizeToAppProperties(Shell shell) {
		b.appProperties.setProperty(shell.getClass().getName() + "_windowSizeX", String.valueOf(shell.getSize().x));
		b.appProperties.setProperty(shell.getClass().getName() + "_windowSizeY", String.valueOf(shell.getSize().y));
	}

	/**
	 * 从black对象的app属性对象里为给定shell读取并应用窗口大小属性
	 * 
	 * @param shell要重设窗口大小的shell对象
	 * @param defaultsize
	 *            当black的app属性对象里没有该shell的窗口信息时所应用的缺省大小
	 * @return
	 */
	public void readWindowSizeFromAppProperties(Shell shell, Point defaultsize) {
		int x = Integer.valueOf(b.appProperties.getProperty(shell.getClass().getName() + "_windowSizeX",
				String.valueOf(defaultsize.x)));
		int y = Integer.valueOf(b.appProperties.getProperty(shell.getClass().getName() + "_windowSizeY",
				String.valueOf(defaultsize.y)));
		shell.setSize(x, y);
	}

	public void serializationBlack() {
		File serialFile = new File("./serializa/black");
		File serialDir = new File("./serializa");
		if (!serialDir.exists())
			serialDir.mkdir();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(serialFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(b);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void hideOrShowLeftPanel() {
		// if(b.appProperties.getProperty("LeftPanel_Show", "true"));

	}

	public void hideOrShowBottomPanel() {

	}

	public void changeWritingViewTextWidth(int width) {
		b.appProperties.setProperty("WritingView_TextX", String.valueOf(width));
		if (b.wv != null && !b.wv.isDisposed()) {
			b.wv.setStyledTextWidth(width);
		}
	}

	public int getChineseCharsCount() {
		return wordCountStat.chineseWordCount(b.getEditer());
	}

	/*
	 * 获取字符数，不计换行符和空格
	 */
	public int getCharsCount() {
		return getCharsCount(b.text.getText());
	}

	/**
	 * 获取给定文本内的字符数，不计换行符和空格
	 * 
	 * @param text
	 * @return
	 */
	public int getCharsCount(String text) {
		wordCountStat wcs = new wordCountStat();
		wcs.wordStat(text);
		return text.length() - wcs.enterCount - wcs.spaceCount;
	}

	// public int getCharsCount(){
	//
	// }
	public int getAllCharsCount() {
		return b.getEditer().getCharCount();
	}

	public void setSlider(Slider slider) {
		if (b.text != null && !b.text.isDisposed()) {
			sliderListener = new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					// TODO Auto-generated method stub
					if (slider != null && !slider.isDisposed()) {
						ScrollBar vb = b.text.getVerticalBar();
						slider.setValues(vb.getSelection(), vb.getMinimum(), vb.getMaximum(), vb.getThumb(),
								vb.getIncrement(), vb.getPageIncrement());
					}
				}
			};
			b.text.addListener(SWT.Paint, sliderListener);
		}
	}

	public void getFileInfo() {
		// System.out.println(b.fileindex.size());
		// Iterator<String> it = b.fileindex.iterator();
		// while(it.hasNext()){
		// String s = it.next();
		// System.out.println(s);
		// }

		String f = b.projectFile.getParent() + "\\Files\\";
		File filedir = new File(f);
		String[] files = filedir.list();
		StringBuilder sb = new StringBuilder();
		for (String s : files) {
			// sb.append(s +"\n");
			if (b.fileindex.indexOf(s) == -1) {
				sb.append(s);
			}
		}
		if (sb.length() == 0)
			getMessageBox("文件校验信息", "文件校验完成，未发现错误！");
		else
			getBMessageBox("文件校验信息", "以下文件在磁盘上存在，但未被程序读取：\n" + sb.toString());
	}

	/**
	 * 在给定的字符串列表ss中查找指定的字符串s
	 * 
	 * @param ss
	 * @param s
	 * @return存在则返回true
	 */
	public boolean vs(ArrayList<String> ss, String s) {
		boolean b = false;
		Iterator<String> it = ss.iterator();
		while (it.hasNext()) {
			String str = it.next();
			if (s.equals(str)) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * 对文档内容进行分词
	 * 
	 * @param br词性
	 */
	public void toAnalysis(String br, boolean showNatureStr) {
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		Result r = ToAnalysis.parse(b.text.getText());
		Iterator<Term> it = r.iterator();
		while (it.hasNext()) {
			Term t = it.next();
			if (br.equals("") || t.getNatureStr().equals(br)) {
				if (!b.ba.vs(list, t.getName()))
					if (showNatureStr)
						list.add(t.getName() + t.getNatureStr());
					else
						list.add(t.getName());
			}
		}
		Iterator<String> it_ = list.iterator();
		while (it_.hasNext()) {
			sb.append(it_.next() + "\n");
		}
		bMessageBox mes = new bMessageBox(b, SWT.None, true) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void saveAction() {
				// TODO Auto-generated method stub
				if (this.textChanged) {
					if (getShowNameByRealName(b.getCurrentEditFile().getName()).indexOf("_替换列表") == -1) {
						File f = getRealFile(getShowNameByRealName(b.getCurrentEditFile().getName()) + "_替换列表");
						ioThread io = new ioThread(b);
						if (f != null) {
							io.writeBlackFile(f, new Document(this.text.getText()), null);
						} else {
							addFileToProject(getShowNameByRealName(b.getCurrentEditFile().getName()) + "_替换列表");
							io.writeBlackFile(b.currentEditFile, new Document(this.text.getText()), null);
							b.getIO().open(b.currentEditFile);
						}
						this.textChanged = false;
					} else
						getMessageBox("替换文件信息", "编辑主文件时才能保存替换信息！");
				}
			}
		};
		mes.textChanged = true;
		mes.text.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if ((arg0.keyCode | arg0.stateMask) == (SWT.CONTROL | 's')) {
					if (mes.textChanged)
						mes.saveAction();
				} else if ((arg0.keyCode | arg0.stateMask) == (SWT.CONTROL | '1')) {

					deletline(mes.text);
				} else if ((arg0.keyCode | arg0.stateMask) == (SWT.CONTROL | '`')) {
					StyledText st = mes.text;
					int lineindex = st.getLineAtOffset(st.getCaretOffset());
					int lineoffset = st.getOffsetAtLine(lineindex);
					String str = st.getLine(lineindex);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < st.getLineCount(); i++) {
						String linestr = st.getLine(i);
						if (linestr.length() > str.length()) {
							if (linestr.indexOf(str) == -1)
								sb.append(linestr + "\n");
						} else
							sb.append(linestr + "\n");
					}
					int caret = st.getCaretOffset();
					st.setText(sb.toString());
					st.setCaretOffset(caret);
				} else if ((arg0.keyCode | arg0.stateMask) == (SWT.CONTROL | '3')) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mes.text.getLineCount(); i++) {
						String linestr = mes.text.getLine(i);
						if (linestr.indexOf("#") == -1)
							sb.append(linestr + "#\n");
						else {
							linestr = linestr.replace("#", "");
							sb.append(linestr + "\n");
						}
					}
					int caret = mes.text.getCaretOffset();
					mes.text.setText(sb.toString());
					mes.text.setCaretOffset(caret);
				}
			}
		});
		mes.text.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub\
				if (mes.text.getSelectionCount() != 0) {
					String text = b.text.getText();
					int index = text.indexOf(mes.text.getSelectionText());
					b.text.setSelection(index, index + mes.text.getSelectionText().length());
					mes.shell.setAlpha(150);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		mes.text.addCaretListener(new CaretListener() {

			@Override
			public void caretMoved(CaretEvent arg0) {
				// TODO Auto-generated method stub
				StyledText st = mes.text;
				int index = st.getLineAtOffset(st.getCaretOffset());
				String str = st.getLine(index);
				String text = b.text.getText();
				int indexoftext = text.indexOf(str);
				b.text.setSelection(indexoftext, indexoftext + str.length());
			}
		});
		changeColorForAllChildren(b.color_white, b.color_black, mes.shell);
		mes.setText(sb.toString());
		mes.setTitle("编辑替换列表");
		mes.shell.setAlpha(150);

		mes.open();
	}

	public void deletline(StyledText st) {
		int lineindex = st.getLineAtOffset(st.getCaretOffset());
		int lineoffset = st.getOffsetAtLine(lineindex);
		String str = st.getLine(lineindex);
		if (str.length() > 0) {
			if (lineoffset - 1 >= 0 && lineoffset + str.length() + 1 < st.getCharCount())
				st.replaceTextRange(lineoffset - 1, str.length() + 1, "");
			else if (lineoffset - 1 < 0 && lineoffset + str.length() + 1 < st.getCharCount())
				st.replaceTextRange(lineoffset, str.length() + 1, "");
			else if (lineoffset - 1 >= 0 && lineoffset + str.length() + 1 >= st.getCharCount())
				st.replaceTextRange(lineoffset - 1, str.length(), "");
			else
				st.replaceTextRange(lineoffset, str.length(), "");
		}
	}

	/**
	 * 从快速替换列表里查找错误
	 * 
	 * @param st
	 */
	public void findErrorOnEditor(StyledText st) {
		List<String> list = cheakDocument.getAllLine(st.getText());
		// int lineindex = st.getLineAtOffset(st.getCaretOffset());
		// String linestr = st.getLine(lineindex);
		// String[] subinput = cheakDocument.subString(linestr, "#");
		// if(subinput != null){
		// if(st.getText().indexOf(subinput[1]) < st.getOffsetAtLine(lineindex)
		// &&
		// st.getText().indexOf(subinput[1]) >
		// st.getOffsetAtLine(lineindex)+linestr.length()){
		// st.getText().i
		// getMessageBox("已存在的名称", subinput[1]);
		// }
		// }
		List<String> replacednamelist = new ArrayList();
		Iterator<String> it_test = list.iterator();
		while (it_test.hasNext()) {
			String str = it_test.next();
			if (str.indexOf("#") != -1) {
				String[] sub = cheakDocument.subString(str, "#");
				if (sub != null && !sub[0].equals("") && !sub[1].equals("") && sub[0].indexOf("#") == -1
						&& sub[1].indexOf("#") == -1) {
					String laststr = sub[0].substring(0, 1);
					if (replacednamelist.indexOf(laststr) == -1)
						replacednamelist.add(sub[1].substring(0, 1));
					else {
						getMessageBox("快速替换", "重复替换错误！\n\n" + "要将新词汇 \"" + laststr + "\"再次替换为\"" + sub[1] + "\"");
						int start = st.getText().indexOf(sub[1]);
						st.setSelection(start, start + sub[1].length());
						break;
					}

				} else {
					getMessageBox("快速替换", "替换文件内包含错误！\n\n文件名：");
					break;
				}
			} else if (str.indexOf("&") != -1) {
				String[] sub = cheakDocument.subString(str, "&");
				if (sub != null && !sub[0].equals("") && !sub[1].equals("") && sub[0].indexOf("#") == -1
						&& sub[1].indexOf("#") == -1)
					;
				else
					getMessageBox("快速替换", "替换文件内包含错误！\n\n文件名：");

			}

		}
	}

	public boolean findErrorOnFastRepale(List<String> list) {
		boolean haserror = false;
		List<String> lastnamelist = new ArrayList();
		Iterator<String> it_test = list.iterator();
		while (it_test.hasNext()) {
			String str = it_test.next();
			if (str.indexOf("#") != -1) {
				String[] sub = cheakDocument.subString(str, "#");
				if (sub != null && !sub[0].equals("") && !sub[1].equals("") && sub[0].indexOf("#") == -1
						&& sub[1].indexOf("#") == -1) {
					String laststr = sub[0].substring(0, 1);
					if (lastnamelist.indexOf(laststr) == -1)
						lastnamelist.add(sub[1].substring(0, 1));
					else {
						haserror = true;
						getMessageBox("快速替换", "重复替换错误！\n\n" + "要将新词汇 \"" + laststr + "\"再次替换为\"" + sub[1] + "\"");
						break;
					}

				} else {
					haserror = true;
					getMessageBox("快速替换", "替换文件内包含错误！\n\n文件名：");
					break;
				}
			} else if (str.indexOf("&") != -1) {
				String[] sub = cheakDocument.subString(str, "&");
				if (sub != null && !sub[0].equals("") && !sub[1].equals("") && sub[0].indexOf("#") == -1
						&& sub[1].indexOf("#") == -1)
					;
				else {
					haserror = true;
					getMessageBox("快速替换", "替换文件内包含错误！\n\n文件名：");
					break;
				}
			}

		}
		return haserror;
	}

	public void fastRepalce() {
		File f = getRealFile(getShowNameByRealName(b.getCurrentEditFile().getName()) + "_替换列表");
		if (f != null) {
			ioThread io = new ioThread(b);
			String text = io.readBlackFile(f, null).get();
			String doc = b.text.getText();
			boolean haserror = false;
			List<String> list = cheakDocument.getAllLine(text);

			haserror = findErrorOnFastRepale(list);

			if (!haserror) {
				Iterator<String> it = list.iterator();
				while (it.hasNext()) {
					String str = it.next();
					if (str.indexOf("#") != -1) {
						String[] sub = cheakDocument.subString(str, "#");
						doc = doc.replace(sub[0], sub[1]);
						// 被替换的人名为两个字，如李澜
						if (sub[0].length() == 2) {
							String firstname = sub[0].substring(0, 1);
							String lastname = sub[0].substring(1, 2);
							if (sub[1].length() == 2) {
								String first = sub[1].substring(0, 1);
								String last = sub[1].substring(1, 2);
								doc = doc.replace("小" + firstname, "小" + first);
								doc = doc.replace("老" + firstname, "老" + first);
								doc = doc.replace("姓" + firstname, "姓" + first);

								doc = doc.replace("小" + lastname, "小" + last);
								doc = doc.replace("阿" + lastname, "阿" + last);
								doc = doc.replace("老" + lastname, "老" + last);

								doc = replacement(doc, firstname, first);
							} else if (sub[1].length() == 3) {// 替换人名为三个字，如刘文天
								String first = sub[1].substring(0, 1);
								String last = sub[1].substring(1, 3);
								doc = doc.replace("小" + firstname, "小" + first);
								doc = doc.replace("老" + firstname, "老" + first);
								doc = doc.replace("姓" + firstname, "姓" + first);

								doc = doc.replace("小" + lastname, last);
								doc = doc.replace("阿" + lastname, last);
								doc = doc.replace("老" + lastname, last);

								doc = replacement(doc, firstname, first);
							}
						} else if (sub[0].length() == 3) {
							String firstname = sub[0].substring(0, 1);
							String lastname = sub[0].substring(1, 3);
							if (sub[1].length() == 2) {
								String first = sub[1].substring(0, 1);
								String last = sub[1].substring(1, 2);
								doc = doc.replace("小" + firstname, "小" + first);
								doc = doc.replace("老" + firstname, "老" + first);
								doc = doc.replace("姓" + firstname, "姓" + first);

								doc = doc.replace(lastname, "小" + last);
								doc = doc.replace(lastname + "姐", first + "姐");
								doc = doc.replace(lastname + "哥", first + "哥");

								doc = replacement(doc, firstname, first);
							} else if (sub[1].length() == 3) {
								String first = sub[1].substring(0, 1);
								String last = sub[1].substring(1, 3);
								doc = doc.replace("小" + firstname, "小" + first);
								doc = doc.replace("老" + firstname, "老" + first);
								doc = doc.replace("姓" + firstname, "姓" + first);

								doc = doc.replace(lastname, last);

								doc = replacement(doc, firstname, first);
							}
						}
					} else if (str.indexOf("&") != -1) {
						String[] sub = cheakDocument.subString(str, "&");
						doc = doc.replace(sub[0], sub[1]);
					}
					// lastnamelist.add(sub[1]);
				}
				if (list.size() > 0) {
					b.text.setText(doc);
					getMessageBox("快速替换", "替换完成！\n\n" + "替换了" + list.size() + "个词汇");
				}
			}
		}

	}

	public String replacement(String doc, String firstname, String first) {
		doc = doc.replace(firstname + "家", first + "家");
		doc = doc.replace(firstname + "总", first + "总");
		doc = doc.replace(firstname + "姐", first + "姐");
		doc = doc.replace(firstname + "哥", first + "哥");
		doc = doc.replace(firstname + "董", first + "董");

		doc = doc.replace(firstname + "院士", first + "院士");
		doc = doc.replace(firstname + "助理", first + "助理");
		doc = doc.replace(firstname + "秘书", first + "秘书");
		doc = doc.replace(firstname + "经理", first + "经理");
		doc = doc.replace(firstname + "董事长", first + "董事长");

		doc = doc.replace(firstname + "老师", first + "老师");
		doc = doc.replace(firstname + "先生", first + "先生");
		doc = doc.replace(firstname + "司机", first + "司机");
		doc = doc.replace(firstname + "师傅", first + "师傅");
		doc = doc.replace(firstname + "公公", first + "公公");
		doc = doc.replace(firstname + "嬷嬷", first + "嬷嬷");

		doc = doc.replace(firstname + "小姐", first + "小姐");
		doc = doc.replace(firstname + "姑娘", first + "姑娘");
		doc = doc.replace(firstname + "夫人", first + "夫人");
		doc = doc.replace(firstname + "太太", first + "太太");
		doc = doc.replace(firstname + "丫鬟", first + "丫鬟");
		doc = doc.replace(firstname + "丫环", first + "丫环");
		doc = doc.replace(firstname + "太监", first + "太监");

		return doc;
	}

	public String keyinfo() {
		return allInfo.keyinfo;
	}

	/**
	 * 问当前编辑的文档创建副本
	 */
	public void asCopy() {
		String text = b.text.getText();
		String name = getShowNameByRealName(b.currentEditFile.getName());
		addFileToProject(name + "(副本)");
		ioThread io = new ioThread(b);
		io.writeBlackFile(b.currentEditFile, new Document(text), null);
		b.getIO().open(b.currentEditFile);
	}

	public MenuItem getMenuItem(Menu parent, int style) {
		MenuItem menu = new MenuItem(parent, style);
		return menu;
	}

	public void setFileChanged() {
		if (b.fileIsSave != 0) {
			b.setFileIsSave(0);
		}
		b.ba.showCharCount();
	}

	/**
	 * 得到一个意大利人名
	 * 
	 * @param gender性别女性f，男性m
	 */
	public void getItalinaName(char gender) {
		if (b.namecreator == null) {
			b.namecreator = new nameCreator(b, new File("./nameCreator"));
		}
		if (b.namecreator.isHasItalinaNameData()) {
			if (b.text.getSelectionCount() > 0) {
				b.text.replaceTextRange(b.text.getSelectionRange().x, b.text.getSelectionRange().y, "");
				;
			}
			String[] s = b.namecreator.getItalinaName(gender);
			insertTextAndSelsction(s[1] + "·" + s[0], b.text);
		} else
			getMessageBox("人名产生错误", "不存在意大利人名数据！");
	}

	public void getEnglishName(char gender) {
		if (b.namecreator == null) {
			b.namecreator = new nameCreator(b, new File("./nameCreator"));
		}
		if (b.namecreator.isHasEnglishNameData()) {
			if (b.text.getSelectionCount() > 0) {
				b.text.replaceTextRange(b.text.getSelectionRange().x, b.text.getSelectionRange().y, "");
				;
			}
			String[] s = b.namecreator.getEnglishName(gender);
			insertTextAndSelsction(s[1] + "·" + s[0], b.text);
		} else
			getMessageBox("人名产生错误", "不存在英文人名数据！");

	}

	public void getChineseName() {
		if (b.namecreator == null) {
			b.namecreator = new nameCreator(b, new File("./nameCreator"));
		}
		if (b.namecreator.isHasChineseNameData()) {
			String[] names = b.namecreator.getChineseNames(1);
			if (b.text.getSelectionCount() > 0) {
				b.text.replaceTextRange(b.text.getSelectionRange().x, b.text.getSelectionRange().y, "");
				;
			}
			b.ba.insertTextAndSelsction(names[0], b.text);
		} else
			getMessageBox("人名产生错误", "不存在中文人名数据！");

	}

	public void getChineseNames(int count) {
		if (b.namecreator == null) {
			b.namecreator = new nameCreator(b, new File("./nameCreator"));
		}
		if (b.namecreator.isHasChineseNameData()) {
			String[] names = b.namecreator.getChineseNames(count);
			StringBuilder sb = new StringBuilder();
			for (String s : names) {
				sb.append(s + "\n");
			}
			b.ba.getBMessageBox("产生了" + count + "个中文人名", sb.toString());
		} else
			getMessageBox("人名产生错误", "不存在中文人名数据！");
	}

	public boolean isDebugMode() {
		if (b.appProperties.getProperty("DebugMode", "true").equals("true"))
			return true;
		else
			return false;
	}

	public void debugMode(boolean enable, boolean quiet) {
		if (enable) {
			setLogs();
			b.appProperties.setProperty("DebugMode", "true");
			if (!quiet)
				getMessageBox("调试信息", "已启用调试模式！");
		} else {
			if (sysErr != null)
				System.setErr(sysErr);
			closeErrorOutputStream();
			b.appProperties.setProperty("DebugMode", "false");
			if (!quiet)
				getMessageBox("调试信息", "已关闭调试模式！");
		}
	}

	public void setLogs() {
		File f = new File("./logs/log.txt");
		if (!f.getParentFile().exists())
			f.getParentFile().mkdir();
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (errorStream == null)
			errorStream = new PrintStream(fileOutputStream);

		errorStream = new PrintStream(fileOutputStream);
		sysErr = System.err;
		System.setErr(errorStream);
	}

	public void closeErrorOutputStream() {
		if (errorStream != null) {
			errorStream.flush();
			errorStream.close();
			errorStream = null;
		}
	}

	public void getNew() {
		String host = "pop.163.com";
		String username = "yangisboy@163.com";
		String password = "dxy13633528994";
		Message[] messages = null;
		String head = "black新版本";
		float current = Float.valueOf(b.appVersion);
		float newver = 0;
		int index = 0;
		try {
			messages = receiveMail.receive(username, password, host);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}

		for (int i = 0; i < messages.length; i++) {
			showMail re = new showMail((MimeMessage) messages[i]);
			String subject = null;
			try {
				subject = re.getSubject();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (subject != null) {
				if (subject.indexOf(head) != -1) {
					String newversion = subject.replace(head, "");
					Float ver = Float.valueOf(newversion);
					if (ver > newver) {
						newver = ver;
						index = i;
					}
				}
			}
		}
		if (newver > current) {
			showMail mail = new showMail((MimeMessage) messages[index]);
			try {
				mail.getMailContent((Part) messages[index], false);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				b.updateinfo = "\n发现新版本：" + newver + "\n\n" + mail.getBodyText();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void showUpdateinof() {
		bMessageBox bme = new bMessageBox(b, SWT.NONE, false) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void saveAction() {
				// TODO Auto-generated method stub

			}
		};
		bme.setTextFontinfo(12, SWT.None);
		bme.setText(b.updateinfo);
		bme.setTitle("有新版本");
		// bme.text.setLineAlignment(0, 1, SWT.CENTER);
		StyleRange sr = new StyleRange();
		sr.start = 1;
		sr.length = bme.text.getLine(1).length();
		sr.font = SWTResourceManager.getFont("宋体", 20, SWT.NONE, false, true);
		bme.text.setStyleRange(sr);
		// bme.text.set
		bme.open();
	}

	public void saveAllAsText() {
		if (b.fileindex.size() == 0)
			return;
		String dir = b.projectFile.getParentFile().getAbsolutePath() + "\\Files\\";
		DirectoryDialog getdir = new DirectoryDialog(b);
		getdir.setText("选择输出目录");
		String dirpath = getdir.open();
		if (dirpath != null && b.fileindex.size() > 0) {
			Iterator<String> it = b.fileindex.iterator();
			while (it.hasNext()) {
				String filename = it.next();
				File file = new File(dir + filename);
				if (file.exists()) {
					String outputname = getShowNameByRealName(filename);
					File output = new File(dirpath + "\\" + outputname + ".txt");
					ioThread io = new ioThread(b);
					String text = io.readBlackFile(file, null).get();
					if (!io.writeTextFile(output, text, "utf-8"))
						getMessageBox("转存文件", "转存" + outputname + "时失败！");
				}
			}
			getMessageBox("转存文件", "已将项目中的所有文件转存至所选的目录！");
			showinExplorer(dirpath, false);
		}
	}

	public void saveAsText() {
		if (b.currentEditFile == null || b.text == null)
			return;
		FileDialog fd = getFileDialog("转存为txt文件", getShowNameByRealName(b.getCurrentEditFile().getName()), b, SWT.SAVE,
				new String[] { "*.txt" });
		if (fd.getFileNames().length == 1) {
			File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
			if (saveCurrentFileAsTXT(f, "utf-8"))
				getMessageBox("", "转存成功");
			else
				getMessageBox("", "转存失败");
		}
	}

	public void saveAllAsTextToOneFile() {
		if (b.fileindex.size() == 0)
			return;

		FileDialog fd = getFileDialog("保存为TXT文件", "", b, SWT.SAVE, new String[] { "*.txt" });

		if (fd.getFileNames().length == 1) {
			File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
			b.saveCurrentFile(false, false);
			findinfo_[] text = getAllTextFromProject(true, false);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < text.length; i++) {
				if (text[i].stringbuilder != null)
					sb.append(text[i].stringbuilder.toString());
			}
			ioThread io = new ioThread(b);
			if (io.writeTextFile(f, sb.toString(), "utf-8"))
				getMessageBox("", "保存成功");
			else
				getMessageBox("", "保存失败");
		}

	}

	/**
	 * 发送反馈信息
	 */
	public void reportInfo() {
		bMessageBox bme = new bMessageBox(b, SWT.NONE, true) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void saveAction() {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
				sb.append("black版本：" + black.appVersion);
				sb.append("\n\n" + this.text.getText());
				List<String> recipients = new ArrayList<String>();
				recipients.add("aliceasmud@gmail.com");
				String subject = "black用户反馈";
				String content = sb.toString();
				List<String> attachmentNames = new ArrayList<String>();
				SendMailBySSL sendMailBySSL = new SendMailBySSL("smtp.163.com", "465", "yangisboy@163.com",
						"dxy13633528994", recipients, subject, content, attachmentNames);
				if (sendMailBySSL.sendMail())
					getMessageBox("发送信息", "反馈发送成功！感谢您对软件所提的建议。写作愉快！");
			}
		};
		bme.setTitle("向开发者发送反馈");
		bme.setText("");
		bme.text.setFocus();
		bme.open();

	}

	/**
	 * 当black程序关闭时被调用
	 */
	public void whenAppClose() {
		closeErrorOutputStream();
	}
}

class findinfo_result {
	public File currenteditfile;
	public findinfo_[] findin;

	public findinfo_result(File currenteditfile, findinfo_[] findin) {
		this.currenteditfile = currenteditfile;
		this.findin = findin;
	}

	public String getAllText() {
		StringBuilder sb = new StringBuilder();
		for (findinfo_ findinfo : findin) {
			sb.append(findinfo.stringbuilder.toString());
		}
		return sb.toString();
	}
}

class findinfo_ {
	public StringBuilder stringbuilder;
	public String subname;

	public findinfo_(StringBuilder sb, String subname) {
		stringbuilder = sb;
		this.subname = subname;
	}
}

class textInfo {
	public String fontName;
	public int fontSize, fontStyle, alignment;
	public boolean scrikeout, underline;

	public textInfo(String fontname, int fontsize, int fontstyle, boolean scrikeout, boolean underline, int alignment) {
		this.fontName = fontname;
		this.fontSize = fontsize;
		this.fontStyle = fontstyle;
		this.scrikeout = scrikeout;
		this.underline = underline;
		this.alignment = alignment;
	}
}

class alignmentInfo {
	public int linestartoffset, alignment;

	public alignmentInfo(int linestartoffset, int alignment) {
		this.linestartoffset = linestartoffset;
		this.alignment = alignment;
	}
}

class fileInfo {
	String showname, realname;
	int caretoffset, toplinx, toppixel;
	boolean inrecycle;

	public fileInfo(String realname, String showname) {
		this.realname = realname;
		this.showname = showname;
	}
}
class markstat implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String text;
	public int count;
	public boolean visible;
	public markstat(String text, int count){
		this.text = text;
		this.count = count;
	}
}
class hasinfo{
	boolean ishas;
	int index;
	public hasinfo(boolean ishas,int index){
		this.ishas = ishas;
		this.index = index;
	}
}
