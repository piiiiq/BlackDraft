package yang.app.qt.black;

import io.qt.core.QPointF;
import io.qt.core.QSize;
import io.qt.core.Qt;
import io.qt.core.Qt.AlignmentFlag;
import io.qt.core.Qt.GlobalColor;
import io.qt.widgets.QApplication;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.widgets.QFrame;
import io.qt.widgets.QFrame.Shape;
import io.qt.gui.QGradient;
import io.qt.gui.QGradient.Spread;
import io.qt.widgets.QLabel;
import io.qt.gui.QLinearGradient;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPainter.RenderHint;
import io.qt.gui.QPalette;
import io.qt.gui.QPen;
import io.qt.widgets.QWidget;

public class treeTitle
  extends QLabel
{
  Ui_treeTitle ui = new Ui_treeTitle();
  
  public treeTitle(QWidget w)
  {
    super(w);
    this.ui.setupUi(this);
    
    setWindowTitle("文件篓");
    setFrameShape(QFrame.Shape.NoFrame);
    setStyleSheet("border-style:none;");
    setFontSize();
  }
  
  public QSize sizeHint()
  {
    return new QSize(200, font().pointSize()+20);
  }
  public void setFontSize() {
	  QFont qFont = new QFont("微软雅黑");
	  qFont.setPointSize(QApplication.font().pointSize()+1);
	  qFont.setBold(true);
	  setAlignment(AlignmentFlag.AlignCenter);
	  setFont(qFont);
//	  QPalette p = palette();
//	  p.setColor(foregroundRole(), new QColor(Qt.GlobalColor.black));
	  setText(windowTitle());
  }
  
  protected void paintEvent(QPaintEvent e)
  {
    QPainter painter = new QPainter(this);
    painter.setRenderHint(QPainter.RenderHint.Antialiasing, true);
    QLinearGradient linearGradient = new QLinearGradient(new QPointF(0.0D, 0.0D), new QPointF(0.0D, height()));
    QColor a = new QColor(239, 241, 244);
    QColor b = new QColor(230, 234, 238);
    
    linearGradient.setColorAt(0.0D, new QColor(Qt.GlobalColor.white));
    
    linearGradient.setColorAt(0.5D, b);
    
    linearGradient.setColorAt(1.0D, new QColor(Qt.GlobalColor.white));
    painter.setPen(new QPen(new QBrush(linearGradient), 1.0D));
    linearGradient.setSpread(QGradient.Spread.PadSpread);
    painter.setBrush(linearGradient);
    painter.drawRect(rect());
    
    painter.setPen(new QPen(new QBrush(black.borderColor), 2.0D));
    painter.drawLine(0, height(), width(), height());
    
    painter.end();
    super.paintEvent(e);

  }
}
