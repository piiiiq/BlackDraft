package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QSize;
import io.qt.widgets.QWidget;

public class Ui_treeTitle
  implements QUiForm<QWidget>
{
  public void setupUi(QWidget treeTitle)
  {
    treeTitle.setObjectName("treeTitle");
    treeTitle.resize(new QSize(400, 300).expandedTo(treeTitle.minimumSizeHint()));
    treeTitle.setStyleSheet("QFrame {\nborder: 1px solid green;\n\n}");
    
    retranslateUi(treeTitle);
    
//    treeTitle.connectSlotsByName();
  }
  
  void retranslateUi(QWidget treeTitle)
  {
    treeTitle.setWindowTitle(QCoreApplication.translate("treeTitle", "Form", null));
  }
}
