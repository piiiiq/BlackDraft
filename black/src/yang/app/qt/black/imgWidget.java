package yang.app.qt.black;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import io.qt.core.QEvent;
import io.qt.core.QObject;
import io.qt.core.QPoint;
import io.qt.core.QRect;
import io.qt.core.QRectF;
import io.qt.core.Qt;
import io.qt.core.Qt.BGMode;
import io.qt.core.Qt.PenStyle;
import io.qt.widgets.QApplication;
import io.qt.gui.QBrush;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QFontMetrics;
import io.qt.gui.QImage;
import io.qt.gui.QKeyEvent;
import io.qt.widgets.QLabel;
import io.qt.gui.QMouseEvent;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPixmap;
import io.qt.gui.QTextOption.Flag;
import io.qt.gui.QTextOption.Flags;
import io.qt.widgets.QWidget;

import yang.demo.allPurpose.httpGet;
import yang.demo.allPurpose.time;

public class imgWidget extends QWidget {
	private QImage img;
//	QLabel text = new QLabel(this);
	black b;
	
	private String title;
	private String location;
	private String story;
	boolean lightColor;
	String currentHistoryPic;
	int currentHistoryPicIndex = -1;
	private File[] historyPicLists;
	boolean locationPic;
	String bingPicPath = "./RC/bing";
	public imgWidget(black b) {
		this.b = b;
		setWindowFlags(Qt.WindowType.Tool);
		
//		updateImgAndText();
		installEventFilter(this);
	}
	/**
	 * 	更新历史图片列表
	 * 	
	 * 	
	 */
	void updateHistoryPicList() {
		File f = new File("./RC/saved");
		historyPicLists = f.listFiles();
		if(historyPicLists.length > 0)
		currentHistoryPicIndex = historyPicLists.length-1;
		else currentHistoryPicIndex = 0;
//		for(int i=0;i<historyPicLists.length;i++) {
//			File his = historyPicLists[i];
//			if(his.getName().equals(currentHistoryPic)) {
//				currentHistoryPicIndex = i;
//				break;
//			}
//		}
	}
	void prevPic() {
		if(historyPicLists == null) {
			updateHistoryPicList();
		}
		if(historyPicLists.length == 0)return;

		if(currentHistoryPicIndex > 0) {
//			System.out.println("a: ");
			currentHistoryPicIndex--;
			File his = historyPicLists[currentHistoryPicIndex];
			img = new QImage(his.getAbsolutePath());
			locationPic = true;
			update();
		}else {
			currentHistoryPicIndex=historyPicLists.length-1;
			File his = historyPicLists[currentHistoryPicIndex];
			img = new QImage(his.getAbsolutePath());
			locationPic = true;
			update();
		}
	}
	void nextPic() {
		if(historyPicLists == null) {
			updateHistoryPicList();
		}
		if(historyPicLists.length == 0)return;
		
		if(currentHistoryPicIndex < historyPicLists.length-1) {
			currentHistoryPicIndex++;
			File his = historyPicLists[currentHistoryPicIndex];
			img = new QImage(his.getAbsolutePath());
			locationPic = true;
			update();
		}else {
			currentHistoryPicIndex=0;
			File his = historyPicLists[currentHistoryPicIndex];
			img = new QImage(his.getAbsolutePath());
			locationPic = true;
			update();
		}
	}
	void showCurrentPic() {
		if(title != null && !title.isEmpty()) {
			img = new QImage(bingPicPath);
			locationPic = false;
			update();
		}else {
			if(historyPicLists == null) {
				updateHistoryPicList();
			}
			if(historyPicLists.length > 0) {
				currentHistoryPicIndex = historyPicLists.length-1;
				img = new QImage(historyPicLists[currentHistoryPicIndex].getAbsolutePath());
				locationPic = true;
				update();
			}
		}
	}
	@Override
	public boolean eventFilter(QObject arg__1, QEvent arg__2) {
		// TODO Auto-generated method stub
		if(arg__2.type() == QEvent.Type.MouseButtonPress) {	
			QMouseEvent m = (QMouseEvent) arg__2;
			QPoint pos = m.pos();
			if(pos.y() < 10) {
				showCurrentPic();
			}
			else if(pos.x() > 10 && pos.x() < width()-10) {
				lightColor = !lightColor;
				update();
			}else if(pos.x() < 10) {
				prevPic();
			}else if(pos.x() > width()-10) {
				nextPic();
			}
			
			return true;
		}else if(arg__2.type() == QEvent.Type.KeyPress) {
			QKeyEvent k = (QKeyEvent) arg__2;
//			System.out.println(k.key()+" "+k.text());
			if(k.key() == Qt.Key.Key_Left.value()) {
				prevPic();
			}else if(k.key() == Qt.Key.Key_Right.value()) {
				nextPic();
			}else if(k.key() == 16777235) {
				//上方向键
				showCurrentPic();
			}
			return true;
		}
		return super.eventFilter(arg__1, arg__2);
	}
	public void updateImgAndText() {
//		System.out.println("ok");
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				b.openWifiOneMin();
				if(b.ping(60000)) {
//					b.checkWepWifi();
					String html = httpGet.httpGet("https://api.berryapi.net/bing/?AppKey=b0nfIll2VA");
					if(html != null && !html.isEmpty()) {
						title = cheakDocument.subString(html, "\"title\":\"", "\",").text;
						location = cheakDocument.subString(html, "\"attribute\":\"", "\",").text;
						story = cheakDocument.subString(html, "\"story\":\"", "\",\"search").text;
					}
					String p = null;
					try {
						p = getHtml("http://guolin.tech/api/bing_pic","utf-8");
					}catch(Exception e) {
						e.printStackTrace();
					}
					if(p == null)return;
					
					downloadPicture(p, bingPicPath);
					
					if(new File(bingPicPath).exists())
					b.uiRun(b,new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub	
							locationPic = false;

							img = new QImage(bingPicPath);
							img = img.scaled(1280, 800);
							if(!isVisible())showFullScreen();
							else {
								hide();
								show();
							}
							if(!title.isEmpty()) {
								File f = new File("./RC/saved");
								if(!f.exists()) {
									f.mkdirs();
								}
								if(f.exists()) {
									String date = time.getCurrentDate("-");
									File save = new File(f.getAbsolutePath()+"/"+date+".jpg");
									if(!save.exists()) {
										QPixmap p = QPixmap.grabWidget(imgWidget.this);
										
										p.save(save.getAbsolutePath());
										b.p("已保存当日的bing图片: "+title);
										updateHistoryPicList();
									}
									
								}
							}
						}
					});
				}
				//备用措施，如果没有与服务器连通的话执行
				else
				b.uiRun(b,new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateHistoryPicList();
						if(historyPicLists.length == 0) {
							hide();
						}else {
							if(!isVisible()) {
								img = new QImage(historyPicLists[historyPicLists.length-1].getAbsolutePath());
								img = img.scaled(1280, 800);
								locationPic = true;
								showFullScreen();
								b.update();
							}
						}
//						if(!new File(bingPicPath).exists()) {
//							hide();
//						}else {
//							if(!isVisible()) {
//								img = new QImage(bingPicPath);
//								img = img.scaled(1280, 800);
//								showFullScreen();
//								b.update();
//							}
//						}
					}
				});
			}
		}).start();
	}
	
	public void paintEvent(QPaintEvent e) {
		super.paintEvent(e);
		QPainter p = new QPainter(this);
		if(img != null)p.drawImage(rect(), img);
//		p.setBackground(new QBrush(new QColor(255,255,255,100)));
//		p.setBackgroundMode(BGMode.OpaqueMode);
		QFont ff = new QFont("微软雅黑",30);
		p.setFont(ff);
		String s = (currentHistoryPicIndex+1) +"/"+historyPicLists.length;
		QRect br = p.fontMetrics().boundingRect(s);
		p.setPen(new QColor(Qt.GlobalColor.black));
		p.drawText(width()-br.width()-20, height()-20, s);
		if(!locationPic)
		if(title != null && !title.isEmpty()) {
			if(!lightColor)
			p.setBrush(new QBrush(new QColor(255,255,255,50)));
			else 
				p.setBrush(new QBrush(new QColor(255,255,255,200)));
			p.setPen(PenStyle.NoPen);
			QFont f = new QFont("微软雅黑",11);
			p.setFont(f);
			QFontMetrics fm = new QFontMetrics(f);
			QRect r = new QRect(50, 101, 500, 700);
			QRect boundingRect = fm.boundingRect(r, Qt.TextFlag.TextWordWrap.value(), story);
			p.drawRoundedRect(30, 30, 540, 81+boundingRect.height(), 10, 10);
			
			
			p.setPen(PenStyle.DotLine);
			
			f.setPointSizeF(17);
			f.setBold(true);
			p.setFont(f);
			p.drawText(50, 60, title);
			f.setPointSize(11);
			f.setItalic(true);
			f.setBold(false);
			p.setFont(f);
			p.drawText(50, 92, location);
			f.setItalic(false);
			p.setFont(f);
			p.drawText(new QRectF(r), story);
			
		}
		
		p.end();
	}
	static String getHtml(String urlString,String encode) throws Exception {
			StringBuffer sb = new StringBuffer();
            URL url = new URL(urlString);
            InputStream in = url.openStream();
            InputStreamReader isr = new InputStreamReader(in,encode);
            BufferedReader bufr = new BufferedReader(isr);
            String str;
            while ((str = bufr.readLine()) != null) {
                sb.append(str);
            }
            bufr.close();
            isr.close();
            in.close();
       
		return sb.toString();
	}
	 //链接url下载图片
    static void downloadPicture(String urlList,String imageName) {
        URL url = null;
        int imageNumber = 0;

        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
