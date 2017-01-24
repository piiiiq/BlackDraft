package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.swt.animateWindows;
import yang.demo.swt.windowLocation;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.StyledText;

public class aboutBlack extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	protected Shell shell;
	private StyledText txtBlackEditor;
	private black black;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public aboutBlack(Shell parent, int style) {
		super(parent, SWT.APPLICATION_MODAL);
		this.black = (black) parent;
		setText("关于Black");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				closeThis();
			}
		});
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(516, 332);
		shell.setText(getText());
		
		txtBlackEditor = new StyledText(shell, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txtBlackEditor.getCaret().dispose();
		txtBlackEditor.setLineSpacing(3);
		txtBlackEditor.setLeftMargin(10);
		txtBlackEditor.setIME(null);
		txtBlackEditor.setText(allInfo.aboutblack);
		txtBlackEditor.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		txtBlackEditor.setEditable(false);
		txtBlackEditor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtBlackEditor.setFont(SWTResourceManager.getFont("微软雅黑", 9, SWT.NORMAL));
		txtBlackEditor.setBounds(0, 0, 510, 216);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeThis();
			}
		});
		btnOk.setBounds(420, 266, 80, 27);
		btnOk.setText("\u786E\u5B9A");
		
		Label label = new Label(shell, SWT.WRAP);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setBounds(10, 237, 490, 56);
		label.setText(black.codeName+
					"\nBlack版本： " +
					yang.app.black.black.getAppVersion() +
					"\n生成日期： " +
					yang.app.black.black.getBuildDate());
		
		showThis();
	}
	public void showThis()
	{
		windowLocation.dialogLocation(black, shell);
	}
	public void closeThis()
	{
		shell.dispose();
	}
}
