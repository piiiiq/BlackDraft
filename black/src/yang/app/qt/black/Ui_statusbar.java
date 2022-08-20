package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QSize;
import io.qt.widgets.QWidget;

public class Ui_statusbar
  implements QUiForm<QWidget>
{
  public void setupUi(QWidget statusbar)
  {
    statusbar.setObjectName("statusbar");
    statusbar.resize(new QSize(400, 300).expandedTo(statusbar.minimumSizeHint()));
    retranslateUi(statusbar);
    
//    statusbar.connectSlotsByName();
  }
  
  void retranslateUi(QWidget statusbar)
  {
    statusbar.setWindowTitle(QCoreApplication.translate("statusbar", "Form", null));
  }
}
