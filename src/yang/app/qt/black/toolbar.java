package yang.app.qt.black;

import io.qt.core.QPointF;
import io.qt.core.Qt;
import io.qt.core.Qt.GlobalColor;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.gui.QGradient;
import io.qt.gui.QGradient.Spread;
import io.qt.gui.QLinearGradient;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPainter.RenderHint;
import io.qt.gui.QPen;
import io.qt.widgets.QToolBar;
import io.qt.widgets.QWidget;

public class toolbar
  extends QToolBar
{
  Ui_toolbar ui = new Ui_toolbar();
  
  public static void main(String[] args) {}
  
  public toolbar(QWidget parent)
  {
    super(parent);
    this.ui.setupUi(this);
    
    setStyleSheet("QToolBar{border-style:none;}");
  }
  
  protected void paintEvent(QPaintEvent e)
  {
    super.paintEvent(e);
    QPainter painter = new QPainter(this);
    painter.setRenderHint(QPainter.RenderHint.Antialiasing, true);
    QLinearGradient linearGradient = new QLinearGradient(new QPointF(0.0D, 0.0D), new QPointF(0.0D, height()));
    QColor a = new QColor(247, 249, 250);
    QColor b = new QColor(232, 236, 240);
    
    linearGradient.setColorAt(0.0D, new QColor(Qt.GlobalColor.white));
    linearGradient.setColorAt(0.4D, a);
    linearGradient.setColorAt(1.0D, b);
    
    painter.setPen(new QPen(new QBrush(linearGradient), 5.0D));
    linearGradient.setSpread(QGradient.Spread.PadSpread);
    painter.setBrush(linearGradient);
    painter.drawRect(rect());
    
    painter.setPen(new QPen(new QBrush(black.borderColor), 2.0D));
    painter.drawLine(0, height(), width(), height());
    
    painter.end();
  }
}
