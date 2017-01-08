package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.swt.windowLocation;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.ModifyEvent;

public class findWord extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	public Shell shell;
	private Text text;
	private Text text_1;
	black black;
	boolean forwardSearch = true, caseSensitive, wholeWord, showAll,regularExpressions;
	private Button button_1;
	private Button button_3;
	private Combo combo;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public findWord(Shell parent, int style) {
		super(parent, style);
		this.black = (black)parent;
		setText("查找/替换");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		black.ba.findinfoResult = null;
		createContents();
		windowLocation.dialogLocationOnLeftBottom(black, shell);
		
		shell.addShellListener(new ShellListener() {
			
			@Override
			public void shellIconified(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellDeiconified(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellDeactivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				shell.setAlpha(100);
			}
			
			@Override
			public void shellClosed(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				shell.setAlpha(255);
			}
		});
		Label label = new Label(shell, SWT.NONE);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		label.setBounds(10, 13, 84, 17);
		label.setText("\u8981\u67E5\u627E\u7684\u5185\u5BB9\uFF1A");
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				replace();
			}
		});
		button.setBounds(96, 257, 80, 27);
		button.setText("\u5168\u90E8\u66FF\u6362");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		label_1.setBounds(10, 42, 84, 17);
		label_1.setText("\u66FF\u6362\u4E3A\uFF1A");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(100, 39, 152, 23);
		text_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13){
					if(black.getEditer() != null){
						black.blackTextArea.findWord(black.tv.getDocument(),text.getText(), forwardSearch, caseSensitive, wholeWord, showAll,regularExpressions);
					}
				}
			}
		});
		
		Group group = new Group(shell, SWT.NONE);
		group.setText("\u65B9\u5411");
		group.setBounds(10, 75, 118, 72);
		
		button_1 = new Button(group, SWT.RADIO);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				forwardSearch = button_1.getSelection();
			}
		});
		button_1.setBounds(10, 21, 80, 17);
		button_1.setText("\u5411\u524D\u67E5\u627E");
		
		final Button button_2 = new Button(group, SWT.RADIO);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		button_2.setSelection(true);
		button_2.setText("\u5411\u540E\u67E5\u627E");
		button_2.setBounds(10, 44, 80, 17);
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText("\u67E5\u770B");
		group_1.setBounds(134, 75, 118, 72);
		
		button_3 = new Button(group_1, SWT.RADIO);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showAll = button_3.getSelection();
				black.blackTextArea.clearFindHistory();
			}
		});
		button_3.setText("\u6240\u6709");
		button_3.setBounds(10, 21, 80, 17);
		
		Button button_4 = new Button(group_1, SWT.RADIO);
		button_4.setSelection(true);
		button_4.setText("\u5355\u9879");
		button_4.setBounds(10, 44, 80, 17);
		
		Group group_2 = new Group(shell, SWT.NONE);
		group_2.setText("\u9009\u9879");
		group_2.setBounds(10, 153, 242, 98);
		
		final Button button_5 = new Button(group_2, SWT.CHECK);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				caseSensitive = button_5.getSelection();
			}
		});
		button_5.setText("\u533A\u5206\u5927\u5C0F\u5199");
		button_5.setBounds(10, 20, 112, 17);
		
		Label label_2 = new Label(group_2, SWT.NONE);
		label_2.setBounds(10, 73, 89, 17);
		label_2.setText("\u67E5\u627E/\u66FF\u6362\u8303\u56F4\uFF1A");
		
		combo = new Combo(group_2, SWT.NONE);
		combo.setItems(new String[] {"\u4EC5\u5F53\u524D\u6587\u4EF6", "\u9879\u76EE\u4E2D\u7684\u6240\u6709\u6587\u4EF6"});
		combo.setBounds(105, 70, 127, 25);
		combo.select(0);
		
		final Button button_6 = new Button(group_2, SWT.CHECK);
		button_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wholeWord = button_6.getSelection();
			}
		});
		button_6.setText("\u5168\u5B57\u5339\u914D");
		button_6.setBounds(128, 20, 104, 17);
		
		final Button button_8 = new Button(group_2, SWT.CHECK);
		button_8.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				regularExpressions = button_8.getSelection();
			}
		});
		button_8.setText("\u5C06\u5173\u952E\u5B57\u4F5C\u4E3A\u6B63\u5219\u8868\u8FBE\u5F0F\u89E3\u6790");
		button_8.setBounds(10, 45, 222, 17);
		
		Button button_7 = new Button(shell, SWT.NONE);
		button_7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		button_7.setText("\u5173\u95ED");
		button_7.setBounds(182, 257, 70, 27);
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
		shell.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				// TODO Auto-generated method stub
				black.blackTextArea.wordIndex = 0;
				black.ba.hideFindResult();
			}
		});
		shell.setText("\u67E5\u627E/\u66FF\u6362");
		shell.setImage(SWTResourceManager.getImage(findWord.class, "/yang/app/black/res/icon/find-article16.png"));
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.setSize(267, 320);
		
		text = new Text(shell, SWT.BORDER);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				black.blackTextArea.clearFindHistory();
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == 13){
					find();
				}
			}
		});
		text.setBounds(100, 10, 152, 23);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				find();
			}
		});
		button.setBounds(10, 257, 80, 27);
		button.setText("\u67E5\u627E");

	}
	void find(){
		if(black.getEditer() != null){
			if(combo.getSelectionIndex() == 0)
				black.blackTextArea.findWord(black.tv.getDocument(),text.getText(), forwardSearch, caseSensitive, wholeWord, showAll,regularExpressions);
			else black.ba.findInAllFiles(text.getText(), forwardSearch, caseSensitive, wholeWord, showAll,regularExpressions);
		}
	}
	void replace(){
		if(combo.getSelectionIndex() == 0){
			if(!text.getText().equals(text_1.getText()))
				black.blackTextArea.replace(black.tv.getDocument(),text.getText(), text_1.getText(), forwardSearch, caseSensitive, wholeWord,true,regularExpressions);
			else black.ba.getMessageBox("查找/替换消息", "新字符不能和被替换的字符相同");
		}else{
			if(!text.getText().equals(text_1.getText()))
				black.ba.replaceInAllFile(text.getText(), text_1.getText(), forwardSearch, caseSensitive, wholeWord,regularExpressions);
			else black.ba.getMessageBox("查找/替换消息", "新字符不能和被替换的字符相同");
		}
			
	}
}
