package yang.app.black;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import yang.demo.swt.windowLocation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class fortest extends Dialog {

	protected Shell shell;
	private Text text;
	boolean isOK;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public fortest(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return isOK;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle()){
			public void dispose(){
				if(isOK) super.dispose();
				else System.exit(0);
			}
			public void checkSubclass(){
				
			}
		};
		shell.setSize(266, 111);
		windowLocation.dialogLocation(getParent(), shell);
		shell.setText("\u9A8C\u8BC1");
		
		text = new Text(shell, SWT.BORDER | SWT.CENTER|SWT.PASSWORD);
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13)
					isOK();
				if((e.keyCode|e.stateMask) == (SWT.CONTROL|SWT.ALT|SWT.SHIFT|'b')){
					isOK = true;
					shell.dispose();
				}
			}
		});
		text.setMessage("\u952E\u5165\u6D4B\u8BD5\u7801");
		text.setBounds(10, 10, 238, 23);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		button.setBounds(170, 49, 80, 27);
		button.setText("\u53D6\u6D88");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isOK();
			}
		});
		button_1.setBounds(84, 49, 80, 27);
		button_1.setText("\u786E\u5B9A");

	}
	public void isOK(){
		String password = text.getText();
		if(password.equals(((black)getParent()).getBuildDate())) {
			isOK = true;
			shell.dispose();
		}else ((black)getParent()).ba.getMessageBox("启动信息", "不可用的测试码");
		
	}
}
