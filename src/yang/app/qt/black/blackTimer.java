package yang.app.qt.black;

import io.qt.core.QObject;
import io.qt.core.QTimer;
import java.util.HashMap;

public abstract class blackTimer
{
  private QTimer timer;
  boolean onlyOnce;
  private HashMap<String, Object> datas = new HashMap();
  private int count;
  
  public blackTimer(QObject parent, boolean onlyOnce)
  {
    this.timer = new QTimer(parent);
    this.onlyOnce = onlyOnce;
    this.timer.timeout.connect(this, "doing()");
    this.timer.start();
  }
  
  public blackTimer(QObject parent, int ms, boolean onlyOnce)
  {
    this.timer = new QTimer(parent);
    this.onlyOnce = onlyOnce;
    this.timer.timeout.connect(this, "doing()");
    this.timer.start(ms);
  }
  
  public blackTimer(QObject parent, int ms, boolean onlyOnce, boolean isStart)
  {
    this.timer = new QTimer(parent);
    this.onlyOnce = onlyOnce;
    this.timer.timeout.connect(this, "doing()");
    if (!isStart) {
      this.timer.setInterval(ms);
    } else {
      this.timer.start(ms);
    }
  }
  
  public void start()
  {
    this.timer.start();
  }
  
  public void setData(String key, Object data)
  {
    this.datas.put(key, data);
  }
  
  public Object Data(String key)
  {
    return this.datas.get(key);
  }
  
  public void stopAndDispose()
  {
    this.timer.stop();
    this.timer.dispose();
    this.timer = null;
  }
  
  public QTimer timer()
  {
    return this.timer;
  }
  
  public int timerId()
  {
    return this.timer.timerId();
  }
  
  private void doing()
  {
    this.count += 1;
    if (this.onlyOnce)
    {
      stopAndDispose();
    }
    else
    {
      int lastAction = 0;
      Object data = Data("lastAction");
      if (data != null)
      {
        lastAction = ((Integer)data).intValue();
        if (lastAction != this.count - 1)
        {
          System.out.println("停止执行，因为执行顺序错误！前一次执行index: " + lastAction + " 当前执行index: " + this.count);
          this.count -= 1;
        }
      }
      else if (this.count > 1)
      {
        System.out.println("停止执行，因为执行顺序错误！前一次执行index: " + lastAction + " 当前执行index: " + this.count);
        this.count -= 1;
        return;
      }
    }
    action(this.timer);
    setData("lastAction", Integer.valueOf(this.count));
  }
  
  public abstract void action(QTimer paramQTimer);
}
