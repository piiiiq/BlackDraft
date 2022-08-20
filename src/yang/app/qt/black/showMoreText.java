package yang.app.qt.black;

import io.qt.core.Qt;
import io.qt.core.Qt.GlobalColor;
import io.qt.core.Qt.WindowFlags;
import io.qt.core.Qt.WindowType;
import io.qt.widgets.QApplication;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.widgets.QDialog;
import io.qt.gui.QFont;
import io.qt.widgets.QFrame;
import io.qt.widgets.QFrame.Shape;
import io.qt.gui.QPalette;
import io.qt.gui.QPalette.ColorGroup;
import io.qt.gui.QPalette.ColorRole;
import io.qt.widgets.QPlainTextEdit;
import io.qt.gui.QTextCharFormat;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveMode;
import io.qt.gui.QTextCursor.SelectionType;
import io.qt.gui.QTextDocument;
import io.qt.widgets.QVBoxLayout;
import io.qt.widgets.QWidget;

public class showMoreText
  extends QDialog
{
  Ui_showMoreText ui = new Ui_showMoreText();
  public QPlainTextEdit plainTextEdit;
  
  public static void main(String[] args)
  {
    QApplication.initialize(args);
    
    showMoreText testshowMoreText = new showMoreText();
    testshowMoreText.show();
  }
  
  public showMoreText()
  {
    this.ui.setupUi(this);
  }
  
  public void mark(String str)
  {
    QTextCursor tc = this.plainTextEdit.textCursor();
    tc.select(QTextCursor.SelectionType.Document);
    QTextCharFormat cff = tc.charFormat();
    cff.clearBackground();
    tc.setCharFormat(cff);
    this.plainTextEdit.setTextCursor(tc);
    if (str.indexOf("·") != -1)
    {
      String[] name = black.checkName(str);
      QTextDocument doc = new QTextDocument(this.plainTextEdit.toPlainText());
      QTextCursor find = doc.find(name[0]);
      while (find.position() != -1)
      {
        QTextCursor tcc = this.plainTextEdit.textCursor();
        tcc.setPosition(find.selectionStart());
        tcc.setPosition(find.selectionEnd(), QTextCursor.MoveMode.KeepAnchor);
        QTextCharFormat cf = tcc.charFormat();
        cf.setBackground(new QBrush(new QColor(Qt.GlobalColor.green)));
        tcc.setCharFormat(cf);
        tcc.clearSelection();
        this.plainTextEdit.setTextCursor(tcc);
        find = doc.find(name[0], find);
      }
      find.setPosition(0);
      find = doc.find(name[1]);
      while (find.position() != -1)
      {
        QTextCursor tcc = this.plainTextEdit.textCursor();
        tcc.setPosition(find.selectionStart());
        tcc.setPosition(find.selectionEnd(), QTextCursor.MoveMode.KeepAnchor);
        QTextCharFormat cf = tcc.charFormat();
        cf.setBackground(new QBrush(new QColor(Qt.GlobalColor.green)));
        tcc.setCharFormat(cf);
        tcc.clearSelection();
        this.plainTextEdit.setTextCursor(tcc);
        find = doc.find(name[1], find);
      }
    }
    QTextDocument doc = new QTextDocument(this.plainTextEdit.toPlainText());
    QTextCursor find = doc.find(str);
    while (find.position() != -1)
    {
      QTextCursor tcc = this.plainTextEdit.textCursor();
      tcc.setPosition(find.selectionStart());
      tcc.setPosition(find.selectionEnd(), QTextCursor.MoveMode.KeepAnchor);
      QTextCharFormat cf = tcc.charFormat();
      cf.setBackground(new QBrush(new QColor(Qt.GlobalColor.green)));
      tcc.setCharFormat(cf);
      tcc.clearSelection();
      this.plainTextEdit.setTextCursor(tcc);
      find = doc.find(str, find);
    }
  }
  
  public showMoreText(QWidget parent)
  {
    super(parent);
    Qt.WindowFlags windowFlags = new Qt.WindowFlags(new Qt.WindowType[] { Qt.WindowType.CustomizeWindowHint, Qt.WindowType.Tool });
    setWindowFlags(windowFlags);
    this.ui.setupUi(this);
    this.ui.plainTextEdit.setFrameShape(QFrame.Shape.NoFrame);
    this.ui.verticalLayout.setMargin(0);
    this.plainTextEdit = this.ui.plainTextEdit;
    QPalette qPalette = this.plainTextEdit.palette();
    qPalette.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Base, new QColor(251, 254, 126));
    qPalette.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.Highlight, new QColor(191, 197, 1));
    qPalette.setColor(QPalette.ColorGroup.All, QPalette.ColorRole.HighlightedText, new QColor(Qt.GlobalColor.black));
    this.plainTextEdit.setPalette(qPalette);
    this.plainTextEdit.setReadOnly(true);
    this.plainTextEdit.setFont(new QFont("微软雅黑", 10));
    this.plainTextEdit.setBackgroundRole(QPalette.ColorRole.ToolTipText);
  }
}
