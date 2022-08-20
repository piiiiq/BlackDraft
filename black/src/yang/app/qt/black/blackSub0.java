package yang.app.qt.black;

import io.qt.widgets.QApplication;
import io.qt.gui.QFont;

public class blackSub0 extends black{
	public blackSub0(String[] args) {
		super(args);
	}
	public static void main(String[] args) {
		QApplication.initialize(args);
		QFont defaultfont = new QFont("微软雅黑", 9);
		QApplication.setFont(defaultfont);
		new blackSub0(args);
		QApplication.exec();
	}
	
}
