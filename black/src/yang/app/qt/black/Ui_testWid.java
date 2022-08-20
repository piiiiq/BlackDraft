package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QSize;
import io.qt.widgets.QWidget;

public class Ui_testWid
  implements QUiForm<QWidget>
{
  public void setupUi(QWidget testWid)
  {
    testWid.setObjectName("testWid");
    testWid.resize(new QSize(400, 300).expandedTo(testWid.minimumSizeHint()));
    retranslateUi(testWid);
    
//    testWid.connectSlotsByName();
  }
  
  void retranslateUi(QWidget testWid)
  {
    testWid.setWindowTitle(QCoreApplication.translate("testWid", "Form", null));
  }
}
