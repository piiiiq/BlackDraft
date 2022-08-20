package yang.app.qt.black;

import io.qt.core.QEvent;
import io.qt.core.QEvent.Type;
import io.qt.core.QObject;
import io.qt.core.QPoint;
import io.qt.core.QPointF;
import io.qt.core.QSize;
import io.qt.core.Qt;
import io.qt.core.Qt.GlobalColor;
import io.qt.widgets.QAction;
import io.qt.widgets.QApplication;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QGradient;
import io.qt.gui.QGradient.Spread;
import io.qt.gui.QIcon;
import io.qt.widgets.QLineEdit;
import io.qt.gui.QLinearGradient;
import io.qt.widgets.QMenu;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPainter.RenderHint;
import io.qt.gui.QPen;
import io.qt.widgets.QToolBar;
import io.qt.widgets.QToolButton;
import io.qt.widgets.QToolButton.ToolButtonPopupMode;
import io.qt.widgets.QWidget;

public class textTitle extends QToolBar {
	Ui_textTitle ui = new Ui_textTitle();
	black b;
	QIcon icon;

	public static void main(String[] args) {
		QApplication.initialize(args);

		textTitle testtextTitle = new textTitle();
		testtextTitle.show();

		QApplication.exec();
	}

	public textTitle() {
		this.ui.setupUi(this);
	}

	public void clearTextTitle() {
		this.ui.lineEdit.clear();
		this.ui.toolButton.setIcon(null);
		this.ui.toolButton.setText("");
	}

	/**
	 * 设置编辑器标题栏的字体大小 此方法会保证编辑器标题栏的高度与文件篓标题栏高度相同 并且能够自动适应普通模式和高分屏模式
	 */
	public void setFontSize() {
		QFont qFont = QApplication.font();
		qFont.setBold(true);
		qFont.setPointSize(qFont.pointSize() + 1);
		setFont(qFont);
		setMaximumHeight(qFont.pointSize() + 20);
		this.ui.lineEdit.setFont(qFont);
	}

	public textTitle(QWidget parent) {
		super(parent);
		this.ui.setupUi(this);
		this.ui.toolButton.pressed.connect(this, "t()");
		this.b = ((black) parent);
		setWindowTitle("");

		this.ui.lineEdit.returnPressed.connect(this, "rename()");
		addWidget(this.ui.toolButton);
		addWidget(this.ui.lineEdit);
		this.ui.toolButton.setPopupMode(QToolButton.ToolButtonPopupMode.MenuButtonPopup);
		QMenu m = new QMenu(this.ui.toolButton);
		QAction a = new QAction(m);
		a.setText("写作视图");
		a.triggered.connect(this, "a()");
		m.addAction(a);
		this.ui.toolButton.setMenu(m);
		this.ui.prev.setIcon(this.b.ico_prevFile);
		this.ui.prev.clicked.connect(this.b, "moveToPreviousOpenedFile()");
		this.ui.next.setIcon(this.b.ico_nextFile);
		this.ui.next.clicked.connect(this.b, "moveToNextOpenedFile()");
		installEventFilter(this);
		this.ui.lineEdit.installEventFilter(this);
		setStyleSheet("QToolBar{border-style:none;}QLineEdit{background: transparent;}");
		setFontSize();
	}

	void a() {
		this.b.writingView(Boolean.valueOf(true));
	}

	public boolean eventFilter(QObject o, QEvent e) {
		if (e.type() == QEvent.Type.Resize) {
			int xprev = width() - 5 - this.ui.next.width() - this.ui.prev.width();
			int y = (height() - this.ui.prev.height()) / 2;
			int xnext = width() - 5 - this.ui.next.width();
			this.ui.lineEdit.setGeometry(this.ui.lineEdit.x(), this.ui.lineEdit.y(), xprev - this.ui.lineEdit.x(),
					this.ui.lineEdit.height());
			this.ui.prev.setGeometry(xprev, y, this.ui.prev.width(), this.ui.prev.height());
			this.ui.next.setGeometry(xnext, y, this.ui.next.width(), this.ui.next.height());
		}
		return super.eventFilter(o, e);
	}

	public void menu(QPoint pos) {
	}

	public void t() {
		boolean checked = !this.b.ui.hideFilePanel.isChecked();
		this.b.ui.hideFilePanel.setChecked(checked);
		this.b.hideFilesTree();
	}

	public void rename() {
		if (!this.ui.lineEdit.text().equals("")) {
			this.b.setTextAsFileTitle(this.ui.lineEdit.text());
		}
	}

	public void setText(String text) {
		this.ui.lineEdit.setText(text);
		this.ui.toolButton.setText(text);
	}

	public void setFileIcon(QIcon icon) {
		this.ui.toolButton.setIcon(icon);
	}

	public QSize sizeHint() {
		return new QSize(width(), 27);
	}

	protected void paintEvent(QPaintEvent e) {
		super.paintEvent(e);
		QPainter painter = new QPainter(this);
		painter.setRenderHint(QPainter.RenderHint.Antialiasing, true);
		QLinearGradient linearGradient = new QLinearGradient(new QPointF(0.0D, 0.0D), new QPointF(0.0D, height()));
		QColor a = new QColor(247, 249, 250);
		QColor b = new QColor(232, 236, 240);

		linearGradient.setColorAt(0.0D, new QColor(Qt.GlobalColor.white));
		linearGradient.setColorAt(0.4D, a);
		linearGradient.setColorAt(1.0D, b);

		painter.setPen(new QPen(new QBrush(linearGradient), 1.0D));
		linearGradient.setSpread(QGradient.Spread.PadSpread);
		painter.setBrush(linearGradient);
		painter.drawRect(rect());
		painter.setPen(new QPen(new QBrush(black.borderColor), 2.0D));
		painter.drawLine(0, height(), width(), height());

		painter.end();
	}
}
