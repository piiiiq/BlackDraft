package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.widgets.QCheckBox;
import io.qt.widgets.QComboBox;
import io.qt.widgets.QDialog;
import io.qt.widgets.QGroupBox;
import io.qt.widgets.QLabel;
import io.qt.widgets.QLineEdit;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QRadioButton;

public class Ui_finddialog
  implements QUiForm<QDialog>
{
  public QLabel label;
  public QLineEdit replace_str;
  public QLabel label_2;
  public QGroupBox groupBox_3;
  public QCheckBox case_2;
  public QComboBox findOptions;
  public QPushButton next;
  public QPushButton previous;
  public QPushButton replace_find;
  public QPushButton replace;
  public QPushButton replaceall;
  public QGroupBox groupBox_4;
  public QRadioButton document;
  public QRadioButton files;
  public QComboBox findText;
  
  public void setupUi(QDialog finddialog)
  {
    finddialog.setObjectName("finddialog");
    finddialog.resize(new QSize(493, 174).expandedTo(finddialog.minimumSizeHint()));
    this.label = new QLabel(finddialog);
    this.label.setObjectName("label");
    this.label.setGeometry(new QRect(10, 10, 41, 20));
    this.replace_str = new QLineEdit(finddialog);
    this.replace_str.setObjectName("replace_str");
    this.replace_str.setGeometry(new QRect(60, 40, 321, 20));
    this.label_2 = new QLabel(finddialog);
    this.label_2.setObjectName("label_2");
    this.label_2.setGeometry(new QRect(10, 40, 41, 20));
    this.groupBox_3 = new QGroupBox(finddialog);
    this.groupBox_3.setObjectName("groupBox_3");
    this.groupBox_3.setGeometry(new QRect(260, 70, 121, 91));
    this.case_2 = new QCheckBox(this.groupBox_3);
    this.case_2.setObjectName("case_2");
//    this.case_2.setEnabled(false);
    this.case_2.setGeometry(new QRect(10, 30, 101, 20));
    this.findOptions = new QComboBox(this.groupBox_3);
    this.findOptions.setObjectName("findOptions");
    this.findOptions.setEnabled(false);
    this.findOptions.setGeometry(new QRect(10, 60, 101, 22));
    this.next = new QPushButton(finddialog);
    this.next.setObjectName("next");
    this.next.setGeometry(new QRect(390, 10, 91, 24));
    this.previous = new QPushButton(finddialog);
    this.previous.setObjectName("previous");
    this.previous.setGeometry(new QRect(390, 40, 91, 24));
    this.replace_find = new QPushButton(finddialog);
    this.replace_find.setObjectName("replace_find");
    this.replace_find.setEnabled(true);
    this.replace_find.setGeometry(new QRect(390, 110, 91, 24));
    this.replace = new QPushButton(finddialog);
    this.replace.setObjectName("replace");
    this.replace.setEnabled(true);
    this.replace.setGeometry(new QRect(390, 80, 91, 24));
    this.replaceall = new QPushButton(finddialog);
    this.replaceall.setObjectName("replaceall");
    this.replaceall.setGeometry(new QRect(390, 140, 91, 24));
    this.groupBox_4 = new QGroupBox(finddialog);
    this.groupBox_4.setObjectName("groupBox_4");
    this.groupBox_4.setGeometry(new QRect(130, 70, 121, 91));
    this.document = new QRadioButton(this.groupBox_4);
    this.document.setObjectName("document");
//    this.document.setEnabled(false);
    this.document.setGeometry(new QRect(10, 30, 101, 20));
    this.files = new QRadioButton(this.groupBox_4);
    this.files.setObjectName("selectionText");
//    this.selectionText.setEnabled(false);
    this.files.setGeometry(new QRect(10, 60, 101, 19));
    this.findText = new QComboBox(finddialog);
    this.findText.setObjectName("findText");
    this.findText.setGeometry(new QRect(60, 10, 321, 22));
    this.findText.setEditable(true);
    retranslateUi(finddialog);
    
//    finddialog.connectSlotsByName();
  }
  
  void retranslateUi(QDialog finddialog)
  {
    finddialog.setWindowTitle(QCoreApplication.translate("finddialog", "Dialog", null));
    this.label.setText(QCoreApplication.translate("finddialog", "查找:", null));
    this.label_2.setText(QCoreApplication.translate("finddialog", "替换:", null));
    this.groupBox_3.setTitle(QCoreApplication.translate("finddialog", "查找选项", null));
    this.case_2.setText(QCoreApplication.translate("finddialog", "忽略大小写", null));
    this.findOptions.clear();
    this.findOptions.addItem(QCoreApplication.translate("finddialog", "包含", null));
    this.findOptions.addItem(QCoreApplication.translate("finddialog", "全字匹配", null));
    this.findOptions.addItem(QCoreApplication.translate("finddialog", "以此文本开始", null));
    this.findOptions.addItem(QCoreApplication.translate("finddialog", "以此文本结束", null));
    this.next.setText(QCoreApplication.translate("finddialog", "下一个", null));
    this.previous.setText(QCoreApplication.translate("finddialog", "前一个", null));
    this.replace_find.setText(QCoreApplication.translate("finddialog", "替换并查找", null));
    this.replace.setText(QCoreApplication.translate("finddialog", "替换", null));
    this.replaceall.setText(QCoreApplication.translate("finddialog", "替换全部", null));
    this.groupBox_4.setTitle(QCoreApplication.translate("finddialog", "替换区域", null));
    this.document.setText(QCoreApplication.translate("finddialog", "当前文档", null));
    this.files.setText(QCoreApplication.translate("finddialog", "当前文件集", null));
  }
}
