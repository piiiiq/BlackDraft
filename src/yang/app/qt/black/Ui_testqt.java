package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.widgets.QDialog;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QTreeView;

public class Ui_testqt
  implements QUiForm<QDialog>
{
  public QTreeView treeView;
  public QPushButton pushButton;
  
  public void setupUi(QDialog testqt)
  {
    testqt.setObjectName("testqt");
    testqt.resize(new QSize(400, 300).expandedTo(testqt.minimumSizeHint()));
    this.treeView = new QTreeView(testqt);
    this.treeView.setObjectName("treeView");
    this.treeView.setGeometry(new QRect(60, 40, 256, 192));
    this.pushButton = new QPushButton(testqt);
    this.pushButton.setObjectName("pushButton");
    this.pushButton.setGeometry(new QRect(310, 260, 75, 24));
    retranslateUi(testqt);
    
//    testqt.connectSlotsByName();
  }
  
  void retranslateUi(QDialog testqt)
  {
    testqt.setWindowTitle(QCoreApplication.translate("testqt", "Dialog", null));
    this.pushButton.setText(QCoreApplication.translate("testqt", "PushButton", null));
  }
}
