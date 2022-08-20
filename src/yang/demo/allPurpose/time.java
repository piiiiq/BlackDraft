package yang.demo.allPurpose;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import yang.app.qt.black.black;
public class time
  implements Serializable
{
  static final long serialVersionUID = 42L;
  public static String getWebsiteDateDAY_OF_MONTH(String webUrl){
	  if(black.getNetworkTime == 0) {
   	   Calendar instance = Calendar.getInstance(Locale.CHINESE);
   	   return instance.get(Calendar.DAY_OF_MONTH)+"";
      }
	  
	  try {
          URL url = new URL(webUrl);// 取得资源对象
          URLConnection uc = url.openConnection();// 生成连接对象
          uc.connect();// 发出连接
          long ld = uc.getDate();// 读取网站日期时间
//          Date date = new Date(ld);// 转换为标准时间对象
         Calendar c = Calendar.getInstance();
          c.setTimeInMillis(ld);
         return c.get(Calendar.DAY_OF_MONTH)+"";
          
      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          return "@@";
      }
      return null;
  }
  /**
   * 从指定的网站获取分钟数（一小时中的）
   * 
   * @param webUrl
   * @return @@ 返回@@表示网络出现问题
   * 
   */
  public static String getWebsiteDateMinOfHours(String webUrl){
	  if(black.getNetworkTime == 0) {
   	   Calendar instance = Calendar.getInstance(Locale.CHINESE);
   	   return instance.get(Calendar.MINUTE)+"";
      }
	  
	  try {
          URL url = new URL(webUrl);// 取得资源对象
          URLConnection uc = url.openConnection();// 生成连接对象
          uc.connect();// 发出连接
          long ld = uc.getDate();// 读取网站日期时间
//          Date date = new Date(ld);// 转换为标准时间对象
         Calendar c = Calendar.getInstance();
          c.setTimeInMillis(ld);
         return c.get(Calendar.MINUTE)+"";
          
      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          return "@@";
      }
      return null;
  }
  /**
   * 从指定的网站获取小时数（一天中的）
   * 
   * @param webUrl
   * @return @@ 返回@@表示网络出现问题
   * 
   */
  public static String getWebsiteDateHourOfDay(String webUrl){
	  if(black.getNetworkTime == 0) {
   	   Calendar instance = Calendar.getInstance(Locale.CHINESE);
   	   return instance.get(Calendar.HOUR_OF_DAY)+"";
      }
	  
	  try {
          URL url = new URL(webUrl);// 取得资源对象
          URLConnection uc = url.openConnection();// 生成连接对象
          uc.connect();// 发出连接
          long ld = uc.getDate();// 读取网站日期时间
//          Date date = new Date(ld);// 转换为标准时间对象
         Calendar c = Calendar.getInstance();
          c.setTimeInMillis(ld);
         return c.get(Calendar.HOUR_OF_DAY)+"";
          
      } catch (MalformedURLException e) {
          e.printStackTrace();
      } catch (IOException e) {
          return "@@";
      }
      return null;
  }
	/**
     * 从指定的网站获取星期几
     * 
     * @param webUrl
     * @return @@ 返回@@表示网络出现问题
     * 
     */
    public static String getWebsiteDateDayOfWeek(String webUrl){
       if(black.getNetworkTime == 0) {
    	   Calendar instance = Calendar.getInstance(Locale.CHINESE);
    	   return instance.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.CHINESE);
       }
    	
    	try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
//            Date date = new Date(ld);// 转换为标准时间对象
           Calendar c = Calendar.getInstance();
            c.setTimeInMillis(ld);
           return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.CHINESE);
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            return "@@";
        }
        return null;
    }
 
    public static void main(String[] args) {
        String webUrl1 = "http://www.bjtime.cn";//bjTime
        String webUrl2 = "http://www.baidu.com";//百度
        String webUrl3 = "http://www.taobao.com";//淘宝
        String webUrl4 = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        String webUrl5 = "http://www.360.cn";//360
        String webUrl6 = "http://www.beijing-time.org";//beijing-time
        System.out.println(getWebsiteDateHourOfDay(webUrl2) + " [bjtime]");
//        System.out.println(getWebsiteDatetime(webUrl2) + " [百度]");
//        System.out.println(getWebsiteDatetime(webUrl3) + " [淘宝]");
//        System.out.println(getWebsiteDatetime(webUrl4) + " [中国科学院国家授时中心]");
//        System.out.println(getWebsiteDatetime(webUrl5) + " [360安全卫士]");
//        System.out.println(getWebsiteDatetime(webUrl6) + " [beijing-time]");
    }
    /**
     * 将msToTimeSimple(long)方法的返回值转换成字符串形式
     * @param time
     * @return
     */
    public static String intTimeToString(int[] time) {
		switch(time[0]) {
		case 0:return time[1]+"天";
		case 1:return time[1]+"小时";
		case 2:return time[1]+"分钟";
		case 3:return time[1]+"秒";
		}
		return null;
	}
    /**
     * 将给定的long值转化成简易时间，此方法仅获取时间的最大单位，例如：准确时间为2天21小时32分钟16秒，调用此方法后会返回最大单位‘天’上的数值，即2天
     * @param l
     * @return返回一个有两个元素的整形数组，第一个元素为单位，0为天，3为秒，中间以此类推；第二个元素为时间单位对应的值
     */
	public static int[] msToTimeSimple(long l) {
		int[] res = new int[2];
		int[] msToTime_int = time.msToTime_int(l);

		for(int i=0;i<msToTime_int.length;i++) {
//			System.out.println(i);
			int t = msToTime_int[i];
			if(t == 0) {
				if(i+1<msToTime_int.length) {
					int j = msToTime_int[i+1];
					if(j > 0) {
						res[0] = i+1;
						res[1] = j;
						return res;
					}
				}
			}else {
				res[0] = i;
				res[1] = t;
				return res;
			}
		}
		return null;
	}
  /**
   * 将给定的毫秒值转换为以x天x小时x分钟x秒为单位的时间
   * @param ms
   * @return
   */
  public static String msToTime(long ms) {
		long l = ms;
		int s = (int) (l/1000);
		int day = s/(24*3600);
		int hours = 0;
		int min = 0;
		int day_ = (s%(24*3600));
		int sec = 0;
		if(day == 0) {
			if(s >= 3600)
				hours = (s/3600);
			else hours = 0;
			
			if(hours == 0) {
				if(s >= 60)
					min = s/60;
				else min = 0;
			}else {
				min = (s%3600)/60;
			}
		}else {
			if(day_ >= 3600)
				hours = day_/3600;
			else hours = 0;
			
			if(hours == 0) {
				if(day_ >= 60)
					min = day_/60;
				else min = 0;
			}else {
				if((day_%3600) >= 60)
					min = (day_%3600)/60;
				else min = 0;
			}
		}
//		System.out.print(day+":"+hours+":"+min);
		sec = s-(day*24*3600)-(hours*3600)-(min*60);
		String sec_text = "";
		if(sec == 0) {
			
		}
		else if(sec < 10) sec_text = "0"+sec+"秒";
		else sec_text = sec+"秒";
		if(day > 0)
			return day+"天"+hours+"小时"+min+"分钟"+sec_text;
		else if(hours > 0)
			return hours+"小时"+min+"分钟"+sec_text;
		else if(min > 0)
			return min+"分钟"+sec_text;
		else return sec_text;
	}
