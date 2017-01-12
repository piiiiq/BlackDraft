package yang.app.black;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import yang.demo.allPurpose.autoDO;
import yang.demo.swt.windowLocation;

import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public abstract class showProgress extends Dialog {

	protected Object result;
	protected Shell shell;
	private ProgressBar progressBar;
	private Label label;
	boolean progerChanged,messageChanged;
	black b;
	autoDO doing;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public showProgress(Shell parent) {
		super(parent, SWT.DIALOG_TRIM);
		b = (black)parent;
		shell = new Shell(getParent(), getStyle());
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		doing = new autoDO(1) {
			
			@Override
			public void action() {
				// TODO Auto-generated method stub
				shell.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(shell.isDisposed()){
							stop();
							return;
						}
						progressBar.setSelection(b.ba.ProgressValue);
						label.setText(b.ba.progressMessage);
						if(b.ba.ProgressValue == 100){
							actionInOtherThread();
							doing.stop();
							if(!shell.isDisposed()){
								progressBar.setSelection(b.ba.ProgressValue);
								label.setText(b.ba.progressMessage);
							}
							dispose();
						}
					}
				});
			}
		};
		doing.start();
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
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(450, 117);
		windowLocation.dialogLocation(getParent(), shell);
		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		progressBar.setBounds(10, 10, 424, 17);
		progressBar.setMaximum(100);
		
		label = new Label(shell, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setAlignment(SWT.CENTER);
		label.setBounds(10, 33, 424, 15);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				b.ba.progressMessage = "";
				b.ba.ProgressValue = 0;
				actionWhenOKButtonSelected();
				dispose();
			}
		});
		btnNewButton.setBounds(359, 53, 75, 25);
		btnNewButton.setText("\u786E\u5B9A");

	}
	abstract void actionInOtherThread();
	abstract void actionWhenOKButtonSelected();
	public void setTitle(String text){
		shell.setText(text);
	}
	public void setMessage(String text){
		label.setText(text);
	}
	public void dispose(){
		shell.dispose();
	}
}
