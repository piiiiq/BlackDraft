package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.widgets.QAction;
import io.qt.widgets.QComboBox;
import io.qt.widgets.QMainWindow;
import io.qt.widgets.QMenu;
import io.qt.widgets.QMenuBar;
import io.qt.widgets.QStatusBar;
import io.qt.widgets.QVBoxLayout;
import io.qt.widgets.QWidget;

public class Ui_MainWindow
  implements QUiForm<QMainWindow>
{
  public QAction reLoad;
  public QWidget centralwidget;
  public QVBoxLayout verticalLayout;
  public QComboBox addres;
  public QMenuBar menubar;
  public QMenu menu;
  public QStatusBar statusbar;
  
  public void setupUi(QMainWindow MainWindow)
  {
    MainWindow.setObjectName("MainWindow");
    MainWindow.resize(new QSize(800, 600).expandedTo(MainWindow.minimumSizeHint()));
    this.reLoad = new QAction(MainWindow);
    this.reLoad.setObjectName("reLoad");
    this.centralwidget = new QWidget(MainWindow);
    this.centralwidget.setObjectName("centralwidget");
    this.verticalLayout = new QVBoxLayout(this.centralwidget);
    this.verticalLayout.setObjectName("verticalLayout");
    this.addres = new QComboBox(this.centralwidget);
    this.addres.setObjectName("addres");
    
    this.verticalLayout.addWidget(this.addres);
    
 
    
    
    MainWindow.setCentralWidget(this.centralwidget);
    this.menubar = new QMenuBar(MainWindow);
    this.menubar.setObjectName("menubar");
    this.menubar.setGeometry(new QRect(0, 0, 800, 23));
    this.menu = new QMenu(this.menubar);
    this.menu.setObjectName("menu");
    MainWindow.setMenuBar(this.menubar);
    this.statusbar = new QStatusBar(MainWindow);
    this.statusbar.setObjectName("statusbar");
    MainWindow.setStatusBar(this.statusbar);
    
    this.menubar.addAction(this.menu.menuAction());
    this.menu.addAction(this.reLoad);
    retranslateUi(MainWindow);
    
//    MainWindow.connectSlotsByName();
  }
  
  void retranslateUi(QMainWindow MainWindow)
  {
    MainWindow.setWindowTitle(QCoreApplication.translate("MainWindow", "MainWindow", null));
    this.reLoad.setText(QCoreApplication.translate("MainWindow", "重新载入", null));
    this.addres.clear();
    this.addres.addItem(QCoreApplication.translate("MainWindow", "http://cn.bing.com", null));
    this.addres.addItem(QCoreApplication.translate("MainWindow", "http://www.baidu.com", null));
    this.addres.addItem(QCoreApplication.translate("MainWindow", "https://www.google.de", null));
    this.menu.setTitle(QCoreApplication.translate("MainWindow", "网页", null));
  }
}
