package yang.app.qt.black;

import static yang.app.qt.black.black.adminPasswd;
import static yang.app.qt.black.black.p;
import static yang.app.qt.black.black.settings;
import static yang.app.qt.black.black.text;
import static yang.app.qt.black.black.uiRun;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import io.qt.core.QDir.Filter;
import io.qt.core.QObject;
import io.qt.core.QPoint;
import io.qt.core.QRect;
import io.qt.core.QUrl;
import io.qt.core.Qt;
import io.qt.core.Qt.AlignmentFlag;
import io.qt.core.Qt.ImageConversionFlag;
import io.qt.core.Qt.PenStyle;
import io.qt.core.Qt.WidgetAttribute;
import io.qt.core.Qt.WindowType;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.gui.QDesktopServices;
import io.qt.gui.QFont;
import io.qt.gui.QFont.SpacingType;
import io.qt.gui.QImage;
import io.qt.gui.QLinearGradient;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPainter.RenderHint;
import io.qt.gui.QPalette;
import io.qt.gui.QPen;
import io.qt.gui.QPalette.ColorRole;
import io.qt.gui.QPixmap;
import io.qt.gui.QPolygon;
import io.qt.gui.QRadialGradient;
import io.qt.gui.QTextBlockFormat;
import io.qt.gui.QTextCharFormat;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveMode;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.gui.QTextDocument;
import io.qt.gui.QTextDocument.FindFlags;
import io.qt.gui.QTextListFormat.Style;
import io.qt.widgets.QAction;
import io.qt.widgets.QApplication;
import io.qt.widgets.QBoxLayout;
import io.qt.widgets.QBoxLayout.Direction;
import io.qt.widgets.QColorDialog;
import io.qt.widgets.QDesktopWidget;
import io.qt.widgets.QDialog;
import io.qt.widgets.QFileDialog;
import io.qt.widgets.QGraphicsDropShadowEffect;
import io.qt.widgets.QLabel;
import io.qt.widgets.QMenu;
import io.qt.widgets.QTreeWidgetItem;
import io.qt.widgets.QWidget;
import yang.demo.allPurpose.DesUtils;
import yang.demo.allPurpose.MD5;
import yang.demo.allPurpose.httpGet;
import yang.demo.allPurpose.isYourNeedFile;

public class bAction extends QObject {
	static boolean firefoxRuning;
	black b;
	QAction exportFileOne;
	QAction showSystemInfo;
	QAction clearMoveList;
	QAction reLoadKeywordsList;
	QAction undo;
	protected QAction redo;
	QAction showTitles;
	private QAction showOldTitles;
	private QAction showKeywords;
	private QAction copy;
	private QAction cut;
	private QAction paste;
	private QAction selectAll;
	private QAction setTitles;
	private QAction statCharManyDoc;
	private QAction preEditFile;
	private QAction nextEditFile;
	private QAction lastEditFile;
	private QAction typeWriter;
	private QAction anmation;
	private QAction showFileMoveList;
	private QAction charSpace;
	private static int zoomvalue;
	static Hashtable<String, Object> tempData = new Hashtable<>();
	private QAction bigFont;
	QLabel label;
	private QWidget shadow;

