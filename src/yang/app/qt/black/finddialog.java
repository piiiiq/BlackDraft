package yang.app.qt.black;


import io.qt.core.QEvent;
import io.qt.core.QObject;
import io.qt.core.Qt;
import io.qt.gui.QKeyEvent;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveMode;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.gui.QTextDocument;
import io.qt.gui.QTextDocument.FindFlag;
import io.qt.gui.QTextDocument.FindFlags;
import io.qt.widgets.QApplication;
import io.qt.widgets.QDialog;
import io.qt.widgets.QMessageBox;
import io.qt.widgets.QWidget;

public class finddialog extends QDialog {
	Ui_finddialog ui = new Ui_finddialog();
	black b;
	boolean onlyShowHistoryOfBlackCommands;
	FindFlags findFlags = new FindFlags();
	private int findIn;

	public static void main(String[] args) {
		QApplication.initialize(args);

		finddialog testfinddialog = new finddialog();
		testfinddialog.show();

		QApplication.exec();
	}

	public finddialog() {
		this.ui.setupUi(this);
//		setFont(new QFont("微软雅黑", 10));

		this.ui.findText.insertItem(0, "hello");
		this.ui.findText.insertItem(2, "test");

		setFixedSize(width(), height());
		this.ui.findText.setFocus();
		this.ui.findText.lineEdit().selectAll();
//		this.ui.findText.currentIndexChanged.connect(this.ui.findText.lineEdit(), "selectAll()");
//		QApplication.connect(ui.findText, "currentIndexChanged", ui.findText.lineEdit(),"selectAll()");
	}

	public finddialog(QWidget parent) {
		super(parent);
		this.b = ((black) parent);

		this.ui.setupUi(this);
		if (b.text.isReadOnly()) {
			ui.replace.setEnabled(false);
			ui.replace_find.setEnabled(false);
			ui.replaceall.setEnabled(false);
		}
		setWindowTitle("查找/替换");
		this.ui.next.pressed.connect(this, "next()");
		this.ui.previous.pressed.connect(this, "previous()");
//		setFont(new QFont("微软雅黑", 10));
		setFixedSize(width(), height());
		this.ui.findText.editTextChanged.connect(this, "findstrchanged(String)");

		this.ui.findText.setFocus();
		this.ui.findText.lineEdit().setPlaceholderText("键入搜索文本");

		this.ui.findText.lineEdit().selectAll();
//		this.ui.findText.currentIndexChanged.connect(this.ui.findText.lineEdit(), "selectAll()");
		QApplication.connect(ui.findText, "currentIndexChanged(int)", ui.findText.lineEdit(),"selectAll()");

		this.ui.findText.installEventFilter(this);
		if (this.ui.findText.currentText().isEmpty()) {
			this.ui.next.setEnabled(false);
			this.ui.previous.setEnabled(false);
			this.ui.replace_find.setEnabled(false);
			this.ui.replaceall.setEnabled(false);
			this.ui.replace.setEnabled(false);
		}
		this.ui.document.setChecked(true);
		this.ui.replaceall.clicked.connect(this, "replaceAll()");
		this.ui.replace_find.clicked.connect(this, "replaceAndFind()");
		this.ui.replace.clicked.connect(this, "replace()");
		ui.case_2.clicked.connect(this, "caseS(boolean)");
		ui.case_2.setChecked(true);
		installEventFilter(this);
		b.btext.currentFindCursor = b.btext.text.textCursor();
		ui.document.setChecked(true);
		ui.document.clicked.connect(this,"findInDoc(boolean)");
		ui.files.clicked.connect(this,"findInFiles(boolean)");
	}
	void findInDoc(boolean b) {
		if(b)
		findIn = 0;
		
	}
	void findInFiles(boolean b){
		if(b)
		findIn = 1;
	}
	/**
	 * 区分大小写开关
	 * @param b
	 */
	void caseS(boolean b) {
		if(!b)
		findFlags.set(FindFlag.FindCaseSensitively);
		else findFlags.clear(FindFlag.FindCaseSensitively);
	}
	

	void replace() {
		String findText = getFindText();
		if (!findText.isEmpty()) {
			this.b.btext.currentFindCursor.insertText(this.ui.replace_str.text());
		}
	}

	void replaceAndFind() {
		String findText = getFindText();
		if (!findText.isEmpty()) {
			if(b.btext.currentFindCursor != null && b.btext.currentFindCursor.hasSelection()) {
				this.b.btext.currentFindCursor.insertText(this.ui.replace_str.text());
			}
			if(!findFlags.isSet(FindFlag.FindBackward))
				next();
			else previous();
		}
	}

	void findstrchanged(String str) {
		if (str.isEmpty()) {
			this.ui.next.setEnabled(false);
			this.ui.previous.setEnabled(false);
			this.ui.replace_find.setEnabled(false);
			this.ui.replaceall.setEnabled(false);
			this.ui.replace.setEnabled(false);
		} else {
			this.ui.next.setEnabled(true);
			this.ui.previous.setEnabled(true);
			if (b.text.isReadOnly())
				return;

			this.ui.replace_find.setEnabled(true);
			this.ui.replaceall.setEnabled(true);
			this.ui.replace.setEnabled(true);
		}
	}

