package yang.app.qt.black;

import static yang.app.qt.black.black.text;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.qt.core.QByteArray;
import io.qt.core.QEvent;
import io.qt.core.QEvent.Type;
import io.qt.core.QObject;
import io.qt.core.QPoint;
import io.qt.core.QPropertyAnimation;
import io.qt.core.QRect;
import io.qt.core.QTimer;
import io.qt.core.QUrl;
import io.qt.core.Qt;
import io.qt.core.Qt.KeyboardModifiers;
import io.qt.core.Qt.PenStyle;
import io.qt.widgets.QAction;
import io.qt.widgets.QApplication;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QFont.SpacingType;
import io.qt.widgets.QFrame;
import io.qt.gui.QKeyEvent;
import io.qt.gui.QKeySequence;
import io.qt.widgets.QLabel;
import io.qt.widgets.QMenu;
import io.qt.gui.QPainter;
import io.qt.gui.QPalette;
import io.qt.gui.QPen;
import io.qt.widgets.QStyleFactory;
import io.qt.gui.QTextBlock;
import io.qt.gui.QTextBlockFormat;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveMode;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.gui.QTextCursor.SelectionType;
import io.qt.gui.QTextDocument;
import io.qt.widgets.QTextEdit;
import io.qt.gui.QTextListFormat;
import io.qt.widgets.QTreeWidgetItem;

import yang.demo.allPurpose.debug;
import yang.demo.allPurpose.fileTool;
import yang.demo.allPurpose.httpGet;
import yang.demo.allPurpose.time;

public class blacktext extends QObject {
	int zoomvalue;
	bTextEdit text;
	Qt.KeyboardModifiers km = new Qt.KeyboardModifiers(
			new Qt.KeyboardModifier[] { Qt.KeyboardModifier.ControlModifier });
	black b;
	int pos = 0;
	int lastpos = -1;
	int charCountAtToday = -1;
	QTextCursor currentFindCursor;
	boolean doNotScroll;
	boolean doNotSetStyle;
	QLabel timeMessage;
	int lastposForFuck;
	private int blockNumber;
	private int lastBlockNumber;
	static int typeModeValue = 2;
	float posAtLocationF;
	long lastTextChangeTime;
	long editTime;
	ArrayList<QTextEdit.ExtraSelection> extraSelection;
	QTreeWidgetItem keywordListFile;
	fileInfo fileInfo;
	int vScrollBarMaxValue;
	long lastposTime;
	long posTime;
	/**
	 * 滚动条真实的长度
	 */
	int trueMax;
	private long lastScrollTime;

	public blacktext(bTextEdit text, black black) {
		this.text = text;
		fileInfo = black.infoOfCurrentEditing;
//		text.verticalScrollBar().setStyle(new QCleanlooksStyle());
//		text.horizontalScrollBar().setStyle(new QCleanlooksStyle());
		currentFindCursor = text.textCursor();
		this.b = black;
		
		typeModeValue = b.getIntValueFromSettings(appInfo.typeModeValue, "2");
		text.setFrameStyle(QFrame.Shape.NoFrame.value());

		setShortcut();
		text.textChanged.connect(this, "textchanged()");
		zoom(b.getEditorTextZoomValue());
		
//		//设定默认字体字号和缩放
//		QFont font_ = new QFont(b.getEditorFontName(), b.getEditorFontSize());
//		if(!b.infoOfCurrentEditing.isKeyWordsList)
//			if(b.getBooleanValueFromSettings(appInfo.charSpace, "false"))
//				font_.setLetterSpacing(SpacingType.PercentageSpacing, appInfo.charSpaceValue);
//		text.setFont(font_);
//		int zoom = b.getEditorTextZoomValue();

		
		text.verticalScrollBar().rangeChanged.connect(this, "r(int,int)");
		
		text.installEventFilter(this);
		text.textChanged.connect(black, "documentTextChanged()");
		text.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
		text.customContextMenuRequested.connect(this, "addMenu(QPoint)");

		text.setEnabled(false);
		text.cursorPositionChanged.connect(this, "cursorPosChanged()");

//		if (this.b.getBooleanValueFromSettings(appInfo.yellowChar, "false")) {
//			qPalette.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Highlight, new QColor(Qt.GlobalColor.black));
//			qPalette.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.HighlightedText,
//					new QColor(Qt.GlobalColor.white));
//		} else {
//			qPalette.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Highlight, new QColor(Qt.GlobalColor.black));
//			qPalette.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.HighlightedText,
//					new QColor(Qt.GlobalColor.white));
//		}
//		text.setCursorWidth(this.b.getEditorCaretWidthValue());
		text.selectionChanged.connect(b, "selectionChanged()");
		text.verticalScrollBar().valueChanged.connect(this, "v(int)");
	}

	void v(int v) {
		text.updateCaret(2);
		b.update();
	}
	/**
	 * 增加滚动条的最大值，以便于能够使用打字机卷动功能
	 * @param v
	 * @param v2
	 */
	void r(int v, int v2) {
		if(!text.isVisible())return;
		if(b.isLoadFile)return;
		text.verticalScrollBar().rangeChanged.disconnect(this);

		trueMax = v2;
		if (typeModeValue == 1)
			return;
		int h = text.viewport().rect().height() - this.text.viewport().rect().height() / typeModeValue;
		if (this.text.verticalScrollBar().maximum() == 0) {
			if (this.text.cursorRect().y() >= h) {
				this.text.verticalScrollBar().setMaximum(h);
			}
		} else {
			this.text.verticalScrollBar().setRange(v, v2 + h);
		}
		text.verticalScrollBar().rangeChanged.connect(this, "r(int,int)");
	}

