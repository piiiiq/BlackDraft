package yang.app.qt.black;

import javax.swing.GroupLayout.Alignment;

import io.qt.QUiForm;
import io.qt.core.QCoreApplication;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.core.Qt;
import io.qt.widgets.QBoxLayout;
import io.qt.widgets.QDialog;
import io.qt.widgets.QFrame;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QSizePolicy;
import io.qt.widgets.QSizePolicy.Policy;
import io.qt.widgets.QTextEdit;
import io.qt.widgets.QBoxLayout.Direction;

public class Ui_bmessageBox
  implements QUiForm<QDialog>
{
  public QPushButton pushButton;
  public QTextEdit textEdit;
  
  public void setupUi(QDialog bmessageBox)
  {
    bmessageBox.setObjectName("bmessageBox");
//    bmessageBox.resize(new QSize(394, 438).expandedTo(bmessageBox.minimumSizeHint()));
    this.pushButton = new QPushButton(bmessageBox);
    this.pushButton.setObjectName("pushButton");
//    this.pushButton.setGeometry(new QRect(310, 400, 75, 24));
    this.textEdit = new QTextEdit(bmessageBox);
    this.textEdit.setObjectName("textEdit");
//    this.textEdit.setGeometry(new QRect(0, 0, 411, 391));
    this.textEdit.setStyleSheet("");
    this.textEdit.setLineWidth(0);
    this.textEdit.setUndoRedoEnabled(false);
    retranslateUi(bmessageBox);
    
//    bmessageBox.connectSlotsByName();
	QBoxLayout topBox = new QBoxLayout(Direction.TopToBottom);
	QBoxLayout buttons = new QBoxLayout(Direction.TopToBottom);
	topBox.setContentsMargins(0, 0, 0, 0);
	topBox.addWidget(textEdit);
	buttons.addWidget(pushButton);
	buttons.setContentsMargins(0, 10, 10, 10);
	buttons.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignRight));
	topBox.addLayout(buttons);
	bmessageBox.setLayout(topBox);
	
  }
 
  void retranslateUi(QDialog bmessageBox)
  {
    bmessageBox.setWindowTitle(QCoreApplication.translate("bmessageBox", "Dialog", null));
    this.pushButton.setText(QCoreApplication.translate("bmessageBox", "确定", null));
    this.textEdit.setHtml(QCoreApplication.translate("bmessageBox", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\np, li { white-space: pre-wrap; }\n</style></head><body style=\" font-family:'SimSun'; font-size:9pt; font-weight:400; font-style:normal;\">\n<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><br /></p></body></html>", 
    
      null));
  }
}
