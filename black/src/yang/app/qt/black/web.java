package yang.app.qt.black;

import io.qt.core.QEvent;
import io.qt.core.QEvent.Type;
import io.qt.core.QObject;
import io.qt.core.QUrl;
import io.qt.core.Qt;
import io.qt.core.Qt.Key;
import io.qt.gui.QCloseEvent;
import io.qt.widgets.QComboBox;
import io.qt.gui.QKeyEvent;
import io.qt.widgets.QMainWindow;
import java.io.PrintStream;

public class web
  extends QMainWindow
{
  private QComboBox addres;
  private QWebView web;
  
  public web(String url)
  {
    setGeometry(100, 100, 700, 600);
    this.addres = new QComboBox(this);
    this.addres.setEditable(true);
    this.addres.installEventFilter(this);
    if (url != null) {
      this.addres.addItem(url);
    }
    this.addres.addItem("http://cn.bing.com");
    this.addres.addItem("http://www.baidu.com");
    this.addres.addItem("https://www.google.de");
    
    this.web = new QWebView(this);
    
    this.web.linkClicked.connect(this, "link(QUrl)");
    
    this.web.load(new QUrl("file:///D:/Apps/Unpack/qtjambi-java/qtjambi-win32-lgpl-4.5.2_01/doc/html/index.html"));
    
    installEventFilter(this);
    show();
  }
  
  public void link(QUrl url)
  {
    System.out.println(url + "hello");
    new web(url.toString());
  }
  
  public void resize()
  {
    this.addres.setGeometry(0, 0, width(), 40);
    this.web.setGeometry(0, this.addres.height(), width(), height() - this.addres.height());
  }
  
  public boolean eventFilter(QObject o, QEvent e)
  {
    if ((o.equals(this)) && (e.type() == QEvent.Type.Resize))
    {
      resize();
    }
    else if ((o.equals(this.addres)) && (e.type() == QEvent.Type.KeyPress))
    {
      QKeyEvent key = (QKeyEvent)e;
      if (key.key() == Qt.Key.Key_Return.value())
      {
        System.out.println(this.addres.currentText());
        this.web.load(new QUrl(this.addres.currentText()));
      }
    }
    return super.eventFilter(o, e);
  }
  
  protected void closeEvent(QCloseEvent arg__1)
  {
    System.exit(0);
  }
}