	void m(int m) {
		System.out.println("move: " + m);
	}	

	void moveCaret() {
		QTimer t = new QTimer(this.b);
		t.startTimer(1);
	}
	
	
	void cursorPosChanged() {
//		scroll();
		text.ensureCursorVisible();
//		QTextCursor tcc = text.textCursor();
//		System.out.println(tcc.block().layout().boundingRect());
		b.debugPrint("pos: "+text.textCursor().position()+" lastPos: "+lastpos);
		b.ba.typerMode(b);
		if(!b.noPageLine) b.update();
		
		if (this.b.isLoadFile) {
			return;
		}
		
		b.activeTime = System.currentTimeMillis();
		b.monitorOff = false;
		
		lastposTime = posTime;
		posTime = System.currentTimeMillis();
		long t = posTime-lastposTime;
		if(text.preeditStringPos == -1 && t < 50) {
//			b.debugPrint("光标移动时间间隔"+t+"ms");
			text.caretMoveWithNoAnmiation = true;
		}else text.caretMoveWithNoAnmiation = false;
		
		
		this.lastpos = this.pos;
		QTextCursor tc = this.text.textCursor();
		
		
//		if(doNotSetStyle)
//		if(!cheakDocument.cheakStringOnly(tc.block().text(),appInfo.syls)) {
//			doNotSetStyle = false;
//		}
		this.pos = tc.position();
		this.lastBlockNumber = this.blockNumber;
		this.blockNumber = tc.blockNumber();
		if(b.isAdmin()) {
			float posF = pos;
			float charCountF = text.charCountOfDoc();
			posAtLocationF = (posF / charCountF * 100F);
			int posAtLocation = (int) posAtLocationF;
			b.caretPos.setText(pos + "(" + posAtLocation + "%)");
			b.caretPos.show();
		}else b.caretPos.hide();
		
		
		
		//添加title到arraylist，目前禁用，没有校正一些数据，
//		if(titleMap != null)
//		if(lastBlockNumber != blockNumber) {
//			QTextCursor tcc = text.textCursor();
//			tcc.setPosition(lastpos);
//			tcc.movePosition(MoveOperation.StartOfBlock);
//			if(cheakDocument.cheakStringOnly(tcc.block().text(),appInfo.syls)) {
//				Integer integer = titleMap.get(tcc.position());
//				if(integer == null) {
//					int countOfDoc = text.charCountOfDoc();	
//					float f_curr = (float)tcc.position()/(float)countOfDoc;
//					int len_curr = (int) ((this.b.height()-50)*f_curr);
//					titleMap.put(tcc.position(), len_curr);
//					b.repaint(b.width()-50, 0, 50, b.height());
//				}
//			}else {
//				Integer integer = titleMap.get(tcc.position());
//				if(integer != null) {
//					titleMap.remove(tcc.position());
//					b.repaint(b.width()-50, 0, 50, b.height());
//				}
//			}
//		}
		b.setMarkAndStyle();
//		ArrayList<timerInfo> timer = b.getAllTimer(-23445934);
//		timerInfo ti = null;
//		if(timer.size() > 0) {
//			ti = timer.get(0);
//			ti.timerName = b.caretPos.text();
//			ti.setTimeOut();
//		}
//		else{
//			ti = new timerInfo(-23445934, 5000, b.caretPos.text(), null, false, false);
//			ti.hideLeftTime = true;
//			ti.isNew = true;
//			b.addTimer(ti);	
//		}
		
		
		if(text.preeditStringPos == -1)
		if ((this.b.keywords != null) && (this.b.keywords.isVisible()) && (!this.b.doNotHideKeywordsDialog)) {
			this.b.keywords.hide();
		}
	}

