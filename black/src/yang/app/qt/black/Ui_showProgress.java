package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.core.Qt;
import io.qt.core.Qt.AlignmentFlag;
import io.qt.widgets.QDialog;
import io.qt.widgets.QLabel;
import io.qt.widgets.QProgressBar;
import io.qt.widgets.QPushButton;

public class Ui_showProgress
  implements QUiForm<QDialog>
{
  public QProgressBar progress;
  public QLabel showMessage;
  public QPushButton OK;
  
  public void setupUi(QDialog showProgress)
  {
    showProgress.setObjectName("showProgress");
    showProgress.resize(new QSize(491, 113).expandedTo(showProgress.minimumSizeHint()));
    this.progress = new QProgressBar(showProgress);
    this.progress.setObjectName("progress");
    this.progress.setGeometry(new QRect(30, 20, 421, 16));
    this.progress.setValue(0);
    this.progress.setTextVisible(false);
    this.showMessage = new QLabel(showProgress);
    this.showMessage.setObjectName("showMessage");
    this.showMessage.setGeometry(new QRect(10, 40, 471, 31));
    this.showMessage.setAlignment( Qt.AlignmentFlag.AlignHCenter, Qt.AlignmentFlag.AlignTop);
    this.showMessage.setWordWrap(true);
    this.OK = new QPushButton(showProgress);
    this.OK.setObjectName("OK");
    this.OK.setGeometry(new QRect(410, 80, 75, 24));
    retranslateUi(showProgress);
    
//    showProgress.connectSlotsByName();
  }
  
  void retranslateUi(QDialog showProgress)
  {
    showProgress.setWindowTitle(QCoreApplication.translate("showProgress", "Dialog", null));
    this.showMessage.setText("");
    this.OK.setText(QCoreApplication.translate("showProgress", "确定", null));
  }
}