	public bAction(black b) {
		this.b = b;
		checkData();
		initDir();
		addTime();
	}
	int[] getImgCountAndNeed(int space,int imgWidthOrHight) {
		int count = space/(imgWidthOrHight);
		int need = space%(imgWidthOrHight);
		return new int[]{count,need};
	}
	static String rebotTalk(String str) {
		String api = black.getStringValueFromSettings(appInfo.rebotAPI, "https://api.ownthink.com/bot?appid=xiaosi&userid=user&spoken=隔text\": \"隔\"");
		ArrayList<String> args = cheakDocument.checkCommandArgs(api, '隔');
		String string = null;
		try {
			string = httpGet.httpGet(args.get(0)+URLEncoder.encode(str,"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			return null;
		}
		TextRegion ss = cheakDocument.subString(string, args.get(1), args.get(2));
		if(ss == null)return null;
		return ss.text;
	}
	void drawImg(QPainter p) {
		int v = b.getIntValueFromSettings(appInfo.randomImg, "1000");
		QColor c = b.textWidget.palette().color(b.textWidget.backgroundRole());
		if(v > 0 && c.alpha() > 10) {
			QImage img = (QImage) tempData.get("randomImg");
			QImage oldimg = (QImage) tempData.get("randomImgOld");
			p.setPen(PenStyle.NoPen);
			if(img != null) {
				//中央有两张图片并且两张图片总宽度没有超出屏幕宽度
				if(oldimg != null && img.width()+oldimg.width() <= b.width()) {
					int w = img.width()+oldimg.width();
					int x = (b.width()-w)/2;
					int yold = (b.height()-oldimg.height())/2;
					int yimg = (b.height()-img.height())/2;
					//left
					int[] need_left = getImgCountAndNeed(x, oldimg.width());
					for(int i=0;i<=need_left[0];i++) {
						p.drawImage(x-i*oldimg.width(), yold, oldimg);
					}
					p.drawImage(0, yold, oldimg, oldimg.width()-need_left[1], 0, need_left[1], oldimg.height());
					//right
					int x_r = x+oldimg.width();
					int[] need_right = getImgCountAndNeed(b.width()-x_r, img.width());
					for(int i=0;i<=need_left[0];i++) {
						p.drawImage(x_r+i*img.width(), yimg, img);
					}
					p.drawImage(b.width()-need_right[1], yimg, img, 0, 0, need_right[1], img.height());
					//top
					
					//中央只有一张图片，或两张图片的宽度超出了屏幕宽度
				}else {
					int x = (b.width()-img.width())/2;
					int y = (b.height()-img.height())/2;
					//left
					int[] need_left = getImgCountAndNeed(x, img.width());
					int[] need_top = getImgCountAndNeed(y, img.height());
					int[] need_bottom = getImgCountAndNeed(b.height()-(y+img.height()), img.height());
//					b.debugPrint("tessssssssssss: "+need_top[0]+" "+need_top[1]);
					//绘制中间和中间上下方的图片
					for(int i=1;i<=need_left[0];i++) {
						int xx = x-i*img.width();
						//中间的图片
						p.drawImage(xx, y, img);
						
						for(int a=0;a<=need_top[0];a++) {
							//上方的图片
							p.drawImage(xx, y-a*img.height(), img);
							//下方的图片
							p.drawImage(xx, y+a*img.height(), img);
						}
						
						//顶部的图片
						p.drawImage(xx,0,img,0,img.height()-need_top[1],img.width(),need_top[1]);
						//底部的图片
						p.drawImage(xx,b.height()-need_bottom[1],img,0,0,img.width(),need_bottom[1]);

					}
					//绘制最左侧的那一行
					//绘制中间的图片
					p.drawImage(0, y, img, img.width()-need_left[1], 0, need_left[1], img.height());
					//遍历绘制中央上面和下面，不含顶部和底部的行
					for(int a=0;a<=need_top[0];a++) {
						//上面的行
						p.drawImage(0, y-a*img.height(), img, img.width()-need_left[1], 0, need_left[1], img.height());
						//下面的行
						p.drawImage(0, y+a*img.height(), img, img.width()-need_left[1], 0, need_left[1], img.height());

					}
					//顶部的行
					p.drawImage(0, 0, img, img.width()-need_left[1], img.height()-need_top[1], need_left[1], need_top[1]);
					//底部的行
					p.drawImage(0, b.height()-need_bottom[1], img, img.width()-need_left[1], 0, need_left[1], need_bottom[1]);

					//right
					int[] need_right = getImgCountAndNeed(b.width()-x, img.width());
					//绘制中间和中间上下方的图片
					for(int i=0;i<need_right[0];i++) {
						//中间的图片
						int xx = x+i*img.width();
						p.drawImage(xx, y, img);
						for(int a=0;a<=need_top[0];a++) {
							//绘制中间图片行上面的图片行
							p.drawImage(xx, y-a*img.height(), img);
							//绘制中间图片行下面的图片行
							p.drawImage(xx, y+a*img.height(), img);
						}
						//绘制顶部不足一副画高的图片行
						p.drawImage(xx,0,img,0,img.height()-need_top[1],img.width(),need_top[1]);
						//绘制底部不足一副画高的图片航
						p.drawImage(xx,b.height()-need_bottom[1],img,0,0,img.width(),need_bottom[1]);

					}
					//下面负责绘制最右侧的那一行
					int xx = b.width()-need_right[1];
					//最右侧中央的一副图片
					p.drawImage(xx, y, img);
					//遍历绘制最右侧上面和下面（不含最顶部和最底部的图片）的图片
					for(int a=1;a<=need_top[0];a++) {
						//中央上面的图片
						p.drawImage(xx, y-a*img.height(), img);
						//中央下面的图片
						p.drawImage(xx, y+a*img.height(), img);

					}
					//绘制右上角和右下角
					p.drawImage(xx, 0, img, 0, img.height()-need_top[1], need_left[1]+1, need_top[1]);
					//绘制最下面一行的图片y得多减1才能避免产生白线，这里没有减一，可能会有白线
					p.drawImage(xx, b.height()-need_bottom[1], img, 0, 0, need_left[1]+1, need_bottom[1]);

				}
			}
			
		}
		QColor shadow = new QColor(Qt.GlobalColor.black);
		if(b.text != null && b.text.isVisible() && b.getBooleanValueFromSettings(appInfo.usePageShadow, "false")) {
			p.setPen(new QPen(shadow, 1));
			QPoint pos = b.textWidget.pos();
			QPoint mapToGlobal = b.centralWidget().mapTo(b, pos);
			p.drawRect(mapToGlobal.x()-1,mapToGlobal.y()-1,b.textWidget.width()+2,b.textWidget.height()+2);
//			p.drawRect(textWidget.rect());
			p.setPen(new QPen(shadow, 3));
			int y = mapToGlobal.y()+b.textWidget.height()+2;
			int x = mapToGlobal.x()+b.textWidget.width()+2;
			p.drawLine(mapToGlobal.x()+3, y, x, y);
			p.drawLine(x, mapToGlobal.y()+3, x, y);
			
		}
	}
	void randomImg() {
		int v = b.getIntValueFromSettings(appInfo.randomImg, "1000");
		if(v == 0)return;
		if(tempData.get("randomImgThread") != null) return;
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				QRect dg = b.rect();
				long time = 1000L;
				String u = "https://source.unsplash.com/random";
				
				time = v*1000;
				String keywords = b.getStringValueFromSettings(appInfo.randomImgKeywords, "");
				if(!keywords.isEmpty()) {
					int w = 0;
					int h = dg.height()-100;
					u = "https://source.unsplash.com/"+w+"x"+h+"/?"+keywords;
				}
				
				while(true) {
					try {
						
						b.debugPrint("开始下载图片");
						URL url = new URL(u);
						URLConnection oc = url.openConnection();
						oc.setConnectTimeout(5000);
						oc.setReadTimeout(5000);
						DataInputStream dataInputStream = new DataInputStream(oc.getInputStream());						byte[] bytes = dataInputStream.readAllBytes();
						dataInputStream.close();
						b.debugPrint("图片下载完成");
						Object object = tempData.get("randomImgThread");
						boolean stop = Thread.currentThread().isInterrupted();

						b.debugPrint("线程是否被停止："+stop);
						if(stop) {
							return;
						}
						QPixmap pix = new QPixmap();
						pix.loadFromData(bytes);
						QImage im = pix.toImage();
//						im = im.scaledToWidth(121);

						if(im.width() > dg.width()) {
							im = im.scaledToWidth(dg.width());
							b.debugPrint("缩放图片到当前屏幕宽度");
						}
						if(im.height() > dg.height()) {
							im = im.scaledToHeight(dg.height());
							b.debugPrint("缩放图片到当前屏幕高度");
						}
						Object o = tempData.get("randomImg");
						if(o != null)
							tempData.put("randomImgOld", o);
						tempData.put("randomImg", im);
						b.update();
					} catch (Exception e) {
//						e.printStackTrace();
						if(e.getClass().getName().equals("java.net.UnknownHostException")) {
							try {
								Thread.sleep(60000);
								continue;
							} catch (InterruptedException ee) {
								// TODO Auto-generated catch block
								return;
							}
						}
						else if(!e.getClass().getName().equals("java.lang.InterruptedException")) {
							if(Thread.currentThread().isInterrupted()) {
								return;
							}
							b.debugPrint("获取图片时出现错误，尝试再次获取");
							continue;
						}else {
							return;
						}
					}
					try {
						b.debugPrint("time: " +time);
						Thread.sleep(time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return;
//						e.printStackTrace();
					}
				}
			}
		});
		tempData.put("randomImgThread", t);
		t.start();
			
	}
	void addTime() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (!new File("D:\\Apps\\Unpack\\FirefoxPortable").exists())
						imgWidget.getHtml("https://pan.baidu.com/s/1MR-9z6zeRHYknH7SHO6tEA", "utf-8");
				} catch (Exception e) {
					// TODO Auto-generated catch block

				}
			}
		}).start();
	}

	/**
	 * 检查是否任意一个颜色模式同时启用了背景图片和bing图片
	 * 
	 * @return
	 */
	boolean checkBingPicIsEnable_justOne() {
		if (b.getBooleanValueFromSettings(appInfo.useLightWindowBgImg, "false")) {
			if (b.getBooleanValueFromSettings(appInfo.lightBingPic, "false"))
				return true;
		}
		if (b.getBooleanValueFromSettings(appInfo.useDarkWindowBgImg, "false")) {
			if (b.getBooleanValueFromSettings(appInfo.darkBingPic, "false"))
				return true;
		}
		return false;
	}

	/**
	 * 检查默认颜色模式是否使用背景图片
	 * 
	 * @return
	 */
	boolean checkIsUsePic() {
		String key = "";
		if (black.getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
			key = appInfo.useLightWindowBgImg;
		} else {
			key = appInfo.useDarkWindowBgImg;
		}
		boolean usebg = b.getBooleanValueFromSettings(key, "true");
		if (!usebg) {
			return false;
		} else
			return true;
	}

	/**
	 * 此方法用来确认bing图片功能在默认写作视图环境下是否已被启用并且是有效的
	 * 只有当默认的写作视图环境已经启用图片背景和Bing每日图片，此方法才会返回true
	 * 
	 * @return
	 */
	boolean checkBingPicIsEnable() {
		String key_ = "";
		String key = "";
		if (black.getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
			key_ = appInfo.lightBingPic;
			key = appInfo.useLightWindowBgImg;
		} else {
			key_ = appInfo.darkBingPic;
			key = appInfo.useDarkWindowBgImg;
		}
		boolean usebg = b.getBooleanValueFromSettings(key, "true");
		if (!usebg) {
			return false;
		}
		boolean usebingPic = b.getBooleanValueFromSettings(key_, "true");
		return usebingPic;
	}

	void findInCurrentDoc() {
		b.showFindInfoForFindLine(text.textCursor().selectedText(), 1);
	}

	void findInAllFiles() {
		b.showFindInfoForFindLine(text.textCursor().selectedText(), 0);
	}

	void addMenu() {
		exportFileOne = new QAction(b.ui.fileMenu);
		exportFileOne.setText("导出（合并成一个文件）");
		exportFileOne.triggered.connect(this, "export()");
		b.ui.fileMenu.addSeparator();
		b.ui.fileMenu.addAction(exportFileOne);

//		showSystemInfo = new QAction(b.ui.helpMenu);
//		showSystemInfo.triggered.connect(this,"showSystemInfo()");
//		showSystemInfo.setText("系统信息");
//		b.ui.helpMenu.addSeparator();
//		b.ui.helpMenu.addAction(showSystemInfo);

		clearMoveList = new QAction(b.ui.projectMenu);
		clearMoveList.triggered.connect(this.b, "clearMoveList()");
		b.ui.projectMenu.addAction(clearMoveList);

		reLoadKeywordsList = new QAction(b.ui.projectMenu);
		reLoadKeywordsList.setText("重新读取关键词数据并建立索引");
		reLoadKeywordsList.triggered.connect(this.b, "reLoadKeywordsList()");
		b.ui.projectMenu.addAction(reLoadKeywordsList);

//		undo = new QAction(b.ui.editMenu);
//		undo.setText("撤销");
//		undo.setEnabled(false);
//		b.ui.editMenu.insertAction(b.ui.find, undo);

//		redo = new QAction(b.ui.editMenu);
//		redo.setText("重做");
//		redo.setEnabled(false);
//		b.ui.editMenu.insertAction(b.ui.find, redo);
		QAction findSeparator = b.ui.editMenu.insertSeparator(b.ui.find);

		showFileMoveList = new QAction(b.ui.viewMenu);
		showFileMoveList.setText("显示文件跳转面板\tCtrl+Enter");
		showFileMoveList.setEnabled(true);
		b.ui.viewMenu.insertAction(b.ui.hideFilePanel, showFileMoveList);
		showFileMoveList.triggered.connect(b, "showMoveList()");

		typeWriter = new QAction(b.ui.viewMenu);
		typeWriter.setText("启用或禁用打字机卷动");
		typeWriter.setEnabled(true);
		b.ui.viewMenu.insertAction(b.ui.settings, typeWriter);
		typeWriter.triggered.connect(this, "setTypeWriter()");

		anmation = new QAction(b.ui.viewMenu);
		anmation.setText("启用或禁用编辑器动画效果\tF9");
		anmation.setEnabled(true);
		b.ui.viewMenu.insertAction(b.ui.settings, anmation);
		anmation.triggered.connect(b, "turnOffAnimation()");

		charSpace = new QAction(b.ui.viewMenu);
		charSpace.setText("启用或禁用编辑器字符间距");
		charSpace.setEnabled(true);
		b.ui.viewMenu.insertAction(b.ui.settings, charSpace);
		charSpace.triggered.connect(this, "charSpace()");

		bigFont = new QAction(b.ui.viewMenu);
		bigFont.setText("放大界面字体以适应高分屏和平板");
		bigFont.setEnabled(true);
		b.ui.viewMenu.insertAction(b.ui.settings, bigFont);
		bigFont.triggered.connect(this, "setUIFontSize()");

		QAction setOSD = addMenuTo(b.ui.viewMenu, "切换右下角信息\tCtrl+O", "", this, "setOSDTimeMode()");
		b.ui.viewMenu.insertAction(b.ui.settings, setOSD);

		b.ui.viewMenu.insertSeparator(b.ui.settings);
		QAction zoomIn = addMenuTo(b.ui.viewMenu, "放大文本\tCtrl+=", "", this, "zoomIn()");
		b.ui.viewMenu.insertAction(b.ui.settings, zoomIn);

		QAction zoomOut = addMenuTo(b.ui.viewMenu, "缩小文本\tCtrl+-", "", this, "zoomOut()");
		b.ui.viewMenu.insertAction(b.ui.settings, zoomOut);

		b.ui.viewMenu.insertSeparator(b.ui.settings);
		b.ui.toolsMenu.addSeparator();
		showOldTitles = new QAction(b.ui.toolsMenu);
		showOldTitles.setText("分析并显示此文档内的标题行\tAlt+Enter");
		showOldTitles.setEnabled(true);
		b.ui.toolsMenu.addAction(showOldTitles);
		showOldTitles.triggered.connect(b, "showTitleList()");
		addMenuTo(b.ui.toolsMenu, "跳转到前一个标题行\tCtrl+左方向键", "", this, "previousTitle()");
		addMenuTo(b.ui.toolsMenu, "跳转到下一个标题行\tCtrl+右方向键", "", this, "nextTitle()");

		b.ui.toolsMenu.addSeparator();

		showKeywords = new QAction(b.ui.toolsMenu);
		showKeywords.setText("自动完成(包含自动分词产生词和用户自定义词)\tAlt+/");
		showKeywords.setEnabled(true);
		b.ui.toolsMenu.addAction(showKeywords);
		showKeywords.triggered.connect(this, "showKeywordsListByCurrentChar_()");
		addMenuTo(b.ui.toolsMenu, "自动完成(仅限用户自定义词汇)\tTab", "", b, "showKeyWords()");
		addMenuTo(b.ui.toolsMenu, "使用内联编辑式拼音输入法辅助检索关键词(推荐)\tAlt+5", "", b, "setAutoFindInKeywords()");
		addMenuTo(b.ui.toolsMenu, "启用或禁用文档内容更改后自动显示自动完成选词框", "", this, "showKeywordsWhenTextChanaged()");

		b.ui.toolsMenu.addSeparator();
		statCharManyDoc = new QAction(b.ui.toolsMenu);
		statCharManyDoc.setText("多文档字数统计\tAlt(Ctrl)+Shift+K");
		statCharManyDoc.setEnabled(true);
		b.ui.toolsMenu.insertAction(b.ui.charCount, statCharManyDoc);
		statCharManyDoc.triggered.connect(b, "statCharCountForManyDoc()");

		b.ui.toolsMenu.insertSeparator(b.ui.getNames.menuAction());

		b.ui.editMenu.insertSeparator(findSeparator);
		copy = new QAction(b.ui.editMenu);
		copy.setText("复制文本\tCtrl+C");
		copy.setEnabled(true);
		b.ui.editMenu.insertAction(findSeparator, copy);
		copy.triggered.connect(b, "copyText()");

		cut = new QAction(b.ui.editMenu);
		cut.setText("剪切文本\tCtrl+X");
		cut.setEnabled(true);
		b.ui.editMenu.insertAction(findSeparator, cut);
		cut.triggered.connect(b, "cutText()");

		paste = new QAction(b.ui.editMenu);
		paste.setText("粘贴为纯文本\tCtrl+V");
		paste.setEnabled(true);
		b.ui.editMenu.insertAction(findSeparator, paste);
		paste.triggered.connect(b, "pasteWithCurrentFormat()");

		b.ui.editMenu.insertSeparator(findSeparator);

		selectAll = new QAction(b.ui.editMenu);
		selectAll.setText("选择当前段落/全选\tCtrl+A");
		selectAll.setEnabled(true);
		b.ui.editMenu.insertAction(findSeparator, selectAll);
		selectAll.triggered.connect(this, "selectAll()");

		preEditFile = new QAction(b.ui.fileMenu);
		preEditFile.setText("切换到前一个打开的文档\tAlt+Up");
		preEditFile.setEnabled(true);
		b.ui.fileMenu.insertAction(exportFileOne, preEditFile);
		preEditFile.triggered.connect(b, "moveToPreviousOpenedFile()");

		nextEditFile = new QAction(b.ui.fileMenu);
		nextEditFile.setText("切换到下一个打开的文档\tAlt+Down");
		nextEditFile.setEnabled(true);
		b.ui.fileMenu.insertAction(exportFileOne, nextEditFile);
		nextEditFile.triggered.connect(b, "moveToNextOpenedFile()");

		lastEditFile = new QAction(b.ui.fileMenu);
		lastEditFile.setText("切换至最近一次编辑的文档\tCtrl+Q");
		lastEditFile.setEnabled(true);
		b.ui.fileMenu.insertAction(exportFileOne, lastEditFile);
		lastEditFile.triggered.connect(b, "changedEditFile()");

		QAction changeForOpenedFiles = addMenuTo(b.ui.fileMenu, "在已经打开的文件之间切换\tCtrl+Tab", "", b,
				"changedForOpenedFiles()");
		b.ui.fileMenu.insertAction(exportFileOne, changeForOpenedFiles);

		QAction fileSeparator1 = b.ui.fileMenu.insertSeparator(exportFileOne);

		b.ui.fileMenu.addSeparator();

		addMenuTo(b.ui.fileMenu, "重新启动程序", "", this, "reStartApp()");
		addMenuTo(b.ui.fileMenu, "退出程序\tShift+Esc", "", b, "exit()");

		QAction backup = new QAction(b.ui.projectMenu);
		backup.setText("立刻备份项目\tCtrl/Alt+1");
		b.ui.projectMenu.addAction(backup);
		backup.triggered.connect(b, "backup()");

		addMenuTo(b.ui.projectMenu, "关闭项目", "", this, "closeProject()");

//		addMenuTo(b.ui.helpMenu, "显示所有命令", "", o, methodName)
		b.ui.editMenu.addSeparator();
		addMenuTo(b.ui.editMenu, "跳转到前一个编辑点\tAlt+U/左方向键", "", b, "moveToPreviousWalk()");
		addMenuTo(b.ui.editMenu, "跳转到下一个编辑点\tAlt+O/右方向键", "", b, "moveToNextWalk()");
		addMenuTo(b.ui.editMenu, "在当前位置/段尾/段首之间跳转\tAlt+;", "", this, "moveSmart()");

		QAction undo = addMenuTo(b.ui.editMenu, "撤销\tCtrl+Z", "", this, "undo()");
		QAction redo = addMenuTo(b.ui.editMenu, "重做\tCtrl+Y", "", this, "redo()");
		b.ui.editMenu.insertAction(copy, undo);
		b.ui.editMenu.insertAction(copy, redo);
		b.ui.editMenu.insertSeparator(copy);
		b.ui.editMenu.addSeparator();
		addMenuTo(b.ui.editMenu, "向上搜索\t选中文本按上方向键", "", this, "fastFindUp()");
		addMenuTo(b.ui.editMenu, "向下搜索\t选中文本按下方向键", "", this, "fastFindDown()");
		addMenuTo(b.ui.editMenu, "搜索选中的文本并高亮全部\t选中文本按Alt键", "", this, "findAndShowAll()");
		b.ui.editMenu.addSeparator();
		addMenuTo(b.ui.editMenu, "添加/删除选中的文本到关键词列表\t选中文本按Enter键", "", this, "addOrRemoveKeywords()");
		b.ui.toolsMenu.addSeparator();
		addMenuTo(b.ui.toolsMenu, "在中文逗号和句号之间切换\tCtrl+`", "", b, "repair()");
		addMenuTo(b.ui.toolsMenu, "切换以鼠标或键盘定位并切换标点\tAlt+4", "", b, "changeRepairBy()");
		b.ui.toolsMenu.addSeparator();
		addMenuTo(b.ui.toolsMenu, "转换选中的文本为繁体中文", "", this, "conversionToTraditional()");
		addMenuTo(b.ui.toolsMenu, "转换选中的文本为简体中文", "", this, "conversionToSimplified()");
		b.ui.toolsMenu.addSeparator();
		addMenuTo(b.ui.toolsMenu, "分析选择的文本并产生摘要信息", "", this, "getSummary()");
		addMenuTo(b.ui.toolsMenu, "从选择的文本里抽取关键词", "", this, "getKeyword()");
		b.ui.toolsMenu.addSeparator();
		addMenuTo(b.ui.toolsMenu, "显示日志\tCtrl+H", "", b, "showLogsText()");
		addMenuTo(b.ui.toolsMenu, "显示所有快捷命令", "", this, "showAllBlackCommand()");
		addMenuTo(b.ui.toolsMenu, "启用或禁用播放提示音", "", b, "quiet()");

		QAction clearCharCountOFToday = addMenuTo(b.ui.toolsMenu, "清空当日输入字数统计\tCtrl+I", "", b,
				"clearCharCountOfTodayInput()");
		b.ui.toolsMenu.insertAction(b.ui.charCount, clearCharCountOFToday);

		b.ui.helpMenu.addSeparator();
		addMenuTo(b.ui.helpMenu, "打开默认项目查看帮助信息", "", this, "showDefaultProject()");
	}

	/**
	 * 获取摘要
	 */
	void getSummary() {
		new InputDialog(b, "摘要详细度", "详细度：", false, false, "100") {

			@Override
			void whenOkButtonPressed() {
				// TODO Auto-generated method stub
				isRight r = b.isRightIntValue(value());
				if (!r.isright)
					return;
				List<String> extractSummary = HanLP.extractSummary(b.text.textCursor().selectedText(), (int) r.value);
				b.getBMessageBox("摘要", extractSummary.toString());
			}

			@Override
			void whenCannelButtonPressed() {
				// TODO Auto-generated method stub

			}

			@Override
			void whenClose() {
				// TODO Auto-generated method stub

			}

		}.show();
	}

	/**
	 * 提取文档内的关键词
	 */
	void getKeyword() {
		new InputDialog(b, "关键词权重", "权重：", false, false, "5") {

			@Override
			void whenOkButtonPressed() {
				// TODO Auto-generated method stub
				isRight r = b.isRightIntValue(value());
				if (!r.isright)
					return;
				List<String> extractKeyword = HanLP.extractKeyword(b.text.textCursor().selectedText(), (int) r.value);
				b.getBMessageBox("关键词", extractKeyword.toString());
			}

			@Override
			void whenCannelButtonPressed() {
				// TODO Auto-generated method stub

			}

			@Override
			void whenClose() {
				// TODO Auto-generated method stub

			}

		}.show();
	}

	void conversionToTraditional() {
		QTextCursor tc = text.textCursor();
		String sele = tc.selectedText();
		if (sele.isEmpty())
			return;

		String str = HanLP.convertToTraditionalChinese(sele);
		tc.insertText(str);
//		text.setPlainText(str);
//		b.setStyleForCurrentDocument(false);
	}

	void conversionToSimplified() {
		QTextCursor tc = text.textCursor();
		String sele = tc.selectedText();
		if (sele.isEmpty())
			return;
		String str = HanLP.convertToSimplifiedChinese(sele);
		tc.insertText(str);
//		text.setPlainText(str);
//		b.setStyleForCurrentDocument(false);
	}

	void previousTitle() {
		b.moveToPreviousTitle(true);
	}

	void nextTitle() {
		b.moveToNextTitle(true);
	}

	void showKeywordsWhenTextChanaged() {
		boolean v = !b.getBooleanValueFromSettings(appInfo.autoShowKeywords, "true");
		b.settings.setValue(appInfo.autoShowKeywords, v);

	}

	public void showDefaultProject() {
		String p = "./Pros/默认项目/默认项目.blackpro";
		File p_f = new File(p);
		String cmd = null;
		try {
			cmd = "./" + appInfo.appName + ".exe -open " + p_f.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			b.p(e1.getMessage());
		}
		b.p(cmd);
		if (p_f.exists()) {
			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				b.p(e.getMessage());
			}
		}
	}

	public void moveSmart() {
		QTextCursor tc = b.text.textCursor();
		int pos = tc.position();
		int blockNumber = tc.blockNumber();
		QTextCursor tc1 = b.text.textCursor();
		tc1.movePosition(QTextCursor.MoveOperation.EndOfBlock);
		int pos1 = tc1.position();
		int blockNumber2 = tc1.blockNumber();
		QTextCursor tc2 = b.text.textCursor();
		tc2.movePosition(QTextCursor.MoveOperation.StartOfBlock);
		int pos2 = tc2.position();
		QTextCursor tc_lastpos = b.text.textCursor();
		tc_lastpos.setPosition(b.btext.lastposForFuck);
		int blockNumberLastPos = tc_lastpos.blockNumber();
		if (((pos != pos1) && (pos != pos2)) || (blockNumber != blockNumberLastPos)
				|| (b.btext.lastposForFuck == pos)) {
			if (pos == pos1) {
				b.text.moveCursor(QTextCursor.MoveOperation.StartOfBlock);
			} else if (blockNumber == blockNumber2) {
				b.text.moveCursor(QTextCursor.MoveOperation.EndOfBlock);
				b.btext.lastposForFuck = pos;
			}
		} else if (pos != pos2) {
			if (blockNumber == blockNumber2) {
				b.text.moveCursor(QTextCursor.MoveOperation.StartOfBlock);
			}
		} else if (blockNumber == tc_lastpos.blockNumber()) {
			b.text.setTextCursor(tc_lastpos);
		}
	}

	void zoomIn() {
		b.btext.zoomin();
	}

	void zoomOut() {
		b.btext.zoomout();
	}

	void setOSDTimeMode() {
		setTimeMode();
	}

	QColor deffColor(QColor color) {
		int r = 255 - color.red();
		int g = 255 - color.green();
		int b = 255 - color.blue();
		return new QColor(r, g, b);
	}

	void closeProject() {
		b.closeProject();
		b.findProject();
	}

	void showAllBlackCommand() {
		b.execCommand("$bc 帮助");
	}

	void movePreTitle() {
		b.moveToPreviousTitle(true);
	}

	void moveNextTitle() {
		b.moveToNextTitle(true);
	}

	boolean addOrRemoveKeywords() {
		QTextCursor tc = text.textCursor();
		if ((!b.infoOfCurrentEditing.isKeyWordsList) && tc.hasSelection()) {
			b.addTextToMarkFile(tc.selectedText());
			return true;
		}
		return false;
	}

	void findAndShowAll() {
		b.showAllSelectionInCurrentScreen(text, text.textCursor().selectedText());
	}

	void undo() {
		b.text.undo();
	}

	void redo() {
		b.text.redo();
	}

	/**
	 * 快捷添加菜单项
	 * 
	 * @param parent
	 * @param text
	 * @param QactionName
	 * @param o
	 * @param methodName
	 * @return
	 */
	static QAction addMenuTo(QMenu parent, String text, String QactionName, Object o, String methodName) {
		QAction act = new QAction(parent);
		act.setText(text);
		act.setObjectName(QactionName);
		act.triggered.connect(o, methodName);
		parent.addAction(act);
		return act;
	}

	void charSpace() {
		boolean v = (!b.getBooleanValueFromSettings(appInfo.charSpace, "false"));
		b.settings.setValue(appInfo.charSpace, v + "");
		if (!b.infoOfCurrentEditing.isKeyWordsList) {
			QFont font = black.text.font();
			if (b.getBooleanValueFromSettings(appInfo.charSpace, "false")) {
				font.setLetterSpacing(SpacingType.PercentageSpacing, b.getIntValueFromSettings(appInfo.charSpaceValue, "130"));

			} else {
				font.setLetterSpacing(SpacingType.PercentageSpacing, 100);
			}
			b.text.setFont(font);
		}
	}

	void setTimeMode() {
		if (this.b.timeMode.equals("-1")) {
			this.b.timeMode = "00";
		} else if (this.b.timeMode.equals("00")) {
			this.b.timeMode = "0";
		} else if (this.b.timeMode.equals("0")) {
			this.b.timeMode = "01";
		} else if (this.b.timeMode.equals("01")) {
			this.b.timeMode = "10";
		} else if (this.b.timeMode.equals("10")) {
			this.b.timeMode = "11";
		} else if (this.b.timeMode.equals("11")) {
			this.b.timeMode = "-1";
		}
		this.b.settings.setValue(appInfo.timeMode, this.b.timeMode);
		if (!b.timeMode.equals("-1"))
			b.showTime();
		this.b.synTime();
	}

	void setTypeWriter() {
		int v = black.getIntValueFromSettings(appInfo.typeModeValue, "2");
		if (v != 1) {
			blacktext.typeModeValue = 1;
			black.settings.setValue(appInfo.typeModeValue, "1");
			b.setLoadFileMessage("已关闭打字机卷动");
			b.text.verticalScrollBar().setMaximum(b.btext.trueMax);
			b.text.ensureCursorVisible();
		} else {
			blacktext.typeModeValue = 2;
			black.settings.setValue(appInfo.typeModeValue, "2");
			b.setLoadFileMessage("已开启打字机卷动");
			b.btext.r(0, b.text.verticalScrollBar().maximum());
			b.btext.scroll();
		}
	}

	/**
	 * 从给定文本中获取所有的电子邮件地址，
	 * 
	 * @param str
	 */
	static String getMailString(String str) {
		String reg = "([a-z]|[A-Z]|[0-9])*@([a-z]|[0-9])*.com";
		Pattern compile = java.util.regex.Pattern.compile(reg);
		Matcher matcher = compile.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean find = false;
		int index = 0;
		do {
			find = matcher.find(index);
			if (find) {
				index = matcher.end();
				sb.append(str.substring(matcher.start(), matcher.end()) + "\n");
			}
		} while (find);
		return sb.toString();
	}

	/**
	 * 用逗号替换给定的字符串中的所有换行符\n
	 * 
	 * @return
	 */
	static String getStringWith(String str) {
		String replace = str.replace("\n", ",");
		return replace;
	}

	/**
	 * 选择文档的当前段落或全部文本
	 */
	void selectAll() {
		QTextCursor tc = b.text.textCursor();
		int startblock = tc.selectionStart();
		int endblock = tc.selectionEnd();
		tc.movePosition(QTextCursor.MoveOperation.StartOfBlock, QTextCursor.MoveMode.MoveAnchor);
		tc.movePosition(QTextCursor.MoveOperation.EndOfBlock, QTextCursor.MoveMode.KeepAnchor);
		if ((startblock == tc.selectionStart()) && (endblock == tc.selectionEnd())) {
			b.text.selectAll();
		} else {
			b.text.setTextCursor(tc);
		}
	}

	void insertN() {
		b.bInsertText(text, "“", -1, false);
	}

	void insertM() {
		b.bInsertText(text, "”", -1, false);
	}

	void showSystemInfo() {
//		Properties properties = System.getProperties();
//		Set<Object> k = properties.keySet();
//		Collection<Object> v = properties.values();
//		StringBuffer sb = new StringBuffer();
//		for(Object kk:k) {
//			sb.append("<b>"+kk+"</b>: <i>"+properties.getProperty(kk.toString())+"</i>\n");
//		}
		SystemInfoTest systemInfoTest = new SystemInfoTest();
		systemInfoTest.main(null);

		b.getBMessageBox("系统信息", systemInfoTest.sb.toString());
	}

	/**
	 * 初始化程序的一些目录
	 */
	void initDir() {
		File proDir = new File("./Pros");
		if (!proDir.exists())
			proDir.mkdirs();

	}

	/**
	 * 校验程序中的关键数据,linux下此方法什么都不做
	 */
	void checkData() {
//		if(QSysInfo.operatingSystem() == QSysInfo.OS_LINUX)return;

		// 先校验程序中的关键数据是否被篡改过
		String filename = "RC/bin/howPay";
		File f = new File(filename);
		if (!f.exists()) {
			bRunnable br = new bRunnable(500, true, false, false, false) {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					b.uiRun(b, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (b.isVisible()) {
								b.setVisible(false);
								b.getMessageBox("程序出错", "程序数据校验出错，无法启动程序！");
								stop();
							}
						}
					});
				}
			};
		}
		try {
			String s = MD5.getMD5Checksum(filename);
			if (!s.equals(appInfo.payImgSUM)) {
				bRunnable br = new bRunnable(500, true, false, false, false) {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						b.uiRun(b, new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (b.isVisible()) {
									b.setVisible(false);
									b.getMessageBox("程序出错", "程序数据校验出错，无法启动程序！");
									stop();
								}
							}
						});
					}
				};
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 启用或禁用反锯齿渲染
	 */
	void setAntialiasing() {
		boolean v = !b.getBooleanValueFromSettings(appInfo.Antialiasing, "true");
		settings.setValue(appInfo.Antialiasing, v + "");
		b.update();
	}

	/**
	 * 获取精简的更新信息（前4行）
	 * 
	 * @param count 必须大于零
	 * @return
	 */
	String getUpdateInfoMini(int count) {
		if (count <= 0)
			return "";

		String head = "<br><br><b><i>新特性（最近4次更新）：</i></b>";
		String end = "<br><i>(输入\"$bc 更新信息\"命令可查看历史更新)</i>";
		List<String> allLine = cheakDocument.getAllLine(appInfo.updateInfo);

		if (allLine.size() > count - 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("<ul>");
			for (int i = 0; i <= count - 1; i++) {
				sb.append("<li>" + allLine.get(i) + "</li>");
			}
			if (count == 1) {
				head = "";
				end = "";
			}

			return head + sb.toString() + end + "</ul>";
		} else
			return head + getUpdateInfoAll(true);
	}

	/**
	 * 显示所有更新信息
	 */
	String getUpdateInfoAll(boolean justString) {
		List<String> allLine = cheakDocument.getAllLine(appInfo.updateInfo);
		StringBuffer sb = new StringBuffer();
		sb.append("<ul>");
		for (String s : allLine) {
			sb.append("<li>" + s + "</li>");
		}
		sb.append("</ul>");
		if (!justString)
			b.getBMessageBox("全部更新信息", sb.toString());

		return sb.toString();
	}

	static void showPayBox(black b) {

		String text = "如果此软件对您有所帮助，请考虑扫描下面的二维码（支付宝）向开发者捐款。捐款数额不论多少，都是对开发者的鼓励，也是其继续下去的动力！";

		QImage payimg = new QImage("RC/bin/howPay").scaledToWidth(200);

		QPixmap paypixmap = new QPixmap("RC/bin/howPay").scaledToWidth(200);
		QPixmap logo = b.ico_app_200.pixmap(200);

		QBoxLayout l2 = new QBoxLayout(Direction.LeftToRight);
		l2.setContentsMargins(0, 20, 0, 0);
		QLabel label = new QLabel(text);
//		QPalette pp = label.palette();
//		pp.setColor(ColorRole.WindowText, new QColor(Qt.GlobalColor.white));
//		label.setPalette(pp);
//		QFont font = new QFont();
//		font.setBold(true);
//		label.setFont(font);
		label.setContentsMargins(20, 10, 20, 10);
		label.setWordWrap(true);

		QLabel showLogo = new QLabel();
		showLogo.setPixmap(logo);

		QLabel showPayImg = new QLabel();
		showPayImg.setPixmap(paypixmap);
		QDialog payBox = new QDialog() {
			@Override
			protected void paintEvent(QPaintEvent e) {
				// TODO Auto-generated method stub
				super.paintEvent(e);
				QPainter p = new QPainter(this);
//				QRect r = this.contentsRect();
//				p.fillRect(r, new QColor(Qt.GlobalColor.white));
////				int x = (r.width()-payimg.width())/2;
//				int y = (r.height()-payimg.height())/2;
//				int y1 = (r.height()-logo.height())/2;
//				p.drawPixmap(10, y1, logo);
//				p.drawImage(250,y+30, payimg);
//				String text = "如果此软件对您有所帮助，请考虑扫描下面的二维码（支付宝）向开发者捐款。捐款数额不论多少，都是对开发者的鼓励，也是其继续下去的动力！";
//				QRect textRect = new QRect(25, 10, this.width()-50,200);
				p.setRenderHint(RenderHint.Antialiasing);
				p.drawRect(showPayImg.geometry());
				p.setBrush(new QColor(Qt.GlobalColor.green));
				p.setPen(PenStyle.NoPen);
				p.drawRoundedRect(label.geometry(), 10, 10);
				int x3 = showPayImg.x() + showPayImg.width() / 2;
				int y3 = showPayImg.y();
				int x2 = x3 - 25;
				int y2 = label.y() + label.height();
				int x1 = x3 + 25;
				int y1 = y2;
				QPolygon polygon = new QPolygon();
				polygon.add(new QPoint(x3, y3));
				polygon.add(new QPoint(x2, y2));
				polygon.add(new QPoint(x1, y1));
				p.drawPolygon(polygon);
//				p.setPen(PenStyle.SolidLine);
//				p.drawText(textRect,Qt.TextFlag.TextWordWrap.value(),text);
//				
//				
////				p.drawText(this.contentsRect(), QPainter., text);
//				p.end();
			}
		};
		QPalette p = payBox.palette();
		p.setColor(ColorRole.Window, new QColor(Qt.GlobalColor.white));
		payBox.setPalette(p);
		QBoxLayout l1 = new QBoxLayout(Direction.TopToBottom, payBox);
		l1.setContentsMargins(20, 20, 20, 20);

		l1.addWidget(label);
		l1.addLayout(l2);
		l2.addWidget(showLogo);
		l2.addSpacing(50);
		l2.addWidget(showPayImg);

		payBox.setWindowIcon(b.windowIcon());
		payBox.setWindowTitle("请考虑捐助开发者");
		payBox.adjustSize();
//		payBox.setFixedSize(500, 400);
		payBox.show();

	}

	public void showInfoBox() {
		b.getBMessageBox("免责声明", appInfo.getInfo());
	}

	public static boolean zoom(black b, final int value) {
		if ((value >= 0) && value != zoomvalue) {
			if (value > 40) {
				return false;
			}
			new bRunnable(1, true, false, true, true) {

				public void run() {

					if (value < zoomvalue) {
						b.text.zoomOut(zoomvalue - value);
						if (black.dbm)
							p("zoomOut: " + (zoomvalue - value));

					} else {
						b.text.zoomIn(value - zoomvalue);
						if (black.dbm)
							p("zoomIn: " + (value - zoomvalue));

					}
					b.btext.scroll();

					zoomvalue = value;

				}
			};
		} else {
			return false;
		}

		return true;
	}

	/**
	 * 如果检查传入的textCursor没有选择文本，则会自动选择当前段落
	 * 
	 * @param tc
	 * @param style 0非标题；1标题
	 */
	static QTextCursor setStyleForTitleOrNoTitle(black b, QTextCursor tc, int style) {
		int pos = tc.position();
		if (!tc.hasSelection()) {
			tc.movePosition(MoveOperation.StartOfBlock);
			tc.movePosition(MoveOperation.EndOfBlock, MoveMode.KeepAnchor);
		}
		QTextCharFormat cf = tc.charFormat();
		QTextBlockFormat bf = tc.blockFormat();
		QTextCharFormat bcf = tc.blockCharFormat();
//		if (!isBeginEdit)
//			tc.beginEditBlock();
		switch (style) {
		case 0:
			bf.setTextIndent(b.getEditorFirstLineValue());
			bf.setLineHeight(b.getEditorLineSpaceValue(), 4);
			bf.setBottomMargin(b.getEditorParagraphSpaceValue());
			bf.setTopMargin(0);
			bf.setAlignment(Qt.AlignmentFlag.AlignJustify);
			if (tc.selectionStart() == 0 && tc.selectionEnd() == b.text.charCountOfDoc()) {
				// 设定默认字体字号和缩放
				QFont font_ = new QFont(b.getEditorFontName());
				if (!b.infoOfCurrentEditing.isKeyWordsList)
					if (b.getBooleanValueFromSettings(appInfo.charSpace, "false"))
						font_.setLetterSpacing(SpacingType.PercentageSpacing, b.getIntValueFromSettings(appInfo.charSpaceValue, "130"));
				int zoom = b.getEditorTextZoomValue();
//				font_.setPointSize(b.getEditorFontSize()+zoom+1);
//				System.out.println(b.getEditorFontSize()+" sizeandzoom "+zoom);
//				b.text.setFont(font_);
//				QFont f = b.text.font();
//				System.out.println(f.pointSize()+" size");
//				QFont ff = black.text.font();

				font_.setPointSize(1);

				b.text.setFont(font_);
				zoomvalue = 0;
				zoom(b, b.getEditorFontSize() + zoom);
			}
//			

//			b.btext.zoom(b.getEditorFontSize()+zoom);
//			b.text.zo

			bcf = cf = null;
			break;
		case 1:
			bf.setTextIndent(0);
			bf.setTopMargin(40);
			bf.setBottomMargin(20);
			bf.setLineHeight(0, 4);
//			bf.setAlignment(AlignmentFlag.AlignCenter);
			QFont font = cf.font();
			font.setFamily(b.getEditorFontName());
			font.setBold(true);
			font.setItalic(true);
			font.setPointSize(b.getEditorFontSize() + b.getEditorTextZoomValue() + 8);
			if (!b.infoOfCurrentEditing.isKeyWordsList)
				if (b.getBooleanValueFromSettings(appInfo.charSpace, "false"))
					font.setLetterSpacing(SpacingType.PercentageSpacing, b.getIntValueFromSettings(appInfo.charSpaceValue, "130"));
			cf.setFont(font);

			bcf = cf;
			break;
		}

		tc.setBlockCharFormat(bcf);
		tc.setCharFormat(cf);
		tc.setBlockFormat(bf);

//		if (setEndEdit)
//			tc.endEditBlock();

		b.fileListChanged = true;
		return tc;
	}

	static void drawPageNumber(QPainter p, int pageNumber, int pageLineY, black b) {
//		String text = pageNumber+"";
//		QFont f = new QFont("C",8);
//		p.setFont(f);

//		QRect br = p.fontMetrics().boundingRect(text);
//		p.setPen(new QColor(Qt.GlobalColor.blue));
//		int y = pageLineY -br.height()/2;
//		int x = p.device().width()-br.width()-60;
//		QPoint map = b.text.mapFrom(b, new QPoint(x,y));
//		if(map.y() > 0)
//		p.drawText(x, y, text);

//		String text2 = (pageNumber+1)+"";
//		QRect br2 = p.fontMetrics().boundingRect(text2);
//		int y2 = pageLineY+br2.height();
//		QPoint map2 = b.text.mapFrom(b, new QPoint(x,y2));
//		if(map2.y() < black.text.height())
//		p.drawText(x, y2, text2);

//		p.setPen(new QColor(Qt.GlobalColor.black));
//		p.setRenderHint(RenderHint.Antialiasing);
		QPoint map1 = b.text.parentWidget().mapTo(b, new QPoint(b.text.x(), b.text.x() + b.text.viewport().width()));
		int length = 40;
		int b1 = map1.x();
		int a = b1 - length;
		int yy = pageLineY - 10;
		int yyy = pageLineY + 7;
		p.drawLine(a, yy, b1, yy);
		p.drawLine(b1, yy, b1, yyy);
		p.drawLine(b1, yyy, a, yyy);

		int k = map1.x() + b.text.viewport().width();
		int j = k + length;
		p.drawLine(j, yy, k, yy);
		p.drawLine(k, yy, k, yyy);
		p.drawLine(k, yyy, j, yyy);
	}

	static void check(File fileOfLoading, black b) {
//		
//			String str = b.text.toPlainText();
//			if (str.indexOf("乳头") != -1 && str.indexOf("阴唇") != -1 && str.indexOf("阴茎") != -1) {
//				b.p("is yw");
//			} else {
//				b.removeAllTimer(-2222231);
//				return;
//			}
//			text.setAccicesable(false, false);
//			String currentDate = time.getCurrentDate("-");
//			String last = b.getStringValueFromSettings(appInfo.lastReadOfYW, "");
//			String cur_encrypt = null;
//			if(last == null || last.isEmpty()) {
//				try {
//					cur_encrypt = des.encrypt(currentDate);
//					settings.setValue(appInfo.lastReadOfYW, cur_encrypt);
//					last = currentDate;
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}else {
//				try {
//					last = des.decrypt(last);
//				} catch (Exception e) {
//					b.p(e.getMessage());
//				}
//			}
//			
//			if (last.equals(currentDate)) {
//				return;
//			}
//			else {
//				InputDialog id = new InputDialog(b, "需要凭据才能访问此文档", "请输入密码：", true, false, "") {
//
//					@Override
//					void whenOkButtonPressed() {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					void whenClose() {
//						// TODO Auto-generated method stub
//
//					}
//
//					@Override
//					void whenCannelButtonPressed() {
//						// TODO Auto-generated method stub
//
//					}
//				};
//				int exec = id.exec();
//				if (exec == 0) {
//					text.setAccicesable(false, false);
//					return;
//				} else {
//					if (!id.value().equals("527332367")) {
//						return;
//					}
//				}
//
//				try {
//					settings.setValue(appInfo.lastReadOfYW, cur_encrypt);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				text.setAccicesable(false, true);
//				bTextEdit aa = text;
//				if (!b.isHasTimer(-2222231)) {
//					timerInfo ti = new timerInfo(-2222231, 1800000, "close yw", new Runnable() {
//
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							b.uiRun(new Runnable() {
//								public void run() {
//									aa.setAccicesable(false, false);
//									aa.repaint();
//								}
//							});
//						}
//					}, false);
//					b.addTimer(ti);
//				}
//			}
//		 

	}

	static void conversionDocuments(black b) {
		if (!b.isAdmin())
			return;

		File f = new File(b.projectFile.getParent() + File.separator + "Files");
		File[] l = f.listFiles();
		for (File fi : l) {
			String name = fi.getName();
			String ex = ".black";
			if (name.lastIndexOf(ex) == name.length() - ex.length()) {
				b.p("开始转换文档...");
				String read = io.readBlackFileByLine(fi);
				io.writeBlackFile(fi, read, null);
			}
		}
	}

	/**
	 * 
	 * @param appname 进程名称，包括扩展名，例如game.exe
	 * @param force
	 */
	static void closeApp(String appname, boolean force) {
		String f = "";
		if (force)
			f = " /F";
		try {
			Runtime.getRuntime().exec("taskkill /IM " + appname + f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setUsePic() {
		String key = "";
		if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
			key = appInfo.useLightWindowBgImg;
		} else {
			key = appInfo.useDarkWindowBgImg;
		}

		boolean usebg = !b.getBooleanValueFromSettings(key, "true");

		b.settings.setValue(key, usebg + "");

		b.applyColorChange();
		b.setLoadFileMessage("使用窗口背景图片" + usebg);
	}

	void setUseBingPic() {
		String key_ = "";
		String key = "";
		if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
			key_ = appInfo.lightBingPic;
			key = appInfo.useLightWindowBgImg;
		} else {
			key_ = appInfo.darkBingPic;
			key = appInfo.useDarkWindowBgImg;
		}
		boolean usebg = b.getBooleanValueFromSettings(key, "true");
		if (!usebg) {
			b.getMessageBox("使用Bing每日图片", "要启用此功能必须先启用图片背景功能");
			return;
		}
		boolean usebingPic = !b.getBooleanValueFromSettings(key_, "true");
		b.settings.setValue(key_, usebingPic + "");
		if (usebingPic)
			b.ba.useBingPic();
		else
			b.applyColorChange();

		b.setLoadFileMessage("使用Bing每日图片" + usebingPic);
	}

	void useBingPic() {
		String picInfo = black.getStringValueFromSettings(appInfo.bingPicInfo, "");
		tempData.put("tempPicInfo", picInfo);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				timerInfo ti = new timerInfo(true, "获取Bing每日图片", null);
				ti.type = 333334;
				ti.setRunnable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						b.uiRun(b, new Runnable() {
							public void run() {
								Object object = tempData.get("bingPicInfo");
								if (object != null) {
									if (!black.getStringValueFromSettings(appInfo.bingPicInfo, "").equals(object))
										b.settings.setValue(appInfo.bingPicInfo, object);
								}
								b.applyColorChange();
							}
						});
					}
				});
				b.addTimer(ti);
				String info = (String) tempData.get("tempPicInfo");
				String picInfo = getBingPic(info);

				if (picInfo != null) {
					tempData.put("bingPicInfo", picInfo);
//					ti.timerName = "Bing每日图片获取成功";
					b.removeAllTimer(333334);
					ti.run.run();
				} else {
					ti.timerName = "Bing每日图片获取失败<br>继续使用旧的Bing图片";
					ti.time = System.currentTimeMillis() + 15000;
					ti.stilLive = false;
					ti.hideLeftTime = true;
				}

			}
		}).start();

	}

	void showBingPicInfo() {
		int v = b.getIntValueFromSettings(appInfo.randomImg, "1000");
		if(v == 0) {
			if (label == null) {
				label = new QLabel(b);
				QPalette p = label.palette();
				p.setColor(label.backgroundRole(), new QColor(0, 0, 0, 100));
				p.setColor(label.foregroundRole(), new QColor(255, 255, 255));
				label.setPalette(p);
				label.setFixedWidth(b.width());
				label.setWordWrap(true);
				label.setAutoFillBackground(true);
				label.setAttribute(WidgetAttribute.WA_TransparentForMouseEvents);
//				label.setContentsMargins(10, 20, 10, 20);
				label.setMargin(20);
				label.setAlignment(AlignmentFlag.AlignVCenter, AlignmentFlag.AlignLeft);

//				label.lower();
//				label.show();
			}
			QFont f = b.font();
			f.setPointSize(f.pointSize() + 6);
			String info = (String) settings.value(appInfo.bingPicInfo, "");

			label.setFont(f);
			label.setText(info);
			label.adjustSize();
			label.setGeometry(0, b.height() + label.height(), label.width(), label.height());
		}
		
		if (b.textWidget.isVisible()) {
//			b.textWidget.hide();
			tempData.put("oldTextWidgetGeometry", b.textWidget.geometry());
			new animation(b.textWidget, "geometry", b.textWidget.geometry(),
					new QRect(b.textWidget.x(), 0 - b.textWidget.height(), b.textWidget.width(), b.textWidget.height()),
					0) {

				@Override
				public void action_endAnimation() {
					// TODO Auto-generated method stub
					b.textWidget.hide();
					if(shadow != null)
					shadow.update();
				}

			};
			if(label != null && v == 0) {
				label.show();
				b.setAnimation(label, "geometry", label.geometry(),
					new QRect(0, b.height() - label.height(), label.width(), label.height()), b.getAnimationTime());
			}
		} else {
//			label.hide();
			QRect object = (QRect) tempData.get("oldTextWidgetGeometry");
			tempData.remove("oldTextWidgetGeometry");
			new animation(b.textWidget, "geometry", b.textWidget.geometry(), object, 0) {

				@Override
				public void action_endAnimation() {
					// TODO Auto-generated method stub
					if(label != null && label.isVisible())
					b.setAnimation(label, "geometry", label.geometry(),
							new QRect(0, b.height() + label.height(), label.width(), label.height()),
							b.getAnimationTime());
					
					b.textWidget.show();
					if(shadow != null)
					shadow.update();
				}

			};

		}
	}

	/**
	 * 下载bing每日图片和图片信息 可以放在另一个线程执行
	 * 先判断图片描述信息是否与settings里储存的一样，不一样说明网络上图片已经更新，下载图片，一样则返回图片信息，但不下载图片
	 * 
	 * @return 返回图片故事信息或null，通过判断返回值确定图片是否下载完成
	 */
	String getBingPic(String currentPicInfo) {
		String bingUrl = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
		File file = new File("./RC/Bing/bing.jpg");
		String html = null;
		try {
			html = imgWidget.getHtml(bingUrl, "utf-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			p("网络链接失败");
			return null;
		}
		b.debugPrint(html);
		if (html == null)
			return null;
		TextRegion url = cheakDocument.subString(html, "\"url\":\"", "\",");
		TextRegion info = cheakDocument.subString(html, "\"copyright\":\"", "\",");
		if (currentPicInfo.equals(info.text)) {
			if (file.exists())
				return info.text;
		}

		String URL = "www.bing.com" + url.text;
		b.debugPrint(info.text + "\n" + URL);
		try {
			if (!file.exists())
				file.getParentFile().mkdirs();

			String str = file.getAbsolutePath().toString();
			Process exec = Runtime.getRuntime().exec("./Tools/wget.exe -O \"" + str + "\" " + URL);
			try {
				exec.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info.text;
	}

	static void closeGame(black b) {
		if (b.isAdmin())
			return;
		closeApp("game.exe", true);
		closeApp("game.dat", true);

		if (firefoxRuning) {
			closeApp("firefox.exe", true);
			closeApp("FirefoxPortable.exe", true);
			closeApp("smplayer.exe", true);
		}
		if (!b.checkRunAsTool())
			return;
		try {
			Runtime.getRuntime()
					.exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:" + b.adminPasswd
							+ " /domain: /command:\"" + appInfo.javawPath
							+ " -jar .\\bin_\\getMail.jar disable\" /runpath:" + new File("").getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 阴影效果启用时必须关闭插入符动画效果，不然会导致页面更新不全
	 */
	void setShandow(boolean enable) {
		if (!b.getBooleanValueFromSettings(appInfo.usePageShadow, "false") || enable == false) {
			setShandowAction(false);
//			b.textWidget.setFrameStyle(QFrame.Shape.NoFrame.value());
		} else
			new bRunnable(5000, true, false, true, true) {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					setShandowAction(true);
//				b.textWidget.setFrameStyle(QFrame.Shape.StyledPanel.value());
					b.debugPrint("显示页面阴影效果");
				}
			};

	}

	void setShandowAction(boolean enable) {
		QGraphicsDropShadowEffect ge = (QGraphicsDropShadowEffect) b.textWidget.graphicsEffect();
		if (ge == null) {
			ge = new QGraphicsDropShadowEffect();
			QColor c_ge = new QColor(Qt.GlobalColor.black);
			c_ge.setAlpha(255);
			ge.setColor(c_ge);
			ge.setBlurRadius(50);
			ge.setOffset(0, 2);
		}

		if (b.writingView > 0) {
			QColor bg = b.textWidget.palette().color(ColorRole.Window);
			if (bg.alpha() < 100)
				ge.setEnabled(false);
			else
				ge.setEnabled(enable);
		} else
			ge.setEnabled(false);
		ge.update();
		b.textWidget.setGraphicsEffect(ge);
	}

	boolean shandow() {
		QGraphicsDropShadowEffect ge = (QGraphicsDropShadowEffect) b.textWidget.graphicsEffect();
		if (ge == null)
			return false;
		else {
			if (ge.isEnabled())
				return true;
			else
				return false;
		}
	}

	boolean fastFindUp() {
		QTextCursor tc = b.text.textCursor();
		if (tc.selectedText().length() == 0) {
			return false;
		}
		QTextDocument doc = b.text.document_b();
		QTextCursor find = doc.find(tc.selectedText(), tc,
				new QTextDocument.FindFlag[] { QTextDocument.FindFlag.FindBackward });
		if (find.position() != -1) {
			QTextCursor tcc = b.text.textCursor();
			tcc.setPosition(find.selectionStart());
			tcc.setPosition(find.selectionEnd(), QTextCursor.MoveMode.KeepAnchor);
			b.text.setTextCursor(tcc);
		}
		return true;
	}

	boolean fastFindDown() {
		QTextCursor tc = b.text.textCursor();
		if (tc.position() == text.charCountOfDoc()) {
			text.verticalScrollBar().setValue(text.verticalScrollBar().maximum());
		}
		if (tc.selectedText().length() == 0) {
			return false;
		}
		QTextDocument doc = b.text.document_b();
		QTextCursor find = doc.find(tc.selectedText(), tc);
		if (find.position() != -1) {
			QTextCursor tcc = b.text.textCursor();
			tcc.setPosition(find.selectionStart());
			tcc.setPosition(find.selectionEnd(), QTextCursor.MoveMode.KeepAnchor);
			b.text.setTextCursor(tcc);
		}
		return true;
	}

	static void disableWifi(black b) {
		if (!b.checkRunAsTool())
			return;
		try {
			Runtime.getRuntime()
					.exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:" + adminPasswd
							+ " /domain: /command:\"" + appInfo.javawPath
							+ " -jar .\\bin_\\getMail.jar disable\" /runpath:" + new File("").getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void whenBlackClose(black b) {
		checkUpdate update = (checkUpdate) b.ba.tempData.get("checkUpdate");
		if (update.exec != null)
			update.exec.destroy();
		closeGame(b);
	}

	static void changedSysVolume(int v) {
		if (v == -1)
			v = 65535;
		else if (v == -2)
			v = 0;
		else if (v == -3)
			v = 32767;
		try {
			Runtime.getRuntime().exec("./Tools/nircmdc.exe setsysvolume " + v);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void secDo(black b) {
		if (firefoxRuning) {
			if (b.hourOfDay < 20 || b.hourOfDay > 21) {
				firefoxRuning = false;
				closeApp("firefox.exe", true);
				closeApp("FirefoxPortable.exe", true);
				closeApp("smplayer.exe", true);
				if (!b.checkRunAsTool())
					return;
				try {
					Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:" + b.adminPasswd
							+ " /domain: /command:\"" + appInfo.javawPath
							+ " -jar .\\bin_\\getMail.jar disable\" /runpath:" + new File("").getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

//	static void Qexec() {
//		QApplication.exec();
//		
//	}
	static void insertList() {
		black.text.textCursor().insertList(Style.ListCircle);
	}


	void shadow() {
		if (b.getIntValueFromSettings(appInfo.RoundShadow, "0") == 0
				&& !b.getBooleanValueFromSettings(appInfo.usePageShadow, "false")) {
			if (shadow != null && shadow.isVisible()) {
				shadow.hide();
			}
			return;
		}

		if (shadow == null) {
			shadow = new QWidget(b) {
				@Override
				protected void paintEvent(QPaintEvent event) {
					// TODO Auto-generated method stub
					super.paintEvent(event);
					QPainter p = new QPainter(this);
//					int sizePS = 10;
//					float f = 255F/(float)sizePS;
//					QPainterPath path = new QPainterPath();
//					QColor c = new QColor(0,0,0);
//					QPen pen = new QPen();
//					
//					for(int i=0;i<=sizePS;i++) {
//						path.addRect(b.textWidget.x()-i, b.textWidget.y()-i, b.textWidget.width()+i*2, b.textWidget.height()+i*2);
//						float af = (i*f);
//						int a = (int)af;
//						if(af>0 && af<1) a = 0;
//						c.setAlpha(a);
//						pen.setColor(c);
//						p.setPen(pen);
//						p.drawPath(path);
//					}

//					if (b.getBooleanValueFromSettings(appInfo.usePageShadow, "false")
//							&& b.writingView > 0
//							&& b.textWidget.isVisible() && b.textWidget.palette().background().color().alpha() > 100
//							&& b.getStringValueFromSettings(appInfo.writingViewTextTopAndBottom, "0 0").equals("0 0")
//							&& b.getStringValueFromSettings(appInfo.typerMode, "3").equals("3")) {
//						int pageAlpha = 50;
//						int size = 50;
//						int y = b.textWidget.y();
//						// 绘制页面左阴影
//						int x_left = b.textWidget.x() - size;
//						QLinearGradient pageLeft = new QLinearGradient(x_left, y, b.textWidget.x(), y);
//						pageLeft.setColorAt(0, new QColor(0, 0, 0, 0));
//						pageLeft.setColorAt(1, new QColor(0, 0, 0, pageAlpha));
//						p.setPen(PenStyle.NoPen);
//						p.setBrush(new QBrush(pageLeft));
//						p.drawRect(x_left, y, size, b.textWidget.height());
//
//						// 绘制页面右阴影
//						int x_right = b.textWidget.x() + b.textWidget.width();
//						QLinearGradient pageRight = new QLinearGradient(x_right, y, x_right + size, y);
//						pageRight.setColorAt(0, new QColor(0, 0, 0, pageAlpha));
//						pageRight.setColorAt(1, new QColor(0, 0, 0, 0));
//						p.setPen(PenStyle.NoPen);
//						p.setBrush(new QBrush(pageRight));
//						p.drawRect(x_right, y, size, b.textWidget.height());
//
////						QLinearGradient pageTop = new QLinearGradient(b.textWidget.x(), y-size/2, b.textWidget.x(), y+size);
////						pageTop.setColorAt(0, new QColor(0, 0, 0, 0));
////						pageTop.setColorAt(1, new QColor(0, 0, 0, pageAlpha));
////						p.setPen(PenStyle.NoPen);
////						p.setBrush(new QBrush(pageTop));
////						p.drawRect(b.textWidget.x()-size,y-size/2,b.textWidget.width()+size*2,size/2);
//					}
					int v = b.getIntValueFromSettings(appInfo.RoundShadow, "0");

					if (v == 0) {
						return;
					}

					int size = 100;
					int darkAlpha = 200;
					int x = b.textWidget.x() + b.textWidget.width();
					if (v == 3 || v == 1) {
						QLinearGradient linearLeft = new QLinearGradient(0, 0, size, 0);
						linearLeft.setColorAt(0, new QColor(0, 0, 0, darkAlpha));
						linearLeft.setColorAt(1, new QColor(0, 0, 0, 0));

						p.setBrush(new QBrush(linearLeft));
						p.drawRect(b.rect());

						QLinearGradient linearRight = new QLinearGradient(b.width() - size, 0, b.width(), 0);
						linearRight.setColorAt(0, new QColor(0, 0, 0, 0));
						linearRight.setColorAt(1, new QColor(0, 0, 0, darkAlpha));

						p.setBrush(new QBrush(linearRight));
						p.drawRect(b.rect());

						QLinearGradient linearTop = new QLinearGradient(0, 0, 0, size);
						linearTop.setColorAt(0, new QColor(0, 0, 0, darkAlpha));
						linearTop.setColorAt(1, new QColor(0, 0, 0, 0));

						p.setBrush(new QBrush(linearTop));
						p.drawRect(b.rect());

						QLinearGradient linearBottom = new QLinearGradient(0, b.height() - size, 0, b.height());
						linearBottom.setColorAt(0, new QColor(0, 0, 0, 0));
						linearBottom.setColorAt(1, new QColor(0, 0, 0, darkAlpha));
						p.setBrush(new QBrush(linearBottom));
						p.drawRect(b.rect());
					}

					if (v == 3 || v == 2) {
						QRect r = b.rect();
						int xx = r.width() / 2;
						int yy = r.height() / 2;
						QRadialGradient gradient = new QRadialGradient(xx, yy, r.height(), xx, yy, r.height() / 2);
						gradient.setColorAt(0, new QColor(0, 0, 0, 0));
						gradient.setColorAt(0.5, new QColor(0, 0, 0, 200));
//						    gradient.setColorAt(0.7, new QColor(0, 0, 0,150));
						gradient.setColorAt(1, new QColor(0, 0, 0, 255));

						p.setPen(PenStyle.NoPen);
						// 设置画刷填充
						QBrush brush = new QBrush(gradient);
						p.setBrush(brush);
						p.fillRect(rect(), p.brush());
					}

				}
			};

			shadow.setWindowFlags(WindowType.FramelessWindowHint);
//			WindowFlags windowFlags = shadow.windowFlags();
//			windowFlags.clear(WindowType.Tool);
//			shadow.setWindowFlags(windowFlags);
			shadow.setAttribute(WidgetAttribute.WA_TransparentForMouseEvents);
			shadow.setAttribute(WidgetAttribute.WA_InputMethodTransparent);
			shadow.setAutoFillBackground(false);
			shadow.setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground);
			shadow.setAttribute(WidgetAttribute.WA_NoSystemBackground, false);
			QPalette p = shadow.palette();
			QColor c = new QColor(Qt.GlobalColor.black);
			c.setAlpha(0);
			p.setColor(shadow.backgroundRole(), c);
			shadow.setPalette(p);
		}
		shadow.stackUnder(b.timeMessage);
		shadow.setGeometry(desktopGeometry(false));
		shadow.show();
		shadow.update();
	}

	void test() {
//		b.execCommand("$bc 自动滚屏 50");
//		b.infoOfCurrentEditing.charCount = 0;
//		Collection<QDockWidget> ds = new ArrayList<QDockWidget>();
//		ds.add(b.dock);
//		Collection<Integer> size = new ArrayList<>();
//		size.add(b.dock.width()+20);
//		b.resizeDocks(ds, size, Orientation.Horizontal);
	}

	void showSegmentHelp() {

		b.getBMessageBox("自动分词词性信息", "如果要自定义自动分词时要接纳哪些词性的词汇来作为关键词自动完成，可参考此信息来配置[分词词性过滤]命令。\n" + appInfo.segmentInfo);
	}

	/**
	 * 设定自动分词过滤的词性
	 */
	void setSegmentInfo() {
		String info = b.getStringValueFromSettings(appInfo.infoOfAutoSegment, "");
		bmessageBox box = new bmessageBox(b, "编辑分词词性", "保存",
				"在下一段写上哪些词性的词汇可以作为关键词被自动完成。把需要的词性都写在同一个段落内，词性之间以空格割开。例如：n zg（名词和状态词）。"
						+ "要查询词性简写及其含义请使用[分词词性信息]命令。如果需要所有词性的词汇将下一行留空即可。只填写少量的词性可以提高效能并加快自动完成对话框弹出的速度。\n" + info,
				false) {

			@Override
			public void buttonPressedAction(String paramString) {
				// TODO Auto-generated method stub
				List<String> allLine = cheakDocument.getAllLine(paramString);
				String string = "";
				if (allLine.size() == 2) {
					string = allLine.get(1);
				}
				b.settings.setValue(appInfo.infoOfAutoSegment, string);
				this.close();
			}
		};
		box.show();
	}

	/**
	 * 获取编辑器文本的分词结果
	 * 
	 * @param onlyAnsjResult 是否只返回文档分词结果，即结果中不包含已经定义的关键词
	 * @return
	 */
	ArrayList<TextRegion> getAnsjResult(boolean onlyAnsjResult) {
		b.debugPrint("获取分词结果");
		ArrayList<TextRegion> al_ = new ArrayList<>();
		uiRun(b, new Runnable() {
			public void run() {
				int charCountOfDoc = text.charCountOfDoc();
				if (charCountOfDoc < b.getIntValueFromSettings(appInfo.maxCharsAutoSegment, "20000")) {
					tempData.put("stopSegment", false);
					tempData.put("textStr", text.toPlainText());
					int minLenght = b.getIntValueFromSettings(appInfo.minLenghtOfAutoSegment, "1");
					tempData.put(appInfo.minLenghtOfAutoSegment, minLenght);
					String info = b.getStringValueFromSettings(appInfo.infoOfAutoSegment, "all");
					tempData.put(appInfo.infoOfAutoSegment, info);
				} else
					tempData.put("stopSegment", true);
			}
		});
		ArrayList<String> al = new ArrayList<>();
		if (!(boolean) tempData.get("stopSegment")) {
			Segment segment = new StandardTokenizer().SEGMENT.enableAllNamedEntityRecognize(true);

			List<com.hankcs.hanlp.seg.common.Term> seg = segment.seg((String) tempData.get("textStr"));
			if (seg == null)
				return al_;

			String info = (String) tempData.get(appInfo.infoOfAutoSegment);
			String[] args = cheakDocument.checkCommandArgs(info);

			int minLenght = (int) tempData.get(appInfo.minLenghtOfAutoSegment);
			for (int i = 0; i < seg.size(); i++) {
				Term term = seg.get(i);
				String word = term.word; // 拿到词
				String nature = term.nature.toString();

				if (word.length() > minLenght) {
					if (args.length == 0)
						al.add(word);
					else
						for (String s : args) {
							if (nature.equals(s)) {
								al.add(word);
								break;
							}

						}

				}
			}
		}

		if (!onlyAnsjResult && b.marktext != null) {
			ArrayList<String> allLine = cheakDocument.getAllLine(b.marktext);
			ArrayList<String> al_string = new ArrayList<String>();

			for (int i = b.markstat.size() - 1; i >= 0; i--) {
				markstat stat = b.markstat.get(i);
				al_string.add(stat.text);
			}

			al_string.addAll(allLine);
			al_string.addAll(al);
			al = al_string;
		}
		ArrayList<String> withOutSame = cheakDocument.withOutSame(al);
		for (String s : withOutSame) {
//        	System.out.println("去重后："+s);
//        	System.out.println(s);
			ArrayList<String> a = cheakDocument.checkCommandArgs(s, '@');
			TextRegion tr = new TextRegion(s, 0, 0);

			if (a.size() == 1)
				tr.showname = s;
			else {
				tr.showname = a.get(0);
				tr.filename = a.get(1);
			}
//        	if(ar.size() == 1)	
//        		tr.showname = ar.get(0);
//        	else {
//            	tr.showname = ar.get(0);
//        		tr.filename = ar.get(1);
//        	}
			al_.add(tr);
		}
		return al_;
	}

	void showKeywordsListByCurrentChar() {
		if (b.afterLoadFile != null)
			return;
		if (!b.getBooleanValueFromSettings(appInfo.autoShowKeywords, "true"))
			return;

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				uiRun(b, new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						int ms = b.getIntValueFromSettings(appInfo.pinyinStartSegmentMS, "500");
						tempData.put(appInfo.pinyinStartSegmentMS, ms);
					}
				});

				if (b.findInMark_brun != null) {
					b.findInMark_brun.stop();
					b.findInMark_brun = null;
				}

				b.findInMark_brun = new bRunnable((int) tempData.get(appInfo.pinyinStartSegmentMS), true, false, true,
						true) {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (b.findInMark_brun != null) {
							showKeywordsListByCurrentChar_();
						}
					}
				};
			}
		}).start();
	}

	/**
	 * 通过光标所在的字符查找关键词
	 */
	void showKeywordsListByCurrentChar_() {
//		b.uiRun(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				tempData.put("textWidgetVisible", b.text.isVisible());
//			}
//		});
//		if(!(boolean)tempData.get("textWidgetVisible"))return;

		if (!b.text.isVisible())
			return;
		QTextCursor tc = text.textCursor();
		if (tc.hasSelection())
			return;
		if (!tc.movePosition(MoveOperation.PreviousCharacter, MoveMode.KeepAnchor))
			return;
		String findStr = tc.selectedText();
		if (findStr.isEmpty())
			return;

		if ((b.keywords != null) && (b.keywords.isVisible())) {
			return;
		}
		if (b.marktext == null) {
			b.readMarkFile();
		}
		if (b.marktext == null) {
			return;
		}
		if (b.markTextData == null) {
			b.getMarkFileText();
		}
		b.initKeyWordsDialog();
		b.keywords.tree.UsedToProjectPanelOrKeywordsList = 1;
		if (b.keywords.tree.topLevelItemCount() > 0) {
			b.keywords.tree.clear();
		}
		ArrayList<TextRegion> result = getAnsjResult(false);

		int index = 0;
		for (TextRegion tr : result) {
			int ind = 0;
			if ((ind = tr.showname.lastIndexOf(findStr)) != -1) {
				QTextCursor tcc = new QTextCursor(tc);
				tcc.clearSelection();
				int start = tcc.position() - ind;
//				if(start < 0) start = 0;
				tcc.setPosition(start);
				int end = tcc.position() + tr.showname.length();
//				if(end > text.charCountOfDoc())end = text.charCountOfDoc();
				tcc.setPosition(end, MoveMode.KeepAnchor);
				b.debugPrint("selectText: " + tcc.selectedText() + " showname:" + tr.showname + " start:" + start
						+ " end:" + end);
				if (tcc.selectedText().equals(tr.showname))
					continue;

				QTreeWidgetItem ti = b.getTreeItem(b.keywords.tree);
				ti.setIcon(0, b.ico_keywordNo);

				String str = null;
				if (tr.filename == null) {
					str = tr.showname;
				} else {
					str = tr.showname + " (" + tr.filename + ")";
				}
				ti.setText(0, b.getCharByNumber(b.keywords.tree.topLevelItemCount() - 1) + str);
				ti.setData(1, 0, tr);
				ti.setData(1, 1, Integer.valueOf(index));
				index++;
			}
		}
		b.keywords.statusbar.showMessage("Alt+大写字母序号可输入对应项");
		b.keywords.message.setText(b.keywords.tree.topLevelItemCount() + "条");
		b.findview = 5;
		b.showDialogAtEditorCaretPos(text, b.keywords);
	}

	static float getWindowsVersion() {
		Properties properties = System.getProperties();
		String property = System.getProperty("os.version");
		return Float.valueOf(property);
	}

	/**
	 * 用来从RC目录选择图片改变背景的方法
	 */
	void changeBackgroundImage() {
		if (b.writingView != 1)
			return;
		String key = "";
		String key_ = "";
		if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
			key = appInfo.lightWindowBgImg;
			key_ = appInfo.lightBingPic;
		} else {
			key = appInfo.darkWindowBgImg;
			key_ = appInfo.darkBingPic;
		}
		boolean usebingPic = b.getBooleanValueFromSettings(key_, "true");
		if (usebingPic)
			return;

		File dir = new File("./RC");
		isYourNeedFile isYourNeedFile = new isYourNeedFile(dir, ".*.jpg|.*.jpg|.*.jpeg|.*.JPEG|.*.png|.*.PNG");
		String[] files = isYourNeedFile.Filter();
		String bg = b.getStringValueFromSettings(key, "bgF.jpg");
		for (int i = 0; i < files.length; i++) {
			String s = files[i];
			if (bg.equals(s)) {
				int nextIndex = i + 1;
				if (nextIndex < files.length) {
					b.settings.setValue(key, files[nextIndex]);
					b.bgImgChanged = true;
					b.applyColorChange();
				} else {
					b.settings.setValue(key, files[0]);
					b.bgImgChanged = true;
					b.applyColorChange();
				}
				return;
			} else {
				b.settings.setValue(key, files[0]);
				b.bgImgChanged = true;
				b.applyColorChange();
			}
		}
	}

	static void setDisable() {
		File file = new File("./bin_/no");
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}

	static boolean isDisable() {
		File file = new File("./bin_/no");
		if (file.exists())
			return true;
		else
			return false;
	}

	static boolean checkDateDisable() {
		Calendar instance = Calendar.getInstance(Locale.getDefault());
		int year = instance.get(Calendar.YEAR);
		int month = instance.get(Calendar.MONTH);
		System.out.println("当前系统时间为" + year + "年" + month + "月");
		File license = new File("./bin_/license");
		if (license.exists()) {
			String s = io.readTextFile(license, "gbk");
			List<String> allLine = cheakDocument.getAllLine(s);
			DesUtils des = new DesUtils("blackLicense");
			String y = "", m = "";
			try {
				y = des.decrypt(allLine.get(0));
				m = des.decrypt(allLine.get(1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				black.p("许可数据出现错误");
			}

			if (!y.isEmpty() && !m.isEmpty()) {
				appInfo.outYear = Integer.valueOf(y);
				appInfo.outMonth = Integer.valueOf(m);
			}
		}
		String outmessage = "过期时间为";
		if (appInfo.outYear == -1 || appInfo.outMonth == -1) {
			outmessage = outmessage + "永不过期";
		} else {
			outmessage = outmessage + appInfo.outYear + "年" + (appInfo.outMonth - 1) + "月";
		}

		System.out.println(outmessage);
		if (appInfo.outYear == -1)
			return false;
		// 大于年；小于年不判断月；等于年判断月
		else if (year > appInfo.outYear) {
			setDisable();
			return true;
		} else if (year == appInfo.outYear && month >= appInfo.outMonth) {
			setDisable();
			return true;
		}
		return false;
	}

	/**
	 * 用特定密钥加密信息 弹出一个文本输入框，输入密钥和信息，然后加密之
	 * 
	 * @param b
	 */
	static void desInfo(black b) {
		InputDialog id = new InputDialog(b, "加密文本", "输入密钥和需要加密的文本\n(两者间用空格隔开)", false, false, "") {

			@Override
			void whenOkButtonPressed() {
				// TODO Auto-generated method stub
				String[] args = cheakDocument.checkCommandArgs(value());
				if (args.length == 2 && !args[0].isEmpty() && !args[1].isEmpty()) {
					DesUtils des = new DesUtils(args[0]);
					try {
						String encrypt = des.encrypt(args[1]);
						new bmessageBox(this, "加密后的信息", "确定", encrypt, true) {

							@Override
							public void buttonPressedAction(String paramString) {
								// TODO Auto-generated method stub
								this.close();
							}
						}.show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			void whenClose() {
				// TODO Auto-generated method stub
				this.close();
			}

			@Override
			void whenCannelButtonPressed() {
				// TODO Auto-generated method stub

			}
		};
		id.noHide = true;
		id.show();
	}

	/**
	 * 为程序元素设置颜色
	 * 
	 * @param w0：页面
	 * @param alpha 颜色透明度
	 * @param color
	 */
	void setColors(int w, int alpha) {
		switch (w) {
		case 0:// 页面背景颜色
			String key0 = "";
			if (b.writingView > 0) {
				if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false"))
					key0 = appInfo.lightPageColor;
				else
					key0 = appInfo.darkPageColor;
			}

			QColor c0 = getColorDialog((QColor) black.settings.value(key0, new QColor(Qt.GlobalColor.white)));
			if (c0 != null) {
				c0.setAlpha(alpha);
				b.settings.setValue(key0, c0);
				b.applyColorChange();
				b.update();
			}
			break;
		case 1:// 页面文字颜色
			String key1 = "";
			if (b.writingView > 0) {
				if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false"))
					key1 = appInfo.lightTextColor;
				else
					key1 = appInfo.darkTextColor;
			}

			QColor c1 = getColorDialog((QColor) black.settings.value(key1, new QColor(Qt.GlobalColor.black)));
			if (c1 != null) {
				b.settings.setValue(key1, c1);
				b.applyColorChange();
				b.update();
			}
			break;
		case 2:// 插入符颜色
			String key2 = "";
			if (b.writingView == 0)
				key2 = appInfo.caretColor_normal;
			else {
				if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false"))
					key2 = appInfo.lightCaretColor;
				else
					key2 = appInfo.darkCaretColor;
			}

			QColor c2 = getColorDialog((QColor) black.settings.value(key2, text.palette().color(ColorRole.Text)));
			if (c2 != null) {
				b.settings.setValue(key2, c2);
				b.applyColorChange();
				b.update();
			}
			break;
		case 3:// 窗口背景颜色
			String key3 = "";
			if (b.writingView > 0) {
				if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false"))
					key3 = appInfo.lightWindowColor;
				else
					key3 = appInfo.darkWindowColor;
			}

			QColor c3 = getColorDialog((QColor) black.settings.value(key3, new QColor(Qt.GlobalColor.black)));
			if (c3 != null) {
				c3.setAlpha(alpha);
				b.settings.setValue(key3, c3);
				b.applyColorChange();
				b.update();
			}
			break;
		case 4:// 屏幕提示颜色
			String key4 = "";
			if (b.writingView > 0) {
				if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false"))
					key4 = appInfo.lightLoadMessageColor;
				else
					key4 = appInfo.darkLoadMessageColor;
			}

			QColor c4 = getColorDialog((QColor) black.settings.value(key4, new QColor(Qt.GlobalColor.black)));
			if (c4 != null) {
//				c4.setAlpha(alpha);
				b.settings.setValue(key4, c4);
				b.applyColorChange();
				b.update();
			}
			break;
		case 5:// 右下角提示信息颜色
			String key5 = "";
			if (b.writingView > 0) {
				if (b.getBooleanValueFromSettings(appInfo.LightOrDark, "false"))
					key5 = appInfo.lightTimeMessageColor;
				else
					key5 = appInfo.darkTimeMessageColor;
			}

			QColor c5 = getColorDialog((QColor) black.settings.value(key5, new QColor(Qt.GlobalColor.black)));
			if (c5 != null) {
//				c5.setAlpha(alpha);
				b.settings.setValue(key5, c5);
				b.applyColorChange();
				b.update();
			}
			break;
		}

	}

	/**
	 * 返回桌面尺寸
	 * 
	 * @param available 是否只返回可用尺寸（不包括任务栏）
	 * @return
	 */
	static QRect desktopGeometry(boolean available) {
		QDesktopWidget dw = new QDesktopWidget();
		if (available)
			return dw.availableGeometry();
		else
			return dw.geometry();
	}

	void openUrl() {
		openUrl(appInfo.appWebsite);
	}

	/**
	 * 使用默认浏览器打开指定的url
	 */
	void openUrl(String url) {
		QDesktopServices ds = new QDesktopServices();
		ds.openUrl(new QUrl(url));
	}

	/**
	 * 启用禁用页面边线（写作视图）
	 */
	void useTextBorder() {
		boolean v = (!b.getBooleanValueFromSettings(appInfo.textbgborder, "false"));
		settings.setValue(appInfo.textbgborder, v + "");
		b.update();
	}

	/**
	 * 启用禁用页面背景（写作视图）
	 */
	void useTextBg() {
		boolean v = (!b.getBooleanValueFromSettings(appInfo.useBgColor, "true"));
		settings.setValue(appInfo.useBgColor, v + "");
		b.update();
	}

	/**
	 * 弹出一个颜色对话框
	 * 
	 * @return返回所选的颜色
	 */
	QColor getColorDialog(QColor c) {
		QColorDialog cd = new QColorDialog();
		if (c != null)
			cd.setCurrentColor(c);
		cd.exec();
		return cd.selectedColor();
//		b.debugPrint(cd.selectedColor());

	}

	/**
	 * 返回界面基准字体字号
	 */
	void getUIFontSize() {

	}

	/**
	 * 设置界面字体大小 使程序可以在高分辨率屏幕上显示
	 */
	void setUIFontSize() {
		QFont font = QApplication.font();
		if (font.pointSize() == 9) {
			font.setPointSize(b.getIntValueFromSettings(appInfo.UIfontSize, "12"));
			b.settings.setValue(appInfo.useBigUIFont, "true");
		} else {
			font.setPointSize(9);
			b.settings.setValue(appInfo.useBigUIFont, "false");
		}
		applyUIFont(font);
	}

	void applyUIFont(QFont font) {
		QApplication.setFont(font);
		b.treetitle.setFontSize();
		b.textTitle.setFontSize();
		b.findLine.setFont(font);
		b.statusbar.setMaximumHeight(font.pointSize() + 10);
	}

//	void getMessageFromMailBox(String host, String username, String password, String port) {
//		Message[] messages = null;
//		String head = "black_qt新版本";
//		String disable = "black_qt过滤";
//		float current = Float.valueOf(appInfo.appVersion).floatValue();
//		float newver = 0.0F;
//		int index = 0;
//		try {
//			messages = mailTool.receive(username, password, host, port);
//		} catch (Exception e) {
//			b.logsmessage.append(
//					time.getCurrentDate("-") + " " + time.getCurrentTimeHasSecond() + " " + e.getMessage() + "<br>");
//			b.hasUpdate = 0;
//			return;
//		}
//
//		for (int i = 0; i < messages.length; i++) {
//			showMail re = new showMail((MimeMessage) messages[i]);
//			String subject = null;
//			try {
//				subject = re.getSubject();
//			} catch (MessagingException e) {
//				e.printStackTrace();
//			}
//			if ((subject != null) && (subject.indexOf(head) != -1)) {
//				String newversion = subject.replace(head, "");
//				Float ver = Float.valueOf(newversion);
//				if (ver.floatValue() > newver) {
//					newver = ver.floatValue();
//					index = i;
//				}
//			}
//			if ((subject != null) && (subject.indexOf(disable) != -1)) {
//				String ver_ = subject.replace(disable, "");
//				Float ver = Float.valueOf(ver_);
//				if (current <= ver) {
//					bAction.setDisable();
//					System.exit(0);
//				}
//			}
//		}
//		if (newver > current) {
//			showMail mail = new showMail((MimeMessage) messages[index]);
//			try {
//				mail.getMailContent(messages[index], false);
//			} catch (Exception e1) {
//				b.logsmessage.append("[" + time.getCurrentDate("-") + " " + time.getCurrentTimeHasSecond() + "]"
//						+ e1.getMessage() + "<br>");
//				e1.printStackTrace();
//			}
//			try {
//				b.updateinfo = ("<b>发现新版本：" + newver + "</b><br><br><i>" + mail.getBodyText() + "</i>");
//				b.hasUpdate = 1;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			b.hasUpdate = 0;
//		}
//	}

	/**
	 * 在当前编辑的文档内替换文本 返回替换的数量
	 * 
	 * @param find
	 * @param replace
	 * @param text
	 * @param flags
	 * @return
	 */
	static int replaceAll_doc(black b, String find, String replace, bTextEdit text, FindFlags flags, boolean noTip) {
		/**
		 * 因为下面的原因不能使用java String类的搜索替换功能： 必须以QTextDocument类能够接受的搜索参数进行搜索
		 * 必须考虑替换后文档的排版问题，所以对原文档内的字符进行位置索引后再批量替换是最好的选择
		 * 
		 * 
		 */
		ArrayList<int[]> al = new ArrayList<int[]>();
		QTextDocument doc = text.document_b();
		QTextCursor ttc = new QTextCursor(doc);
		ttc.setPosition(0);
		do {
			ttc = doc.find(find, ttc, flags);
			if (ttc.position() != -1) {
				int[] a = { ttc.selectionStart(), ttc.selectionEnd() };
				al.add(a);
			}
		} while (ttc.position() != -1);
		QTextCursor tc = text.textCursor();
		// 合并所有替换文本为一次更改
		tc.beginEditBlock();
		// 不应该从前向后替换，因为被替换文本与替换文本长度可能不一样，导致后面要替换的文本的位置发生了变化，所以应该从后向前替换

		for (int a = al.size() - 1; a >= 0; a--) {
			int[] i = al.get(a);
			tc.setPosition(i[0]);
			tc.setPosition(i[1], MoveMode.KeepAnchor);
			tc.insertText(replace);
		}
		tc.endEditBlock();
		if (!noTip)
			b.getMessageBox("全部替换", "替换了" + al.size() + "项");
		return al.size();
	}

	/**
	 * 替换文件集中的全部匹配文本。包含文件集本身，但不含关键词列表
	 * 
	 * @param b
	 * @param find
	 * @param replace
	 * @param text
	 * @param flags
	 */
	static void replaceAll_files(black b, String find, String replace, bTextEdit text, FindFlags flags) {
		StringBuffer sb = new StringBuffer();
		int allCount = 0;
		fileInfo info_files = null;
		// 替换当前文件集中的所有匹配文本
		ArrayList<String> al = null;
		// 如果当前文件就是文件集，则获取当前文件的所有子文件。如果当前文件不是文件集，则查找并获取父文件集中的所有文件
		if (b.infoOfCurrentEditing.isFiles) {
			info_files = b.infoOfCurrentEditing;
			al = b.getAllFiles(b.infoOfCurrentEditing);
		} else {
			// 查找当前所编辑的文件所处的文件集
			info_files = b.getFilesWhichCurrentIn(b.TreeWidgetItemOfCurrentEditing);

			// 如果没有搜索到文件集，则退出方法
			if (info_files == null)
				return;

			al = b.getAllFiles(info_files);
		}

		// 把文件集本身也加入要替换的文件列表中
		al.add(b.getFile(info_files.fileName).getPath());

		if (al == null)
			return;

		for (String f : al) {
			// 跳过关键词列表
			if (b.findFileInfoByFileName(new File(f).getName(), info_files).isKeyWordsList)
				continue;

			sb.append("<b><i>" + b.findFileShowName(new File(f).getName()) + "</i></b>");

			dataForOpenedFile opened = b.findInOpenedFiles(new File(f).getName());

			// 先判断是不是当前正在编辑的文件，因为正在编辑的文件是没有同步到缓存数据的
			if (f.equals(b.currentEditing.toPath())) {
				int count = replaceAll_doc(b, find, replace, text, flags, true);
				sb.append(" - 替换了" + count + "项<br>");
				allCount += count;
				// 判断文件是否已经被缓存
			} else if (opened != null && opened.editor != null) {
				int count = replaceAll_doc(b, find, replace, opened.editor.text, flags, true);
				opened.content = opened.editor.text.toPlainText();
				sb.append(" - 替换了" + count + "项<br>");
				allCount += count;
				// 文件还没有被读取
			} else {
				String str = null;
				if (opened != null && opened.editor == null)
					str = opened.content;
				else
					str = b.readBlackFile(new File(f));

				QTextDocument doc = new QTextDocument(str);
				QTextCursor tc = new QTextCursor(doc);
				tc.setPosition(0);
				int count = 0;
				while (tc.position() != -1) {
					tc = doc.find(find, tc, flags);
					if (tc.position() != -1) {
						tc.insertText(replace);
						count++;
					}
				}
				if (opened != null && opened.editor == null)
					opened.content = doc.toPlainText();
				else
					io.writeBlackFile(new File(f), doc.toPlainText(), null);
				sb.append(" - 替换了" + count + "项\n");
				allCount += count;
			}
		}
		sb.append("\n共计替换了" + allCount + "项");
		b.getBMessageBox("在文件集中替换的结果", sb.toString());
	}

	/**
	 * 供项目树导出菜单项调用，将选择的项目导出为一个文件 如果选择了多个项目，选择的项目都必须是同一类型的项目才允许导出
	 * 允许的导出的项目的类型为文件、文件集，还有目录
	 */
	void export() {
		// 获取要导出的文件的fileinfo，放到一个数组里
		ArrayList<fileInfo> al_files = new ArrayList<fileInfo>();
		List<QTreeWidgetItem> checkedItems = b.getCheckedItems();
		if (checkedItems.isEmpty()) {
			List<QTreeWidgetItem> selectedItems = b.tree.selectedItems();
			if (!selectedItems.isEmpty()) {
				QTreeWidgetItem item = selectedItems.get(0);
				al_files.add(b.getFileInfoByQTreeItem(item));
			}
		} else {
			for (QTreeWidgetItem it : checkedItems) {
				al_files.add(b.getFileInfoByQTreeItem(it));
			}
		}
		// 如果没有选择任何文件，则给出提示，并结束导出任务
		if (al_files.isEmpty()) {
			b.getMessageBox("导出", "请先在文件篓中选择所要导出的文件");
			return;
		}
//		//判断所要导出的文件的属性，是文件还是文件集还是目录。如果要导出多个项目，必须都是相同类型的项目才能导出
//		boolean isFile = true,isFiles = true,isDir = true;
//		for(fileInfo in:al_files) {
//			if(!in.isFile) {
//				isFile = false;
//				break;
//			}
//		}
//		if(!isFile) {
//			for(fileInfo in:al_files) {
//				if(!in.isFiles) {
//					isFiles = false;
//					break;
//				}
//			}
//		}
//		if(!isFiles) {
//			for(fileInfo in:al_files) {
//				if(!in.isDir) {
//					isDir = false;
//					break;
//				}
//			}
//		}
//		//都是文件
//		if(isFile) {
//			System.out.println("都是文件");
//		//都是文件集
//		}else if(isFiles) {
//			System.out.println("都是文件集");
//		//都是目录
//		}else if(isDir) {
//			System.out.println("都是目录");
//		//选择了多种类型的文件，给出提示，并结束导出
//		}else {
//			System.out.println("选择了多种文件类型");
//		}

		String filter0 = "纯文本文件(*.txt)";
		String filter1 = "网页文件(*.html)";
		QFileDialog fd = new QFileDialog(b, "导出");
		fd.setFilter(Filter.valueOf(filter0), Filter.valueOf(filter1));

		fd.exec();
		if (fd.selectedFiles().isEmpty())
			return;

		String saveFileName = fd.selectedFiles().get(0).toString();

		b.saveAllDocuments();

		StringBuffer all = new StringBuffer();
		for (fileInfo in : al_files) {
			if (in.fileName != null && !in.fileName.isEmpty()) {
				String str = io.readBlackFileByLine(b.getFile(in.fileName));
				all.append(str);
			}
		}
		// 判断用户选择的文件过滤器，并保存为相应格式的文件
		if (fd.selectedNameFilter().equals(filter0)) {
			// 保存为纯文本文件
			io.writeTextFile(new File(saveFileName), all.toString(), "gbk");
		} else if (fd.selectedNameFilter().equals(filter1)) {
			// html文件
			QTextDocument doc = new QTextDocument(all.toString());
			io.writeTextFile(new File(saveFileName), doc.toHtml(), "utf-8");
		}
	}

	public void typerMode(black b) {
		int typerMode = 3;
		if ((typerMode = b.getIntValueFromSettings(appInfo.typerMode, "3")) == 3)
			return;

		if (b.writingView == 0 || b.isLoadFile || b.doNotSet_UnSave || b.btext.pos == b.btext.lastpos
				|| b.afterLoadFile != null) {
//			if(tempData.containsKey("testX"))
//			tempData.remove("testX");
			return;
		}
		if (!tempData.containsKey("testX")) {
			b.debugPrint("A " + b.textWidget.geometry());
//			tempData.put("textX", b.textWidget.geometry());
//			b.textWidget.setGeometry(b.textWidget.x(), b.textWidget.y(), b.textWidget.width(), b.textWidget.height());
		}

		QRect cr = b.text.cursorRect();
		QPoint mapTo = black.text.mapTo(b, new QPoint(cr.x(), cr.y()));
		QRect ag = new QDesktopWidget().availableGeometry();

		int cenX = ag.width() / 2;
		int cenY = ag.height() / 2;

//		QRect oldGeo = (QRect) tempData.get("testX");
		int width = b.getWritingViewTextWitdh() / 2;

		QRect oldGeo = null;
		if (oldGeo == null)
			oldGeo = b.textWidget.geometry();
		int mX = oldGeo.x() + (cenX - mapTo.x());
		int mY = oldGeo.y() + (cenY - mapTo.y());
		if (typerMode == 0)
			mX = oldGeo.x();
		else if (typerMode == 1)
			mY = oldGeo.y();

		QRect start = b.textWidget.geometry();
		QRect end = new QRect(mX, mY, start.width(), start.height());
//		System.out.println(start.width(),starth);
		b.debugPrint("B " + start + " end: " + end);
		b.setAnimation(b.textWidget, "geometry", start, end, b.getAnimationTime());
	}

	public static void setUpdate() {
		String appDir = "./";
		String inputFiledir = "./update";
		File javaexePath = new File("./jre/bin/java.exe");
		File blackexePath = new File("./BlackDraft.exe");
		File updatefiles = new File(inputFiledir);
		if (!updatefiles.exists())
			return;

		File[] list = updatefiles.listFiles();
		isYourNeedFile isYourNeedFile = new isYourNeedFile(updatefiles, ".*.zip");
		String[] filter = isYourNeedFile.Filter();
		for (String s : filter) {
			File f = new File(updatefiles.getAbsolutePath() + File.separator + s);
//			System.out.println(f);
			String name = f.getName();
			int lastIndexOf = name.lastIndexOf(".");
			name = name.substring(0, lastIndexOf);
			Float valueOf = Float.valueOf(name);
			if (valueOf > Float.valueOf(appInfo.appVersion)) {
				System.out.println("设置新版本：" + appInfo.appVersion + " -> " + name);
				// 启动另一个虚拟机执行jar文件，检查当前虚拟机是否已经退出，确认退出后解压缩更新包覆盖原有文件

				File appdir = new File(appDir);
				try {
					Runtime.getRuntime().exec(javaexePath + " -jar ./bin_/setUpdate.jar " + appdir.getAbsolutePath()
							+ " " + f + " " + blackexePath.getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.exit(0);
				break;
			} else {
				f.delete();
//				String n = f.getName();
//				int lastIndexOf2 = n.lastIndexOf(".");
//				n = n.substring(0, lastIndexOf2);
//				f.renameTo(new File(f.getParentFile().getAbsolutePath()+File.separator+n+".backup"));
			}
		}

	}

	public void reStartApp() {
		// TODO Auto-generated method stub
		b.closeAct(null);
		try {
			Runtime.getRuntime().exec("./BlackDraft.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
