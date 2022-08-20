package yang.demo.allPurpose;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public abstract class autoDO
  implements Serializable
{
  static final long serialVersionUID = 42L;
  private Timer timer;
  private TimerTask task;
  private long timeForAutoSave;
  public int time;
  
  public autoDO(int time)
  {
    this.timer = new Timer();
    setTimeForAutoSave(time);
    this.time = time;
  }
  
  public void stop()
  {
    if (this.timer != null) {
      this.timer.cancel();
    }
  }
  
  public void setTimeForAutoSave(int sec)
  {
    if (sec != -1) {
      this.timeForAutoSave = (sec * 1000);
    } else if (this.timer != null) {
      this.timer.cancel();
    }
  }
  
  public abstract void action();
  
  public void start()
  {
    this.task = new TimerTask()
    {
      public void run()
      {
        autoDO.this.action();
      }
    };
    this.timer.scheduleAtFixedRate(this.task, this.timeForAutoSave, this.timeForAutoSave);
  }
}
