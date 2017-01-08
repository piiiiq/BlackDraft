package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

import yang.demo.swt.windowLocation;

public class importFiles extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	protected Shell shell;
	boolean onlytext = true;
	String textencode = "gb2312";

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public importFiles(Shell parent, int style) {
		super(parent, style);
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
		shell = new Shell(getParent(), SWT.TITLE | SWT.SYSTEM_MODAL);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(450, 229);
		shell.setText("\u5BFC\u5165\u6587\u4EF6");
		windowLocation.dialogLocation((black)shell.getParent(), shell);
		
		Group grpDocdocx = new Group(shell, SWT.NONE);
		grpDocdocx.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpDocdocx.setText("doc/docx\u6587\u4EF6\u5BFC\u5165\u9009\u9879");
		grpDocdocx.setBounds(10, 10, 424, 74);
		
		final Button button = new Button(grpDocdocx, SWT.CHECK);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onlytext = button.getSelection();
			}
		});
		button.setSelection(true);
		button.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		button.setBounds(10, 29, 341, 17);
		button.setText("\u5FFD\u7565\u6587\u4EF6\u5185\u5BB9\u6837\u5F0F\uFF08\u53EA\u5BFC\u5165\u7EAF\u6587\u672C\u5185\u5BB9\uFF0C\u5BFC\u5165\u6210\u529F\u7387\u6700\u9AD8\uFF09");
		
		Group grpTxt = new Group(shell, SWT.NONE);
		grpTxt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpTxt.setText("txt\u6587\u4EF6\u5BFC\u5165\u9009\u9879");
		grpTxt.setBounds(10, 90, 424, 52);
		
		Label label = new Label(grpTxt, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setBounds(10, 22, 60, 17);
		label.setText("\u6587\u4EF6\u7F16\u7801\uFF1A");
		
		final Combo combo = new Combo(grpTxt, SWT.NONE);
		combo.setBounds(76, 17, 95, 25);
		combo.setItems(new String[] {"GB2312", "GBK", "UTF-8", "UTF-16", "ISO-8859-1", "US-ASCII", "UTF-16BE", "UTF-16LE", "windows-1252"});
		combo.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		combo.select(0);
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				textencode = combo.getItem(combo.getSelectionIndex());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button btnQuxc = new Button(shell, SWT.NONE);
		btnQuxc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnQuxc.setBounds(373, 163, 61, 27);
		btnQuxc.setText("\u53D6\u6D88");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				((black)(shell.getParent())).ba.importFiles(onlytext, textencode);
			}
		});
		button_1.setText("导入（支持black/txt/doc/docx文件）");
		button_1.setBounds(150, 163, 220, 27);
	}
}
