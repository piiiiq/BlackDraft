package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QSize;
import io.qt.widgets.QDialog;
import io.qt.widgets.QPlainTextEdit;
import io.qt.widgets.QVBoxLayout;

public class Ui_showMoreText
  implements QUiForm<QDialog>
{
  public QVBoxLayout verticalLayout;
  public QPlainTextEdit plainTextEdit;
  
  public void setupUi(QDialog showMoreText)
  {
    showMoreText.setObjectName("showMoreText");
    showMoreText.resize(new QSize(400, 300).expandedTo(showMoreText.minimumSizeHint()));
    this.verticalLayout = new QVBoxLayout(showMoreText);
    this.verticalLayout.setObjectName("verticalLayout");
    this.plainTextEdit = new QPlainTextEdit(showMoreText);
    this.plainTextEdit.setObjectName("plainTextEdit");
    
    this.verticalLayout.addWidget(this.plainTextEdit);
    
    retranslateUi(showMoreText);
    
//    showMoreText.connectSlotsByName();
  }
  
  void retranslateUi(QDialog showMoreText)
  {
    showMoreText.setWindowTitle(QCoreApplication.translate("showMoreText", "Dialog", null));
  }
}
