package yang.app.qt.black;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.qt.QNoNativeResourcesException;
import io.qt.core.QEvent;
import io.qt.core.QMargins;
import io.qt.core.QObject;
import io.qt.core.QPoint;
import io.qt.core.QRect;
import io.qt.core.QRectF;
import io.qt.core.Qt.ConnectionType;
import io.qt.core.Qt.InputMethodQuery;
import io.qt.core.Qt.WidgetAttribute;
import io.qt.gui.QFont;
import io.qt.gui.QFontMetrics;
import io.qt.gui.QImage;
import io.qt.gui.QInputMethod;
import io.qt.gui.QInputMethodEvent;
import io.qt.gui.QInputMethodEvent.Attribute;
import io.qt.gui.QKeyEvent;
import io.qt.gui.QMouseEvent;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPalette.ColorRole;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.gui.QTextDocument;
import io.qt.widgets.QApplication;
import io.qt.widgets.QLabel;
import io.qt.widgets.QTextEdit;
import io.qt.widgets.QTreeWidgetItem;

class bTextEdit extends QTextEdit {
	private String preeditString;
	boolean wrap;
	black b;
	private QTreeWidgetItem currentItem;
	TextRegion tr;
	/**
	 * 此开关用来确定输入法是否处于输入状态。在输入法存在中间字符串，且没有提交最终字符串之前，此开关一直处于真值
	 */
	boolean inputing;
	QLabel caret;
	private long caretChangedTime;
	QImage arrow = new QImage("classpath:yang/app/qt/black/icons/arrow.png");
	protected boolean caretStoped;
	private bRunnable bRunnable_ForUpdates;
	QTextCursor tcStart;
	QTextCursor tcEnd;
	boolean noUpdateTitle;
	private boolean accicesable = true;
	private boolean showable = true;
	public boolean redoAvailable,undoAvailable;
	boolean caretMoveWithNoAnmiation = false;
	int preeditStringPos = -1,preeditStringLength;
	String IMECommitString;
	String IMEPreeditString;
	/**
	 * 强制隐藏插入符开关
	 */
	boolean hideCaret;
	boolean notUpdateCaretPos;
	bRunnable brunnable_caret;

	private void redoAvailable(boolean available){
		redoAvailable = available;
//		System.out.println("redo: "+available);
	}
	private void undoAvailable(boolean available) {
		undoAvailable = available;
//		System.out.println("undo: "+available);

	}
	public bTextEdit(black b) {
//		setViewport(new QGLWidget());
		this.b = b;
//		setAcceptDrops(false);
		setContentsMargins(100, 0, 100, 0);
		setCursorWidth(b.getEditorCaretWidthValue());
		
		initCaret();
		
		cursorPositionChanged.connect(this,"cursorPosChanged()");
		installEventFilter(this);
		super.redoAvailable.connect(this, "redoAvailable(boolean)");
		super.undoAvailable.connect(this,"undoAvailable(boolean)");
		verticalScrollBar().installEventFilter(this);
//		viewport().setUpdatesEnabled(false);
		verticalScrollBar().rangeChanged.connect(this, "rangeChanged()");
		verticalScrollBar().sliderMoved.connect(this,"rangeChanged()");
		verticalScrollBar().valueChanged.connect(this,"rangeChanged()");
//		QInputMethod aa = QApplication.inputMethod();
//		aa.setInputItemRectangle(new QRectF(0, 0, 100, 100));
//		aa.setVisible(false);
//		aa.anchorRectangleChanged.connect(this, "test()", ConnectionType.AutoConnection);
//		aa.visibleChanged.connect(this, "test()", ConnectionType.AutoConnection);
//		aa.cursorRectangleChanged.connect(this, "test()", ConnectionType.AutoConnection);
//		aa.destroyed.connect(this, "test()", ConnectionType.AutoConnection);
//		aa.inputDirectionChanged.connect(this, "test()", ConnectionType.AutoConnection);
//		aa.inputItemClipRectangleChanged.connect(this, "test()", ConnectionType.AutoConnection);
//		aa.animatingChanged.connect(this, "test()", ConnectionType.AutoConnection);
//		aa.keyboardRectangleChanged.connect(this, "test()", ConnectionType.AutoConnection);
		
	}
	void test() {
//		b.debugPrint("ssssssssssssssssssssssss");
		QInputMethod aa = QApplication.inputMethod();
		aa.setInputItemRectangle(new QRectF(100, 0, 100, 100));
		aa.setVisible(true);
		
		
	}
	
