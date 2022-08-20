package yang.app.qt.black;

import io.qt.core.QSize;
import io.qt.widgets.QApplication;
import io.qt.gui.QBrush;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPainter.RenderHint;
import io.qt.gui.QPen;
import io.qt.widgets.QStatusBar;
import io.qt.widgets.QWidget;

public class statusbar
  extends QStatusBar
{
  Ui_statusbar ui = new Ui_statusbar();
  black b;
  
  public static void main(String[] args)
  {
    QApplication.initialize(args);
    
    statusbar teststatusbar = new statusbar();
    teststatusbar.show();
    
	QApplication.exec();
  }
  
  public statusbar()
  {
    this.ui.setupUi(this);
  }
  
  public statusbar(QWidget parent)
  {
    super(parent);
    this.ui.setupUi(this);
    this.b = ((black)parent);
  }
  
  public QSize sizeHint()
  {
    return new QSize(width(), 20);
  }
  
  protected void paintEvent(QPaintEvent e)
  {
    super.paintEvent(e);
    QPainter painter = new QPainter(this);
    
    painter.setRenderHint(QPainter.RenderHint.Antialiasing, true);
    
    painter.setPen(new QPen(new QBrush(black.borderColor), 2.0D));
    
    painter.drawLine(0, 0, width(), 0);
    
    painter.end();
  }
}
