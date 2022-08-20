package yang.app.qt.black;

import java.io.DataInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import io.qt.core.QObject;
import io.qt.core.QSettings;
import io.qt.core.Qt;
import io.qt.core.Qt.ConnectionType;
import io.qt.gui.QColor;
import io.qt.gui.QImage;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPalette;
import io.qt.gui.QPixmap;
import io.qt.widgets.QApplication;
import io.qt.widgets.QTextEdit;
import yang.app.qt.shuchong.shuchong;
import yang.demo.allPurpose.httpGet;

public class test extends QObject {
	

	public static void main(String[] args) throws Exception {
		
		testQT(args);
	}

	public static void testQT(String[] args) {
		QApplication.initialize(args);
		
		new shuchong();

		QApplication.exec();
		QApplication.shutdown();
	}

	
}