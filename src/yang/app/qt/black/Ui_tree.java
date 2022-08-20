package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QSize;
import io.qt.widgets.QWidget;

public class Ui_tree
  implements QUiForm<QWidget>
{
  public void setupUi(QWidget tree)
  {
    tree.setObjectName("tree");
    tree.resize(new QSize(400, 300).expandedTo(tree.minimumSizeHint()));
    retranslateUi(tree);
    
//    tree.connectSlotsByName();
  }
  
  void retranslateUi(QWidget tree)
  {
    tree.setWindowTitle(QCoreApplication.translate("tree", "Form", null));
  }
}