	public void addMenu(QPoint point) {
		if(appInfo.miniMode.equals("1"))return;
		QMenu qm = new QMenu(this.text);
		qm.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
		
		QAction undo = b.ba.addMenuTo(qm, "撤销\tCtrl+Z", "", text, "undo()");
		b.ba.addMenuTo(qm, "重做\tCtrl+Y", "", text, "redo()");
		
		qm.addSeparator();

		QAction copy = new QAction(qm);
		copy.setText("复制");
		copy.triggered.connect(b, "copyText()");
		copy.setShortcut("ctrl+c");
		QAction cut = new QAction(qm);
		cut.setText("剪切");
		cut.setShortcut("ctrl+x");
		cut.triggered.connect(b, "cutText()");

		QAction pasteWithoutFormat = new QAction(qm);
		pasteWithoutFormat.setText("粘贴为纯文本");
		pasteWithoutFormat.triggered.connect(this.b, "pasteWithCurrentFormat()");
		pasteWithoutFormat.setShortcut("ctrl+v");
		QAction selectAll = new QAction(qm);
		selectAll.setText("选择当前段落/全选");
		selectAll.setShortcut("ctrl+a");
		selectAll.triggered.connect(b.ba, "selectAll()");

		qm.addAction(copy);
		qm.addAction(cut);

		qm.addAction(pasteWithoutFormat);
		qm.addSeparator();
		qm.addAction(selectAll);
		qm.addSeparator();

		QAction setTextAsTitle = new QAction(qm);
		setTextAsTitle.setText("将所选的文本设为文档标题");
		setTextAsTitle.triggered.connect(this.b, "setFileTitleAsSelectedText()");
		qm.addAction(setTextAsTitle);
		
		QAction findInCurrentDoc = new QAction(qm);
		findInCurrentDoc.setText("在当前文档中检索选中的文本\t选中文本按下Tab键");
		findInCurrentDoc.triggered.connect(b.ba, "findInCurrentDoc()");
		qm.addAction(findInCurrentDoc);
		
		QAction findInAll = new QAction(qm);
		findInAll.setText("在所有项目文件中检索选中的文本\t选中文本按下Ctrl+Tab键");
		findInAll.triggered.connect(b.ba, "findInAllFiles()");
		qm.addAction(findInAll);
		if (this.text.textCursor().selectedText().length() == 0) {
			setTextAsTitle.setEnabled(false);
			findInCurrentDoc.setEnabled(false);
			findInAll.setEnabled(false);
		}
		QAction findInGoldenDict = new QAction(qm);
		findInGoldenDict.setText("在GoldenDict中检索(未选择文本时则切分光标所在行进行检索)");
		findInGoldenDict.setShortcut("ctrl+g");
		findInGoldenDict.triggered.connect(this.b, "findInGoldenDict()");
		qm.addAction(findInGoldenDict);

		QAction isSetTitleStyle = new QAction(qm);
		isSetTitleStyle.setCheckable(true);
		if (this.b.infoOfCurrentEditing.isKeyWordsList) {
			isSetTitleStyle.setEnabled(false);
		} else if (this.b.infoOfCurrentEditing.setTitleStyle) {
			isSetTitleStyle.setChecked(true);
		} else {
			isSetTitleStyle.setChecked(false);
		}
		QAction h = new QAction(qm);
		h.setCheckable(true);
		if (this.b.infoOfCurrentEditing.isKeyWordsList) {
			h.setEnabled(false);
		} else if (this.b.infoOfCurrentEditing.showMark) {
			h.setChecked(true);
		} else {
			h.setChecked(false);
		}
		h.setText("自动高亮此文档中的对称类标点符号(括号、中文引号等)");
		h.triggered.connect(this.b, "setMarkBackground()");
		qm.addAction(h);

		qm.addSeparator();
		QAction resetDocFormat = new QAction(qm);
		resetDocFormat.setText("删除文档内所有的空行和段前空格");
		resetDocFormat.triggered.connect(this.b, "resetDocumentFormat()");
		qm.addAction(resetDocFormat);

		QAction resetDocTextLayout = new QAction(qm);
		resetDocTextLayout.setText("尝试删除文档内所有不合规则的换行符(例如没有标点的句子后面的换行符)");
		resetDocTextLayout.triggered.connect(this.b, "resetTextStringLayout()");
		qm.addAction(resetDocTextLayout);
		qm.addSeparator();

		QAction showInExplorer = new QAction(qm);
		showInExplorer.setText("在系统资源管理器中显示当前所编辑的文件");
		showInExplorer.triggered.connect(b, "showInExplorer()");
		qm.addAction(showInExplorer);

		
		if (this.b.infoOfCurrentEditing.walkList != null) {
			QAction clearWalkList = new QAction(qm);
			clearWalkList.setText("清空此文档的编辑点记录\t" + (this.b.infoOfCurrentEditing.walkList.size() - 1) + "条记录");
			clearWalkList.triggered.connect(this.b, "clearWalkList()");
			qm.addAction(clearWalkList);
		}
		
		qm.addAction(this.b.ui.commit);
		qm.addAction(this.b.ui.commitAndPush);

		qm.addSeparator();

		QAction openfile = new QAction(qm);
		openfile.setText("打开其他文件");
		Menu openfileMenu = this.b.listMenuWithTreeWidget(null, "openFileByMenu(QAction)", true);
		openfileMenu.setStyleSheet(this.b.styleSheet());
		openfile.setMenu(openfileMenu);
		qm.addAction(openfile);
		qm.addSeparator();
		
		
		if (black.writingView > 0) {
			QAction act = bAction.addMenuTo(qm, "切换颜色模式\tCtrl/Alt+2", "", b, "setLightOrDark()");
			qm.insertAction(undo, act);
			if(b.ba.checkBingPicIsEnable()) {
				QAction showBingPicInfo = bAction.addMenuTo(qm, "显示Bing每日图片故事\t双击窗口背景", "", b.ba, "showBingPicInfo()");
				qm.insertAction(undo, showBingPicInfo);
				QAction disableBingPic = bAction.addMenuTo(qm, "禁用Bing每日图片", "", b.ba, "setUseBingPic()");
				qm.insertAction(undo, disableBingPic);
			}else if(b.ba.checkIsUsePic()) {
				QAction nextPic = bAction.addMenuTo(qm, "下一张背景图片\tAlt+PageDown", "", b.ba, "changeBackgroundImage()");
				qm.insertAction(undo, nextPic);
				QAction useBingPic = bAction.addMenuTo(qm, "使用Bing每日图片", "", b.ba, "setUseBingPic()");
				qm.insertAction(undo, useBingPic);
				QAction disablePic = bAction.addMenuTo(qm, "禁用背景图片", "", b.ba, "setUsePic()");
				qm.insertAction(undo, disablePic);
			}else {
				QAction usePic = bAction.addMenuTo(qm, "使用背景图片", "", b.ba, "setUsePic()");
				qm.insertAction(undo, usePic);
			}
			
			qm.insertSeparator(undo);
			
			QAction a = new QAction("退出写作视图", this.text);
			a.triggered.connect(this.b, "exitWritingView()");
			a.setShortcut("ESC");
			qm.addAction(a);

			QAction exit = new QAction("退出程序", this.text);
			exit.setShortcut("shift+esc");
			exit.triggered.connect(this.b, "exit()");
			qm.addAction(exit);

			qm.addSeparator();
			QAction charcount = new QAction(qm);
			String selectedText = this.text.textCursor().selectedText();
			if (selectedText.length() > 0) {
				charcount.setText("字符数\t" + selectedText.length() + "/" + this.text.document_b().characterCount());
			} else {
				charcount.setText("字符数\t" + String.valueOf(this.text.document_b().characterCount()));
			}
			charcount.triggered.connect(this.b, "showCharCountBox()");
			qm.addAction(charcount);
		}
		qm.popup(this.text.mapToGlobal(point));
		if (this.b.keywords != null) {
			this.b.keywords.scrollBarOfTreeHideing = false;
		}
	}

