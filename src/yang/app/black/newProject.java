package yang.app.black;


import java.io.File;
import java.io.Serializable;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import yang.demo.swt.windowLocation;

public class newProject extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected boolean result;
	protected Shell shell;
	private Text text;
	black b;
	private Button button_1;
	private Button button_2;
	private Text text_1;
	String saveDir;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public newProject(black b, int style) {
		super(b, style);
		this.b = b;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.layout();
		shell.setVisible(true);
		//shell.open();

		button_1.setFocus();
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(414, 228);
		shell.setText("\u65B0\u5EFA\u9879\u76EE");
		windowLocation.showWindowOnScreenCenter(shell);
		Label label = new Label(shell, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		label.setBounds(10, 10, 388, 48);
		label.setText("\u65B0\u5EFA\u4E00\u4E2A\u9879\u76EE\uFF0C\u4EE5\u4FBF\u7528\u4E8E\u64B0\u5199\u548C\u7BA1\u7406\u5C0F\u8BF4\u3001\u5267\u672C\u8349\u7A3F\r\n\uFF08\u6CE8\u610F\uFF1A\u4E3A\u4E86\u6570\u636E\u5B89\u5168\u8D77\u89C1\uFF0C\u8BF7\u4E0D\u8981\u5C06\u9879\u76EE\u4FDD\u5B58\u5728\u7CFB\u7EDF\u5206\u533A\uFF09");
		
		text = new Text(shell, SWT.BORDER | SWT.CENTER);
		text.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		text.setMessage("键入此草稿项目的名称");
		text.setBounds(10, 64, 388, 23);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String dir = new DirectoryDialog(b).open();
				if(dir != null)
					text_1.setText(dir);
				
			}
		});
		button.setBounds(273, 93, 125, 27);
		button.setText("\u9009\u62E9\u9879\u76EE\u7684\u4FDD\u5B58\u4F4D\u7F6E");
		
		button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(b,SWT.OPEN);
				fd.setFilterExtensions(new String[]{"*.bpro"});
				String file = fd.open();
				if(file != null){
					b.ba.openProject(new File(file));
					result = true;
					shell.dispose();
				}
			}
		});
		button_1.setBounds(273, 159, 125, 27);
		button_1.setText("\u5DF2\u6709\u9879\u76EE\uFF1F\u6253\u5F00\u5B83");
		
		button_2 = new Button(shell, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText().length() > 0){
					if(text_1.getText().length() > 0){
						File f = new File(text_1.getText()+"\\"+text.getText());
						if(f.exists()){
							b.ba.getMessageBox("", "该名称已被使用，请指定其他名称");
						}else{
							b.ba.createProject(text.getText(), text_1.getText());
							result = true;
							shell.dispose();
						}
					}
					else b.ba.getMessageBox("", "请选择项目保存位置");
				}else b.ba.getMessageBox("", "请填写项目名称");
			}
		});
		button_2.setBounds(318, 126, 80, 27);
		button_2.setText("\u521B\u5EFA\u9879\u76EE");
		
		text_1 = new Text(shell, SWT.WRAP | SWT.MULTI);
		text_1.setEditable(false);
		text_1.setEnabled(false);
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_1.setBounds(10, 98, 257, 55);

	}
}
