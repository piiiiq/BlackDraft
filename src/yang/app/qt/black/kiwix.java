package yang.app.qt.black;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.qt.core.QEvent;
import io.qt.core.QObject;
import io.qt.core.QSize;
import io.qt.core.QUrl;
import io.qt.core.Qt;
import io.qt.core.Qt.Orientation;
import io.qt.core.Qt.WindowFlags;
import io.qt.core.Qt.WindowType;
import io.qt.widgets.QAction;
import io.qt.widgets.QApplication;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QKeyEvent;
import io.qt.gui.QKeySequence.StandardKey;
import io.qt.gui.QPalette;
import io.qt.gui.QTextDocument.FindFlag;
import io.qt.widgets.QTreeWidgetItem;
import io.qt.network.QNetworkAccessManager;
import io.qt.network.QNetworkAccessManager.NetworkAccessibility;
import io.qt.network.QNetworkConfiguration;
import io.qt.network.QNetworkProxy;
import io.qt.network.QNetworkReply;
import io.qt.network.QNetworkRequest;
import io.qt.network.QNetworkRequest.Attribute;
import io.qt.webkit.QWebFrame;
import io.qt.webkit.QWebHistory;
import io.qt.webkit.QWebHistoryItem;
import io.qt.webkit.QWebPage;
import io.qt.webkit.QWebView;
import io.qt.webkit.QWebPage.LinkDelegationPolicy;
import io.qt.webkit.QWebPage.WebAction;
import io.qt.webkit.QWebPage_ExtensionOption;

import net.sourceforge.pinyin4j.PinyinHelper;
import yang.demo.allPurpose.DesUtils;