	public boolean eventFilter(QObject o, QEvent e) {
		if ((o.equals(this.ui.findText)) && (e.type() == QEvent.Type.KeyPress)) {
			QKeyEvent keypress = (QKeyEvent) e;
			if (keypress.key() == Qt.Key.Key_Return.value()) {
				String findText = getFindText();
				if (findText.indexOf(appInfo.blackCommand) != 0) {
					next();
				} else {
					this.b.execCommand(findText);
				}
				return true;
			}
		} else if ((o.equals(this)) && (e.type() == QEvent.Type.Show)) {
			if (!this.onlyShowHistoryOfBlackCommands) {
				for (int i = this.b.findHistory.size() - 1; i >= 0; i--) {
					this.ui.findText.insertItem(this.b.findHistory.size() - 1 - i, (String) this.b.findHistory.get(i));
				}
				String selectedText = b.text.textCursor().selectedText();
				if (!selectedText.isEmpty()) {
//        	ui.findText.insertItem(0, selectedText);
					ui.findText.setEditText(selectedText);
					ui.findText.lineEdit().selectAll();
				}
			} else {
				for (int i = this.b.findHistory.size() - 1; i >= 0; i--) {
					if (((String) this.b.findHistory.get(i)).indexOf(appInfo.blackCommand) == 0) {
						this.ui.findText.insertItem(this.b.findHistory.size() - 1 - i,
								(String) this.b.findHistory.get(i));
					}
				}
			}
		} else if ((o.equals(this)) && (e.type() == QEvent.Type.Hide)) {
			this.b.finddialog = null;
		}
		return false;
	}

	String getFindText() {
		String text = this.ui.findText.currentText();
		if (!text.isEmpty()) {
			this.b.addToFindHistory(text);
		}
		return text;
	}
	/**此方法是负责查找搜索选项并执行搜索的方法
	 * 不管是向上还是向下搜索最终都会调用此方法
	 * 返回的光标是black.text可以直接使用的光标
	 * 如果没有查找到结果，返回null
	 * @return
	 */
	QTextCursor findnext() {
//		System.out.println("start: "+b.btext.currentFindCursor.position());
		String findstr = getFindText();
		if(findstr.isEmpty())return null;
		
		QTextDocument doc_b = this.b.btext.text.document_b();
		QTextCursor find = null;
		if(!findFlags.isSet(FindFlag.FindBackward))
				find = doc_b.find(findstr, b.btext.currentFindCursor.position(),findFlags);
		else find = doc_b.find(findstr, b.btext.currentFindCursor.position()-1,findFlags);
		//不能将document_b产生的TextCursor对象应用到b.btext.text上，会导致虚拟机崩溃
		QTextCursor tc = b.btext.text.textCursor();
		if (find.position() == -1) {
			return null;
		} else {
			tc.setPosition(find.selectionStart());
			tc.setPosition(find.selectionEnd(), MoveMode.KeepAnchor);
			return tc;
		}
	}
	/**
	 * 设定findFlags
	 * 然后交给findnext方法去具体执行搜索
	 * 此方法负责处理findnext搜索后返回结果并给出提示
	 */
	void next() {
		findFlags.clear(FindFlag.FindBackward);
		QTextCursor findnext = findnext();
		if (findnext != null) {
			this.b.btext.text.setTextCursor(findnext);
			this.b.btext.currentFindCursor = findnext;
		} else {
			int messageBoxWithYesNO = b.getMessageBoxWithYesNO("查找", "没有查找到结果，是否再次从文档开头查找", "是", "否", QMessageBox.Icon.Question, 0);
			if(messageBoxWithYesNO == 1) {
				this.b.btext.currentFindCursor.setPosition(0);
				next();
			}
		}
	}
	/**
	 * 设定findFlags
	 * 然后交给findnext方法去具体执行搜索
	 * 此方法负责处理findnext搜索后返回结果并给出提示
	 */
	void previous() {
		findFlags.set(FindFlag.FindBackward);
		QTextCursor findpre = findnext();
		if (findpre != null) {
			this.b.btext.text.setTextCursor(findpre);
			this.b.btext.currentFindCursor = findpre;
		} else {
			int messageBoxWithYesNO = b.getMessageBoxWithYesNO("查找", "没有查找到结果，是否再次从结尾开始查找", "是", "否", QMessageBox.Icon.Question, 0);
			if(messageBoxWithYesNO == 1) {
				b.btext.currentFindCursor.movePosition(MoveOperation.End);
				previous();
			}
		}
//		findFlags.clear(FindFlag.FindBackward);
	}

	void replaceAll() {
		String findText = getFindText();
		if (!findText.isEmpty()) {
			if(findIn == 0)
			bAction.replaceAll_doc(b,findText, ui.replace_str.text(), b.text,findFlags,false);
			else
				bAction.replaceAll_files(b,findText, ui.replace_str.text(), b.text,findFlags);

			
		}
	}
}