/**
 * 将long值转换为包含天时分秒的时间
 * @param ms
 * @return包含4个元素的整形数组，0为天，3为秒
 */
  public static int[] msToTime_int(long ms) {
		long l = ms;
		int s = (int) (l/1000);
		int day = s/(24*3600);
		int hours = 0;
		int min = 0;
		int day_ = (s%(24*3600));
		int sec = 0;
		if(day == 0) {
			if(s >= 3600)
				hours = (s/3600);
			else hours = 0;
			
			if(hours == 0) {
				if(s >= 60)
					min = s/60;
				else min = 0;
			}else {
				min = (s%3600)/60;
			}
		}else {
			if(day_ >= 3600)
				hours = day_/3600;
			else hours = 0;
			
			if(hours == 0) {
				if(day_ >= 60)
					min = day_/60;
				else min = 0;
			}else {
				if((day_%3600) >= 60)
					min = (day_%3600)/60;
				else min = 0;
			}
		}
//		System.out.print(day+":"+hours+":"+min);
		sec = s-(day*24*3600)-(hours*3600)-(min*60);
		int[] times = new int[4];
		times[0] = day;
		times[1] = hours;
		times[2] = min;
		times[3] = sec;
		return times;
	}
  public static String getCurrentDate(String cutapart)
  {
    int year = Calendar.getInstance().get(1);
    int month = Calendar.getInstance().get(2) + 1;
    int dayofMonth = Calendar.getInstance().get(5);
    String strYear = String.valueOf(year);
    String strMonth = null;
    String strDayofMonth = null;
    if (month < 10) {
      strMonth = "0" + month;
    } else {
      strMonth = String.valueOf(month);
    }
    if (dayofMonth < 10) {
      strDayofMonth = "0" + dayofMonth;
    } else {
      strDayofMonth = String.valueOf(dayofMonth);
    }
    return strYear + cutapart + strMonth + cutapart + strDayofMonth;
  }
  
  public static String getCurrentTime()
  {
    String hourOfDay = String.valueOf(Calendar.getInstance().get(11));
    String minute = String.valueOf(Calendar.getInstance().get(12));
    if (Integer.valueOf(hourOfDay).intValue() < 10) {
      hourOfDay = "0" + hourOfDay;
    }
    if (Integer.valueOf(minute).intValue() < 10) {
      minute = "0" + minute;
    }
    return hourOfDay + ":" + minute;
  }
  
  public static String getCurrentTimeHasSecond()
  {
    String hourOfDay = String.valueOf(Calendar.getInstance().get(11));
    String minute = String.valueOf(Calendar.getInstance().get(12));
    String second = String.valueOf(Calendar.getInstance().get(13));
    if (Integer.valueOf(hourOfDay).intValue() < 10) {
      hourOfDay = "0" + hourOfDay;
    }
    if (Integer.valueOf(minute).intValue() < 10) {
      minute = "0" + minute;
    }
    if (Integer.valueOf(second).intValue() < 10) {
      second = "0" + second;
    }
    return hourOfDay + ":" + minute + ":" + second;
  }
  public static String getDate(long time)
  {
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(time);
    String t = String.valueOf(cal.get(1)) + "-" + String.valueOf(cal.get(2)) + "-" + 
    		String.valueOf(cal.get(5));
    return t;
  }
  public static String getTime(long time)
  {
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(time);
    String t = String.valueOf(cal.get(1)) + "-" + String.valueOf(cal.get(2)) + "-" + String.valueOf(new StringBuilder(String.valueOf(cal.get(5))).append(" ").append(String.valueOf(new StringBuilder(String.valueOf(cal.get(11))).append(":").append(String.valueOf(new StringBuilder(String.valueOf(cal.get(12))).append(":").append(String.valueOf(cal.get(13))).toString())).toString())).toString());
    return t;
  }
  
  public static int getDays(long mils)
  {
    return (int)(mils / 1000L / 3600L / 24L);
  }
  
  public static long getCurrentTime_long()
  {
    Calendar cal = Calendar.getInstance();
    return cal.getTimeInMillis();
  }
}
