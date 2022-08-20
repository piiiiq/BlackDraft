package yang.app.qt.black;

import io.qt.core.QPoint;
import io.qt.core.QSize;
import io.qt.widgets.QAction;
import io.qt.gui.QKeyEvent;
import io.qt.widgets.QMenu;
import io.qt.gui.QMouseEvent;

public class Menu
  extends QMenu
{
  protected void mousePressEvent(QMouseEvent e)
  {
    super.mousePressEvent(e);
    QPoint pos = e.pos();
    QSize size = size();
    if ((pos.x() > 0) && (pos.x() < size.width()) && (pos.y() > 0) && (pos.y() < size.height()))
    {
      QAction actionAt = actionAt(e.pos());
      if ((actionAt != null) && (actionAt.isEnabled())) {
        this.triggered.emit(actionAt);
      }
    }
  }
  
  protected void keyPressEvent(QKeyEvent e)
  {
    if (e.key() == 16777220)
    {
      QAction menuAction = menuAction();
      QMenu menu = menuAction.menu();
      if (menu != null)
      {
        QAction act = menu.activeAction();
        if ((act != null) && (act.isEnabled())) {
          this.triggered.emit(act);
        }
      }
    }
    else
    {
      super.keyPressEvent(e);
    }
  }
}
