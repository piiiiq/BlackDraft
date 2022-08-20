package yang.app.qt.black;

import io.qt.core.QEvent;
import io.qt.core.QObject;
import io.qt.core.Qt;
import io.qt.widgets.QAction;
import io.qt.widgets.QBoxLayout;
import io.qt.gui.QKeyEvent;
import io.qt.widgets.QLabel;
import io.qt.widgets.QLineEdit;
import io.qt.widgets.QToolButton;
import io.qt.widgets.QWidget;
import io.qt.widgets.QBoxLayout.Direction;
import io.qt.widgets.QLineEdit.EchoMode;
import io.qt.widgets.QDialog;

public abstract class InputDialog extends QDialog{
	private QLineEdit line;
	private QToolButton button_ok;
	private QToolButton button_can;
	boolean noHide;

	/**
	 * 初始化后需要调用exec方法显示,点击确定返回1，点击取消返回0
	 * 输入框也可以用来显示能够复制的信息，显示信息时应设置成只读模式
	 * 如果想要点击按钮后不自动关闭对话框，请将noHide开关设为true
	 * value方法获取输入值
	 * @param title
	 * @param lineEditKeyName
	 */
	public InputDialog(QWidget parent,String title,String lineEditKeyName,boolean passwordMode,boolean onlyReadMode,String InputLineValue) {
		super(parent);
//		black.speakText(lineEditKeyName, -1);

		setWindowTitle(title);
		QBoxLayout topBox = new QBoxLayout(Direction.TopToBottom);
		setLayout(topBox);
		QBoxLayout box = new QBoxLayout(Direction.RightToLeft);
		
		topBox.insertLayout(0, box);
		QBoxLayout buttons = new QBoxLayout(Direction.LeftToRight);
		buttons.setContentsMargins(0, 10, 0, 0);
		topBox.insertLayout(1, buttons, 0);
		button_ok = new QToolButton();
		button_ok.setText("确定");
		button_ok.clicked.connect(this,"ok()");
		buttons.addWidget(button_ok);
		button_can = new QToolButton();
		button_can.clicked.connect(this,"can()");
		button_can.setText("取消");
		buttons.addWidget(button_can);
		buttons.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignRight));
		line = new QLineEdit(this);
		if(InputLineValue != null) line.setText(InputLineValue);
		line.setReadOnly(onlyReadMode);
		line.setFocus();
		if(passwordMode)
		line.setEchoMode(EchoMode.Password);
		line.installEventFilter(this);
//		QAction enter_action = new QAction(line);
//		enter_action.setShortcut("enter");
//		enter_action.triggered.connect(this,"enter()");
//		line.addAction(enter_action);
		box.addWidget(line);
		QLabel label = new QLabel();
		label.setText(lineEditKeyName);
		box.addWidget(label);
		adjustSize();
		
	}
	abstract void whenOkButtonPressed();
	abstract void whenCannelButtonPressed();

	void setLineText(String text) {
		line.setText(text);
	}
	void setOkButtonName(String name) {
		button_ok.setText(name);
	}
	void setCannelButtonName(String name) {
		button_can.setText(name);
	}
	String copyLineText() {
		line.selectAll();
		line.copy();
		return line.text();
	}
	void ok() {
		whenOkButtonPressed();
		if(noHide)return;
		if(!line.text().isEmpty())
		done(1);
	}
	void can() {
		whenCannelButtonPressed();
		if(noHide)return;
		done(0);
	}
	public String value() {
		return line.text();
	}
	public boolean eventFilter(QObject arg__1, QEvent arg__2) {
		// TODO Auto-generated method stub
		if(arg__2.type()==QEvent.Type.KeyPress) {
			QKeyEvent key = (QKeyEvent) arg__2;
//			System.out.println(key.key()+" "+Qt.Key.Key_Enter.value());

			if(key.key() == 16777220) {
				ok();
				return true;
			}
		}else if(arg__2.type() == QEvent.Type.Hide) {
			whenClose();
		}
		return super.eventFilter(arg__1, arg__2);
	}
	abstract void whenClose();
}
