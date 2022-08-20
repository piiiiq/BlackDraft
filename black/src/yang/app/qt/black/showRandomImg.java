package yang.app.qt.black;

import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import io.qt.core.QEvent;
import io.qt.core.QRect;
import io.qt.core.Qt;
import io.qt.core.Qt.WidgetAttribute;
import io.qt.core.Qt.WindowType;
import io.qt.gui.QAccessible.Event;
import io.qt.gui.QColor;
import io.qt.gui.QIcon;
import io.qt.gui.QImage;
import io.qt.gui.QKeyEvent;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPalette;
import io.qt.gui.QPixmap;
import io.qt.widgets.QAction;
import io.qt.widgets.QApplication;
import io.qt.widgets.QWidget;

public class showRandomImg extends QWidget{
	protected QImage im;
	QRect r = bAction.desktopGeometry(true);
	private Thread thread;
	private QPixmap pix;

	public static void main(String args[]) {
		QApplication.initialize(args);
		new showRandomImg(10,"http://hello.com","woman,face");
		QApplication.exec();
	}
	public showRandomImg(int time,String url,String keywords) {
		test(time,url,keywords);
	}
	protected void paintEvent(QPaintEvent event) {
		// TODO Auto-generated method stub
		super.paintEvent(event);
		QPainter p = new QPainter(this);
		if(im != null) {
			int x = (r.width()-im.width())/2;
			int y = (r.height()-im.height())/2; 
			this.setGeometry(x, y, im.width(), im.height());
			this.setFixedSize(im.width(), im.height());
			setWindowIcon(new QIcon(pix));
			p.drawImage((width()-im.width())/2,(height()-im.height())/2, im);
		}

	}
	public void test(int time,String Url,String keywords) {
		
//		setAutoFillBackground(true);
		setWindowFlags(WindowType.CustomizeWindowHint,WindowType.Dialog);
		
//		setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground);
		setAttribute(WidgetAttribute.WA_NoSystemBackground,false);
		QPalette p = palette();
		QColor c = new QColor(Qt.GlobalColor.blue);
//		c.setAlpha(0);
		p.setColor(backgroundRole(), c);
		setPalette(p);
		setGeometry(10, 10, 800, 400);
		show();
//		showFullScreen();
		setWindowTitle("随机图片");

		thread = new Thread(new Runnable() {
			

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String urlLink = null;
				if(Url.equals("0")) {
					if(keywords != null) {
						urlLink = "https://source.unsplash.com/"+(r.width()-100)+"x"+(r.height()-100)+"/?"+keywords;
					}else {
						urlLink = "https://source.unsplash.com/random";
					}
				}
				else urlLink = Url;
				
				while(true) {
					try {
						URL url = new URL(urlLink);
						URLConnection oc = url.openConnection();
						oc.setConnectTimeout(5000);
						oc.setReadTimeout(5000);
						DataInputStream dataInputStream = new DataInputStream(oc.getInputStream());
						byte[] bytes = dataInputStream.readAllBytes();
						dataInputStream.close();
						
						pix = new QPixmap();
						pix.loadFromData(bytes);
						
						im = pix.toImage();
						
						if(im.width() > r.width()-100) {
							im = im.scaledToWidth(r.width()-100);
						}
						if(im.height() > r.height()-100) {
							im = im.scaledToHeight(r.height()-100);
						}
						update();
					} catch (Exception e) {
//						e.printStackTrace();
						if(e.getClass().getName().equals("java.net.UnknownHostException")) {
							try {
								Thread.sleep(60000);
								continue;
							} catch (InterruptedException ee) {
								// TODO Auto-generated catch block
								return;
							}
						}
						else if(!e.getClass().getName().equals("java.lang.InterruptedException")) {
							continue;
						}
					}
					try {
						Thread.sleep(time*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return;
					}
				}
				
			}
		});
		thread.start();
		

	}
	@Override
	public boolean event(QEvent event) {
		// TODO Auto-generated method stub
		boolean e = super.event(event);
		if(event.type() == QEvent.Type.Hide) {
			thread.interrupt();
		}else if(event.type() == QEvent.Type.KeyPress) {
			QKeyEvent k = (QKeyEvent) event;
			if(k.key() == Qt.Key.Key_Escape.value()) {
				thread.interrupt();
				close();
			}
		}
		return e;
	}
}
