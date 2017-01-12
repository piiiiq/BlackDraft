package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import yang.demo.swt.windowLocation;

public class rename extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected String result;
	protected Shell shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	private Text text;
	int style;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public rename(Shell parent, int style) {
		super(parent, SWT.None);
		this.style = style;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell.setSize(334, 121);
		shell.setText("\u547D\u540D\u6587\u4EF6");
		windowLocation.showWindowOnScreenCenter(shell);
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(10, 19, 48, 17);
		if(style == ((black)(shell.getParent())).ba.RENAME)
			label.setText("\u65B0\u540D\u79F0\uFF1A");
		else label.setText("名称：");
		
		text = new Text(shell, SWT.BORDER);
		text.setText(((black)getParent()).ba.getShowNameByTreeSelection());
		text.selectAll();
		text.setBounds(64, 16, 254, 23);
		text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.keyCode == 13){
					if(text.getText() != ""){
						result = text.getText();
						shell.dispose();
					}else{
						((black)shell.getParent()).ba.getMessageBox("", "名称不能为空！");
					}
				}
			}
		});
//		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		button.setBounds(238, 55, 80, 27);
		button.setText("\u53D6\u6D88");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText() != ""){
					result = text.getText();
					shell.dispose();
				}else{
					((black)shell.getParent()).ba.getMessageBox("", "名称不能为空！");
				}
			}
		});
		button_1.setText("\u786E\u5B9A");
		button_1.setBounds(152, 55, 80, 27);

	}
	public void setTitle(String text){
		shell.setText(text);
	}
}
