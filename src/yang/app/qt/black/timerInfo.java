package yang.app.qt.black;

import io.qt.widgets.QMessageBox;
import yang.demo.allPurpose.time;

class timerInfo
{
 public long startTime;
 public long time;
 public  String messageOfTimeout;
 public  String timerName;
 public  String progressMessage;
  QMessageBox box;
  public int type = -1;
  public Runnable run;
  public boolean canExit;
  public boolean stilLive,hideLeftTime;
  public int id;
  public  boolean doNotShowTip;
  public Object data;
  public boolean showProgress = true;
  public boolean showNow,
  /**
   * 此开关打开后调用addtimer()方法会自动检查type是否重复，如果重复会给出提示
   */
  isNew;
  
  public timerInfo(long startTime, long time, String messageOfTimeout, String timerName, QMessageBox box)
  {
    this.startTime = startTime;
    this.time = time;
    this.messageOfTimeout = messageOfTimeout;
    this.timerName = timerName;
    this.box = box;
  }
  
  public timerInfo(int type, long startTime, long time, Runnable run)
  {
    this.type = type;
    this.run = run;
    this.startTime = startTime;
    this.time = time;
  }
  
  public timerInfo(int type, long time, String timerName, Runnable run, boolean canExit)
  {
    this.type = type;
    this.run = run;
    this.timerName = timerName;
    this.startTime = yang.demo.allPurpose.time.getCurrentTime_long();
    this.time = time;
    this.canExit = canExit;
  }
  
  public timerInfo(int type, long time, String timerName, Runnable run, boolean canExit, boolean donNotShowTip)
  {
    this.type = type;
    this.run = run;
    this.timerName = timerName;
    this.startTime = yang.demo.allPurpose.time.getCurrentTime_long();
    this.time = time;
    this.canExit = canExit;
    this.doNotShowTip = donNotShowTip;
  }
  
  public timerInfo(boolean stillLive, String timerName,Runnable run)
  {
    this.stilLive = stillLive;
    this.timerName = timerName;
    this.run = run;
    this.progressMessage = "";
  }
  /**
   * 将一个设定了时间的timer进行延时，使之剩余的时间等于所设定的时间
   */
  public void setTimeOut() {
	  this.startTime = yang.demo.allPurpose.time.getCurrentTime_long();
  }
  public void setID(int id)
  {
    this.id = id;
  }
  public void setRunnable(Runnable run) {
	  this.run = run;
  }
  public void setData(Object data) {
	  this.data = data;
  }
}
