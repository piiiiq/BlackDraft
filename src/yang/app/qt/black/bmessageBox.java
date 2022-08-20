package yang.app.qt.black;

import io.qt.core.QEvent;
import io.qt.core.QObject;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.widgets.QDialog;
import io.qt.gui.QFont;
import io.qt.widgets.QFrame;
import io.qt.widgets.QFrame.Shape;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QTextEdit;
import io.qt.widgets.QWidget;

public abstract class bmessageBox
  extends QDialog
{
	boolean justHideWhenClose;
  Ui_bmessageBox ui = new Ui_bmessageBox();
  
  public bmessageBox(QWidget parent, String title, String buttonText, String message, boolean readonly)
  {
    super(parent);
    this.ui.setupUi(this);
    setWindowTitle(title);
    this.ui.textEdit.setFrameStyle(QFrame.Shape.NoFrame.value());
    
//    setFixedSize(width(), height());
    this.ui.pushButton.setText(buttonText);
    this.ui.textEdit.setHtml(message.replaceAll("\n", "<br>"));
    this.ui.textEdit.setReadOnly(readonly);
    this.ui.pushButton.pressed.connect(this, "buttonPressed()");
    
    this.ui.textEdit.setFont(parent.font());
    
    installEventFilter(this);
    show();
//    this.ui.textEdit.setGeometry(0, 0, width(), this.ui.textEdit.height());
  }
  public QSize sizeHint() {
	  QRect dg = bAction.desktopGeometry(false);	
//		bmessageBox.setMinimumSize(dg.width()/2, dg.height()/2);
	  return new QSize(dg.width()/2,dg.height()/2);
  }
  
  public boolean eventFilter(QObject o, QEvent e)
  {
	  if(e.type() == QEvent.Type.Close) {
		  if(justHideWhenClose)return true;
	  }else if(e.type() == QEvent.Type.Hide) {
		  setVisible(false);
		  if(justHideWhenClose)return true;
	  }
    return super.eventFilter(o, e);
  }
  
  void buttonPressed()
  {
    buttonPressedAction(this.ui.textEdit.toPlainText());
  }
  
  public void setText(String text)
  {
    this.ui.textEdit.setPlainText(text);
  }
  
  public abstract void buttonPressedAction(String paramString);
}
