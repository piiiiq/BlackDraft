package yang.app.black;

import java.io.File;
import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.swt.animateWindows;
import yang.demo.swt.windowLocation;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Scale;

public class settings extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	protected Shell shell;
	public black black;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public settings(Shell parent, int style) {
		super(parent, SWT.APPLICATION_MODAL);
		black = (black)parent;
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
		shell.setText("\u9996\u9009\u9879");
		shell.setSize(420, 419);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setLayout(new FormLayout());
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.top = new FormAttachment(0);
		fd_tabFolder.left = new FormAttachment(0);
		fd_tabFolder.bottom = new FormAttachment(0, 390);
		fd_tabFolder.right = new FormAttachment(0, 414);
		tabFolder.setLayoutData(fd_tabFolder);
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		
		tabItem.setText("\u7F16\u8F91\u5668\u8BBE\u7F6E");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tabItem.setControl(composite_1);
		Label label = new Label(composite_1, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setBounds(10, 13, 61, 17);
		label.setText("\u9996\u884C\u7F29\u8FDB\uFF1A");
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("\u884C\u8DDD\uFF1A");
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setBounds(162, 13, 36, 17);
		
		final Spinner spinner = new Spinner(composite_1, SWT.BORDER);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.setEditerIndent(spinner.getSelection());
			}
		});
		spinner.setBounds(77, 10, 47, 23);
		spinner.setSelection(black.getEditerIndent());
		
		final Spinner spinner_1 = new Spinner(composite_1, SWT.BORDER);
		spinner_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.setEditerLineSpace(spinner_1.getSelection());
			}
		});
		spinner_1.setBounds(202, 10, 47, 23);
		spinner_1.setSelection(black.getEditerLineSpace());
		final Combo combo = new Combo(composite_1, SWT.READ_ONLY);
		combo.setBounds(112, 90, 128, 25);
		combo.setBackgroundMode(SWT.INHERIT_FORCE);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.setEditorFont(combo.getItem(combo.getSelectionIndex()));
			}
		});
		combo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		combo.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		String[] fonts = black.ba.getALLFonts();
		combo.setItems(fonts);
		String font = black.getEditorDefaultFontFromCFG();
		if(font != null){
			for(int i=0;i<fonts.length;i++){
				if(fonts[i].equals(font)) {
					combo.select(i);
					break;
				}
			}
		}
		
		Label lblNewLabel = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		lblNewLabel.setLayoutData(new FormData());
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel.setBounds(10, 36, 380, 15);
		lblNewLabel.setText("New Label");
		
		Link link = new Link(composite_1, 0);
		link.setText("\u7F16\u8F91\u5668\u9ED8\u8BA4\u5B57\u4F53\uFF1A");
		link.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		link.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		link.setBounds(10, 93, 96, 17);
		
		Link link_1 = new Link(composite_1, 0);
		link_1.setText("\u7F16\u8F91\u5668\u7F29\u653E\uFF1A");
		link_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		link_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		link_1.setBounds(246, 94, 80, 17);
		
		Label label1 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		label1.setText("New Label");
		label1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label1.setBounds(10, 73, 380, 11);
		
		final Combo combo1 = new Combo(composite_1, SWT.READ_ONLY);
		combo1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		combo1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				(black).setEditorZoom(Integer.valueOf(combo1.getItem(combo1.getSelectionIndex())));
			}
		});
		for(int i=100; i<= 400; i+=10){
			combo1.add(String.valueOf(i));
		}
		combo1.select((Integer.valueOf((black).getEditorZoomFromCFG())-100)/10);
		combo1.setBounds(332, 90, 61, 25);
		
		TabItem tabItem_2 = new TabItem(tabFolder, SWT.NONE);
		tabItem_2.setText("\u4FDD\u5B58\u9009\u9879");
		final Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tabItem_2.setControl(composite_3);
		composite_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		composite_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_3.setLayout(null);		
		Group group_2 = new Group(composite_3, SWT.NONE);
		group_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_2.setText("\u81EA\u52A8\u4FDD\u5B58");
		group_2.setBounds(10, 10, 386, 67);
		
		final Link link_4 = new Link(group_2, 0);
		link_4.setBounds(111, 36, 200, 17);
		link_4.setText("\u6587\u6863\u66F4\u6539\u8D85\u8FC7\u6B64\u65F6\u95F4\u540E\u81EA\u52A8\u4FDD\u5B58(\u79D2)\uFF1A");
		link_4.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		link_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		final Spinner spinner1 = new Spinner(group_2, SWT.BORDER);
		spinner1.setMinimum(2);
		spinner1.setMaximum(3600);
		spinner1.setSelection(300);
		spinner1.setSelection(Integer.valueOf(black.appProperties.getProperty("AutoSave", "1")));
		spinner1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.setAutoSave(spinner1.getSelection());
			}
		});
		spinner1.setBounds(317, 34, 59, 23);
		final Button button1 = new Button(group_2, SWT.CHECK);
		button1.setToolTipText("\u5373\u4F7F\u5173\u95ED\u81EA\u52A8\u4FDD\u5B58\uFF0C\u7A0B\u5E8F\u4E5F\u4F1A\u5728\u5207\u6362\u6587\u6863\u548C\u9000\u51FA\u7A0B\u5E8F\u65F6\u4FDD\u5B58\u7528\u6237\u5BF9\u6587\u6863\u6240\u505A\u7684\u66F4\u6539");
		button1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				link_4.setEnabled(button1.getSelection());
				spinner1.setEnabled(button1.getSelection());
				if(button1.getSelection() == false){
					black.setAutoSave(-1);
					black.appProperties.setProperty("AutoSave", "-1");
				}else{
					black.setAutoSave(1);
					black.appProperties.setProperty("AutoSave", "1");
					spinner1.setSelection(1);
				}
			}
		});
		button1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		button1.setBounds(12, 36, 93, 17);
		button1.setText("\u542F\u7528\u81EA\u52A8\u4FDD\u5B58");
		
		final Spinner spinner11 = new Spinner(composite_3, SWT.BORDER);
		spinner11.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.setEditerIndent(spinner11.getSelection());
			}
		});
		spinner11.setBounds(77, 10, 47, 23);
		spinner11.setSelection(black.getEditerIndent());
		
		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("\u9605\u8BFB\u89C6\u56FE");
		if(black.appProperties.getProperty("AutoSave", "1").equals("-1")){
			link_4.setEnabled(false);
			spinner1.setEnabled(false);
		}
		if(!black.appProperties.getProperty("AutoSave", "1").equals("-1"))
			button1.setSelection(true);
		else button1.setSelection(false);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("\u5199\u4F5C\u89C6\u56FE");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(composite);
		
		Group group = new Group(composite, SWT.NONE);
		group.setText("\u7F16\u8F91\u5668\u5BBD\u5EA6");
		group.setBounds(10, 10, 386, 204);
		
		final Scale scale = new Scale(group, SWT.NONE);
		scale.setMaximum(windowLocation.returnScreenWidth());
		scale.setSelection(Integer.valueOf(black.appProperties.getProperty("WritingView_TextX", String.valueOf(windowLocation.returnScreenWidth()-
				windowLocation.returnScreenWidth()/4))));
		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				black.ba.changeWritingViewTextWidth(scale.getSelection());
			}
		});
		scale.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scale.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scale.setBounds(10, 32, 366, 42);
		
		final Scale scale1 = new Scale(group, SWT.NONE);
		scale1.setMaximum(windowLocation.returnScreenWidth());
		if(black.wv != null && !black.wv.isDisposed())
			scale1.setSelection(black.wv.getSize().x);
		
		scale1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(black.wv != null && !black.wv.isDisposed()){
					int h = black.wv.getSize().y;
					int x = (windowLocation.returnScreenWidth() - black.wv.getSize().x)/2;
					black.wv.setBounds(x, 0, scale1.getSelection(), h);
				}
			}
		});
		scale1.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scale1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scale1.setBounds(10, 92, 366, 42);
		showThis();
	}
	
	public void showThis()
	{
		animateWindows.showWindow(black,shell);
	}
	public void closeThis()
	{
		shell.dispose();
	}
}
