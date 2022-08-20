package yang.app.qt.black;

import io.qt.widgets.QMessageBox;
import io.qt.widgets.QMessageBox.Icon;
import io.qt.widgets.QMessageBox.StandardButton;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QWidget;

public abstract class messageBox
{
  public messageBox(QWidget qo, String title, String message, String textOfButtonCancel, String textOfButtonOk)
  {
    QMessageBox messagebox = new QMessageBox(qo);
    messagebox.setWindowOpacity(0.9D);
    QPushButton cancel = messagebox.addButton(QMessageBox.StandardButton.Cancel);
    cancel.setText(textOfButtonCancel);
    QPushButton ok = messagebox.addButton(QMessageBox.StandardButton.Ok);
    ok.setText(textOfButtonOk);
    ok.clicked.connect(this, "action()");
    
    messagebox.setText(message);
    messagebox.setWindowTitle(title);
    messagebox.setIcon(QMessageBox.Icon.Question);
    messagebox.show();
  }
  
  public abstract void action();
}
