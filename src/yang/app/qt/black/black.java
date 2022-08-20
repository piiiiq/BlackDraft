package yang.app.qt.black;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;

import io.qt.QUnsuccessfulInvocationException;
import io.qt.core.QByteArray;
import io.qt.core.QEvent;
import io.qt.core.QLocale;
import io.qt.core.QLocale.Language;
import io.qt.core.QModelIndex;
import io.qt.core.QObject;
import io.qt.core.QPoint;
import io.qt.core.QPointF;
import io.qt.core.QPropertyAnimation;
import io.qt.core.QRect;
import io.qt.core.QRectF;
import io.qt.core.QSettings;
import io.qt.core.QSize;
import io.qt.core.QSysInfo;
import io.qt.core.QTimer;
import io.qt.core.Qt;
import io.qt.core.Qt.AlignmentFlag;
import io.qt.core.Qt.BrushStyle;
import io.qt.core.Qt.ConnectionType;
import io.qt.core.Qt.DockWidgetArea;
import io.qt.core.Qt.PenStyle;
import io.qt.core.Qt.ScrollBarPolicy;
import io.qt.core.Qt.TimerType;
import io.qt.core.Qt.WidgetAttribute;
import io.qt.core.Qt.WindowFlags;
import io.qt.core.Qt.WindowType;
import io.qt.gui.QBrush;
import io.qt.gui.QClipboard;
import io.qt.gui.QCloseEvent;
import io.qt.gui.QColor;
import io.qt.gui.QCursor;
import io.qt.gui.QFont;
import io.qt.gui.QFont.SpacingType;
import io.qt.gui.QFontMetrics;
import io.qt.gui.QGradient;
import io.qt.gui.QHoverEvent;
import io.qt.gui.QIcon;
import io.qt.gui.QImage;
import io.qt.gui.QInputMethodEvent;
import io.qt.gui.QKeyEvent;
import io.qt.gui.QKeySequence;
import io.qt.gui.QLinearGradient;
import io.qt.gui.QMouseEvent;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPainter.RenderHint;
import io.qt.gui.QPainterPath;
import io.qt.gui.QPalette;
import io.qt.gui.QPalette.ColorRole;
import io.qt.gui.QPen;
import io.qt.gui.QPixmap;
import io.qt.gui.QPolygon;
import io.qt.gui.QRegion;
import io.qt.gui.QTextBlock;
import io.qt.gui.QTextBlockFormat;
import io.qt.gui.QTextCharFormat;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveMode;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.gui.QTextCursor.SelectionType;
import io.qt.gui.QTextDocument;
import io.qt.gui.QTextLayout;
import io.qt.gui.QTextLine;
import io.qt.widgets.QAbstractItemView;
import io.qt.widgets.QAction;
import io.qt.widgets.QApplication;
import io.qt.widgets.QComboBox;
import io.qt.widgets.QDesktopWidget;
import io.qt.widgets.QDialogButtonBox;
import io.qt.widgets.QDockWidget;
import io.qt.widgets.QDockWidget.DockWidgetFeature;
import io.qt.widgets.QFileDialog;
import io.qt.widgets.QFrame;
import io.qt.widgets.QGraphicsDropShadowEffect;
import io.qt.widgets.QGraphicsOpacityEffect;
import io.qt.widgets.QLabel;
import io.qt.widgets.QLineEdit;
import io.qt.widgets.QMenu;
import io.qt.widgets.QMenuBar;
import io.qt.widgets.QMessageBox;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QTextEdit.ExtraSelection;
import io.qt.widgets.QToolButton;
import io.qt.widgets.QTreeWidget;
import io.qt.widgets.QTreeWidgetItem;
import io.qt.widgets.QVBoxLayout;
import io.qt.widgets.QWidget;
import net.sourceforge.pinyin4j.PinyinHelper;
import yang.demo.allPurpose.DesUtils;
import yang.demo.allPurpose.MD5;
import yang.demo.allPurpose.MSTTSSpeech;
import yang.demo.allPurpose.cfg_read_write;
import yang.demo.allPurpose.debug;
import yang.demo.allPurpose.fileTool;
import yang.demo.allPurpose.gitTool;
import yang.demo.allPurpose.isYourNeedFile;
import yang.demo.allPurpose.time;
import yang.demo.mail.SendMailBySSL;
/**
 * 窗口透明度信息改在QTmud类内设置
 * @author test
 *
 */
public class black extends QTmud {
	Ui_black ui = new Ui_black();
	black_tree tree;
	blacktext btext;
//	QFont menuFont = new QFont("微软雅黑", 15);
	File currentEditing;
	public static QSettings settings;
	boolean saved = true;
	public ArrayList<fileInfo> filesList;
	File filesListFile;
	File projectFile;
	Properties projectInfo;
	QIcon ico_folder = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/dir.png"));
	QIcon ico_file = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/filenochar.png"));
	QIcon ico_recycle = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/recyclenofile.png"));
	QIcon ico_recycleHasFile = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/recyclehasfiles.png"));
	QIcon ico_prevFile = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/prevfile.png"));
	QIcon ico_nextFile = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/nextfile.png"));
	QIcon ico_files = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/files.png"));
	QIcon ico_draft = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/dfart.png"));
	QIcon ico_reserach = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/reserach.png"));
	QIcon ico_fileOfHasText = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/filehaschar.png"));
	QIcon ico_notes = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/notepad_icon&48.png"));
	QIcon ico_warning = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/attention_icon&48.png"));
	QIcon ico_addItem = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/32/additem.png"));
	QIcon ico_titleface = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/flat.png"));
	QIcon ico_find = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/find.png"));
	QIcon ico_names = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/names.png"));
	QIcon ico_timer = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/stop_watch_icon&48.png"));
	QIcon ico_app_24 = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/ico1_24.png"));
	QIcon ico_app_48 = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/ico1_48.png"));
	QIcon ico_app_200 = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/ico1_200.png"));

	QIcon ico_keywordsList = new QIcon(new QPixmap("classpath:yang/app/qt/black/iconsScr/16/keywords.png"));
	QIcon ico_keywordFirstTop = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/keywordFirstTop.png_"));
	QIcon ico_keywordTop = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/keywordTop.png_"));
	QIcon ico_keywordDoc = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/keywordDoc.png_"));
	QIcon ico_keywordLine = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/keywordLine.png_"));
	QIcon ico_keywordNo = new QIcon(new QPixmap("classpath:yang/app/qt/black/myIcons/keywordNo.png_"));
	QIcon ico_mark = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/pin_map_icon&48.png"));
	QIcon ico_mark_white = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/pin_map_icon&48_white.png"));
	QIcon ico_mark_left = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/br_prev_icon&48.png"));
	QIcon ico_mark_right = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/br_next_icon&48.png"));
	QIcon ico_mark_f = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/pin_map_icon&48_f.png"));
	QIcon ico_mark_white_f = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/pin_map_icon&48_white_f.png"));
	QIcon ico_back = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/fullscreen-exit.png"));
	QIcon ico_close = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/close.png"));
	QIcon ico_save = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/content-save-outline.png"));
	QIcon ico_exitWritingView = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/arrow-collapse.png"));
	QIcon ico_minimize = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/minus.png"));
	QIcon ico_maximize = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/window-maximize.png"));
	QIcon ico_redo = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/redo-variant.png"));
	QIcon ico_undo = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/undo-variant.png"));
	QIcon ico_wikipedia = new QIcon(new QPixmap("classpath:yang/app/qt/black/icons/wikipedia.png"));
	QImage img_topBackground = new QImage("classpath:yang/app/qt/black/icons/topbg.png");
	QTreeWidgetItem draft;
	QTreeWidgetItem recycle;
	QTreeWidgetItem reserach;
	boolean isDoubleClickedOnTreeItem;
	public QMenu treeMenu;
	boolean fileListChanged;
	boolean mustSaveFileListFile;
	public fileInfo infoOfCurrentEditing;
	public QLabel countmessage;
	public QTreeWidgetItem TreeWidgetItemOfCurrentEditing;
	public QTreeWidgetItem treeWidgetItemOfKeyWordsFile;
	List<TextRegion> markTextData;
	public String marktext;
	boolean marktextIsChanged;
	boolean markstatIsChanged;
	ArrayList<markstat> markstat = new ArrayList();
	public keyWords keywords;
	public static bTextEdit text;
	static int findview = 0;
	boolean doNotAddToMoveList;
	boolean doNotHideKeywordsDialog;
	public nameCreator namecreator;
	public QColor windowColor;
	static int writingView = 0;
	QByteArray saveGeometry;
	boolean progressBugStop;
	String progressMessage;
	int progressValue;
	static StringBuilder logsmessage = new StringBuilder();
	public static QColor borderColor = new QColor(137, 142, 150);
	treeTitle treetitle = new treeTitle(this.dock);

	textTitle textTitle;
	toolbar toolbar;
	private QMenuBar menubar;
	QDockWidget dock;
	public QComboBox zoomBox;
	ArrayList<String> moveList = new ArrayList<String>();
	int moveIndex = -1;
	ArrayList<dataForOpenedFile> openedFiles = new ArrayList();
	Qt.WindowFlags windowFlags;
	String updateinfo;
	int hasUpdate;
	long allActiveTime;
	long lastActiveTime;
	long startTime;
	long lastTime;
	String writingDate;
	boolean doNotSet_UnSave;
	private bRunnable brunnableForOSD;
	String timeMode;
	int charCountWithoutEnter;
	private ArrayList<String> superfluousFiles;
	private bRunnable timerForAutoCheckNetworks;
	private String networkList;
	boolean scrollBarHideing;
	protected boolean scrollBarOfTreeHideing;
	QLabel timeMessage;
	QLabel loadFileMessage;
	boolean isLoadFile;
	private long loadFileTime_start;
	private long loadFileTime_end;
	action afterLoadFile = null;
	int plusCharCount;
	private String[] previousOpenedFile = new String[2];
	private String[] nextOpenedFile = new String[2];
	private String showNameOfCurrentEditingFile;
	QLineEdit findLine;
	private keyWords keywordsForFindLine;
	private QToolButton findbutton;
	ArrayList<String> findHistory;
	private QComboBox findBox;
	finddialog finddialog;
	protected String readText;
	QGraphicsOpacityEffect effectForTextTitle = new QGraphicsOpacityEffect();
	ArrayList<blackCommand> commands = new ArrayList();
	ArrayList<timerInfo> timerInfos = new ArrayList();
	int timerId;
	boolean doNotShowKeepWrtingMessage = true;
	private boolean doNotShowShadow;
	protected boolean beeping;
	boolean windowOpacity;
	private boolean doNotHideLoadFileMessage;
	private boolean execCommandAndQuiet;
	private int lastRepairPos;
	static Clip clip = null;
	String[] args;
	static boolean isFoceShow;
	static boolean clipIsStart;
	boolean saveMode;
	boolean yellowForceColor;
	boolean noAnimation;
	int alpha_windowBackground;
	protected boolean addToMoveListEnd;
	boolean LineAndDocFirst_keywords;
	int alpha_text = 204;
	public long lastInputTime_long;
	private boolean textChanged;
	private File lastEditFile;
	static boolean quietMode;
	QLabel caretPos;
	long appStartTime, appRunTime;
	QFrame textWidget;
	QVBoxLayout textLayout;
	QLabel IME;
	boolean noEditorEffect;
	private QGraphicsDropShadowEffect drop;
	private pinyinTool pinyinTool;
	public static int getNetworkTime = 0;
	Process goldendict;
	String backupDir;
	int backupTime;
	private Long lastBackUpTime;
	private boolean backupFromSynTime;
	int withoutBuleValue = 60;
	String userName;
	private long hideTime;
	static MSTTSSpeech speech;
	static boolean isSpeak;
	private boolean isFoceCloneFromLocal;
	static Thread thread_voice;
	int hourOfDay;
	int checkMailTimerID = -1;
	static String adminPasswd;
	ArrayList<TextRegion> mailData;
	boolean mailDataIsChanged;
	kiwix k;
	long activeTime;
	boolean monitorOff;
	boolean dontNotSetMonitor = true;
	static boolean blackUD;
	static imgWidget img;
	QImage bg;
	private String lastCheckMailDate;
	private static bmessageBox logsBox;
	boolean inReserach;
	private boolean noCloseTitlePanel;
	private int Only;
	private String sum_getMail;
	/**
	 * debugMode
	 */
	static boolean dbm;
	static DesUtils des = new DesUtils("black");
	static DesUtils des_doc = new DesUtils("blackfile");
	static int minute;
	static int second;
	bAction ba = new bAction(this);
	boolean showFind;
	private int pageCurrent;
	private int allPage;
	boolean noPageLine;
	boolean noMarkChar;
	boolean bgImgChanged;
	protected long lastSynTitlesData;
	private boolean needResetStyleOfDoc;
	boolean donotAddWalkInfo;
	private boolean findKeywordsByPinyin;
	statusbar statusbar;
	private QGraphicsDropShadowEffect se;
	protected bRunnable findInMark_brun;
	private static boolean hasUIRunnable;
	static black b;

	public static void main(String[] args) {
		bAction.setUpdate();
		QApplication.initialize(args);
		black b = new black(args);
		b.setLocale(new QLocale(Language.Chinese));
		QApplication.exec();
	}

	public black() {
		this.ui.setupUi(this);
		setGeometry(400, 400, 400, 400);
		show();
	}

	public black(String[] args) {
		b = this;
		setAutoFillBackground(false);

//		setWindowFlags(Qt.WindowType.Tool,Qt.WindowType.WindowSystemMenuHint,Qt.WindowType.WindowTitleHint);
		if (bAction.isDisable() || bAction.checkDateDisable()) {
			QMessageBox mess = getMessageBoxNotShow("软件过期",
					"此版本的" + appInfo.appName + "已经过期！<br>请发送邮件至下面的地址提醒开发者提交新版本：<br><i>beihuiguixian@hotmail.com</i>");
//			mess.setIconPixmap(ico_app_48.pixmap(100));
			mess.setWindowIcon(ico_app_24);
			mess.exec();
			System.exit(0);
		}
		appStartTime = System.currentTimeMillis();

		this.settings = super.getSettings();

		sum_getMail = (String) settings.value(appInfo.MD5OFGetMail, "");
		String path = System.getProperty("java.class.path");
		if (path.indexOf(appInfo.blackUD) != -1) {
			blackUD = true;
			p("<b>" + appInfo.appName + "位于" + appInfo.blackUD + "文件夹内，已启用备用模式</b>");
		}
		// 缓存管理员密码
		Object value = settings.value(appInfo.additionalInfo);
		if (value != null) {
			DesUtils des = new DesUtils("black"); // 自定义密钥
			try {
				adminPasswd = des.decrypt(value.toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else
			p("<b>" + appInfo.appName + "没有存储管理员密码</b>");

		this.args = args;
		userName = System.getProperty("user.name");
//		userName = "test";
		int j = args.length;
		for (int i = 0; i < j; i++) {
			String arg = args[i];
			if (arg.equals("-show")) {
				System.out.println("\n强制显示窗口");
				isFoceShow = true;
			} else if (arg.equals("-help")) {
				System.out.println(appInfo.argsHelpInfo);
				System.exit(0);
			} else if (arg.equals("-?")) {
				System.out.println(appInfo.argsHelpInfo);
				System.exit(0);
			} else if (arg.equals("-h")) {
				System.out.println(appInfo.argsHelpInfo);
				System.exit(0);
			} else if (arg.equals("-clone")) {
				System.out.println("\n从本地仓库克隆");
				isFoceCloneFromLocal = true;
			} else if (arg.equals("-loaderVer")) {
				appInfo.loaderVer = arg;
			} else if (arg.equals("-debug")) {
				System.out.println("\n已启用调试模式");
				dbm = true;
			} else if (arg.equals("-noMarkChar")) {
				System.out.println("\n已禁用绘制段落标记");
				noMarkChar = true;
			} else if (arg.equals("-open")) {
				System.out.println("\n打开项目");
				if (args[i + 1] != null && new File(args[i + 1]).exists())
					bAction.tempData.put("OpenProjectNow", args[i + 1]);
				else
					System.out.println("要打开的项目路径似乎有错，程序将继续使用上一次编辑的项目");
			} else {
				System.out.println("\n无效的参数:" + arg);
//				System.exit(0);
			}
		}
		if (!dbm && !new File("./" + appInfo.appName + ".exe").exists()) {
			System.out.println("主程序" + appInfo.appName + ".exe命名错误，程序退出");
			System.exit(0);
		}

		setWindowIcon(this.ico_app_48);
		setWindowTitleB(appInfo.appName);
		int year = Calendar.getInstance().get(Calendar.YEAR);

		if (isAdmin() || bAction.getWindowsVersion() < 6 || year > 2019) {
			appInfo.miniMode = "0";
		} else {
			File runpath = new File("");
			System.out.println("启动目录为: " + runpath.getAbsolutePath());
			File defaultPath = new File("D:\\Apps\\Unpack\\MyApp\\BlackForWindows");
//			if(!runpath.getAbsolutePath().equals(defaultPath.getAbsolutePath())) {
////				if(!exeIsRuningWithTitle("Eclipse IDE")) {
//					getMessageBox("", "此black实例不是储存在合法路径内，因此不允许启动！");
//					return;
////				}else appInfo.miniMode = "0";
//			}

		}
		bAction.disableWifi(this);
//		if(!dbm) {
//			InputDialog id = new InputDialog(this,"需要凭据","凭据：",true,false,"") {
//				
//				@Override
//				void whenOkButtonPressed() {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				void whenClose() {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				void whenCannelButtonPressed() {
//					// TODO Auto-generated method stub
//					
//				}
//			};
//			
//			int exec = id.exec();
//			if(exec == 1) {
//				if(id.value().equals("1234")) {
//				}else if(id.value().equals(adminPasswd)) {
//					appInfo.miniMode = "0";
//				}else {
//					System.exit(0);
//				}
//			}else System.exit(0);
//		}

		this.ui.setupUi(this);
		this.windowColor = palette().color(QPalette.ColorRole.Window);
		QPalette p = palette();
		p.setColor(QPalette.ColorRole.Window, new QColor(Qt.GlobalColor.white));
		setPalette(p);

		setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
		this.customContextMenuRequested.connect(this, "addMenu(QPoint)");
		this.menubar = this.ui.menubar;

		this.ui.addFile.triggered.connect(this, "addFile()");
		this.ui.addDir.triggered.connect(this, "addFolder()");
		this.ui.writingview.triggered.connect(this, "writingView(Boolean)");

		this.ui.newProject.triggered.connect(this, "createProject()");
		this.ui.openProject.triggered.connect(this, "findProject()");
		this.ui.moveToRecycle.triggered.connect(this, "moveFileToRecyle()");
		this.ui.clearRecycle.triggered.connect(this, "clearRecyle()");

		this.ui.addKeywords.setIcon(this.ico_keywordsList);
		this.ui.addKeywords.triggered.connect(this, "addKeyWordsListToProject()");
		this.ui.oneChineseName.triggered.connect(this, "getChineseName()");
		this.ui.engManName.triggered.connect(this, "getEnglishName_male()");
		this.ui.engWomanName.triggered.connect(this, "getEnglishName_female()");
		this.ui.itManName.triggered.connect(this, "getItalinaName_male()");
		this.ui.itWomanName.triggered.connect(this, "getItalinaName_female()");
		this.ui.find.triggered.connect(this, "find()");
		this.ui.settings.triggered.connect(this, "showSettingsDialog()");
		this.ui.charCount.triggered.connect(this, "showCharCountBox()");
		this.ui.setGit.triggered.connect(this, "setGit_ui()");
		this.ui.commit.triggered.connect(this, "commit_ui()");
		this.ui.commit.setShortcut("ctrl+shift+c");
		this.ui.push.triggered.connect(this, "push_ui()");
		this.ui.commitAndPush.triggered.connect(this, "gitWork_ui()");
		this.ui.chineseNames.triggered.connect(this, "getChineseNames()");
		this.ui.about.triggered.connect(this, "showAbout()");
		this.ui.howPay.triggered.connect(this, "showPay()");
		this.ui.info.triggered.connect(ba, "showInfoBox()");

		this.ui.resetProject.setEnabled(false);

		this.ui.about.setShortcut(new QKeySequence(Qt.KeyboardModifier.ControlModifier.value()
				+ Qt.KeyboardModifier.ShiftModifier.value() + Qt.Key.Key_A.value()));
		this.ui.saveProjectAllChanged.triggered.connect(this, "saveProjectChanged()");
		this.ui.openProjectDir.triggered.connect(this, "openProjectDir()");
		this.ui.clearTempFiles.triggered.connect(this, "clearTempFiles()");
		this.ui.clearLogs.triggered.connect(this, "clearLogs()");
		this.ui.hideFilePanel.triggered.connect(this, " hideFilesTree()");
		this.ui.checkUpdate.triggered.connect(this, "checkUpdate()");

		this.ui.dockTree.layout().setContentsMargins(0, 0, 0, 0);
		this.ui.verticalLayout.setMargin(0);

		this.ui.verticalLayout.setSpacing(0);
		this.textTitle = new textTitle(this);
		this.textTitle.setEnabled(false);

		this.ui.verticalLayout.addWidget(this.textTitle);

		this.toolbar = new toolbar(this);
		this.toolbar.setMovable(false);
		this.toolbar.setIconSize(new QSize(32, 32));
		this.toolbar.setWindowTitle("工具栏");
		QToolButton add = new QToolButton(this.toolbar);
		add.setIcon(this.ico_addItem);
		add.setText("添加");
		add.pressed.connect(this, "addFile()");
		add.setPopupMode(QToolButton.ToolButtonPopupMode.MenuButtonPopup);
		QMenu addmenu = new QMenu(add);
		addmenu.addAction(this.ui.addFile);
		addmenu.addAction(this.ui.addKeywords);
		addmenu.addAction(this.ui.addDir);
		add.setMenu(addmenu);
		this.toolbar.addWidget(add);
		this.toolbar.addAction(this.ui.writingview);
		this.findBox = new QComboBox(this.toolbar);

		this.findBox.setContextMenuPolicy(Qt.ContextMenuPolicy.NoContextMenu);
		this.findLine = new QLineEdit(this.findBox);

//		this.findBox.currentIndexChanged.connect(this, "currentIndexChangedForFindBox()");
		QApplication.connect(findBox, "currentIndexChanged(int)", this, "currentIndexChangedForFindBox()");

		this.findBox.setLineEdit(this.findLine);

		this.findLine.setAlignment(new Qt.AlignmentFlag[] { Qt.AlignmentFlag.AlignCenter });
		this.findLine.setPlaceholderText("检索项目");
		this.findbutton = new QToolButton(this.findBox);
		this.findbutton.setStyleSheet("QToolButton{background: transparent;border:yellow;}");
		this.findbutton.setIcon(this.ico_find);
		this.findbutton.triggered.connect(this, "findLineReturnPressd()");
		this.findbutton.pressed.connect(this, "findLineReturnPressd()");

		this.findBox.installEventFilter(this);
		this.findLine.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
		this.findLine.customContextMenuRequested.connect(this, "addMenuForFindLine(QPoint)");

		addToolBar(this.toolbar);

		this.dock = this.ui.dockTree;
		this.dock.installEventFilter(this);
		this.dock.setWindowTitle("文件篓");
		this.dock.setTitleBarWidget(treetitle);
		this.dock.setVisible(true);
		dock.setFeatures(DockWidgetFeature.DockWidgetMovable, DockWidgetFeature.DockWidgetFloatable);
		dock.setAllowedAreas(DockWidgetArea.LeftDockWidgetArea);
		this.tree = new black_tree(this.ui.dockTree, this);
		this.ui.dockTree.setWidget(this.tree);
		this.ui.dockWidgetContents.setContentsMargins(0, 0, 0, 0);
		this.ui.dockTree.setContentsMargins(0, 0, 0, 0);
		QPalette qPalette = this.tree.palette();
		qPalette.setColor(QPalette.ColorRole.Base, new QColor(230, 234, 238));
		this.tree.setPalette(qPalette);

		this.tree.clicked.connect(this, "treeClicked(QModelIndex)");
		this.tree.setSelectionMode(QAbstractItemView.SelectionMode.ExtendedSelection);

		this.tree.setHeaderHidden(true);
		this.tree.itemPressed.connect(this, "openfile()");
		this.tree.itemChanged.connect(this, "editFinished(QTreeWidgetItem,Integer)");
//		this.tree.itemDoubleClicked.connect(ba,"test()");
		this.tree.doubleClicked.connect(this, "doubleClicked()");
		this.tree.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
		this.tree.customContextMenuRequested.connect(this, "showTreeMenu(QPoint)");
		this.tree.itemExpanded.connect(this, "expanded(QTreeWidgetItem)");
		this.tree.itemCollapsed.connect(this, "expanded(QTreeWidgetItem)");
		this.tree.installEventFilter(this);
//		tree.setEditTriggers(EditTrigger.DoubleClicked);

		statusbar = new statusbar(this);
		statusbar.setSizeGripEnabled(true);

//		statusbar.setMaximumHeight(20);
		setStatusBar(statusbar);
		QPalette palette = statusbar.palette();
		palette.setColor(QPalette.ColorRole.Window, this.windowColor);
		statusbar.setAutoFillBackground(true);
		statusbar.setBackgroundRole(QPalette.ColorRole.Window);
		statusbar.setPalette(palette);
		caretPos = new QLabel();
		statusbar.addPermanentWidget(caretPos);
		this.countmessage = new QLabel();
		statusbar.addPermanentWidget(this.countmessage);

		this.zoomBox = new QComboBox(this);
		this.zoomBox.setFrame(false);
		for (int i = 100; i <= 400; i += 10) {
			this.zoomBox.addItem("缩放" + String.valueOf(i) + "%");
		}
		int zoomindex = this.zoomBox.findText("缩放" + String.valueOf(getEditorTextZoomValue() * 10 + 100) + "%");
		this.zoomBox.setCurrentIndex(zoomindex);
//		this.zoomBox.currentIndexChanged.connect(this, "setEditorZoomValue(int)");
		QApplication.connect(zoomBox, "currentIndexChanged(int)", this, "setEditorZoomValue(int)");

		statusbar.addPermanentWidget(this.zoomBox);

		installEventFilter(this);
		p("Black版本: " + appInfo.appVersion);

		this.timeMessage = new QLabel(this);
		timeMessage.setAttribute(WidgetAttribute.WA_TransparentForMouseEvents);

		this.timeMessage.setAlignment(new Qt.AlignmentFlag[] { Qt.AlignmentFlag.AlignRight });
		this.timeMessage.linkActivated.connect(this, "link(String)");

		QFont font = new QFont("微软雅黑", 20);
		font.setBold(true);
		this.timeMessage.setFont(font);

		this.loadFileMessage = new QLabel(this);
		loadFileMessage.setAttribute(WidgetAttribute.WA_TransparentForMouseEvents);

		this.loadFileMessage.setAlignment(new Qt.AlignmentFlag[] { Qt.AlignmentFlag.AlignRight });
		font.setPointSize(40);
		this.loadFileMessage.setFont(font);
		this.loadFileMessage.hide();

		this.timeMode = ((String) settings.value(appInfo.timeMode, "0"));
		this.windowOpacity = Boolean.valueOf((String) settings.value(appInfo.windowOpacity, "false")).booleanValue();
		this.saveMode = Boolean.valueOf((String) this.settings.value(appInfo.saveMode, "true")).booleanValue();
		this.yellowForceColor = Boolean.valueOf((String) this.settings.value(appInfo.yellowForceColor, "false"))
				.booleanValue();
		this.alpha_windowBackground = getIntValueFromSettings(appInfo.alpha_windowBackground, "0");
		quietMode = getBooleanValueFromSettings(appInfo.quietMode, "false");
		this.LineAndDocFirst_keywords = getBooleanValueFromSettings(appInfo.lineAndDocFirst, "false");
		backupTime = getIntValueFromSettings(appInfo.backUpTime, "86400000");
		withoutBuleValue = getIntValueFromSettings(appInfo.withoutBlueValue, "60");
		findKeywordsByPinyin = getBooleanValueFromSettings(appInfo.usePinyinFindKeywords, "true");

		if (getIntValueFromSettings(appInfo.insByMouseOrKey, "0") == 1) {
			timerInfo ti = new timerInfo(true, "通过键盘改写标点", null);
			ti.type = 987;
			ti.showProgress = false;
			addTimer(ti);
		}

		addCommands.addCommands(this);

		textWidget = new QFrame(this);
		textWidget.setAutoFillBackground(true);

		textLayout = new QVBoxLayout();
		textWidget.setLayout(textLayout);
		ui.verticalLayout.addWidget(textWidget);

		ba.addMenu();
		new setMenus(this);
		this.toolbar.hide();
		this.dock.hide();
		statusbar.hide();
		this.textTitle.hide();

//		if(!isAdmin()) {
//			WindowFlags wf = windowFlags();
//			if(!wf.isSet(Qt.WindowType.WindowStaysOnTopHint)) {
//				wf.set(Qt.WindowType.WindowStaysOnTopHint);
//				setWindowFlags(wf);
//			}
//		}
//		showTime();
		checkUpdate();

		// 设置界面字体大小
		QFont defaultfont = new QFont(appInfo.uiFont);
		if (!getBooleanValueFromSettings(appInfo.useBigUIFont, "false"))
			defaultfont.setPointSize(9);
		else
			defaultfont.setPointSize(getIntValueFromSettings(appInfo.UIfontSize, "12"));
		QApplication.setFont(defaultfont);
		ba.applyUIFont(defaultfont);

		String proPath = (String) bAction.tempData.get("OpenProjectNow");
		if (proPath == null)
			reOpenProject();
		else {
			try {
				openProject(proPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isFoceShow) {
			show();
			showAllWidget();
		}
		if (isFoceCloneFromLocal) {
			cloneFromLocal();
		}
		findLogsInRootDir();
		showAppUpdateLog();

		File f = new File(appInfo.mailDat);
		if (f.exists()) {
			mailData = (ArrayList<TextRegion>) io.readObjFile(f);
			int count = 0;
			for (TextRegion trr : mailData) {
				if (trr.end != -99)
					count++;
			}
			if (count > 0) {
				timerInfo ti = new timerInfo(true, "<font style=\"font-size:13pt;color:blue\">有" + count + "封邮件</font>",
						null);
				ti.showProgress = false;
				ti.type = -999;
				checkMailTimerID = addTimer(ti);
			}
			for (int i = 0; i < mailData.size(); i++) {
				TextRegion tr = mailData.get(i);
				if (tr.end != -99) {
					timerInfo ti = new timerInfo(-998, 300000, tr.filename, null, false, true);
					addTimer(ti);
					ti.setData(tr.start);
				}
			}
		}
//		doCheckMail();
//		if (isHasWord) {
//			timerInfo ti = new timerInfo(true, "Black处于锁定模式<br>因为系统已安装了Office Word<br>请使用Office Word编辑文档", null);
//			ti.type = -7889;
//			ti.showProgress = false;
//			ti.isNew = true;
//			addTimer(ti);
//		}

		if (ba.checkBingPicIsEnable_justOne()) {
			ba.useBingPic();
			p("预下载Bing每日图片");
		}
		ba.shadow();
		ba.randomImg();
	}

	/**
	 * 确定给定的可执行文件名称（含扩展名，区分大小写）所表示的程序是否在运行
	 * 
	 * @param exeName
	 * @return
	 */
	static boolean exeIsRuning(String exeName) {
		boolean is = false;
		try {
			Process exec = Runtime.getRuntime().exec("tasklist");
			StringBuilder sb = black.readProcessOutput(exec, "gbk");
			if (sb.toString().indexOf(exeName) != -1) {
				is = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	static boolean exeIsRuningWithTitle(String windowTitle) {
		boolean is = false;
		try {
			Process exec = Runtime.getRuntime().exec("tasklist /V");
			StringBuilder sb = black.readProcessOutput(exec, "gbk");
			if (sb.toString().indexOf(windowTitle) != -1) {
				is = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	void setSpeak() {
		isSpeak = !isSpeak;
		setLoadFileMessage("朗读语音：" + isSpeak);
	}

	void stopVoice() {
		p(thread_voice.isAlive());
		thread_voice.stop();
		thread_voice.interrupt();
		// 升级jdk14后下面这行报错，先注释掉
//		thread_voice.destroy();
		thread_voice = null;
		p("stop");
	}

	void speakText(String text, int rate) {
		if (isSpeak || rate != -1) {
			uiRun(this, new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int voiceRate = getIntValueFromSettings(appInfo.voiceRate, "0");
					if (speech == null) {
						speech = new MSTTSSpeech();
						if (rate == -1)
							speech.setRate(voiceRate);
						else
							speech.setRate(rate);
//						speech.setVolume(10);
					}
				}
			});
			thread_voice = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					speech.speak(text);
					thread_voice = null;
				}
			});
			thread_voice.start();
		}

	}

	void changeToAdminMode() {
		if (userName.equals("Administrator")) {
			int messageBoxWithYesNO = getMessageBoxWithYesNO("权限降级", "你确定要从管理员降级为普通用户吗？", "确定", "取消",
					QMessageBox.Icon.Question, 0);
			if (messageBoxWithYesNO == 1) {
				userName = System.getProperty("user.name");
				if (!userName.equals("Administrator")) {
					getMessageBox("降级", "已降级为普通用户！");
				} else
					getMessageBox("降级", "无法降级权限，因为程序是以管理员用户启动的！");
			}
			return;
		}

		InputDialog input = new InputDialog(this, "切换到管理员模式", "请输入管理员密码：", true, false, null) {

			@Override
			void whenOkButtonPressed() {
				// TODO Auto-generated method stub

			}

			@Override
			void whenCannelButtonPressed() {
				// TODO Auto-generated method stub

			}

			void whenClose() {
				// TODO Auto-generated method stub

			}
		};
		if (text.textCursor().hasSelection())
			input.setLineText(text.textCursor().selectedText());

		int result = input.exec();
		if (result == 1) {
			String value = input.value();
			DesUtils des = new DesUtils("black");
			try {
				String key = getStringValueFromSettings(appInfo.additionalInfo, "");
				if (key.isEmpty()) {
					getMessageBox("错误", "尚未设定additionalInfo！");
					return;
				}
				String decrypt = des.decrypt(key);
				if (decrypt.equals(value)) {
					userName = "Administrator";
					if (!dontNotSetMonitor)
						dontNotSetMonitor();
					getMessageBox("用户模式切换", "已切换到管理员模式！");
					try {
						String getMailPath = "./bin_//getMail.jar";
						sum_getMail = MD5.getMD5Checksum(getMailPath);
						Object md5getmail = settings.value(appInfo.MD5OFGetMail, "");
						if (!sum_getMail.equals(md5getmail))
							if (!sum_getMail.isEmpty()) {
								settings.setValue(appInfo.MD5OFGetMail, sum_getMail);
								getMessageBox("", "MD5 of getMail.jar is Updated!");
							}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
					getMessageBox("密码不匹配", "密码错误！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				getMessageBox("解密", "解密字符串时出错：\n" + e.getMessage());
			}
		}
	}

	boolean isAdmin() {
//		if (userName.equals("Administrator")) {
//			return true;
//		} else
//			return false;
		return true;
	}

	void setLightOrDark() {
		if (writingView == 0) {
			setLoadFileMessage("只在写作视图下允许更改颜色模式");
			return;
		}
		boolean lightOrDark = !getBooleanValueFromSettings(appInfo.LightOrDark, "false");
		settings.setValue(appInfo.LightOrDark, lightOrDark);

//		if(ba.checkBingPicIsEnable())ba.useBingPic();

		update();
		applyColorChange();
		if (lightOrDark)
			setLoadFileMessage("亮色模式");
		else
			setLoadFileMessage("暗色模式");
	}

	void applyColorChange() {
		if (text == null)
			return;
		QPalette p_textW = textWidget.palette();
		QPalette p_text = text.palette();
		QPalette pb = palette();
		QPalette p = text.caret.palette();
		if (se == null) {
			se = new QGraphicsDropShadowEffect(timeMessage);
			se.setBlurRadius(5);
			se.setOffset(2, 2);
//			timeMessage.setGraphicsEffect(se);
		}
		se.setEnabled(false);

		if (writingView > 0) {
			QPalette palette = this.timeMessage.palette();
			palette.setBrush(QPalette.ColorRole.WindowText, new QBrush(new QColor(Qt.GlobalColor.darkGreen)));
			this.timeMessage.setPalette(palette);
			p_text.setColor(ColorRole.Base, new QColor(0, 0, 0, 0));
			int alpha = getIntValueFromSettings(appInfo.writingViewAlpha, "255");
			/**
			 * showPic貌似先在是用来区分颜色模式的，值是亮色模式还是暗色模式 高性能写作视图下： 插入符的颜色与文字颜色相同，不支持自定义 不显示编辑器背景色
			 * 亮色模式下背景为白色，编辑器文字颜色为黑色，不能更改 暗色模式下背景为黑色，编辑器文字颜色使用非高性能写作视图用户自定义的颜色，可以更改
			 */

			if (!getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
				// 设定loadmessage颜色
				QPalette p_loadMessage = this.loadFileMessage.palette();
				p_loadMessage.setColor(QPalette.ColorRole.WindowText,
						(QColor) settings.value(appInfo.darkLoadMessageColor, new QColor(Qt.GlobalColor.darkGreen)));
				this.loadFileMessage.setPalette(p_loadMessage);
				// 设定timemessage颜色
				QPalette p_timeMessage = this.timeMessage.palette();
				p_timeMessage.setColor(QPalette.ColorRole.WindowText,
						(QColor) settings.value(appInfo.darkTimeMessageColor, new QColor(Qt.GlobalColor.darkGreen)));
				this.timeMessage.setPalette(p_timeMessage);

//				text.verticalScrollBar().setStyleSheet("background:green");
				p_textW.setColor(ColorRole.Window,
						(QColor) settings.value(appInfo.darkPageColor, new QColor(0, 0, 0, 0)));

				QColor textDark = (QColor) settings.value(appInfo.darkTextColor, new QColor(Qt.GlobalColor.black));
				if (getBooleanValueFromSettings(appInfo.simpleWritingView, "false")) {
					p.setColor(text.caret.backgroundRole(), (QColor) settings.value(appInfo.darkCaretColor, textDark));
				}
				p_text.setColor(ColorRole.Text, textDark);
				p_text.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Highlight,
						new QColor(Qt.GlobalColor.green));
				p_text.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.HighlightedText,
						new QColor(Qt.GlobalColor.black));
				// 高性能写作视图下窗口颜色和透明度才会有效
				if (!getBooleanValueFromSettings(appInfo.useDarkWindowBgImg, "true"))
					pb.setColor(QPalette.ColorRole.Window,
							(QColor) settings.value(appInfo.darkWindowColor, new QColor(Qt.GlobalColor.black)));
				else {
					se.setColor(ba.deffColor((QColor) settings.value(appInfo.darkTimeMessageColor,
							new QColor(Qt.GlobalColor.darkGreen))));
					se.setEnabled(true);
					timeMessage.setGraphicsEffect(se);
					String picPath = null;
					if (getBooleanValueFromSettings(appInfo.darkBingPic, "true")) {
						picPath = "./RC/Bing/bing.jpg";
					} else {
						picPath = "./RC/" + (String) settings.value(appInfo.darkWindowBgImg);
					}
					int v = b.getIntValueFromSettings(appInfo.randomImg, "1000");
					if(v == 0) {
						QImage windowBg = new QImage(picPath);
						QRect dg = ba.desktopGeometry(false);
						windowBg = windowBg.scaled(dg.width(), dg.height());
						QBrush brush = new QBrush(windowBg);
						pb.setBrush(QPalette.ColorRole.Window, brush);
					}else {
						pb.setColor(ColorRole.Window,new QColor(255, 255, 255));
					}
					
				}
			} else {
				// 设定loadmessage颜色
				QPalette p_loadMessage = this.loadFileMessage.palette();
				p_loadMessage.setColor(QPalette.ColorRole.WindowText,
						(QColor) settings.value(appInfo.lightLoadMessageColor, new QColor(Qt.GlobalColor.darkGreen)));
				this.loadFileMessage.setPalette(p_loadMessage);
				// 设定timemessage颜色
				QPalette p_timeMessage = this.timeMessage.palette();
				p_timeMessage.setColor(QPalette.ColorRole.WindowText,
						(QColor) settings.value(appInfo.lightTimeMessageColor, new QColor(Qt.GlobalColor.darkGreen)));
				this.timeMessage.setPalette(p_timeMessage);
				// 设定滚动条颜色
//				text.verticalScrollBar().setStyleSheet("background:green");

				p_textW.setColor(ColorRole.Window,
						(QColor) settings.value(appInfo.lightPageColor, new QColor(0, 0, 0, 0)));
				if (getBooleanValueFromSettings(appInfo.simpleWritingView, "false")) {
					QColor textLight = (QColor) settings.value(appInfo.lightTextColor,
							new QColor(Qt.GlobalColor.white));
					p_text.setColor(ColorRole.Text, textLight);
					p.setColor(text.caret.backgroundRole(),
							(QColor) settings.value(appInfo.lightCaretColor, textLight));
				} else
					p_text.setColor(ColorRole.Text,
							(QColor) settings.value(appInfo.colortext, new QColor(Qt.GlobalColor.black)));
				p_text.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Highlight,
						new QColor(Qt.GlobalColor.black));
				p_text.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.HighlightedText,
						new QColor(Qt.GlobalColor.white));
				// 高性能写作视图下窗口颜色和透明度才会有效
				if (!getBooleanValueFromSettings(appInfo.useLightWindowBgImg, "true"))
					pb.setColor(QPalette.ColorRole.Window,
							(QColor) settings.value(appInfo.lightWindowColor, new QColor(Qt.GlobalColor.black)));
				else {
					se.setColor(ba.deffColor((QColor) settings.value(appInfo.lightTimeMessageColor,
							new QColor(Qt.GlobalColor.darkGreen))));
					se.setEnabled(true);
					timeMessage.setGraphicsEffect(se);
					String picPath = null;
					if (getBooleanValueFromSettings(appInfo.lightBingPic, "true")) {
						picPath = "./RC/Bing/bing.jpg";
					} else {
						picPath = "./RC/" + (String) settings.value(appInfo.lightWindowBgImg);
					}
					int v = b.getIntValueFromSettings(appInfo.randomImg, "1000");
					if(v == 0) {
						QImage windowBg = new QImage(picPath);
						QRect dg = ba.desktopGeometry(false);
						windowBg = windowBg.scaled(dg.width(), dg.height());
						QBrush brush = new QBrush(windowBg);
						pb.setBrush(QPalette.ColorRole.Window, brush);
					}else {
						pb.setColor(ColorRole.Window,new QColor(255, 255, 255));
					}
					
				}
			}

			// 非写作视图
		} else {
//			timeMessage.setGraphicsEffect(null);
			// 设定loadmessage颜色
			QPalette p_loadMessage = this.loadFileMessage.palette();
			p_loadMessage.setColor(QPalette.ColorRole.WindowText, (QColor) new QColor(Qt.GlobalColor.black));
			this.loadFileMessage.setPalette(p_loadMessage);
			// timemessage
			QPalette palette = this.timeMessage.palette();
			palette.setBrush(QPalette.ColorRole.WindowText, new QBrush(new QColor(Qt.GlobalColor.darkGreen)));
			this.timeMessage.setPalette(palette);

			p_textW.setColor(ColorRole.Window, withoutBlueColor(new QColor(Qt.GlobalColor.white), withoutBuleValue));
			p_text.setColor(ColorRole.Base, withoutBlueColor(new QColor(Qt.GlobalColor.white), withoutBuleValue));
			p_text.setColor(ColorRole.Text, withoutBlueColor(new QColor(Qt.GlobalColor.black), withoutBuleValue));
			pb.setColor(QPalette.ColorRole.Window, new QColor(Qt.GlobalColor.white));
			p_text.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Highlight, new QColor(Qt.GlobalColor.black));
			p_text.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.HighlightedText,
					new QColor(Qt.GlobalColor.white));
//			text.verticalScrollBar().setStyleSheet("");
			QColor c = (QColor) settings.value(appInfo.caretColor_normal, p_text.color(ColorRole.Text));
			p.setColor(text.caret.backgroundRole(), c);
		}
		// 非高性能写作视图模式下可以更改插入符的颜色
		if (writingView == 0) {

		} else if (!getBooleanValueFromSettings(appInfo.simpleWritingView, "false")) {
			QColor c = (QColor) settings.value(appInfo.caretColor_writingview, p_text.color(ColorRole.Text));
			p.setColor(text.caret.backgroundRole(), c);
		}

		text.caret.setPalette(p);
		textWidget.setPalette(p_textW);
		text.setPalette(p_text);
		setPalette(pb);
		QColor c = textWidget.palette().color(textWidget.backgroundRole());
		if(writingView == 0 || c.alpha() < 10) {
			Thread t = (Thread) b.ba.tempData.get("randomImgThread");
			if(t != null) {
				b.ba.tempData.remove("randomImgThread");
//				ba.tempData.remove("randomImg");
//				ba.tempData.remove("randomImgOld");
				while(!t.isInterrupted()) {
					t.interrupt();
					b.debugPrint("线程是否被中断："+t.isInterrupted());
				}
			}
		}else {
			b.ba.randomImg();
		}
//		btext.cursorPosChanged();
	}

	QColor withoutBlueColor(QColor color, int value) {
		if (writingView == 0 || getIntValueFromSettings(appInfo.editorColor, "0") == 0)
			return color;
		float lr = (255F - color.red()) / 100F;
		float lb = color.blue() / 100F;
//		float lg = (255F-color.green())/100F;
		int newR = (int) (color.red() + lr * value);
		int newB = (int) (color.blue() - lb * value);
//		int newG = (int) (color.green()+lg*value);
		if (newR > 255)
			newR = 255;
		if (newB < 0)
			newB = 0;
//		if(newG > 255) newG = 255;
		color.setRed(newR);
		color.setBlue(newB);
//		color.setGreen(newG);
		return color;
	}

	public static ArrayList<Integer> resetByMax(ArrayList<Integer> al) {
		ArrayList<Integer> re = new ArrayList<>();

		while (al.size() > 0) {
			int a = -1;
			int index_currentMax = 0;

			for (int i = 0; i < al.size(); i++) {
				int it = al.get(i);
				if (it > a) {
					a = it;
					index_currentMax = i;
				}
			}
			re.add(a);
			al.remove(index_currentMax);
		}
		return re;
	}

	void resetProjectFileIndex(boolean showMessage) {
		if (projectFile != null) {
			debugPrint("yesss");
			String path = projectFile.getParent() + "\\Files";
			File f = new File(path);
			File[] fs = f.listFiles();

			ArrayList<Integer> al = new ArrayList<>();
			for (File ff : fs) {
				String name = ff.getName();
				String[] ss = cheakDocument.subString(name, ".black");
				al.add(new Integer(ss[0]));
			}
			ArrayList<Integer> re = resetByMax(al);
			if (re.size() > 0) {
				Integer max = re.get(0);
				String property = projectInfo.getProperty("fileindex");
				String resetMessage = "";
				Integer vo = -1;
				if (property != null) {
					vo = Integer.valueOf(property);
					resetMessage = "\nfileindex属性的值在合理的范围内，无需进行调整";
					if (vo <= max) {
						projectInfo.setProperty("fileindex", (max + 1) + "");
						resetMessage = "\n已将fileindex属性的值设置成了" + max;
					}

				} else {
					projectInfo.setProperty("fileindex", (max + 1) + "");
					resetMessage = "\n已将fileindex属性的值设置成了" + max;
				}
				if (showMessage)
					getMessageBox("检查并重设fileindex属性",
							"当前index：" + vo + "\nFiles目录里的文件的最大编号：" + max + ".black" + resetMessage);
				else
					p(resetMessage);
			}
		}
	}

	void runBackup(String sec, String dest, int id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<File> al = new ArrayList<File>();
				boolean iscopy = fileTool.copy(sec, dest, al);
				if (iscopy) {
					projectInfo.setProperty(appInfo.lastBackUpTime, System.currentTimeMillis() + "");
					lastBackUpTime = System.currentTimeMillis();
					timerInfo timerInfo = getTimerInfo(id);
					timerInfo.timerName = "备份了" + al.size() + "个文件";
					timerInfo.showProgress = false;
					new bRunnable(10000, true, false, false, true) {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							removeTimer(id);
						}
					};
				} else {
					removeTimer(id);
				}
			}
		}).start();
	}

	boolean isSameDir(String backupDir) {
		File backupdir = new File(backupDir);
		if (backupdir.isDirectory()) {
			if (projectFile.getParentFile().getParent().equals(backupDir)) {
				File[] files = backupdir.listFiles();
				for (File f : files) {
					if (f.getName().equals(getProjectName()) && f.isDirectory()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	void backup() {
		backupDir = projectInfo.getProperty(appInfo.backUpDir, "");
		if (!backupFromSynTime)
			if (backupDir.isEmpty() || !new File(backupDir).exists() || isSameDir(backupDir)) {
				File[] roots = File.listRoots();
				for (File ff : roots) {
					File f = new File(ff.getPath() + "BlackBackups");
					if (f.exists()) {
						if (!isSameDir(f.getPath())) {
							getMessageBox("自动查找备份位置",
									"已在驱动器" + ff + "里查找到备份目录\n" + appInfo.appName + "以后将自动备份项目到该位置：\n" + f.getPath());
							backupDir = f.getPath();
//							projectInfo.setProperty(appInfo.backUpDir, backupDir);
							break;
						}
					}
				}
			}

		if (backupDir.isEmpty()) {
			if (!isAdmin())
				return;
			uiRun(black.this, new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					backupDir = getDirDialog("选择备份位置");
				}
			});
			if (!backupDir.isEmpty() && new File(backupDir).isDirectory())
				projectInfo.setProperty(appInfo.backUpDir, backupDir);
		}
		if (new File(backupDir).exists()) {
			if (isSameDir(backupDir)) {
				getMessageBox("备份项目", "无法执行备份，因为当前所编辑的项目的路径与备份路径相同！");
				return;
			}
			if (isHasTimer(1234567))
				return;

			removeAllTimer(123456);
			timerInfo timerInfo = new timerInfo(true, "正在备份项目", null);
			timerInfo.type = 1234567;
			int id = addTimer(timerInfo);
			runBackup(projectFile.getParent(), backupDir, id);
		} else {
			if (!backupFromSynTime) {
				if (isHasTimer(1234))
					return;
				timerInfo ti = new timerInfo(true, "无法找到备份位置！", null);
				ti.showProgress = false;
				ti.type = 1234;
				int id = addTimer(ti);
				new bRunnable(5000, true, false, false, true) {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						removeTimer(id);
					}
				};
			} else {
				if (isHasTimer(123456))
					return;
				timerInfo timerInfo = new timerInfo(true, "需要备份项目！", null);
				timerInfo.showProgress = false;
				timerInfo.type = 123456;
				addTimer(timerInfo);
				speakText("请备份项目！", -1);
			}
		}
	}

	public void cloneFromLocal() {
		File dir = null;
		if (projectFile != null)
			dir = new File(projectFile.getParent() + "_clone_" + time.getCurrentDate("-"));
		else
			dir = new File(filesListFile.getParentFile().getParent() + "_clone_" + time.getCurrentDate("-"));
		if (!dir.exists()) {
			dir.mkdir();
			p(dir);
		} else {
			getMessageBox("从本地git仓库克隆项目", "目录" + dir + "已存在，已停止克隆\n-----\n按Ctrl+Shift+D可删除该目录");
			p(dir + "已存在,停止克隆!");
			return;
		}
		int id = addTimer(new timerInfo(true, "正在从本地仓库克隆项目", null));
		String dir_path = dir.getPath();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				copyBranchFromLocal(dir_path, projectInfo.getProperty(appInfo.projectName), null, false);
				uiRun(black.this, new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						p("克隆成功!");
						removeTimer(id);
						getMessageBox("从本地git仓库克隆项目", "克隆成功！");
						showinExplorer(dir_path, true);
					}
				});
			}
		}).start();
	}

	public void changeAlpha_text() {
		if ((!this.windowOpacity) || (writingView != 2) || (this.alpha_windowBackground != 0)) {
			return;
		}
		if (this.alpha_text == 204) {
			this.alpha_text = 1;
		} else if (this.alpha_text == 1) {
			this.alpha_text = 50;
		} else if (this.alpha_text == 50) {
			this.alpha_text = 100;
		} else if (this.alpha_text == 100) {
			this.alpha_text = 150;
		} else if (this.alpha_text == 150) {
			this.alpha_text = 204;
		}
		update();

		setLoadFileMessage("alpha: " + this.alpha_text);
	}

	static boolean checkWord() {
		return false;
//		boolean isExists = false;
//        String system = System.getenv("SystemDrive");
//    	String path = system+"\\Program Files\\Microsoft Office\\Office16\\WINWORD.exe";
//    	if(new File(path).exists()) {
//    		isExists = true;
//    	}
//    	return isExists;
	}

	void doCheckMail() {
		if (blackUD || appInfo.mode == 0)
			return;

		timerInfo ti = new timerInfo(-997, 3600000, "同步邮件", new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!time.getCurrentDate(" ").equals(lastCheckMailDate))
					checkMail();
				doCheckMail();
			}
		}, false, true);
		addTimer(ti);
	}

	void showPay() {
		bAction.showPayBox(this);
	}

	public ArrayList<timerInfo> getAllTimer(int type) {
		ArrayList<timerInfo> al = new ArrayList<timerInfo>();
		for (timerInfo in : timerInfos) {
			if (in.type == type)
				al.add(in);
		}
		return al;
	}

	pinyinTool initPinyinTool() {
		if (pinyinTool == null) {
			pinyinTool = new pinyinTool(new File("./PinyinTool"));
		}
		return pinyinTool;
	}

	void noBackgroundForWritingView() {
		if (writingView == 0)
			return;
		boolean v = !getBooleanValueFromSettings(appInfo.noBackgroundForWritingView, "false");
		settings.setValue(appInfo.noBackgroundForWritingView, v + "");
		this.update();
	}

	void showAppUpdateLog() {
		Object value = settings.value(appInfo.appVersionKey, "-1");
		Double valueOf = Double.valueOf((String) value);
		if (valueOf < Double.valueOf(appInfo.appVersion)) {
			getBMessageBox(appInfo.appName + "已更新", "<h2>此次更新包含以下新特性或改进：</h2>" + ba.getUpdateInfoMini(1)
					+ "<p align=right>" + "<b>更新日期" + appInfo.buildDate + "</b></p>");
		}
	}

	/**
	 * 为给定的Qwidget设定窗口显示规则，防止它超出显示范围
	 */
	void desktopRectRule(QWidget w) {
		QDesktopWidget desktop = new QDesktopWidget();
		QRect availableGeometry = desktop.availableGeometry();
		int newX = -1, newY = -1;
		if (w.x() + w.width() > availableGeometry.width()) {
			int v = w.pos().x() + w.width() - availableGeometry.width();
			newX = w.x() - v;
		} else if (w.x() <= 0)
			newX = 0;

		if (w.y() + w.height() > availableGeometry.height()) {
			int v = w.pos().y() + w.height() - availableGeometry.height();
			newY = w.y() - v;
		} else if (w.y() <= 0)
			newY = 0;

		if (newX == -1)
			newX = w.x();
		if (newY == -1)
			newY = w.y();
		w.setGeometry(newX, newY, w.width(), w.height());
	}

	void toFullPinyin(String str, int start, boolean dontfind) {
		// 在没启用嵌入式输入前，下面的代码都在方法最后的新线程内执行
		String spiltChar = "";
		if (getBooleanValueFromSettings(appInfo.useSpiltCharForPinyin, "true"))
			spiltChar = "'";

		String sub1 = initPinyinTool().toFullPinyin(str.substring(0, start), spiltChar);
		String sub2 = initPinyinTool().toFullPinyin(str.substring(start, str.length()), spiltChar);
		String fullpinyin = sub1 + sub2;
		uiRun(this, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showIMEString(null, -1, sub1, sub2, fullpinyin, true);
			}
		});
		findKeywordByPinyin(fullpinyin, str, dontfind);

	}

	/**
	 * 通过拼音检索关键词 此方法在延时之后开始通过拼音检索关键词
	 * 如果dontfind参数为false，调用此方法将会结束上一次调用此方法启动的延时检索动作（如果有的话）
	 * 
	 * @param fullPinyin
	 * @param doublePinyin
	 * @param dontfind
	 */
	void findKeywordByPinyin(String fullPinyin, String doublePinyin, boolean dontfind) {
		debugPrint(debug.getNameOfClassAndMethodOfCaller());
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				uiRun(black.this, new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						int ms = getIntValueFromSettings(appInfo.pinyinStartSegmentMS, "500");
						ba.tempData.put(appInfo.pinyinStartSegmentMS, ms);
					}
				});

				if (!dontfind) {
					if (findInMark_brun != null) {
						findInMark_brun.stop();
						findInMark_brun = null;
					}

					findInMark_brun = new bRunnable(getIntValueFromSettings(appInfo.pinyinStartSegmentMS, "500"), true,
							false, false, true) {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (black.this.findInMark_brun != null || !dontfind)
								findMarkString(fullPinyin, doublePinyin);
						}
					};
				} else {
					if (findInMark_brun != null) {
						findInMark_brun.stop();
						findInMark_brun = null;
					}
				}
			}
		}).start();
	}

	void debugPrint(Object o) {
		if (dbm) {
			System.out.println(o);
		}
	}

	void debugPrintWithTime(Object o) {
		if (dbm) {
			p(o);
		}
	}

	void showIMEString(String str, int start, String sub1, String sub2, String fullpinyin, boolean dontfind) {
		debugPrint(str);
		if (text.preeditStringPos == -1 && text.IMEPreeditString.isEmpty()) {
			if (IME != null && IME.isVisible())
				IME.hide();
			return;
		}
		if (str != null) {
			if (str.isEmpty()) {
				return;
			} else {
				// 如果没有读取关键词列表，为了节省能耗，禁用全拼转双拼
				if (Boolean.valueOf((String) settings.value(appInfo.conversionToFullPinyin, "false"))) {
					if (markTextData != null
							|| !Boolean.valueOf((String) settings.value(appInfo.hideIMEPanel, "false"))) {
						toFullPinyin(str, start, dontfind);
						return;
					}
				}
			}

		}
		if (getBooleanValueFromSettings(appInfo.hideIMEPanel, "false")
				|| getBooleanValueFromSettings(appInfo.useInLineIME, "false")) {
			return;
		}
		if (sub1 == null && sub2 == null) {
			if (!str.isEmpty()) {
				sub1 = str.substring(0, start);
				sub2 = str.substring(start, str.length());
			} else
				sub1 = sub2 = "";

		}

		QRect cursorRect = text.cursorRect();
		QPoint mapTo = text.viewport().mapToGlobal(new QPoint(cursorRect.x(), cursorRect.y()));
		int editorCaretWidthValue = getEditorCaretWidthValue();
		if (IME == null) {
			IME = new QLabel(textWidget) {
				@Override
				protected void paintEvent(QPaintEvent e) {
					// TODO Auto-generated method stub

					QPainter p = new QPainter(this);
					QColor bg = new QColor(41, 41, 41);
//					if(writingView > 0) {
//						QColor color = textWidget.palette().color(textWidget.backgroundRole());
//						if(color.alpha() == 0) {
//							color = palette().color(backgroundRole());
//						}
//						bg = color;
//					}else {
////						bg = text.palette().color(ColorRole.Base);
//						bg = new QColor(41,41,41);
//
//					}

					bg.setAlpha(255);
					ba.tempData.put("IMEbg", bg);
					p.setBrush(bg);
					p.setPen(PenStyle.NoPen);
//					QRect rect = this.geometry();
//					new QRect(rect.left(), top, width, height)

					p.setRenderHint(RenderHint.Antialiasing);
					QRect rr = this.rect();
					int mar = this.margin() - 10;

					rr = new QRect(rr.left() + mar, rr.top() + mar, rr.right() - mar * 2, rr.bottom() - mar * 2);
					p.drawRoundedRect(rr, 10, 10);

					QPolygon polygon = new QPolygon();
					QRect cr = text.cursorRect();
					QPoint crmapTo = text.mapToGlobal(new QPoint(cr.x(), cr.y()));
					int x1 = crmapTo.x() + editorCaretWidthValue / 2;
					int y1 = crmapTo.y() - (mar - 5);
					int x2 = x1 - 5;
					int y2 = y1 - 6;
					int x3 = x1 + 5;
					int y3 = y2;
					QPoint map1 = this.mapFromGlobal(new QPoint(x1, y1));// 中间点
					QPoint map2 = this.mapFromGlobal(new QPoint(x2, y2));
					QPoint map3 = this.mapFromGlobal(new QPoint(x3, y3));
					debugPrint("hello: " + map1 + " " + map2 + " " + map3);
					polygon.add(map1);
					polygon.add(map2);
					polygon.add(map3);
					p.drawPolygon(polygon);

//					p.setBrush(BrushStyle.NoBrush);
//					
//					p.setPen(new QPen(new QColor(Qt.GlobalColor.lightGray),1));
//					p.drawRoundedRect(rr, 10, 10);
//					p.drawLine(map1,map2);
//					p.drawLine(map1,map3);
//					p.setPen(new QPen(bg,1));
//					p.drawLine(map2.x()+1,map2.y(),map3.x()-1,map3.y());
////					p.drawLine(map2, map3);
					p.end();
					super.paintEvent(e);

				}
			};
			Qt.WindowFlags windowFlags = new Qt.WindowFlags(new Qt.WindowType[] { Qt.WindowType.FramelessWindowHint,
					Qt.WindowType.WindowStaysOnTopHint, Qt.WindowType.Tool });
			IME.setWindowFlags(windowFlags);
//			IME.setContentsMargins(10, 10, 10, 20);
			IME.setMargin(20);
			IME.setAttribute(WidgetAttribute.WA_TranslucentBackground);
//			IME.setWindowOpacity(0.5);
			QGraphicsDropShadowEffect shad = new QGraphicsDropShadowEffect();
			QColor c_shad = new QColor(Qt.GlobalColor.black);
			c_shad.setAlpha(50);
			shad.setColor(c_shad);
			shad.setBlurRadius(10);
			shad.setOffset(1, 1);
			IME.setGraphicsEffect(shad);
		}

//		IME.setFrameStyle(QFrame.Shape.StyledPanel.value());
//		IME.setAutoFillBackground(true);
		String cname = "white";
		String colorname = "yellow";
		if (writingView > 0)
			cname = new QColor(139, 139, 139).name();
		IME.setStyleSheet("background:translucent;color:" + cname);
		QFont font = text.font();
//		font.setFamily("Courier New");
//		font.setUnderline(true);
		QFont f = font();
		f.setPointSize(font.pointSize());
//		f.setLetterSpacing(font.letterSpacingType(), font.letterSpacing());
		IME.setFont(f);
		String mark = "^";
		IME.setAlignment(AlignmentFlag.AlignVCenter);
		IME.setText("<u>" + sub1 + "</u>" + "<font face=\"" + font.family() + ", serif\" size=\"" + 3 + "\" color=\""
				+ colorname + "\" bgcolor=\"white\"><b>" + mark + "</b></font>" + sub2);

		IME.adjustSize();
//		int newY = -1;
//		if (IME.height() > cursorRect.height()) {
//			newY = mapTo.y() - (IME.height() - cursorRect.height()) / 2;
//		} else if (IME.height() == cursorRect.height()) {
//			newY = mapTo.y();
//		} else {
//			newY = mapTo.y() + (cursorRect.height() - IME.height()) / 2;
//		}
		int width = IME.width();
		if (width < 60) {
			width = width + (60 - width);
		}

		QRect rect = new QRect(mapTo.x() - 30 - text.caret.width() / 2, mapTo.y() - IME.height(), width, IME.height());

		setAnimation(IME, "geometry", IME.geometry(), rect, 0);

		IME.show();

		desktopRectRule(IME);
	}

	void resetTextStringLayout() {
		int messageBoxWithYesNO = getMessageBoxWithYesNO("警告", "此操作可能会使此文档内的锚点（如果有）出错，你确定要继续此操作吗？", "确定", "取消",
				QMessageBox.Icon.Warning, 0);
		if (messageBoxWithYesNO == 0)
			return;

		String str = text.toPlainText();
		List<String> allLine = cheakDocument.getAllLine(str);
		StringBuilder sb = new StringBuilder();
		for (String s : allLine) {
			if (!s.isEmpty()) {
				String substring = s.substring(s.length() - 1, s.length());
				if (s.indexOf("第") != -1 && s.indexOf("章") != -1) {
					sb.append(s + "\n");
				} else if (!substring.equals("。") && !substring.equals("”")) {
					sb.append(s);
				} else
					sb.append(s + "\n");
			}
		}
		QTextCursor tc = text.textCursor();
		tc.select(SelectionType.Document);
		tc.beginEditBlock();
		tc.insertText(sb.toString());
		tc.endEditBlock();
		setStyleForCurrentDocument(false);
	}

	void findLogsInRootDir() {
		String rootdir = System.getProperty("user.dir");
		File root = new File(rootdir);
		if (root.exists()) {
			isYourNeedFile isYourNeedFile = new isYourNeedFile(root, ".*.log");
			String[] filter = isYourNeedFile.Filter();
			if ((filter != null) && (filter.length > 0)) {
				int r = getMessageBoxWithYesNO("启动错误", "在程序根目录下发现了虚拟机崩溃日志文件", "打开程序根目录", "删除全部日志文件",
						QMessageBox.Icon.Question, 0);
				if (r == 1) {
					showinExplorer(rootdir, false);
				} else {
					for (String s : filter) {
						File log = new File(root.getAbsolutePath() + File.separator + s);
						if (log.exists()) {
							if (log.delete()) {
								p("删除成功：" + log.getName());
							}
						}
					}
				}
			}
		}
	}

	QPainterPath getPathForMask(int width, int height, int r) {
		QPainterPath path = new QPainterPath();
		path.moveTo(0.0D, height + r);
		path.lineTo(0.0D, 0.0D);
		path.lineTo(width, 0.0D);
		path.lineTo(width, height + r);
		path.lineTo(width - r, height);
		path.lineTo(r, height);
		path.lineTo(0.0D, height + r);
		return path;
	}

	QPainterPath getPath(int width, int height, int r) {
		QPainterPath path = new QPainterPath();
		path.moveTo(0.0D, height + r);
		path.lineTo(0.0D, 0.0D);
		path.lineTo(width, 0.0D);
		path.lineTo(width, height + r);
		path.arcTo(width - r * 2, height, r * 2, r * 2, 0.0D, 90.0D);
		path.lineTo(r, height);
		path.arcTo(0.0D, height, r * 2, r * 2, 90.0D, 90.0D);
		return path;
	}

	QRegion getRegion(QPainterPath path) {
		return new QRegion(path.toFillPolygon().toPolygon());
	}

	void setFontStyle(QFont font) {
		if (getBooleanValueFromSettings(appInfo.boldFont, "false")) {
			font.setUnderline(true);
		}
		if (getBooleanValueFromSettings(appInfo.italicFont, "false")) {
			font.setItalic(true);
		}
	}

	void setBold() {
		boolean v = !getBooleanValueFromSettings(appInfo.boldFont, "false");
		this.settings.setValue(appInfo.boldFont, v);
		QFont font = text.font();
		if (getBooleanValueFromSettings(appInfo.boldFont, "false")) {
			font.setUnderline(true);
		} else {
			font.setUnderline(false);
		}
		text.setFont(font);
	}

	void setItalic() {
		boolean v = !getBooleanValueFromSettings(appInfo.italicFont, "false");
		this.settings.setValue(appInfo.italicFont, v);
		QFont font = text.font();
		if (getBooleanValueFromSettings(appInfo.italicFont, "false")) {
			font.setItalic(true);
		} else {
			font.setItalic(false);
		}
		text.setFont(font);
	}

	void changedEditFile() {
		if (this.lastEditFile == null) {
			return;
		}
		File f = this.lastEditFile;
		changedFileOfCurrentEdit();
		openFileByTreeItem(findTreeItemByFileName(f.getName(), null));
		synTime();
	}

	void changedForOpenedFiles() {
		if (this.currentEditing == null) {
			return;
		}
		boolean start = false;
		boolean isFind = false;
		for (int i = 0; i < this.openedFiles.size(); i++) {
			dataForOpenedFile dataForOpenedFile = (dataForOpenedFile) this.openedFiles.get(i);
			if ((!start) && (dataForOpenedFile.filename.equals(this.currentEditing.getName()))) {
				start = true;
			} else if ((start) && (dataForOpenedFile.editor != null)
					&& (!dataForOpenedFile.filename.equals(this.currentEditing.getName()))) {
				isFind = true;
				this.addToMoveListEnd = true;
				changedFileOfCurrentEdit();
				openFileByTreeItem(findTreeItemByFileName(dataForOpenedFile.filename, null));
				synTime();
				break;
			}
		}
		if (!isFind) {
			for (int i = 0; i < this.openedFiles.size(); i++) {
				dataForOpenedFile dataForOpenedFile = (dataForOpenedFile) this.openedFiles.get(i);
				if (dataForOpenedFile.filename.equals(this.currentEditing.getName())) {
					return;
				}
				if (dataForOpenedFile.editor != null) {
					this.addToMoveListEnd = true;
					changedFileOfCurrentEdit();
					openFileByTreeItem(findTreeItemByFileName(dataForOpenedFile.filename, null));
					synTime();
					break;
				}
			}
		}
	}

	public static int getIntValueFromSettings(String key, String defaultValue) {
		return Integer.valueOf((String) settings.value(key, defaultValue));
	}

	public static String getStringValueFromSettings(String key, String defaultValue) {
		return (String) settings.value(key, defaultValue);
	}

	public static boolean getBooleanValueFromSettings(String key, String defaultValue) {
		return Boolean.valueOf(String.valueOf(settings.value(key, defaultValue)));
	}

	void quiet() {
		quietMode = !quietMode;
		if (quietMode) {
			setLoadFileMessage("已开启静音模式");
			stopBeepSound();
		} else {
			setLoadFileMessage("已关闭静音模式");
		}
	}

	void te(String leftStr, String rightStr) {
		boolean is = false;
		QTextCursor tc = text.textCursor();
		int blockpos = tc.block().position();
		tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter, QTextCursor.MoveMode.KeepAnchor);
		if (tc.selectedText().equals(rightStr)) {
			QTextCharFormat cf = tc.charFormat();
			QBrush br = new QBrush(new QColor(Qt.GlobalColor.lightGray));
			QBrush fr = new QBrush(new QColor(Qt.GlobalColor.black));
			cf.setForeground(fr);
			cf.setBackground(br);
			ExtraSelection es = new ExtraSelection();
			es.setCursor(tc);
			es.setFormat(cf);
			btext.extraSelection.add(es);
		}
		do {
			tc.clearSelection();
			tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter, QTextCursor.MoveMode.KeepAnchor);
			if (tc.position() >= blockpos) {
				if (tc.selectedText().equals(leftStr)) {
					QTextCharFormat cf = tc.charFormat();
					QBrush br = new QBrush(new QColor(Qt.GlobalColor.yellow));
					QBrush fr = new QBrush(new QColor(Qt.GlobalColor.black));
					cf.setForeground(fr);
					cf.setBackground(br);
					ExtraSelection es = new ExtraSelection();
					es.setCursor(tc);
					es.setFormat(cf);
					btext.extraSelection.add(es);
					is = true;
				}
				if (tc.position() == blockpos) {
					is = true;
				}
			} else {
				is = true;
			}
		} while (!is);
	}

	void teF(String leftStr, String rightStr) {
		boolean is = false;

		QTextCursor tc = text.textCursor();
		int blockpos = tc.block().position() + tc.block().text().length();
		tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter, QTextCursor.MoveMode.KeepAnchor);
		if (tc.selectedText().equals(leftStr)) {
			QTextCharFormat cf = tc.charFormat();
			QBrush br = new QBrush(new QColor(Qt.GlobalColor.yellow));
			QBrush fr = new QBrush(new QColor(Qt.GlobalColor.black));
			cf.setForeground(fr);
			cf.setBackground(br);
			ExtraSelection es = new ExtraSelection();
			es.setCursor(tc);
			es.setFormat(cf);
			btext.extraSelection.add(es);
		}
		do {
			tc.clearSelection();
			tc.movePosition(QTextCursor.MoveOperation.NextCharacter, QTextCursor.MoveMode.KeepAnchor);
			if (tc.position() <= blockpos) {
				if (tc.selectedText().equals(rightStr)) {
					QTextCharFormat cf = tc.charFormat();
					QBrush br = new QBrush(new QColor(Qt.GlobalColor.lightGray));
					QBrush fr = new QBrush(new QColor(Qt.GlobalColor.black));
					cf.setForeground(fr);
					cf.setBackground(br);
					ExtraSelection es = new ExtraSelection();

					es.setCursor(tc);
					es.setFormat(cf);
					btext.extraSelection.add(es);
					is = true;
				}
				if (tc.position() == blockpos) {
					is = true;
				}
			} else {
				is = true;
			}
		} while (!is);
	}

	void setMarkBackground() {
		this.infoOfCurrentEditing.showMark = (!this.infoOfCurrentEditing.showMark);
	}

	void backspace() {
		Qt.KeyboardModifiers modifiers = new Qt.KeyboardModifiers(Qt.KeyboardModifier.NoModifier.value());
		QKeyEvent k = new QKeyEvent(QEvent.Type.KeyPress, Qt.Key.Key_Backspace.value(), modifiers);
		QApplication.postEvent(text, k);
	}

	void enter() {
		Qt.KeyboardModifiers modifiers = new Qt.KeyboardModifiers(Qt.KeyboardModifier.NoModifier.value());
		QKeyEvent k = new QKeyEvent(QEvent.Type.KeyPress, Qt.Key.Key_Enter.value(), modifiers);
		QApplication.postEvent(text, k);
	}

	void textContentsChange(int s, int e, int l) {
	}

	void showMark() {
		QTextCursor tc = text.textCursor();
		tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter, QTextCursor.MoveMode.KeepAnchor);
		String str = tc.selectedText();
		if (str.equals("“")) {
			teF("“", "”");
		} else if (str.equals("”")) {
			te("“", "”");
		} else if (str.equals("‘")) {
			teF("‘", "’");
		} else if (str.equals("’")) {
			te("‘", "’");
		} else if (str.equals("(")) {
			teF("(", ")");
		} else if (str.equals(")")) {
			te("(", ")");
		} else if (str.equals("《")) {
			teF("《", "》");
		} else if (str.equals("》")) {
			te("《", "》");
		} else if (str.equals("（")) {
			teF("（", "）");
		} else if (str.equals("）")) {
			te("（", "）");
		} else if (str.equals("【")) {
			teF("【", "】");
		} else if (str.equals("】")) {
			te("【", "】");
		}
	}

	void changeRepairBy() {
		int ins = getIntValueFromSettings(appInfo.insByMouseOrKey, "0");
		if (ins == 0) {
			settings.setValue(appInfo.insByMouseOrKey, "1");
			timerInfo ti = new timerInfo(true, "通过键盘改写标点", null);
			ti.type = 987;
			ti.showProgress = false;
			addTimer(ti);
			synTimeAction(brunnableForOSD);
		} else {
			settings.setValue(appInfo.insByMouseOrKey, "0");
			removeAllTimer(987);
			synTimeAction(brunnableForOSD);
		}
	}

	public void repair() {
		debugPrint("更改标点");
		this.btext.doNotScroll = true;
		QTextCursor tc = text.textCursor();
		int pos = tc.position();
		QTextCursor cursorForPosition = null;
		if (getIntValueFromSettings(appInfo.insByMouseOrKey, "0") == 0) {
			QPoint p = QCursor.pos();
			QPoint mapF = text.mapFromGlobal(p);
			cursorForPosition = text.cursorForPosition(mapF);
		} else
			cursorForPosition = tc;

		if (cursorForPosition != null) {
			cursorForPosition.movePosition(QTextCursor.MoveOperation.NextCharacter);
			cursorForPosition.movePosition(QTextCursor.MoveOperation.NextCharacter, QTextCursor.MoveMode.KeepAnchor);
			if (cursorForPosition.selectedText().equals("，")) {
				aa(cursorForPosition);
			} else if (cursorForPosition.selectedText().equals("。")) {
				bb(cursorForPosition);
			} else {
				cursorForPosition.movePosition(QTextCursor.MoveOperation.PreviousCharacter);
				cursorForPosition.movePosition(QTextCursor.MoveOperation.PreviousCharacter);
				cursorForPosition.movePosition(QTextCursor.MoveOperation.NextCharacter,
						QTextCursor.MoveMode.KeepAnchor);
				if (cursorForPosition.selectedText().equals("，")) {
					aa(cursorForPosition);
				} else if (cursorForPosition.selectedText().equals("。")) {
					bb(cursorForPosition);
				} else {
					cursorForPosition.movePosition(QTextCursor.MoveOperation.PreviousCharacter);
					cursorForPosition.movePosition(QTextCursor.MoveOperation.PreviousCharacter,
							QTextCursor.MoveMode.KeepAnchor);
					if (cursorForPosition.selectedText().equals("，")) {
						aa(cursorForPosition);
					} else if (cursorForPosition.selectedText().equals("。")) {
						bb(cursorForPosition);
					} else {
						if ((this.lastRepairPos == pos) ||

								(this.lastRepairPos == pos - 1)) {
							int result = getMessageBoxWithYesNO("重复修改", "你确定要再次修改吗？", "是", "否",
									QMessageBox.Icon.Question, 0);
							if (result == 0) {
								return;
							}
						}
						char cAt = text.document_b().characterAt(pos);
						if (cAt == 65292) {
							tc.movePosition(QTextCursor.MoveOperation.NextCharacter, QTextCursor.MoveMode.KeepAnchor);
							tc.insertText("。");
							tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter);
							text.setTextCursor(tc);
							this.lastRepairPos = tc.position();
						} else if (cAt == '。') {
							tc.movePosition(QTextCursor.MoveOperation.NextCharacter, QTextCursor.MoveMode.KeepAnchor);
							tc.insertText("，");
							tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter);
							text.setTextCursor(tc);
							this.lastRepairPos = tc.position();
						} else {
							char ccAt = text.document_b().characterAt(pos - 1);
							if (ccAt == 65292) {
								tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter,
										QTextCursor.MoveMode.KeepAnchor);
								this.lastRepairPos = tc.position();
								tc.insertText("。");

								text.setTextCursor(tc);
							} else if (ccAt == '。') {
								tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter,
										QTextCursor.MoveMode.KeepAnchor);
								this.lastRepairPos = tc.position();
								tc.insertText("，");

								text.setTextCursor(tc);
							}
						}
					}
				}
			}
		}
		this.btext.doNotScroll = false;
	}

	void aa(QTextCursor cursorForPosition) {
		if ((this.lastRepairPos == cursorForPosition.position())
				|| (this.lastRepairPos == cursorForPosition.position() + 1)
				|| (this.lastRepairPos == cursorForPosition.position() - 1)) {
			int result = getMessageBoxWithYesNO("重复修改标点", "你确定要再次修改这个标点吗？", "是", "否", QMessageBox.Icon.Question, 0);
			if (result == 1) {
				cursorForPosition.insertText("。");

				text.setTextCursor(cursorForPosition);
			}
		} else {
			cursorForPosition.insertText("。");

			text.setTextCursor(cursorForPosition);
			this.lastRepairPos = cursorForPosition.position();
		}
	}

	void bb(QTextCursor cursorForPosition) {
		if ((this.lastRepairPos == cursorForPosition.position())
				|| (this.lastRepairPos == cursorForPosition.position() + 1)
				|| (this.lastRepairPos == cursorForPosition.position() - 1)) {
			int result = getMessageBoxWithYesNO("重复修改标点", "你确定要再次修改这个标点吗？", "是", "否", QMessageBox.Icon.Question, 0);
			if (result == 1) {
				cursorForPosition.insertText("，");

				text.setTextCursor(cursorForPosition);
			}
		} else {
			cursorForPosition.insertText("，");

			text.setTextCursor(cursorForPosition);
			this.lastRepairPos = cursorForPosition.position();
		}
	}

	public void setBlurRadius() {
	}

	static String pinyinStr(String s) {
		ArrayList<String> list = new ArrayList();
		int start = 0;
		for (int i = 2; i <= s.length(); i += 2) {
			String substring = s.substring(start, i);
			list.add(substring);
			start = i;
		}
		System.out.println(start + " " + s.length());
		if (start != s.length()) {
			list.add(s.substring(start, s.length()));
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String next = (String) it.next();
			if (it.hasNext()) {
				sb.append(next + "'");
			} else {
				sb.append(next);
			}
		}
		return sb.toString();
	}

	void setIsShowKeepWritingMessage() {
		this.doNotShowKeepWrtingMessage = (!this.doNotShowKeepWrtingMessage);
		if (this.doNotShowKeepWrtingMessage) {
			getMessageBox("写作提醒", "已关闭写作提醒");
		} else {
			getMessageBox("写作提醒", "已开启写作提醒");
		}
	}

	void addCommand(blackCommand com) {
		if (com != null) {
			this.commands.add(com);
		}
	}

	isRight isRightIntValue(String value) {
		boolean right = true;
		Object valueOf = null;
		try {
			valueOf = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			right = false;
			getMessageBox("参数错误", "包含非数字字符！");
		}
		return new isRight(right, valueOf);
	}

	void checkCommand(String com, TextRegion checkCommand) {
		boolean ishas = false;
		String[] args = cheakDocument.checkCommandArgs(com);
		if (args.length > 1) {
			if (args[1].equals("h") || args[1].equals("help") || args[1].equals("帮助")) {
				Iterator<blackCommand> it;
				StringBuilder sb = new StringBuilder();
				it = this.commands.iterator();
				while (it.hasNext()) {
					blackCommand next = (blackCommand) it.next();
					if (it.hasNext()) {
						sb.append("<b>" + next.command + "</b><br>" + next.info + "<br>-------<br>");
					} else {
						sb.append("<b>" + next.command + "</b><br>" + next.info);
					}
				}
				getBMessageBox("可用命令", sb.toString());
				return;
			}
		}

		for (blackCommand comm : this.commands) {
			//把整条命令按空格拆分，得到的数组内第1个项目就是命令文本
			ArrayList<String> argss = cheakDocument.checkCommandArgs(checkCommand.text, ' ');
			String commandText = null;
			if(argss.size() >= 1) commandText = argss.get(0);
			else {
				getMessageBox("命令执行错误", "没有输入命令文本");
				return;
			}
			//判断命令文本与当前遍历的命令是否相同，如果不同，跳过此次循环
			if(!commandText.equals(comm.command))continue;
			
			ishas = true;
			
			//如果命令相匹配，则开始提取命令给定的参数
			TextRegion command = cheakDocument.checkCommand(checkCommand.text, " " + comm.command+" ");
			//没有获取到命令给定的参数，如果命令的定义中存在参数，就给出提示，然后跳过余下的循环语句
			if (command == null) {
				if ((!comm.noArgs)) {
					getMessageBox("命令执行警告", "缺少参数！");
					continue;
				}else {
					//如果命令不需要参数，则执行，然后跳出循环
					addToFindHistory(com);
					comm.action(null);
					break;
				}
			}else {
				if ((command.text.indexOf("?") != -1) || (command.text.indexOf("？") != -1)) {
					getMessageBox(comm.command + "命令", comm.info);
				} else {
					String[] array = cheakDocument.checkCommandArgs(command.text);
					if ((!comm.noArgs) && (array.length > 0)) {
						addToFindHistory(com);
						comm.action(array);
					} 
					else if ((comm.noArgs) && (array.length > 0)) {
						getMessageBox("命令执行警告", "多余的参数！");
					} 
					
				}
				break;
			}
			
		}
		if (!ishas) {
			getMessageBox("命令执行警告", "没有该命令！");
		}
	}

	void execCommand(String com) {
		TextRegion checkCommandQ = cheakDocument.checkCommand(com, appInfo.blackCommandAndQuiet);
		if (checkCommandQ != null) {
			this.execCommandAndQuiet = true;
			checkCommand(com, checkCommandQ);
			if ((this.finddialog != null) && (this.finddialog.isVisible())) {
				this.finddialog.hide();
			}
			this.execCommandAndQuiet = false;
		} else {
			this.execCommandAndQuiet = false;
			TextRegion checkCommand = cheakDocument.checkCommand(com, appInfo.blackCommand);
			if (checkCommand != null) {
				checkCommand(com, checkCommand);
			}
		}
	}

	void currentIndexChangedForFindBox() {
		this.findLine.selectAll();
	}

	void clearMoveList() {
		this.moveList.removeAll(this.moveList);
		this.moveIndex = -1;
		updateOSDMessages(1);
		ba.clearMoveList.setText("清空文件跳转记录\t" + moveList.size() + "条记录");
	}

	void readFindHistory() {
		File file = new File(
				this.projectFile.getParent() + File.separator + "Settings" + File.separator + appInfo.FindHistory);
		if (file.exists()) {
			Object readObjFile = io.readObjFile(file);
			if (readObjFile != null) {
				this.findHistory = ((ArrayList) readObjFile);
			} else {
				this.findHistory = new ArrayList();
			}
			synFindHistoryToCombox();
		} else {
			this.findHistory = new ArrayList();
		}
	}

	void saveFindHistory() {
		String path = this.projectFile.getParent() + File.separator + "Settings" + File.separator + appInfo.FindHistory;
		io.writeObjFile(new File(path), this.findHistory);
	}

	void addToFindHistory(String findtext) {
		if (findtext.indexOf("$bc") != 0)
			return;

//		if (this.findHistory.size() >= 50) {
//			boolean isRemove = false;
//			for (int i = 0; i < this.findHistory.size(); i++) {
//				String string = (String) this.findHistory.get(i);
//				if (string.indexOf(appInfo.blackCommand) == -1) {
//					this.findHistory.remove(i);
//					isRemove = true;
//					break;
//				}
//			}
//			if (!isRemove) {
//				getMessageBox("添加检索记录", "无法添加检索记录，因为记录数量已达上限(50)\n-----\n请删除已有的记录后再试！");
//				return;
//			}
//		}

		if (findHistory.size() >= 50)
			findHistory.remove(0);

		for (int i = 0; i < this.findHistory.size(); i++) {
			String string = (String) this.findHistory.get(i);
			if (string.equals(findtext)) {
				this.findHistory.remove(i);
				break;
			}
		}
		this.findHistory.add(findtext);
		synFindHistoryToCombox();
	}

	void addMenuForFindLine(QPoint pos) {
		QMenu qm = new QMenu(text);
		qm.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
		QAction copy = new QAction(qm);
		copy.setText("复制");
		copy.setShortcut("ctrl+c");
		copy.triggered.connect(this.findLine, "copy()");
		qm.addAction(copy);

		QAction cut = new QAction(qm);
		cut.setText("剪切");
		cut.setShortcut("ctrl+x");
		cut.triggered.connect(this.findLine, "cut()");
		qm.addAction(cut);

		QAction paste = new QAction(qm);
		paste.setText("粘贴");
		paste.setShortcut("ctrl+v");
		paste.triggered.connect(this.findLine, "paste()");
		qm.addAction(paste);

		qm.addSeparator();

		QAction selectAll = new QAction(qm);
		selectAll.setText("全选");
		selectAll.setShortcut("ctrl+a");
		selectAll.triggered.connect(this.findLine, "selectAll()");
		qm.addAction(selectAll);

		qm.addSeparator();
		QAction findInCurrentFileOnly = new QAction(qm);
		findInCurrentFileOnly.setText("仅检索当前文档");
		findInCurrentFileOnly.setCheckable(true);
		findInCurrentFileOnly.triggered.connect(this, "findInCurrent()");
		qm.addAction(findInCurrentFileOnly);

		QAction findInAll = new QAction(qm);
		findInAll.setText("检索所有文档");
		findInAll.setCheckable(true);
		findInAll.triggered.connect(this, "findInAll()");
		qm.addAction(findInAll);
		if (getFindIn() == 1) {
			findInCurrentFileOnly.setChecked(true);
		} else {
			findInAll.setChecked(true);
		}
		qm.addSeparator();

		QAction clearHistoryOnlyCurrent = new QAction(qm);
		clearHistoryOnlyCurrent.setText("将当前条目从检索记录中删除");
		clearHistoryOnlyCurrent.triggered.connect(this, "clearFindHistoryOnlyCurrent()");
		qm.addAction(clearHistoryOnlyCurrent);

		QAction clearHistoryWithoutCMD = new QAction(qm);
		clearHistoryWithoutCMD.setText("清除全部检索记录但保留" + appInfo.appName + "命令");
		clearHistoryWithoutCMD.triggered.connect(this, "clearFindHistoryWithoutCMD()");
		qm.addAction(clearHistoryWithoutCMD);

		QAction clearHistory = new QAction(qm);
		clearHistory.setText("清除全部检索记录");
		clearHistory.triggered.connect(this, "clearFindHistory()");
		qm.addAction(clearHistory);

		qm.popup(this.findLine.mapToGlobal(pos));
	}

	void beep() {
		if ((clip != null) && (clip.isActive())) {
			clip.stop();
		} else {
			startBeepSound(-1);
		}
	}

	static void stopBeepSound() {
		if ((clip != null) && (clip.isActive())) {
			clip.stop();
		}
	}

	static void startBeepSound(int looptime) {
		startBeepSound(looptime, 0);
	}

	static void startBeepSound(final int looptime, final int type) {
		if ((quietMode) || ((clip != null) && (clip.isActive()))) {
			return;
		}
		new bRunnable(0, true, true, false, true) {
			public void run() {
				String filename = null;
				if (type == 0) {
					filename = "00.wav";
				} else if (type == 1) {
					filename = "01.wav";
				} else if (type == 2) {
					filename = "02.wav";
				} else if (type == 3) {
					filename = "03.wav";
				} else if (type == 4) {
					filename = "04.wav";
				} else if (type == 5) {
					filename = "05.wav";
				}
				File f = new File("./sound/" + filename);
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
					return;
				}
				if (!f.exists()) {
					return;
				}
				AudioInputStream audioInputStream = null;
				try {
					audioInputStream = AudioSystem.getAudioInputStream(f);
				} catch (UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					clip = AudioSystem.getClip();
				} catch (LineUnavailableException e) {
					p("出现音频系统错误");
				}
				try {
					clip.open(audioInputStream);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (clip == null)
					return;

				clip.loop(looptime);

				clip.addLineListener(new LineListener() {
					public void update(LineEvent event) {
						if (event.getType().equals(LineEvent.Type.START)) {
							black.clipIsStart = true;
						} else if (event.getType().equals(LineEvent.Type.STOP)) {
							clip.stop();
						}
					}
				});
				clip.start();
				while ((!clipIsStart) || (clip != null && clip.isActive())) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException localInterruptedException) {
					}
				}
				clipIsStart = false;
				clip = null;
			}
		};
	}

	void clearFindHistoryWithoutCMD() {
		ArrayList<String> list = new ArrayList();
		for (String s : this.findHistory) {
			if (s.indexOf("$bc") == -1) {
				list.add(s);
			}
		}
		for (String s : list) {
			this.findHistory.remove(s);
		}
		synFindHistoryToCombox();
	}

	void clearFindHistoryOnlyCurrent() {
		this.findHistory.remove(this.findLine.text());
		synFindHistoryToCombox();
	}

	void clearFindHistory() {
		this.findHistory.removeAll(this.findHistory);
		synFindHistoryToCombox();
	}

	int getFindIn() {
		return Integer.valueOf((String) this.settings.value(appInfo.findIn, "1")).intValue();
	}

	void findInAll() {
		this.settings.setValue(appInfo.findIn, String.valueOf(0));
	}

	void findInCurrent() {
		this.settings.setValue(appInfo.findIn, String.valueOf(1));
	}

	void showFindDialogWithoutFindHistory() {
		if (this.finddialog == null) {
			this.finddialog = new finddialog(this);
			this.finddialog.onlyShowHistoryOfBlackCommands = true;
			this.finddialog.show();
		}
	}

	void findLineReturnPressd() {
		String str = this.findLine.text();
		if (str.length() > 0) {
			if (str.indexOf(appInfo.blackCommand) != 0) {
				addToFindHistory(str);
				showFindInfoForFindLine(str, getFindIn());
			} else {
				execCommand(str);
			}
		}
	}

	void setFindLineLocation() {
		this.findBox.setGeometry(width() - 205, 7, 200, 28);
		this.findbutton.setGeometry(this.findBox.width() - this.findBox.height() - 15, 0, this.findBox.height(),
				this.findBox.height());
	}

	public void removeTimer(int id) {
		timerInfo in = null;
		for (timerInfo info : this.timerInfos) {
			if (info.id == id) {
				in = info;
			}
		}
		this.timerInfos.remove(in);
	}

	void link(String s) {
		TextRegion com = cheakDocument.checkCommand(s, "timer");
		if (com != null) {
			removeTimer(Integer.valueOf(com.text).intValue());
		}
	}

	void addMenu(QPoint pos) {
		QMenu qm = new QMenu(this);
		qm.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
		if (writingView > 0) {
			if (ba.checkBingPicIsEnable()) {
				String text = "显示Bing每日图片故事\t双击窗口背景";
				QAction showBingPicInfo = null;
				if (!textWidget.isVisible())
					text = "隐藏Bing每日图片故事\t双击窗口背景";
				showBingPicInfo = bAction.addMenuTo(qm, text, "", ba, "showBingPicInfo()");
				qm.addAction(showBingPicInfo);
			}
			QAction exitWritingView = new QAction(qm);
			exitWritingView.setText("退出写作视图\tEsc");
			exitWritingView.triggered.connect(this, "exitWritingView()");
			qm.addAction(exitWritingView);
		}
		qm.popup(mapToGlobal(pos));
	}

	public void exitWritingView() {
		if (writingView > 0) {
			exitwritingView(Boolean.valueOf(true));
		}
	}

	void autoCheckNetworks() {
		if (this.timerForAutoCheckNetworks != null) {
			this.timerForAutoCheckNetworks.stop();
			this.timerForAutoCheckNetworks = null;
			getMessageBox("自动检查可用网络", "已停止自动检查网络");
		} else {
			final QMessageBox messageBox = getMessageBox("自动检查可用网络", "已开始自动检查网络，间隔5分钟");
			this.timerForAutoCheckNetworks = new bRunnable(10000, true, true, false, false) {
				public void run() {
					StringBuilder checkNetWorksMessage = black.this.checkNetWordksAction(true, this);
					boolean isShowAutoCheckNetworksMessage = false;
					List<String> allLine = cheakDocument.getAllLine(checkNetWorksMessage.toString());
					for (String s : allLine) {
						if (!s.equals("")) {
							if (s.indexOf("没有找到带有WEP的网络") != -1) {
								isShowAutoCheckNetworksMessage = true;
								stop();
								break;
							}
							//
							else if (s.indexOf("已连接") != -1) {
								if (s.indexOf("，但无Internet") == -1) {
									isShowAutoCheckNetworksMessage = true;
									break;
								}
							} else if ((s.indexOf("发现带有WEP关键词的无线网络") == -1) && (s.indexOf("--------") == -1)) {
								isShowAutoCheckNetworksMessage = true;
								break;
							}
						}
					}
					setData("0", Boolean.valueOf(isShowAutoCheckNetworksMessage));
					execInUIThread(new Runnable() {
						public void run() {
							p("检查可用网络：" + Data("0"));
						}
					});
					if (isShowAutoCheckNetworksMessage) {
						setData("1", checkNetWorksMessage.toString());
						execInUIThread(new Runnable() {
							public void run() {
								if (messageBox.isVisible()) {
									messageBox.hide();
								}
								black.this.getMessageBox("发现可用网络", (String) Data("1"), true);
							}
						});
					}
				}
			};
		}
	}

	void editNetWorks() {
		final File file = new File(
				this.projectFile.getParent() + File.separator + "Settings" + File.separator + "networks");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.networkList == null) {
			this.networkList = io.readTextFile(file, "utf-8");
		}
		bmessageBox bmessageBox = new bmessageBox(this, "编辑网络列表", "确定", this.networkList, false) {
			public void buttonPressedAction(String content) {
				if (!content.equals(black.this.networkList)) {
					io.writeTextFile(file, content, "utf-8");
					black.this.networkList = content;
				}
				close();
				dispose();
			}
		};
		bmessageBox.show();
	}

	void checkNetWorks() {
		new bRunnable(0, true, true, false, true) {
			public void run() {
				black.this.checkNetWordksAction(false, this);
			}
		};
	}

	StringBuilder checkNetWordksAction(boolean quiet, bRunnable run) {
		final StringBuilder networkOfShow = new StringBuilder();
		ArrayList<String> networklis = new ArrayList();
		try {
			Process exec = Runtime.getRuntime().exec("netsh wlan show networks mode=bssid");
			StringBuilder sb = readProcessOutput(exec, "gbk");
			String networks = sb.toString();
			File file = new File(
					this.projectFile.getParent() + File.separator + "Settings" + File.separator + "networks");
			if (!file.exists()) {
				if (!quiet) {
					run.execInUIThread(new Runnable() {
						public void run() {
							black.this.getMessageBox("警告", "没有定义网络列表！\n请先通过右键菜单中的“无线网络-编辑网络列表”选项添加网络");
						}
					});
				}
				return networkOfShow;
			}
			if (this.networkList == null) {
				run.execInUIThread(new Runnable() {
					public void run() {
						black.this.p("读取无线网络列表");
					}
				});
				this.networkList = io.readTextFile(file, "utf-8");
			}
			List<String> allLine = cheakDocument.getAllLine(this.networkList);
			Process execc = null;
			try {
				execc = Runtime.getRuntime().exec("netsh wlan show interfaces");
			} catch (IOException e) {
				e.printStackTrace();
			}
			String interfaces = readProcessOutput(execc, "gbk").toString();
			if (networks.indexOf("WEP") != -1) {
				networklis.add("发现带有WEP关键词的无线网络");
				networklis.add("--------");
			} else
				networklis.add("没有找到带有WEP的网络，路由器可能已被重置");

			for (String s : allLine) {
				if (networks.indexOf(s) != -1) {
					if (interfaces.indexOf(s) == -1) {
						networklis.add(s);
					} else {
						int id = -1;
						Process ping = null;
						try {
							id = addTimer(new timerInfo(true, "正在检查远程网络节点", null));
							ping = Runtime.getRuntime().exec("ping www.baidu.com");
						} catch (IOException e) {
							e.printStackTrace();
						}
						StringBuilder pingSb = readProcessOutput(ping, "gbk");
						removeTimer(id);
						if (pingSb.indexOf("ms TTL=") != -1) {
							networklis.add(s + " (已连接)");
						} else {
							networklis.add(s + " (已连接，但无Internet)");
						}
					}
				}
			}
			if (networklis.size() > 0) {
				Iterator<String> it = networklis.iterator();
				while (it.hasNext()) {
					String next = (String) it.next();
					if (it.hasNext()) {
						networkOfShow.append(next + "\n");
					} else {
						networkOfShow.append(next);
					}
				}
			}
			if (!quiet) {
				run.execInUIThread(new Runnable() {
					public void run() {
						black.this.getMessageBox("发现网络", networkOfShow.toString());
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return networkOfShow;
	}

	void showNetWorks() {
		try {
			Process exec = Runtime.getRuntime().exec("netsh wlan show networks mode=bssid");
			StringBuilder sb = readProcessOutput(exec, "gbk");
			getBMessageBox("网络列表", sb.toString()).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void showMoveList() {
		if ((keywords != null) && (keywords.isVisible())) {
			return;
		}
		initKeyWordsDialog();
		keywords.tree.UsedToProjectPanelOrKeywordsList = 1;
		if (keywords.tree.topLevelItemCount() > 0) {
			keywords.tree.clear();
		}
		for (int i = 0; i < this.moveList.size(); i++) {
			String s = (String) this.moveList.get(i);
			QTreeWidgetItem ti = getTreeItem(keywords.tree);
			dataForOpenedFile findInOpenedFiles = findInOpenedFiles(s);
			ti.setText(0, getCharByNumber(keywords.tree.topLevelItemCount() - 1) + findFileShowName(s) + " (" + i + "/"
					+ (this.moveList.size() - 1) + ")");
			if (findInOpenedFiles != null) {
				ti.setData(3, 3, "已打开");
			}
			ti.setData(1, 0, new TextRegion(s, 0, 0));
			if (s.equals(this.currentEditing.getName())) {
				QFont font = ti.font(0);
				font.setBold(true);
				font.setItalic(true);
				ti.setFont(0, font);
				keywords.tree.setCurrentItem(ti);
			}
		}
		findview = 4;
		keywords.message.setText("文件跳转记录(" + keywords.tree.topLevelItemCount() + "条)");
		keywords.statusbar.showMessage("按Enter键跳转到所选文件");
		keywords.noDefaultSelection = true;

		showDialogAtCenter(keywords);
	}

	String getMemoryAndRunTime() {
		String appUsedMemory = getAppUsedMemory();
		float F_appUsedMemory = Float.valueOf(appUsedMemory).floatValue();
		int task;

		if (F_appUsedMemory < 100.0F) {
			task = (int) (F_appUsedMemory - 32.0F);
		} else {
			task = (int) (F_appUsedMemory - 42.0F);
		}
		appRunTime = System.currentTimeMillis() - appStartTime;
		String message = "占用内存(专用工作集)：" + appUsedMemory + " MB\n" + "占用内存(任务管理器)：" + task + " MB\n--------\n"
				+ "程序已持续运行：" + time.msToTime(appRunTime);
		return message;
	}

	public void getSizeOfEditorMemory() {
		QMessageBox messageBox = getMessageBox("性能", getMemoryAndRunTime());

		timerInfo timerInfo = new timerInfo(true, "获取性能数据", null);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				uiRun(black.this, new Runnable() {
					public void run() {
						if (messageBox.isVisible())
							messageBox.setText(getMemoryAndRunTime());
						else {
							removeTimer(timerInfo.id);
						}
					}
				});
			}
		};
		timerInfo.setRunnable(runnable);
		addTimer(timerInfo);
	}

	public String getAppUsedMemory() {
		String lastline = null;
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];
		try {
			Process exec = Runtime.getRuntime().exec("tasklist -fi \"PID eq " + pid + "\" -FO LIST");
			List<String> allLine = cheakDocument.getAllLine(readProcessOutput(exec, "gbk").toString());
			if (allLine.size() == 0) {
				lastline = "";
			} else {
				String str = (String) allLine.get(allLine.size() - 1);
				int index = str.indexOf(':');
				String substring = str.substring(index + 1, str.length() - 1);
				StringBuilder sb = new StringBuilder();
				char[] arrayOfChar;
				int j = (arrayOfChar = substring.toCharArray()).length;
				for (int i = 0; i < j; i++) {
					char c = arrayOfChar[i];
					if ((c >= '0') && (c <= '9')) {
						sb.append(c);
					}
				}
				String f = String.valueOf(Float.valueOf(sb.toString()).floatValue() / 1024.0F);
				int indexof = -1;
				if ((indexof = f.indexOf('.')) != -1) {
					f = f.substring(0, indexof + 2);
				}
				lastline = f;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lastline;
	}

	static StringBuilder readProcessOutput(Process process, String encode) {
		StringBuilder sb = new StringBuilder();
		sb.append(read(process.getInputStream(), null, encode));
		sb.append(read(process.getErrorStream(), null, encode));
		return sb;
	}

	// 读取输入流
	static StringBuilder read(InputStream inputStream, PrintStream out, String encode) {
		StringBuilder sb = new StringBuilder();
		try {
			String enco = "gbk";
			if (encode != null)
				enco = encode;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, enco));

			String line;
			while ((line = reader.readLine()) != null) {
				if (out != null)
					out.println(line);
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;
	}

	/**
	 * ping百度网，如果没有ping通，此操作将会一直阻塞线程，直到超过给定的时间
	 * 
	 * @param ms超时返回的时间
	 * @return
	 */
	static boolean ping(long ms) {
		int outtime = 0;
		while (true) {
			try {
				Process exec = Runtime.getRuntime().exec("ping www.baidu.com");
				StringBuilder ping = black.readProcessOutput(exec, "gbk");
				if (ping.indexOf("ms TTL=") != -1)
					return true;
				else {
					if (outtime >= ms)
						return false;

					try {
						Thread.sleep(500);
						outtime += 500;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void checkMail() {
		uiRun(this, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (adminPasswd == null) {
					Object value = settings.value(appInfo.additionalInfo);
					if (value == null) {
						getMessageBox("错误", "尚未设定管理员加密密码！");
						return;
					}
					DesUtils des = new DesUtils("black"); // 自定义密钥
					try {
						adminPasswd = des.decrypt(value.toString());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (adminPasswd == null)
					return;

				File f = new File(appInfo.mailDat);
				if (mailDataIsChanged) {
					io.writeObjFile(f, mailData);
					mailDataIsChanged = false;
				}

				try {
					if (!checkRunAsTool() || !checkDevcon())
						return;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				File flag0 = new File("./0");
				if (flag0.exists())
					flag0.delete();
				File flag1 = new File("./1");
				if (flag1.exists())
					flag1.delete();
				File flag2 = new File("./2");
				if (flag2.exists())
					flag2.delete();

				try {
					p("正在检查邮件...");
					Runtime.getRuntime()
							.exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:" + adminPasswd
									+ " /domain: /command:\"" + appInfo.javawPath
									+ " -jar .\\bin_\\getMail.jar work\" /runpath:" + new File("").getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (!flag0.exists() && !flag1.exists() && !flag2.exists()) {
//					System.out.println("等待文件就绪");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				uiRun(black.this, new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (img != null)
							img.updateImgAndText();
						;
					}
				});

				if (flag0.exists()) {
					flag0.delete();
					p("没有收到新邮件，因为没有与邮件服务器连接");
					return;
				} else if (flag1.exists()) {
					flag1.delete();
					lastCheckMailDate = time.getCurrentDate(" ");
					p("收到了新邮件");
				} else if (flag2.exists()) {
					flag2.delete();
					lastCheckMailDate = time.getCurrentDate(" ");
					p("没有收到新邮件，但已成功与邮件服务器连接");
					return;
				}

				mailData = (ArrayList) io.readObjFile(f);
				timerInfo timerInfo = getTimerInfo(checkMailTimerID);
				int i = 0;
				for (TextRegion tr : mailData) {
					if (tr.end != -99)
						i++;
				}
				if (i > 0) {
					String mess = "<font style=\"font-size:13pt;color:blue\">有" + i + "封邮件</font>";
					if (timerInfo == null) {
						timerInfo ti = new timerInfo(true, mess, null);
						ti.type = -999;
						ti.showProgress = false;
						checkMailTimerID = addTimer(ti);
					} else {
						timerInfo.timerName = mess;
					}
//					speakText(mess, 0);
					int infoCount = 0;
					removeAllTimer(-998);
					for (TextRegion tr : mailData) {
						if (tr.end != -99) {
							infoCount++;
							timerInfo ti = new timerInfo(true, tr.filename, null);
							ti.showProgress = false;
							ti.type = -998;
							ti.setData(tr.start);
							addTimer(ti);
						}
					}
					if (infoCount > 0)
						new bRunnable(300000, true, false, false, true) {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								removeAllTimer(-998);
							}
						};

				} else {
					removeTimer(checkMailTimerID);
					checkMailTimerID = -1;
				}
			}
		}).start();
	}

//	void checkMail() {
//		Object value = settings.value(appInfo.additionalInfo);
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//				devconTools dt = new devconTools("./Tools/devcon.exe");
//				boolean devIsEnable = dt.devIsEnable("PCI\\VEN_168C&DEV_002B&CC_0280");
//				boolean devIsRuning = devIsEnable;
//
//				if (!devIsEnable) {
//					if (dt.enableDev("PCI\\VEN_168C&DEV_002B&CC_0280"))
//						devIsRuning = true;
//				}
//				if (devIsRuning) {
//					try {
//						if (ping(60000)) {
//							String host = "pop.163.com";
//							String username = "yangisboy@163.com";
//							String password = "dxy13633528994";
//							int count = 0;
//							ArrayList<TextRegion> mail_old = null;
//							if(new File(appInfo.mailDat).exists()) {
//								mail_old = (ArrayList<TextRegion>) io.readObjFile(new File(appInfo.mailDat));
//							}
//							ArrayList<TextRegion> al = new ArrayList();
//							Folder folder_INBOX = mailTool.getFolder_INBOX(username, password, host);
//							Message[] messages = folder_INBOX.getMessages();
//							
//							for (int i = 0; i < messages.length; i++) {
//								if(mail_old != null)
//								for(TextRegion trr:mail_old) {
//									if(i == trr.start) {
//										if(trr.end == -99) {
//											messages[i].setFlag(Flag.DELETED, true);
//											continue;
//										}
//									}
//								}
//								String subject = messages[i].getSubject();
//								if (subject.indexOf("black") == -1) {
//									count++;
//									String contentType = messages[i].getContentType();
//									StringBuilder sb = new StringBuilder();
//									if (contentType.startsWith("text/plain")) {
//										ReceiveMail.getMailTextContent(messages[i], sb, true);
//									} else
//										ReceiveMail.getMailTextContent(messages[i], sb, false);
//									TextRegion tr = new TextRegion(sb.toString(), i, -1);
//									tr.filename = subject;
//									al.add(tr);
//								}
//							}
//							timerInfo timerInfo = getTimerInfo(checkMailTimerID);
//							if(al.size()>0) {
//								String mess = "有"+al.size()+"封邮件";
//								if(timerInfo == null) {
//									timerInfo ti = new timerInfo(true, mess, null);
//									ti.type = -999;
//									ti.showProgress = false;
//									checkMailTimerID = addTimer(ti);
//								}else {
//									timerInfo.timerName = mess;
//								}
//								
//								speakText(mess, 0);
//							}else {
//								removeTimer(checkMailTimerID);
//								checkMailTimerID = -1;
//							}
//							
//							io.writeObjFile(new File(appInfo.mailDat), al);
//							folder_INBOX.close(true);
//						}
//					} catch (Exception e) {
////						getMessageBox("收取邮件", e.getMessage());
//					}
//
//				}
//				if (!devIsEnable) {
//					dt.disableDev("PCI\\VEN_168C&DEV_002B&CC_0280");
//				}
//			}
//		}).start();
//	}

	void setTimeMessage(String message) {
		if(message == null) {
			if(dbm)
			getMessageBox("", "发生了空指针异常");
			return;
		}
		if (brunnableForOSD == null) {
			timeMessage.hide();
			return;
		}

		if (!message.equals("")) {
//			QPalette palette = this.timeMessage.palette();
//			palette.setBrush(QPalette.ColorRole.WindowText, new QBrush(new QColor(Qt.GlobalColor.darkGreen)));
//			this.timeMessage.setPalette(palette);
			this.timeMessage.setText(message);
			this.timeMessage.setVisible(true);
			timeMessage.raise();
			setTimeMessageLabelLocation();
		} else {
			this.timeMessage.setVisible(false);
		}
	}

	void setLoadFileMessage_DoNotHide(String message) {
		this.doNotHideLoadFileMessage = true;
		setLoadFileMessage(message);
	}

	void hideLoadFileMessage() {
		this.doNotHideLoadFileMessage = false;
	}

	public void setLoadFileMessage(String message) {
		this.loadFileMessage.setText(message);
		setLoadFileMessageLabelLocation();
		if (!this.loadFileMessage.isVisible()) {
			this.loadFileMessage.show();
		}
		showTime();
	}

	public void setLoadFileMessageLabelLocation() {
//		if(keywords != null && keywords.isVisible()) loadFileMessage.setParent(keywords);
//		else loadFileMessage.setParent(this);
		this.loadFileMessage.adjustSize();
		this.loadFileMessage.setGeometry((width() - this.loadFileMessage.width()) / 2,
				(height() - this.loadFileMessage.height()) / 2, this.loadFileMessage.width(),
				this.loadFileMessage.height());
	}

	public void setTimeMessageLabelLocation() {
		if (text == null) {
			return;
		}
		setLoadFileMessageLabelLocation();
		timeMessage.setFixedWidth(width() / 2);
		this.timeMessage.adjustSize();
		QPoint mapTo = this.ui.centralwidget.mapTo(this, textWidget.pos());
		if (writingView == 0) {
			int x = mapTo.x() + textWidget.width() - this.timeMessage.width();
			x -= 17;
			setAnimation(this.timeMessage, "geometry", this.timeMessage.geometry(),
					new QRect(x - 10, mapTo.y() + textWidget.height() - this.timeMessage.height() - 10,
							this.timeMessage.width(), this.timeMessage.height()),
					getAnimationTime());
		} else if (writingView == 1) {
			setAnimation(this.timeMessage, "geometry", this.timeMessage.geometry(),
					new QRect(width() - this.timeMessage.width() - 20, height() - this.timeMessage.height() - 10,
							this.timeMessage.width(), this.timeMessage.height()),
					getAnimationTime());
		} else {
			setAnimation(this.timeMessage, "geometry", this.timeMessage.geometry(),
					new QRect(width() - this.timeMessage.width() - 30, mapTo.y() + 40, this.timeMessage.width(),
							this.timeMessage.height()),
					getAnimationTime());
		}
	}

	public void setAnimation(final QWidget widget, final String key, Object startValue, final Object endValue,
			int time) {
		new animation(widget, key, startValue, endValue, time) {

			@Override
			public void action_endAnimation() {
				// TODO Auto-generated method stub
				
			}

		};
	}

	public void showTime() {
		if (brunnableForOSD == null)
			this.brunnableForOSD = new bRunnable(1000, true, true, false, false) {
				public void run() {
					clearData();
					black.this.synTimeAction(this);
				}
			};
	}

	void updateOSDMessages(int what) {
		switch (what) {
		case -1:
			this.showNameOfCurrentEditingFile = getShowNameOfCurrentEditingFile();

			this.previousOpenedFile[0] = previousOpenedFile_();
			if (this.previousOpenedFile[0] != null) {
				this.previousOpenedFile[1] = findFileShowName(this.previousOpenedFile[0]);
				this.textTitle.ui.prev.setEnabled(true);
			} else {
				this.textTitle.ui.prev.setEnabled(false);
			}
			this.nextOpenedFile[0] = nextOpenedFile_();
			if (this.nextOpenedFile[0] != null) {
				this.nextOpenedFile[1] = findFileShowName(this.nextOpenedFile[0]);
				this.textTitle.ui.next.setEnabled(true);
			} else {
				this.textTitle.ui.next.setEnabled(false);
			}
			this.plusCharCount = (this.charCountWithoutEnter - this.btext.charCountAtToday);

			break;
		case 0:
			this.showNameOfCurrentEditingFile = getShowNameOfCurrentEditingFile();
			break;
		case 1:
			this.previousOpenedFile[0] = previousOpenedFile_();
			if (this.previousOpenedFile[0] != null) {
				this.previousOpenedFile[1] = findFileShowName(this.previousOpenedFile[0]);
				this.textTitle.ui.prev.setEnabled(true);
			} else {
				this.textTitle.ui.prev.setEnabled(false);
			}
			this.nextOpenedFile[0] = nextOpenedFile_();
			if (this.nextOpenedFile[0] != null) {
				this.nextOpenedFile[1] = findFileShowName(this.nextOpenedFile[0]);
				this.textTitle.ui.next.setEnabled(true);
			} else {
				this.textTitle.ui.next.setEnabled(false);
			}
			break;
		case 2:
			this.plusCharCount = (this.charCountWithoutEnter - this.btext.charCountAtToday);
			uiRun(this, new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					statusBar().showMessage(plusCharCount + " 个字");
				}
			});
		}
	}

	public void synTime() {
		new bRunnable(0, true, true, false, true) {
			public void run() {
				black.this.synTimeAction(this);
			}
		};
	}

	static boolean checkIsRunningOnUDiskOS() {
		String system = System.getenv("SystemDrive");
		Float size = new File(system).getTotalSpace() / 1024F / 1024F / 1024F;
//	    System.out.println(size);
		if (size == 20.002926F)
			return true;
		return false;
	}

	void turnOffMonitor() {
		try {
			Runtime.getRuntime().exec("./Tools/nircmdc.exe monitor off");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monitorOff = true;
	}

	void dontNotSetMonitor() {
		dontNotSetMonitor = !dontNotSetMonitor;
		setLoadFileMessage("禁止自动灭屏: " + dontNotSetMonitor);
	}

	void synTimeAction(final bRunnable run) {
		if(run == null) {
			showTime();
			return;
		}
		long currentTimeMillis = System.currentTimeMillis();
		Calendar instance = Calendar.getInstance();
		hourOfDay = instance.get(11);
		minute = instance.get(Calendar.MINUTE);
		second = instance.get(Calendar.SECOND);

		if (!dontNotSetMonitor && !monitorOff && currentTimeMillis - activeTime > 2000) {
			turnOffMonitor();
		}

//		if (k != null) {
//			if (hourOfDay < 22 && hourOfDay != 12) {
//				run.execInUIThread(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						k.closeKiwix();
//					}
//				});
//			}
//		}

		if (btext != null && btext.editTime > 0)
			if (!time.getDate(currentTimeMillis).equals(time.getDate(btext.lastTextChangeTime))) {
				clearCharCountOfTodayInput();
			}
		if (hideTime != -1) {
			int time = (int) ((currentTimeMillis - hideTime) / 1000);
//			System.out.println(time);
			if (time > 5) {
				run.execInUIThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						show();
						getMessageBox("恢复显示", appInfo.appName + "已自动强制显示窗口");
					}
				});
			}
		}

		if (backupTime != 0) {

			try {
				Long value = currentTimeMillis - lastBackUpTime;
				if (value > backupTime) {
					backupFromSynTime = true;
					backup();
					backupFromSynTime = false;
				} else
					removeAllTimer(123456);
			} catch (NullPointerException e) {
				p("出现了空指针异常");
			}
			;
		} else
			removeAllTimer(123456);

//		appRunTime = System.currentTimeMillis() - appStartTime;
//		java.sql.Time timet = new java.sql.Time(appRunTime);
//		
//		System.out.println(timet.getDay()+"-"+timet.getHours()+"-"+timet.getMinutes()+"-"+timet.getSeconds());
		if (textChanged) {
			synTextChange();
		}

		run.execInUIThread(new Runnable() {
			public void run() {
				if ((black.this.btext == null) || (!black.text.isVisible())) {
					run.setData("0", Boolean.valueOf(false));
					black.this.timeMessage.hide();
				}
			}
		});
		if ((run.Data("0") != null) && (!((Boolean) run.Data("0")).booleanValue())) {
			return;
		}
		String messageText = "";
		String moveInfo = "";
		if ((!this.timeMode.equals("-1")) && (!this.timeMode.equals("00"))) {
			if (writingView > 0) {
				if (this.saved) {
					messageText = "<p style=\"font-family: 微软雅黑, serif; font-size:25pt; font-style:bold;\">"
							+ this.showNameOfCurrentEditingFile + "</p>";
				} else {
					messageText = "<p style=\"font-family: 微软雅黑, serif; font-size:25pt; font-style:bold; font-style:italic;\">"
							+ this.showNameOfCurrentEditingFile + " </p>";
				}
			}
			if (this.moveList.size() > 0) {
				moveInfo = "<p style=\"font-family: 微软雅黑, serif; font-size:9pt;\">" + this.moveIndex + "/"
						+ (this.moveList.size() - 1);
				if (this.previousOpenedFile[0] != null) {
					moveInfo = moveInfo + "<br>" + this.previousOpenedFile[1] + "▲";
				}
				if (this.nextOpenedFile[0] != null) {
					moveInfo = moveInfo + "<br>" + this.nextOpenedFile[1] + "▼";
				}
				moveInfo = moveInfo + "</p>";
			}
			timeBeep();
		}
		ArrayList<String> mess = new ArrayList();
		ArrayList<timerInfo> remove = new ArrayList();
		timerInfo info;
		for (int i = 0; i < this.timerInfos.size(); i++) {
			info = (timerInfo) this.timerInfos.get(i);
			if (info.stilLive) {
				if (info.showProgress) {
					if (info.progressMessage.length() < 6) {
						info.progressMessage = (". " + info.progressMessage);
					} else {
						info.progressMessage = "";
					}
					mess.add(info.progressMessage + info.timerName);
				} else
					mess.add(info.timerName);

				if (info.run != null)
					info.run.run();
			} else {
				long time_out = info.startTime + info.time;
				long time_current = time.getCurrentTime_long();
				if (time_current >= time_out) {
					remove.add(info);
					if (info.run != null)
						info.run.run();
				} else if ((info.type != 0) || ((!this.timeMode.equals("-1")) && (!this.timeMode.equals("00")))) {
					long leftTime_ms = time_out - time_current;
//					long leftTime = (time_out - time_current) / 1000L;
					if (!info.doNotShowTip) {
						if (info.canExit) {
							if (!info.hideLeftTime)
								mess.add(info.timerName + ": " + "<a href=timer" + info.id + ">"
										+ time.msToTime(leftTime_ms) + "</a>");
							else
								mess.add("<a href=timer" + info.id + ">" + info.timerName + "</a>");
						} else {
							if (!info.hideLeftTime)
								mess.add(info.timerName + ": " + time.msToTime(leftTime_ms));
							else
								mess.add(info.timerName);
						}
					}
				}
			}

		}
		for (timerInfo in : remove) {
			this.timerInfos.remove(in);
		}
		String timerMessage = "";
		Iterator<String> it = mess.iterator();
		while (it.hasNext()) {
			String next = (String) it.next();
			if (it.hasNext()) {
				timerMessage = timerMessage + next + "<br>";
			} else {
				timerMessage = timerMessage + next;
			}
		}
		if (mess.size() > 0) {
			timerMessage = "<p style=\"font-family: 微软雅黑, serif; font-size:9pt;\">" + timerMessage + "</p>";
		}
		if (this.timeMode.equals("00")) {
			messageText = "";
			if ((hourOfDay > 10) && (hourOfDay < 14)) {
				timeBeep();
			} else if ((hourOfDay > 18) || (hourOfDay < 6)) {
				timeBeep();
			}
		} else if (this.timeMode.equals("0")) {
			String hourOfDay = String.valueOf(Calendar.getInstance().get(11));
			messageText = messageText + hourOfDay + moveInfo;
		} else if (this.timeMode.equals("01")) {
			String hourOfDay = String.valueOf(Calendar.getInstance().get(11));
			messageText = messageText + hourOfDay + "<br>" + this.plusCharCount + moveInfo;
		} else if (this.timeMode.equals("10")) {
			messageText = messageText + time.getCurrentTime() + moveInfo;
		} else if (this.timeMode.equals("11")) {
			messageText = messageText + time.getCurrentTime() + "<br>" + this.plusCharCount + moveInfo;
		} else {
			if ((hourOfDay > 10) && (hourOfDay < 14)) {
				messageText = hourOfDay + "";
				timeBeep();
			} else if ((hourOfDay > 18) || (hourOfDay < 6)) {
				messageText = hourOfDay + "";
				timeBeep();
			} else {
				messageText = "";
			}
		}
		if (timerMessage.length() > 0) {
			messageText = messageText + timerMessage;
		}
		run.setData("1", messageText);
		run.execInUIThread(new Runnable() {
			public void run() {
				black.this.setTimeMessage((String) run.Data("1"));
			}
		});
		if (!this.doNotHideLoadFileMessage) {
			run.execInUIThread(new Runnable() {
				public void run() {
					if (black.this.loadFileMessage.isVisible()) {
						run.setData("2", Boolean.valueOf(true));
					}
				}
			});
		}
		if ((run.Data("2") != null) && (((Boolean) run.Data("2")).booleanValue())) {
			long current = System.currentTimeMillis();
			long loadFileLableShowTime = current - this.loadFileTime_end;
			if (loadFileLableShowTime > 1000L) {
				run.execInUIThread(new Runnable() {
					public void run() {
						black.this.loadFileMessage.hide();
						loadFileTime_end = -1;
					}
				});
			}
		}
		bAction.secDo(this);

		// 检查是否可以停止时间守护线程运行，如果条件允许则停止线程
		if (timerInfos.size() == 0 && backupTime == 0 && timeMode.equals("00") && loadFileTime_end == -1) {
			if(brunnableForOSD != null)
				brunnableForOSD.stop();
			brunnableForOSD = null;
		}
	}

	String getDate() {
		Calendar instance = Calendar.getInstance(Locale.ENGLISH);
		String time = "";
		time = time + instance.getDisplayName(7, 2, Locale.ENGLISH);
		time = time + ", " + instance.getDisplayName(2, 2, Locale.ENGLISH);
		return time;
	}

	void timeBeep() {
		new Thread(new Runnable() {
			public void run() {
				String timeHasSecond = time.getCurrentTimeHasSecond();
				String substring = timeHasSecond.substring(2, timeHasSecond.length());
				if (substring.equals(":00:00")) {
					if (hourOfDay == 11)
						speakText(hourOfDay + "点整，你应该去吃午餐了", -1);
					else if (hourOfDay == 12)
						speakText(hourOfDay + "点整，你在吃午餐吗？", -1);
					else if (hourOfDay == 19)
						speakText(hourOfDay + "点整，你应该去吃晚餐了", -1);
					else if (hourOfDay == 20)
						speakText(hourOfDay + "点整，你在吃晚餐吗？", -1);

					black.startBeepSound(0, 2);
				} else if (substring.equals(":30:00")) {
					black.startBeepSound(0, 1);
				}
			}
		})

				.start();
	}

	public int addTimer(timerInfo info) {
		showTime();

		if (info.isNew) {
			for (timerInfo ti : timerInfos) {
				if (ti.type == info.type) {
					uiRun(this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getMessageBox("timerInfo类型重复", "timerInfo[" + info.timerName + "]的type值"
									+ "与以下timerInfo重复：\n" + ti.timerName + "\n\n请修改type值后重试");
						}
					});
					break;
				}
			}
		}
		info.setID(this.timerId);
		this.timerInfos.add(info);
		this.timerId += 1;
		return info.id;
	}

	public void initEditor() {
		text = new bTextEdit(this);
		text.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
		setTextVScrollBarLocation();
//		text.setContentsMargins(0, 0, 0, 50);
		text.setUndoRedoEnabled(false);
		// 如果启用下面这一行，从普通视图启动后，编辑器的滚动条没反应
//		text.verticalScrollBar().setParent(textWidget);
		textLayout.addWidget(text);
		text.lower();
//		this.ui.verticalLayout.addWidget(text);
		this.btext = new blacktext(text, this);
		applyColorChange();
		if (appInfo.miniMode.equals("1"))
			text.setAccicesable(false, false);
		new setMenus(this);
	}

	public void deleteEditor() {
		if ((keywords != null) && (keywords.isVisible())) {
			keywords.hide();
		}

		textLayout.removeWidget(text);
		text.setVisible(false);
		text.verticalScrollBar().hide();
		text = null;
		this.btext = null;
		update();
		new setMenus(this);
	}

	public String getProjectName() {
		String name = "";
		String projectname = this.projectInfo.getProperty(appInfo.projectName, "");
		if (projectname == null || projectname.isEmpty()) {
			String pn = projectFile.getName();
			int last = pn.lastIndexOf(".");
			name = pn.substring(0, last);
		} else
			name = projectname;
		return name;
	}

	public String getVersion() {
		return appInfo.appVersion;
	}

	void showAllWidget() {
		new setMenus(this);
		if (writingView == 0) {
			if (getBooleanValueFromSettings(appInfo.noDock, "false")) {
				if (this.menubar != null) {
//					if(isAdmin() || projectInfo.keySet().size() == 0)
					this.menubar.show();
				}
				if (this.toolbar != null) {
					this.toolbar.show();
				}
				if (this.dock != null) {
					this.dock.show();
				}
			} else {
				this.ui.hideFilePanel.setChecked(true);
				if (dock.isVisible()) {
					dock.hide();
					menubar.hide();
					toolbar.hide();
				}
			}
			if (this.textTitle != null) {
				this.textTitle.show();
			}
			if (textWidget != null)
				textWidget.show();
			if (text != null) {
				text.show();
				text.verticalScrollBar().show();
			}
			if (statusBar() != null) {
				statusBar().show();
			}
		} else {
			if (text != null) {
				text.show();
			}
			if (textWidget != null)
				textWidget.show();

		}
	}

	void setTextMargins() {
		debugPrint("重新读取Margin数据并应用");
		int scrollbarWidth = 0;
		if (text != null && text.verticalScrollBar() != null) {
			scrollbarWidth = text.verticalScrollBar().width();
			debugPrint("编辑器滚动条宽度：" + text.verticalScrollBar().width());
		}
		if (writingView == 0) {
			if (!getBooleanValueFromSettings(appInfo.noDock, "false"))
				textLayout.setContentsMargins(25, 0, 25 + scrollbarWidth, 5);
			else
				textLayout.setContentsMargins(0, 0, scrollbarWidth, 5);
		} else {
			textLayout.setContentsMargins(getDocumentMargin(), 0, getDocumentMargin(), 5);
		}
	}

	public void hideFilesTree() {
		if (writingView > 0)
			return;

		boolean visible = !this.ui.hideFilePanel.isChecked();
		this.dock.setVisible(visible);

		menubar.setVisible(visible);
//		textTitle.setVisible(visible);

		this.toolbar.setVisible(visible);
//		statusBar().setVisible(visible);

		settings.setValue(appInfo.noDock, visible);
		setTextMargins();
	}

	public void clearLogs() {
		this.logsmessage = new StringBuilder();
		logsBox.setText("");
	}

	public String previousOpenedFile_() {
		if (this.moveList.size() == 0) {
			return null;
		}
		String moveInfo = null;
		int moveindex = this.moveIndex;
		String moveinfo = null;
		String findFileShowName;
		do {
			while (moveindex == -1) {
				moveindex = this.moveList.size() - 1;
				moveinfo = (String) this.moveList.get(moveindex);
				if (!findFileShowName(moveinfo).equals(getShowNameOfCurrentEditingFile())) {
					moveInfo = moveinfo;
					return moveInfo;
				}
			}
			if (moveindex - 1 < 0) {
				break;
			}
			moveindex--;
			if (moveindex < 0 || moveindex >= moveList.size())
				return moveInfo;
			moveinfo = (String) this.moveList.get(moveindex);
			findFileShowName = findFileShowName(moveinfo);
			if (findFileShowName == null) {
				this.moveList.remove(moveindex);
				if (moveindex == this.moveList.size()) {
					moveindex = this.moveList.size() - 1;
				}
			}
		} while ((findFileShowName == null) || (findFileShowName(moveinfo).equals(getShowNameOfCurrentEditingFile())));
		moveInfo = moveinfo;

		return moveInfo;
	}

	public String nextOpenedFile_() {
		if (this.moveList.size() == 0) {
			return null;
		}
		String moveInfo = null;
		int moveindex = this.moveIndex;
		String moveinfo = null;
		String findFileShowName;
		do {
			while (moveindex == -1) {
				moveindex = this.moveList.size() - 1;
				moveinfo = (String) this.moveList.get(moveindex);
				if (!findFileShowName(moveinfo).equals(getShowNameOfCurrentEditingFile())) {
					moveInfo = moveinfo;
					return moveInfo;
				}
			}
			if (moveindex + 1 >= this.moveList.size()) {
				break;
			}
			moveindex++;
			moveinfo = (String) this.moveList.get(moveindex);
			findFileShowName = findFileShowName(moveinfo);
			if (findFileShowName == null) {
				this.moveList.remove(moveindex);
				if (moveindex == this.moveList.size()) {
					moveindex = this.moveList.size() - 1;
				}
			}
		} while ((findFileShowName == null) || (findFileShowName.equals(getShowNameOfCurrentEditingFile())));
		moveInfo = moveinfo;

		return moveInfo;
	}

	public String previousOpenedFile() {
		if (this.moveList.size() == 0) {
			return null;
		}
		String moveInfo = null;
		if (this.moveIndex == -1) {
			moveInfo = (String) this.moveList.get(this.moveList.size() - 1);
		} else if (this.moveIndex - 1 >= 0) {
			moveInfo = (String) this.moveList.get(this.moveIndex - 1);
		}
		return moveInfo;
	}

	public void moveToPreviousOpenedFile() {
		if (this.moveList.size() == 0) {
			return;
		}
		this.doNotAddToMoveList = true;
		String moveInfo = null;
		if (this.moveIndex == -1) {
			this.moveIndex = (this.moveList.size() - 1);
			moveInfo = (String) this.moveList.get(this.moveIndex);
		} else if (this.moveIndex - 1 >= 0) {
			this.moveIndex -= 1;
			moveInfo = (String) this.moveList.get(this.moveIndex);
		}
		if (moveInfo == null) {
			return;
		}
		if (this.currentEditing != null) {
			if (!moveInfo.equals(this.currentEditing.getName())) {
				changedFileOfCurrentEdit();
				openFileByTreeItem(findTreeItemByFileName(moveInfo, null));
			} else {
				moveToPreviousOpenedFile();
			}
		}
		synTime();
	}

	public String nextOpenedFile() {
		if (this.moveList.size() == 0) {
			return null;
		}
		String moveInfo = null;
		if (this.moveIndex == -1) {
			moveInfo = (String) this.moveList.get(this.moveList.size() - 1);
		} else if (this.moveIndex + 1 < this.moveList.size()) {
			moveInfo = (String) this.moveList.get(this.moveIndex + 1);
		}
		return moveInfo;
	}

	public void moveToNextOpenedFile() {
		if (this.moveList.size() == 0) {
			return;
		}
		this.doNotAddToMoveList = true;
		String moveInfo = null;
		if (this.moveIndex == -1) {
			this.moveIndex = (this.moveList.size() - 1);
			moveInfo = (String) this.moveList.get(this.moveIndex);
		} else if (this.moveIndex + 1 < this.moveList.size()) {
			this.moveIndex += 1;
			moveInfo = (String) this.moveList.get(this.moveIndex);
		}
		if (moveInfo == null) {
			return;
		}
		if (this.currentEditing != null) {
			if (!moveInfo.equals(this.currentEditing.getName())) {
				changedFileOfCurrentEdit();
				openFileByTreeItem(findTreeItemByFileName(moveInfo, null));
			} else {
				moveToNextOpenedFile();
			}
		}
		synTime();
	}

	public void sendReportInfo(String title, StringBuilder infos) {
		List<String> recipients = new ArrayList();
		recipients.add("aliceasmud@gmail.com");
		String subject = title;
		String content = infos.toString();
		List<String> attachmentNames = new ArrayList();
		SendMailBySSL sendMailBySSL = new SendMailBySSL("smtp.163.com", "465", "yangisboy@163.com", "dxy13633528994",
				recipients, subject, content, attachmentNames);
		if (sendMailBySSL.sendMail()) {
			this.logsmessage.append("信息投递成功");
		}
	}

	public String getBasedInfo() {
		return appInfo.based;
	}

	public void addMoveInfoOnEnd(String filename) {
		this.addToMoveListEnd = false;
		this.moveIndex = (this.moveList.size() - 1);
		addMoveInfo(filename);
	}

	public void addMoveInfo(String filename) {
		if (this.doNotAddToMoveList) {
			this.doNotAddToMoveList = false;
			return;
		}
		ba.clearMoveList.setText("清空文件跳转记录\t" + (moveList.size() - 1) + "条记录");

		for (int i = 0; i < this.moveList.size(); i++) {
			String moveinfo = (String) this.moveList.get(i);
			if (moveinfo.equals(filename)) {
				this.moveList.remove(i);
				if ((this.moveIndex != 0) && (i <= this.moveIndex)) {
					this.moveIndex -= 1;
				}
				p("从MoveList中删除一条相同的条目");
			}
		}
		if ((this.moveList.size() > 0) && (this.moveIndex >= 0) && (this.moveIndex < this.moveList.size())) {
			String str = (String) this.moveList.get(this.moveIndex);
			if (str.equals(filename)) {
				return;
			}
		}
		if (this.moveList.size() >= 100) {
			this.moveList.remove(0);
			this.moveIndex -= 1;
		}
		if (this.moveList.size() == 0) {
			this.moveList.add(filename);
			this.moveIndex = (this.moveList.size() - 1);
		} else {
			if (moveIndex < 0 || moveIndex >= moveList.size())
				return;

			if (((String) this.moveList.get(this.moveIndex)).equals(filename)) {
				p("向文件移动记录所添加的文件名与上一个文件相同，所以什么都没做");
				return;
			}
			if (this.moveIndex + 1 < this.moveList.size()) {
				this.moveList.add(this.moveIndex + 1, filename);
				this.moveIndex += 1;
			} else {
				this.moveList.add(filename);
				this.moveIndex = (this.moveList.size() - 1);
			}
		}
		updateOSDMessages(1);
	}

	public int findInMoveInfo(String filename) {
		int indexOf = this.moveList.indexOf(filename);
		return indexOf;
	}

	public dataForOpenedFile findInOpenedFiles(String filename) {
		for (dataForOpenedFile fileData : this.openedFiles) {
			if (fileData.filename.equals(filename)) {
				return fileData;
			}
		}
		return null;
	}

	public void addFileToOpenedFilesData(dataForOpenedFile openedfile) {
		this.openedFiles.add(openedfile);
		String showName = findFileInfoByFileName(openedfile.filename, null).showName;
		p("已将文件(" + openedfile.filename + ")[" + showName + "]加入缓存");
	}

	public void setProgressBarValue(int currentvalue, int max, String message) {
		float current_ = currentvalue;
		float max_ = max;
		int value = (int) (current_ / max_ * 100.0F);
		setLoadFileMessage(message + value + "%");
	}

	public void openProjectDir() {
		showinExplorer(this.projectFile.getParent(), false);
	}

	void showInExplorer() {
		showinExplorer(currentEditing.getPath(), true);
	}

	void showinExplorer(String path, boolean select) {
		String isselect = "";
		if (select) {
			isselect = "/select, ";
		}
		try {
			Runtime.getRuntime().exec("explorer " + isselect + path);
		} catch (IOException e) {
			getMessageBox("IOException", e.getMessage());
		}
	}

	public void saveProjectChanged() {
		saveAllDocuments();
		if (this.fileListChanged) {
			saveFileListFile();
		}
		statusBar().showMessage("已保存项目中的所有更改", 100);
	}

	public void focusMode() {
		int focusmode = getIntValueFromSettings(appInfo.focusMode, "0");
		if (focusmode == 0)
			settings.setValue(appInfo.focusMode, "1");
		else if (focusmode == 1)
			settings.setValue(appInfo.focusMode, "2");
		else
			settings.setValue(appInfo.focusMode, "0");
		// this.btext.applyEditorColor();
//		this.btext.cursorPosChanged();
	}

	public void treeClicked(QModelIndex index) {
		if (isCheackable(this.draft)) {
			this.tree.setCurrentIndex(index);
			QTreeWidgetItem item = this.tree.currentItem();
			if (item != null) {
				if (item.checkState(0).compareTo(Qt.CheckState.Checked) == 0) {
					clearOrAddSelectedStateForCheckBox(item, false);
				} else {
					clearOrAddSelectedStateForCheckBox(item, true);
				}
			}
		}
	}

	/**
	 * 显示传统的标题列表
	 */
	public void showTitleList() {
		if ((keywords != null) && (keywords.isVisible())) {
			return;
		}
		initKeyWordsDialog();
		keywords.tree.UsedToProjectPanelOrKeywordsList = 1;

		keywords.tree.clear();

		int pre = -1;
		QTextCursor tc_currentLine = text.textCursor();
		if (cheakDocument.cheakString(tc_currentLine.block().text())) {
			pre = tc_currentLine.block().position();
		} else {
			pre = moveToPreviousTitle(false);
		}
		QTextCursor tc = text.textCursor();
		int charCountOfDoc = text.charCountOfDoc();
		tc.setPosition(0);
		int pos = 0, lastpos = 0;

		do {
			QTextBlock tb = tc.block();
			String str = tb.text();
			if (cheakDocument.cheakStringOnly(str, appInfo.syls)) {
				lastpos = pos;
				pos = tb.position();
				if (keywords.tree.topLevelItemCount() > 0) {
					QTreeWidgetItem topLevelItem = keywords.tree.topLevelItem(keywords.tree.topLevelItemCount() - 1);
					topLevelItem.setData(3, 3, "(" + (pos - lastpos) + "个字)");
					topLevelItem.setData(4, 4, lastpos + "$" + pos);
				}
				QTreeWidgetItem ti = getTreeItem(keywords.tree);
				ti.setText(0, str);
				QFont f = ti.font(0);
				f.setPointSize(text.font().pointSize());
				ti.setFont(0, f);
				TextRegion tr = new TextRegion(str, pos, pos + str.length());
				ti.setData(1, 0, tr);
				if (pos == pre) {
					keywords.tree.setCurrentItem(ti);
				}
			}
		} while (tc.movePosition(QTextCursor.MoveOperation.NextBlock));
		if (keywords.tree.topLevelItemCount() > 0) {
			QTreeWidgetItem topLevelItem = keywords.tree.topLevelItem(keywords.tree.topLevelItemCount() - 1);
			topLevelItem.setData(3, 3, "(" + (charCountOfDoc - pos) + "个字)");
			topLevelItem.setData(4, 4, pos + "$" + charCountOfDoc);
		}
		keywords.message.setText("当前文档内的所有标题(" + keywords.tree.topLevelItemCount() + ")");
		keywords.statusbar.showMessage("Enter:跳转至所在位置|数字键2:选择标题内容 ");
		keywords.onlyUserAction = true;
		keywords.noDefaultSelection = true;

		showDialogAtCenter(keywords);
	}

	void setTitleStyleForDoc() {
		QTextCursor tc = text.textCursor();
		tc.movePosition(MoveOperation.Start);
		do {
			QTextBlock tb = tc.block();
			String str = tb.text();
			QTextCursor tbc = new QTextCursor(tb);
			tbc.select(SelectionType.BlockUnderCursor);
			QTextCharFormat cf = tb.charFormat();
			QTextBlockFormat bf = cf.toBlockFormat();

			if (cheakDocument.cheakString(str)) {
				cf.setFontFamily("微软雅黑");
				cf.setFontPointSize(getEditorFontSize() + getEditorTextZoomValue() + 3);
				cf.setForeground(new QBrush(new QColor(Qt.GlobalColor.yellow)));
				bf.setTextIndent(0);
//				QFont font = new QFont("微软雅黑",getEditorFontSize()+getEditorTextZoomValue()+3);
//				cf.setFont(font);
			} else {

			}
			tbc.setCharFormat(cf);
			tbc.setBlockCharFormat(cf);
			tbc.setBlockFormat(bf);
		} while (tc.movePosition(QTextCursor.MoveOperation.NextBlock));
	}

	void clearWalkList() {
		this.infoOfCurrentEditing.walkList = null;
		this.infoOfCurrentEditing.walkIndex = -1;
	}

	void moveToPreviousWalk() {
		boolean stil = false;
		do {
			fileInfo.walkInfo walkInfo = this.infoOfCurrentEditing.getPreviousWalkInfo();
			stil = moveFromWalkInfo(walkInfo);
		} while (stil);
	}

	void moveToNextWalk() {
		boolean stil = false;
		do {
			fileInfo.walkInfo walkInfo = this.infoOfCurrentEditing.getNextWalkInfo();
			stil = moveFromWalkInfo(walkInfo);
		} while (stil);
	}

	boolean moveFromWalkInfo(fileInfo.walkInfo walkInfo) {
		if (walkInfo == null) {
			return false;
		}
		QTextDocument doc = text.document_b();
		QTextBlock findBlockByNumber = doc.findBlockByNumber(walkInfo.blockNumber);
		if (findBlockByNumber.text().equals(walkInfo.textOfLine)) {
//			setLoadFileMessage("第" + walkInfo.blockNumber + "行");

			QTextCursor tc = text.textCursor();
			tc.setPosition(findBlockByNumber.position() + walkInfo.offestInLine);
			text.setTextCursor(tc);
			this.btext.scroll();

			return false;
		}
		for (int i = 0; i < doc.blockCount(); i++) {
			QTextBlock b = doc.findBlockByNumber(i);
			if (walkInfo.textOfLine.equals(b.text())) {
//				setLoadFileMessage("第" + walkInfo.blockNumber + "行");
				QTextCursor tc = text.textCursor();
				tc.setPosition(b.position() + walkInfo.offestInLine);
				text.setTextCursor(tc);
				this.btext.scroll();

				return false;
			}
		}
		this.infoOfCurrentEditing.removeCurrentWalkInfo();
		return true;
	}

	public int moveToPreviousTitle(boolean isSetView) {
		QTextDocument doc = text.document_b();
		QTextCursor tc = text.textCursor();
		int index = tc.blockNumber();
		if (index == 0) {
			return -1;
		}
		for (int i = index - 1; i >= 0; i--) {
			QTextBlock tb = doc.findBlockByNumber(i);
			QTextCursor curs = new QTextCursor(tb);
			String str = tb.text();
			if (cheakDocument.cheakStringOnly(str, appInfo.syls)) {
				if (isSetView) {
					QTextCursor tcfind = text.textCursor();
					tcfind.setPosition(curs.selectionStart());
					tcfind.setPosition(curs.selectionEnd(), QTextCursor.MoveMode.KeepAnchor);

					this.btext.doNotSetStyle = true;
					text.setTextCursor(tcfind);
					this.btext.scroll();
//					this.btext.doNotSetStyle = false;
				}
				return tb.position();
			}
		}
		return -1;
	}

	public int moveToNextTitle(boolean isSetView) {
		QTextDocument doc = text.document_b();
		QTextCursor tc = text.textCursor();
		int index = tc.blockNumber();
		if (index >= doc.blockCount() - 2) {
			return -1;
		}
		for (int i = index + 1; i < doc.blockCount(); i++) {
			QTextBlock tb = doc.findBlockByNumber(i);
			QTextCursor curs = new QTextCursor(tb);
			String str = tb.text();
			if (cheakDocument.cheakStringOnly(str, appInfo.syls)) {
				if (isSetView) {
					QTextCursor tcfind = text.textCursor();
					tcfind.setPosition(curs.selectionStart());
					tcfind.setPosition(curs.selectionEnd(), QTextCursor.MoveMode.KeepAnchor);
					this.btext.doNotSetStyle = true;
					text.setTextCursor(tcfind);
					this.btext.scroll();

//					this.btext.doNotSetStyle = false;
				}
				return tb.position();
			}
		}
		return -1;
	}

	public void showAbout() {
		String verOfSetUpdate = null;
		if (!new File("./bin_/setUpdate.jar").exists())
			verOfSetUpdate = "不可用";
		else {
			try {
				Process exec = Runtime.getRuntime().exec(appInfo.javawPath + " -jar ./bin_/setUpdate.jar -version");
				StringBuilder sb = black.readProcessOutput(exec, "utf-8");
				verOfSetUpdate = sb.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String verinfo = "";
		if (appInfo.outYear != -1)
			verinfo = "</b></i> (评估版本,评估截止期限为" + appInfo.outYear + "年" + (appInfo.outMonth - 1) + "月)<b><i>";
		QMessageBox messageBox = getMessageBoxNotShow("关于" + appInfo.appName,
				appInfo.aboutApp + ba.getUpdateInfoMini(4) + appInfo.authorInfo + "<br>-------<br>"
						+ "构建于<i> Java 14 & Qt 5.15</i><br>" + "运行于<i> " + QSysInfo.prettyProductName() + "</i><br><br>"
						+ "<b><i>主程序版本: " + appInfo.appVersion + verinfo + "<br>部署模块版本: " + verOfSetUpdate
//				+ "<br>loader版本: " 
//				+ appInfo.loaderVer 
						+ "<br>生成日期: " + appInfo.buildDate + "</i></b>");
//		messageBox.button(which)
		QPushButton addButton = messageBox.addButton(QMessageBox.StandardButton.No);
		addButton.setText("浏览有关此程序的更多信息");
		addButton.clicked.connect(ba, "openUrl()");
		messageBox.setIconPixmap(ico_app_200.pixmap(100));
		messageBox.addButton("确定", QMessageBox.ButtonRole.RejectRole);
		messageBox.exec();
	}

	public void openFileByMenu(QAction q) {
		QTreeWidgetItem qt = (QTreeWidgetItem) q.data();
		openFileByTreeItem(qt);
	}

	public void setGitMenu() {
		if ((this.projectInfo == null) || (!gitSetUp())) {
			this.ui.commit.setEnabled(false);
			this.ui.push.setEnabled(false);
			this.ui.commitAndPush.setEnabled(false);
			this.ui.branchTool.setEnabled(false);
		} else {
			this.ui.commit.setEnabled(true);
			this.ui.push.setEnabled(true);
			this.ui.commitAndPush.setEnabled(true);
		}
	}

	public void saveAsACopy() {
		if (isCheackable(this.draft)) {
			List<QTreeWidgetItem> checkedItems = getCheckedItems();
			for (QTreeWidgetItem qt : checkedItems) {
				saveAsACopy(qt);
			}
		} else {
			saveAsACopy(this.tree.currentItem());
		}
	}

	public void saveAsACopy(QTreeWidgetItem qt) {
		fileInfo in_current = getFileInfoByQTreeItem(qt);
		if (!in_current.isFile) {
			return;
		}
		String index = this.projectInfo.getProperty(appInfo.fileIndex);
		File file = new File(
				this.projectFile.getParent() + File.separator + "Files" + File.separator + index + ".black");
		if (!file.exists()) {
			QTreeWidgetItem parent = qt.parent();
			fileInfo in_new = in_current.clone();
			in_new.showName += "(副本)";
			in_new.fileName = file.getName();
			String text = readBlackFile(getFile(in_current.fileName));
			boolean isok = io.writeBlackFile(file, text, null);
			if (isok) {
				if (parent != null) {
					int indexOfChild = parent.indexOfChild(qt);
					fileInfo in_parent = getFileInfoByQTreeItem(parent);
					if (indexOfChild + 1 < in_parent.subfiles.size()) {
						in_parent.subfiles.add(indexOfChild + 1, in_new);
						QTreeWidgetItem newitem = new QTreeWidgetItem();
						newitem.setData(1, 0, in_new);
						appaySettingsForTreeItem(newitem);
						parent.insertChild(indexOfChild + 1, newitem);
					} else {
						QTreeWidgetItem newitem = new QTreeWidgetItem();
						newitem.setData(1, 0, in_new);
						appaySettingsForTreeItem(newitem);
						parent.addChild(newitem);
						in_parent.subfiles.add(in_new);
					}
				} else {
					int indexOfTopLevelItem = this.tree.indexOfTopLevelItem(qt);
					if (indexOfTopLevelItem + 1 < this.filesList.size()) {
						this.filesList.add(indexOfTopLevelItem + 1, in_new);
						QTreeWidgetItem newitem = new QTreeWidgetItem();
						newitem.setData(1, 0, in_new);
						appaySettingsForTreeItem(newitem);
						this.tree.insertTopLevelItem(indexOfTopLevelItem + 1, newitem);
					} else {
						this.filesList.add(in_new);
						QTreeWidgetItem newitem = new QTreeWidgetItem();
						newitem.setData(1, 0, in_new);
						appaySettingsForTreeItem(newitem);
						this.tree.addTopLevelItem(newitem);
					}
				}
				this.projectInfo.setProperty(appInfo.fileIndex, String.valueOf(Integer.valueOf(index).intValue() + 1));
				saveProjectFile();
				saveFileListFile();
			}
		}
	}

	public void appaySettingsForTreeItem(QTreeWidgetItem qt) {
		fileInfo in = getFileInfoByQTreeItem(qt);
		qt.setText(0, in.showName);
		if (in.isDir) {
			qt.setIcon(0, this.ico_folder);
		} else if (in.isRoot == -1) {
			if (in.isFiles) {
				if ((in.subfiles != null) && (in.subfiles.size() > 0)) {
					qt.setIcon(0, this.ico_files);
				} else {
					in.isFiles = false;
					in.isFile = true;
					in.subfiles = null;
					if (in.charCount == 0) {
						qt.setIcon(0, this.ico_file);
					} else {
						qt.setIcon(0, this.ico_fileOfHasText);
					}
				}
			} else if (in.isFile) {
				if (in.charCount == 0) {
					qt.setIcon(0, this.ico_file);
				} else {
					qt.setIcon(0, this.ico_fileOfHasText);
				}
			} else if (in.isKeyWordsList) {
				qt.setIcon(0, this.ico_keywordsList);
			} else {
				p("属性错误：" + in);
			}
		}
		setEditable(qt, false);
	}

	public void setFileTitleAsSelectedText() {
		String selectedText = text.textCursor().selectedText();
		setTextAsFileTitle(selectedText);
	}

	public void setTextAsFileTitle(String text) {
		if ((text != null) && (!text.equals(""))) {
			this.infoOfCurrentEditing.showName = text;
			this.TreeWidgetItemOfCurrentEditing.setText(0, text);
			this.textTitle.setText(text);
		}
	}

	public void gitWork_ui() {
		if (gitSetUp()) {
			String[] gitInfo = getGitInfo();
			if (gitInfo != null) {
				gitWorking(gitInfo[0], gitInfo[1], gitInfo[2]);
			}
		}
	}

	public void push_ui() {
		new showProgress(this, this, "上推至远程仓库", true) {
			public void whenOkButtonPressed() {
			}

			public void whenAutoClose() {
			}

			@Override
			public void whenDialogClosed() {
				// TODO Auto-generated method stub

			}
		};
		new Thread(new Runnable() {
			public void run() {
				black.this.setProgressInfo("连接远程仓库...", 20);
				String[] info = black.this.getGitInfo();
				black.this.push(info[0], info[1], info[2], true);

				black.this.setProgressInfo("上传完成！", 100);
			}
		}).start();
	}

	public void commit_ui() {
		if (gitSetUp()) {
			saveAllDocuments();
			int id = addTimer(new timerInfo(true, "正在提交更改到本地仓库", null));

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					List<DiffEntry> commit = commit(null, true);
					StringBuilder sb = new StringBuilder();
					if (commit != null) {
						for (DiffEntry diff : commit) {
							sb.append(diff.getNewPath() + "\n");
						}
					}

					uiRun(black.this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							removeTimer(id);
							if (commit == null)
								getMessageBox("提交更改到本地仓库", "操作被终止！因为没有更改任何文件！");
							else {
								getBMessageBox("更改并提交了" + commit.size() + "个文件", sb.toString());
							}

						}
					});
				}
			}).start();
		}
	}

	public void setGit_ui() {
		settings showSettingsDialog = showSettingsDialog();
		showSettingsDialog.ui.taps.setCurrentIndex(3);
	}

	public void testConn(String host, String username, String password) {
		setGitRespositoryPath();
		commit(null, true);
		push(host, username, password, true);
	}

	void openWifiOneMin() {
		if (adminPasswd != null) {
			try {
				if (checkRunAsTool() && checkDevcon()) {
					if (isAdmin()) {
						Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:"
								+ adminPasswd + " /domain: /command:\"" + appInfo.javawPath
								+ " -jar .\\bin_\\getMail.jar enable1Min\" /runpath:" + new File("").getAbsolutePath());
					} else {
						if (hourOfDay < 20 || hourOfDay > 21)
							Runtime.getRuntime()
									.exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:" + adminPasswd
											+ " /domain: /command:\"" + appInfo.javawPath
											+ " -jar .\\bin_\\getMail.jar enable1MinByForce\" /runpath:"
											+ new File("").getAbsolutePath());
						else {
							Runtime.getRuntime()
									.exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:" + adminPasswd
											+ " /domain: /command:\"" + appInfo.javawPath
											+ " -jar .\\bin_\\getMail.jar enable1Min\" /runpath:"
											+ new File("").getAbsolutePath());
						}
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	void checkWepWifi() {
		try {
			Process exec = Runtime.getRuntime().exec("netsh wlan show networks mode=bssid");
			StringBuilder sb = readProcessOutput(exec, "gbk");
			String networks = sb.toString();
			if (networks.indexOf("WEP") != -1) {
				uiRun(this, new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						InputDialog input = new InputDialog(black.this, "发现WEP网络", "管理员密码为：", false, true,
								adminPasswd + "") {
							@Override
							void whenOkButtonPressed() {
								// TODO Auto-generated method stub
								this.close();
							}

							@Override
							void whenClose() {
								// TODO Auto-generated method stub
								this.close();
							}

							@Override
							void whenCannelButtonPressed() {
								// TODO Auto-generated method stub
								this.close();
							}
						};
						input.show();
					}
				});

			}
		} catch (Exception e) {

		}
	}

	public void gitWorking(final String host, final String username, final String password) {
		final StringBuilder sb = new StringBuilder();
		new showProgress(this, this, "同步至远程仓库", true) {
			public void whenOkButtonPressed() {
				hide();
				dispose();
			}

			public void whenAutoClose() {
				black.this.getBMessageBox("同步结果", sb.toString());
			}

			public void whenDialogClosed() {
				// TODO Auto-generated method stub
			}
		};
		new Thread(new Runnable() {
			public void run() {
				// 预先打开内置无线网卡1分钟，以便连接网络仓库
				openWifiOneMin();
				setProgressInfo("正在检查网络...", 10);
				if (!ping(30000)) {
					progressBugStop = true;
					progressMessage = "同步失败，网络不可用！";
				}

				List<DiffEntry> commit = black.this.commit(null, true);
				black.this.setProgressInfo("正在将本地仓库更改同步到远程仓库", 30);

				Iterable<PushResult> push = black.this.push(host, username, password, true);
				black.this.setProgressInfo("已将更改同步至远程仓库，开始加载同步结果...", 70);
				if (commit != null) {
					sb.append("更改的文件(" + commit.size() + ")：\n--------------\n");
					for (DiffEntry diff : commit) {
						sb.append(diff.getNewPath() + "\n");
					}
				}
				black.this.setProgressInfo("已获取文件更改信息，开始获取服务器返回结果...", 80);
				sb.append("-------------------\n远程仓库返回值：\n");
				ArrayList<String> al = new ArrayList();
				Iterator localIterator2;
				if (push != null) {
					Iterator localIterator3;
					for (localIterator2 = push.iterator(); localIterator2.hasNext(); localIterator3.hasNext()) {
						PushResult pr = (PushResult) localIterator2.next();
						Collection<RemoteRefUpdate> remoteUpdates = pr.getRemoteUpdates();
						localIterator3 = remoteUpdates.iterator();
						// continue;
						RemoteRefUpdate rru = (RemoteRefUpdate) localIterator3.next();
						sb.append("远程分支名： " + rru.getRemoteName() + "\n");
						sb.append("对象ID： " + rru.getNewObjectId() + "\n");
						sb.append("更改备注： " + rru.getMessage() + "\n");
						sb.append("是否删除： " + rru.isDelete() + "\n");
						sb.append("强制更新： " + rru.isForceUpdate() + "\n");
						sb.append("状态： " + rru.getStatus() + "\n");
						if (rru.getStatus().toString().equals("OK")) {
							al.add(rru.getRemoteName());
						}
					}
				}
				sb.append("\n================\n同步的分支(" + al.size() + ")" + "：\n");
				for (String str : al) {
					sb.append(str + "\n");
				}
				black.this.setProgressInfo("同步成功！", 100);
			}
		})

				.start();
	}

	public String[] getGitInfo() {
		String host = null, username = null, password = null;
		try {
			host = des.decrypt(this.projectInfo.getProperty(appInfo.gitHost));
			username = des.decrypt(this.projectInfo.getProperty(appInfo.gitUsername));
			password = des.decrypt(this.projectInfo.getProperty(appInfo.gitPassword));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			p("没有储存Git凭据");
		}

		if ((host == null) || (username == null) || (password == null)) {
			return null;
		}
		return new String[] { host, username, password };
	}

	public void setProgressInfo(String message, int progressValue) {
		if (!this.progressBugStop) {
			this.progressMessage = message;
			this.progressValue = progressValue;
		} else {
		}
	}

	public void createBranch(String branchName) {
		String[] gitInfo = getGitInfo();
		String host = null, username = null, password = null;
		if (gitInfo != null) {
			host = gitInfo[0];
			username = gitInfo[1];
			password = gitInfo[2];
		} else
			return;

		try {
			gitTool.createNewBranch(branchName, this.projectFile.getParent());
		} catch (RefAlreadyExistsException e) {
			addlogs(null, e);
			return;
		} catch (RefNotFoundException e) {
			addlogs(null, e);
			return;
		} catch (InvalidRefNameException e) {
			addlogs(null, e);
			return;
		} catch (IOException e) {
			addlogs(null, e);
			return;
		} catch (GitAPIException e) {
			addlogs(null, e);
			return;
		}
		push(host, username, password, true);
	}

	public void copyBranchFromLocal(String dirpath, String branchName, Collection<String> branchs, boolean all) {
		if (!isHasGitDir()) {
			return;
		}
		try {
			gitTool.cloneFromLocal(this.projectFile.getParent(), dirpath, all, branchs, branchName);
		} catch (InvalidRemoteException e) {
			addlogs(null, e);
		} catch (TransportException e) {
			addlogs(null, e);
		} catch (IOException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		}
	}

	public void copyBranchFromRemote(String dirpath, String branchName, Collection<String> branchs, boolean all) {
		String[] gitInfo = getGitInfo();
		String host = null, username = null, password = null;
		if (gitInfo != null) {
			host = gitInfo[0];
			username = gitInfo[1];
			password = gitInfo[2];
		} else
			return;

		try {
			gitTool.CloneFromRemote(dirpath, host, all, branchs, branchName, username, password);
		} catch (IOException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		} catch (JGitInternalException e) {
			addlogs(null, e);
		}
	}

	public void deleteBranchFromRemote(String branchName, boolean local) {
		String[] gitInfo = getGitInfo();
		String host = null, username = null, password = null;
		if (gitInfo != null) {
			host = gitInfo[0];
			username = gitInfo[1];
			password = gitInfo[2];
		} else
			return;

		try {
			gitTool.deleteBranchFromRemote(this.projectFile.getParent(), host, branchName, local, username, password);
		} catch (InvalidRemoteException e) {
			addlogs(null, e);
		} catch (TransportException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		} catch (IOException e) {
			addlogs(null, e);
		}
	}

	public Collection<Ref> getAllBranchFromRemote() {
		String[] gitInfo = getGitInfo();
		String host = null, username = null, password = null;
		if (gitInfo != null) {
			host = gitInfo[0];
			username = gitInfo[1];
			password = gitInfo[2];
		} else
			return null;

		Collection<Ref> allBranchFromRemote = null;
		try {
			allBranchFromRemote = gitTool.getAllBranchFromRemote(host, username, password);
		} catch (InvalidRemoteException e) {
			addlogs(null, e);
		} catch (TransportException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		}
		return allBranchFromRemote;
	}

	public Iterable<PushResult> push(String host, String username, String password, boolean quiet) {
		Iterable<PushResult> pushToRemote = null;
		try {
			pushToRemote = gitTool.pushToRemote(this.projectFile.getParent(), host, username, password, true);
		} catch (InvalidRemoteException e) {
			addlogs(null, e);
		} catch (TransportException e) {
			addlogs(null, e);
		} catch (IOException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		}
		if ((!quiet) && (pushToRemote != null)) {
			StringBuilder sb = new StringBuilder();
			Iterator localIterator2;
			for (Iterator localIterator1 = pushToRemote.iterator(); localIterator1.hasNext(); localIterator2
					.hasNext()) {
				PushResult pr = (PushResult) localIterator1.next();
				Collection<RemoteRefUpdate> remoteUpdates = pr.getRemoteUpdates();
				localIterator2 = remoteUpdates.iterator();
				// continue;
				RemoteRefUpdate rru = (RemoteRefUpdate) localIterator2.next();
				sb.append(rru + "\n");
			}
			getBMessageBox("上传结果", sb.toString());
		}
		return pushToRemote;
	}

	public boolean gitSetUp() {
		String[] gitInfo = getGitInfo();
		if (gitInfo != null) {

			if (isHasGitDir()) {
				return true;
			}
			setGitRespositoryPath();
			if (isHasGitDir()) {
				getMessageBox("git信息", "检查到项目已储存git凭据，但未设置git目录\n-------\n已自动修复git目录！");
				return true;
			}
			getMessageBox("git信息", "检查到项目已储存git凭据，但缺少git目录\n--------\n已尝试自动修复，但修复失败！");
			return false;
		}
		return false;
	}

	public bmessageBox getBMessageBox(String title, String message) {
		bmessageBox bmessageBox = null;
		if ((this.finddialog == null) || (!this.finddialog.isActiveWindow())) {
			bmessageBox = new bmessageBox(this, title, "确定", message, true) {
				public void buttonPressedAction(String content) {
					close();
					dispose();
				}
			};
		} else {
			bmessageBox = new bmessageBox(this.finddialog, title, "确定", message, true) {
				public void buttonPressedAction(String content) {
					close();
					dispose();
				}
			};
		}
		return bmessageBox;
	}

	public List<DiffEntry> commit(String message, boolean quiet) {
		if (!isHasGitDir()) {
			setGitRespositoryPath();
		}
		changeBranch(this.projectInfo.getProperty(appInfo.projectName));

		String mess = null;
		List<DiffEntry> commit = null;
		if (message == null) {
			mess = time.getCurrentDate("-") + "(" + time.getCurrentTime().replace(":", "点") + "分)" + "项目备份";
		} else {
			mess = message;
		}
		try {
			commit = gitTool.commit(this.projectFile.getParent(), mess);
		} catch (IOException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		}
		if ((!quiet) && (commit != null)) {
			StringBuilder sb = new StringBuilder();
			for (DiffEntry diff : commit) {
				sb.append(diff.getNewPath() + "\n");
			}
			getBMessageBox("更改的文件", sb.toString());
		}
		getCommitLogsFormLocalRespository();
		return commit;
	}

	public Iterable<RevCommit> getCommitLogsFormLocalRespository() {
		Iterable<RevCommit> rev = null;
		try {
			rev = gitTool.getCommitInfoFromLocalRespository(this.projectFile.getParent());
		} catch (NoHeadException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		} catch (IOException e) {
			addlogs(null, e);
		}
		return rev;
	}

	public ArrayList<RevCommit> getCommits(String[] branchNames) {
		ArrayList<RevCommit> al = null;
		try {
			al = gitTool.getCommitsFromBranch(this.projectFile.getParent(), branchNames);
		} catch (NoHeadException e) {
			addlogs(null, e);
		} catch (IOException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		}
		return al;
	}

	public void changeBranch(String branchName) {
		try {
			gitTool.changeBranch(this.projectFile.getParent(), branchName);
		} catch (RefAlreadyExistsException e) {
			addlogs(null, e);
		} catch (RefNotFoundException e) {
			addlogs(null, e);
		} catch (InvalidRefNameException e) {
			addlogs(null, e);
		} catch (CheckoutConflictException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		} catch (IOException e) {
			addlogs(null, e);
		}
	}

	public boolean isHasGitDir() {
		File dir = new File(this.projectFile.getParent() + File.separator + ".git");
		if ((dir.exists()) && (dir.list().length > 0)) {
			return true;
		}
		return false;
	}

	public void setGitRespositoryPath() {
		try {
			if (!isHasGitDir()) {
				gitTool.createGitRepository(this.projectFile.getParent());
			}
		} catch (IOException e) {
			addlogs(null, e);
		} catch (IllegalStateException e) {
			addlogs(null, e);
		}
		try {
			gitTool.createNewBranch(this.projectInfo.getProperty(appInfo.projectName), this.projectFile.getParent());
		} catch (RefAlreadyExistsException e) {
			addlogs(null, e);
		} catch (RefNotFoundException e) {
			addlogs(null, e);
		} catch (InvalidRefNameException e) {
			addlogs(null, e);
		} catch (IOException e) {
			addlogs(null, e);
		} catch (GitAPIException e) {
			addlogs(null, e);
		}
	}

	public void addlogs(String log, Exception e) {
		if (e == null) {
			this.logsmessage.append("[" + time.getCurrentDate("-") + " " + time.getCurrentTime() + "] " + log + "<br>");
		} else {
			this.logsmessage.append("[" + time.getCurrentDate("-") + " " + time.getCurrentTime() + "] " + "("
					+ e.getStackTrace()[0].getClassName() + " " + e.getStackTrace()[0].getMethodName() + " "
					+ e.getStackTrace()[0].getLineNumber() + "行 ：" + e.getMessage() + "<br>");
			this.progressMessage = ("操作失败: " + e.getMessage());

			this.progressBugStop = true;
		}
	}

	public void saveGitInfo(String host, String username, String password) {
		try {
			this.projectInfo.setProperty(appInfo.gitHost, des.encrypt(host));
			this.projectInfo.setProperty(appInfo.gitUsername, des.encrypt(username));
			this.projectInfo.setProperty(appInfo.gitPassword, des.encrypt(password));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		saveProjectFile();
		if (isHasGitDir()) {
			setGitMenu();
		}
	}

	void clearCharCountOfTodayInput() {
		infoOfCurrentEditing.charCountOfLastInput = (time.getCurrentDate("-") + " 0");
		btext.charCountAtToday = charCountWithoutEnter;
		btext.editTime = 0;
		updateOSDMessages(2);
	}

	public void showCharCountBox() {
		String str = text.toPlainText();
		int[] stat = cheakDocument.wordStat(str);
		int characterCount = str.length();
		int blockCount = stat[2];
		int lineCount = stat[2];
		int charCountWithoutReturn = characterCount - blockCount;
		String editTime = null;
		if (btext.editTime < 1000)
			editTime = "0秒";
		else
			editTime = time.msToTime(btext.editTime);
		float writeSpeed = plusCharCount / (btext.editTime / 1000F) * 3600F;

		String message = "今日输入字符\t\t" + this.plusCharCount + "\n今日编辑时间\t\t约" + editTime + "\n-----------\n" + "字符总数\t\t"
				+ characterCount + "\n" + "字数(不计换行符)\t\t" + charCountWithoutReturn + "\n" + "中日韩字符数\t\t" + stat[1]
				+ "\n" + "空格数\t\t\t" + stat[0] + "\n" + "换行符数\t\t" + stat[2] + "\n" + "段数\t\t\t" + blockCount + "\n"
				+ "行数\t\t\t" + lineCount + "\n" + "页数\t\t\t" + ("第" + pageCurrent + "页，共" + allPage + "页")
				+ "\n-----------\n" + "光标位置\t\t" + caretPos.text() + "\n" + "光标前有\t\t" + text.textCursor().position()
				+ "个字符\n" + "光标后有\t\t" + (text.charCountOfDoc() - text.textCursor().position()) + "个字符";

		QMessageBox messagebox = new QMessageBox(QMessageBox.Icon.NoIcon, "文档统计", message,
				new QMessageBox.StandardButtons(QDialogButtonBox.StandardButton.Ok.value()), this);

		messagebox.show();
	}

	public void removeAllSaveTimer() {
		ArrayList<timerInfo> remove = new ArrayList();
		for (timerInfo info : this.timerInfos) {
			if (info.type == 0) {
				remove.add(info);
			}
		}
		for (timerInfo info : remove) {
			this.timerInfos.remove(info);
		}
	}

	public boolean isHasSaveTimer() {
		boolean ishas = false;
		for (timerInfo info : this.timerInfos) {
			if (info.type == 0) {
				ishas = true;
				break;
			}
		}
		return ishas;
	}

	public void addSaveTimer() {
		if (isHasSaveTimer()) {
			removeAllSaveTimer();
		}
		addTimer(new timerInfo(0, getAutoSave() * 1000, "保存文档", new Runnable() {
			public void run() {
				new bRunnable(0, true, true, true, true) {
					public void run() {
						black.this.saveProjectChanged();
					}
				};
			}
		}, false));
	}

	public int getAutoSave() {
		Integer save = Integer.valueOf((String) this.settings.value(appInfo.autoSave, "300"));
		return save.intValue();
	}

	public void setAutoSave(int value) {
		if (value > 0) {
			this.settings.setValue(appInfo.autoSave, String.valueOf(value));
			addSaveTimer();
		} else if (value == -1) {
			this.settings.setValue(appInfo.autoSave, String.valueOf(value));
			removeAllSaveTimer();
		}
	}

	public int getDocumentMargin() {
		Integer margin = Integer.valueOf((String) this.settings.value(appInfo.documentMargin, "20"));
		return margin.intValue();
	}

	public void setDocumentMargin(int value) {
		this.settings.setValue(appInfo.documentMargin, String.valueOf(value));
		if (writingView == 1) {
			textLayout.setContentsMargins(value, 0, value, 0);
		}
	}

	public void setWritingViewMode(int value) {
		this.settings.setValue(appInfo.writingViewMode, String.valueOf(value));
		if (writingView > 0) {
			exitwritingView(Boolean.valueOf(true));
			writingView(Boolean.valueOf(true));
		}
	}

	public void setWritingViewTextWidth(int value) {
		if (writingView > 0) {
			int margings = value / 2;
			setTextLocationForWritingView(margings);

		}
		this.settings.setValue(appInfo.textWidthForWritingView, String.valueOf(value));
	}

	public int getWritingViewTextWitdh() {
		QDesktopWidget desk = new QDesktopWidget();
		QRect screenGeometry = desk.screenGeometry();
		int value = Integer.valueOf((String) this.settings.value(appInfo.textWidthForWritingView,
				String.valueOf(screenGeometry.width() / 3))).intValue();
		return value;
	}

	public boolean hasActiveWindowOnChildren() {
		List<QObject> children = children();
		for (QObject o : children) {
			QWidget wid = null;
			try {
				wid = (QWidget) o;
			} catch (ClassCastException localClassCastException) {
			}
			if ((wid != null) && (wid.isActiveWindow())) {
				return true;
			}
		}
		return false;
	}

	static int mapAlphaByTime() {
		int h = Calendar.getInstance().get(11);
		int m = Calendar.getInstance().get(12);

		int s = 18000;
		Float a_s = Float.valueOf(254.0F / s);
		int alpha_ = -1;
		int hourOfDay = h;
		if ((hourOfDay >= 4) && (hourOfDay < 9)) {
			int hour = hourOfDay - 4;
			int sec = m * 60;
			int secs = hour * 3600 + sec;
			alpha_ = (int) (255.0F - a_s.floatValue() * secs);
		} else if ((hourOfDay >= 9) && (hourOfDay < 17)) {
			alpha_ = 1;
		} else if ((hourOfDay >= 17) && (hourOfDay < 22)) {
			int hour = hourOfDay - 17;
			int sec = m * 60;
			int secs = hour * 3600 + sec;
			alpha_ = (int) (1.0F + a_s.floatValue() * secs);
		} else if ((hourOfDay >= 22) && (hourOfDay <= 23)) {
			alpha_ = 255;
		} else if ((hourOfDay >= 0) && (hourOfDay < 4)) {
			alpha_ = 255;
		}
		return alpha_;
	}

	void changeWritingView() {
		if (writingView == 0) {
			return;
		}
		if (writingView == 1) {
			setWritingViewMode(2);
		} else if (writingView == 2) {
			setWritingViewMode(1);
		}
	}

	double getShadowBlurRadius() {
		if (writingView == 1) {
			return Double.valueOf((String) this.settings.value(appInfo.blurRadiusWindowMode, "0")).doubleValue();
		}
		if (writingView == 2) {
			return Double.valueOf((String) this.settings.value(appInfo.blurRadiusFullScreen, "0")).doubleValue();
		}
		return -1.0D;
	}

	public void setWindowTitleB(String title) {
		setWindowTitle(title);
	}

	void setEditorHeight(int v) {
//		int width = getWritingViewTextWitdh();
//		settings.setValue(appInfo.editorHeight, v + "");
//		this.ui.verticalLayout.setContentsMargins(0, v, width, v - 1);

//		textLayout.setContentsMargins(getDocumentMargin(), 0,
//				getDocumentMargin() - getIntValueFromSettings(appInfo.textVScrollbarWidth, "15"), 0);
	}

	int getEditorHeight() {
		return this.getIntValueFromSettings(appInfo.editorHeight, "10");
	}

	public void VScrollSetValue_(int value) {
		if (value == text.verticalScrollBar().value())
			return;
//		else if(value > text.verticalScrollBar().value()) {
//			startv = value-20;
//		}else {
//			startv = value+20;
//		}
		QPropertyAnimation pro = new QPropertyAnimation();
		pro.setTargetObject(text.verticalScrollBar());
		pro.setPropertyName(new QByteArray("value"));
		pro.setStartValue(text.verticalScrollBar().value());
		pro.setEndValue(value);
		pro.setDuration(100);
		pro.start();
		new bRunnable(100, true, false, true, true) {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if ((int) pro.currentValue() != text.verticalScrollBar().value())
					text.verticalScrollBar().setValue(value);
			}
		};
	}

	void setTextLocationForWritingView(int width) {
		debugPrint("为写作视图设置编辑器位置，宽度为"+width);
//		this.ui.verticalLayout.setContentsMargins(27, 20, width, 20);
		String topAndBottom = getStringValueFromSettings(appInfo.writingViewTextTopAndBottom, "0 0");
		String[] args = cheakDocument.checkCommandArgs(topAndBottom);
		int top = 0, bottom = 0;
		isRight isTop = isRightIntValue(args[0]);
		if (isTop.isright) {
			top = (int) isTop.value;
		}
		isRight isBottom = isRightIntValue(args[1]);
		if (isBottom.isright) {
			bottom = (int) isBottom.value;
		}
		int[] r = text.setRange();
		this.ui.verticalLayout.setContentsMargins(width, r[0], width, r[1]);
//		textWidget.setGeometry(width()-width, top, width, bottom);
	}

	public void writingView(Boolean isSetEditorWidth) {
		if (btext == null)
			return;
//		if (isHasWord)
//			return;
//		if (!isAdmin())
//		return;
		if (writingView == 1) {
			return;
		}
		if (this.btext.text.isEnabled()) {
			writingView = 1;
			this.saveGeometry = saveGeometry();
			if (keywords != null) {
				keywords.hide();
			}
			hide();

			this.textTitle.setVisible(false);
			this.ui.menubar.setVisible(false);

			this.toolbar.setVisible(false);

			statusBar().setVisible(false);

			int width = getWritingViewTextWitdh() / 2;
			setTextLocationForWritingView(width);
			setTextVScrollBarLocation();
			setTextMargins();

			// this.windowFlags = windowFlags();
			//
			// show();
			// QPalette background = text.palette();
			// background.setColor(ColorRole.Base, new QColor(235, 230, 230));
			// background.setColor(ColorRole.Text, new QColor(0, 0, 0));

			// background.setColor(ColorRole.Base, new QColor(174, 251, 172));
			// text.setPalette(background);
			windowFlags = windowFlags();
			
			WindowFlags newWF = windowFlags;
			debugPrint("isSetFrameLessWindowHint "+newWF.isSet(Qt.WindowType.FramelessWindowHint));
			newWF.set(Qt.WindowType.FramelessWindowHint);
			setWindowFlags(newWF);
//			hide();// setWindowFlags使窗口隐藏后，有时需先调用一次hide方法，再调用show或showFullScreen才能让窗口显示出来

			/**
			 * 背景透明选项似乎只在全屏模式下可以开启 即便是无边框窗口，只要不是全屏幕大小都不能开启背景透明特性，开启后窗口不会显示
			 * 升级Qt到5.15后好像解决了这个问题
			 */
//			setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground, true);
//			setAttribute(Qt.WidgetAttribute.WA_NoSystemBackground, false);
//			setStyleSheet("background:transparent");
			if (getBooleanValueFromSettings(appInfo.fullScreen, "true")) {
				showFullScreen();
			} else {
				QDesktopWidget desk = new QDesktopWidget();
				QRect availableGeometry = desk.availableGeometry();
				desk.dispose();
				setGeometry(availableGeometry);
				show();
			}

//			setWindowOpacity(0.99);

			if (this.ui.dockTree.isVisible()) {
				this.ui.dockTree.hide();
			}
//			this.ui.hideFilePanel.setChecked(true);
			// this.btext.applyEditorColor();

			applyColorChange();

			text.setVisible(false);
			text.setVisible(true);
			text.setFocus();
			this.btext.scroll();
			debugPrint("C " + textWidget.geometry());

		}
	}

	public void cutText() {
		if (!text.isAccicesable())
			return;
		QTextCursor tc = text.textCursor();
		if (!tc.hasSelection())
			return;

		text.cut();
		setLoadFileMessage("剪切文本");
	}

	public void copyText() {
		if (!text.isAccicesable())
			return;
		text.copy();
		setLoadFileMessage("复制文本");
	}

	public void pasteWithCurrentFormat() {
		if (!text.isAccicesable())
			return;
		QTextCursor tc = text.textCursor();
		QClipboard clipboard = QApplication.clipboard();
		String originalText = clipboard.text();
		tc.insertText(originalText);

		setLoadFileMessage("粘贴文本");
	}

	void resetDocumentFormat() {
		int messageBoxWithYesNO = getMessageBoxWithYesNO("警告", "此操作可能会使此文档内的锚点（如果有）出错，你确定要继续此操作吗？", "确定", "取消",
				QMessageBox.Icon.Warning, 0);
		if (messageBoxWithYesNO == 0)
			return;

		StringBuilder sb = new StringBuilder();
		QTextDocument doc = text.document_b();
		int count = doc.blockCount();
		for (int i = 0; i < count; i++) {
			QTextBlock block = doc.findBlockByNumber(i);
			String str = block.text();
			if (str.length() > 0) {
				for (int a = 0; a < str.length(); a++) {
					char charAt = str.charAt(a);
					if ((charAt != ' ') && (charAt != '\t') && (charAt != '　')) {
						String substring = str.substring(a, str.length());
						if (i < count - 1) {
							sb.append(substring + "\n");
							break;
						}
						sb.append(substring);
						break;
					}
				}
			}
		}
		QTextCursor tc = text.textCursor();
		tc.beginEditBlock();
		tc.movePosition(QTextCursor.MoveOperation.Start);
		tc.movePosition(QTextCursor.MoveOperation.End, QTextCursor.MoveMode.KeepAnchor);
		tc.insertText(sb.toString());
		tc.endEditBlock();
		setStyleForCurrentDocument(false);
	}

	public void exitwritingView(Boolean b) {
//		if (!isAdmin())
//			return;

		if (keywords != null) {
			keywords.hide();
		}
		if (!textWidget.isVisible()) {
			QRect object = (QRect) ba.tempData.get("oldTextWidgetGeometry");
			if (object != null)
				textWidget.setGeometry(object);
			textWidget.show();
//			ba.showBingPicInfo();
			if(ba.label != null) {
				ba.label.hide();
				ba.label.setGeometry(new QRect(0, height() + 100, width(), 100));			
			}
			
		}

		QPalette palette = statusBar().palette();
		palette.setColor(QPalette.ColorRole.Window, this.windowColor);

		statusBar().setAutoFillBackground(true);
		statusBar().setBackgroundRole(QPalette.ColorRole.Window);

		statusBar().setPalette(palette);

		this.ui.verticalLayout.setContentsMargins(0, 0, 0, 0);
//		if (getBooleanValueFromSettings(appInfo.noDock, "false")) {
//			this.ui.menubar.setVisible(true);
//			this.toolbar.setVisible(true);
//			this.ui.dockTree.setVisible(true);
//			this.ui.hideFilePanel.setChecked(false);
//		}else {
//			if(dock.isVisible()) {
//				
//			}
//		}
		statusBar().setVisible(true);

		this.textTitle.setVisible(true);
		if (writingView == 1) {
//			setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground, false);
//			setAttribute(Qt.WidgetAttribute.WA_NoSystemBackground, false);
			setWindowState(new Qt.WindowState[] { Qt.WindowState.WindowNoState });
			windowFlags.clear(WindowType.FramelessWindowHint);
			setWindowFlags(windowFlags);
//			
			restoreGeometry(this.saveGeometry);

			repaint();
			writingView = 0;

//			setWindowOpacity(0.1);			
			if (dock.isFloating())
				dock.setFloating(false);

			if (getBooleanValueFromSettings(appInfo.fullScreen, "true")) {
//				if (!getBooleanValueFromSettings(appInfo.windowIsMax, "false"))
//					showNormal();
//				else
//					showMaximized();
			} else {
				show();
			}

			if (b) {
				show();
			}
		}
		writingView = 0;
		text.setVisible(false);
		text.setVisible(true);
		setTextMargins();
		//退出写作视图时需要重新设置textWideget的边距
		text.setRange();
		applyColorChange();
		showAllWidget();
		text.setFocus();
		this.btext.scroll();
	}

	protected void paintEvent(QPaintEvent event) {
		QPainter p = new QPainter(this);
		if (writingView == 0) {
			int startX = this.dock.x() + this.dock.width() + 1;
			int height = this.dock.titleBarWidget().height() - 2;
			int startY = 0;
			if (this.toolbar.isVisible()) {
				startY = this.toolbar.y() + this.toolbar.height();
			} else {
				startY = this.ui.menubar.y() + this.ui.menubar.height();
			}
			p.setRenderHint(QPainter.RenderHint.HighQualityAntialiasing, true);
			p.setPen(new QPen(new QBrush(borderColor), 1.0D));
			if ((this.dock != null) && (this.dock.isVisible())) {
				p.drawLine(this.dock.x() + this.dock.width(), startY, this.dock.x() + this.dock.width(),
						statusBar().y());
			}
			if ((textTitle != null) && (textTitle.isVisible())) {
				p.drawLine(this.dock.x() + this.dock.width(), startY + this.dock.titleBarWidget().height() - 1,
						this.dock.x() + this.dock.width() + 5, startY + this.dock.titleBarWidget().height() - 1);
				QLinearGradient linearGradient = new QLinearGradient(new QPointF(startX, startY),
						new QPointF(startX, startY + height));
				QColor a = new QColor(247, 249, 250);
				QColor b = new QColor(232, 236, 240);

				linearGradient.setColorAt(0.0D, new QColor(Qt.GlobalColor.white));
				linearGradient.setColorAt(0.4D, a);
				linearGradient.setColorAt(1.0D, b);

				p.setPen(new QPen(new QBrush(linearGradient), 0.0D));

				linearGradient.setSpread(QGradient.Spread.PadSpread);
				p.setBrush(linearGradient);

				p.drawRect(startX, startY, 2, height);
			}
		}
		else {
			ba.drawImg(p);
			
		}

//		if (writingView == 1 && !getBooleanValueFromSettings(appInfo.simpleWritingView, "false")) {
//			if (appInfo.miniMode.equals("1")) {
//				p.fillRect(rect(), new QColor(Qt.GlobalColor.black));
//				String text = "Black处于备用模式";
//				QFont font = p.font();
//				font.setPointSize(font.pointSize() + getEditorTextZoomValue());
//				p.setFont(font);
//				QFontMetrics fm = p.fontMetrics();
//				QRect br = fm.boundingRect(text);
//				p.setPen(new QColor(Qt.GlobalColor.white));
//				p.drawText((width() - br.width()) / 2, (height() - br.height()) / 2, text);
//				return;
//			}
//			QColor color = palette().color(backgroundRole());
////			if(img != null && img.isVisible())
////			color.setAlpha(200);
////			else color.setAlpha(255);
//			p.fillRect(rect(), color);
//
//			int whatColor = getIntValueFromSettings(appInfo.editorColor, "0");
//			if (whatColor == 2) {
//				if (text != null && text.isVisible()) {
//					if (bg == null || bgImgChanged) {
//						bgImgChanged = false;
//						String bgimg = getStringValueFromSettings(appInfo.lightWindowBgImg, "bgF.png");
//						bg = new QImage("./RC/" + bgimg);
//					}
//					int w = 0, h = 0;
//					QDesktopWidget desk = new QDesktopWidget();
//
//					if (!getBooleanValueFromSettings(appInfo.fullScreen, "true")) {
//						QRect availableGeometry = desk.availableGeometry();
//						w = availableGeometry.width();
//						h = availableGeometry.height();
//					} else {
//						QRect geo = desk.geometry();
//						w = geo.width();
//						h = geo.height();
//					}
//
//					bg = bg.scaled(w, h);
//					QSize s = bg.size();
//					int x = (width() - s.width()) / 2;
//					int y = (height() - s.height()) / 2;
//					p.drawImage(x, y, bg);
//
//					if (getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
////							QColor bg = new QColor(106,106,106);
////							QColor fg = new QColor(Qt.GlobalColor.white);
////							QColor c = new QColor(116,112,112);
////							QRect rect = rect();
////							p.fillRect(rect, new QColor(Qt.GlobalColor.black));
////							p.fillRect(0,65,width()-50,height()-105, fg);
//
////							int focus = getIntValueFromSettings(appInfo.focusMode, "0");
////							if (inReserach || focus == 0) {
////								QColor line = new QColor(Qt.GlobalColor.black);
////								QColor title = new QColor(Qt.GlobalColor.black);
////								QColor top = new QColor(Qt.GlobalColor.blue);
////								QColor caret = new QColor(Qt.GlobalColor.black);
////								btext.titleMap(p,100,50,100,title,caret,top,line);
////							}
//
////							p.setPen(fg);
//					} else {
//						QColor bg = null;
//						// 白天添加白色层，对背景图片的亮度进行补偿。晚上相反，添加灰色层降低亮度
//						if (hourOfDay > 7 && hourOfDay < 17)
//							bg = new QColor(255, 255, 255, 100);
//						else
//							bg = new QColor(116, 112, 112, 100);
//
//						p.fillRect(0, 0, width(), height(), bg);
//
////							QColor fg = new QColor(Qt.GlobalColor.black);
////							QColor bg = new QColor(21,21,21);
////							p.fillRect(0,0,width(),20, bg);
////							p.fillRect(0,20,width()-50,height()-40, fg);
//
////							int focus = getIntValueFromSettings(appInfo.focusMode, "0");
////							if (inReserach || focus == 0) {
////								QColor textColor = text.palette().color(ColorRole.Text);
////								QColor top = new QColor(Qt.GlobalColor.blue);
////								QColor caret = new QColor(Qt.GlobalColor.yellow);
////								btext.titleMap(p,100,50,100,textColor,caret,top,textColor);	
////							}
//
//					}
//					// 绘制页面背景
//					boolean useTextbgColor = getBooleanValueFromSettings(appInfo.useBgColor, "true");
//					if (useTextbgColor) {
//						QColor pagebg = (QColor) settings.value(appInfo.colortextbg, new QColor(Qt.GlobalColor.white));
//						pagebg.setAlpha(200);
//						p.setBrush(pagebg);
//						boolean useborder = getBooleanValueFromSettings(appInfo.textbgborder, "false");
//						if (!useborder)
//							p.setPen(PenStyle.NoPen);
//						QRect mmmR = new QRect(textWidget.x() - textLayout.contentsMargins().left(),
//								textWidget.y() + textLayout.contentsMargins().top(),
//								textWidget.width() + textLayout.contentsMargins().left()
//										+ textLayout.contentsMargins().right(),
//								textWidget.height() - textLayout.contentsMargins().bottom());
////						p.fillRect(mmmR, new QColor(Qt.GlobalColor.yellow));
//						if (getBooleanValueFromSettings(appInfo.Antialiasing, "true"))
//							p.setRenderHint(RenderHint.Antialiasing);
//						p.drawRoundedRect(mmmR, 3, 3);
////						int xxx = mmmR.x()+mmmR.width();
////						int yyy = mmmR.y()+mmmR.height();
////						QPolygon ppp = new QPolygon();
////						QColor sign = new QColor(Qt.GlobalColor.black);
////						sign.setAlpha(0);
////						p.setBrush(sign);
////						int space = 20;
////						ppp.add(xxx, yyy);
////						ppp.add(xxx-space, yyy);
////						ppp.add(xxx,yyy-space);
////						p.drawPolygon(ppp);
//
//					}
//
////					QColor c = new QColor(Qt.GlobalColor.gray);
////					p.setPen(c);
////					// Courier New
////					if (text == null || !text.isTextContentVisible())
////						return;
////					QFont f = new QFont("微软雅黑", 8);
////					f.setBold(true);
////					p.setFont(f);
////					String chs = "";
////					if (text.isReadOnly())
////						chs = text.charCountOfDoc() + "";
////					else
////						chs = plusCharCount + ":" + text.charCountOfDoc();
////					
////					QRect re = p.fontMetrics().boundingRect(chs);
////					p.drawText((width() - re.width()) / 2, Math.abs((45 - re.height()) / 2), chs);
////					
////					p.setPen(new QColor(Qt.GlobalColor.white));
////					QFont ff = new QFont("微软雅黑", 9);
////					p.setFont(ff);
//					if (noPageLine)
//						return;
//					// 绘制编辑线
////					QRect cr = text.cursorRect();
////					QPoint m = text.mapTo(this, new QPoint(cr.x(),cr.y()));
////					int yy = m.y()+cr.height();
////					p.setPen(new QPen(new QColor(Qt.GlobalColor.black), 1, PenStyle.DotLine));
////					p.drawLine(0, yy, width(), yy);
////					p.drawLine(m.x(),0,m.x(),height());
//					// 绘制分页线
//					int pageHeight = getIntValueFromSettings(appInfo.pageHeght, "1619");
//					debugPrint(pageHeight);
//					pageCurrent = text.verticalScrollBar().value() / pageHeight + 1;
//					allPage = text.verticalScrollBar().maximum() / pageHeight + 1;
////					p.drawText(80, height() - 8, "第"+pageCurrent+"页，共" + allPage + "页");
//
//					int left = pageHeight - (text.verticalScrollBar().value() % pageHeight);
//					int lineLeft = textWidget.x() - textLayout.contentsMargins().left();
//					int lineRight = textWidget.width() + lineLeft + textLayout.contentsMargins().left()
//							+ textLayout.contentsMargins().right();
//					if (left >= 0 && left <= text.viewport().height()) {
//						boolean ishas = false;
//						QTextCursor tc = text.cursorForPosition(new QPoint(0, left));
//						tc.movePosition(MoveOperation.StartOfBlock);
//						QRect crr = text.cursorRect(tc);
//						QTextLayout layout = tc.block().layout();
////						System.out.println(tc.block().text() +" "+layout.lineCount());
//						for (int i = 0; i < layout.lineCount(); i++) {
//							QTextLine line = layout.lineAt(i);
//							QRectF liner = line.rect();
//							QRect re1 = new QRect((int) liner.left(), (int) (crr.top() + liner.top()),
//									(int) liner.width(), (int) liner.height());
////							System.out.println(re1 +" "+left);
//							if (re1.contains(re1.left(), left)) {
//								ishas = true;
////								p.setPen(new QPen(new QColor(Qt.GlobalColor.yellow), 1, PenStyle.DotLine));
////								p.drawLine(0, left, width(), left);
//								int a = Math.abs(re1.top() - left);
//								int b = re1.bottom() - left;
//								p.setPen(new QPen(new QColor(Qt.GlobalColor.black), 1, PenStyle.DotLine));
//								if (a < b) {
//									int y1 = re1.top() - 5;
//									QPoint map = text.mapTo(this, new QPoint(0, y1));
//
//									if (!useTextbgColor)
//										p.drawLine(0, map.y(), width(), map.y());
//									else
//										p.drawLine(lineLeft, map.y(), lineRight, map.y());
//
//									bAction.drawPageNumber(p, pageCurrent, map.y(), this);
//								} else {
//									int y1 = re1.bottom() + 5;
//									QPoint map = text.mapTo(this, new QPoint(0, y1));
//									if (!useTextbgColor)
//										p.drawLine(0, map.y(), width(), map.y());
//									else
//										p.drawLine(lineLeft, map.y(), lineRight, map.y());
//
//									bAction.drawPageNumber(p, pageCurrent, map.y(), this);
//								}
//								break;
//							} else if (left < re1.top() && left > re1.top() - 15) {
//								p.setPen(new QPen(new QColor(Qt.GlobalColor.black), 1, PenStyle.DotLine));
//								ishas = true;
////								System.out.println("a");
//								int y1 = re1.top() - 5;
//								QPoint map = text.mapTo(this, new QPoint(0, y1));
//								if (!useTextbgColor)
//									p.drawLine(0, map.y(), width(), map.y());
//								else
//									p.drawLine(lineLeft, map.y(), lineRight, map.y());
//								bAction.drawPageNumber(p, pageCurrent, map.y(), this);
//								break;
//							} else if (left > re1.bottom() && left < re1.bottom() + 15) {
//								p.setPen(new QPen(new QColor(Qt.GlobalColor.black), 1, PenStyle.DotLine));
//								ishas = true;
////								System.out.println("b");
//								int y1 = re1.bottom() + 5;
//								QPoint map = text.mapTo(this, new QPoint(0, y1));
//								if (!useTextbgColor)
//									p.drawLine(0, map.y(), width(), map.y());
//								else
//									p.drawLine(lineLeft, map.y(), lineRight, map.y());
//								bAction.drawPageNumber(p, pageCurrent, map.y(), this);
//								break;
//							}
//						}
//						if (!ishas) {
////							System.out.println("c");
//							p.setPen(new QPen(new QColor(Qt.GlobalColor.black), 1, PenStyle.DotLine));
//							QPoint map = text.mapTo(this, new QPoint(0, left));
//							if (!useTextbgColor)
//								p.drawLine(0, map.y(), width(), map.y());
//							else
//								p.drawLine(lineLeft, map.y(), lineRight, map.y());
//							bAction.drawPageNumber(p, pageCurrent, map.y(), this);
//						}
//					}
//
//				}
//			}

//			boolean nobackground = this.getBooleanValueFromSettings(appInfo.noBackgroundForWritingView, "false");
//			if (!nobackground) {
//				drop.setEnabled(false);
//				textWidget.setFrameShape(QFrame.Shape.NoFrame);
//				p.fillRect(rect(), new QColor(106,143,185,50));

//				int whatColor = getIntValueFromSettings(appInfo.editorColor, "0");
//				if(whatColor == 3) {
//					p.setPen(new QPen(new QColor(Qt.GlobalColor.white), 2));
//				}else
//					p.setPen(new QPen(new QColor(51,51,51), 2));
//				
//				if(!noEditorEffect) {
//					float maximum = text.verticalScrollBar().maximum();
//					float value = text.verticalScrollBar().value();
//					float v = (value/maximum)*(textWidget.width()-2);
//					QPalette palette = textWidget.palette();
//					int y = textWidget.y()+textWidget.height()+1;
//					
//					p.drawLine(textWidget.x()+1, y, 
//							textWidget.x()+(int)v, y);
//					if(palette.color(ColorRole.Window).alpha() < 100) {
//						p.setRenderHint(RenderHint.Antialiasing);
//						QBrush bru = new QBrush(new QColor(Qt.GlobalColor.white));
//						bru.setStyle(BrushStyle.SolidPattern);
//						p.setBrush(bru);
//						int r = 10;
//						int yy = y-r/2-1;//最后的1为线宽
//						int xx = textWidget.x()-r/2+1;
//						p.drawEllipse(xx, yy, r, r);
//						p.drawEllipse(textWidget.x()+textWidget.width()-r/2, yy, r, r);
//					}
//				}

//			}
//			else {
//				if(!noEditorEffect) {
//					p.setPen(borderColor);
//					p.drawRoundRect(textWidget.x()-1, textWidget.y()-1, textWidget.width()+1, textWidget.height()+1,0,0);
//				}
//
//			}

//		}

//		else drop.setEnabled(false);
	}

	void setMarkAndStyle() {
		btext.extraSelection = new ArrayList<ExtraSelection>();
		QTextCursor tc = text.textCursor();

		if (showFind && !tc.selectedText().isEmpty()) {
			showAllSelectionInCurrentScreen(text, tc.selectedText());
			return;
		}
//		//focus
//		int focus = getIntValueFromSettings(appInfo.focusMode, "0");
//		if(!inReserach) {
//			if (focus > 0) {
//				int whatColor = getIntValueFromSettings(appInfo.editorColor, "0");
//				QColor bc = null;
//				if(black.writingView == 0
//						|| getBooleanValueFromSettings(appInfo.showPic, "false")) bc = new QColor(Qt.GlobalColor.cyan);
//				else bc = new QColor(21,21,21);
//				QColor fc = text.palette().color(ColorRole.Text);
//
//				QTextCharFormat bcf = tc.blockCharFormat();
//				bcf.setForeground(new QBrush(bc));
//				if (focus == 2) {
//					tc.movePosition(QTextCursor.MoveOperation.StartOfLine);
//				} else {
//					tc.movePosition(QTextCursor.MoveOperation.StartOfBlock);
//				}
//				tc.movePosition(QTextCursor.MoveOperation.Start, QTextCursor.MoveMode.KeepAnchor);
//				QTextEdit_ExtraSelection extstart = new QTextEdit_ExtraSelection();
//				extstart.setCursor(tc);
//				extstart.setFormat(bcf);
//				btext.extraSelection.add(extstart);
//
//				tc.setPosition(btext.pos);
//				if (focus == 2) {
//					tc.movePosition(QTextCursor.MoveOperation.EndOfLine);
//				} else {
//					tc.movePosition(QTextCursor.MoveOperation.EndOfBlock);
//				}
//				tc.movePosition(QTextCursor.MoveOperation.End, QTextCursor.MoveMode.KeepAnchor);
//				QTextEdit_ExtraSelection extend = new QTextEdit_ExtraSelection();
//				extend.setCursor(tc);
//				extend.setFormat(bcf);
//				btext.extraSelection.add(extend);
//
//				bcf.setForeground(new QBrush(fc));
//
//				tc.setPosition(btext.pos);
//				if (focus == 2) {
//					tc.select(QTextCursor.SelectionType.LineUnderCursor);
//				} else {
//					tc.movePosition(QTextCursor.MoveOperation.StartOfBlock);
//					tc.movePosition(QTextCursor.MoveOperation.EndOfBlock, QTextCursor.MoveMode.KeepAnchor);
//				}
//				QTextEdit_ExtraSelection extBlock = new QTextEdit_ExtraSelection();
//				extBlock.setFormat(bcf);
//				extBlock.setCursor(tc);
//				btext.extraSelection.add(extBlock);
//			}
//		}
//		
		// mark
		if (infoOfCurrentEditing.showMark)
			showMark();
//		
//		//title
//		if(inReserach || focus == 0) {
//			QTextCursor tcStart = text.cursorForPosition(new QPoint(0, 0));
//			QTextCursor tcEnd = text.cursorForPosition(new QPoint(0,height()));
//			do {
//				if(tcStart.blockNumber() > tcEnd.blockNumber())break;
//				tcStart.movePosition(MoveOperation.StartOfBlock);
//				int blockPos = tcStart.position();
//				
//				if(markTextData != null && treeWidgetItemOfKeyWordsFile.equals(btext.keywordListFile)) {
//					String b_str = tcStart.block().text();
//					for(TextRegion tr:markTextData) {
//						int index = 0;
//						while((index=b_str.indexOf(tr.text,index)) != -1) {
//							QTextEdit_ExtraSelection ex = new QTextEdit_ExtraSelection();
//							QTextCursor textCursor = text.textCursor();
//							int index_= index+blockPos;
//							textCursor.setPosition(index_);
//							textCursor.setPosition(index_+tr.text.length(), MoveMode.KeepAnchor);
////							System.out.println((index+blockPos)+" "+(index+b_str.length()+blockPos)+" "+tr.text);
//
//							QTextCharFormat cf = textCursor.charFormat();
////							cf.setForeground(new QBrush(new QColor(Qt.GlobalColor.blue)));
//							cf.setFontUnderline(true);
//							cf.setUnderlineColor(new QColor(Qt.GlobalColor.blue));
//							cf.setUnderlineStyle(UnderlineStyle.WaveUnderline);
//							ex.setCursor(textCursor);
//							ex.setFormat(cf);
//							btext.extraSelection.add(ex);
//							index = index+tr.text.length();
//						};
//						
//					}
//				}
//				int titleStyle = -1;
//				QTextCursor ttc = text.textCursor();
//				if(ttc.movePosition(MoveOperation.Start)) {
//					String str = ttc.block().text();
//					if(str.equals("$smartTitle")) {
//						titleStyle = 0;
//					}else if(str.equals("$noTitle")) {
////						titleStyle = 1;
//						continue;
//					}
//				}
//				int b = text.textCursor().blockNumber();
//				if(text.isReadOnly() || b != tcStart.blockNumber() || btext.doNotSetStyle) {
//					if(cheakDocument.cheakStringOnly(tcStart.block().text(),appInfo.syls)) {
//						boolean noShow = false;
//						if(titleStyle == 0) {
//							QTextCursor textCursor = new QTextCursor(tcStart);
//							if(textCursor.movePosition(MoveOperation.PreviousBlock)) {
//								if(cheakDocument.cheakStringOnly(textCursor.block().text(),appInfo.syls)) {
//									noShow = true;
//								}else {
//									textCursor.movePosition(MoveOperation.NextBlock);
//									if(textCursor.movePosition(MoveOperation.NextBlock)) {
//										if(cheakDocument.cheakStringOnly(textCursor.block().text(),appInfo.syls)) {
//											noShow = true;
//										}
//									}
//								}
//							}else {
//								if(textCursor.movePosition(MoveOperation.NextBlock)) {
//									if(cheakDocument.cheakStringOnly(textCursor.block().text(),appInfo.syls)) {
//										noShow = true;
//									}
//								}
//							}
//							
//						}
//						
//						if(!noShow) {
//							QTextEdit_ExtraSelection ex = new QTextEdit_ExtraSelection();
//							tcStart.select(SelectionType.BlockUnderCursor);
//							QTextCharFormat cf = tcStart.charFormat();
//							cf.setFontUnderline(true);
//							
//							ex.setCursor(tcStart);
//							ex.setFormat(cf);
//							btext.extraSelection.add(ex);
//						}
//					}
//				}
//			} while (tcStart.movePosition(QTextCursor.MoveOperation.NextBlock));
//		}

		text.setExtraSelections(btext.extraSelection);
	}

	void showAllSelectionInCurrentScreen(bTextEdit text, String str) {
		ArrayList<ExtraSelection> ex_al = new ArrayList<ExtraSelection>();
		QTextCursor tcStart = text.cursorForPosition(new QPoint(0, 0));
		QTextCursor tcEnd = text.cursorForPosition(new QPoint(0, height()));
		do {
			tcStart.movePosition(MoveOperation.StartOfBlock);
			int block_pos = tcStart.position();
			int index = 0;
			String b_str = tcStart.block().text();
			while ((index = b_str.indexOf(str, index)) != -1) {
				QTextCursor textCursor = text.textCursor();
				int index_ = index + block_pos;
				textCursor.setPosition(index_);
				textCursor.setPosition(index_ + str.length(), MoveMode.KeepAnchor);
				ExtraSelection ex = new ExtraSelection();
				QTextCharFormat cf = textCursor.charFormat();
				cf.setForeground(new QBrush(new QColor(Qt.GlobalColor.black)));
				cf.setBackground(new QBrush(new QColor(Qt.GlobalColor.yellow)));
				cf.setFontUnderline(true);
				cf.setUnderlineColor(new QColor(Qt.GlobalColor.green));
				ex.setCursor(textCursor);
				ex.setFormat(cf);
				ex_al.add(ex);
				index = index + str.length();
			}
			QRect c = text.cursorRect(tcStart);
			if (tcStart.blockNumber() > tcEnd.blockNumber())
				break;
		} while (tcStart.movePosition(QTextCursor.MoveOperation.NextBlock));
		text.setExtraSelections(ex_al);
		showFind = true;
	}

	void popPicWidget() {
		if (img != null && img.isVisible()) {
			if (!img.isActiveWindow()) {
				img.hide();
				img.show();
				img.setFocus();
			}
		}
	}

	public void importOldBlackProject() {
		if (!isAdmin())
			return;

		String openFileName = QFileDialog.getOpenFileName(this, "", "", "旧" + appInfo.appName + "项目 (*.bpro)").result;
		File profile = new File(openFileName);
		if (profile.exists()) {
			cfg_read_write.cfg_read(profile);
			File indexfile = new File(
					profile.getParent() + File.separator + "Settings" + File.separator + "fileindex.blaobj");
			if (indexfile.exists()) {
				ArrayList<String> index = (ArrayList) io.readObjFile(indexfile);
				File info = new File(profile.getParent() + File.separator + "Settings" + File.separator + "fileinfo");
				if (info.exists()) {
					Properties fileinfo = cfg_read_write.cfg_read(info);
					int fileindex = Integer.valueOf(this.projectInfo.getProperty(appInfo.fileIndex)).intValue();
					this.tree.setUpdatesEnabled(false);
					for (String s : index) {
						File file = new File(profile.getParent() + File.separator + "Files" + File.separator + s);
						if (file.exists()) {
							String fileshowname = fileinfo.getProperty(file.getName(), file.getName());
							int filecharcount = Integer
									.valueOf(fileinfo.getProperty(file.getName() + "ChineseCharCount", "0")).intValue();
							String text = io.readBlackFileByLine(file);
							String intofilename = fileindex + ".black";
							io.writeBlackFile(new File(this.projectFile.getParent() + File.separator + "Files"
									+ File.separator + intofilename), text, null);
							QTreeWidgetItem treeItem = getTreeItem(this.tree);
							treeItem.setText(0, fileshowname);
							fileInfo oldinfo = new fileInfo(fileshowname, intofilename);
							if (fileshowname.equals("预定义")) {
								oldinfo.isKeyWordsList = true;
								treeItem.setIcon(0, this.ico_keywordsList);
							} else {
								oldinfo.isFile = true;
								if (filecharcount == 0) {
									treeItem.setIcon(0, this.ico_file);
								} else {
									treeItem.setIcon(0, this.ico_fileOfHasText);
								}
							}
							oldinfo.charCount = filecharcount;
							treeItem.setData(1, 0, oldinfo);
							setEditable(treeItem, false);
							this.filesList.add(oldinfo);
						}
						fileindex++;
					}
					this.tree.setUpdatesEnabled(true);
					this.projectInfo.setProperty("fileindex", String.valueOf(fileindex));
					saveProjectFile();
					saveFileListFile();

					getMessageBox("导入", "导入完成！");
				}
			}
		}
	}

	public void checkUpdate() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				debugPrint("是否已经在检查更新：" + !(ba.tempData.get("checkUpdate") == null));
				if (ba.tempData.get("checkUpdate") == null) {
					yang.app.qt.black.checkUpdate checkUpdate = new checkUpdate(black.this) {

						@Override
						public void whenKnowUpdateSize() {
							// TODO Auto-generated method stub

						}

						@Override
						public void whenDownloadDone() {
							// TODO Auto-generated method stub

						}
					};
				} else
					return;
				
//				appInfo.updateFrom = "163";
//				p("检查更新(" + appInfo.updateFrom + ")...");
//				ba.getMessageFromMailBox(appInfo.host0, appInfo.username0, appInfo.password0, appInfo.port0);
//				if (hasUpdate == 0) {
//					p("没有在(" + appInfo.updateFrom + ")检查到更新，开始在(yandex)中检查...");
//					ba.getMessageFromMailBox(appInfo.host1, appInfo.username1, appInfo.password1, appInfo.port1);
//					appInfo.updateFrom = "yandex";
//				}
//				uiRun(black.this, new Runnable() {
//					public void run() {
//						if (hasUpdate == 0) {
//							p("检查更新完成(附加地址)，没有发现新版本");
//						} else if (hasUpdate == 1) {
//							p("检查更新完成(附加地址)，发现了新版本(" + appInfo.updateFrom + ")");
//							appInfo.updateFrom = "";
//							getBMessageBox("更新信息", updateinfo);
//						}
//					}
//				});
			}

		});
		t.start();
	}

	public int isWritingView() {
		Integer value = Integer.valueOf((String) this.settings.value(appInfo.isWritingView, "0"));
		return value.intValue();
	}

	public void saveWritingViewState() {
		this.settings.setValue(appInfo.isWritingView, String.valueOf(writingView));
	}

	public int onlyPart() {
		Integer value = Integer.valueOf((String) this.settings.value(appInfo.onlyAPart, "1"));
		return value.intValue();
	}

	public void setOnlyAPart(boolean ischeck) {
		int i = 0;
		if (ischeck) {
			i = 1;
		}
		this.settings.setValue(appInfo.onlyAPart, String.valueOf(i));
	}

	public void setTopKeywordsCount(int value) {
		this.settings.setValue(appInfo.topKeywordsCount, String.valueOf(value));
	}

	public int getTopKeywordsCount() {
		Integer value = Integer.valueOf((String) this.settings.value(appInfo.topKeywordsCount, "-1"));
		return value.intValue();
	}

	public int getEditorFontSize() {
		Integer value = Integer.valueOf((String) this.settings.value(appInfo.fontSize, "10"));
		return value.intValue();
	}

	public String getEditorFontName() {
		String fontname = (String) this.settings.value(appInfo.font, "宋体");
		return fontname;
	}

	public int getEditorParagraphSpaceValue() {
		int value = Integer.valueOf((String) this.settings.value(appInfo.paragraphSpace, "0")).intValue();
		return value;
	}

	public int getEditorFirstLineValue() {
		int value = Integer.valueOf((String) this.settings.value(appInfo.firstLine, "0")).intValue();
		return value;
	}

	public int getEditorLineSpaceValue() {
		int value = Integer.valueOf((String) this.settings.value(appInfo.lineHeight, "0")).intValue();
		return value;
	}

	public int getEditorTextZoomValue() {
		int value = Integer.valueOf((String) this.settings.value(appInfo.zoomValue, "0")).intValue();
		return value;
	}

	public int getEditorCaretWidthValue() {
		int value = Integer.valueOf((String) this.settings.value(appInfo.caretWidth, "1")).intValue();
		return value;
	}

	public void setEditorFontSize(int value) {
		QFont font = text.font();
//		font.setPointSize(value + this.btext.zoomvalue);
//		text.setFont(font);
		setStyleForCurrentDocument(false);
		needResetStyleOfDoc = true;
		this.settings.setValue(appInfo.fontSize, String.valueOf(value));
	}

	public void setCaretWidth(int value) {
		this.settings.setValue(appInfo.caretWidth, String.valueOf(value));
		if (text.brunnable_caret == null)
			text.setCursorWidth(value);
		else
			text.caret.setFixedWidth(value);
	}

	public void setEditorFirstLine(int value) {

//		if(value == 0) return;
		boolean issaved = this.saved;
		this.settings.setValue(appInfo.firstLine, String.valueOf(value));
		setStyleForCurrentDocument(false);
		needResetStyleOfDoc = true;

		setSaved(issaved);
	}

	public void setEditorParagraphSpace(int value) {

		boolean issaved = this.saved;
		this.settings.setValue(appInfo.paragraphSpace, String.valueOf(value));
		setStyleForCurrentDocument(false);
		needResetStyleOfDoc = true;

		setSaved(issaved);
	}

	public void setEditorLineSpace(int value) {

		boolean issaved = this.saved;
		this.settings.setValue(appInfo.lineHeight, String.valueOf(value));
		setStyleForCurrentDocument(false);
		needResetStyleOfDoc = true;

		setSaved(issaved);
	}

	public void setEditorZoomValue(int zoomvalue) {

		boolean iszoom = this.btext.zoom(zoomvalue);
		if ((iszoom) && (zoomvalue != getEditorTextZoomValue())) {
			this.settings.setValue(appInfo.zoomValue, String.valueOf(zoomvalue));
			this.zoomBox.setCurrentIndex(zoomvalue);
			setStyleForCurrentDocument(false);
			needResetStyleOfDoc = true;
		}
	}

	public void setEditorFont(QFont font) {

		font.setPointSizeF(text.font().pointSizeF());
//		font.setLetterSpacing(SpacingType.AbsoluteSpacing, 100);
//		font.setWordSpacing(-10);
//		font.setHintingPreference(HintingPreference.PreferNoHinting);
		if (!infoOfCurrentEditing.isKeyWordsList) {
			if (getBooleanValueFromSettings(appInfo.charSpace, "false"))
				font.setLetterSpacing(SpacingType.PercentageSpacing, getIntValueFromSettings(appInfo.charSpaceValue, "130"));
		}

		this.settings.setValue(appInfo.font, font.family());
		setStyleForCurrentDocument(false);
		needResetStyleOfDoc = true;

	}

	public settings showSettingsDialog() {
		settings set = new settings(this);
		set.open();
		return set;
	}

	public void find() {
		if (this.finddialog == null) {
			this.finddialog = new finddialog(this);
			this.finddialog.onlyShowHistoryOfBlackCommands = false;
			this.finddialog.show();
		}
	}

	public int getTextSelectionCount(bTextEdit text) {
		QTextCursor tc = text.textCursor();
		return tc.selectedText().length();
	}

	public QSize getTextSelectionRange(bTextEdit text) {
		QTextCursor tc = text.textCursor();
		return new QSize(tc.selectionStart(), tc.selectionEnd());
	}

	public void getItalinaName_male() {
		getItalinaName('m');
	}

	public void getItalinaName_female() {
		getItalinaName('f');
	}

	public void getEnglishName_male() {
		getEnglishName('m');
	}

	public void getEnglishName_female() {
		getEnglishName('f');
	}

	public void getItalinaName(char gender) {
		if (this.namecreator == null) {
			this.namecreator = new nameCreator(this, new File("./nameCreator"));
		}
		if (this.namecreator.isHasItalinaNameData()) {
			String[] s = this.namecreator.getItalinaName(gender);
			bInsertText(text, s[1] + "·" + s[0], -1, true);
		} else {
			getMessageBox("人名产生错误", "不存在意大利人名数据！");
		}
	}

	public void getEnglishName(char gender) {
		if (this.namecreator == null) {
			this.namecreator = new nameCreator(this, new File("./nameCreator"));
		}
		if (this.namecreator.isHasEnglishNameData()) {
			String[] s = this.namecreator.getEnglishName(gender);
			bInsertText(text, s[1] + "·" + s[0], -1, true);
		} else {
			getMessageBox("人名产生错误", "不存在英文人名数据！");
		}
	}

	public void getChineseName() {
		if (this.namecreator == null) {
			this.namecreator = new nameCreator(this, new File("./nameCreator"));
		}
		if (this.namecreator.isHasChineseNameData()) {
			String[] names = this.namecreator.getChineseNames(1);

			bInsertText(text, names[0], -1, true);

		} else {
			getMessageBox("人名产生错误", "不存在中文人名数据！");
		}
	}

	public void getChineseNames() {
		getChineseNames(20);
	}

	public void getChineseNames(int count) {
		if (this.namecreator == null) {
			this.namecreator = new nameCreator(this, new File("./nameCreator"));
		}
		if (this.namecreator.isHasChineseNameData()) {
			if ((keywords != null) && (keywords.isVisible())) {
				return;
			}
			initKeyWordsDialog();
			keywords.tree.clear();
			String[] names = this.namecreator.getChineseNames(count);
			String[] arrayOfString1;
			int j = (arrayOfString1 = names).length;
			for (int i = 0; i < j; i++) {
				String s = arrayOfString1[i];
				QTreeWidgetItem ti = getTreeItem(keywords.tree);
				ti.setText(0, s);
				TextRegion tr = new TextRegion(s, 0, 0);
				ti.setData(1, 0, tr);
				ti.setIcon(0, this.ico_names);
			}
			findview = 3;
			keywords.message.setText("20个中文人名 ");
			showDialogAtEditorCaretPos(text, keywords);
		} else {
			getMessageBox("人名产生错误", "不存在中文人名数据！");
		}
	}

	void readMarkFile() {
		QTreeWidgetItem findKeyWordsList = findKeyWordsList(this.TreeWidgetItemOfCurrentEditing);
		if (findKeyWordsList != null) {
			fileInfo in = getFileInfoByQTreeItem(findKeyWordsList);
			File markfile = getFile(in.fileName);
			if ((markfile != null) && (markfile.exists())) {
				this.marktext = readBlackFile(markfile).replaceAll("\n\n", "\n");
				this.treeWidgetItemOfKeyWordsFile = findKeyWordsList;
				String statfileName = "markstat_" + markfile.getName().replace(".black", "");
				File statfile = new File(
						this.projectFile.getParent() + File.separator + "Settings" + File.separator + statfileName);
				if (statfile.exists()) {
					ArrayList<markstat> statdata = (ArrayList) io.readObjFile(statfile);
					if (statdata != null) {
						this.markstat = statdata;
					} else {
						getMessageBox("加载数据", "加载关键词统计数据时出现错误\n存在统计文件，但读取文件失败");
					}
				}
			} else {
				getMessageBox("关键词列表", "已定义关键词列表，但未在磁盘中找到其所对应的文件");
			}
		} else {
			p("未查找到关键词列表");
//			getMessageBox("关键词列表", "未查找到关键词列表");
		}
	}

	void writeToMarkStatFile() {
		fileInfo fileInfoByQTreeItem = getFileInfoByQTreeItem(this.treeWidgetItemOfKeyWordsFile);
		File markfile = getFile(fileInfoByQTreeItem.fileName);
		if ((markfile != null) && (markfile.exists())) {
			String statfileName = "markstat_" + markfile.getName().replace(".black", "");
			File statfile = new File(
					this.projectFile.getParent() + File.separator + "Settings" + File.separator + statfileName);
			if (!statfile.exists()) {
				if (!statfile.getParentFile().exists()) {
					statfile.getParentFile().mkdir();
				}
				try {
					statfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					getMessageBox("保存关键词数据", "保存关键词调频数据时出错！");
				}
			}
			if ((this.markstat.size() > 0) && (this.markstatIsChanged)) {
				io.writeObjFile(statfile, this.markstat);
				this.markstatIsChanged = false;
			}
		}
	}

	void writeToMarkFile() {
		fileInfo in = getFileInfoByQTreeItem(this.treeWidgetItemOfKeyWordsFile);
		File markfile = getFile(in.fileName);
		if ((markfile != null) && (markfile.exists()) && (this.marktextIsChanged)) {
			saveBlackFile(markfile, this.marktext);
			this.marktextIsChanged = false;
		}
	}

	/**
	 * 添加关键词到关键词列表 此方法先检查关键词列表中是否存在要添加的关键词，如果存在则什么也不做，如果不存在则添加 此方法不会删除已存在关键词
	 * 即便添加成功，此方法也不会给出提示
	 * 
	 * @param str
	 */
	void addTextToMarkFile_(String str) {
		if (this.marktext == null) {
			readMarkFile();
			if (this.marktext == null) {
				return;
			}
		}
		if (this.markTextData == null) {
			getMarkFileText();
		}
		String newstr = str.replaceAll("\n", "");
		for (TextRegion tr : this.markTextData) {
			if (cheakDocument.subString(tr.text, appInfo.keywordsSeparator)[0].equals(newstr)) {
				return;
			}
		}

		if (!this.marktext.equals("")) {
			changeMarktext(this.marktext + "\n" + newstr);
		} else {
			changeMarktext(newstr);
		}
		getMarkFileText();

	}

	/**
	 * 添加关键词到关键词列表 如果关键词列表里已经存在要添加的关键词，则会将其从关键词列表中删除，如果没有则添加
	 * 
	 * @param str
	 */
	void addTextToMarkFile(String str) {
		if (this.marktext == null) {
			readMarkFile();
			if (this.marktext == null) {
				return;
			}
		}
		if (this.markTextData == null) {
			getMarkFileText();
		}
		String newstr = str.replaceAll("\n", "");
		String newtext = null;
		boolean ishas = false;
		for (TextRegion tr : this.markTextData) {
			if (cheakDocument.subString(tr.text, appInfo.keywordsSeparator)[0].equals(newstr)) {
				newtext = this.marktext.replaceAll(tr.text, "");
				ishas = true;
				break;
			}
		}
		if (ishas) {
			changeMarktext(newtext);
			getMessageBox("从关键词列表中删除词汇", "已将所选词汇从关键词列表中删除");
		} else {
			if (!this.marktext.equals("")) {
				changeMarktext(this.marktext + "\n" + newstr);
			} else {
				changeMarktext(newstr);
			}
			getMessageBox("添加词汇到关键词列表", "已将所选词汇加入关键词列表");
		}
		getMarkFileText();
	}

	void changeMarktext(String newtext) {
		this.marktext = newtext.replaceAll("\n\n", "\n");

		dataForOpenedFile findInOpenedFiles = findInOpenedFiles(
				getFileInfoByQTreeItem(this.treeWidgetItemOfKeyWordsFile).fileName);
		if (findInOpenedFiles != null) {
			if (findInOpenedFiles.editor != null) {
				findInOpenedFiles.editor = null;
			}
			findInOpenedFiles.content = this.marktext;
		}
		this.marktextIsChanged = true;
	}

	QMessageBox getMessageBox(String title, String message, boolean onTop) {
		speakText(message, -1);
		QMessageBox qMessageBox = getMessageBoxNotShow(title, message);
		if (onTop) {
			Qt.WindowFlags windowf = qMessageBox.windowFlags();
			windowf.set(new Qt.WindowType[] { Qt.WindowType.WindowStaysOnTopHint });
			qMessageBox.setWindowFlags(windowf);
		}
		qMessageBox.setIcon(QMessageBox.Icon.NoIcon);
		qMessageBox.show();
		return qMessageBox;
	}

	public static QMessageBox getMessageBox(QWidget parent, String title, String message) {

		QMessageBox qMessageBox = new QMessageBox(parent);

		qMessageBox.setIcon(QMessageBox.Icon.NoIcon);
		qMessageBox.setWindowTitle(title);
		qMessageBox.setText(message);
		qMessageBox.show();
		return qMessageBox;
	}

	QMessageBox getMessageBox(String title, String message, boolean onTop, QIcon icon) {
		speakText(message, -1);

		QMessageBox qMessageBox = getMessageBoxNotShow(title, message);

		if (onTop) {
			Qt.WindowFlags windowf = qMessageBox.windowFlags();
			windowf.set(new Qt.WindowType[] { Qt.WindowType.WindowStaysOnTopHint });
			qMessageBox.setWindowFlags(windowf);
		}
		qMessageBox.setIconPixmap(icon.pixmap(100));
		if (!this.execCommandAndQuiet) {
			qMessageBox.show();
		}
		return qMessageBox;
	}

	QMessageBox getMessageBox(String title, String message, boolean onTop, QIcon icon, boolean hideOnActive) {
		speakText(message, -1);

		final QMessageBox qMessageBox = getMessageBoxNotShow(title, message);
		if (hideOnActive) {
			qMessageBox.installEventFilter(new QObject() {
				public boolean eventFilter(QObject arg__1, QEvent arg__2) {
					if ((arg__1.equals(qMessageBox)) && (arg__2.type() == QEvent.Type.MouseButtonPress)) {
						qMessageBox.close();
					}
					return super.eventFilter(arg__1, arg__2);
				}
			});
		}
		if (onTop) {
			Qt.WindowFlags windowf = qMessageBox.windowFlags();
			windowf.set(new Qt.WindowType[] { Qt.WindowType.WindowStaysOnTopHint });
			qMessageBox.setWindowFlags(windowf);
		}
		qMessageBox.setIconPixmap(icon.pixmap(100));

		qMessageBox.show();
		return qMessageBox;
	}

	QMessageBox getMessageBox(String title, String message) {
		speakText(message, -1);

		QMessageBox qMessageBox = null;
		if ((this.finddialog != null) && (this.finddialog.isVisible())) {
			qMessageBox = new QMessageBox(this.finddialog);
		} else {
			qMessageBox = new QMessageBox(this);
		}

		qMessageBox.setIcon(QMessageBox.Icon.NoIcon);
		qMessageBox.setWindowTitle(title);
		qMessageBox.setText(message);
//		blackParentWindowForQwidget(qMessageBox);
		qMessageBox.show();
		return qMessageBox;
	}

	QMessageBox getMessageBoxNotShow(String title, String message) {
		speakText(message, -1);

		QMessageBox qMessageBox = null;
		if ((this.finddialog != null) && (this.finddialog.isVisible())) {
			qMessageBox = new QMessageBox(this.finddialog);
		} else {
			qMessageBox = new QMessageBox(this);
		}

		qMessageBox.setIcon(QMessageBox.Icon.NoIcon);
		qMessageBox.setWindowTitle(title);
		qMessageBox.setText(message);
		return qMessageBox;
	}

	int getMessageBoxWithYesNO(String title, String message, String buttonYesText, String buttonNoText,
			QMessageBox.Icon ico, int defaultButton) {
		speakText(message, -1);

		QMessageBox box = getMessageBoxNotShow(title, message);
		QPushButton reject = box.addButton(buttonNoText, QMessageBox.ButtonRole.RejectRole);
		QPushButton accept = box.addButton(buttonYesText, QMessageBox.ButtonRole.AcceptRole);
		if (defaultButton == 0) {
			box.setDefaultButton(reject);
		} else if (defaultButton == 1) {
			box.setDefaultButton(accept);
		}
		if (ico != null) {
			box.setIcon(ico);
		}
		return box.exec();
	}

	int getMessageBoxWithYesNO(String title, String message, String buttonYesText, String buttonNoText, QIcon ico,
			int defaultButton) {
		speakText(message, -1);

		QMessageBox box = getMessageBoxNotShow(title, message);
		QPushButton reject = box.addButton(buttonNoText, QMessageBox.ButtonRole.RejectRole);
		QPushButton accept = box.addButton(buttonYesText, QMessageBox.ButtonRole.AcceptRole);
		if (defaultButton == 0) {
			box.setDefaultButton(reject);
		} else if (defaultButton == 1) {
			box.setDefaultButton(accept);
		}
		if (ico != null) {
			box.setIconPixmap(ico.pixmap(100));
		}
		return box.exec();
	}

	int getMessageBoxWithYesNO(String title, String message, String buttonYesText, String buttonNoText, QIcon ico,
			int defaultButton, boolean onTop) {
		speakText(message, -1);

		QMessageBox box = getMessageBoxNotShow(title, message);
		QPushButton reject = box.addButton(buttonNoText, QMessageBox.ButtonRole.RejectRole);
		QPushButton accept = box.addButton(buttonYesText, QMessageBox.ButtonRole.AcceptRole);
		if (onTop) {
			Qt.WindowFlags windowf = box.windowFlags();
			windowf.set(new Qt.WindowType[] { Qt.WindowType.WindowStaysOnTopHint });
			box.setWindowFlags(windowf);
		}
		if (defaultButton == 0) {
			box.setDefaultButton(reject);
		} else if (defaultButton == 1) {
			box.setDefaultButton(accept);
		}
		if (ico != null) {
			box.setIconPixmap(ico.pixmap(100));
		}
		return box.exec();
	}

	void ishasInMarkdata() {
		if (this.markstat == null) {
			return;
		}
		Iterator<markstat> it_stat = this.markstat.iterator();
		while (it_stat.hasNext()) {
			String str = ((markstat) it_stat.next()).text;
			boolean ishas = false;
			Iterator<TextRegion> it_mark = this.markTextData.iterator();
			while (it_mark.hasNext()) {
				String text = ((TextRegion) it_mark.next()).text;
				if (text.equals(str)) {
					ishas = true;
					break;
				}
			}
			if (!ishas) {
				it_stat.remove();
			}
		}
	}

	void deleteFromMarkStatData(String text) {
		for (int i = 0; i < this.markstat.size(); i++) {
			markstat markst = (markstat) this.markstat.get(i);
			if (markst.text.equals(text)) {
				this.markstat.remove(i);
				break;
			}
		}
		this.markstatIsChanged = true;
	}

	hasinfo markstatIshas(String text) {
		boolean ishas = false;
		int index = 0;
		for (int i = 0; i < this.markstat.size(); i++) {
			if (text.equals(((markstat) this.markstat.get(i)).text)) {
				ishas = true;
				index = i;
				break;
			}
		}
		return new hasinfo(ishas, index);
	}

	void setTopOnMarkstat(String text) {
		for (int i = 0; i < this.markstat.size(); i++) {
			if (((markstat) this.markstat.get(i)).text.equals(text)) {
				int stat = ((markstat) this.markstat.get(i)).count;
				this.markstat.remove(i);
				this.markstat.add(new markstat(text, stat));
				return;
			}
		}
	}

	ArrayList<TextRegion> resetIndex(ArrayList<TextRegion> list) {
		ArrayList<TextRegion> al = new ArrayList();
		while (list.size() > 0) {
			int maxindex = 0;
			int max = 0;
			for (int i = 0; i < list.size(); i++) {
				if (((TextRegion) list.get(i)).end > max) {
					max = ((TextRegion) list.get(i)).end;
					maxindex = i;
				}
			}
			al.add((TextRegion) list.get(maxindex));
			list.remove(maxindex);
		}
		return al;
	}

	void setindexOfMarkstat() {
		this.markstatIsChanged = true;
		ArrayList<markstat> al = new ArrayList();
		while (this.markstat.size() > 0) {
			int maxindex = 0;
			int max = 0;
			for (int i = 0; i < this.markstat.size(); i++) {
				if (((markstat) this.markstat.get(i)).count > max) {
					max = ((markstat) this.markstat.get(i)).count;
					maxindex = i;
				}
			}
			al.add((markstat) this.markstat.get(maxindex));
			this.markstat.remove(maxindex);
		}
		this.markstat = al;
	}

	public static String[] checkName(String name) {
		String[] names = new String[2];
		int index;
		if ((index = name.indexOf("·")) == -1) {
			if (name.length() == 2) {
				names[0] = String.valueOf(name.charAt(0));
				names[1] = String.valueOf(name.charAt(1));
			} else if (name.length() == 3) {
				names[0] = String.valueOf(name.charAt(0));
				names[1] = name.substring(1, 3);
			}
		} else {
			names[0] = name.substring(0, index);
			names[1] = name.substring(name.lastIndexOf("·") + 1, name.length());
		}
		return names;
	}

	public static int lastIndexOf(String name, String in) {
		int max = -1;
		if (name.indexOf('·') != -1) {
			String firstname = name.substring(name.lastIndexOf('·') + 1, name.length());
			String lastname = name.substring(0, name.indexOf('·'));
			int all = in.lastIndexOf(name);
			int first = in.lastIndexOf(firstname);
			int last = in.lastIndexOf(lastname);
			if ((last == all) && (first == all + lastname.length() + 1)) {
				max = all;
			} else {
				int[] index = { all, first, last };
				for (int i = 0; i < index.length; i++) {
					if (index[i] > max) {
						max = index[i];
					}
				}
			}
		}
		return max;
	}

	/**
	 * 不要被方法名骗了，这个方法并不是通过统计数据给关键词排序 而是通过分析关键词在当前文档中的位置给关键词排序
	 * 
	 * @return
	 */
	List<TextRegion> setindexOfMarkstatBy() {
		QTextCursor tc = text.textCursor();
		if (tc.position() > 0) {
			tc.movePosition(QTextCursor.MoveOperation.Start, QTextCursor.MoveMode.KeepAnchor);
			String s = tc.selectedText();
			String text = null;
			if (onlyPart() == 0) {
				text = s;
			} else if (s.length() > 10000) {
				text = s.substring(s.length() - 10000, s.length());
			} else {
				text = s;
			}
			List<TextRegion> al = this.markTextData;
			List<TextRegion> al_new = new ArrayList();
			while (al.size() > 0) {
				int max = 0;
				int maxindex = 0;
				for (int i = 0; i < al.size(); i++) {
					String str = cheakDocument.subString(((TextRegion) al.get(i)).text, appInfo.keywordsSeparator)[0];
					int index = 0;
					if (str.indexOf('·') != -1) {
						index = lastIndexOf(str, text);
					} else {
						index = text.lastIndexOf(str);
					}
					if (index > max) {
						max = index;
						maxindex = i;
					}
				}
				TextRegion tr = al.get(maxindex);
				tr.showname = tr.text;
				tr.filename = null;
				String[] subString = cheakDocument.subString(tr.text, appInfo.keywordsSeparator);
				if (subString.length == 2 && !subString[1].isEmpty()) {
					tr.showname = subString[0];
					tr.filename = subString[1];
				}
				al_new.add(tr);
				al.remove(maxindex);
			}
			this.markTextData = al_new;
			return al_new;
		}
		return null;
	}

	public void checkIsHasDiffKeyWordsList() {
		if (this.treeWidgetItemOfKeyWordsFile == null) {
			return;
		}
		if (findKeyWordsList(this.TreeWidgetItemOfCurrentEditing) == null) {
			return;
		}
		if (!findKeyWordsList(this.TreeWidgetItemOfCurrentEditing).equals(this.treeWidgetItemOfKeyWordsFile)) {
			QMessageBox me = new QMessageBox(this);
			me.setWindowTitle("不同的关键词列表");
			me.setIcon(QMessageBox.Icon.Question);
			me.setText("发现不同的关键词列表，是否切换？");
			QPushButton apply = me.addButton(QMessageBox.StandardButton.Ok);
			apply.setText("切换");
			QPushButton cancel = me.addButton(QMessageBox.StandardButton.Cancel);
			cancel.setText("取消");

			apply.pressed.connect(this, "reLoadKeywordsList()");
			me.show();
		}
	}

	public void reLoadKeywordsList() {
		if (this.marktextIsChanged) {
			writeToMarkFile();
		}
		if (this.markstatIsChanged) {
			writeToMarkStatFile();
		}
		readMarkFile();
		getMarkFileText();
	}

	boolean findString(String text, String str) {
		if (cheakDocument.findString(text, str)) {
			return true;
		}
		if (str.indexOf("·") != -1) {
			String firstname = str.substring(str.lastIndexOf('·') + 1, str.length());
			String lastname = str.substring(0, str.indexOf('·'));
			if (text.indexOf(firstname) != -1) {
				return true;
			}
			if (text.indexOf(lastname) != -1) {
				return true;
			}
		}
		return false;
	}

	static String getCharByNumber(int number) {
		if ((number < 0) || (number >= 26)) {
			return "";
		}
		char[] c = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
				'u', 'v', 'w', 'x', 'y', 'z' };
		char ch = c[number];
		String up = String.valueOf(ch).toUpperCase();
		return up + ". ";
	}

	static int getNumberByChar(String str) {
		int number = -1;
		if ((str.length() == 0) || (str.length() > 1)) {
			return number;
		}
		char ch = str.charAt(0);
		char[] c = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
				'u', 'v', 'w', 'x', 'y', 'z' };
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ch) {
				number = i;
			}
		}
		return number;
	}

	void showKeyWords() {
		if ((keywords != null) && (keywords.isVisible())) {
			return;
		}
		if (this.marktext == null) {
			readMarkFile();
		}
		if (this.marktext == null) {
			return;
		}
		if (this.markTextData == null) {
			getMarkFileText();
		}
		initKeyWordsDialog();
		keywords.tree.UsedToProjectPanelOrKeywordsList = 1;
		if (keywords.tree.topLevelItemCount() > 0) {
			keywords.tree.clear();
		}
		int index = 0;
		List<TextRegion> tr_no = new ArrayList();
		List<TextRegion> tr_line = new ArrayList();
		List<TextRegion> tr_doc = new ArrayList();

		QTextCursor tc1 = text.textCursor();
		tc1.movePosition(QTextCursor.MoveOperation.StartOfBlock, QTextCursor.MoveMode.KeepAnchor);
		String line = tc1.selectedText();
		QTextCursor tc2 = text.textCursor();
		tc2.movePosition(QTextCursor.MoveOperation.Start, QTextCursor.MoveMode.KeepAnchor);
		String doc = tc2.selectedText();
		hasinfo info = null;
		List<TextRegion> al = null;
		if ((this.LineAndDocFirst_keywords) || (getTopKeywordsCount() == -1)
				|| (this.markstat.size() < getTopKeywordsCount())) {
			al = setindexOfMarkstatBy();
		}
		Iterator<TextRegion> it_tr = null;
		if (al != null) {
			it_tr = al.iterator();
		} else {
			it_tr = this.markTextData.iterator();
		}
		while (it_tr.hasNext()) {
			TextRegion tr = (TextRegion) it_tr.next();
			String[] subString = cheakDocument.subString(tr.text, appInfo.keywordsSeparator);
			tr.showname = subString[0];
			tr.filename = subString[1];
			if ((!this.LineAndDocFirst_keywords) && ((info = markstatIshas(tr.text)).ishas)) {
				((markstat) this.markstat.get(info.index)).visible = true;
			} else if ((this.LineAndDocFirst_keywords) || (getTopKeywordsCount() == -1)
					|| (this.markstat.size() < getTopKeywordsCount())) {
				if (findString(line, tr.showname)) {
					tr_line.add(tr);
					if ((this.LineAndDocFirst_keywords) && ((info = markstatIshas(tr.text)).ishas)) {
						((markstat) this.markstat.get(info.index)).visible = false;
					}
				} else if (findString(doc, tr.showname)) {
					tr_doc.add(tr);
					if ((this.LineAndDocFirst_keywords) && ((info = markstatIshas(tr.text)).ishas)) {
						((markstat) this.markstat.get(info.index)).visible = false;
					}
				} else if ((this.LineAndDocFirst_keywords) && ((info = markstatIshas(tr.text)).ishas)) {
					((markstat) this.markstat.get(info.index)).visible = true;
				} else {
					tr_no.add(tr);
				}
			} else {
				tr_no.add(tr);
			}
		}
		if (!this.LineAndDocFirst_keywords) {
			itemOfStat(index);
			itemOfLineAndDoc(tr_line, tr_doc, index);
			itemOfNo(tr_no, index);
		} else {
			itemOfLineAndDoc(tr_line, tr_doc, index);
			itemOfStat(index);
			itemOfNo(tr_no, index);
		}
		keywords.message.setText(this.markTextData.size() + "条");
		keywords.statusbar.showMessage("Tab:|下移Num1:上移|Del:取消置顶 ");
		showDialogAtEditorCaretPos(text, keywords);
	}

	void itemOfNo(List tr_no, int index) {
		Iterator<TextRegion> it_no = tr_no.iterator();
		while (it_no.hasNext()) {
			TextRegion trno = (TextRegion) it_no.next();
			QTreeWidgetItem ti = getTreeItem(keywords.tree);
			ti.setIcon(0, this.ico_keywordNo);

			String str = null;
			if (trno.filename.equals("")) {
				str = trno.showname;
			} else {
				str = trno.showname + " (" + trno.filename + ")";
			}
			ti.setText(0, getCharByNumber(keywords.tree.topLevelItemCount() - 1) + str);
			ti.setData(1, 0, trno);
			ti.setData(1, 1, Integer.valueOf(index));
			index++;
		}
	}

	void itemOfLineAndDoc(List tr_line, List tr_doc, int index) {
		Iterator<TextRegion> it_line = tr_line.iterator();
		while (it_line.hasNext()) {
			TextRegion trline = (TextRegion) it_line.next();
			QTreeWidgetItem ti = getTreeItem(keywords.tree);
			ti.setIcon(0, this.ico_keywordLine);

			ti.setForeground(0, new QBrush(new QColor(Qt.GlobalColor.darkBlue)));
			String str = null;
			if (trline.filename.equals("")) {
				str = trline.showname;
			} else {
				str = trline.showname + " (" + trline.filename + ")";
			}
			ti.setText(0, getCharByNumber(keywords.tree.topLevelItemCount() - 1) + str);
			ti.setData(1, 0, trline);
			ti.setData(1, 1, Integer.valueOf(index));
			ti.setData(3, 3, "(当前段落)");
			index++;
		}
		Iterator<TextRegion> it_doc = tr_doc.iterator();
		while (it_doc.hasNext()) {
			TextRegion trdoc = (TextRegion) it_doc.next();
			QTreeWidgetItem ti = getTreeItem(keywords.tree);
			ti.setIcon(0, this.ico_keywordDoc);

			ti.setForeground(0, new QBrush(new QColor(Qt.GlobalColor.darkGreen)));
			ti.setData(1, 0, trdoc);
			ti.setData(1, 1, Integer.valueOf(index));
			ti.setData(3, 3, "(当前文档)");

			String str = null;
			if (trdoc.filename.equals("")) {
				str = trdoc.showname;
			} else {
				str = trdoc.showname + " (" + trdoc.filename + ")";
			}
			ti.setText(0, getCharByNumber(keywords.tree.topLevelItemCount() - 1) + str);

			index++;
		}
	}

	void itemOfStat(int index) {
		if (this.markstat.size() > 0) {
			markstat stat = (markstat) this.markstat.get(this.markstat.size() - 1);
			if (stat.visible) {
				TextRegion trstat = new TextRegion(stat.text, 0, 0);

				QTreeWidgetItem ti = getTreeItem(keywords.tree);
				ti.setIcon(0, this.ico_keywordFirstTop);
				if ((stat.count > 1) && (stat.count <= 5)) {
					ti.setBackground(0, new QBrush(new QColor(250, 254, 90)));
				} else if ((stat.count > 5) && (stat.count <= 10)) {
					ti.setBackground(0, new QBrush(new QColor(249, 254, 56)));
				} else if ((stat.count > 10) && (stat.count <= 20)) {
					ti.setBackground(0, new QBrush(new QColor(248, 254, 10)));
				} else if (stat.count > 20) {
					ti.setBackground(0, new QBrush(new QColor(191, 197, 1)));
				} else {
					ti.setBackground(0, new QBrush(new QColor(251, 254, 126)));
				}
				String[] subString = cheakDocument.subString(trstat.text, appInfo.keywordsSeparator);
				String str = null;
				if (subString[1].equals("")) {
					str = subString[0];
				} else {
					str = subString[0] + " (" + subString[1] + ")";
				}
				ti.setText(0, getCharByNumber(keywords.tree.topLevelItemCount() - 1) + str);
				ti.setData(1, 0, trstat);
				ti.setData(1, 1, Integer.valueOf(index));
				ti.setData(3, 3, "(上次所选)(" + ((markstat) this.markstat.get(this.markstat.size() - 1)).count + ")");
				index++;
			}
		}
		if (this.markstat.size() > 1) {
			for (int i = this.markstat.size() - 2; i >= 0; i--) {
				markstat stat = (markstat) this.markstat.get(i);
				if (stat.visible) {
					TextRegion trstat = new TextRegion(((markstat) this.markstat.get(i)).text, 0, 0);
					QTreeWidgetItem ti = getTreeItem(keywords.tree);
					ti.setIcon(0, this.ico_keywordTop);
					if ((stat.count > 1) && (stat.count <= 5)) {
						ti.setBackground(0, new QBrush(new QColor(250, 254, 90)));
					} else if ((stat.count > 5) && (stat.count <= 10)) {
						ti.setBackground(0, new QBrush(new QColor(249, 254, 56)));
					} else if ((stat.count > 10) && (stat.count <= 20)) {
						ti.setBackground(0, new QBrush(new QColor(248, 254, 10)));
					} else if (stat.count > 20) {
						ti.setBackground(0, new QBrush(new QColor(191, 197, 1)));
					} else {
						ti.setBackground(0, new QBrush(new QColor(251, 254, 126)));
					}
					String[] subString = cheakDocument.subString(trstat.text, appInfo.keywordsSeparator);
					String str = null;
					if (subString[1].equals("")) {
						str = subString[0];
					} else {
						str = subString[0] + " (" + subString[1] + ")";
					}
					ti.setText(0, getCharByNumber(keywords.tree.topLevelItemCount() - 1) + str);
					ti.setData(1, 0, trstat);
					ti.setData(1, 1, Integer.valueOf(index));
					ti.setData(3, 3, "(" + ((markstat) this.markstat.get(i)).count + ")");

					index++;
				}
			}
		}
	}

	public int getKeywordsDialogWidth() {
		int value = Integer
				.valueOf((String) this.settings.value(appInfo.keywordsWidth, ba.desktopGeometry(true).width() + ""))
				.intValue();
		return value;
	}

	public int getKeywordsDialogHeight() {
		int value = Integer
				.valueOf((String) this.settings.value(appInfo.keywordsHeight, ba.desktopGeometry(true).height() + ""))
				.intValue();
		return value;
	}

	public void setKeywordsDialogWidth(int value) {
		this.settings.setValue(appInfo.keywordsWidth, String.valueOf(value));
	}

	public void setKeywordsDialogHeight(int value) {
		this.settings.setValue(appInfo.keywordsHeight, String.valueOf(value));
	}

	void showDialogForFindLine() {
		if (keywordsForFindLine.tree.topLevelItemCount() == 0) {
			getMessageBox("检索文件", "没有检索到任何结果");
			return;
		}
//		this.findBox.installEventFilter(keywordsForFindLine);
		int w = ba.desktopGeometry(true).width() / 2;
		int h = ba.desktopGeometry(true).height() - 100;
		int x = 0;
		int y = 0;
//		if(findBox.isVisible()) {
//			QPoint pos = this.findBox.pos();
//			QPoint mapToGlobal = this.toolbar.mapToGlobal(pos);
//			QDesktopWidget desk = new QDesktopWidget();
//			if (mapToGlobal.x() + w > desk.width()) {
//				x = mapToGlobal.x() - (mapToGlobal.x() + w - desk.width()) - 5;
//			} else {
//				x = mapToGlobal.x();
//			}
//			keywordsForFindLine.setGeometry(x, mapToGlobal.y() + this.findBox.height() + 5, w, h);
//		}
		x = (ba.desktopGeometry(true).width() / 2) - (w / 2);
		y = 50;
		keywordsForFindLine.setGeometry(x, y, w, h);

		keywordsForFindLine.show();
	}

	void showDialogAtCenter(keyWords keywords) {
		if (keywords.tree.topLevelItemCount() == 0) {
			p("没有信息可显示");
			return;
		}
		text.installEventFilter(keywords);
//
//		int whatColor = getIntValueFromSettings(appInfo.editorColor, "0");
//		if (whatColor == 2 && !getBooleanValueFromSettings(appInfo.showPic, "false")) {
//			QColor bg = new QColor(116, 112, 112);
//			keywords.setStyleSheet("color:black; background:" + bg.name() + ";");
//		}

		keywords.show();

		QDesktopWidget desk = new QDesktopWidget();
		QRect g = desk.availableGeometry();
		int w = getAdjustKeywordsTreeWidth(g.width() / 2, keywords);
		int setW = 0;
		if (w > (setW = g.width() / 2)) {
			w = setW;
		}
		if (findview == 4) {
			w += text.font().pointSize() * 7;
		}
		int h = getAdjustKeyWordsTreeHeight(height() / 2, keywords);
		int x = x() + (width() - w) / 2;
		int y = y() + (height() - h) / 2;
		keywords.setGeometry(x, y, w, h);
	}

	void showDialogAtLeftBottom(bTextEdit text, keyWords keywordsDialog) {
		if (keywordsDialog.tree.topLevelItemCount() == 0) {
			return;
		}
		text.installEventFilter(keywordsDialog);
		this.btext.scroll();
		int whatColor = getIntValueFromSettings(appInfo.editorColor, "0");

		if (writingView == 0 || whatColor < 2)
			keywordsDialog.tree.setStyleSheet("background:transparent;color:white");
		else if (whatColor == 2) {
			if (!getBooleanValueFromSettings(appInfo.LightOrDark, "false")) {
				keywordsDialog.tree.setStyleSheet("background:transparent;color:yellow;");
			} else {
				keywordsDialog.tree.setStyleSheet("background:transparent;color:white;");
			}
		} else
			keywordsDialog.tree.setStyleSheet("background:transparent;color:white;");

		keywordsDialog.translucentBackgound = true;

		keywordsDialog.show();

		QRect cursorRect = text.cursorRect();
		QFont f = keywordsDialog.tree.font();
		f.setPointSize(text.font().pointSize());
		QFontMetrics fm = new QFontMetrics(f);
		int h = keywordsDialog.tree
				.visualItemRect(keywordsDialog.tree.topLevelItem(keywordsDialog.tree.topLevelItemCount() - 1)).height()
				* keywordsDialog.tree.topLevelItemCount();

		QPoint map = text.mapToGlobal(new QPoint(cursorRect.x(), cursorRect.y()));
//		int x = map.x();
//		int y = map.y() - h - 10;

		QDesktopWidget desk = new QDesktopWidget();
		int x = 0;
		int y = 0;
		if (writingView == 0)
			y = desk.availableGeometry().y() + desk.availableGeometry().height() - h;
		else
			y = desk.geometry().height() - h;
		int w = getAdjustKeywordsTreeWidth(desk.width(), keywords);
		int width = desk.rect().width();
		int height = desk.rect().height();
//		if (x + w > width) {
//			x -= w;
//		}
		keywordsDialog.setGeometry(x, y, w, h);
		desk.dispose();
	}

	void showDialogAtEditorCaretPosTop(bTextEdit text, keyWords keywordsDialog) {
		if (keywordsDialog.tree.topLevelItemCount() == 0) {
			return;
		}
		text.installEventFilter(keywordsDialog);
		this.btext.scroll();
		keywordsDialog.show();

		QRect cursorRect = text.cursorRect();
		QFont f = keywordsDialog.tree.font();
		f.setPointSize(text.font().pointSize());
		QFontMetrics fm = new QFontMetrics(f);
		int h = keywordsDialog.tree
				.visualItemRect(keywordsDialog.tree.topLevelItem(keywordsDialog.tree.topLevelItemCount() - 1)).height();

		int w = fm.width(keywordsDialog.tree.topLevelItem(keywordsDialog.tree.topLevelItemCount() - 1).text(0)) + 10;

		QPoint map = text.mapToGlobal(new QPoint(cursorRect.x(), cursorRect.y()));
		int x = map.x();
		int y = map.y() - h - 10;

		QDesktopWidget desk = new QDesktopWidget();
//		int x = 0;
//		int y = desk.availableGeometry().y()+desk.availableGeometry().height()-h;
		int width = desk.rect().width();
		int height = desk.rect().height();
		if (x + w > width) {
			x -= w;
		}
		keywordsDialog.setGeometry(x, y, w, h);
		desk.dispose();
	}

	int getAdjustKeyWordsTreeHeight(int max, keyWords dig) {
		int h = 0;
//		dig.show();
		for (int i = 0; i < dig.tree.topLevelItemCount(); i++) {
			QTreeWidgetItem item = dig.tree.topLevelItem(i);
			QRect r = dig.tree.visualItemRect(item);
			h += r.height();
		}
		if (h > max) {
			h = max;
		}
		return h;
	}

	int getAdjustKeywordsTreeWidth(int max, keyWords dig) {
		int w = 0;
		QFont font = dig.tree.font();
//		font.setPointSize(text.font().pointSize());
		for (int i = 0; i < dig.tree.topLevelItemCount(); i++) {
			QTreeWidgetItem item = dig.tree.topLevelItem(i);
			QFontMetrics fm = new QFontMetrics(font);
			Object data = item.data(3, 3);
			int width = 0;
			if (data != null) {
				width = fm.width(item.text(0) + " " + data);
			} else
				width = fm.width(item.text(0));
			if (width > w) {
				w = width;
			}
		}
		w = w + 20;
		if (w > max) {
			w = max;
			return w;
		} else
			return w;
	}

	void showDialogAtEditorCaretPos(bTextEdit text, keyWords keywordsDialog) {
		if (keywordsDialog.tree.topLevelItemCount() == 0) {
			p("没有信息可显示");
			return;
		}
		text.installEventFilter(keywordsDialog);
//		keywordsDialog.translucentBackgound = true;
//		keywordsDialog.tree.setStyleSheet("background:transparent;color:white;");
		keywordsDialog.show();

		QRect cursorRect = text.cursorRect();
		QPoint map = text.mapToGlobal(new QPoint(cursorRect.x(), cursorRect.y()));
		int x = map.x();
		int y = map.y() + cursorRect.height();
		int w = 0;
		QDesktopWidget desk = new QDesktopWidget();
		int width = desk.availableGeometry().width();
		int height = desk.availableGeometry().height();

		if ((findview == 0) || (findview == 3) || findview == 5) {
			w = getAdjustKeywordsTreeWidth(width / 2, keywords);
		} else {
			w = text.width() / 2;
		}
		int h = getAdjustKeyWordsTreeHeight(text.height() / 2 - statusBar().height(), keywords);

		if (x + w > width) {
			x -= w;
		}
		if (y + h > height) {
			y = map.y() - h;
		}
		desk.dispose();
		keywordsDialog.setGeometry(x, y, w, h);
	}

	void bInsertText(bTextEdit textedit, String text, int pos, boolean isSelection) {
		if (pos != -1) {
			QTextCursor tc = textedit.textCursor();
			tc.setPosition(pos);

			tc.insertText(text);
			textedit.setTextCursor(tc);
			if (isSelection) {
				tc.setPosition(pos, QTextCursor.MoveMode.MoveAnchor);
				tc.setPosition(pos + text.length(), QTextCursor.MoveMode.KeepAnchor);
				textedit.setTextCursor(tc);
			}
		} else {
			QTextCursor tc = textedit.textCursor();
			int p = tc.position();
			tc.insertText(text);
			if (isSelection) {
				int p_end = tc.position();
				tc.setPosition(p, QTextCursor.MoveMode.MoveAnchor);
				tc.setPosition(p_end, MoveMode.KeepAnchor);
				textedit.setTextCursor(tc);

				// 貌似在调用方法前就清除了选择
			}
		}
		textedit.updateCaret(2);
	}

	public void addtomarkstat(String text) {
		if (markstat == null)
			return;
		hasinfo info = markstatIshas(text);
		if (info.ishas) {
			((markstat) this.markstat.get(info.index)).count += 1;

			setTopOnMarkstat(text);
		} else {
			this.markstat.add(new markstat(text, 1));
		}
		this.markstatIsChanged = true;
	}

	void initKeyWordsDialog() {

		if (keywords == null) {
			keywords = new keyWords(this, text) {
				public void whenSubmit(QTreeWidgetItem qt) {
					close();
					if (black.findview == 0) {
						TextRegion tr = (TextRegion) qt.data(1, 0);
						black.this.bInsertText(this.text,
								cheakDocument.subString(tr.text, appInfo.keywordsSeparator)[0], -1, false);
						// 添加分词出来的条目到关键词列表
						if (getBooleanValueFromSettings(appInfo.autoSaveInputOfAutoSegment, "true")) {
							addTextToMarkFile_(tr.text);
							if ((tr.describe != null) && (!tr.describe.equals(""))) {
								black.this.addtomarkstat(tr.describe);
							} else {
								black.this.addtomarkstat(tr.text);
							}
						}
					} else if (black.findview == 1) {
						final TextRegion tr = (TextRegion) qt.data(1, 0);
						if (tr.filename == null) {
							QTextCursor tc = this.text.textCursor();
							tc.setPosition(tr.start);
							tc.setPosition(tr.start + tr.end, QTextCursor.MoveMode.KeepAnchor);
							this.text.setTextCursor(tc);
							black.this.btext.scroll();
						} else {
							QTreeWidgetItem find = black.this.findTreeItemByFileName(tr.filename, null);
							black.this.changedFileOfCurrentEdit();

							black.this.openFileByTreeItem(find);
							black.this.afterLoadFile = new action() {
								public void action() {
									QTextCursor tc = text.textCursor();
									tc.setPosition(tr.start);
									tc.setPosition(tr.start + tr.end, QTextCursor.MoveMode.KeepAnchor);
									text.setTextCursor(tc);
									black.this.btext.scroll();
								}
							};
						}

					} else if (black.findview == 2) {
						this.onlyUserAction = false;

						TextRegion tr = (TextRegion) qt.data(1, 0);
						QTextCursor tc = this.text.textCursor();
						tc.setPosition(tr.start, QTextCursor.MoveMode.MoveAnchor);
						black.this.btext.doNotSetStyle = true;
						this.text.setTextCursor(tc);
						black.this.btext.scroll();
					} else if (black.findview == 3) {
						TextRegion tr = (TextRegion) qt.data(1, 0);
						black.this.bInsertText(this.text, tr.text, -1, true);
					} else if (black.findview == 4) {
						TextRegion tr = (TextRegion) qt.data(1, 0);
						black.this.moveIndex = this.tree.indexOfTopLevelItem(this.tree.currentItem());

						black.this.addToMoveListEnd = true;
						black.this.changedFileOfCurrentEdit();
						QTreeWidgetItem findTreeItemByFileName = black.this.findTreeItemByFileName(tr.text, null);
						black.this.openFileByTreeItem(findTreeItemByFileName);
					} else if (black.findview == 5) {
						TextRegion tr = (TextRegion) qt.data(1, 0);
						QTextCursor tc = text.textCursor();
						int current = tc.position();
						if (!tc.movePosition(MoveOperation.PreviousCharacter, MoveMode.KeepAnchor))
							return;
						String findStr = tc.selectedText();
						if (findStr.isEmpty())
							return;
						String text = cheakDocument.subString(tr.text, appInfo.keywordsSeparator)[0];
						int indexOf = text.indexOf(findStr);
						if (indexOf != 0) {
							String startPart = text.substring(0, indexOf);
//							System.out.println("startPart: "+startPart);
							int start = tc.position() - startPart.length();
							if (start >= 0) {
								tc.clearSelection();
								tc.setPosition(start, MoveMode.KeepAnchor);
//								System.out.println("selectedText: "+tc.selectedText());
								if (tc.selectedText().equals(startPart)) {
									tc.clearSelection();
									tc.setPosition(current, MoveMode.KeepAnchor);
									tc.insertText(text);
								} else {
									tc.setPosition(current);
									tc.movePosition(MoveOperation.PreviousCharacter, MoveMode.KeepAnchor);
									tc.insertText(text);
								}
							} else
								tc.insertText(text);
						} else
							tc.insertText(text);

						// 添加分词出来的条目到关键词列表
						if (getBooleanValueFromSettings(appInfo.autoSaveInputOfAutoSegment, "true")) {
							addTextToMarkFile_(tr.text);
							if ((tr.describe != null) && (!tr.describe.equals(""))) {
								black.this.addtomarkstat(tr.describe);
							} else {
								black.this.addtomarkstat(tr.text);
							}
						}

					}
				}

				public void whenHide() {
					this.text.removeEventFilter(black.this.keywords);
					black.this.keywords.onlyUserAction = false;
					black.this.keywords.noDefaultSelection = false;
					if (findview == 0) {
						debugPrint("删除缓存的文档分词结果");
						ba.tempData.remove("document_lastPinyinSeg");
						keywords.tree.setFrameShape(QFrame.Shape.StyledPanel);
					}
				}

				public void selectionChanged(QTreeWidgetItem qt) {
					if (black.findview == 0) {
						if (!this.donotShowMoreText) {
							// qt.setToolTip(0, qt.text(0));
							// qt.setStatusTip(0, qt.text(0));
							TextRegion tr = (TextRegion) qt.data(1, 0);

							String find = black.this.findTextByLastIndexOf(tr.text, black.this.draft);
							tr.describe = find;
						}
					} else if (black.findview == 2) {
						black.this.doNotHideKeywordsDialog = true;
						TextRegion tr = (TextRegion) qt.data(1, 0);
						QTextCursor tc = this.text.textCursor();
						tc.setPosition(tr.start, QTextCursor.MoveMode.MoveAnchor);
						black.this.btext.doNotSetStyle = true;
						this.text.setTextCursor(tc);
						black.this.btext.scroll();
						black.this.doNotHideKeywordsDialog = false;
					}
				}

				public void whenNumberTwoPressed(QTreeWidgetItem qt) {
					if (black.findview == 0) {
						String text = ((TextRegion) qt.data(1, 0)).text;
						String[] str = cheakDocument.subString(text, appInfo.keywordsSeparator);
						String[] checkName = black.checkName(str[0]);
						if (checkName[0] != null) {
							black.this.bInsertText(black.text, checkName[0], -1, false);
							black.this.addtomarkstat(text);
						}
					} else if (findview == 2) {
						String data = (String) qt.data(4, 4);
						ArrayList<String> args = cheakDocument.checkCommandArgs(data, '$');
						QTextCursor tc = text.textCursor();
						tc.setPosition(Integer.valueOf(args.get(1)));
						tc.setPosition(Integer.valueOf(args.get(0)), MoveMode.KeepAnchor);
						text.setTextCursor(tc);
//						text.copy();

//						setLoadFileMessage("已复制到粘贴板");
					}

				}

				public void whenNumberThreePressed(QTreeWidgetItem qt) {
					if (black.findview != 0) {
						return;
					}
					String text = ((TextRegion) qt.data(1, 0)).text;
					String[] str = cheakDocument.subString(text, appInfo.keywordsSeparator);
					String[] checkName = black.checkName(str[0]);
					if (checkName[1] != null) {
						black.this.bInsertText(black.text, checkName[1], -1, false);
						black.this.addtomarkstat(text);
					}
				}

				public void whenDelPressed(QTreeWidgetItem qt) {
					if (black.findview != 0) {
						return;
					}
					String text = ((TextRegion) qt.data(1, 0)).text;
					hasinfo ishas = black.this.markstatIshas(text);
					if (!ishas.ishas) {
						return;
					}
					black.this.deleteFromMarkStatData(text);
					qt.setText(0, qt.text(0) + "[已移除]");
					QFont font = qt.font(0);
					font.setStrikeOut(true);
					qt.setFont(0, font);
				}

				public void whenWindowSizeChanged(QSize size) {
				}
			};
		}
//		if (writingView > 0) {
//			keywords.setStyleSheet("");
//			keywords.tree.setStyleSheet(
//					"QTreeWidget::item:selected {background-color: rgb(150,197,1);color: rgb(255,255,255);}");
//		} else {
//			keywords.setStyleSheet("");
//			keywords.tree.setStyleSheet(
//					"QTreeWidget::item:selected {background-color: rgb(150,197,1);color: rgb(255,255,255);}");
//		}
	}

	void getMarkFileText() {
		List<TextRegion> list = new ArrayList();
		if (this.marktext == null) {
			return;
		}
		List<String> lis = cheakDocument.getAllLine(this.marktext);
		for (String st : lis) {
			if (!st.isEmpty()) {
				TextRegion tr = new TextRegion(st, 0, 0);
				list.add(tr);
			}
		}
		this.markTextData = list;
		ishasInMarkdata();
	}

	public static fileInfo getFileInfoByQTreeItem(QTreeWidgetItem qt) {
		fileInfo in = (fileInfo) qt.data(1, 0);
		return in;
	}

	File getFile(String filename) {
		String filepath = this.projectFile.getParent() + File.separator + "Files" + File.separator + filename;
		return new File(filepath);
	}

	void t(QTreeWidgetItem qt) {
		p(qt.text(0));
	}

	void addKeyWordsListToProject() {
		addKeyWordsListToProject("未命名关键词列表");
	}

	public boolean eventFilter(QObject o, QEvent e) {
//		System.out.println(o.getClass().getName());
//		if(e.type() == QEvent.Type.KeyPress) {
//			System.out.println(o.getClass().getName());
//		}
//		System.out.println(e.type());
		if (o.equals(this) && e.type() == QEvent.Type.MouseButtonDblClick) {
			QMouseEvent me = (QMouseEvent) e;
			if (me.button().name().equals("LeftButton")) {
				if (writingView > 0) {
					if (ba.checkBingPicIsEnable() || b.getIntValueFromSettings(appInfo.randomImg, "1000") > 0) {
						ba.showBingPicInfo();
					}
				}

			}

		} else if ((o.equals(this.tree)) && (e.type() == QEvent.Type.KeyPress)) {
			QKeyEvent key = (QKeyEvent) e;
			if (key.key() == 16777220) {
				openfile();
				return true;
			}
			if ((key.key() | key.modifiers().value()) == 150994963) {
				if (isOnSameParent()) {
					moveToTop();
					return true;
				}
			} else if ((key.key() | key.modifiers().value()) == 150994965) {
				if (isOnSameParent()) {
					moveToBottom();
					return true;
				}
			} else if ((key.key() | key.modifiers().value()) == 150994962) {
				if ((isOnSameParent()) && (!isIncludeRootDir())) {
					moveToLeft();
					return true;
				}
			} else if (((key.key() | key.modifiers().value()) == 150994964) && (isOnSameParent())
					&& (!isIncludeRootDir())) {
				moveToRight();
				return true;
			}
		} else if ((o.equals(this)) && (e.type() == QEvent.Type.HoverMove)) {
			QHoverEvent hover = (QHoverEvent) e;
			if ((writingView > 0) && (text != null)) {
				QPoint ma = text.mapFrom(this, hover.pos());
//				System.out.println(ma+" "+hover.pos());

				if (appInfo.miniMode.equals("0")) {
					if (!dock.isFloating()) {
						if (hover.pos().x() < 5) {
							dock.setFloating(true);
							dock.setGeometry(0, 0, 200, height());
							dock.show();
						}
					} else {
						QPoint mapFrom = dock.mapFrom(this, hover.pos());
						if (!dock.rect().contains(mapFrom)) {
							dock.hide();

							dock.setFloating(false);
						}
					}
//					if (!menubar.isVisible()) {
//						if (hover.pos().y() < 5) {
//							menubar.show();
//							toolbar.show();
//						}
//					} else {
//						QPoint mapFrom = menubar.mapFrom(this, hover.pos());
//						QPoint mapf = toolbar.mapFrom(this, hover.pos());
//						if (!menubar.rect().contains(mapFrom) && !toolbar.rect().contains(mapf)) {
//							menubar.hide();
//							toolbar.hide();
//						}
//					}

					int x = text.width() - ma.x();
					if (x < 50) {
						if (!text.verticalScrollBar().isVisible()) {
							text.verticalScrollBar().show();
						}
					} else if (text.verticalScrollBar().isVisible()) {
						text.verticalScrollBar().hide();
					}

				}
			} else if (((writingView == 0) || !getBooleanValueFromSettings(appInfo.fullScreen, "true"))
					&& (this.btext != null) && ((!text.verticalScrollBar().isVisible()))) {
				text.verticalScrollBar().setVisible(true);
			}
			if ((hover.pos().x() <= this.dock.x() + this.dock.width()) && (hover.pos().y() > this.dock.y())
					&& (hover.pos().y() <= this.dock.y() + this.dock.height())) {
				this.tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
			} else {
				this.tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
			}
		} else if ((o.equals(this)) && (e.type() == QEvent.Type.HoverLeave)) {
			this.tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
			if ((writingView > 0) && (text != null)) {
				text.verticalScrollBar().hide();
			}
		} else if ((o.equals(this)) && (e.type() == QEvent.Type.WindowActivate)) {
			ba.randomImg();
			
		} else if ((o.equals(this)) && (e.type() == QEvent.Type.WindowDeactivate)) {
			if ((text != null) && (text.tr != null)) {
				text.tr = null;
			}
			if (text != null && text.inputing) {
				QInputMethodEvent ime = new QInputMethodEvent();
				ime.setCommitString(null);
				text.event(ime);
			}
			Thread t = (Thread) b.ba.tempData.get("randomImgThread");
			if(t != null) {
				b.ba.tempData.remove("randomImgThread");
//				ba.tempData.remove("randomImg");
//				ba.tempData.remove("randomImgOld");
				while(!t.isInterrupted()) {
					t.interrupt();
					b.debugPrint("线程是否被中断："+t.isInterrupted());
				}
			}
//			if ((keywords != null) && (keywords.isVisible()) && (!keywords.isActiveWindow())) {
//				keywords.hide();
//			}

		} else if ((o.equals(this)) && (e.type() == QEvent.Type.Resize)) {
			if ((keywords != null) && (keywords.isVisible())) {
				keywords.hide();
			}
//			if ((keywordsForFindLine != null) && (keywordsForFindLine.isVisible())) {
//				keywordsForFindLine.hide();
//			}
			setTimeMessageLabelLocation();
			setLoadFileMessageLabelLocation();
			setFindLineLocation();

		}else if ((!o.equals(this)) || (e.type() != QEvent.Type.Show)) {
			hideTime = -1;
//			System.out.println("show"+" "+hideTime);

			if ((o.equals(this)) && (e.type() == QEvent.Type.Move)) {
				if ((keywords != null) && (keywords.isVisible())) {
					keywords.hide();
				}
//				if ((keywordsForFindLine != null) && (keywordsForFindLine.isVisible())) {
//					keywordsForFindLine.hide();
//				}
			} else if ((o.equals(this.findBox)) && (e.type() == QEvent.Type.FocusIn)) {
				new bRunnable(0, true, true, true, true) {
					public void run() {
						black.this.findLine.selectAll();
					}
				};
//				if (keywordsForFindLine != null) {
//					keywordsForFindLine.noDefaultSelection = true;
//					showDialogForFindLine();
//				}
			} else if ((o.equals(this.findBox)) && (e.type() == QEvent.Type.FocusOut)) {
//				if ((keywordsForFindLine != null) && (!keywordsForFindLine.isActiveWindow())) {
//					keywordsForFindLine.close();
//				}
			} else if ((o.equals(this.findBox)) && (e.type() == QEvent.Type.KeyPress)) {
				QKeyEvent key = (QKeyEvent) e;
				if (key.key() == Qt.Key.Key_Return.value()) {
					findLineReturnPressd();
					return true;
				} else if (key.key() == Qt.Key.Key_Escape.value()) {
					if (keywordsForFindLine != null && keywordsForFindLine.isVisible())
						keywordsForFindLine.hide();
					return true;
				}
			}
//			if(text != null && text.isVisible()) text.setFocus();
			
		} else if ((!o.equals(this)) || (e.type() != QEvent.Type.Hide)) {
//			String name = debug.getNameOfClassAndMethodOfCaller();
//			System.out.println(name);
			hideTime = System.currentTimeMillis();
//			System.out.println("hide");
		}
		return false;
	}

	int getAnimationTime() {
		int value = Integer.valueOf((String) this.settings.value(appInfo.animationTime, "200")).intValue();
		return value;
	}

	void setAnimationTime(int value) {
		this.settings.setValue(appInfo.animationTime, String.valueOf(value));
	}

	void turnOffAnimation() {
		int animationTime = getAnimationTime();
		if (animationTime == 200) {
			setAnimationTime(0);
			setLoadFileMessage("已关闭动画效果");
		} else {
			setAnimationTime(200);
			setLoadFileMessage("已启用动画效果");
		}
	}

	void beep(final QMessageBox mb) {
		QObject o = new QObject() {
			public boolean eventFilter(QObject arg__1, QEvent arg__2) {
				if (arg__2.type() == QEvent.Type.Hide) {
					black.stopBeepSound();
					mb.removeEventFilter(this);
				}
				return super.eventFilter(arg__1, arg__2);
			}
		};
		mb.installEventFilter(o);
		startBeepSound(-1);
	}

	boolean isHasTimer(int type) {
		for (timerInfo in : this.timerInfos) {
			if (in.type == type) {
				return true;
			}
		}
		return false;
	}

	timerInfo[] findTimer(int type) {
		ArrayList<timerInfo> al = new ArrayList();
		for (timerInfo in : this.timerInfos) {
			if (in.type == type) {
				al.add(in);
			}
		}
		return al.toArray(new timerInfo[al.size()]);
	}

	timerInfo getTimerInfo(int id) {
		for (timerInfo in : this.timerInfos) {
			if (in.id == id) {
				return in;
			}
		}
		return null;
	}

	void removeAllTimer(int type) {
		ArrayList<timerInfo> forremove = new ArrayList();
		for (timerInfo in : this.timerInfos) {
			if (in.type == type) {
				forremove.add(in);
			}
		}
		for (timerInfo in : forremove) {
			this.timerInfos.remove(in);
		}
	}

	void expanded(QTreeWidgetItem qt) {
		if (qt.isExpanded()) {
			fileInfo in = (fileInfo) qt.data(1, 0);
			in.expaned = true;
		} else {
			fileInfo in = (fileInfo) qt.data(1, 0);
			in.expaned = false;
		}
		this.fileListChanged = true;
	}

	boolean isOnSameParent() {
		Object p = null;
		List<QTreeWidgetItem> checkedItems = getCheckedItems();
		for (QTreeWidgetItem qt : checkedItems) {
			QTreeWidgetItem parent = qt.parent();
			if (p == null) {
				p = parent;
			} else if (!p.equals(parent)) {
				return false;
			}
		}
		return true;
	}

	public void rename() {
		this.isDoubleClickedOnTreeItem = true;
		this.tree.editItem(this.tree.currentItem());
	}

	public boolean isRootDir() {
		QTreeWidgetItem currentItem = this.tree.currentItem();
		if ((currentItem != null) && ((currentItem.equals(this.draft)) || (currentItem.equals(this.reserach))
				|| (currentItem.equals(this.recycle)))) {
			return true;
		}
		return false;
	}

	void showTreeMenu(QPoint pos) {
		this.treeMenu = new QMenu(this.tree);
		this.treeMenu.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
		this.treeMenu.addAction(ba.exportFileOne);
		treeMenu.addSeparator();

		if (!isCheackable(this.draft)) {
			this.treeMenu.addAction(this.ui.addFile);
			this.treeMenu.addAction(this.ui.addKeywords);
			this.treeMenu.addAction(this.ui.addDir);
			this.treeMenu.addSeparator();
		}

		QAction rename = new QAction(this.treeMenu);
		rename.setText("重命名");
		rename.triggered.connect(this, "rename()");
		this.treeMenu.addAction(rename);
//		QAction saveasCopy = new QAction(this.treeMenu);
//		saveasCopy.setText("为所选文档创建副本");
//		saveasCopy.triggered.connect(this, "saveAsACopy()");
//		if ((getCheckedItems().size() == 0) && (this.tree.currentItem() == null)) {
//			saveasCopy.setEnabled(false);
//		}
//		if ((isRootDir()) || (isIncludeRootDir())) {
//			saveasCopy.setEnabled(false);
//		}
//		this.treeMenu.addAction(saveasCopy);
		this.treeMenu.addSeparator();

		QAction amove = new QAction(this.treeMenu);
		this.treeMenu.addAction(amove);
		amove.setText("移动");
		if (!isCheackable(this.draft)) {
			amove.setEnabled(false);
			amove.setText("不可移动（未选择项目）");
		} else if (getCheckedItems().size() == 0) {
			amove.setEnabled(false);
			amove.setText("不可移动（未选择项目）");
		} else if (isIncludeRootDir()) {
			amove.setEnabled(false);
			amove.setText("不可移动（包含不可移动的项目）");
		} else {
			Menu moveMenu = new Menu();
			amove.setMenu(moveMenu);
			QAction ato = new QAction(moveMenu);
			ato.setText("到");
			moveMenu.addAction(ato);
			Menu listMenu = listMenuWithTreeWidget(null, "triggered_listMenu(QAction)", true);
			ato.setMenu(listMenu);
			if (isOnSameParent()) {
				QAction atop = new QAction(moveMenu);
				atop.setText("上面");
				atop.triggered.connect(this, "moveToTop()");
				QAction abottom = new QAction(moveMenu);
				abottom.setText("下面");
				abottom.triggered.connect(this, "moveToBottom()");
				QAction aleft = new QAction(moveMenu);
				aleft.setText("左侧");
				aleft.triggered.connect(this, "moveToLeft()");
				QAction aright = new QAction(moveMenu);
				aright.setText("右侧");
				aright.triggered.connect(this, "moveToRight()");
				moveMenu.addAction(atop);
				moveMenu.addAction(abottom);
				moveMenu.addAction(aleft);
				moveMenu.addAction(aright);
			}
			moveMenu.triggered.connect(this, "triggered_moveMenu(QAction)");
		}
		QAction selectTreeItem = new QAction(this.treeMenu);
		this.treeMenu.addAction(selectTreeItem);
		if (!isCheackable(this.draft)) {
			selectTreeItem.setText("显示多选框");
		} else {
			selectTreeItem.setText("隐藏多选框");
			if (getCheckedItems().size() > 0) {
				QAction clearselect = new QAction(this.treeMenu);
				clearselect.setText("清除选择");
				clearselect.triggered.connect(this, "clearSelectedOnCheckBox()");
				this.treeMenu.addAction(clearselect);
			}
			QAction selectOne = new QAction(this.treeMenu);
			selectOne.setText("选择(不选择子项)");
			selectOne.triggered.connect(this, "selectOne()");
			this.treeMenu.addAction(selectOne);
			QAction selectAll = new QAction(this.treeMenu);
			selectAll.setText("全选");
			selectAll.triggered.connect(this, "selectAll()");
			this.treeMenu.addAction(selectAll);
		}
		selectTreeItem.triggered.connect(this, "selectTreeItem()");

		this.treeMenu.addSeparator();
		this.treeMenu.addAction(this.ui.moveToRecycle);
		this.treeMenu.addAction(this.ui.clearRecycle);
		this.treeMenu.addSeparator();

		QAction qAction = new QAction(this.treeMenu);
		qAction.setText("所选文件共包含" + getCharCountBySelectedItems() + "个字");
		qAction.setEnabled(false);
		this.treeMenu.addAction(qAction);
		this.treeMenu.popup(this.tree.mapToGlobal(pos));
		QTreeWidgetItem itemAt = this.tree.itemAt(pos);
		if (itemAt == null) {
			this.tree.clearSelection();
			this.tree.setCurrentItem(null);
		}
	}

	public void selectOne() {
		this.tree.currentItem().setCheckState(0, Qt.CheckState.Checked);
	}

	public void selectAll() {
		clearOrAddSelectedStateForCheckBox(null, false);
	}

	public void selectionAllSubItem() {
		List<QTreeWidgetItem> items = getCheckedItems();
		for (QTreeWidgetItem qt : items) {
			clearOrAddSelectedStateForCheckBox(qt, false);
		}
	}

	public void clearSelectedOnCheckBox() {
		clearOrAddSelectedStateForCheckBox(null, true);
	}

	public void selectTreeItem() {
		if (!isCheackable(this.draft)) {
			setCheckableForTreeItem(null, true);
		} else {
			setCheckableForTreeItem(null, false);
		}
	}

	public List<QTreeWidgetItem> getCheckedItems() {
		return getCheckedItems(null);
	}

	public boolean isCheackable(QTreeWidgetItem qt) {
		Object data = qt.data(0, 10);
		if (data != null) {
			return true;
		}
		return false;
	}

	public List<QTreeWidgetItem> getCheckedItems(QTreeWidgetItem qt) {
		ArrayList<QTreeWidgetItem> list = new ArrayList();
		if (qt == null) {
			for (int i = 0; i < this.tree.topLevelItemCount(); i++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(i);
				Qt.CheckState checkState = topLevelItem.checkState(0);
				if (checkState.compareTo(Qt.CheckState.Checked) == 0) {
					list.add(topLevelItem);
				}
				if (topLevelItem.childCount() > 0) {
					List<QTreeWidgetItem> list2 = getCheckedItems(topLevelItem);
					list.addAll(list2);
				}
			}
		} else {
			for (int i = 0; i < qt.childCount(); i++) {
				QTreeWidgetItem child = qt.child(i);
				Qt.CheckState checkState = child.checkState(0);
				if (checkState.compareTo(Qt.CheckState.Checked) == 0) {
					list.add(child);
				}
				if (child.childCount() > 0) {
					List<QTreeWidgetItem> list2 = getCheckedItems(child);
					list.addAll(list2);
				}
			}
		}
		return list;
	}

	public void clearOrAddSelectedStateForCheckBox(QTreeWidgetItem qt, boolean isclear) {
		if (qt == null) {
			for (int i = 0; i < this.tree.topLevelItemCount(); i++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(i);
				if (isclear) {
					topLevelItem.setCheckState(0, Qt.CheckState.Unchecked);
				} else {
					topLevelItem.setCheckState(0, Qt.CheckState.Checked);
				}
				if (topLevelItem.childCount() > 0) {
					clearOrAddSelectedStateForCheckBox(topLevelItem, isclear);
				}
			}
		} else {
			for (int i = 0; i < qt.childCount(); i++) {
				QTreeWidgetItem child = qt.child(i);
				if (isclear) {
					child.setCheckState(0, Qt.CheckState.Unchecked);
				} else {
					child.setCheckState(0, Qt.CheckState.Checked);
				}
				if (child.childCount() > 0) {
					clearOrAddSelectedStateForCheckBox(child, isclear);
				}
			}
		}
	}

	public void setCheckableForTreeItem(QTreeWidgetItem qt, boolean addOrRemoveCheckbox) {
		if (qt == null) {
			for (int i = 0; i < this.tree.topLevelItemCount(); i++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(i);
				if (addOrRemoveCheckbox) {
					topLevelItem.setCheckState(0, Qt.CheckState.Unchecked);
				} else {
					topLevelItem.setData(0, 10, null);
				}
				if (topLevelItem.childCount() > 0) {
					setCheckableForTreeItem(topLevelItem, addOrRemoveCheckbox);
				}
			}
		} else {
			for (int i = 0; i < qt.childCount(); i++) {
				QTreeWidgetItem child = qt.child(i);
				if (addOrRemoveCheckbox) {
					child.setCheckState(0, Qt.CheckState.Unchecked);
				} else {
					child.setData(0, 10, null);
				}
				if (child.childCount() > 0) {
					setCheckableForTreeItem(child, addOrRemoveCheckbox);
				}
			}
		}
	}

	public int getCharCountBySelectedItems() {
		int charCount = 0;
		List<QTreeWidgetItem> selectedItems = null;
		if (!isCheackable(this.draft)) {
			selectedItems = this.tree.selectedItems();
		} else {
			selectedItems = getCheckedItems();
		}
		if (selectedItems != null) {
			for (QTreeWidgetItem qt : selectedItems) {
				fileInfo fileInfoByQTreeItem = getFileInfoByQTreeItem(qt);
				charCount += fileInfoByQTreeItem.charCount;
			}
		}
		return charCount;
	}

	QTreeWidgetItem getItemUnderCursor() {
		QPoint pos = this.tree.mapFromGlobal(this.treeMenu.pos());
		QTreeWidgetItem itemAt = this.tree.itemAt(pos);
		return itemAt;
	}

	boolean isIncludeRootDir() {
		List<QTreeWidgetItem> checkedItems = getCheckedItems();
		for (QTreeWidgetItem qt : checkedItems) {
			if ((qt.equals(this.draft)) || (qt.equals(this.reserach)) || (qt.equals(this.recycle))) {
				return true;
			}
		}
		return false;
	}

	void moveToRight() {
		List<QTreeWidgetItem> selectedItems = getCheckedItems();
		QTreeWidgetItem parent = ((QTreeWidgetItem) selectedItems.get(0)).parent();
		QTreeWidgetItem item = null;
		if (parent != null) {
			fileInfo in_parent = (fileInfo) parent.data(1, 0);
			int indexOfChild = parent.indexOfChild((QTreeWidgetItem) selectedItems.get(0));
			if (indexOfChild > 0) {
				QTreeWidgetItem child = parent.child(indexOfChild - 1);
				fileInfo in_child = (fileInfo) child.data(1, 0);
				for (Iterator localIterator = selectedItems.iterator(); localIterator.hasNext();) {
					item = (QTreeWidgetItem) localIterator.next();
					fileInfo in = (fileInfo) item.data(1, 0);
					if ((in_child.isDir) || (in_child.isFiles) || (in_child.isRoot != -1)) {
						parent.removeChild(item);
						child.addChild(item);
						item.setExpanded(in.expaned);
						child.setExpanded(true);
						in_parent.subfiles.remove(indexOfChild);
						in_child.subfiles.add(in);
					} else if (in_child.isFile) {
						parent.removeChild(item);
						if ((in_parent.isFiles) && (in_parent.subfiles.size() == 0)) {
							in_parent.subfiles = null;
							in_parent.isFiles = false;
							in_parent.isFile = true;
							if (in_parent.charCount == 0) {
								parent.setIcon(0, this.ico_file);
							} else {
								parent.setIcon(0, this.ico_fileOfHasText);
							}
						}
						child.addChild(item);
						item.setExpanded(in.expaned);
						child.setExpanded(true);
						child.setIcon(0, this.ico_files);
						in_parent.subfiles.remove(indexOfChild);
						in_child.isFiles = true;
						in_child.isFile = false;
						in_child.initFloder().add(in);
					}
				}
			}
		} else {
			int indexOfTopLevelItem = this.tree.indexOfTopLevelItem((QTreeWidgetItem) selectedItems.get(0));
			if (indexOfTopLevelItem > 0) {
				QTreeWidgetItem child = this.tree.topLevelItem(indexOfTopLevelItem - 1);
				fileInfo in_child = (fileInfo) child.data(1, 0);
				for (QTreeWidgetItem it : selectedItems) {
					int index = this.tree.indexOfTopLevelItem(it);
					fileInfo in = (fileInfo) it.data(1, 0);
					if ((in_child.isDir) || (in_child.isFiles) || (in_child.isRoot != -1)) {
						this.tree.takeTopLevelItem(index);
						child.addChild(it);
						it.setExpanded(in.expaned);
						child.setExpanded(true);
						this.filesList.remove(indexOfTopLevelItem);
						in_child.subfiles.add(in);
					} else if (in_child.isFile) {
						this.tree.takeTopLevelItem(index);

						child.addChild(it);
						it.setExpanded(in.expaned);
						child.setExpanded(true);
						child.setIcon(0, this.ico_files);
						this.filesList.remove(indexOfTopLevelItem);
						in_child.isFiles = true;
						in_child.isFile = false;
						in_child.initFloder().add(in);
					}
				}
			}
		}
		this.tree.setCurrentItem((QTreeWidgetItem) selectedItems.get(0));
		this.fileListChanged = true;
	}

	void moveToLeft() {
		List<QTreeWidgetItem> selectedItems = getCheckedItems();
		QTreeWidgetItem parent = ((QTreeWidgetItem) selectedItems.get(0)).parent();
		if (parent != null) {
			QTreeWidgetItem parentOfparent = parent.parent();
			fileInfo in_parent = (fileInfo) parent.data(1, 0);
			QTreeWidgetItem item;
			if (parentOfparent != null) {
				fileInfo in_parentOfparent = (fileInfo) parentOfparent.data(1, 0);
				int indexOfParent = parentOfparent.indexOfChild(parent);
				if (indexOfParent + 1 < parentOfparent.childCount()) {
					for (QTreeWidgetItem it : selectedItems) {
						fileInfo in = (fileInfo) it.data(1, 0);
						parent.removeChild(it);
						parentOfparent.insertChild(indexOfParent + 1, it);
						it.setExpanded(in.expaned);
						in_parent.subfiles.remove(in);
						in_parentOfparent.subfiles.add(indexOfParent + 1, in);
						if ((in_parent.isFiles) && (in_parent.subfiles.size() == 0)) {
							if (in_parent.charCount == 0) {
								parent.setIcon(0, this.ico_file);
							} else {
								parent.setIcon(0, this.ico_fileOfHasText);
							}
							in_parent.isFiles = false;
							in_parent.isFile = true;
							in_parent.subfiles = null;
						}
					}
				} else {
					Iterator it = selectedItems.iterator();
					while (it.hasNext()) {
						QTreeWidgetItem ite = (QTreeWidgetItem) it.next();
						fileInfo in = (fileInfo) ite.data(1, 0);
						parent.removeChild(ite);
						parentOfparent.addChild(ite);
						ite.setExpanded(in.expaned);
						in_parent.subfiles.remove(in);
						in_parentOfparent.subfiles.add(in);
						if ((in_parent.isFiles) && (in_parent.subfiles.size() == 0)) {
							if (in_parent.charCount == 0) {
								parent.setIcon(0, this.ico_file);
							} else {
								parent.setIcon(0, this.ico_fileOfHasText);
							}
							in_parent.isFiles = false;
							in_parent.isFile = true;
							in_parent.subfiles = null;
						}
					}
				}
			} else {
				int indexOfTopLevelItem = this.tree.indexOfTopLevelItem(parent);
				if (indexOfTopLevelItem + 1 < this.tree.topLevelItemCount()) {
					for (QTreeWidgetItem it : selectedItems) {
						fileInfo in = (fileInfo) it.data(1, 0);
						parent.removeChild(it);
						this.tree.insertTopLevelItem(indexOfTopLevelItem + 1, it);
						it.setExpanded(in.expaned);
						in_parent.subfiles.remove(in);
						this.filesList.add(indexOfTopLevelItem + 1, in);
						if ((in_parent.isFiles) && (in_parent.subfiles.size() == 0)) {
							if (in_parent.charCount == 0) {
								parent.setIcon(0, this.ico_file);
							} else {
								parent.setIcon(0, this.ico_fileOfHasText);
							}
							in_parent.isFile = true;
							in_parent.isFiles = false;
							in_parent.subfiles = null;
						}
					}
				} else {
					for (QTreeWidgetItem it : selectedItems) {
						fileInfo in = (fileInfo) it.data(1, 0);
						parent.removeChild(it);
						this.tree.addTopLevelItem(it);
						it.setExpanded(in.expaned);
						in_parent.subfiles.remove(in);
						this.filesList.add(in);
						if ((in_parent.isFiles) && (in_parent.subfiles.size() == 0)) {
							if (in_parent.charCount == 0) {
								parent.setIcon(0, this.ico_file);
							} else {
								parent.setIcon(0, this.ico_fileOfHasText);
							}
							in_parent.isFile = true;
							in_parent.isFiles = false;
							in_parent.subfiles = null;
						}
					}
				}
			}
			this.tree.setCurrentItem((QTreeWidgetItem) selectedItems.get(0));
		}
		this.fileListChanged = true;
	}

	void moveToBottom() {
		List<QTreeWidgetItem> selectedItems = getCheckedItems();
		for (int i = selectedItems.size() - 1; i >= 0; i--) {
			QTreeWidgetItem item = (QTreeWidgetItem) selectedItems.get(i);
			if (item != null) {
				if (item.parent() != null) {
					QTreeWidgetItem parent = item.parent();
					fileInfo in = (fileInfo) item.data(1, 0);
					fileInfo in_parent = (fileInfo) parent.data(1, 0);
					int index = parent.indexOfChild(item);
					if (index + 1 < parent.childCount()) {
						parent.removeChild(item);
						parent.insertChild(index + 1, item);
						item.setExpanded(in.expaned);
						in_parent.subfiles.remove(in);
						in_parent.subfiles.add(index + 1, in);
					}
				} else {
					fileInfo in = (fileInfo) item.data(1, 0);
					int indexOfTopLevelItem = this.tree.indexOfTopLevelItem(item);
					if (indexOfTopLevelItem + 1 < this.tree.topLevelItemCount()) {
						this.tree.takeTopLevelItem(indexOfTopLevelItem);
						this.tree.insertTopLevelItem(indexOfTopLevelItem + 1, item);
						item.setExpanded(in.expaned);
						this.filesList.remove(in);
						this.filesList.add(indexOfTopLevelItem + 1, in);
					} else {
						return;
					}
				}
				this.tree.setCurrentItem(item);
			}
		}
		this.fileListChanged = true;
	}

	void moveToTop() {
		List<QTreeWidgetItem> selectedItems = getCheckedItems();
		for (QTreeWidgetItem item : selectedItems) {
			if (item != null) {
				if (item.parent() != null) {
					QTreeWidgetItem parent = item.parent();
					fileInfo in = (fileInfo) item.data(1, 0);
					fileInfo in_parent = (fileInfo) parent.data(1, 0);
					int index = parent.indexOfChild(item);
					if (index > 0) {
						parent.removeChild(item);
						parent.insertChild(index - 1, item);
						item.setExpanded(in.expaned);
						in_parent.subfiles.remove(in);
						in_parent.subfiles.add(index - 1, in);
					}
				} else {
					fileInfo in = (fileInfo) item.data(1, 0);
					int indexOfTopLevelItem = this.tree.indexOfTopLevelItem(item);
					if (indexOfTopLevelItem > 0) {
						this.tree.takeTopLevelItem(indexOfTopLevelItem);
						this.tree.insertTopLevelItem(indexOfTopLevelItem - 1, item);
						item.setExpanded(in.expaned);
						this.filesList.remove(in);
						this.filesList.add(indexOfTopLevelItem - 1, in);
					} else {
						return;
					}
				}
				this.tree.setCurrentItem(item);
			}
		}
		this.fileListChanged = true;
	}

	void triggered_moveMenu(QAction a) {
		if (a.isEnabled()) {
			hideTreeMenu();
			if (a.menu() != null) {
				triggered_listMenu(a);
			} else {
				a.triggered.emit(Boolean.valueOf(true));
			}
		}
	}

	void hideTreeMenu() {
		if (this.treeMenu != null) {
			this.treeMenu.hide();
		}
	}

	void showLogsText() {
		if (logsBox == null) {
			logsBox = new bmessageBox(this, "日志", "确定", this.logsmessage.toString(), true) {

				@Override
				public void buttonPressedAction(String paramString) {
					// TODO Auto-generated method stub
					setVisible(false);
				}
			};
		} else
			logsBox.setVisible(true);
		logsBox.ui.textEdit.moveCursor(QTextCursor.MoveOperation.End);
	}

	static void p(Object o) {
		String log_time = time.getCurrentDate("-") + " " + time.getCurrentTimeHasSecond();
		String log_message = o.toString();
		System.out.println(log_time + " " + log_message);
		String log = "<b>" + log_time + "</b> " + log_message + "<br>";
		logsmessage.append(log);

		uiRun(b, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (logsBox != null) {
					logsBox.ui.textEdit.insertHtml(log);
					logsBox.ui.textEdit.moveCursor(QTextCursor.MoveOperation.End);
				}
			}
		});
//		speakText(o.toString(), -1);
	}

	void p_(Object o) {
		p(o);
	}

	void p__(Object o) {
		p(o);
		statusBar().showMessage(o.toString());
//		speakText(o.toString(), -1);
	}

	void triggered_listMenu(QAction a) {
		if (a.isEnabled()) {
			hideTreeMenu();
			QTreeWidgetItem to = (QTreeWidgetItem) a.data();
			List<QTreeWidgetItem> selectedItems = getCheckedItems();
			for (QTreeWidgetItem src : selectedItems) {
				QTreeWidgetItem from = src.parent();
				moveFile(src, from, to);
				clearOrAddSelectedStateForCheckBox(src, true);
			}
		}
	}

	void moveFile(QTreeWidgetItem src, QTreeWidgetItem from, QTreeWidgetItem to) {
		if (src != null) {
			if ((src.equals(this.draft)) || (src.equals(this.reserach)) || (src.equals(this.recycle))
					|| (isHas(src, to))) {
				return;
			}
			fileInfo info_src = (fileInfo) src.data(1, 0);
			if (to != null) {
				fileInfo info_to = (fileInfo) to.data(1, 0);
				if (info_to.isDir) {
					if (from != null) {
						fileInfo info_from = (fileInfo) from.data(1, 0);
						info_from.subfiles.remove(info_src);
						from.removeChild(src);
						if ((info_from.isFiles) && (info_from.subfiles.size() == 0)) {
							from.setIcon(0, this.ico_file);
							info_from.isFiles = false;
							info_from.subfiles = null;
						}
					} else {
						this.filesList.remove(info_src);
						this.tree.takeTopLevelItem(this.tree.indexOfTopLevelItem(src));
					}
					info_to.subfiles.add(info_src);
					to.addChild(src);
					setExpanded(to, true);
				} else if (info_to.isRoot != -1) {
					if (from != null) {
						fileInfo info_from = (fileInfo) from.data(1, 0);
						info_from.subfiles.remove(info_src);
						from.removeChild(src);
						if ((info_from.isFiles) && (info_from.subfiles.size() == 0)) {
							from.setIcon(0, this.ico_file);
							info_from.isFiles = false;
							info_from.subfiles = null;
						}
					} else {
						this.filesList.remove(info_src);
						this.tree.takeTopLevelItem(this.tree.indexOfTopLevelItem(src));
					}
					info_to.subfiles.add(info_src);
					to.addChild(src);
					setExpanded(to, true);
				} else if (info_to.isFiles) {
					if (from != null) {
						fileInfo info_from = (fileInfo) from.data(1, 0);
						info_from.subfiles.remove(info_src);
						from.removeChild(src);
						if ((info_from.isFiles) && (info_from.subfiles.size() == 0)) {
							from.setIcon(0, this.ico_file);
							info_from.isFiles = false;
							info_from.subfiles = null;
						}
					} else {
						this.filesList.remove(info_src);
						this.tree.takeTopLevelItem(this.tree.indexOfTopLevelItem(src));
					}
					info_to.subfiles.add(info_src);
					to.addChild(src);
					setExpanded(to, true);
				} else if (info_to.isFile) {
					if (from != null) {
						fileInfo info_from = (fileInfo) from.data(1, 0);
						info_from.subfiles.remove(info_src);
						from.removeChild(src);
						if ((info_from.isFiles) && (info_from.subfiles.size() == 0)) {
							from.setIcon(0, this.ico_file);
							info_from.isFiles = false;
							info_from.subfiles = null;
						}
					} else {
						this.filesList.remove(info_src);
						this.tree.takeTopLevelItem(this.tree.indexOfTopLevelItem(src));
					}
					to.setIcon(0, this.ico_files);
					to.addChild(src);
					setExpanded(to, true);
					info_to.isFiles = true;
					info_to.isFile = false;
					info_to.initFloder();
					info_to.subfiles.add(info_src);
				}
			} else if (from != null) {
				fileInfo info_from = (fileInfo) from.data(1, 0);
				info_from.subfiles.remove(info_src);
				from.removeChild(src);
				if ((info_from.isFiles) && (info_from.subfiles.size() == 0)) {
					from.setIcon(0, this.ico_file);
					info_from.isFiles = false;
					info_from.subfiles = null;
				}
				this.tree.addTopLevelItem(src);
				this.filesList.add(info_src);
			}
			this.fileListChanged = true;
		}
	}

	void initKeywordsForFindLine() {
		if (keywordsForFindLine == null) {
			keywordsForFindLine = new keyWords(null, true) {
				public void whenWindowSizeChanged(QSize size) {
				}

				public void whenSubmit(QTreeWidgetItem qt) {
//					close();
					// 首次跳转前保存位置信息，方便回到原来的位置
					if (!qt.text(0).isEmpty()) {
						TextRegion lastEdit = new TextRegion("", 0, 0);
						lastEdit.filename = currentEditing.getName();
						QTextCursor lastTc = black.this.text.textCursor();
						lastEdit.start = lastTc.selectionStart();
						lastEdit.end = lastTc.selectionEnd() - lastEdit.start;
						ba.tempData.put("lastEditLocation", lastEdit);
					}

					final TextRegion tr = (TextRegion) qt.data(1, 0);
					if (tr.filename.equals(black.this.currentEditing.getName())) {
						QTextCursor tc = black.this.btext.text.textCursor();
						tc.setPosition(tr.start);
						tc.setPosition(tr.start + tr.end, QTextCursor.MoveMode.KeepAnchor);
						black.this.btext.text.setTextCursor(tc);
						black.this.btext.scroll();
					} else {
						QTreeWidgetItem find = black.this.findTreeItemByFileName(tr.filename, null);
						doNotSet_UnSave = true;
						black.this.changedFileOfCurrentEdit();
						black.this.openFileByTreeItem(find);
						black.this.afterLoadFile = new action() {
							public void action() {
								black.this.afterLoadFile = null;
								QTextCursor tc = black.this.btext.text.textCursor();
								tc.setPosition(tr.start);
								tc.setPosition(tr.start + tr.end, QTextCursor.MoveMode.KeepAnchor);
								black.this.btext.text.setTextCursor(tc);
								black.this.btext.scroll();
							}
						};
					}
				}

				public void whenNumberTwoPressed(QTreeWidgetItem qt) {

				}

				public void whenNumberThreePressed(QTreeWidgetItem qt) {
				}

				public void whenHide() {
//					black.this.findBox.removeEventFilter(black.this.keywordsForFindLine);
					this.noDefaultSelection = false;
					ba.tempData.remove("lastEditLocation");
					ba.tempData.remove("lastFindIn");
					ba.tempData.remove("lastFindText");
				}

				public void whenDelPressed(QTreeWidgetItem qt) {
				}

				public void selectionChanged(QTreeWidgetItem qt) {
				}
			};
			QAction act_back = new QAction(keywordsForFindLine);
			act_back.setShortcut("ctrl+1");
			act_back.setShortcut("alt+1");
			act_back.triggered.connect(black.this, "testComeBack()");
			keywordsForFindLine.addAction(act_back);
			QAction act_reFind = new QAction(keywordsForFindLine);
			act_reFind.setShortcut("ctrl+2");
			act_reFind.setShortcut("alt+2");
			act_reFind.triggered.connect(black.this, "testReFind()");
			keywordsForFindLine.addAction(act_reFind);
			keywordsForFindLine.setWindowTitle("检索结果");
			keywordsForFindLine.setWindowIcon(this.windowIcon());
		}
	}

	/**
	 * 检索框中按下数字键2，可以跳转到上一次编辑的位置
	 */
	void testComeBack() {
		QTreeWidgetItem newqt = new QTreeWidgetItem();
		newqt.setData(1, 0, ba.tempData.get("lastEditLocation"));
		keywordsForFindLine.whenSubmit(newqt);
	}

	/**
	 * 重新检索项目
	 */
	void testReFind() {
//		findLineReturnPressd();
		showFindInfoForFindLine((String) ba.tempData.get("lastFindText"), (int) ba.tempData.get("lastFindIn"));
	}

	/**
	 * 全局检索框
	 * 
	 * @param text
	 * @param findIn为0时搜索整个项目，非0时只搜索当前文件
	 */
	public void showFindInfoForFindLine(String text, int findIn) {
		if (keywordsForFindLine != null) {
			keywordsForFindLine.tree.clear();
		} else {
			initKeywordsForFindLine();
		}
		ba.tempData.put("lastFindText", text);
		ba.tempData.put("lastFindIn", findIn);

		keywordsForFindLine.tree.clear();
		ArrayList<TextRegion> findTextAsFast = null;
		findTextAsFast = (ArrayList) cheakDocument.searchByLine(this.text.toPlainText(), text,
				getShowNameOfCurrentEditingFile(), this.currentEditing.getName());
		if (findIn == 0) {
			findTextAsFast.addAll(findTextAsFast(text, null, new ArrayList()));
		}
		for (TextRegion tr : findTextAsFast) {
			tr.describe = tr.text;
			tr.text = text;
			QTreeWidgetItem treeItem = getTreeItem(keywordsForFindLine.tree);
			treeItem.setText(0, tr.showname + " - " + tr.describe);
			treeItem.setData(1, 0, tr);
		}
		keywordsForFindLine.setWindowTitle("检索结果");
		keywordsForFindLine.statusbar.showMessage(
				keywordsForFindLine.tree.topLevelItemCount() + "个结果 （按下Alt/Ctrl+1键可跳回原来位置|按下Alt/Ctrl+2键可重新检索）");
		if (getFindIn() == 0) {
			keywordsForFindLine.message.setText("检索所有文件");
		} else {
			keywordsForFindLine.message.setText("检索(" + getShowNameOfCurrentEditingFile() + ")");
		}
		keywordsForFindLine.forFindLine = true;
		ba.tempData.put("提示重新检索", 0);

		showDialogForFindLine();
	}

	public void showFindInfo(bTextEdit text, QTreeWidgetItem qt, boolean onlyCurrentDocument) {
		String findText = text.textCursor().selectedText();
		findBox.lineEdit().setText(findText);
		int findIn = 1;
		if (!onlyCurrentDocument)
			findIn = 0;
		showFindInfoForFindLine(findText, findIn);
//		if ((keywords != null) && (keywords.isVisible())) {
//			return;
//		}
//		if (keywords == null) {
//			initKeyWordsDialog();
//		}
//		keywords.tree.clear();
//		String str = text.textCursor().selectedText();
//		addToFindHistory(str);
//		ArrayList<TextRegion> findTextAsFast = null;
//		if (!onlyCurrentDocument) {
//			findTextAsFast = findTextAsFast(str, qt, new ArrayList());
//			keywords.message.setText("检索父目录(" + findTextAsFast.size() + "条结果)");
//		} else {
//			findTextAsFast = (ArrayList) cheakDocument.searchByLine(text.toPlainText(), str, "当前文档", null);
//			keywords.message.setText("检索当前文档(" + findTextAsFast.size() + "条结果)");
//		}
//		keywords.statusbar.showMessage("Enter:跳转至所在位置");
//		for (TextRegion tr : findTextAsFast) {
//			tr.describe = tr.text;
//			tr.text = str;
//			QTreeWidgetItem treeItem = getTreeItem(keywords.tree);
//			if (onlyCurrentDocument) {
//				treeItem.setText(0, tr.describe);
//			} else {
//				treeItem.setText(0, tr.showname + " - " + tr.describe);
//			}
//			treeItem.setData(1, 0, tr);
//		}
//		showDialogAtEditorCaretPos(text, keywords);

	}

	public ArrayList<TextRegion> findTextAsFast(String text, QTreeWidgetItem qt, ArrayList<TextRegion> al) {
		if (qt == null) {
			for (int i = 0; i < this.tree.topLevelItemCount(); i++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(i);
				fileInfo fileInfoByQTreeItem = getFileInfoByQTreeItem(topLevelItem);
				if (((!topLevelItem.equals(this.TreeWidgetItemOfCurrentEditing)) && (fileInfoByQTreeItem.isFile))
						|| (fileInfoByQTreeItem.isFiles)) {
					File file = getFile(fileInfoByQTreeItem.fileName);
					String str = readBlackFile(file);
					if (str != null) {
						List<TextRegion> searchByLine = cheakDocument.searchByLine(str, text,
								fileInfoByQTreeItem.showName, fileInfoByQTreeItem.fileName);
						al.addAll(searchByLine);
					}
				}
				if (topLevelItem.childCount() > 0) {
					findTextAsFast(text, topLevelItem, al);
				}
			}
		} else {
			for (int i = 0; i < qt.childCount(); i++) {
				QTreeWidgetItem child = qt.child(i);
				fileInfo fileInfoByQTreeItem = getFileInfoByQTreeItem(child);
				if (((!child.equals(this.TreeWidgetItemOfCurrentEditing)) && (fileInfoByQTreeItem.isFile))
						|| (fileInfoByQTreeItem.isFiles)) {
					File file = getFile(fileInfoByQTreeItem.fileName);
					String str = readBlackFile(file);
					if (str != null) {
						List<TextRegion> searchByLine = cheakDocument.searchByLine(str, text,
								fileInfoByQTreeItem.showName, fileInfoByQTreeItem.fileName);
						al.addAll(searchByLine);
					}
				}
				if (child.childCount() > 0) {
					findTextAsFast(text, child, al);
				}
			}
		}
		return al;
	}

	public String findByLastOfLine(String text, String str) {
		List<String> allLine = cheakDocument.getAllLine(text);
		for (int i = allLine.size() - 1; i >= 0; i--) {
			String line = (String) allLine.get(i);
			int index = -1;
			index = line.lastIndexOf(str);
			if (index != -1) {
				return line;
			}
			if (str.indexOf("·") != -1) {
				String firstname = str.substring(str.lastIndexOf('·') + 1, str.length());
				String lastname = str.substring(0, str.indexOf('·'));
				index = line.lastIndexOf(firstname);
				if (index != -1) {
					return line;
				}
				index = line.lastIndexOf(lastname);
				if (index != -1) {
					return line;
				}
			}
		}
		return null;
	}

	public String findTextByLastIndexOf(String text, QTreeWidgetItem qt) {
		if (qt == null) {
			for (int i = this.tree.topLevelItemCount() - 1; i >= 0; i--) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(i);
				if (topLevelItem.childCount() > 0) {
					String findTextByLastIndexOf = findTextByLastIndexOf(text, topLevelItem);
					if (findTextByLastIndexOf != null) {
						return findTextByLastIndexOf;
					}
				}
				fileInfo fileInfoByQTreeItem = getFileInfoByQTreeItem(topLevelItem);
				if ((fileInfoByQTreeItem.isFile) || (fileInfoByQTreeItem.isFiles)) {
					String str = null;
					if (!fileInfoByQTreeItem.equals(this.infoOfCurrentEditing)) {
						File file = getFile(fileInfoByQTreeItem.fileName);
						str = readBlackFile(file);
					} else {
						str = this.text.toPlainText();
					}
					if (str != null) {
						String findByLastOfLine = findByLastOfLine(str, text);
						if (findByLastOfLine != null) {
							return findByLastOfLine;
						}
					}
				}
			}
		} else {
			for (int i = qt.childCount() - 1; i >= 0; i--) {
				QTreeWidgetItem child = qt.child(i);
				if (child.childCount() > 0) {
					String findTextByLastIndexOf = findTextByLastIndexOf(text, child);
					if (findTextByLastIndexOf != null) {
						return findTextByLastIndexOf;
					}
				}
				fileInfo fileInfoByQTreeItem = getFileInfoByQTreeItem(child);
				if ((fileInfoByQTreeItem.isFile) || (fileInfoByQTreeItem.isFiles)) {
					String str = null;
					if (!fileInfoByQTreeItem.equals(this.infoOfCurrentEditing)) {
						File file = getFile(fileInfoByQTreeItem.fileName);
						str = readBlackFile(file);
					} else {
						str = this.text.toPlainText();
					}
					if (str != null) {
						String findByLastOfLine = findByLastOfLine(str, text);
						if (findByLastOfLine != null) {
							return findByLastOfLine;
						}
					}
				}
			}
		}
		return null;
	}

	int repearFileInfos(fileInfo in) {
		int count = 0;
		if (in == null) {
			for (int i = 0; i < this.filesList.size(); i++) {
				fileInfo fileInfo = (fileInfo) this.filesList.get(i);
				if ((!fileInfo.isDir) && (!fileInfo.isFiles) && (fileInfo.isRoot == -1) && (!fileInfo.isFile)
						&& (!fileInfo.isKeyWordsList)) {
					if (getFile(fileInfo.fileName).exists()) {
						if (fileInfo.subfiles != null) {
							if (fileInfo.subfiles.size() > 0) {
								fileInfo.isFiles = true;
							} else {
								fileInfo.isFile = true;
								fileInfo.subfiles = null;
							}
						} else {
							fileInfo.isFile = true;
						}
					} else if (fileInfo.subfiles != null) {
						fileInfo.isDir = true;
					}
					p("错误的fileInfo： " + fileInfo.showName);
					count++;
				}
				if (fileInfo.subfiles != null) {
					count += repearFileInfos(fileInfo);
				}
			}
		} else {
			for (int i = 0; i < in.subfiles.size(); i++) {
				fileInfo fileInfo = (fileInfo) in.subfiles.get(i);
				if ((!fileInfo.isDir) && (!fileInfo.isFiles) && (fileInfo.isRoot == -1) && (!fileInfo.isFile)
						&& (!fileInfo.isKeyWordsList)) {
					if (getFile(fileInfo.fileName).exists()) {
						if (fileInfo.subfiles != null) {
							if (fileInfo.subfiles.size() > 0) {
								fileInfo.isFiles = true;
							} else {
								fileInfo.isFile = true;
								fileInfo.subfiles = null;
							}
						} else {
							fileInfo.isFile = true;
						}
					} else if (fileInfo.subfiles != null) {
						fileInfo.isDir = true;
					}
					p("错误的fileInfo： " + fileInfo.showName);
					count++;
				}
				if (fileInfo.subfiles != null) {
					count += repearFileInfos(fileInfo);
				}
			}
		}
		return count;
	}

	String clearPlusInShowName(fileInfo in) {
		StringBuilder sb = new StringBuilder();
		if (in == null) {
			for (int i = 0; i < this.filesList.size(); i++) {
				fileInfo fileInfo = (fileInfo) this.filesList.get(i);
				if (fileInfo.showName.indexOf("+") == 0) {
					String oldName = fileInfo.showName;
					fileInfo.showName = fileInfo.showName.substring(1, fileInfo.showName.length());
					QTreeWidgetItem findTreeItemByFileInfo = findTreeItemByFileInfo(fileInfo, null);
					findTreeItemByFileInfo.setText(0, fileInfo.showName);
					if (fileInfo.fileName != null && fileInfo.fileName.equals(currentEditing.getName()))
						textTitle.setText(fileInfo.showName);
					sb.append(oldName + " -> " + fileInfo.showName + "\n");
				}

				if (fileInfo.subfiles != null) {
					sb.append(clearPlusInShowName(fileInfo));
				}
			}
		} else {
			for (int i = 0; i < in.subfiles.size(); i++) {
				fileInfo fileInfo = (fileInfo) in.subfiles.get(i);
				if (fileInfo.showName.indexOf("+") == 0) {
					String oldName = fileInfo.showName;
					fileInfo.showName = fileInfo.showName.substring(1, fileInfo.showName.length());
					QTreeWidgetItem findTreeItemByFileInfo = findTreeItemByFileInfo(fileInfo, null);
					findTreeItemByFileInfo.setText(0, fileInfo.showName);
					if (fileInfo.fileName != null && fileInfo.fileName.equals(currentEditing.getName()))
						textTitle.setText(fileInfo.showName);
					sb.append(oldName + " -> " + fileInfo.showName + "\n");
				}

				if (fileInfo.subfiles != null) {
					sb.append(clearPlusInShowName(fileInfo));
				}
			}
		}
		return sb.toString();
	}

	void statCharCountForManyDoc() {
		String statCharCount = statCharCount(null);
		List<String> allLine = cheakDocument.getAllLine(statCharCount);
		int charCount = 0;
		for (String s : allLine) {
			String[] subString = cheakDocument.subString(s, "\t");
			if (!subString[1].isEmpty())
				charCount += Integer.valueOf(subString[1]);
		}
		getBMessageBox("多文档字数统计", "<b>有" + allLine.size() + "个文档纳入了统计范围：</b>\n<i>" + statCharCount + "</i>\n<b>共计"
				+ charCount + "个字符</b>");
	}

	String statCharCount(fileInfo in) {
		StringBuilder sb = new StringBuilder();
		if (in == null) {
			for (int i = 0; i < this.filesList.size(); i++) {
				fileInfo fileInfo = (fileInfo) this.filesList.get(i);
				if (fileInfo.showName.indexOf("+") == 0) {
					sb.append(fileInfo.showName + "\t" + fileInfo.charCount + "\n");
				}

				if (fileInfo.subfiles != null) {
					sb.append(statCharCount(fileInfo));
				}
			}
		} else {
			for (int i = 0; i < in.subfiles.size(); i++) {
				fileInfo fileInfo = (fileInfo) in.subfiles.get(i);
				if (fileInfo.showName.indexOf("+") == 0) {
					sb.append(fileInfo.showName + "\t" + fileInfo.charCount + "\n");
				}

				if (fileInfo.subfiles != null) {
					sb.append(statCharCount(fileInfo));
				}
			}
		}
//		if(sb.length() > 0 && sb.charAt(sb.length()-1) == '\n') {
//			return sb.substring(0, sb.length()-1);
//		}else 
		return sb.toString();
	}

	String clearEditPoint(fileInfo in) {
		StringBuilder sb = new StringBuilder();
		if (in == null) {
			for (int i = 0; i < this.filesList.size(); i++) {
				fileInfo fileInfo = (fileInfo) this.filesList.get(i);
				if (fileInfo.fileName != null && !fileInfo.fileName.equals(currentEditing.getName())
						&& fileInfo.walkList != null && !fileInfo.walkList.isEmpty()) {
					fileInfo.walkList.clear();
					sb.append(fileInfo.showName + "\n");
				}

				if (fileInfo.subfiles != null) {
					sb.append(clearEditPoint(fileInfo));
				}
			}
		} else {
			for (int i = 0; i < in.subfiles.size(); i++) {
				fileInfo fileInfo = (fileInfo) in.subfiles.get(i);
				if (fileInfo.fileName != null && !fileInfo.fileName.equals(currentEditing.getName())
						&& fileInfo.walkList != null && !fileInfo.walkList.isEmpty()) {
					fileInfo.walkList.clear();
					sb.append(fileInfo.showName + "\n");
				}

				if (fileInfo.subfiles != null) {
					sb.append(clearEditPoint(fileInfo));
				}
			}
		}
//		if(sb.length() > 0 && sb.charAt(sb.length()-1) == '\n') {
//			return sb.substring(0, sb.length()-1);
//		}else 
		return sb.toString();
	}

	int getFileInfoCount(fileInfo in) {
		int count = 0;
		if (in == null) {
			for (int i = 0; i < this.filesList.size(); i++) {
				count++;
				fileInfo fileInfo = (fileInfo) this.filesList.get(i);
				if (fileInfo.subfiles != null) {
					count += getFileInfoCount(fileInfo);
				}
			}
		} else {
			for (int i = 0; i < in.subfiles.size(); i++) {
				count++;
				fileInfo fileInfo = (fileInfo) in.subfiles.get(i);
				if (fileInfo.subfiles != null) {
					count += getFileInfoCount(fileInfo);
				}
			}
		}
		return count;
	}

	void applySettingsToAllTreeWidget(QTreeWidgetItem qt) {
		if (qt == null) {
			for (int i = 0; i < this.tree.topLevelItemCount(); i++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(i);
				fileInfo in = (fileInfo) topLevelItem.data(1, 0);
				topLevelItem.setExpanded(in.expaned);
				if (in.isFile) {
					if (in.charCount > 0) {
						topLevelItem.setIcon(0, this.ico_fileOfHasText);
					} else {
						topLevelItem.setIcon(0, this.ico_file);
					}
				}
				if (topLevelItem.childCount() > 0) {
					applySettingsToAllTreeWidget(topLevelItem);
				}
			}
		} else {
			for (int i = 0; i < qt.childCount(); i++) {
				QTreeWidgetItem child = qt.child(i);
				fileInfo in = (fileInfo) child.data(1, 0);
				if (in.isFile) {
					if (in.charCount > 0) {
						child.setIcon(0, this.ico_fileOfHasText);
					} else {
						child.setIcon(0, this.ico_file);
					}
				}
				child.setExpanded(in.expaned);
				if (child.childCount() > 0) {
					applySettingsToAllTreeWidget(child);
				}
			}
		}
	}

	Menu listMenuWithTreeWidget(QTreeWidgetItem qt, String methodname, boolean noselected) {
		Menu menu = null;
		if (qt == null) {
			menu = new Menu();
			for (int i = 0; i < this.tree.topLevelItemCount(); i++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(i);
				QAction qAction = new QAction(menu);
				if (noselected) {
					List<QTreeWidgetItem> checkedItems = null;
					if (isCheackable(this.draft)) {
						checkedItems = getCheckedItems();
					} else {
						checkedItems = this.tree.selectedItems();
					}
					for (QTreeWidgetItem currentItem : checkedItems) {
						if ((currentItem != null) && (topLevelItem.equals(currentItem))) {
							qAction.setDisabled(true);
						}
					}
				}
				qAction.setData(topLevelItem);
				qAction.setText(topLevelItem.text(0));
				qAction.setIcon(topLevelItem.icon(0));
				menu.addAction(qAction);
				if (topLevelItem.childCount() > 0) {
					Menu listMenuWithTreeWidget = listMenuWithTreeWidget(topLevelItem, methodname, noselected);
					qAction.setMenu(listMenuWithTreeWidget);
				}
			}
		} else if (qt.childCount() > 0) {
			menu = new Menu();
			for (int i = 0; i < qt.childCount(); i++) {
				QTreeWidgetItem child = qt.child(i);
				QAction action = new QAction(menu);
				if (noselected) {
					List<QTreeWidgetItem> checkedItems = null;
					if (isCheackable(this.draft)) {
						checkedItems = getCheckedItems();
					} else {
						checkedItems = this.tree.selectedItems();
					}
					for (QTreeWidgetItem currentItem : checkedItems) {
						if ((currentItem != null) && (child.equals(currentItem))) {
							action.setDisabled(true);
						}
					}
				}
				action.setData(child);
				action.setText(child.text(0));
				action.setIcon(child.icon(0));
				menu.addAction(action);
				if (child.childCount() > 0) {
					Menu listMenuWithTreeWidget = listMenuWithTreeWidget(child, methodname, noselected);
					action.setMenu(listMenuWithTreeWidget);
				}
			}
		}
		if (menu != null) {
			menu.triggered.connect(this, methodname);
		}
		return menu;
	}

	void doubleClicked() {
		this.isDoubleClickedOnTreeItem = true;
	}

	void editFinished(QTreeWidgetItem qt, Integer col) {
//		p("testttt: "+qt.text(col));
		if (this.isDoubleClickedOnTreeItem) {
			this.isDoubleClickedOnTreeItem = false;
//			openFileByTreeItem(qt);
			fileInfo in = (fileInfo) qt.data(1, 0);
			if ((col.intValue() == 0) && (in != null) && (!qt.text(0).equals(in.showName))) {
				in.showName = qt.text(col.intValue());
				if (qt.equals(this.TreeWidgetItemOfCurrentEditing)) {
					this.textTitle.setText(in.showName);
				}
				this.mustSaveFileListFile = true;
			}
		}
		if (this.mustSaveFileListFile) {
			saveFileListFile();
			this.mustSaveFileListFile = false;
		}
	}

	public void reOpenProject() {
		String lastpro = (String) this.settings.value("app/lastUsedProject", "");
		try {
			openProject(lastpro);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void whenNoProjectOpend() {
		hideAllWidget();
		menubar.show();
		show();
		setWindowTitle(appInfo.appName);
		findProject();
	}

	void findProject() {
		if (appInfo.mode == 0)
			showOpenProjectDialog();
		else
			findProject(null);
	}

	void findProject(keyWords k) {
		ArrayList<TextRegion> al = new ArrayList<>();
		ArrayList<TextRegion> al_ = new ArrayList();

		String finddir1 = "Files" + File.separator + "Black";
		String finddir2 = "BlackBackups";
		File[] roots = File.listRoots();
		for (File r : roots) {
			String find1 = r.getPath() + File.separator + finddir1;
			File dir1 = new File(find1);
			if (dir1.exists() && dir1.isDirectory()) {
				File[] files = dir1.listFiles();
				for (File f : files) {
					if (f.isDirectory()) {
						File[] ls = f.listFiles();
						for (File l : ls) {
							if (l.toString().indexOf(".blackpro") != -1) {
								TextRegion tr = cheakDocument.subString(l.getName(), 0, ".blackpro");
								tr.filename = l.toString();
								tr.showname = l.lastModified() + "";
								al.add(tr);
//								System.out.println(time.msToTime(t));
							}
						}
					}
				}
			}

			String find2 = r.getPath() + File.separator + finddir2;
			File dir2 = new File(find2);
			if (dir2.exists() && dir2.isDirectory()) {
				File[] files = dir2.listFiles();
				for (File f : files) {
					if (f.isDirectory()) {
						File[] ls = f.listFiles();
						for (File l : ls) {
							if (l.toString().indexOf(".blackpro") != -1) {
								TextRegion tr = cheakDocument.subString(l.getName(), 0, ".blackpro");
								tr.filename = l.toString();
								tr.showname = l.lastModified() + "";
								al.add(tr);
							}
						}
					}
				}
			}
		}

		// 根据最后的修改时间重新排序
		while (al.size() > 0) {
			int maxindex = 0;
			long max = 0;
			for (int i = 0; i < al.size(); i++) {
				TextRegion tr = al.get(i);
				long time = Long.valueOf(tr.showname);
				if (time > max) {
					max = time;
					maxindex = i;
				}
			}
			al_.add(al.get(maxindex));
			al.remove(maxindex);
		}
		ArrayList<String> older = null;
		// 列出项目条目
		if (k == null) {
			k = new keyWords(this, false) {

				@Override
				public void whenWindowSizeChanged(QSize paramQSize) {
					// TODO Auto-generated method stub

				}

				@Override
				public void whenSubmit(QTreeWidgetItem paramQTreeWidgetItem) {
					// TODO Auto-generated method stub
					TextRegion tr = (TextRegion) paramQTreeWidgetItem.data(4, 4);
					if (tr.text.equals("$open")) {
						this.close();
						showOpenProjectDialog();
					} else if (tr.text.equals("$refind"))
						findProject(this);
					else {
						this.close();
						try {
							openProject(tr.filename);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
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
			k.tree.UsedToProjectPanelOrKeywordsList = 2;
			findview = 3;
			k.setMinimumWidth(width() / 3 * 2);
			setDialogAtCenter(k);
		} else {
			older = new ArrayList();
			for (int i = 0; i < k.tree.topLevelItemCount(); i++) {
				Object data = k.tree.topLevelItem(i).data(4, 4);
				if (data != null) {
					TextRegion tr = (TextRegion) data;
					if (!tr.text.equals("$open") && !tr.text.equals("$refind")) {
						older.add(tr.filename);
					}
				}
			}
			k.tree.clear();
		}
		String currentProjectPath = null;
		if (projectFile != null)
			currentProjectPath = projectFile.getPath();

		for (TextRegion tr : al_) {
			QTreeWidgetItem ti = new QTreeWidgetItem();
			boolean isNew = true;
			if (older != null)
				for (String s : older) {
					if (s.equals(tr.filename)) {
						isNew = false;
						break;
					}
				}
			else
				isNew = false;
			if (currentProjectPath != null && currentProjectPath.equals(tr.filename)) {
				QFont f = ti.font(0);
				f.setUnderline(true);
				ti.setFont(0, f);
			}
			ti.setText(0, tr.text + " - (" + tr.filename + ")");
			if (isNew) {
				QFont f = ti.font(0);
				f.setItalic(true);
				f.setBold(true);
				ti.setFont(0, f);
				ti.setForeground(0, new QBrush(new QColor(Qt.GlobalColor.blue)));
			}
			ti.setData(4, 4, tr);
			long t = System.currentTimeMillis() - Long.valueOf(tr.showname);
			ti.setData(3, 3, time.intTimeToString(time.msToTimeSimple(t)) + "前");
			k.tree.addTopLevelItem(ti);
		}
		QTreeWidgetItem ti_refind = new QTreeWidgetItem();
		ti_refind.setForeground(0, new QBrush(new QColor(Qt.GlobalColor.blue)));
		ti_refind.setText(0, "重新查找项目");
		ti_refind.setData(4, 4, new TextRegion("$refind", 0, 0));
		QFont f = ti_refind.font(0);
		f.setPointSize(f.pointSize() + 2);
		f.setBold(true);
		f.setItalic(true);
		ti_refind.setFont(0, f);
		k.tree.addTopLevelItem(ti_refind);

		if (isAdmin()) {
			QTreeWidgetItem ti_openProject = new QTreeWidgetItem();
			ti_openProject.setForeground(0, new QBrush(new QColor(Qt.GlobalColor.darkYellow)));
			ti_openProject.setText(0, "打开其他项目");
			ti_openProject.setData(4, 4, new TextRegion("$open", 0, 0));
			ti_openProject.setFont(0, f);
			k.tree.addTopLevelItem(ti_openProject);
		}

		QTreeWidgetItem topLevelItem = k.tree.topLevelItem(0);
		if (topLevelItem != null)
			k.tree.setCurrentItem(topLevelItem);
		k.setWindowTitle("在存储器上查找到的项目");
	}

	public void setDialogAtCenter(QWidget w) {
		QRect g = rect();
		int x = g.x() + (g.width() - w.width()) / 2;
		int y = g.y() + (g.height() - w.height()) / 2;
		QPoint mapToGlobal = mapToGlobal(new QPoint(x, y));
		w.setGeometry(mapToGlobal.x(), mapToGlobal.y(), w.width(), w.height());
		w.show();
	}

	public void closeProject() {
		if ((this.projectFile != null) && (this.projectInfo != null)) {
			p("正在关闭项目...");
			saveHistoryOfFileEditing();
			saveFindHistory();
			this.settings.setValue(appInfo.lastUsedProject, this.projectFile.getAbsolutePath());
			if (this.markstatIsChanged) {
				p("统计信息已改变");
				writeToMarkStatFile();
			}
			if (this.marktextIsChanged) {
				p("关键词列表已改变");
				writeToMarkFile();
			}
			this.markstat = null;
			this.marktext = null;
			this.markTextData = null;

			if (this.lastEditFile != null) {
				this.projectInfo.setProperty(appInfo.lastEditFile, this.lastEditFile.getName());
				this.lastEditFile = null;
			}
			if (this.currentEditing != null) {
				this.projectInfo.setProperty(appInfo.editing, this.currentEditing.getName());

				closeCurrentEditing();
			}
			saveProjectFile();
			saveAllFile();
			if (this.fileListChanged) {
				p("fileinfoArraylist内的数据已改变，正在保存数据...");
				saveFileListFile();
			}
			this.projectFile = null;
			this.projectInfo = null;
			this.filesList = null;
			this.countmessage.clear();
			hideAllWidget();
			menubar.show();
			p("项目已关闭");
		}
		disposeOpenedFile();
		resetTree();
		findLine.clear();
		findBox.clear();
		setSaved(true);
	}

	void saveHistoryOfFileEditing() {
		File history = new File(
				this.projectFile.getParent() + File.separator + "Settings" + File.separator + "HistoryOfFileEditing");
		io.writeObjFile(history, this.moveList);
		this.projectInfo.setProperty(appInfo.moveIndex, String.valueOf(this.moveIndex));
	}

	void readHistoryOfFileEditing() {
		File history = new File(
				this.projectFile.getParent() + File.separator + "Settings" + File.separator + "HistoryOfFileEditing");
		if (history.exists()) {
			this.moveList = ((ArrayList) io.readObjFile(history));
			// 如果反序列化出错
			if (moveList == null)
				moveList = new ArrayList<>();
			for (int i = 0; i < this.moveList.size(); i++) {
				if (findFileShowName((String) this.moveList.get(i)) == null) {
					this.moveList.remove(i);
				}
			}
			this.moveIndex = Integer.valueOf(this.projectInfo.getProperty(appInfo.moveIndex, "0")).intValue();
			if (this.moveIndex >= this.moveList.size()) {
				this.moveIndex = (this.moveList.size() - 1);
			}
		} else {
			this.moveList = new ArrayList();
			this.moveIndex = 0;
		}
	}

	public void exit() {
		if ((text != null) && (text.isVisible())) {
			text.show();
		}
		closeEvent(null);
	}

	void closeAct(QEvent event) {
		saveWritingViewState();
		if (writingView > 0) {
			// exitwritingView(Boolean.valueOf(false));
			super.geometry = saveGeometry;
		}
		System.out.println("geometry is "+geometry());
		hide();
		if (brunnableForOSD != null)
			brunnableForOSD.stop();
		closeProject();
		settings.setValue(appInfo.appVersionKey, appInfo.appVersion);

		if (mailDataIsChanged) {
			io.writeObjFile(new File(appInfo.mailDat), mailData);
			mailDataIsChanged = false;
		}

		p("程序关闭中...");
		if (event != null) {
			event.ignore();
		}
		if (k != null) {
			k.closeKiwix();
			k = null;
		}
		this.settings.setValue(appInfo.windowOpacity, this.windowOpacity);
		this.settings.setValue(appInfo.saveMode, this.saveMode);
		this.settings.setValue(appInfo.yellowForceColor, this.yellowForceColor);
		this.settings.setValue(appInfo.alpha_windowBackground, this.alpha_windowBackground);
		this.settings.setValue(appInfo.quietMode, quietMode);
		this.settings.setValue(appInfo.lineAndDocFirst, this.LineAndDocFirst_keywords);
		settings.setValue(appInfo.withoutBlueValue, withoutBuleValue + "");
		super.whenClose();
		this.settings.sync();
	}

	protected void closeEvent(QCloseEvent event) {
		closeAct(event);
		bAction.whenBlackClose(this);
		System.exit(0);
	}

	public void openfile(fileInfo info, boolean resetView) {
		if (this.isLoadFile) {
			p("isLoadFile开关处于开启状态，停止打开文件！");
			return;
		}
		String parent = this.projectFile.getParent();
		final File file = new File(parent + File.separator + "Files" + File.separator + info.fileName);
		if (file.exists()) {
			this.infoOfCurrentEditing = info;
			this.afterLoadFile = new action() {
				public void action() {
					if (!black.this.isVisible()) {
						int isWritingView = black.this.isWritingView();
						black.this.p("还原视图" + isWritingView);
						if (isWritingView > 0) {
							black.this.writingView(true);
						} else {
							black.this.show();
						}
					}
					if (!black.this.addToMoveListEnd) {
						black.this.addMoveInfo(file.getName());
					} else {
						black.this.addMoveInfoOnEnd(file.getName());
					}
				}
			};
			openBlackFile(file);
		}
	}

	public void resetEditorView(fileInfo info) {

		int cursorPos = info.cursorPos;
		int scrollbarValue = info.scrollbarValue;
		QTextCursor cursor = this.btext.text.textCursor();
		if (info.selectionStart == -1) {
			cursor.setPosition(cursorPos);
		} else {
			cursor.setPosition(info.selectionStart, QTextCursor.MoveMode.MoveAnchor);
			cursor.setPosition(cursorPos, QTextCursor.MoveMode.KeepAnchor);
		}
		this.btext.text.setTextCursor(cursor);
		this.btext.text.verticalScrollBar().setValue(scrollbarValue);
	}

	public void openFileByTreeItem(QTreeWidgetItem currentItem) {
		if (this.isLoadFile) {
			p("isLoadFile开关处于开启状态，停止打开文件！");
			return;
		}
		if (currentItem != null) {
			fileInfo in = (fileInfo) currentItem.data(1, 0);
			// 有时in会为空，下面这行只为了不报错而已
			if (in == null)
				return;

			if (!in.isDir) {
				if (in.isRoot == -1) {
					if (in.isFiles) {
						if ((this.currentEditing != null) && (this.currentEditing.getName().equals(in.fileName))) {
							return;
						}
						changedFileOfCurrentEdit();
						this.TreeWidgetItemOfCurrentEditing = currentItem;
						this.infoOfCurrentEditing = in;
						this.textTitle.setText(currentItem.text(0));
						this.textTitle.setFileIcon(currentItem.icon(0));
						openfile(in, true);
					} else if (in.isFile) {
						if ((this.currentEditing != null) && (this.currentEditing.getName().equals(in.fileName))) {
							return;
						}
						changedFileOfCurrentEdit();
						this.TreeWidgetItemOfCurrentEditing = currentItem;
						this.infoOfCurrentEditing = in;
						this.textTitle.setText(currentItem.text(0));
						this.textTitle.setFileIcon(currentItem.icon(0));
						openfile(in, true);
					} else if (in.isKeyWordsList) {
						if ((this.currentEditing != null) && (this.currentEditing.getName().equals(in.fileName))) {
							return;
						}
						changedFileOfCurrentEdit();
						this.TreeWidgetItemOfCurrentEditing = currentItem;
						this.infoOfCurrentEditing = in;
						this.textTitle.setText(currentItem.text(0));
						this.textTitle.setFileIcon(currentItem.icon(0));
						openfile(in, true);
					}
				}
			}
		}
	}

	public void openfile() {
		Qt.MouseButtons mouseButtons = QApplication.mouseButtons();
		if (mouseButtons.value() == Qt.MouseButton.LeftButton.value()) {
			QTreeWidgetItem currentItem = this.tree.currentItem();
			openFileByTreeItem(currentItem);
		}
	}

	/**
	 * 读取文件，如果文件已经程序缓存，则从缓存中获取文件，如果没有被缓存，则从磁盘读取文件
	 * 
	 * @param file
	 * @return
	 */
	public String readBlackFile(File file) {
		String text = null;
		String showName = findFileInfoByFileName(file.getName(), null).showName;
		if (showName == null) {
			showName = "";
		}
		dataForOpenedFile openedfile = findInOpenedFiles(file.getName());
		if (openedfile != null) {
			if (openedfile.editor != null) {
				p("从缓存中提取编辑器[" + showName + "]");
				text = openedfile.editor.text.toPlainText();
			} else {
				p("从缓存中获取文件[" + showName + "]");
				text = openedfile.content;
			}
		} else {
			long readfilea = System.currentTimeMillis();
			text = io.readBlackFileByLine(file);

			long readfileb = System.currentTimeMillis();
			p("读取文件用时" + (readfileb - readfilea) + "毫秒" + "[" + showName + "]");
			addFileToOpenedFilesData(new dataForOpenedFile(file.getName(), text, null, true));
		}
		return text;
	}

	public void openBlackFile(File file) {
//		if(text!=null && text.caret!=null && text.caret.isVisible())text.caret.hide();
		if (se != null && se.isEnabled()) {
			se.setEnabled(false);
			timeMessage.setGraphicsEffect(se);
		}

		this.loadFileTime_start = System.currentTimeMillis();
		boolean isSaved = true;
		String showName = findFileInfoByFileName(file.getName(), null).showName;
		if (showName == null) {
			showName = "";
		}
		final dataForOpenedFile openedfile = findInOpenedFiles(file.getName());
		if (openedfile != null) {
			isSaved = openedfile.isSaved;
			openedfile.isSaved = true;
			if (openedfile.editor != null) {
				p("从缓存中提取编辑器[" + showName + "]");
				if (writingView == 0) {
//					this.effectForTextTitle.setOpacity(0.0D);
//					this.textTitle.setGraphicsEffect(this.effectForTextTitle);
				}
				long a = System.currentTimeMillis();
				setLoadFileMessage(showName + "\n0%");
				isLoadFile = true;
				bRunnable bt = new bRunnable(1, false, false, true, true) {
					public void run() {
						textLayout.addWidget(openedfile.editor.text);
						black.this.btext = openedfile.editor;
						black.text = openedfile.editor.text;
						if (black.this.getEditorTextZoomValue() != black.this.btext.zoomvalue) {
							black.this.setEditorZoomValue(black.this.getEditorTextZoomValue());
						}
						openedfile.editor.text.setVisible(true);
						black.text.setEnabled(true);

						File file = (File) Data("file");
						boolean isSaved = ((Boolean) Data("isSaved")).booleanValue();
						long starttime = ((Long) Data("startTime")).longValue();
						String showName = (String) Data("showName");

						black.this.whenLoadFileDone(file, isSaved, starttime, showName, true);

						black.this.setLoadFileMessage(showName + "\n100%");
					}
				};
				bt.setData("file", file);
				bt.setData("isSaved", Boolean.valueOf(isSaved));
				bt.setData("startTime", Long.valueOf(a));
				bt.setData("showName", showName);
				bt.start();
			} else {
				p("从缓存中提取文件[" + showName + "]");
				initEditor();

				text.setEnabled(true);
				this.textTitle.setEnabled(true);
				String text = openedfile.content;

				String str = text;

				long a = System.currentTimeMillis();
				this.isLoadFile = true;
				int part = text.length() / 100;
				hideAllWidget();
				new loadFile(this, str, showName, part, file, isSaved, a, false);
			}
			this.textTitle.setEnabled(true);
		} else {
			String text = null;
			long readfilea = System.currentTimeMillis();
			text = io.readBlackFileByLine(file);

			long readfileb = System.currentTimeMillis();
			p("读取文件用时" + (readfileb - readfilea) + "毫秒" + "[" + showName + "]");
			if (text == null) {
				return;
			}
			initEditor();

			addFileToOpenedFilesData(new dataForOpenedFile(file.getName(), null, this.btext, true));
			this.text.setEnabled(true);
			this.textTitle.setEnabled(true);

			String str = text;
			long a = System.currentTimeMillis();
			this.isLoadFile = true;
			this.doNotSet_UnSave = true;
			int part = text.length() / 100;
			hideAllWidget();

			new loadFile(this, str, showName, part, file, isSaved, a, false);
		}
	}

	void hideAllWidget() {
		if (this.menubar != null) {
			this.menubar.hide();
		}
		if (this.dock != null) {
			this.dock.hide();
		}
		if (this.toolbar != null) {
			this.toolbar.hide();
		}
		if (this.textTitle != null) {
			this.textTitle.hide();
		}
		if (textWidget != null) {
			textWidget.hide();
		}
		if (text != null) {
			text.hide();
		}
		if (statusBar() != null) {
			statusBar().hide();
		}
		new setMenus(this);
		synTime();
	}

	void setTextVScrollBarLocation() {
		text.verticalScrollBar().adjustSize();
		QRect g = text.verticalScrollBar().geometry();
		if (writingView == 0) {
			text.verticalScrollBar().setParent(textWidget);
			text.verticalScrollBar().setGeometry(textWidget.width() - g.width(), 0, g.width(), textWidget.height());
		} else {
			text.verticalScrollBar().setParent(this);
//			QPoint map = b.mapFrom(b.textWidget, b.textWidget.pos());
			text.verticalScrollBar().setGeometry(width() - g.width(), 0, g.width(), height());
		}
	}

	void whenLoadFileDone(File fileOfLoading, boolean isSaved, long a, String showName, boolean opened) {
		new setMenus(this);
		long b = System.currentTimeMillis();
		if (!opened) {
			setStyleForCurrentDocument(true);
			if (writingView == 0) {
				this.effectForTextTitle.setOpacity(1.0D);
				this.textTitle.show();
				if (this.btext != null) {
					text.verticalScrollBar().show();
				}
			}
		} else {
			if (writingView == 0) {
				this.effectForTextTitle.setOpacity(1.0D);
			}
		}

//		setTextMargins();

		applyColorChange();
		p("显示文件用时" + (b - a) + "毫秒" + "[" + showName + "]");

		bAction.check(fileOfLoading, this);

		this.doNotSet_UnSave = false;
		if (keywords != null) {
			keywords.text = text;
		}
		this.btext.text.setFocus();
		this.currentEditing = fileOfLoading;
		synCharCount();
		text.setUndoRedoEnabled(true);
		this.doNotSet_UnSave = true;
		if (!opened) {
			resetEditorView(findFileInfoByFileName(fileOfLoading.getName(), null));
			showAllWidget();
		}

		this.isLoadFile = false;

		documentTextChanged();
		synTextChange();
		this.doNotSet_UnSave = false;
		if (this.btext.charCountAtToday == -1) {
			String charCountOfLastInput = this.infoOfCurrentEditing.charCountOfLastInput;
			if (charCountOfLastInput != null) {
				int index = charCountOfLastInput.indexOf(" ");
				String date = charCountOfLastInput.substring(0, index);
				String charCount = charCountOfLastInput.substring(index + 1, charCountOfLastInput.length());
				if (date.equals(time.getCurrentDate("-"))) {
					this.btext.charCountAtToday = (this.charCountWithoutEnter - Integer.valueOf(charCount).intValue());
				} else {
					this.btext.charCountAtToday = this.charCountWithoutEnter;
				}
			} else {
				this.btext.charCountAtToday = this.charCountWithoutEnter;
			}
		}
		setSaved(isSaved);

		if (writingView == 0) {
			text.verticalScrollBar().show();
		} else {
			text.verticalScrollBar().hide();
		}
		this.loadFileTime_end = System.currentTimeMillis();
		updateOSDMessages(-1);
		this.textChanged = true;
		text.setFocus();

//		if (isHasWord)
//			text.setReadOnly(true);
//		else
//			text.setReadOnly(false);

//		if (rootIs(TreeWidgetItemOfCurrentEditing, reserach))
//			text.setReadOnly(true);
//		else
//			text.setReadOnly(false);

		if (rootIs(TreeWidgetItemOfCurrentEditing, reserach))
			inReserach = true;
		else
			inReserach = false;
//		btext.cursorPosChanged();

		btext.keywordListFile = findKeyWordsList(TreeWidgetItemOfCurrentEditing);

		btext.vScrollBarMaxValue = text.verticalScrollBar().maximum();
		if (this.afterLoadFile != null) {
			this.afterLoadFile.action();
			this.afterLoadFile = null;
		}
		update();
		if (needResetStyleOfDoc && opened)
			setStyleForCurrentDocument(false);
		
		text.rangeChanged();
		
		//还原自动滚屏数据
		execCommand("$bc 自动滚屏 "+infoOfCurrentEditing.autoscrollvalue);
		

	}

	/**
	 * 为整个文档设定格式 首次加载文档设定格式应该检查fileInfo中的Titles数组中的锚点数据
	 * 非首次加载文档设定格式应该检查锚点面板中的锚点数据，而不是blacktext中的titles数组，因为titles数组在切换文件时才会与锚点面板同步
	 */
	public void setStyleForCurrentDocument(boolean firstSet) {
		needResetStyleOfDoc = false;
		doNotSet_UnSave = true;
		this.btext.text.setUpdatesEnabled(false);

//		if(rootIs(TreeWidgetItemOfCurrentEditing,reserach)) {
////			this.btext.text.setUpdatesEnabled(false);
////			QTextCursor c = text.textCursor();
////			c.select(QTextCursor.SelectionType.Document);
////			QTextBlockFormat bf = new QTextBlockFormat();
////			bf.setTextIndent(getEditorFirstLineValue());
////			bf.setLineHeight(10, 4);
////			bf.setBottomMargin(0);
////			bf.setAlignment(Qt.AlignmentFlag.AlignJustify);
////			QTextCharFormat cf = bf.toCharFormat();
//////			cf.setFontFamily("黑体");
//////			cf.setFontPointSize(getEditorFontSize()+getEditorTextZoomValue());
////			c.setCharFormat(cf);
////			c.setBlockFormat(bf);
////			this.btext.text.setUpdatesEnabled(true);
//		}
//		else {
//		if (!this.infoOfCurrentEditing.isKeyWordsList) {
		// 为非关键词文档中的无格式文本设定默认格式
		QTextCursor c = text.textCursor();
		c.select(QTextCursor.SelectionType.Document);
		c.beginEditBlock();
		bAction.setStyleForTitleOrNoTitle(this, c, 0);

		c.endEditBlock();
//		}
//		}
		// 为首次加载文档设定标题格式，由于loadfile开关处于打开状态，所以无需添加撤销重做忽略信息
		if (firstSet) {
			if (infoOfCurrentEditing.titles == null) {
				infoOfCurrentEditing.titles = new ArrayList<>();
				return;
			}
			QTextCursor tc = text.textCursor();
			for (int i : infoOfCurrentEditing.titles) {
				tc.setPosition(i);
				tc.movePosition(MoveOperation.EndOfBlock, MoveMode.KeepAnchor);
				bAction.setStyleForTitleOrNoTitle(this, tc, 1);
			}
		}
		doNotSet_UnSave = false;
		this.btext.text.setUpdatesEnabled(true);

	}

	public int getOnly() {
		return Only++;
	}

	public void saveDocumentByUser() {
		if (isHasSaveTimer()) {
			removeAllSaveTimer();
		}
		saveAllDocuments();
	}

	public void setSaved(boolean saved) {
		if (saved) {
			String title = null;
			if ((this.projectFile != null) && (this.projectInfo != null)) {
				title = getProjectName() + " - " + appInfo.appName;
			} else {
				title = appInfo.appName;
			}
			setWindowTitleB(title);
			QFont font = this.textTitle.ui.lineEdit.font();
			font.setItalic(false);
			this.textTitle.ui.lineEdit.setFont(font);
		} else {
			String title = null;
			if ((this.projectFile != null) && (this.projectInfo != null)) {
				title = getProjectName() + "* - " + appInfo.appName;
			} else {
				title = appInfo.appName;
			}
			setWindowTitleB(title);
			QFont font = this.textTitle.ui.lineEdit.font();
			font.setItalic(true);
			this.textTitle.ui.lineEdit.setFont(font);
			if ((getAutoSave() != -1) && (!isHasSaveTimer())) {
				addSaveTimer();
			}
		}
		this.saved = saved;
	}

	void synCharCount() {
		int charCount = this.btext.text.charCountOfDoc();
		this.countmessage.setText(charCount + " 个字");
		update();
	}

	void addWalkInfo() {
		if (donotAddWalkInfo || text.preeditStringPos != -1) {
			return;
		}
		fileInfo.walkInfo lastMoveInfo = this.infoOfCurrentEditing.getLastWalkInfo();
		QTextCursor tcc = text.textCursor();
		int blockNumber = tcc.blockNumber();
		int length = tcc.position() - tcc.block().position();
		String text = tcc.block().text();
		if (lastMoveInfo != null) {
			if (lastMoveInfo.blockNumber == blockNumber) {
				if (lastMoveInfo.offestInLine != length) {
					this.infoOfCurrentEditing.updateLastWalkInfo(blockNumber, length, text);
				}
			} else if (lastMoveInfo.blockNumber == blockNumber - 1) {
				this.infoOfCurrentEditing.updateLastWalkInfo(blockNumber, length, text);
			} else {
				this.infoOfCurrentEditing.addWalkInfo(blockNumber, length, text);
			}
		} else {
			this.infoOfCurrentEditing.addWalkInfo(blockNumber, length, text);
		}
	}

	ArrayList<TextRegion> resetMarkTextData() {
		ArrayList<TextRegion> al = new ArrayList();
		for (TextRegion tr : this.markTextData) {
			String[] subb = cheakDocument.subString(tr.text, appInfo.keywordsSeparator);
			String[] sub = cheakDocument.subString(subb[0], "·");

			TextRegion r = null;
			TextRegion r2 = null;
			if (!subb[1].equals("")) {
				r = new TextRegion(sub[0] + appInfo.keywordsSeparator + subb[1], 0, 0);
				r.filename = subb[1];
				if (!sub[1].equals("")) {
					r2 = new TextRegion(sub[1] + appInfo.keywordsSeparator + subb[1], 0, 0);
					r2.filename = subb[1];
					r.describe = tr.text;
					r2.describe = tr.text;
				}
			} else {
				r = new TextRegion(sub[0], 0, 0);
				if (!sub[1].equals("")) {
					r.describe = tr.text;
					r2 = new TextRegion(sub[1], 0, 0);
					r2.describe = tr.text;
				}
			}
			r.showname = sub[0];
			al.add(r);
			if (!sub[1].equals("")) {
				r2.showname = sub[1];
				al.add(r2);
			}
		}
		return al;
	}

	void setAutoFindInKeywords() {
		findKeywordsByPinyin = !getBooleanValueFromSettings(appInfo.usePinyinFindKeywords, "true");
		settings.setValue(appInfo.usePinyinFindKeywords, findKeywordsByPinyin);
		setLoadFileMessage("拼音检索：" + findKeywordsByPinyin);
	}

	/**
	 * 
	 * @param fullPY全拼编码
	 * @param doublePY双拼编码，如果没有双拼编码，此参数为null
	 */
	void findMarkString(String fullPY, String doublePY) {
		if (findKeywordsByPinyin)
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String pinyin = fullPY;
//					if (markTextData == null) {
//						return;
//					}
					ArrayList<TextRegion> all = (ArrayList<TextRegion>) ba.tempData.get("document_lastPinyinSeg");
					if (all == null) {
						all = ba.getAnsjResult(false);
						ba.tempData.put("document_lastPinyinSeg", all);
					}

					if (all.size() == 0)
						all = resetMarkTextData();

					if (!cheakDocument.isAllNoChineseString(pinyin)) {
						String allchars = "";
						boolean inlineChinese = false;
						for (int i = 0; i < pinyin.length(); i++) {
							char charAt = pinyin.charAt(i);
							String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(charAt);
							if (hanyuPinyinStringArray != null) {
								String substring = hanyuPinyinStringArray[0].substring(0,
										hanyuPinyinStringArray[0].length() - 1);
								if (allchars.equals(""))
									allchars = substring;
								else
									allchars = allchars + '\'' + substring;
								inlineChinese = true;
							}
						}
						if (inlineChinese) {
							pinyin = allchars;
						}
					}
					// System.out.println(pinyin);

					ArrayList<String> args = cheakDocument.checkCommandArgs(pinyin, '\'');
					ArrayList<TextRegion> al = new ArrayList();

					for (TextRegion r : all) {
						String[] subString = cheakDocument.subString(r.text, appInfo.keywordsSeparator);
						String s = subString[0];
						int index = -1;
						int count = 0;
						A: for (int i = 0; i < s.length(); i++) {
							char c = s.charAt(i);
							ArrayList<String> arrayList = (ArrayList) args.clone();
							String[] p = PinyinHelper.toHanyuPinyinStringArray(c);
							if (p != null) {
								int j = p.length;
								for (int a = 0; a < j; a++) {
									String str = p[a];
									// 截取不带声调的拼音
									String pinyin_marktext = str.substring(0, str.length() - 1);
									for (int x = 0; x < arrayList.size(); x++) {
										String s_pinyin = (String) arrayList.get(x);
										if ((s_pinyin.equals(pinyin_marktext)) // 整个拼音全部匹配
												|| // 或者
													// 是输入的拼音串中包含的最后一个拼音，且这个拼音长度小于3，例如li，且声母匹配
										((x == arrayList.size() - 1) && (s_pinyin.length() < 3)
												&& (pinyin_marktext.indexOf(s_pinyin) == 0))) {
											if ((count == 0) && (x > 0)) {
												break A;
											}
											count++;
											if (index == -1) {
												index = a;
											}
											arrayList.remove(x);
											break;
										}
//									else if(count > 0) {
//										count=0;
//									}
									}
								}
							}
						}
						if (count > 0) {
							if (count < args.size()) {// 匹配到的数量小于输入数量，表示有字没有匹配到，将匹配度降为0
								r.start = index;
								r.end = 0;
							} else {// 按
								float aa = count;
								float bb = s.length();
								float f = aa / bb;
								r.start = index;
								r.end = ((int) (f * 100.0F));
							}
//						System.out.println(s+" 匹配数："+count+" 输入数："+args.size()+" 匹配度："+r.end);
							al.add(r);
						}
					}
					ArrayList<TextRegion> resetIndex = resetIndex(al);
					uiRun(black.this, new Runnable() {

						@Override
						public void run() {
//						System.out.print("ok");
							// TODO Auto-generated method stub
							initKeyWordsDialog();
							keywords.tree.UsedToProjectPanelOrKeywordsList = 1;
							findview = 0;
							keywords.tree.setFrameShape(QFrame.Shape.NoFrame);
							if (keywords.tree.topLevelItemCount() > 0) {
								keywords.tree.clear();
							}
							int index = 0;
							if (!getBooleanValueFromSettings(appInfo.moreFindMarkWithPinyin, "false")) {
								if (resetIndex.size() > 0) {
									TextRegion tr = (TextRegion) resetIndex.get(0);
									QTreeWidgetItem ti = getTreeItem(keywords.tree);
									String str = null;
									if (tr.filename == null) {
										str = tr.showname;
									} else {
										str = tr.showname + " (" + tr.filename + ")";
									}
									ti.setText(0, str);
									ti.setData(1, 0, tr);
									ti.setData(1, 1, Integer.valueOf(index));
									if (doublePY != null)
										tr.setData(doublePY);
									else
										tr.setData(fullPY);
									text.tr = tr;
								}
							} else {
//							keywords.tree.setFont();
								for (TextRegion tr : resetIndex) {
									if (index > 10)
										break;
//								TextRegion tr = (TextRegion) resetIndex.get(0);
									QTreeWidgetItem ti = getTreeItem(keywords.tree);
									String str = null;
									String charByNumber = getCharByNumber(index);
									if (tr.filename == null) {
										str = charByNumber + tr.showname;
									} else {
										str = charByNumber + tr.showname + " (" + tr.filename + ")";
									}
									ti.setText(0, str);
									ti.setData(1, 0, tr);
									ti.setData(1, 1, Integer.valueOf(index));
									index++;
								}
								if (index != 0) {
									TextRegion tr = (TextRegion) keywords.tree.topLevelItem(0).data(1, 0);
									if (doublePY != null)
										tr.setData(doublePY);
									else
										tr.setData(fullPY);
									text.tr = tr;
								}
							}

							keywords.hideStatusBar = true;
							keywords.donotShowMoreText = true;

							keywords.show();
							if (keywords.tree.topLevelItemCount() > 0) {
//								keywords.message.setText("输入大写字母序号可选中项目 | 输入Z可切换成全名");
								if (!getBooleanValueFromSettings(appInfo.moreFindMarkWithPinyin, "false")) {
									showDialogAtEditorCaretPosTop(text, keywords);
									keywords.tree.setCurrentItem(
											keywords.tree.topLevelItem(keywords.tree.topLevelItemCount() - 1));
									keywords.tree.scrollToItem(
											keywords.tree.topLevelItem(keywords.tree.topLevelItemCount() - 1));
								} else {
									showDialogAtLeftBottom(text, keywords);
									keywords.tree.setCurrentItem(keywords.tree.topLevelItem(0));
									keywords.tree.scrollToItem(keywords.tree.topLevelItem(0));
								}

							} else {
								keywords.hide();
							}
						}
					});

				}
			}).start();
	}

	public void documentTextChanged() {
		if (text.preeditStringPos != -1) {
			return;
		}
		ba.showKeywordsListByCurrentChar();
		if (!this.doNotSet_UnSave) {
			if (this.saved) {
				setSaved(!this.saved);
			}
			addWalkInfo();

			long currentTime = System.currentTimeMillis();
			long t = currentTime - btext.lastTextChangeTime;
			if (t < 300000) {
				btext.editTime += t;
			}
			btext.lastTextChangeTime = currentTime;

			this.textChanged = true;
			if (keywordsForFindLine != null && keywordsForFindLine.isVisible()) {
				int object = (int) ba.tempData.get("提示重新检索");
				if (object == 0) {
					ba.tempData.put("提示重新检索", 1);
//					keywordsForFindLine.tree.clear();
					int messageBoxWithYesNO = getMessageBoxWithYesNO("检索信息", "项目内容已发生改变，是否重新检索？", "重新检索", "取消", null, 0,
							true);
					if (messageBoxWithYesNO == 0) {
						keywordsForFindLine.setWindowTitle("检索结果(项目内容已改变，需要重新检索)");
//						keywordsForFindLine.hide();
					} else {
						testReFind();
					}
				}

			}
		}

	}

	void synTextChange() {
		if (text == null || text.preeditStringPos != -1) {
			return;
		}
		int a = plusCharCount;
		if (text == null) {
			return;
		}
		this.textChanged = false;
		uiRun(black.this, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				charCountWithoutEnter = text.charCountOfDoc();
			}
		});

		updateOSDMessages(2);
		uiRun(black.this, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				countmessage.setText(charCountWithoutEnter + " 个字");
				if (TreeWidgetItemOfCurrentEditing != null) {
					if (infoOfCurrentEditing.charCount != charCountWithoutEnter) {
						infoOfCurrentEditing.charCount = charCountWithoutEnter;
						fileListChanged = true;
					}
					if (infoOfCurrentEditing.isFile) {
						Object info = TreeWidgetItemOfCurrentEditing.data(99, 99);

						if (charCountWithoutEnter > 0) {
							if (info == null || !info.equals("不是空的")) {
								textTitle.setFileIcon(ico_fileOfHasText);
								TreeWidgetItemOfCurrentEditing.setIcon(0, ico_fileOfHasText);
								TreeWidgetItemOfCurrentEditing.setData(99, 99, "不是空的");
							}
						} else if (info == null || !info.equals("空的")) {
							textTitle.setFileIcon(ico_file);
							TreeWidgetItemOfCurrentEditing.setIcon(0, ico_file);
							TreeWidgetItemOfCurrentEditing.setData(99, 99, "空的");
						}
					}
				}
			}
		});

//		StringBuffer sb = new StringBuffer();
//		String c = c(a,plusCharCount,1000);
//		if(c == null)return;
//		else if(c.indexOf("-") == -1 && !c.equals("0")) sb.append("+"+c);
//		else sb.append(c);
//		
//		if(!sb.toString().isEmpty()) {
//			int time = (int) (btext.editTime/3600000);
//			if(time>0)sb.append("<br>约"+time+"小时");			
//			
//			removeAllTimer(-22222222);
//			timerInfo ti = new timerInfo(-22222222, 60000, sb.toString(), null, false, false);
//			ti.hideLeftTime = true;
//			addTimer(ti);
//			QApplication.invokeAndWait(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					if(text.isVisible())
//						startBeepSound(1, 3);
//				}
//			});
//			
//		}
	}

	static String c(int v, int v2, int value) {
		int x = v / value * value;
		int x_ = v2 / value * value;
		if (x != x_)
			return String.valueOf(x_);
		else
			return null;
	}

	void selectionChanged() {
		showFind = false;
		if (!text.isAccicesable())
			return;

		int charCount = this.text.charCountOfDoc();
		int length = this.text.charCountOfSelection();
		if (length > 0) {
			monitorOff = true;
			String mes = length + "/" + charCount + " 个字";
			countmessage.setText(mes);
			if (writingView > 0) {
				ArrayList<timerInfo> timer = getAllTimer(-33456);
				timerInfo ti = null;
				if (timer.size() > 0) {
					ti = timer.get(0);
					ti.timerName = mes + " | " + caretPos.text();
				} else {
					ti = new timerInfo(true, mes + " | " + caretPos.text(), null);
					ti.showProgress = false;
					ti.type = -33456;
					ti.isNew = true;
					addTimer(ti);
				}
				//选中文本后不会立即提示字符数，因为如果立即提示会有性能损失，可能阻塞UI线程
//				if (brunnableForOSD != null) {
//					new Thread(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							synTimeAction(brunnableForOSD);
//						}
//					}).start();
//				}
			}
		} else {
			countmessage.setText(charCount + " 个字");
			monitorOff = false;
			removeAllTimer(-33456);
		}
		repaint();
	}

	public void saveBlackFile(final File file, final String text) {
		new Thread(new Runnable() {
			public void run() {
				io.writeBlackFile(file, text, null);
			}
		})

				.start();
	}

	/**
	 * 保存所有文件，包括当前所编辑的文件 如果要保存全部文件应该使用此方法，saveAllFile方法不会保存正在编辑的文件
	 */
	public void saveAllDocuments() {
		saveDocuments(true);
	}

	public void changedFileOfCurrentEdit() {
		if (this.currentEditing == null) {
			return;
		}
		this.lastEditFile = this.currentEditing;

		dataForOpenedFile find = findInOpenedFiles(this.currentEditing.getName());
		if (find != null) {
			find.editor = this.btext;
			find.isSaved = this.saved;
		} else {
			addFileToOpenedFilesData(
					new dataForOpenedFile(this.currentEditing.getName(), null, this.btext, this.saved));
		}
		if (text.textCursor().selectedText().length() == 0) {
			int position = this.btext.text.textCursor().position();
			this.infoOfCurrentEditing.cursorPos = position;
			this.infoOfCurrentEditing.selectionStart = -1;
		} else {
			QTextCursor tc = text.textCursor();
			int selectionStart = tc.selectionStart();
			int selectionEnd = tc.selectionEnd();
			this.infoOfCurrentEditing.cursorPos = selectionEnd;
			this.infoOfCurrentEditing.selectionStart = selectionStart;
		}
		saveCharCountOfLastInput();
		int value = this.btext.text.verticalScrollBar().value();
		this.infoOfCurrentEditing.scrollbarValue = value;
		this.fileListChanged = true;

		this.infoOfCurrentEditing = null;
		this.TreeWidgetItemOfCurrentEditing = null;
		deleteEditor();

		this.textTitle.clearTextTitle();
		this.textTitle.setEnabled(false);
		this.currentEditing = null;
	}

	String findFileShowName(String filename) {
		fileInfo findFileInfoByFileName = findFileInfoByFileName(filename, null);
		if (findFileInfoByFileName != null) {
			return findFileInfoByFileName.showName;
		}
		return null;
	}

	/**
	 * 保存所有文件，但不包括当前文件
	 */
	public void saveAllFile() {
		for (dataForOpenedFile opened : this.openedFiles) {
			if (!opened.isSaved) {
				p("写入文件(" + opened.filename + ")[缓存]" + findFileShowName(opened.filename));
				saveBlackFile(getFile(opened.filename), opened.editor.text.toPlainText());

				opened.isSaved = true;
			}
		}
	}

	public void clearTempFiles() {
		for (dataForOpenedFile opened : this.openedFiles) {
			if (!opened.isSaved) {
				p("写入文件(" + opened.filename + ")[缓存]" + findFileShowName(opened.filename));
				saveBlackFile(getFile(opened.filename), opened.editor.text.toPlainText());
			}
		}
		disposeOpenedFile();
		p("缓存的文件数量：" + this.openedFiles.size());
	}

	void saveCharCountOfLastInput() {
		this.infoOfCurrentEditing.charCountOfLastInput = (time.getCurrentDate("-") + " "
				+ (this.charCountWithoutEnter - this.btext.charCountAtToday));
	}

	public void saveDocuments(boolean saveView) {
		if ((this.currentEditing != null) && (!this.saved)) {
			String str = text.toPlainText();
			p("写入文件" + findFileShowName(this.currentEditing.getName()));
			String text = str;
			saveBlackFile(this.currentEditing, text);
			setSaved(true);
		}
		saveAllFile();
		if (saveView) {
			if (text.textCursor().selectedText().length() == 0) {
				int position = this.btext.text.textCursor().position();
				this.infoOfCurrentEditing.cursorPos = position;
				this.infoOfCurrentEditing.selectionStart = -1;
			} else {
				QTextCursor tc = text.textCursor();
				int selectionStart = tc.selectionStart();
				int selectionEnd = tc.selectionEnd();
				this.infoOfCurrentEditing.cursorPos = selectionEnd;
				this.infoOfCurrentEditing.selectionStart = selectionStart;
			}
			int value = this.btext.text.verticalScrollBar().value();
			this.infoOfCurrentEditing.scrollbarValue = value;
			saveCharCountOfLastInput();
			this.fileListChanged = true;
		}
	}

	public void closeCurrentEditing() {
		if (this.currentEditing != null) {
			if (this.currentEditing != null) {
				this.lastEditFile = this.currentEditing;
			}
			saveDocuments(true);
			this.infoOfCurrentEditing = null;
			this.TreeWidgetItemOfCurrentEditing = null;
			deleteEditor();
			this.textTitle.clearTextTitle();
			this.textTitle.setEnabled(false);
			this.currentEditing = null;
		}
	}

	public void findInGoldenDict() {
		findInGoldenDict(null);
	}

	static void uiRun(black b, Runnable run) {
		hasUIRunnable = true;
		try {
			QApplication.staticMetaObject.invokeMethod(() -> b.run(run), ConnectionType.AutoConnection);
		} catch (QUnsuccessfulInvocationException e) {
			e.printStackTrace();
		}
//		System.out.println("-----------");
		while (hasUIRunnable) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
//		System.out.println("=============");

	}

	void run(Runnable run) {
		hasUIRunnable = true;
		run.run();
		hasUIRunnable = false;
	}

	boolean checkDevcon() {
		String devconPath = "./Tools/Devcon.exe";
		File devconApp = new File(devconPath);
		if (!devconApp.exists())
			return false;
		else {
			try {
				if (!MD5.getMD5Checksum(devconPath).equals("8dd27f1aa717c3dca0b1b9c9e47c03f5")) {
					uiRun(this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getMessageBox("文件被篡改", "devcon程序校验错误，该程序可能已被篡改！");
						}
					});
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	boolean checkRunAsTool() {
		if (isAdmin()) {
			return true;
		}
		String runasToolPath = "./Tools/RunAs/lsrunas.exe";
		String getMailPath = "./bin_//getMail.jar";
		String javawPath = "./jre/bin/javaw.exe";
		File runasApp = new File(runasToolPath);
		if (!runasApp.exists())
			return false;
		else {
			try {
				if (!MD5.getMD5Checksum(runasToolPath).equals("e189d3864af704466862cf2f98c18d1d")) {
					uiRun(black.this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getMessageBox("文件被篡改", "lsrunas程序校验错误，该程序可能已被篡改！");
						}
					});
					return false;
				}
				if (!MD5.getMD5Checksum(javawPath).equals("0a636d8e3574b2a5bf0aff218b145222")) {
					uiRun(black.this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getMessageBox("文件被篡改", "javaw.exe校验错误，该文件可能已被篡改！");
						}
					});
					return false;
				}
				if (!MD5.getMD5Checksum(getMailPath).equals(sum_getMail)) {
					uiRun(black.this, new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getMessageBox("文件被篡改", "getMail.jar校验错误，该文件可能已被篡改！");
						}
					});
					return false;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	public void findInGoldenDict(String find) {
//		if (!checkRunAsTool())
//			return;
		boolean canFind = false;
//		String user = "golden";
//		if (userName.equals("Administrator")) {
//			canFind = true;
//			user = "Administrator";
//		} else {
//			String timeServer = "http://www.ntsc.ac.cn";// 中国科学院国家授时中心服务器地址
//			String hourOfDay = time.getWebsiteDateHourOfDay(timeServer);
//			if (hourOfDay.equals("@@")) {
//				p("不允许启动GoldenDict，因为无法获取网络时间！");
//				return;
//			} else if (Integer.valueOf(hourOfDay) < 22) {
//				p("不允许启动GoldenDict，因为不在允许的时间段！");
//				return;
//			}
//		}

		String path = getGoldenDictPath();
		if ((path != null) && (new File(path).exists())) {
			canFind = true;
		} else {
			String filepath = getFileDialog("选择Goldendict可执行文件", "可执行文件 (Goldendict.exe)");
			if (!filepath.equals("")) {
				setGoldenDictPath(filepath);
				findInGoldenDict(find);
				return;
			}
		}
		if (canFind) {
//			DesUtils des = new DesUtils("black"); // 自定义密钥
//			String key = null;
//			String value = getStringValueFromSettings(appInfo.additionalInfo, "");
//			if (value.isEmpty()) {
//				p("不允许启动GoldenDict，因为尚未设定管理员加密密码！");
//				return;
//			}
//			try {
//				key = des.decrypt(value);
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				p("对加密密码解密时出现错误！");
//				return;
//			}

			String findtext = null;
			String str;
			if (find == null) {
				str = text.textCursor().selectedText();
				if (str.length() > 0) {
					findtext = str;
				} else {
					findtext = seclectText(text, false);
				}
				if (findtext != null) {
				}
			} else {
				findtext = find;
			}
			try {
//				Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:"+user+" /password:"+key+" /domain: /command:\""+path+ " \"" + findtext + "\"\" /runpath:c:");
				Runtime.getRuntime().exec(path + " \"" + findtext + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void showLogs() {
		File file = new File("./Logs/log.txt");
		if (file.exists()) {
			String readTextFile = io.readTextFile(file, "gbk");
			getBMessageBox("WinRun4J日志 | " + file.length() / 1024 + "KB", readTextFile).show();
		} else {
			getMessageBox("文件不存在", "WinRun4J Logs文件不存在\n文件路径：./Logs/log.txt");
		}
	}

	void clearWinRun4jLogs() {
		File file = new File("./Logs/log.txt");
		if (file.exists()) {
			boolean isclear = io.writeTextFile(file, "", "gbk");
			if (isclear)
				getMessageBox("清理成功", "已成功清理WinRun4J日志");
			else
				getMessageBox("清理不成功", "文件可能正在使用中，WinRun4J可能正在运行");
		} else {
			getMessageBox("文件不存在", "WinRun4J Logs文件不存在\n文件路径：./Logs/log.txt");
		}
	}

	public static String seclectText(bTextEdit text, boolean isSelection) {
		QTextCursor tc = text.textCursor();
		int pos = tc.position();
		int pos_start = -1;
		int pos_end = -1;
		String[] syls = new String[] { "，", "。", ",", ".", "？", "?", "！", "!", "“", "”" };
		do {
			if (tc.atBlockStart()) {
				pos_start = pos;
				break;
			}
			tc.movePosition(QTextCursor.MoveOperation.PreviousCharacter);
			int position = tc.position();
			tc.movePosition(QTextCursor.MoveOperation.NextCharacter, QTextCursor.MoveMode.KeepAnchor);
			String str = tc.selectedText();
			char charAt = str.charAt(0);
			if (cheakDocument.cheakOnly(charAt, syls)) {
				pos_start = position + 1;
				break;
			}
			tc.setPosition(position);
		} while (!tc.atBlockStart());
		pos_start = tc.position();

		tc.setPosition(pos);
		for (;;) {
			if (tc.atBlockEnd()) {
				pos_end = pos;
				break;
			}
			tc.movePosition(QTextCursor.MoveOperation.NextCharacter, QTextCursor.MoveMode.KeepAnchor);
			String str = tc.selectedText();
			char charAt = ' ';
			if (!str.equals("")) {
				charAt = str.charAt(0);
			}
			if (cheakDocument.cheakOnly(charAt, syls)) {
				pos_end = tc.position() - 1;
				break;
			}
			if (tc.atBlockEnd()) {
				pos_end = tc.position();
				break;
			}
			tc.clearSelection();
		}
		tc.clearSelection();
		tc.setPosition(pos_start);
		tc.setPosition(pos_end, QTextCursor.MoveMode.KeepAnchor);
		if (isSelection) {
			text.setTextCursor(tc);
		}
		return tc.selectedText();
	}

	public void setGoldenDictPath(String path) {
		DesUtils desUtils = new DesUtils("black");
		try {
			this.settings.setValue(appInfo.goldenDictPath, desUtils.encrypt(path));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getGoldenDictPath() {
		DesUtils desUtils = new DesUtils("black");
		String value = null;
		String path = (String) settings.value(appInfo.goldenDictPath);
		if (path == null)
			return null;
		try {
			value = desUtils.decrypt(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public String getDirDialog(String title) {
		String dir = QFileDialog.getExistingDirectory(this, title);
		return dir;
	}

	public String getFileDialog(String title, String filter) {
		String openFileName = QFileDialog.getOpenFileName(this, title, "", filter).result;
		return openFileName;
	}

	public String getFileDialog() {
		String openFileName = QFileDialog.getOpenFileName(this, "", "", "Images (*.png *.xpm *.jpg)").result;

		return openFileName;
	}

	void createProject() {
		if (!isAdmin())
			return;

		String saveFileName = QFileDialog.getSaveFileName(this, "保存" + appInfo.appName + "项目", "",
				appInfo.appName + "项目文件 (*.blackpro)").result;
		if ((saveFileName != null) && (!saveFileName.equals(""))) {
			File f = new File(saveFileName);
			int lastIndexOf = f.getName().lastIndexOf('.');
			if (lastIndexOf == -1)
				lastIndexOf = f.getName().length();
			String projectName = f.getName().substring(0, lastIndexOf);
			createAndOpenProject(f.getParent(), projectName);
		}
	}

	QTreeWidgetItem findTreeItemByFileInfo(fileInfo fileinfo, QTreeWidgetItem in) {
		QTreeWidgetItem i = null;
		if (in == null) {
			for (int a = 0; a < this.tree.topLevelItemCount(); a++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(a);
				QTreeWidgetItem findTreeItemByFileName = findTreeItemByFileInfo(fileinfo, topLevelItem);
				if (findTreeItemByFileName != null) {
					i = findTreeItemByFileName;
					break;
				}
			}
		} else {
			fileInfo info = (fileInfo) in.data(1, 0);
			if (info.equals(fileinfo)) {
				i = in;
			} else if (in.childCount() > 0) {
				for (int a = 0; a < in.childCount(); a++) {
					QTreeWidgetItem child = in.child(a);
					i = findTreeItemByFileInfo(fileinfo, child);
					if (i != null) {
						break;
					}
				}
			}
		}
		return i;
	}

	QTreeWidgetItem findTreeItemByFileName(String filename, QTreeWidgetItem in) {
		QTreeWidgetItem i = null;
		if (in == null) {
			for (int a = 0; a < this.tree.topLevelItemCount(); a++) {
				QTreeWidgetItem topLevelItem = this.tree.topLevelItem(a);
				QTreeWidgetItem findTreeItemByFileName = findTreeItemByFileName(filename, topLevelItem);
				if (findTreeItemByFileName != null) {
					i = findTreeItemByFileName;
					break;
				}
			}
		} else {
			fileInfo info = (fileInfo) in.data(1, 0);
			if (filename.equals(info.fileName)) {
				i = in;
			} else if (in.childCount() > 0) {
				for (int a = 0; a < in.childCount(); a++) {
					QTreeWidgetItem child = in.child(a);
					i = findTreeItemByFileName(filename, child);
					if (i != null) {
						break;
					}
				}
			}
		}
		return i;
	}

	fileInfo findFileInfoByFileName(String filename, fileInfo in) {
		fileInfo i = null;
		if (in == null) {
			for (fileInfo info : this.filesList) {
				fileInfo findFileInfoByFileName = findFileInfoByFileName(filename, info);
				if (findFileInfoByFileName != null) {
					i = findFileInfoByFileName;
					break;
				}
			}
		} else if (filename.equals(in.fileName)) {
			i = in;
		} else if (in.subfiles != null) {
			for (fileInfo info : in.subfiles) {
				i = findFileInfoByFileName(filename, info);
				if (i != null) {
					break;
				}
			}
		}
		return i;
	}

	void createAndOpenProject(String dirPath, String projectName) {
		File prodir = new File(dirPath + File.separator + projectName);
		boolean ok = prodir.mkdir();
		if (ok) {
			File profile = new File(prodir.getAbsolutePath() + File.separator + projectName + ".blackpro");
			Properties pro = new Properties();
			pro.setProperty("fileindex", "0");
			pro.setProperty(appInfo.projectName, projectName);
			cfg_read_write.cfg_write(pro, profile);

			File settingsdir = new File(profile.getParent() + File.separator + "Settings");
			File filesdir = new File(profile.getParent() + File.separator + "Files");
			if (!settingsdir.exists()) {
				settingsdir.mkdir();
			}
			if (!filesdir.exists()) {
				filesdir.mkdir();
			}
			File listFile = new File(profile.getParent() + File.separator + "Settings" + File.separator + "fileslist");
			ArrayList<Object> list = new ArrayList();

			fileInfo draft_info = new fileInfo("草稿", null);
			draft_info.isRoot = 0;
			draft_info.initFloder();

			fileInfo reserach_info = new fileInfo("研究", null);
			reserach_info.isRoot = 1;
			reserach_info.initFloder();

			fileInfo recycle_info = new fileInfo("废纸篓", null);
			recycle_info.isRoot = 2;
			recycle_info.initFloder();

			list.add(draft_info);
			list.add(reserach_info);
			list.add(recycle_info);

			io.writeObjFile(listFile, list);
			closeProject();
			try {
				openProject(profile.getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 在草稿箱内添加一个新文件
			tree.setCurrentItem(draft);
			addFile();
			QTreeWidgetItem currentItem = this.tree.currentItem();
			openFileByTreeItem(currentItem);
		}
	}

	void resetTree() {
		this.tree.clear();
		if (keywordsForFindLine != null)
			keywordsForFindLine.tree.clear();
	}

	void showOpenProjectDialog() {
		String openFileName = QFileDialog.getOpenFileName(this, "打开项目", "", appInfo.appName + "项目 (*.blackpro)").result;
		if (!openFileName.equals("")) {
			closeProject();
			try {
				openProject(openFileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			showAllWidget();
		} else {
			if (projectFile != null)
				return;

			File file = new File("./Pros/默认项目/默认项目.blackpro");
			if (file.exists())
				try {
					openProject(file.getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
	}

	void checkProjectFile() {
		File filedir = new File(this.projectFile.getParent() + File.separator + "Files" + File.separator);
		if ((filedir.exists()) && (filedir.isDirectory())) {
			String[] list = filedir.list();
			this.superfluousFiles = new ArrayList();
			String[] arrayOfString1;
			int j = (arrayOfString1 = list).length;
			for (int i = 0; i < j; i++) {
				String s = arrayOfString1[i];
				fileInfo info = findFileInfoByFileName(s, null);
				if (info == null) {
					p("文件(" + s + ")在Files目录里存在，但fileslist中没有它的信息");
					this.superfluousFiles.add(s);
				}
			}
			String filenames = "";
			for (String s : this.superfluousFiles) {
				filenames = filenames + s + "\n";
			}
			if ((this.superfluousFiles != null) && (this.superfluousFiles.size() > 0)) {
				QMessageBox message = new QMessageBox(this);
				QPushButton cancel = message.addButton(QMessageBox.StandardButton.Cancel);
				cancel.setText("取消");
				QPushButton ok = message.addButton(QMessageBox.StandardButton.Ok);
				ok.setText("确定");
				ok.clicked.connect(this, "deleteSuperfluousFiles()");

				message.setText("下面的文件在Files目录里存在，但fileslist中没有它们的信息\n是否从磁盘中删除这些文件？此操作将无法撤销！\n" + filenames);
				message.setWindowTitle("清理项目文件");
				message.setIconPixmap(this.ico_warning.pixmap(48, 48));
				message.show();
			}
		}
	}

	void deleteSuperfluousFiles() {
		if ((this.superfluousFiles != null) && (this.superfluousFiles.size() > 0)) {
			StringBuilder sb = new StringBuilder();
			for (String filename : this.superfluousFiles) {
				File file = getFile(filename);
				if (file.exists()) {
					String isD = null;
					boolean isDelete = file.delete();

					if (isDelete) {
						isD = "成功";
					} else {
						isD = "失败(程序关闭时将再次尝试删除)";
						file.deleteOnExit();
					}
					if (dbm) {
						p("从磁盘删除文件 " + filename + " " + isD);
						sb.append("从磁盘删除文件 " + filename + " " + isD + "\n");
					}
				}
			}
			this.superfluousFiles = null;
			if (!dbm)
				return;
			bmessageBox bMessageBox = getBMessageBox("文件清理结果", sb.toString());
			bMessageBox.show();
		}
	}

	void disposeOpenedFile() {
		p("开始清空缓存数据...");
		for (dataForOpenedFile opened : this.openedFiles) {
			blacktext editor = opened.editor;
			if (editor != null) {
				editor.text.dispose();
				editor.text = null;
				editor = null;
			}
			opened.content = null;
		}
		this.openedFiles.removeAll(this.openedFiles);
		p("缓存文件数量：" + this.openedFiles.size());
	}

	void synFindHistoryToCombox() {
		this.findBox.clear();
		if (this.finddialog != null) {
			this.finddialog.ui.findText.clear();
		}
		for (int i = this.findHistory.size() - 1; i >= 0; i--) {
			this.findBox.insertItem(this.findHistory.size() - 1 - i, (String) this.findHistory.get(i));
			if (this.finddialog != null) {
				this.finddialog.ui.findText.insertItem(this.findHistory.size() - 1 - i,
						(String) this.findHistory.get(i));
			}
		}
	}

	/**
	 * 打开项目 如果已经打开了一个项目，则先关闭项目
	 * 
	 * @param projcetFilePath
	 * @throws IOException
	 */
	void openProject(String projcetFilePath) throws IOException {
		File profile = new File(projcetFilePath);
		if (projectFile != null && (projectFile.getCanonicalPath().equals(profile.getCanonicalPath()))) {
			p("什么也没做，因为项目已经打开了");
			return;
		}
		if (projectFile != null && projectInfo != null) {
			closeProject();
		}
		if (profile.exists()) {
			this.projectInfo = cfg_read_write.cfg_read(profile);
			if (profile.length() == 0 || projectInfo == null || projectInfo.isEmpty()) {
//				userName = "Administrator";
				menubar.show();
				show();
				getMessageBox("载入项目出错", "载入项目时出现错误");

//				getMessageBox("载入项目出错", "载入项目时出现错误\n\n已切换到管理员模式，以便恢复数据");
			}
			String lastBackUpTime_string = projectInfo.getProperty(appInfo.lastBackUpTime, "0");
			lastBackUpTime = Long.valueOf(lastBackUpTime_string);

			this.filesListFile = new File(
					profile.getParent() + File.separator + "Settings" + File.separator + "fileslist");
			Object readObjFile = io.readObjFile(this.filesListFile);
			if (readObjFile != null) {
				this.filesList = ((ArrayList) readObjFile);
				this.projectFile = profile;
				readFindHistory();
				readHistoryOfFileEditing();
				if (this.moveList.size() > 0) {
					this.doNotAddToMoveList = true;
				}
				String editing = this.projectInfo.getProperty(appInfo.editing);

				checkProjectFile();
				repearFileInfos(null);
				ListProjectFiles();
				setGitMenu();
				if (editing != null) {
					File file = getFile(editing);
					if (file.exists()) {
						QTreeWidgetItem findTreeItemByFileName = findTreeItemByFileName(file.getName(), null);
						if (findTreeItemByFileName != null) {
							this.TreeWidgetItemOfCurrentEditing = findTreeItemByFileName;
							this.infoOfCurrentEditing = ((fileInfo) findTreeItemByFileName.data(1, 0));
							this.tree.setCurrentItem(findTreeItemByFileName);

							openFileByTreeItem(this.TreeWidgetItemOfCurrentEditing);
						}
					} else
						show();
				} else
					show();
				String lastEditFile = this.projectInfo.getProperty(appInfo.lastEditFile);
				if (lastEditFile != null) {
					File lasteditfile = getFile(lastEditFile);
					if (lasteditfile.exists()) {
						this.lastEditFile = lasteditfile;
					}
				}
				setSaved(true);
				this.toolbar.setEnabled(true);
				statusBar().setEnabled(true);
				if (this.moveList.size() > 0) {
					this.doNotAddToMoveList = false;
				}
				showAllWidget();

				readMarkFile();
				if (marktext != null) {
					getMarkFileText();
				}

			} else {
				projectFile = null;
				projectInfo = null;
				menubar.show();
				show();
				getMessageBox("加载项目", "加载项目时出现错误!请检查项目文件是否有效");
				whenNoProjectOpend();
			}
		} else {
			whenNoProjectOpend();
		}
//		showAllWidget();
	}

	ArrayList<QTreeWidgetItem> getFolderFiles(fileInfo info) {
		ArrayList<fileInfo> subfiles = info.subfiles;
		ArrayList<QTreeWidgetItem> al = new ArrayList();
		if (subfiles == null) {
			return al;
		}
		for (fileInfo in : subfiles) {
			if (in.isDir) {
				QTreeWidgetItem qt = new QTreeWidgetItem();
				Qt.ItemFlags flags = qt.flags();
				flags.set(new Qt.ItemFlag[] { Qt.ItemFlag.ItemIsEditable });
				qt.setFlags(flags);
				qt.setText(0, in.showName);
				qt.setData(1, 0, in);
				qt.setIcon(0, this.ico_folder);
				ArrayList<QTreeWidgetItem> folderFiles = getFolderFiles(in);
				qt.addChildren(folderFiles);

				al.add(qt);
			} else if (in.isFiles) {
				QTreeWidgetItem qt = new QTreeWidgetItem();
				Qt.ItemFlags flags = qt.flags();
				flags.set(new Qt.ItemFlag[] { Qt.ItemFlag.ItemIsEditable });
				qt.setFlags(flags);
				qt.setText(0, in.showName);
				qt.setData(1, 0, in);
				qt.setIcon(0, this.ico_files);
				ArrayList<QTreeWidgetItem> folderFiles = getFolderFiles(in);
				qt.addChildren(folderFiles);

				al.add(qt);
			} else if (in.isKeyWordsList) {
				QTreeWidgetItem qt = new QTreeWidgetItem();
				Qt.ItemFlags flags = qt.flags();
				flags.set(new Qt.ItemFlag[] { Qt.ItemFlag.ItemIsEditable });
				qt.setFlags(flags);
				qt.setText(0, in.showName);
				qt.setData(1, 0, in);
				qt.setIcon(0, this.ico_keywordsList);
				al.add(qt);
			} else if (in.isFile) {
				QTreeWidgetItem qt = new QTreeWidgetItem();
				Qt.ItemFlags flags = qt.flags();
				flags.set(new Qt.ItemFlag[] { Qt.ItemFlag.ItemIsEditable });
				qt.setFlags(flags);
				qt.setText(0, in.showName);
				qt.setData(1, 0, in);
				qt.setIcon(0, this.ico_file);
				al.add(qt);
			}
		}
		return al;
	}

	void ListProjectFiles() {
		if (this.filesList != null) {
			resetTree();
			for (int i = 0; i < this.filesList.size(); i++) {
				fileInfo fi = (fileInfo) this.filesList.get(i);
				if (fi.isRoot != -1) {
					if (fi.isRoot == 0) {
						this.draft = new QTreeWidgetItem(this.tree);
						this.draft.setIcon(0, this.ico_draft);
						this.draft.setText(0, fi.showName);
						this.draft.setData(1, 0, fi);
						this.draft.addChildren(getFolderFiles(fi));
						setEditable(this.draft, false);
					} else if (fi.isRoot == 1) {
						this.reserach = new QTreeWidgetItem(this.tree);
						this.reserach.setIcon(0, this.ico_reserach);
						this.reserach.setText(0, fi.showName);
						this.reserach.setData(1, 0, fi);
						this.reserach.addChildren(getFolderFiles(fi));
						setEditable(this.reserach, false);
					} else if (fi.isRoot == 2) {
						this.recycle = new QTreeWidgetItem(this.tree);
						this.recycle.setIcon(0, this.ico_recycle);
						this.recycle.setText(0, fi.showName);
						this.recycle.setData(1, 0, fi);
						this.recycle.addChildren(getFolderFiles(fi));
						setEditable(this.recycle, false);
					}
				} else if (fi.isDir) {
					QTreeWidgetItem qt = new QTreeWidgetItem(this.tree);
					qt.addChildren(getFolderFiles(fi));
					qt.setText(0, fi.showName);
					qt.setIcon(0, this.ico_folder);
					qt.setData(1, 0, fi);
					setEditable(qt, false);
				} else if (fi.isFiles) {
					QTreeWidgetItem qt = new QTreeWidgetItem(this.tree);
					qt.addChildren(getFolderFiles(fi));
					qt.setText(0, fi.showName);
					qt.setIcon(0, this.ico_files);
					qt.setData(1, 0, fi);
					setEditable(qt, false);
				} else if (fi.isKeyWordsList) {
					QTreeWidgetItem qt = new QTreeWidgetItem(this.tree);
					qt.setText(0, fi.showName);
					qt.setIcon(0, this.ico_keywordsList);
					qt.setData(1, 0, fi);
					setEditable(qt, false);
				} else if (fi.isFile) {
					QTreeWidgetItem qt = new QTreeWidgetItem(this.tree);
					qt.setText(0, fi.showName);
					qt.setIcon(0, this.ico_file);
					qt.setData(1, 0, fi);
					setEditable(qt, false);
				}
			}
			applySettingsToAllTreeWidget(null);
		}
	}

	QTreeWidgetItem getTreeItem(QTreeWidgetItem parent) {
		return new QTreeWidgetItem(parent);
	}

	QTreeWidgetItem getTreeItem(QTreeWidget parent) {
		return new QTreeWidgetItem(parent);
	}

	QTreeWidgetItem findKeyWordsList(QTreeWidgetItem current) {
		QTreeWidgetItem parent = current.parent();
		if (parent != null) {
			for (int i = 0; i < parent.childCount(); i++) {
				QTreeWidgetItem child = parent.child(i);
				fileInfo in = (fileInfo) child.data(1, 0);
				if (in.isKeyWordsList) {
					return child;
				}
			}
			return findKeyWordsList(parent);
		}
		for (int i = 0; i < this.tree.topLevelItemCount(); i++) {
			QTreeWidgetItem child = this.tree.topLevelItem(i);
			fileInfo in = (fileInfo) child.data(1, 0);
			if (in.isKeyWordsList) {
				return child;
			}
		}
		return null;
	}

	void addKeyWordsListToProject(String showname) {
		String showName = null;
		if (showname != null) {
			showName = showname;
		} else {
			showName = "未命名文档";
		}
		int fileindex = Integer.valueOf(this.projectInfo.getProperty("fileindex", "0")).intValue();
		File newfile = new File(
				this.projectFile.getParent() + File.separator + "Files" + File.separator + fileindex + ".black");
		if (!newfile.exists()) {
			io.writeBlackFile(newfile, "", null);
			QTreeWidgetItem parent = this.tree.currentItem();
			if (parent != null) {
				fileInfo fi = (fileInfo) parent.data(1, 0);
				if ((fi.isDir) || (fi.isRoot != -1) || (fi.isFiles)) {
					fileInfo in = new fileInfo(showname, newfile.getName());
					in.isKeyWordsList = true;
					fi.subfiles.add(in);
					QTreeWidgetItem item = new QTreeWidgetItem(parent);
					item.setData(1, 0, in);
					item.setIcon(0, this.ico_keywordsList);
					item.setText(0, showName);
					parent.setExpanded(true);

					this.mustSaveFileListFile = true;
					setEditable(item, true);
				} else if (fi.isFile) {
					fi.isFiles = true;
					fi.isFile = false;
					fi.initFloder();
					parent.setIcon(0, this.ico_files);
					fileInfo in = new fileInfo(showname, newfile.getName());
					in.isKeyWordsList = true;
					fi.subfiles.add(in);
					QTreeWidgetItem treeItem = getTreeItem(parent);
					treeItem.setData(1, 0, in);
					treeItem.setText(0, showName);
					treeItem.setIcon(0, this.ico_keywordsList);
					parent.setExpanded(true);

					this.mustSaveFileListFile = true;
					setEditable(treeItem, true);
				}
			} else {
				fileInfo in = new fileInfo(showname, newfile.getName());
				in.isKeyWordsList = true;

				this.filesList.add(in);
				QTreeWidgetItem item = new QTreeWidgetItem(this.tree);
				item.setData(1, 0, in);
				item.setIcon(0, this.ico_keywordsList);
				item.setText(0, showName);

				this.mustSaveFileListFile = true;
				setEditable(item, true);
			}
			this.projectInfo.setProperty("fileindex", String.valueOf(fileindex + 1));
			cfg_read_write.cfg_write(this.projectInfo, this.projectFile);
		}
	}

	void addFile() {
		addFileToProject(null);
	}

	void addFileToProject(String showname) {
		String showName = null;
		if (showname != null) {
			showName = showname;
		} else {
			showName = "未命名文档";
		}
		int fileindex = Integer.valueOf(this.projectInfo.getProperty("fileindex", "0")).intValue();
		File newfile = new File(
				this.projectFile.getParent() + File.separator + "Files" + File.separator + fileindex + ".black");
		// 检查文件指针可用性，如果不可用则自动修复index错误，并再次获取文件指针
		if (newfile.exists()) {
			resetProjectFileIndex(false);
		}

		if (!newfile.exists()) {
			QTreeWidgetItem parent = this.tree.currentItem();
			if (parent != null) {
				fileInfo fi = (fileInfo) parent.data(1, 0);
				if ((fi.isDir) || (fi.isRoot != -1) || (fi.isFiles)) {
					fileInfo in = new fileInfo(showname, newfile.getName());
					in.isFile = true;
					fi.subfiles.add(in);
					QTreeWidgetItem item = new QTreeWidgetItem(parent);
					item.setData(1, 0, in);
					item.setIcon(0, this.ico_file);
					item.setText(0, showName);
					parent.setExpanded(true);

					this.mustSaveFileListFile = true;
					setEditable(item, true);
					io.writeBlackFile(newfile, "", null);
					this.projectInfo.setProperty("fileindex", String.valueOf(fileindex + 1));
					cfg_read_write.cfg_write(this.projectInfo, this.projectFile);

				} else if (fi.isKeyWordsList) {
					getMessageBox("添加文件", "关键词列表不允许含有子文件");
				} else if (fi.isFile) {
					fi.isFiles = true;
					fi.isFile = false;
					fi.initFloder();
					parent.setIcon(0, this.ico_files);
					fileInfo in = new fileInfo(showname, newfile.getName());
					in.isFile = true;
					fi.subfiles.add(in);
					QTreeWidgetItem treeItem = getTreeItem(parent);
					treeItem.setData(1, 0, in);
					treeItem.setText(0, showName);
					treeItem.setIcon(0, this.ico_file);
					parent.setExpanded(true);

					this.mustSaveFileListFile = true;
					setEditable(treeItem, true);
					io.writeBlackFile(newfile, "", null);
					this.projectInfo.setProperty("fileindex", String.valueOf(fileindex + 1));
					cfg_read_write.cfg_write(this.projectInfo, this.projectFile);
				}
			} else {
				fileInfo in = new fileInfo(showname, newfile.getName());
				in.isFile = true;
				this.filesList.add(in);
				QTreeWidgetItem item = new QTreeWidgetItem(this.tree);
				item.setData(1, 0, in);
				item.setIcon(0, this.ico_file);
				item.setText(0, showName);

				this.mustSaveFileListFile = true;
				io.writeBlackFile(newfile, "", null);
				this.projectInfo.setProperty("fileindex", String.valueOf(fileindex + 1));
				cfg_read_write.cfg_write(this.projectInfo, this.projectFile);
//				openFileByTreeItem(item);
				setEditable(item, true);

				p("fuck");
			}
		} else {
			getMessageBox("新建文件", "新建文件出错，文件已存在：" + newfile.getName() + "\n请检查项目文件中的fileindex属性，或使用（重设index）命令让"
					+ appInfo.appName + "自行修复错误");
		}
	}

	void setEditable(QTreeWidgetItem qt, boolean andEditing) {
		Qt.ItemFlags flags = qt.flags();
		flags.set(new Qt.ItemFlag[] { Qt.ItemFlag.ItemIsEditable });
		qt.setFlags(flags);
		if (andEditing) {
			this.isDoubleClickedOnTreeItem = andEditing;
			this.tree.editItem(qt, 0);
		}
	}

	void addFolder() {
		addFolderToProject("未命名文件夹");
	}

	void addFolderToProject(String showname) {
		int fileindex = Integer.valueOf(this.projectInfo.getProperty("fileindex", "0")).intValue();

		QTreeWidgetItem parent = this.tree.currentItem();
		if (parent != null) {
			fileInfo fi = (fileInfo) parent.data(1, 0);
			if ((fi.isDir) || (fi.isRoot != -1) || (fi.isFiles)) {
				fileInfo in = new fileInfo(showname, String.valueOf(fileindex));
				in.isDir = true;
				in.initFloder();
				fi.subfiles.add(in);
				QTreeWidgetItem item = new QTreeWidgetItem(parent);

				item.setData(1, 0, in);
				item.setIcon(0, this.ico_folder);
				item.setText(0, showname);
				parent.setExpanded(true);

				this.mustSaveFileListFile = true;
				setEditable(item, true);
			} else if (!fi.isKeyWordsList) {
				if (fi.isFile) {
					fileInfo in = new fileInfo(showname, String.valueOf(fileindex));
					in.isDir = true;
					in.initFloder();
					fi.initFloder();
					fi.isFiles = true;
					fi.isFile = false;
					parent.setIcon(0, this.ico_files);
					fi.subfiles.add(in);
					QTreeWidgetItem item = new QTreeWidgetItem(parent);

					item.setData(1, 0, in);
					item.setIcon(0, this.ico_folder);
					item.setText(0, showname);

					this.mustSaveFileListFile = true;
					setEditable(item, true);
				}
			}
		} else {
			fileInfo in = new fileInfo(showname, String.valueOf(fileindex));
			in.isDir = true;
			in.initFloder();
			this.filesList.add(in);
			QTreeWidgetItem item = new QTreeWidgetItem(this.tree);

			item.setData(1, 0, in);
			item.setIcon(0, this.ico_folder);
			item.setText(0, showname);
			this.mustSaveFileListFile = true;
			setEditable(item, true);
		}
		this.projectInfo.setProperty("fileindex", String.valueOf(fileindex + 1));
		cfg_read_write.cfg_write(this.projectInfo, this.projectFile);
	}

	void saveProjectFile() {
		cfg_read_write.cfg_write(this.projectInfo, this.projectFile);
	}

	void saveFileListFile() {
		io.writeObjFile(this.filesListFile, this.filesList);
	}

	boolean isHas(QTreeWidgetItem src, QTreeWidgetItem to) {
		QTreeWidgetItem parent = src.parent();
		if ((parent == null) && (to == null)) {
			return true;
		}
		if (parent != null) {
			return parent.equals(to);
		}
		if (to != null) {
			return false;
		}
		return false;
	}

	void moveFileToRecyle() {
		List<QTreeWidgetItem> selectedItems = null;
		if (isCheackable(this.draft)) {
			selectedItems = getCheckedItems();
		} else {
			selectedItems = this.tree.selectedItems();
		}
		for (QTreeWidgetItem qt : selectedItems) {
			if ((!qt.equals(this.draft)) && (!qt.equals(this.reserach)) && (!qt.equals(this.recycle))
					&& (!isHas(qt, this.recycle))) {
				fileInfo fi = (fileInfo) qt.data(1, 0);
				QTreeWidgetItem parent = qt.parent();
				if (parent != null) {
					fileInfo parent_info = (fileInfo) parent.data(1, 0);
					if (parent_info != null) {
						parent_info.subfiles.remove(fi);
						fileInfo recycle_info = (fileInfo) this.recycle.data(1, 0);

						qt.parent().removeChild(qt);
						qt.setData(1, 0, fi);
						this.recycle.addChild(qt);
						recycle_info.subfiles.add(fi);
						if ((parent_info.isFiles) && (parent_info.subfiles.size() == 0)) {
							if (parent_info.charCount > 0) {
								parent.setIcon(0, this.ico_fileOfHasText);
							} else {
								parent.setIcon(0, this.ico_file);
							}
							parent_info.isFiles = false;
							parent_info.subfiles = null;
							parent_info.isFile = true;
						}
					}
				} else {
					this.filesList.remove(fi);
					fileInfo recycle_info = (fileInfo) this.recycle.data(1, 0);
					this.tree.takeTopLevelItem(this.tree.indexOfTopLevelItem(qt));
					qt.setData(1, 0, fi);
					this.recycle.addChild(qt);
					recycle_info.subfiles.add(fi);
				}
			}
		}
		io.writeObjFile(this.filesListFile, this.filesList);
	}

	void resetDefaultFolder() {
		fileInfo info = new fileInfo("草稿", "0");
		info.isRoot = 0;
		info.initFloder();
		this.filesList.add(info);
		fileInfo info1 = new fileInfo("研究", "0");
		info1.isRoot = 1;
		info1.initFloder();
		this.filesList.add(info1);
		fileInfo info2 = new fileInfo("废纸篓", "0");
		info2.isRoot = 2;
		info2.initFloder();
		this.filesList.add(info2);
	}

	void resetProject() {
		this.filesList.removeAll(this.filesList);
		resetDefaultFolder();
		String filesdir = this.projectFile.getParent() + File.separator + "Files";
		File f = new File(filesdir);
		File[] listFiles = f.listFiles();
		File[] arrayOfFile1;
		int j = (arrayOfFile1 = listFiles).length;
		for (int i = 0; i < j; i++) {
			File file = arrayOfFile1[i];
			if (!file.delete()) {
				file.deleteOnExit();
			}
		}
		this.projectInfo.setProperty("fileindex", String.valueOf(0));
		cfg_read_write.cfg_write(this.projectInfo, this.projectFile);
		io.writeObjFile(this.filesListFile, this.filesList);
		ListProjectFiles();
	}

	void clearRecyleFiles() {
		boolean isRemove = false;
		fileInfo fi = (fileInfo) this.recycle.data(1, 0);
		ArrayList<String> allFiles = getAllFiles(fi);
		ArrayList<dataForOpenedFile> deleteFiles = new ArrayList();
		StringBuilder sb = new StringBuilder();
		File f;
		for (String file : allFiles) {
			f = new File(file);
			for (dataForOpenedFile opened : this.openedFiles) {
				if (opened.filename.equals(f.getName())) {
					deleteFiles.add(opened);
				}
			}
			if (f.equals(this.currentEditing)) {
				p("关闭文件：" + f.getName());
				closeCurrentEditing();
			}
			if (f.exists()) {
				boolean isDelete = f.delete();
				String isD = null;
				if (isDelete) {
					isD = "成功.";
				} else {
					isD = "失败.";
				}
				p("从磁盘中删除 " + f.getName() + " " + isD);
				sb.append("从磁盘中删除 " + f.getName() + " " + isD + "\n");
				isRemove = true;
			}
		}
		for (dataForOpenedFile opened : deleteFiles) {
			p("从缓存中删除 " + opened.filename);
			this.openedFiles.remove(opened);
		}
		List<QTreeWidgetItem> takeChildren = this.recycle.takeChildren();
		for (QTreeWidgetItem qt : takeChildren) {
			fileInfo in = (fileInfo) qt.data(1, 0);
			fi.subfiles.remove(in);
			this.recycle.removeChild(qt);
			isRemove = true;
		}
		if (isRemove) {
			io.writeObjFile(this.filesListFile, this.filesList);
		}
		bmessageBox bMessageBox = getBMessageBox("文件删除结果", sb.toString());
		bMessageBox.show();
	}

	void clearRecyle() {
		if (this.recycle.childCount() == 0) {
			return;
		}
		QMessageBox message = new QMessageBox(this);
		QPushButton cancel = message.addButton(QMessageBox.StandardButton.Cancel);
		cancel.setText("取消");
		QPushButton ok = message.addButton(QMessageBox.StandardButton.Ok);
		ok.setText("确定");
		ok.clicked.connect(this, "clearRecyleFiles()");
		message.setText("确定要删除废纸篓内的全部文件？此操作将无法撤销！");
		message.setWindowTitle("清空废纸篓");
		message.setIconPixmap(this.ico_warning.pixmap(48, 48));
		message.show();
	}

	/**
	 * 获取目录和文件集中的全部文件 返回储存了文件全路径的数组
	 * 
	 * @param dirInfo
	 * @return
	 */
	ArrayList<String> getAllFiles(fileInfo dirInfo) {
		ArrayList<String> files = new ArrayList();
		ArrayList<fileInfo> subfiles = dirInfo.subfiles;
		for (fileInfo in : subfiles) {
			if (!in.isDir) {
				String filepath = this.projectFile.getParent() + File.separator + "Files" + File.separator
						+ in.fileName;
				files.add(filepath);
			} else {
				ArrayList<String> allFiles = getAllFiles(in);
				files.addAll(allFiles);
			}
		}
		return files;
	}

	boolean rootIs(QTreeWidgetItem item, QTreeWidgetItem rootItem) {
		QTreeWidgetItem parent = item.parent();
		if (parent != null) {
			while (parent.parent() != null) {
				parent = parent.parent();
			}
			if (parent.equals(rootItem)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前文档所在的文件集(父文件集)，如果没有则返回空
	 * 
	 * @param item
	 * @param rootItem
	 * @return
	 */
	fileInfo getFilesWhichCurrentIn(QTreeWidgetItem item) {
		QTreeWidgetItem parent = item.parent();
		while (parent != null) {
			fileInfo fi = getFileInfoByQTreeItem(parent);
			if (fi.isFiles) {
				return fi;
			}
			parent = parent.parent();

		}

		return null;
	}

	void setExpanded(QTreeWidgetItem qt, boolean expand) {
		qt.setExpanded(expand);
		QTreeWidgetItem parent = qt.parent();
		if (parent != null) {
			parent.setExpanded(expand);
			while (parent.parent() != null) {
				parent = parent.parent();
				parent.setExpanded(expand);
			}
		}
	}

	public String getShowNameOfCurrentEditingFile() {
		return this.infoOfCurrentEditing.showName;
	}

	public boolean isDoNotShowShadow() {
		return this.doNotShowShadow;
	}

	public void setDoNotShowShadow(boolean doNotShowShadow) {
		this.doNotShowShadow = doNotShowShadow;
	}
}
