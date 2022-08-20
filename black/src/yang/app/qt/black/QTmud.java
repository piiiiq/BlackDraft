package yang.app.qt.black;

import io.qt.core.QByteArray;
import io.qt.core.QEvent;
import io.qt.core.QObject;
import io.qt.core.QRect;
import io.qt.core.QSettings;
import io.qt.core.Qt;
import io.qt.core.Qt.WidgetAttribute;
import io.qt.core.Qt.WindowFlags;
import io.qt.core.Qt.WindowStates;
import io.qt.gui.QCloseEvent;
import io.qt.widgets.QApplication;
import io.qt.widgets.QMainWindow;
/**
 * Qt5.15的窗口透明度必须在设置最大化窗口属性前设置，否则会出现窗口透明度失效的问题
 * @author test
 *
 */
public class QTmud extends QMainWindow {
	QSettings settings = new QSettings("./CFG/configs", QSettings.Format.IniFormat);
	QByteArray geometry;

	public static void main(String[] args) {
		System.out.println("hello");
		QApplication.initialize(args);

		QTmud testQTmud = new QTmud();
		QApplication.exec();
	}

	public QTmud() {
		//在可能从设置中还原最大化窗口信息之前设置窗口透明度
		setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground, true);
		setAttribute(Qt.WidgetAttribute.WA_NoSystemBackground, false);
		this.settings.setIniCodec("utf-8");
		restoreWindowSizeAndLocationAndState();
//		show();
	}

	public void restoreWindowSizeAndLocationAndState() {
//		this.testAttribute(Qt.WidgetAttribute.)
//		System.out.println("wf: "+wf);
		restoreGeometry((QByteArray) this.settings.value("app/geometry"));
//		WindowFlags wf1 = windowFlags();
//		System.out.println("wf1: "+wf1);
		
		restoreState((QByteArray) this.settings.value("app/state"));
	}

	public QSettings getSettings() {
		return this.settings;
	}
	public void whenClose() {
		System.out.println("QTmud geometry is "+geometry());
		if(!isVisible()) {
			System.out.println("Window is Hide");
		}
		this.settings.beginGroup("app");
		WindowStates windowState = windowState();
		if(windowState.isSet(Qt.WindowState.WindowMaximized)) {
			System.out.println("Window is MaxImized!");
		}
		if(windowState.isSet(Qt.WindowState.WindowFullScreen)) {
			System.out.println("Window is FullScreen!");
		}
		if (geometry == null)
			geometry = saveGeometry();
		this.settings.setValue("geometry", geometry);
		this.settings.setValue("state", saveState());
		this.settings.endGroup();
	}

	protected void closeEvent(QCloseEvent event) {
		whenClose();
		super.closeEvent(event);
	}
}
