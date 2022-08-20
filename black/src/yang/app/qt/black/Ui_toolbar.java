package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QSize;
import io.qt.widgets.QWidget;

public class Ui_toolbar
  implements QUiForm<QWidget>
{
  public void setupUi(QWidget toolbar)
  {
    toolbar.setObjectName("toolbar");
    toolbar.resize(new QSize(400, 300).expandedTo(toolbar.minimumSizeHint()));
    retranslateUi(toolbar);
    
//    toolbar.connectSlotsByName();
  }
  
  void retranslateUi(QWidget toolbar)
  {
    toolbar.setWindowTitle(QCoreApplication.translate("toolbar", "Form", null));
  }
}
