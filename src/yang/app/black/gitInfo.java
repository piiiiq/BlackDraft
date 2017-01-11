package yang.app.black;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.swt.windowLocation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class gitInfo extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text path;
	private Text username;
	private Text password;
	black b;
	private Button remember;
	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public gitInfo(Shell parent, int style) {
		super(parent, style);
		b = (black)parent;
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
		shell = new Shell(getParent(), getStyle());
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(495, 234);
		shell.setText("\u914D\u7F6EGit");
		windowLocation.dialogLocation(getParent(), shell);
		String host_ = b.projectProperties.getProperty("GitHost");
		String username_ = b.projectProperties.getProperty("GitUsername");
		String password_ = b.projectProperties.getProperty("GitPassword");
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_1.setText("\u4ED3\u5E93\u4F4D\u7F6E");
		group_1.setBounds(10, 10, 469, 60);
		
		path = new Text(group_1, SWT.BORDER);
		path.setBounds(138, 29, 321, 21);
		if(path != null)
			path.setText(host_);
		
		Label lblGithttps = new Label(group_1, SWT.NONE);
		lblGithttps.setBounds(10, 32, 122, 15);
		lblGithttps.setAlignment(SWT.CENTER);
		lblGithttps.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblGithttps.setText("Git\u4ED3\u5E93\u8DEF\u5F84(https):");
		
		Group group = new Group(shell, SWT.NONE);
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group.setText("\u7528\u6237\u51ED\u636E");
		group.setBounds(10, 76, 469, 90);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(10, 28, 105, 15);
		label.setAlignment(SWT.CENTER);
		label.setText("\u7528\u6237\u540D:");
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(10, 55, 105, 15);
		label_1.setAlignment(SWT.CENTER);
		label_1.setText("\u5BC6\u7801:");
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		username = new Text(group, SWT.BORDER);
		username.setBounds(121, 24, 338, 21);
		if(username_ != null)
			username.setText(username_);
		
		password = new Text(group, SWT.PASSWORD|SWT.BORDER);
		password.setBounds(121, 51, 338, 21);
		if(password_ != null)
			password.setText(password_);
		
		Button ok = new Button(shell, SWT.NONE);
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(remember.getSelection()){
					if(path!= null && !path.getText().equals("")){
						if(username != null && !username.equals("")){
							if(password != null && !password.equals("")){
								b.ba.saveGitInfo(path.getText(), username.getText(), password.getText());
								b.ba.setGitRespositoryPath();
								b.ba.gitWorking();
							}
						}
					}
				}else{
					b.ba.setGitRespositoryPath();
					b.ba.gitWorking();
				}			
			}
		});
		ok.setBounds(337, 172, 142, 25);
		ok.setText("\u8FDE\u63A5\u4ED3\u5E93\u5E76\u4E0A\u4F20\u9879\u76EE");
		
		remember = new Button(shell, SWT.CHECK);
		remember.setBounds(10, 172, 75, 25);
		remember.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		remember.setText("\u8BB0\u4F4F\u51ED\u636E");
		if(host_ != null && username != null && password != null)
			remember.setSelection(true);

	}
}