	void actionForSetTitleStyle(Boolean bool) {
		this.b.infoOfCurrentEditing.setTitleStyle = (!this.b.infoOfCurrentEditing.setTitleStyle);
		new bRunnable(0, true, true, true, true) {
			public void run() {
				blacktext.this.b.btext.resetEditor();
			}
		};
	}

	void checkNetworks() {
		this.b.checkNetWorks();
	}

	public boolean zoom(final int value) {
		if ((value >= 0) && (value != this.zoomvalue)) {
			int v = value * 10 + 100;
			if (v > 400) {
				return false;
			}
			this.b.setLoadFileMessage("缩放至" + v + "%");
			new bRunnable(1, true, false, true, true) {
				public void run() {
					if (value < blacktext.this.zoomvalue) {
//						int newzoom = blacktext.this.zoomvalue - value;
//						blacktext.this.text.zoomOut(newzoom);
//						blacktext.this.scroll();
						blacktext.this.zoomvalue = value;
					} else {
//						blacktext.this.text.zoomIn(value - blacktext.this.zoomvalue);
//						blacktext.this.scroll();
						blacktext.this.zoomvalue = value;
					}
				}
			};
		} else {
			return false;
		}
		return true;
	}

	boolean keywordsIsShowing() {
		if ((this.b.keywords != null) && (this.b.keywords.isVisible())) {
			return true;
		}
		return false;
	}

