package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;

import yang.demo.swt.windowLocation;

public class logDialog extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	protected Shell shell;
	private StyledText styledText;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public logDialog(Shell parent, int style) {
		super(parent, SWT.CLOSE | SWT.APPLICATION_MODAL);
		createContents();
	}
	
	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		
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
		shell = new Shell(getParent(), SWT.CLOSE);
		shell.setSize(450, 300);
		shell.setText("\u65E5\u5FD7\u7A97\u53E3");
		windowLocation.dialogLocationOnLeftBottom(getParent(), shell);
		styledText = new StyledText(shell, SWT.READ_ONLY | SWT.WRAP|SWT.V_SCROLL);
		styledText.setAlwaysShowScrollBars(false);
		styledText.setBounds(0, 0, 444, 271);
		
	}
	
	
	public void appendLog(String text){
		styledText.append(text);
		styledText.invokeAction(ST.PAGE_DOWN);
	}
	public void hideWindow(){
		shell.setVisible(false);
	}

}