public class kiwix extends QObject {
	Process kiwixserver;
	String kiwixserverLibrary = "D:\\Apps\\Unpack\\kiwix\\library\\library.xml";
	String kiwixserverPath = "D:\\Apps\\Unpack\\kiwix\\xulrunner\\kiwix-serve.exe";
	String serverUrl = "127.0.0.1";
	String serverPort = "8000";
	black b;
	QWebView web;
	boolean first = true;
	private String key;
	ArrayList<String> arts;
	private boolean kiwixNotExists;
	private keyWords keylist;
	private boolean linkClicked;
	private QNetworkAccessManager nam;
	/**
	 * 标记载入状态
	 * -1没有载入文档
	 * 0载入开始
	 * 
	 */
	int loadProgress = -1;
	//"生物","微生物","病毒","细菌","基因"
	public kiwix(black b) {
		this.b = b;
	}
	ArrayList<String> getKeywordsList() {
		String[] keywords = null;
		String text = b.getStringValueFromSettings(appInfo.wikipeidaKeywordsList, "");
		DesUtils des = new DesUtils("black");
		try {
			String list = des.decrypt(text);
			String listtext = "";
			if(list != null)listtext = list;
			return cheakDocument.checkCommandArgs(listtext, '\n');
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	void setAddres() {
		System.out.println("hello");
	}

	public void startWeb() {
		
		this.web = new QWebView() {
//			public void load(QUrl url) {
//				
//			}
		};
		this.web.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
		this.web.setWindowIcon(this.b.windowIcon());
		this.web.setWindowTitle("维基百科离线版");
		QPalette p = web.palette();
		p.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Highlight, new QColor(Qt.GlobalColor.black));
		p.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.HighlightedText,
				new QColor(Qt.GlobalColor.white));
		web.setPalette(p);
		
		this.web.urlChanged.connect(this, "urlChanged(QUrl)");
		web.linkClicked.connect(this,"linkClicked(QUrl)");
		web.page().setLinkDelegationPolicy(LinkDelegationPolicy.DelegateAllLinks);

		this.web.installEventFilter(this);
		float zoom = (float)b.getEditorTextZoomValue()/10F+1F;
		web.setZoomFactor(zoom);
//		web.setWindowState(Qt.WindowState.WindowMaximized);
		web.loadProgress.connect(this,"loadProgress(int)");
		web.loadFinished.connect(this,"loadFinished()");
		web.loadStarted.connect(this,"loadStarted()");
		
		QAction ad = new QAction(web);
		ad.setShortcut("ctrl+d");
		ad.triggered.connect(this,"ctrlD()");
		web.addAction(ad);
		QAction as = new QAction(web);
		as.setShortcut("ctrl+s");
		as.triggered.connect(this,"ctrlS()");
		web.addAction(as);
		QAction af = new QAction(web);
		af.setShortcut("ctrl+f");
		af.triggered.connect(this,"ctrlF()");
		web.addAction(af);
//		nam = new QNetworkAccessManager(web);
//		web.page().setNetworkAccessManager(nam);
//		nam.setNetworkAccessible(NetworkAccessibility.NotAccessible);
//		nam.finished.connect(this, "nac(QNetworkReply)");
//		nam.authenticationRequired.connect(this,"nac()");
//		nam.proxyAuthenticationRequired.connect(this,"nac()");
//		nam.networkSessionConnected.connect(this, "nac()");
//		nam.networkAccessibleChanged.connect(this,"nac()");

//		QNetworkConfiguration net = new QNetworkConfiguration();
//		nam.blockSignals(true);
//		QNetworkProxy proxy = new QNetworkProxy();
//		nam.setProxy(proxy);
//		nam.setConfiguration(net);
//		nam.activeConfiguration();

//		this.web.show();
//		QDesktopWidget desk = new QDesktopWidget();
//		int w = desk.width()-100;
//		int h = desk.height()-100;
//		int x = this.b.x() + (this.b.width() - w) / 2;
//		int y = this.b.y() + (this.b.height() - h) / 2;
//		this.web.setGeometry(x, y, w, h);
//		web.page().setContentEditable(true);
		arts = getKeywordsList();
		
		QApplication.invokeLater(new Runnable() {
			public void run() {
				if(kiwixNotExists)
				web.load(new QUrl("https://baike.baidu.com"));
				else {
					String text = b.text.textCursor().selectedText();
					if(text.isEmpty()) {
						linkClicked = true;
						kiwix.this.web.load(new QUrl(kiwix.this.getLastUrl()));
//						kiwix.this.web.load(new QUrl(getHomeUrl()));
					}
					else web.load(new QUrl(getUrl(text)));
				}
				
			}
		});
		
		web.showMaximized();
	}
	void nac() {
		System.out.println("hello");
	}
	void nac(QNetworkReply n) {
		
	}
	void linkClicked(QUrl url) {
//		linkClicked = true;
		web.load(url);
	}
	void ctrlF() {
		InputDialog input = new InputDialog(web, "", "", false, false, "") {

			@Override
			void whenOkButtonPressed() {
				// TODO Auto-generated method stub
//				if(web.page().hasSelection()) {
//					web.findText("");
//				}
				if(value().isEmpty() )return;
				
				
				if(!web.findText(value(), QWebPage.FindFlag.FindBackward)) {
					b.getMessageBox(web, "", "no");
				}
			}

			@Override
			void whenCannelButtonPressed() {
				// TODO Auto-generated method stub
//				if(web.page().hasSelection()) {
//					web.findText("");
//				}
				if(value().isEmpty() )return;
				if(value().indexOf("@") != -1) {
					String[] sub = cheakDocument.subString(value(), "@");
					if(sub[1].equals("c")) {
						if(sub[0] != null && !sub[0].isEmpty()) {
							boolean ishas = false;
							int count = 0;
							do {
								ishas = web.findText(sub[0]);
								if(ishas)
								count++;
							}while(ishas);
							b.getMessageBox(web,"", count+"个结果");
						}
					}else if(sub[1].equals("s")) {
						if(sub[0] != null && !sub[0].isEmpty())
						web.findText(sub[0],QWebPage.FindFlag.HighlightAllOccurrences);
						
					}
					return;
				}
				
				if(!web.findText(value())) {
					b.getMessageBox(web, "", "no");
				}
			}

			void whenClose() {
				// TODO Auto-generated method stub
				web.reload();
				
			}};
		input.setCannelButtonName("向下");
		input.setOkButtonName("向上");
		input.noHide = true;
		input.show();
	}
	void ctrlS() {
		if(keylist != null && keylist.isVisible())return;
		String text = b.getStringValueFromSettings(appInfo.wikipeidaKeywordsList, "");
		DesUtils des = new DesUtils("black");
		String list = null;
			try {
				list = des.decrypt(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(list != null && !list.isEmpty()) {
			keylist = new keyWords(web,false) {
				
				@Override
				public void whenWindowSizeChanged(QSize paramQSize) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void whenSubmit(QTreeWidgetItem paramQTreeWidgetItem) {
					// TODO Auto-generated method stub
					String url = getUrl(paramQTreeWidgetItem.text(0));
					web.load(new QUrl(url));
				}
				
				@Override
				public void whenNumberTwoPressed(QTreeWidgetItem paramQTreeWidgetItem) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void whenNumberThreePressed(QTreeWidgetItem paramQTreeWidgetItem) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void whenHide() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void whenDelPressed(QTreeWidgetItem paramQTreeWidgetItem) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void selectionChanged(QTreeWidgetItem paramQTreeWidgetItem) {
					// TODO Auto-generated method stub
					
				}
			};
			
			String k = getKey(web.url().toString());
			List<String> allLine = cheakDocument.getAllLine(list);
			for(String s:allLine) {
				if(!s.isEmpty()) {
					QTreeWidgetItem ti = b.getTreeItem(keylist.tree);
					ti.setText(0, s);
					
					if(k != null && k.equals(s)) {
						keylist.tree.setCurrentItem(ti);
					}
				}
			}
			keylist.setWindowTitle("已经借阅的条目");
			keylist.setMinimumHeight(web.height()/2);
			keylist.setGeometry(web.width()-keylist.width(), web.height()-keylist.height(), keylist.width()/2, keylist.height());
			WindowFlags f = keylist.windowFlags();
			f.clear(Qt.WindowType.WindowMaximizeButtonHint);
			keylist.setWindowFlags(f);
			keylist.show();
			keylist.setGeometry(web.width()-keylist.width(), web.height()-keylist.height(), keylist.width(), keylist.height());
			
		}
	}
	void ctrlD(){
		if(!b.isAdmin())
			if(b.hourOfDay < 22 && b.hourOfDay != 12) {
				b.getMessageBox(web, "借阅条目", "此时间段不允许借阅条目\n\n只有管理员或在中午12点到1点间允许借阅条目").show();;
				return;
			}
		String k = getKey(web.url().toString());
		if(k == null)return;
		
		String text = b.getStringValueFromSettings(appInfo.wikipeidaKeywordsList, "");
		DesUtils des = new DesUtils("black");
		String list = null;
			try {
				list = des.decrypt(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(list != null && !list.isEmpty()) {
			if(list.indexOf(k) != -1) {
				b.getMessageBox(web, "借阅条目", "此条目已经借阅，无需重复借阅").show();;
				return;
			}
			list = list+"\n"+k;
			int[] stat = cheakDocument.wordStat(list);
			if(stat[2] > 20) {
				b.getMessageBox(web,"借阅条目", "词条数量不能大于20个").show();;
				return;
			}
		}else list = k;
		try {
			String encrypt = des.encrypt(list);
			b.settings.setValue(appInfo.wikipeidaKeywordsList, encrypt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(keylist != null && keylist.isVisible()) {
			QTreeWidgetItem ti = b.getTreeItem(keylist.tree);
			ti.setText(0, k);
			keylist.tree.setCurrentItem(ti);
		}
		b.getMessageBox(web, "借阅条目", "条目(<b>"+k+"</b>)借阅成功").show();;
	}
	void loadStarted() {
		loadProgress = 0;
//		if(first)
//		web.setUpdatesEnabled(false);
	}
	void loadProgress(int value) {
		if(value < 100) {
			web.setWindowTitle("("+value+"%) "+key);
		}
		else {
			web.setWindowTitle(key);
		}
	}
	void loadFinished() {
		if(!kiwixNotExists) {
			web.setUpdatesEnabled(false);
			loadProgress = -1;
			if(first) {
				web.page().currentFrame().setScrollBarValue(Orientation.Vertical,b.getIntValueFromSettings(appInfo.kiwixScrollBarValue, "0"));
				first = false;
			}
//			System.out.println("starting conversion "+getKey(web.url().toString()));
//			String html = web.page().currentFrame().toHtml();
//			StringBuffer sb = new StringBuffer();
//			for(int i=0;i<html.length();i++) {
//				char at = html.charAt(i);
//				if(cheakDocument.isAsia(at) && !cheakDocument.cheak(at)) {
//					String[] pys = PinyinHelper.toHanyuPinyinStringArray(at);
//					if(pys != null)
//					sb.append(pys[0]+" ");
//				}else sb.append(at);
//			}
//			web.setHtml(sb.toString(), web.url());
			web.setUpdatesEnabled(true);
			web.repaint();
		}
	
	}
	public void closeKiwix() {
		if(web != null) {
			web.close();
		}
	}
	public boolean eventFilter(QObject o, QEvent e) {
//		System.out.println(e.type());
		if (e.type() == QEvent.Type.Close) {
			if(!kiwixNotExists) {
				setLastUrl(web.url().toString());
				b.settings.setValue(appInfo.kiwixScrollBarValue, ""+web.page().currentFrame().scrollBarValue(Orientation.Vertical));
				this.kiwixserver.destroy();
				try {
					Runtime.getRuntime().exec("taskkill /IM kiwix-serve.exe /F");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			dispose();
			b.k = null;
		}else if(e.type() == QEvent.Type.KeyPress) {
			QKeyEvent k = (QKeyEvent) e;
			if(k.modifiers().isSet(Qt.KeyboardModifier.AltModifier)) {
				if(k.matches(StandardKey.Forward)) {
//					web.pageAction(WebAction.Forward);
					if(web.history().canGoForward()) {
						if(loadProgress != -1)return true;
						linkClicked = true;
						web.forward();
//						System.out.println("forward");
						return true;
					}
				}else if(k.matches(StandardKey.Back)) {
//					web.pageAction(WebAction.Back);
					if(web.history().canGoBack()) {
						if(loadProgress != -1)return true;
						linkClicked = true;
						web.back();
//						System.out.println("back");
						return true;
					}
				}
			}
			
//			System.out.println(k.key());

//			if(k.key() == Qt.Key.Key_Control.value()) {
//				
//			}
		}
		return false;
	}
	QUrl getRandomURLFromArts() {
		if(arts.size() == 0) return null;
		String k = getKey(web.url().toString());
		if(k != null) {
			int next = 0;
			if(arts.size() > 1) {
				boolean isHas = false;
				for(String s:arts) {
					if(k.equals(s)) {
						isHas = true;
						break;
					}
				}
				Random r = new Random();
				do {
					next = r.nextInt(arts.size()-1);
				}while(arts.get(next).equals(k));
			}
			
			
			String ar = arts.get(next);
			QUrl url = new QUrl(getUrl(ar));
//			System.out.println("random: "+ar+" url: "+url);
			return url;
		}else {
			Random r = new Random();
			int next = r.nextInt(arts.size()-1);
			String ar = arts.get(next);
			QUrl url = new QUrl(getUrl(ar));
//			System.out.println("random: "+ar+" url: "+url);
			return url;
		}
	}
	void urlChanged(QUrl url) {
		if(kiwixNotExists) return;
		
//		if(!linkClicked && !b.isAdmin())
//		if(b.hourOfDay < 22 && b.hourOfDay != 12) {
//			boolean isHas = false;
//			String key = getKey(url.toString());
//			if(key != null) {
//				for(String ar:arts) {
//					if(key.equals(ar)) {
//						isHas = true;
//						break;
//					}
//				}
//				if(!isHas) {
//					web.history().back();
//					QUrl url_ = getRandomURLFromArts();
//					if(url_ != null) {
//						url = url_;
//						web.load(url);
//					}
//					
//				}
//			}
//			
//		}
		key = getKey(url.toString());
		if(linkClicked) {
			linkClicked = false;
		}
	}
	String getKey(String url) {
		if(kiwixNotExists) return "";
		
		if(url  == null || url.isEmpty() || url.indexOf(".html") == -1) return null;
		int a = url.lastIndexOf(".html");
		int b = url.lastIndexOf("/");
		return url.substring(b+1, a);
	}
	String getUrl(String key) {
		if(kiwixNotExists) return "";
		return getHomeUrl()+"/wikipedia_zh_all_novid_2018-06/A/"+key+".html";
	}
	public String getLastUrl() {
		if(kiwixNotExists) return "";

		String url = (String) this.b.settings.value(appInfo.kiwixLastURL, getHomeUrl());
//		System.out.println("lastUrl: "+url);
		
		if(url.equals("about:blank"))return getHomeUrl();
		return url;
	}

	public void setLastUrl(String url) {
//		System.out.println("lasturl: "+url);
		this.b.settings.setValue(appInfo.kiwixLastURL, url);
	}

	public void startKiwixServer() {
		File file = new File(kiwixserverLibrary);
		if(!file.exists()) {
			kiwixNotExists = true;
			return;
		}
		String cmd = this.kiwixserverPath + " --library --port=" +this.serverPort + " " + this.kiwixserverLibrary;
		try {
//			Runtime.getRuntime().exec("taskkill /IM kiwix-serve.exe /F");
//			System.out.println(cmd);
			this.kiwixserver = Runtime.getRuntime().exec(cmd);
		} catch (IOException localIOException) {
		}
	}

	public void setCommand() {
		String path = (String) this.b.settings.value("KiwixServerPath");
		if (path != null) {
			this.kiwixserverPath = path;
		}
		String library = (String) this.b.settings.value("Library");
		if (library != null) {
			this.kiwixserverLibrary = library;
		}
	}

	public void setkiwixserverPath(String path) {
		this.kiwixserverPath = path;
		this.b.settings.setValue("KiwixServerPath", path);
	}

	public void setkiwixserverLibrary(String labrary) {
		this.kiwixserverLibrary = labrary;
		this.b.settings.setValue("Library", labrary);
	}

	public String getHomeUrl() {
		return "http://" + this.serverUrl + ":" + this.serverPort;
	}
}