	/**
	 * 初始化插入符并启动闪烁线程
	 */
	void initCaret() {
		caret = new QLabel(b.textWidget);
		caret.setAutoFillBackground(true);
		caret.setAttribute(WidgetAttribute.WA_TransparentForMouseEvents);
		caret.setGeometry(0, 0, 0, 0);
		if(cursorWidth() == 0) {
			brunnable_caret = new bRunnable(500) {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateCaret();
				}
			};
		}
	}
	
	void updateCaret() {
		try {
			if(!isVisible() || !hasFocus()) return;
		}catch(QNoNativeResourcesException e) {};
		
		
		if(System.currentTimeMillis()-caretChangedTime < 30000) {
			cursorPosChanged(false);
		}else {
			if(caret != null && !caretIsVisible()) {
				cursorPosChanged(true);
				caretStoped = true;
			}
		}
	}
	void pauseUpdateFor100MS(boolean updateNow,int time) {
		if(updateNow) {
			if(!viewport().updatesEnabled()) viewport().setUpdatesEnabled(true);
			setCaretVisible(false);
			viewport().repaint();
		}else {
			if(b.writingView == 0)return;
			else if(bRunnable_ForUpdates != null)return;
		}
		
		viewport().setUpdatesEnabled(false);


		bRunnable_ForUpdates = new bRunnable(time,true,false,true,true) {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					viewport().setUpdatesEnabled(true);
					bRunnable_ForUpdates = null;
				}
		};
	}
	
	
	
	public void rangeChanged() {
		if(!showable)return;
		if(!isVisible())return;
		b.setMarkAndStyle();
		
		updateCaret(2);
		if(b.writingView != 1)return;
		String topAndBottom = b.getStringValueFromSettings(appInfo.writingViewTextTopAndBottom, "0 0");
		String[] args = cheakDocument.checkCommandArgs(topAndBottom);
		if(!args[0].equals("0") && !args[1].equals("0")) return;
		setRange();
	}
	/**
	 * 返回根据当前滚动条位置计算出的上下留白值
	 * 
	 * @return
	 */
	int[] setRange() {
		int[] i = null;
		if(b.writingView == 0) {
			b.textWidget.setGeometry(b.textWidget.x(), 0, b.textWidget.width(), b.height());
			b.textWidget.setContentsMargins(0, 0, 0, 0);
			i = new int[] {0,0};
			return i;
		}
		/*
		 * 下面代码上下留白值设定为0时执行
		 * 在顶部时：
		 *顶部留白，底部不留白
		 * 
		 * 在底部时：
		 * 如果滚动条最大值超过编辑器高度，底部不留白
		 * 滚动条最大值没超过编辑器高度，底部留白
		 * 
		 */
		int space = 20;
		int topMargin = 50;
		QRect geo = b.ui.verticalLayout.geometry();
//		b.setUpdatesEnabled(false);
		if(verticalScrollBar().value() <= 4) {
			b.textWidget.setGeometry(b.textWidget.x(), space, b.textWidget.width(), b.height()-space);
//			b.setAnimation(b.textWidget, "geometry", b.textWidget.geometry(), new QRect(b.textWidget.x(), space, b.textWidget.width(), b.height()-space), 500);

			//下面的方法存在性能问题，文档字数多的时候会明显卡顿
//			b.ui.verticalLayout.setGeometry(new QRect(geo.x(), space, geo.width(), b.height()-space));
			b.textWidget.setContentsMargins(0, topMargin, 0, 0);
			i = new int[] {space,0};
		}
		else if(verticalScrollBar().value() >= verticalScrollBar().maximum()-viewport().height()){
			b.textWidget.setGeometry(b.textWidget.x(), 0, b.textWidget.width(), b.height()-space);
			b.textWidget.setContentsMargins(0, 0, 0, 0);
			i = new int[] {0,space};

		}else {
			b.textWidget.setGeometry(b.textWidget.x(), 0, b.textWidget.width(), b.height());
			b.textWidget.setContentsMargins(0, 0, 0, 0);
			i = new int[] {0,0};
		}
//		b.centralWidget().layout().addWidget(b.textWidget);
		b.debugPrint("e: "+verticalScrollBar().value());
//		b.setUpdatesEnabled(true);
		b.debugPrint("a "+b.textWidget.geometry());
//		b.textWidget.updateGeometry();
		b.debugPrint("b "+b.textWidget.geometry());
//		b.btext.scroll();
		b.update();
		
//		b.textWidget.hide();
//		b.textWidget.showNormal();
//		if(bRunnable == null)
//		bRunnable = new bRunnable(10) {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				rangeChanged();
//				this.stop();
//			}
//		};
		return i;
	}
	public boolean eventFilter(QObject arg__1, QEvent arg__2) {
//		System.out.println(arg__2.type());
		if (arg__1.equals(this)) {
//			rangeChanged();

//			System.out.println(arg__2.type());

			if (arg__2.type() == QEvent.Type.Hide) {
				if (caret != null)
					caret.hide();
			} else if (arg__2.type() == QEvent.Type.FocusOut) {
				if (caret != null)
					caret.hide();
			} else if (arg__2.type() == QEvent.Type.FocusIn) {
				if (caret != null)
					updateCaret(2);
			} else if (arg__2.type() == QEvent.Type.Resize) {
				b.setTextVScrollBarLocation();
			} else if (arg__2.type() == QEvent.Type.Show) {
				if (caret != null)
					setCaretVisible(true);
//				setParent(null);
//				System.out.println("hello");
			} else if (arg__2.type() == QEvent.Type.KeyPress) {
//				QKeyEvent k = (QKeyEvent) arg__2;
//				QTextCursor t = textCursor();
//
//				if (!k.text().isEmpty()) {
//					if (!t.hasSelection()) {
//						undo.setNextCommand(
//								new undoCommand("键入", k.text(), t.position(),
//								t.position() + k.text().length()));
//						t.insertText(k.text());
//					} else {
//						bAction.setTitleOnSelectionText(b, k.text());
//					}
//					return true;
//				}

			}
//			else if(arg__2.type() == QEvent.Type.LayoutRequest) {
//				pauseUpdateFor100MS(false,200);
//			}
		} else if (arg__1.equals(verticalScrollBar())) {
			if (arg__2.type() == QEvent.Type.Show) {
				if (b.writingView > 0)
					b.setTextVScrollBarLocation();
//				System.out.println("hello");
			}
		}
		return super.eventFilter(arg__1, arg__2);
	}
	
	/**
	 * 1如果光标已经停止或还在闪烁，让其开始或继续闪烁，并更新时间戳
	 * 2如果光标已经停止或还在闪烁，强制其显示，并更新时间戳
	 * 
	 */
	void updateCaret(int act) {
		if(act == 1) {
			caretChangedTime = System.currentTimeMillis();
			if(caretStoped) {
				cursorPosChanged(false);
				caretStoped = false;
			}
		}else if(act == 2) {
			caretChangedTime = System.currentTimeMillis();
			cursorPosChanged(true);
			caretStoped = false;
		}
	}
	void cursorPosChanged() {
		if(!isVisible())return;
		updateCaret(2);
	}
	void cursorPosChanged(boolean isShow) {
//		if(cursorWidth() != 0) {
//			caret.hide();
//			return;
//		}
//		if(!accicesable || hideCaret) {
//			if(caretIsVisible()) {
//				setCaretVisible(false);
//			}
//			return;
//		}
//		if(!notUpdateCaretPos) {
//			QRect r = cursorRect();
//			QPoint p = new QPoint(r.x(),r.y());
//			QPoint mapTo = mapTo(b.textWidget, p);
//			r.setX(mapTo.x());
//			r.setY(mapTo.y());
//
//			int x = 0;
//				int y = r.y()-2;
//				
//				if(preeditStringPos == -1) {
//					x = r.x()-3;
//				}else {
//					QTextCursor t = textCursor();
//					int pos = preeditStringPos+preeditStringLength;
//					if(pos > charCountOfDoc())
//					t.setPosition(charCountOfDoc());
//					else 
//						t.setPosition(pos);
//
//					QRect cursorRect = cursorRect(t);
//					QPoint p_ = new QPoint(cursorRect.x(),cursorRect.y());
//					QPoint mapTo_ = mapTo(b.textWidget, p_);
//					cursorRect.setX(mapTo_.x());
//					cursorRect.setY(mapTo_.y());
//					
//					if(cursorRect.y() != r.y()) {
//						y = cursorRect.y()-2;
//					}
//					x = cursorRect.x()-3;
//				}
//				
//				if(System.currentTimeMillis()-b.btext.posTime >= 50) caretMoveWithNoAnmiation = false;
//				
//				
//				QRect end = new QRect(x, y, b.getEditorCaretWidthValue(), r.height()+5);
//				
//				if(caret.x() != end.x() || caret.y() != end.y() ||
//						caret.width() != end.width() || caret.height() != end.height()) {
//					if(b.getAnimationTime()!=0 && !caretMoveWithNoAnmiation) {
////						if(!inputing && caret_end_IME_last != null) {
////							b.setAnimation(caret, "geometry", caret_end_IME_last, end, 100);
////							caret_end_IME_last = null;
////						}
////						else {
//							b.debugPrint(caret.geometry()+" &&& "+end+" "+caret.isVisible());
//							b.setAnimation(caret, "geometry", caret.geometry(), end, b.getAnimationTime());
////						}
//					}else {
//						b.debugPrint(caret.geometry()+" $$$ "+end);
//						caret.setGeometry(end);
//					}
//				}
//		}
//		if(isVisible() && hasFocus()) {
//			if(caret != null) {
//				if(!isShow) {
//					if(caretIsVisible()) {
//						setCaretVisible(false);
//					}else {
//						setCaretVisible(true);
//					}
//				}else {
//					setCaretVisible(true);
//
//				}
//				
//			}
//		}else {
//			setCaretVisible(false);
//		}
	}
	void setCaretVisible(boolean show){
		caret.setAutoFillBackground(show);
		if(!caret.isVisible())caret.show();
	}
	boolean caretIsVisible() {
		if(caret.isVisible() && caret.autoFillBackground())return true;
		else return false;
	}
	public void findText(String text) {
		String str = toPlainText();
		int index = str.indexOf(text, textCursor().position());
		textCursor().setPosition(index);
	}

	public int blockCount() {
		QTextCursor tc = textCursor();
		tc.movePosition(QTextCursor.MoveOperation.End);
		int blockNumber = tc.blockNumber();
		return blockNumber;
	}

	public int charCountOfDoc() {
		QTextCursor tc = textCursor();
		tc.select(QTextCursor.SelectionType.Document);
		return tc.selectionEnd() - tc.selectionStart();
	}
	
	public int charCountOfSelection() {
		QTextCursor tc = textCursor();
		return tc.selectionEnd() - tc.selectionStart();
	}

	public int charCountWithoutReturnOfDoc() {
		QTextCursor tc = textCursor();
		tc.select(QTextCursor.SelectionType.Document);
		int charCount = tc.selectionEnd() - tc.selectionStart();
		tc.movePosition(QTextCursor.MoveOperation.End);
		int charWithoutReturn = charCount - tc.blockNumber();
		return charWithoutReturn;
	}

	public QTextDocument document_b() {
		QTextDocument doc = new QTextDocument(toPlainText());
		return doc;
	}
	
	void setAccicesable(boolean canEditable,boolean canReadable) {
		accicesable = canEditable;
		setReadOnly(!canEditable);
		showable = canReadable;
	}
	boolean isAccicesable() {
		return accicesable;
	}
	boolean isTextContentVisible() {
		return showable;
	}
	
	public void paintEvent(QPaintEvent e) {
		if(!accicesable && !showable) {
			QPainter p = new QPainter(viewport());
			p.fillRect(viewport().rect(), palette().color(ColorRole.Base));
			p.setPen(palette().color(ColorRole.Text));
			String text = null;
			if(appInfo.miniMode.equals("1")) {
				if(b.writingView == 0)
				text = "Black处于备用模式";
				else return;
			}
			else
				text = "此文档已被禁止访问";
			QFont font = QApplication.font();
			font.setPointSize(font.pointSize()+b.getEditorTextZoomValue());
			p.setFont(font);
			QFontMetrics fm = p.fontMetrics();
			QRect br = fm.boundingRect(text);
			p.drawText((viewport().width()-br.width())/2, (viewport().height()-br.height())/2, text);
			return;
		}
		super.paintEvent(e);
		
		if(b.noMarkChar)return;
		
		QPainter p = new QPainter(this);
		boolean no = true;
		QRect cr = cursorRect();
		QRect rect = e.rect();
//		System.out.println(e.rect()+" caretH:"+cr.height()+" W:"+cr.width());
		if(rect.height() == cr.height() && rect.width() <= 8) no = false;
		if(no) {
			int focusmode = black.getIntValueFromSettings(appInfo.focusMode, "0");
			if(focusmode > 0) {
				return;
			}
			int fontsize = b.getEditorFontSize();
			int zoom = b.getEditorTextZoomValue();
			if(fontsize+zoom < 10) {
				return;
			}
			tcStart = cursorForPosition(new QPoint(0, 0));
			tcEnd = cursorForPosition(new QPoint(0,height()));
//			System.out.println(tc1.blockNumber()+" "+tc2.blockNumber());
			
			do {
				tcStart.movePosition(MoveOperation.EndOfBlock);
				QRect c = cursorRect(tcStart);
				if(tcStart.blockNumber() > tcEnd.blockNumber())break;
//				if(tcStart.position() != textCursor().position()) 
				p.drawImage(c.x()+5, c.y()+c.height()/2, arrow);
//				QTextCursor tc = textCursor();
//				if(!isReadOnly() && tc.blockNumber() == tcStart.blockNumber()) {
//					if(cheakDocument.cheakStringOnly(tcStart.block().text(),appInfo.syls)) {
//						QFont font = new QFont("微软雅黑",b.getEditorFontSize()+b.getEditorTextZoomValue());
//						p.setFont(font);
//						p.setRenderHint(RenderHint.Antialiasing);
//						String text = "编辑标题";
//						QRect br = p.fontMetrics().boundingRect(text);
//						int bgW = br.width()+4;
//						int charH = p.fontMetrics().boundingRect('编').height();
//						int bgH = charH+4;
//						p.setBrush(new QColor(Qt.GlobalColor.black));
//						p.drawRoundedRect(c.x()+17,c.y()+5,bgW+4,bgH, 2, 2);
//					
//						p.setPen(new QPen(new QColor(Qt.GlobalColor.white),0));
//						p.drawText(c.x()+19, c.y()+17, text);
//					}
//				}
			} while (tcStart.movePosition(QTextCursor.MoveOperation.NextBlock));
//			System.out.println(start+" hello "+end);
			
		}
//		if(preeditStringPos != -1) {
//			
//			int y = rect.y()+rect.height();
////			p.setBrush(new QColor(Qt.GlobalColor.black));
//			QPen pen = new QPen(new QColor(Qt.GlobalColor.black), 2);
//			pen.setStyle(PenStyle.DashLine);
//			p.setPen(pen);
//			
////			b.debugPrint("ok");
//			p.drawLine(rect.x(), rect.y()+rect.height(), caretX_IME, rect.y()+rect.height());
//		}
		
		
//		if(b.writingView == 0) {
//			if(b.btext != null) {
//				QColor line = new QColor(Qt.GlobalColor.black);
//				QColor title = new QColor(Qt.GlobalColor.black);
//				QColor top = new QColor(Qt.GlobalColor.blue);
//				QColor caret = new QColor(Qt.GlobalColor.black);
//				b.btext.titleMap(p,200,100,0,title,caret,top,line);
//			}
//		}
	}

	protected void mouseDoubleClickEvent(QMouseEvent e) {
		black.seclectText(this, true);
	}
	

	public boolean event(QEvent e) {
		
//		System.out.println(e.type());
		
//		if(e.type() == QEvent.Type.Paint) {
//			pauseUpdateFor100MS(false,200);
//		}
		
//		if(e.type() == QEvent.Type.KeyPress) {
//			QKeyEvent key = (QKeyEvent) e;
//			if(key.key() == Qt.Key.Key_Left.value() || key.key() == Qt.Key.Key_Right.value() || key.key() == Qt.Key.Key_Up.value() || key.key() == Qt.Key.Key_Down.value()) {
//				caretMoveWithNoAnmiation = true;
//				System.out.println("niuo ");
//			}
//		}else if(e.type() == QEvent.Type.KeyRelease) {
//			QKeyEvent key = (QKeyEvent) e;
//			if(key.key() == Qt.Key.Key_Left.value() || key.key() == Qt.Key.Key_Right.value() || key.key() == Qt.Key.Key_Up.value() || key.key() == Qt.Key.Key_Down.value()) {
//				caretMoveWithNoAnmiation = false;
//			}
//		}
//		else 
			if (e.type() == QEvent.Type.InputMethod) {
			
			updateCaret(1);
			QInputMethodEvent me = (QInputMethodEvent) e;
			IMEPreeditString = me.preeditString();
			IMECommitString = me.commitString();
			b.debugPrint("输入法中间字符串是否为空："+me.preeditString().isEmpty()+" 提交字符串是否为空："+me.commitString().isEmpty()
					);
			int start = 0;
			List<Attribute> lis = null;
			QInputMethodEvent newImeE = null;
			if(!b.getBooleanValueFromSettings(appInfo.useInLineIME, "false")
					|| b.getBooleanValueFromSettings(appInfo.fixedCaretPosWhenInput, "false")) {
				lis = new ArrayList();
				Iterator<Attribute> it = me.attributes().iterator();
				while(it.hasNext()) {
					Attribute a =  it.next();
					if (a.type().equals(QInputMethodEvent.AttributeType.Cursor)) {
						start = a.start();
						a.setStart(0);
						a.setLength(1);
					}
					lis.add(a);
				}
				newImeE = new QInputMethodEvent(me.preeditString(), lis);
//				newImeE = me;
			}else {
				newImeE = me;
			}
			
			this.preeditString = newImeE.preeditString();
			if ((this.b.keywords != null) && (this.b.keywords.isVisible())) {
				if ((me.commitString().isEmpty()) || (!cheakDocument.isAllNoChineseString(me.commitString()))) {
					newImeE.setCommitString(me.commitString(), me.replacementStart(), me.replacementLength());
				}
			} else {
				newImeE.setCommitString(me.commitString(), me.replacementStart(), me.replacementLength());
			}
			e = newImeE;
			if ((this.preeditString.equals(" ")) && (me.commitString().isEmpty())) {
				b.monitorOff = true;
				b.showIMEString("",-1,null,null,null,false);
				this.inputing = true;
				if ((this.b.keywords != null) && (this.b.keywords.isVisible())) {
					if(b.findview == 5) {
						b.keywords.hide();
					}
				}
				return true;
			}
			else if ((!this.preeditString.equals(" ")) && (!this.preeditString.isEmpty())) {
				if(this.preeditString.length() == 1)updateCaret(2);
				if(b.getBooleanValueFromSettings(appInfo.noCaretWhenInput, "false"))
					setCursorWidth(0);
				
				b.monitorOff = true;

				this.inputing = true;
				boolean dontFind = false;
				if ((this.b.keywords != null) && (this.b.keywords.isVisible())) {
					if(b.findview == 5) {
						b.keywords.hide();
					}
					currentItem = this.b.keywords.tree.currentItem();
//					if (this.currentItem != null) {
//						this.tr = ((TextRegion) this.currentItem.data(1, 0));
//					}
					if(currentItem != null && tr != null) {
						if (this.preeditString.indexOf("Z") != -1) {
//							tr.setData(preeditString);
							dontFind = true;
							if (this.tr.describe != null) {
								String[] sub = cheakDocument.subString(this.tr.describe, appInfo.keywordsSeparator);
								this.tr.text = this.tr.describe;
								this.tr.showname = sub[0];
								if (!sub[1].equals("")) {
									this.tr.filename = sub[1];
								}
								if (this.preeditString.indexOf("Z'Z") == -1) {
									String str = null;
									if (this.tr.filename == null) {
										str = this.tr.showname;
									} else {
										str = this.tr.showname + " (" + this.tr.filename + ")";
									}
									this.currentItem.setText(0, str);
									if(!b.getBooleanValueFromSettings(appInfo.moreFindMarkWithPinyin, "false"))
										this.b.showDialogAtEditorCaretPosTop(this, this.b.keywords);
									else b.showDialogAtLeftBottom(this, b.keywords);
								} else {
									Attribute att = new Attribute(
											QInputMethodEvent.AttributeType.Cursor, 0, this.preeditString.length(), null);
									ArrayList<Attribute> atts = new ArrayList();
									atts.add(att);
									QInputMethodEvent ime = new QInputMethodEvent(null, atts);
									ime.setCommitString("");

									return super.event(ime);
								}
							}
						} else if (this.preeditString.indexOf("X") != -1) {
							dontFind = true;
							this.currentItem.setText(0, this.b.getDate());
							if(!b.getBooleanValueFromSettings(appInfo.moreFindMarkWithPinyin, "false"))
								this.b.showDialogAtEditorCaretPosTop(this, this.b.keywords);
							else b.showDialogAtLeftBottom(this, b.keywords);						
						} else {
							//判断编码串里包含了大写字母，判断大写字母与关键词框里的项目的序号是否匹配
							for(int i=0;i<b.keywords.tree.topLevelItemCount();i++) {
								QTreeWidgetItem item = b.keywords.tree.topLevelItem(i);
								String text = item.text(0);
								String s = text.substring(0, 1);
								if(preeditString.indexOf(s) != -1) {
									dontFind = true;
									this.tr = ((TextRegion) item.data(1, 0));
									b.debugPrint("输入法中间字符串输入了关键词编号："+s);
									b.keywords.tree.takeTopLevelItem(i);
									b.keywords.tree.insertTopLevelItem(0, item);
									b.keywords.tree.setCurrentItem(item);
								}
							}
						}
					}
					
				}
				

				//如果没有开启了双拼转全拼，默认由下面这一行处理拼音检索关键词的功能
				b.showIMEString(preeditString,start,null,null,null,dontFind);
				//如果没有开启双拼转全拼，则由下面的if块处理拼音检索关键词的功能
				b.debugPrint("没有启用双拼转全拼");
				if(!Boolean.valueOf((String)b.settings.value(appInfo.conversionToFullPinyin, "false"))){
					b.findKeywordByPinyin(this.preeditString,null,dontFind);
				}
				if(b.getBooleanValueFromSettings(appInfo.useInLineIME, "false"))
					return super.event(newImeE);
				return true;
			} else if ((this.preeditString.isEmpty()) && (me.commitString().isEmpty())) {
				if(cursorWidth() == 0)
				setCursorWidth(b.getEditorCaretWidthValue());
				if ((this.b.keywords != null) && (this.b.keywords.isVisible())) {
					if(b.findview == 5) {
						b.keywords.hide();
					}
				}
				if(b.findInMark_brun != null) {
					b.findInMark_brun.stop();
					b.findInMark_brun = null;
				}
				b.debugPrint("A");
				b.monitorOff = false;
				
				b.showIMEString("",-1,null,null,null,false);
				
//				b.getMessageBox("", "yes");
				if ((this.b.keywords != null) && (this.b.keywords.isVisible())) {
					this.b.keywords.hide();
				}
				this.tr = null;
				this.inputing = false;
				if(b.getBooleanValueFromSettings(appInfo.useInLineIME, "false"))
					return super.event(newImeE);
				return true;
			}
			else if (!me.commitString().isEmpty()) {
				if(cursorWidth() == 0)
				setCursorWidth(b.getEditorCaretWidthValue());

				if(b.findInMark_brun != null) {
					b.findInMark_brun.stop();
					b.findInMark_brun = null;
				}
				b.debugPrint("B");
				
//				if(black.isSpeak && cheakDocument.cheak(me.commitString().substring(me.commitString().length()-1, me.commitString().length()).charAt(0)))
//					black.speakText(black.seclectText(this, false)+me.commitString(),-1);
				
				b.showIMEString("",-1,null,null,null,false);
				
				if (!Boolean.valueOf((String) b.settings.value(appInfo.hideIMEPanel, "false"))) {
//					if(b.IME != null && b.IME.isVisible()) {
//						b.IME.hide();
//					}
					if(textCursor().hasSelection()) {
						textCursor().insertText(me.commitString());
						return true;
					}
				}
				
				
				if (me.commitString().indexOf("S") == -1) {
					//提交的字符串中必须含有中文字符才允许上屏，不允许编码字符（英文和英文分号等）上屏，
					//如果检测到提交的字符串是编码类字符，则检查是否存在已标记的关键词，如果存在，则将关键词上屏
					if (cheakDocument.isEnglishString(me.commitString()) 
							|| me.commitString().indexOf(";") != -1) {//微软双拼的编码字符包括英文和英文分号
//						newImeE.setCommitString(null);
						if(tr != null)
						b.debugPrint(tr.data+"|"+me.commitString());
						//没有找到已经标记的关键词
						if (this.tr == null) {
							super.event(me);
//							notUpdateCaretPos = false;
//							updateCaret(2);
//							return true;
						}else {
							me.setCommitString(null);
							super.event(me);
							//找到了已经标记的关键词
							QTreeWidgetItem ti = new QTreeWidgetItem();
							ti.setData(1, 0, this.tr);
							this.b.keywords.whenSubmit(ti);
							this.tr = null;
						}
						
//						hideCaret = false;
						notUpdateCaretPos = false;
//						updateCaret(2);
						return true;
					}
					//提交的是非英文和编码类字符串
					else {
						newImeE.setCommitString(me.commitString());
					}
				//包含大写S的英文字符串可以被提交，但会先删除掉字符串中的所有大写S
				} else {
					
					String newText = me.commitString().replaceFirst("S", "");

					newImeE.setCommitString(newText);
				}
				if ((this.b.keywords != null) && (this.b.keywords.isVisible())) {
					this.b.keywords.hide();
				}
				b.monitorOff = false;
				this.inputing = false;
			}
//			hideCaret = false;
			notUpdateCaretPos = false;
//			updateCaret(2);
		}
		return super.event(e);
	}
}
