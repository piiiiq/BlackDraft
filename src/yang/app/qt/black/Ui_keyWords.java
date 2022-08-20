package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QSize;
import io.qt.widgets.QHBoxLayout;
import io.qt.widgets.QMainWindow;
import io.qt.widgets.QWidget;

public class Ui_keyWords
  implements QUiForm<QMainWindow>
{
  public QWidget centralwidget;
  public QHBoxLayout horizontalLayout;
  
  public void setupUi(QMainWindow keyWords)
  {
    keyWords.setObjectName("keyWords");
    keyWords.resize(new QSize(447, 145).expandedTo(keyWords.minimumSizeHint()));
    this.centralwidget = new QWidget(keyWords);
    this.centralwidget.setObjectName("centralwidget");
    this.horizontalLayout = new QHBoxLayout(this.centralwidget);
    this.horizontalLayout.setObjectName("horizontalLayout");
    keyWords.setCentralWidget(this.centralwidget);
    retranslateUi(keyWords);
    
//    keyWords.connectSlotsByName();
  }
  
  void retranslateUi(QMainWindow keyWords)
  {
    keyWords.setWindowTitle(QCoreApplication.translate("keyWords", "MainWindow", null));
  }
}
