package yang.app.black;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class showTime{
	
	private Timer timer;
	private TimerTask task;
	private Label timeLabel, progressLabel;
	private Display display;
	private int mode, lastHour, lastMinute;
	private Shell shell;
	public showTime(Label label, int mode)//mode分为3种，1为完全显示（即时分秒），2为简单模式（只显示时和分），3为极简模式（只显示时）
	{
		this.timeLabel = label;
		this.mode = mode;
		display = label.getDisplay();
		run();
	}
	public void setProgressLabel(Label progressLabel)//设置进度标签，此标签可以随时间改变长度
	{
		this.progressLabel = progressLabel;
	}
	public void setMode(int mode)
	{
		this.mode = mode;
		switch(mode)
		{
		case 2:
			timeLabel.setText(String.valueOf(lastHour + ":" + lastMinute));
			break;
		case 3:
			timeLabel.setText(String.valueOf(lastHour));
			
			default:
		}
	}
	
	public void changeProgressLabel(int hour)
	{
		int progressLabelY = progressLabel.getSize().y;
		shell = progressLabel.getShell();
		int shellWidth = shell.getSize().x;
		progressLabel.setSize(hour*(shellWidth/24), progressLabelY);

	}
	public void setShell(Shell shell)
	{
		this.shell = shell;
	}
	public void changeColor(int hour,int min,int sec)
	{
		
		RGB rgb = new RGB(hour*10,min*4,sec*4);
		progressLabel.setBackground(SWTResourceManager.getColor(rgb));
	}
	
	public void closeTask()
	{
		timer.cancel();
		
	}
	public void run()
	{
		timer = new Timer();
		task = new TimerTask() {
	            @Override
				public void run() {
 		    
	            	display.asyncExec(new Runnable(){
	            		@Override
						public void run(){
	            		GregorianCalendar cal = new GregorianCalendar();
		    		    // Get the components of the time
		    		    //int hour12 = cal.get(Calendar.HOUR);            // 0..11
		    		    int hour24 = cal.get(Calendar.HOUR_OF_DAY);     // 0..23
		    		    int min = cal.get(Calendar.MINUTE);             // 0..59
		    		    int sec = cal.get(Calendar.SECOND);             // 0..59
		    		    //int ms = cal.get(Calendar.MILLISECOND);         // 0..999
		    		    // ampm = cal.get(Calendar.AM_PM);             // 0=AM, 1=PM
		    		    if(!timeLabel.isDisposed())
		    		    {
		    		    	switch(mode)
		    		    	{
		    		    	case 1:
		    		    		timeLabel.setText(String.valueOf(hour24) + ":" + String.valueOf(min) + ":" + String.valueOf(sec));
		    		    		break;
		    		    	case 2:
		    		    		timeLabel.setText(String.valueOf(hour24) + ":" + String.valueOf(min));
		    		    		break;
		    		    	case 3:
		    		    		timeLabel.setText(String.valueOf(hour24));
		    		    		
		    		    	default:
		    		    		lastHour = hour24;
		    		    		lastMinute = min;
		    		    	}
		    		    }
		    		     if(progressLabel != null && !progressLabel.isDisposed())
		    		    	changeProgressLabel(hour24);
//		    		     
//		    		     if(shell != null && !shell.isDisposed())
//		    		    	changeColor(hour24, min, sec);
//		    		    
		    		    		
		    		    
		    		    
		    		    
	    		    }});
	               
	            }
		};
		    timer.scheduleAtFixedRate(task, 0, 1000);

		
		

	}
   
}
