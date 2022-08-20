package yang.app.qt.black;

import io.qt.core.QTimer;
import io.qt.widgets.QApplication;
import io.qt.widgets.QDialog;
import io.qt.widgets.QLabel;
import io.qt.widgets.QProgressBar;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QWidget;

public abstract class showProgress
  extends QDialog
{
  Ui_showProgress ui = new Ui_showProgress();
  black b;
  boolean autoClose;
  private QTimer timer;
  
  public static void main(String[] args)
  {
    QApplication.initialize(args);
    
    showProgress testshowProgress = new showProgress()
    {
      public void whenAutoClose() {}
      
      public void whenOkButtonPressed() {}

      public void whenDialogClosed() {
		// TODO Auto-generated method stub
		
      }
      
    };
    testshowProgress.show();
    
	QApplication.exec();
  }
//  void t() {
//	  System.out.println("ok");
//  }
  
  public showProgress()
  {
    this.ui.setupUi(this);
    this.ui.progress.setValue(100);
    this.finished.connect(this,"whenDialogClosed()");
  }
  
  public showProgress(QWidget parent, black b, String title, boolean autoClose)
  {
    super(parent);
    this.ui.setupUi(this);
    this.autoClose = autoClose;
    this.b = b;
    setWindowTitle(title);
    
    this.ui.OK.setEnabled(false);
    setFixedSize(width(), height());
    this.ui.OK.pressed.connect(this, "whenOkButtonPressed()");
    this.finished.connect(this,"whenDialogClosed()");

    this.timer = new QTimer(this);
    this.timer.timeout.connect(this, "action()");
    this.timer.start(500);
//    setPalette(b.palette());
    b.progressBugStop = false;
    show();
  }
  
  public void action()
  {
    if (!this.b.progressBugStop)
    {
      this.ui.showMessage.setText(this.b.progressMessage);
      this.ui.progress.setValue(this.b.progressValue);
      if (this.b.progressValue >= 100)
      {
        this.ui.progress.setValue(100);
        this.timer.stop();
        this.timer.dispose();
        if (this.autoClose)
        {
          whenAutoClose();
          close();
        }
        else
        {
          this.ui.OK.setEnabled(true);
        }
      }
    }
    else
    {
      this.ui.showMessage.setText(this.b.progressMessage);
      this.ui.progress.setValue(this.b.progressValue);
      this.timer.stop();
      this.timer.dispose();
      this.ui.OK.setEnabled(true);
    }
  }
  public abstract void whenDialogClosed();
  public abstract void whenAutoClose();
  
  public abstract void whenOkButtonPressed();
}
