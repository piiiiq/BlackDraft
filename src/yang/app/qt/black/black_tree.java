package yang.app.qt.black;

import io.qt.core.QEvent;
import io.qt.core.QEvent.Type;
import io.qt.core.QModelIndex;
import io.qt.core.QObject;
import io.qt.core.QRect;
import io.qt.core.Qt;
import io.qt.core.Qt.AlignmentFlag;
import io.qt.core.Qt.BrushStyle;
import io.qt.core.Qt.GlobalColor;
import io.qt.core.Qt.ScrollBarPolicy;
import io.qt.widgets.QApplication;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QFontMetrics;
import io.qt.widgets.QFrame;
import io.qt.widgets.QFrame.Shape;
import io.qt.gui.QPainter;
import io.qt.gui.QPainter.RenderHint;
import io.qt.gui.QPen;
import io.qt.widgets.QScrollBar;
import io.qt.widgets.QStyleOptionViewItem;
import io.qt.widgets.QTreeWidget;
import io.qt.widgets.QTreeWidgetItem;
import io.qt.widgets.QWidget;

public class black_tree extends QTreeWidget {
	Ui_tree ui = new Ui_tree();
	black b;
	public int UsedToProjectPanelOrKeywordsList;

	public static void main(String[] args) {
		QApplication.initialize(args);

		black_tree testtree = new black_tree();
		testtree.show();

		QApplication.exec();
	}

	public black_tree() {
		this.ui.setupUi(this);
		setHeaderHidden(true);
		this.UsedToProjectPanelOrKeywordsList = 1;
		for (int i = 0; i < 20; i++) {
			QTreeWidgetItem qt = new QTreeWidgetItem(this);
			qt.setText(0, i + "");
		}

		setFrameShape(QFrame.Shape.NoFrame);
	}

	public black_tree(QWidget parent, black b) {
		super(parent);
		this.b = b;
		this.ui.setupUi(this);
		setFrameShape(QFrame.Shape.NoFrame);

		setAutoFillBackground(true);
		verticalScrollBar().setMaximumWidth(17);

		setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
	}

	public void a() {
	}

	protected void drawRow(QPainter painter, QStyleOptionViewItem options, QModelIndex index) {
		super.drawRow(painter, options, index);
		String text = null;
		QTreeWidgetItem item = itemFromIndex(index);
		if ((this.UsedToProjectPanelOrKeywordsList == 0) && (this.b != null)) {
			fileInfo in = black.getFileInfoByQTreeItem(item);
			if ((in.isDir) || (in.isFiles) || (in.isRoot != -1)) {
				int fileInfoCount = this.b.getFileInfoCount(in);
				if (fileInfoCount > 0) {
					text = String.valueOf(fileInfoCount);
				}
			}
			painter.setPen(new QPen(new QBrush(black.borderColor), 1.0D));
			QBrush brush = new QBrush(black.borderColor, Qt.BrushStyle.SolidPattern);
			painter.setBrush(brush);
		} else if (this.UsedToProjectPanelOrKeywordsList > 0) {
			Object data = item.data(3, 3);
			if (data != null) {
				text = (String) data;
			}
			painter.setPen(new QPen(new QBrush(new QColor(Qt.GlobalColor.black)), 1.0D));
			QBrush brush = new QBrush(new QColor(Qt.GlobalColor.black), Qt.BrushStyle.SolidPattern);
			painter.setBrush(brush);
		}
		if (text == null) {
			return;
		}
		if ((this.UsedToProjectPanelOrKeywordsList == 1) && (black.findview == 0)) {
			return;
		}
		QRect rect = visualRect(index);

		QFont font = null;
		if (this.UsedToProjectPanelOrKeywordsList == 0) {
			font = new QFont("微软雅黑", b.font().pointSize()-1);
		} else {
			font = new QFont("微软雅黑");
			if (UsedToProjectPanelOrKeywordsList == 1 && black.text != null) {
				font.setPointSize(black.text.font().pointSize()-1);
			} else {
				font.setPointSize(b.font().pointSize()-1);
			}
			font.setBold(true);
		}
		QFontMetrics fm = new QFontMetrics(font);
		int width = fm.width(text);
		int height = fm.height() - 2;
		int y = (rect.height() - height) / 2 + rect.y();
		QRect r = null;
		r = new QRect(viewport().width() - width - verticalScrollBar().width(), y, width + 15, height);
		painter.setRenderHint(QPainter.RenderHint.Antialiasing, true);
		if (this.UsedToProjectPanelOrKeywordsList == 0) {
			painter.drawRoundRect(r, 70, 70);
			painter.setPen(new QPen(new QColor(Qt.GlobalColor.white), 1.0D));
		} else {
			QBrush foreground = item.foreground(0);
			if (!foreground.style().equals(Qt.BrushStyle.NoBrush)) {
				painter.setPen(new QPen(foreground, 1.0D));
			} else {
				painter.setPen(new QColor(Qt.GlobalColor.darkGreen));
			}
		}
		painter.setFont(font);
		painter.drawText(r, Qt.AlignmentFlag.AlignCenter.value(), text);
	}

	public boolean eventFilter(QObject o, QEvent e) {
		if (e.type() == QEvent.Type.HoverEnter) {
			setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
		} else if (e.type() == QEvent.Type.HoverLeave) {
			setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
		}
		return false;
	}
}
