package yang.app.qt.black;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.core.Qt;
import io.qt.core.Qt.ArrowType;
import io.qt.core.Qt.ContextMenuPolicy;
import io.qt.widgets.QButtonGroup;
import io.qt.widgets.QLineEdit;
import io.qt.widgets.QSizePolicy;
import io.qt.widgets.QSizePolicy.Policy;
import io.qt.widgets.QToolButton;
import io.qt.widgets.QWidget;

public class Ui_textTitle
  implements QUiForm<QWidget>
{
  public QLineEdit lineEdit;
  public QToolButton toolButton;
  public QToolButton prev;
  public QToolButton next;
  
  public void setupUi(QWidget textTitle)
  {
    textTitle.setObjectName("textTitle");
    textTitle.resize(new QSize(522, 42).expandedTo(textTitle.minimumSizeHint()));
    QSizePolicy sizePolicy = new QSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Fixed);
    sizePolicy.setHorizontalStretch((byte)0);
    sizePolicy.setVerticalStretch((byte)27);
    sizePolicy.setHeightForWidth(textTitle.sizePolicy().hasHeightForWidth());
    textTitle.setSizePolicy(sizePolicy);
    textTitle.setMaximumSize(new QSize(16777215, 42));
    this.lineEdit = new QLineEdit(textTitle);
    this.lineEdit.setObjectName("lineEdit");
    this.lineEdit.setGeometry(new QRect(38, 9, 301, 20));
    this.lineEdit.setContextMenuPolicy(Qt.ContextMenuPolicy.DefaultContextMenu);
    this.lineEdit.setFrame(false);
    this.toolButton = new QToolButton(textTitle);
    this.toolButton.setObjectName("toolButton");
    this.toolButton.setGeometry(new QRect(9, 9, 23, 22));
    this.toolButton.setMaximumSize(new QSize(16777215, 27));
    this.toolButton.setArrowType(Qt.ArrowType.NoArrow);
    this.prev = new QToolButton(textTitle);
    QButtonGroup buttonGroup = new QButtonGroup(textTitle);
    buttonGroup.addButton(this.prev);
    this.prev.setObjectName("prev");
    this.prev.setGeometry(new QRect(460, 10, 22, 23));
    QSizePolicy sizePolicy1 = new QSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
    sizePolicy1.setHorizontalStretch((byte)0);
    sizePolicy1.setVerticalStretch((byte)0);
    sizePolicy1.setHeightForWidth(this.prev.sizePolicy().hasHeightForWidth());
    this.prev.setSizePolicy(sizePolicy1);
    this.prev.setMaximumSize(new QSize(16777215, 27));
    this.prev.setIconSize(new QSize(22, 23));
    this.prev.setAutoRaise(true);
    this.prev.setArrowType(Qt.ArrowType.NoArrow);
    this.next = new QToolButton(textTitle);
    buttonGroup.addButton(this.next);
    this.next.setObjectName("next");
    this.next.setGeometry(new QRect(490, 10, 22, 23));
    QSizePolicy sizePolicy2 = new QSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
    sizePolicy2.setHorizontalStretch((byte)0);
    sizePolicy2.setVerticalStretch((byte)0);
    sizePolicy2.setHeightForWidth(this.next.sizePolicy().hasHeightForWidth());
    this.next.setSizePolicy(sizePolicy2);
    this.next.setMaximumSize(new QSize(16777215, 27));
    this.next.setIconSize(new QSize(22, 23));
    this.next.setAutoRaise(true);
    this.next.setArrowType(Qt.ArrowType.NoArrow);
    retranslateUi(textTitle);
    
//    textTitle.connectSlotsByName();
  }
  
  void retranslateUi(QWidget textTitle)
  {
    textTitle.setWindowTitle(QCoreApplication.translate("textTitle", "Form", null));
    this.lineEdit.setStyleSheet("");
    this.toolButton.setStyleSheet("");
    this.toolButton.setText("");
    this.prev.setStyleSheet("");
    this.prev.setText("");
    this.next.setStyleSheet("");
    this.next.setText("");
  }
}