	public boolean eventFilter(QObject o, QEvent e) {
		if ((o.equals(this.text)) && (e.type() == QEvent.Type.KeyPress)) {
			if(text.isReadOnly())return true;
			QKeyEvent keypress = (QKeyEvent) e;
//			System.out.println(keypress.key());
			if (keypress.key() == Qt.Key.Key_Tab.value()) {
				QTextCursor tc = this.text.textCursor();
				if (tc.selectedText().length() == 0) {
					if (keypress.modifiers().value() == Qt.KeyboardModifier.NoModifier.value()) {
						if (!this.b.infoOfCurrentEditing.isKeyWordsList) {
							scroll();
							black.findview = 0;
							this.b.showKeyWords();
							return true;
						}
					} else if (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value()) {
						this.b.changedForOpenedFiles();
						return true;
					}
				} else if (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value()) {
					if (this.b.TreeWidgetItemOfCurrentEditing.parent() != null) {
						scroll();
						black.findview = 1;
						this.b.showFindInfo(black.text, this.b.TreeWidgetItemOfCurrentEditing.parent(), false);
						return true;
					}
				} else {
					scroll();
					black.findview = 1;
					this.b.showFindInfo(black.text, this.b.TreeWidgetItemOfCurrentEditing.parent(), true);
					return true;
				}
				return true;
			} else if (keypress.key() == Qt.Key.Key_Return.value()) {
				if (keypress.modifiers().value() == Qt.KeyboardModifier.NoModifier.value()) {
					QTextCursor tc = text.textCursor();
					boolean movePosition = tc.movePosition(MoveOperation.PreviousBlock);
					if(movePosition) {
						tc.movePosition(MoveOperation.StartOfBlock);
						tc.movePosition(MoveOperation.EndOfBlock,MoveMode.KeepAnchor);
						if(tc.selectedText().equals("：：：")) {
							QTextCursor ttc = text.textCursor();
							ttc.movePosition(MoveOperation.StartOfBlock);
							ttc.movePosition(MoveOperation.EndOfBlock,MoveMode.KeepAnchor);
							String rebotTalk = bAction.rebotTalk(ttc.selectedText());
							if(rebotTalk == null) return b.ba.addOrRemoveKeywords();

							ttc.insertText(rebotTalk);
							ttc.movePosition(MoveOperation.StartOfBlock);
							ttc.movePosition(MoveOperation.EndOfBlock,MoveMode.KeepAnchor);
							text.setTextCursor(ttc);
							return true;
						}else movePosition = false;
					}
					if(!movePosition)
					return b.ba.addOrRemoveKeywords();
				} else if (keypress.modifiers().value() == Qt.KeyboardModifier.AltModifier.value()) {
					black.findview = 2;
					this.b.showTitleList();
				} else if (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value()) {
					this.b.showMoveList();
				} else if (keypress.modifiers()
						.isSet(new Qt.KeyboardModifier[] { Qt.KeyboardModifier.ControlModifier })) {
					if (keypress.modifiers().isSet(new Qt.KeyboardModifier[] { Qt.KeyboardModifier.ShiftModifier })) {
						this.b.checkIsHasDiffKeyWordsList();
						return true;
					}
				}else if(keypress.modifiers().isSet(Qt.KeyboardModifier.ShiftModifier)) {
					return true;
				}
			}else if(keypress.key() == Qt.Key.Key_Backspace.value()) {
				if(keypress.modifiers().isSet(Qt.KeyboardModifier.ShiftModifier)){
					text.undo();
					return true;
				}
			}
			else if (keypress.key() == Qt.Key.Key_Home.value()) {
				QTextCursor tc = this.text.textCursor();
				QRect r = this.text.cursorRect();
				QRect r_ = this.text.viewport().rect();
				if ((r.y() + r.height() < 0) || (r.y() > r_.height())) {
					scroll();
				} else if (tc.atBlockStart()) {
//					scroll();
					text.moveCursor(QTextCursor.MoveOperation.Start);
				} else {
					this.text.moveCursor(QTextCursor.MoveOperation.StartOfBlock);
					scroll();
				}
				return true;
			} else if (keypress.key() == Qt.Key.Key_End.value()) {
				QTextCursor tc = this.text.textCursor();
				if (tc.atBlockEnd()) {
					this.text.moveCursor(QTextCursor.MoveOperation.End);
//					scroll();
				} else if (tc.atEnd()) {
					text.verticalScrollBar().setValue(text.verticalScrollBar().maximum());
					return true;
				} else {
					this.text.moveCursor(QTextCursor.MoveOperation.EndOfBlock);
					scroll();
				}
				return true;
			} else if ((keypress.key() == Qt.Key.Key_Left.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.AltModifier.value())) {
				this.b.moveToPreviousWalk();
			} else if ((keypress.key() == Qt.Key.Key_Right.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.AltModifier.value())) {
				this.b.moveToNextWalk();
			} else if ((keypress.key() == Qt.Key.Key_Left.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
				this.b.moveToPreviousTitle(true);
				return true;
			} else if ((keypress.key() == Qt.Key.Key_Right.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
				this.b.moveToNextTitle(true);
				return true;
			} else if ((keypress.key() == Qt.Key.Key_Up.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.AltModifier.value())) {
				this.b.moveToPreviousOpenedFile();
			} else if ((keypress.key() == Qt.Key.Key_Down.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.AltModifier.value())) {
				this.b.moveToNextOpenedFile();
			} else if ((keypress.key() == Qt.Key.Key_C.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
				b.copyText();
				return true;
			} else if ((keypress.key() == Qt.Key.Key_X.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
				b.cutText();
				return true;
			} else if ((keypress.key() == Qt.Key.Key_V.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
				this.b.pasteWithCurrentFormat();
				return true;
			} else if ((keypress.key() == Qt.Key.Key_A.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
				b.ba.selectAll();
				return true;
			} else if (keypress.key() == Qt.Key.Key_Escape.value()) {
				if (keypress.modifiers().value() == Qt.KeyboardModifier.ShiftModifier.value()) {
					this.b.exit();
				} else if ((keypress.modifiers().value() == Qt.KeyboardModifier.NoModifier.value())
						&& (black.writingView > 0)) {
					this.b.exitWritingView();
				}
			} else if ((keypress.key() == Qt.Key.Key_Z.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
//				this.doNotSetStyle = true;
//				this.doNotScroll = true;
//				b.reundo(false);
//				this.doNotSetStyle = false;
//				this.doNotScroll = false;
//				text.undo.undo();
//				return true;
			} else if ((keypress.key() == Qt.Key.Key_Y.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value())) {
//				this.doNotSetStyle = true;
//				this.doNotScroll = true;
//				b.reundo(true);

//				this.doNotSetStyle = false;
//				this.doNotScroll = false;
//				text.undo.redo();
//				return true;
			} else if ((keypress.key() == Qt.Key.Key_Up.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.NoModifier.value())) {
				Object 导航 = b.ba.tempData.get("导航");
				if(导航 == null && b.infoOfCurrentEditing.autoscrollvalue == 0)
				return b.ba.fastFindUp();
				else {
					scrollWithAnimation(text.verticalScrollBar().value()-(text.cursorRect().height()+b.getEditorLineSpaceValue()));
					return true;
				}
			} else if ((keypress.key() == Qt.Key.Key_Down.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.NoModifier.value())) {
				Object 导航 = b.ba.tempData.get("导航");
				if(导航 == null && b.infoOfCurrentEditing.autoscrollvalue == 0)
				return b.ba.fastFindDown();
				else {
					scrollWithAnimation(text.verticalScrollBar().value()+(text.cursorRect().height()+b.getEditorLineSpaceValue()));
					return true;
				}
			} else if ((keypress.key() == Qt.Key.Key_PageDown.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.NoModifier.value())) {
				doNotScroll = true;
//				QTextCursor tc = this.text.textCursor();
//				if (tc.atEnd()) {
//					text.verticalScrollBar().setValue(text.verticalScrollBar().maximum());
//				}
				scrollWithAnimation(text.verticalScrollBar().value()+text.viewport().height()-(b.getEditorLineSpaceValue()+b.getEditorParagraphSpaceValue()));
//				text.verticalScrollBar().setProperty("value", text.verticalScrollBar().value()+text.viewport().height());
				doNotScroll = false;
				return true;
			}else if((keypress.key() == Qt.Key.Key_PageUp.value())
					&& (keypress.modifiers().value() == Qt.KeyboardModifier.NoModifier.value())) {
				doNotScroll = true;
				scrollWithAnimation(text.verticalScrollBar().value()-text.viewport().height()+b.getEditorLineSpaceValue()+b.getEditorParagraphSpaceValue());
				doNotScroll = false;
				return true;
			}
			else if (keypress.modifiers().value() == Qt.KeyboardModifier.AltModifier.value()) {
				if ((keypress.key() == Qt.Key.Key_PageDown.value())) {
					b.ba.changeBackgroundImage();
				}
				
				if(text.textCursor().hasSelection()) {
					b.showAllSelectionInCurrentScreen(text, text.textCursor().selectedText());
				}
				if (keypress.key() == Qt.Key.Key_I.value()) {
					moveUp();
				} else if (keypress.key() == Qt.Key.Key_K.value()) {
					moveDown();
				} else if (keypress.key() == Qt.Key.Key_J.value()) {
					moveLeft();
				} else if (keypress.key() == Qt.Key.Key_L.value()) {
					moveRight();
				} else if (keypress.key() == Qt.Key.Key_U.value()) {
					this.b.moveToPreviousWalk();
				} else if (keypress.key() == Qt.Key.Key_O.value()) {
					this.b.moveToNextWalk();
				}
				return true;
			}
			else if (keypress.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value()) {
				if(keypress.key() == Qt.Key.Key_Up.value()) {
					if(b.ba.tempData.get("导航") == null)
						b.ba.tempData.put("导航", 0);
					else b.ba.tempData.remove("导航");
					doNotScroll = true;
					scrollWithAnimation(text.verticalScrollBar().value()-(text.cursorRect().height()+b.getEditorLineSpaceValue()));
					doNotScroll = false;
					return true;
				}else if(keypress.key() == Qt.Key.Key_Down.value()) {
					if(b.ba.tempData.get("导航") == null)
						b.ba.tempData.put("导航", 0);
					else b.ba.tempData.remove("导航");
					doNotScroll = true;
					scrollWithAnimation(text.verticalScrollBar().value()+(text.cursorRect().height()+b.getEditorLineSpaceValue()));
					doNotScroll = false;
					return true;
				}
			}


		} else if ((o.equals(this.text)) && (e.type() == QEvent.Type.FocusIn)) {
			this.b.tree.clearSelection();
			this.b.TreeWidgetItemOfCurrentEditing.setSelected(true);
		}
		return false;
	}

	public void a3() {
		int h = this.text.viewport().rect().height() / 2;
		if (this.text.verticalScrollBar().maximum() == 0) {
			if (this.text.cursorRect().y() >= h) {
				this.text.verticalScrollBar().setMaximum(h);
			}
		}
	}

	public void textchanged() {
		vScrollBarMaxValue = text.verticalScrollBar().maximum();
		scroll();
		this.b.lastInputTime_long = time.getCurrentTime_long();
	}

	public void scroll() {
		if (this.doNotScroll || typeModeValue == 1 || b.isLoadFile || text.preeditStringPos != -1 || text.inputing) {
			return;
		}
//		if(text.textCursor().atStart() || text.textCursor().atEnd())return;
		QTextBlockFormat tbf = this.text.textCursor().blockFormat();
		int i = (int) (tbf.lineHeight() - tbf.topMargin() - tbf.bottomMargin()
				- (this.text.font().weight() - this.zoomvalue) / typeModeValue);
		if (this.text.cursorRect().y() >= this.text.viewport().rect().height() / typeModeValue) {
			int h = this.text.cursorRect().y() - this.text.viewport().rect().height() / typeModeValue;
			int newtop = this.text.verticalScrollBar().value() + h - i;
//			this.text.verticalScrollBar().setValue(newtop);
			scrollWithAnimation(newtop);
		} else {
			int h = this.text.viewport().rect().height() / typeModeValue - this.text.cursorRect().y();
			int newtop = this.text.verticalScrollBar().value() - h - i;
//			this.text.verticalScrollBar().setValue(newtop);
			scrollWithAnimation(newtop);
		}
	}
	void scrollWithAnimation(int endValue) {
		Long last = System.currentTimeMillis()-lastScrollTime;
		int animationTime = b.getAnimationTime();
		if(last < b.getAnimationTime()) animationTime = 0;
		int value = text.verticalScrollBar().value();
		b.setAnimation(text.verticalScrollBar(), "value", value, endValue, animationTime);
		lastScrollTime = System.currentTimeMillis();
	}
	public void addList() {
		QTextListFormat listformat = new QTextListFormat();
		listformat.setStyle(QTextListFormat.Style.ListDecimal);
		this.text.textCursor().insertList(listformat);
	}



//	void applyEditorColor() {
//		QPalette background = this.text.palette();
//
//		QPalette qPalette = this.text.palette();
//		this.text.setPalette(qPalette);
//		if (!this.b.getBooleanValueFromSettings(appInfo.focusMode, "false")) {
//			QColor bc = null;
//			QColor fc = null;
//			int whatColor = b.getIntValueFromSettings(appInfo.editorColor, "0");
//			if (whatColor < 2) {
//				bc = b.withoutBlueColor(new QColor(Qt.GlobalColor.white), b.withoutBuleValue);
//				fc = b.withoutBlueColor(new QColor(Qt.GlobalColor.black), b.withoutBuleValue);
//			} else {
//				bc = new QColor(51, 51, 51);
//				fc = new QColor(238, 238, 238);
//			}
//
//			background.setColor(QPalette.ColorRole.Base, bc);
//			background.setColor(QPalette.ColorRole.Text, fc);
//			this.text.setPalette(background);
////			QPalette pb = this.b.palette();
////			pb.setColor(QPalette.ColorRole.Window, bc);
////			pb.setColor(QPalette.ColorRole.WindowText, fc);
////			this.b.setPalette(pb);
//		}
//		this.text.verticalScrollBar().setStyleSheet("");
//		this.text.horizontalScrollBar().setStyleSheet("");
//	}

	void J() {
		this.b.bInsertText(this.text, "“", -1, false);
	}

	void K() {
		this.b.bInsertText(this.text, "”", -1, false);
	}

	

	public void deleteDir() {
		File dir = new File(this.b.projectFile.getParent() + "_clone_" + time.getCurrentDate("-"));
		if ((dir.exists()) && (fileTool.deleteDir(dir.getPath()))) {
			this.b.getMessageBox("删除git克隆目录", "已删除" + dir.getPath() + "目录");
			this.b.p("已删除" + dir.getPath() + "目录");
		}
	}
	/**
	 * 显示基于插入符前一位字符查找的关键词列表
	 * 使用此方法产生的关键词显示框会在延迟时间之后才会显示，具体的延迟时间参见appInfo.pinyinStartSegmentMS
	 */
	void showKeywords() {
		if (!this.b.infoOfCurrentEditing.isKeyWordsList) {
			black.findview = 5;
//			this.b.showKeyWords();
			b.ba.showKeywordsListByCurrentChar_();
		}
	}

	

	public void startWiki() {
		kiwix kiwix = new kiwix(this.b);
		kiwix.startKiwixServer();
		kiwix.startWeb();
	}

	public void startWeb() {
	}

	String findNext(String style) {
		List<String> keys = QStyleFactory.keys();
		for (int i = 0; i < keys.size(); i++) {
			String string = ((String) keys.get(i)).toLowerCase();
			if (style.equals(string)) {
				if (i + 1 < keys.size()) {
					return (String) keys.get(i + 1);
				}
				return (String) keys.get(0);
			}
		}
		return null;
	}

	void ttt() {
		String objectName = QApplication.style().objectName();
		String lowerCase = objectName.toLowerCase();
		String findNext = findNext(lowerCase);
		if (findNext != null) {
			QApplication.setStyle(findNext);
		}
	}

	void testtts() {
		this.text.hide();
	}

	void moveUp() {
		QKeyEvent key = new QKeyEvent(Type.KeyPress, Qt.Key.Key_Up.value(),
				new KeyboardModifiers(Qt.KeyboardModifier.NoModifier));
		QApplication.postEvent(text, key);

	}

	void moveDown() {
		QKeyEvent key = new QKeyEvent(Type.KeyPress, Qt.Key.Key_Down.value(),
				new KeyboardModifiers(Qt.KeyboardModifier.NoModifier));
		QApplication.postEvent(text, key);
	}

	void moveLeft() {
		QKeyEvent key = new QKeyEvent(Type.KeyPress, Qt.Key.Key_Left.value(),
				new KeyboardModifiers(Qt.KeyboardModifier.NoModifier));
		QApplication.postEvent(text, key);

	}

	void moveRight() {
		QKeyEvent key = new QKeyEvent(Type.KeyPress, Qt.Key.Key_Right.value(),
				new KeyboardModifiers(Qt.KeyboardModifier.NoModifier));
		QApplication.postEvent(text, key);
	}

	void t3() {
		b.getMessageBox("", "");
	}
	
	public void setShortcut() {
//		addShortcut(b.ba, "alt+n", "insertN()");
//		addShortcut(b.ba, "alt+m", "insertM()");
		addShortcut(b, "ctrl+shift+/", "dontNotSetMonitor()");
//		addShortcut(this, "shift+backspace", "t3()");
		addShortcut(b, "alt+1", "backup()");
		addShortcut(b, "ctrl+1", "backup()");
		addShortcut(this.b, "ctrl+shift+f", "showFindDialogWithoutFindHistory()");
		addShortcut(this.b, "alt+shift+f", "showFindDialogWithoutFindHistory()");
		addShortcut(this.b, "alt+2", "setLightOrDark()");
		addShortcut(this.b, "ctrl+2", "setLightOrDark()");
		if(appInfo.mode == 1) {
			addShortcut(b, "alt+3", "changeToAdminMode()");
			addShortcut(b, "ctrl+3", "changeToAdminMode()");
		}
//		else addShortcut(b, "alt+3", "setSpeak()");
		addShortcut(b, "alt+5", "setAutoFindInKeywords()");
		addShortcut(this.b, "f12", "popPicWidget()");
		if(b.isAdmin())
		addShortcut(this.b, "ctrl+f12", "focusMode()");

//		addShortcut(this.b,"alt+4","noBackgroundForWritingView()");
		addShortcut(this.b, "alt+0", "quiet()");
		if(appInfo.miniMode.equals("0")) {
			addShortcut(this.b, "ctrl+q", "changedEditFile()");
			addShortcut(b, "ctrl+i", "clearCharCountOfTodayInput()");
			addShortcut(b.ba, "alt+;", "moveSmart()");
//			addShortcut(this.b, "alt+p", "repair()");
			addShortcut(b, "ctrl+`", "repair()");
			addShortcut(this, "alt+/", "showKeywords()");
			addShortcut(this, "ctrl+m", "resetEditor()");
			addShortcut(this.b, "ctrl+s", "saveDocumentByUser()");
			addShortcut(b, "alt+4", "changeRepairBy()");
			
			
			this.text.addAction(this.b.ui.oneChineseName);
			this.text.addAction(this.b.ui.engManName);
			this.text.addAction(this.b.ui.engWomanName);
			this.text.addAction(this.b.ui.itManName);
			this.text.addAction(this.b.ui.itWomanName);
			this.text.addAction(this.b.ui.settings);
			this.text.addAction(this.b.ui.chineseNames);
			this.text.addAction(this.b.ui.commit);
		}
		this.text.addAction(this.b.ui.about);
		addShortcut(this.b, "ctrl+shift+k", "statCharCountForManyDoc()");
		addShortcut(this.b, "alt+shift+k", "statCharCountForManyDoc()");
		addShortcut(this.b, "alt+h", "backspace()");
		addShortcut(this.b, "alt+f", "enter()");
		addShortcut(this.b, "ctrl+g", "findInGoldenDict()");
		addShortcut(b, "ctrl+shift+h", "showLogs()");
		addShortcut(b, "alt+shift+h", "clearWinRun4jLogs()");
		addShortcut(this, "ctrl+shift+d", "deleteDir()");
		addShortcut(b, "ctrl+shift+s", "cloneFromLocal()");
		addShortcut(b.ba, "ctrl+o", "setTimeMode()");
		addShortcut(this, "ctrl+=", "zoomin()");
		addShortcut(this, "ctrl+-", "zoomout()");
		
		addShortcut(this.b, "ctrl+n", "getSizeOfEditorMemory()");
		
		addShortcut(this.b, "ctrl+h", "showLogsText()");
//		addShortcut(this.b, "alt+1", "setYellowChar()");
		addShortcut(this, "ctrl+shift+1", "checkNetworks()");
		addShortcut(this.b, "ctrl+shift+2", "autoCheckNetworks()");
		
		this.text.addAction(this.b.ui.find);
		this.text.addAction(this.b.ui.charCount);
		this.text.addAction(this.b.ui.hideFilePanel);
		b.ui.hideFilePanel.setShortcut("alt+shift+b");
		this.text.addAction(this.b.ui.writingview);

		addShortcut(this, "ctrl+/", "testt()");
		addShortcut(b, "f9", "turnOffAnimation()");
	}

	void testt() {
//		b.ba.getColorDialog();
//		imgWidget img = new imgWidget(b);
//		img.updateImgAndText();
//		img.updateHistoryPicList();
//		img.showFullScreen();
//		b.getMessageBox("", "共"+text.undo.al_command.size()+"条撤销重做记录");
//		b.checkDocTitle();
//		if(b.k == null) {
//			b.k = new kiwix(b);
//			b.k.startKiwixServer();
//			b.k.startWeb();
//		}else {
//			String text = b.text.textCursor().selectedText();
//			if(text.isEmpty())
//				b.k.closeKiwix();
//			else b.k.web.load(new QUrl(b.k.getUrl(text)));
//		}
		
	
	}

	public void resetEditor() {
		String content = black.text.toPlainText();
		dataForOpenedFile openedfile = this.b.findInOpenedFiles(this.b.infoOfCurrentEditing.fileName);
		QTreeWidgetItem treeWidgetItemOfCurrentEditing = this.b.TreeWidgetItemOfCurrentEditing;
		this.b.changedFileOfCurrentEdit();
		openedfile.editor.text.dispose();
		openedfile.editor.text = null;
		openedfile.editor = null;
		openedfile.content = content;
		
		this.b.openFileByTreeItem(treeWidgetItemOfCurrentEditing);
	}

	public void addShortcut(QObject o, QKeySequence key, String mothedname) {
		QAction a = new QAction(this.text);
		a.setShortcut(key);
		a.triggered.connect(o, mothedname);
		this.text.addAction(a);
	}

	public void addShortcut(QObject o, String shortcut, String mothedname) {
		QAction q = new QAction(this.text);
		q.setShortcut(shortcut);
		q.triggered.connect(o, mothedname);
		this.text.addAction(q);
	}

	public void zoomin() {
//		if(b.isAdmin())
		this.b.setEditorZoomValue(this.zoomvalue + 1);
	}

	public void zoomout() {
//		if(b.isAdmin())
		this.b.setEditorZoomValue(this.zoomvalue - 1);
	}
}
