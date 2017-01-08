package yang.app.black;

import java.awt.event.KeyAdapter;
import java.io.Serializable;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;

import yang.demo.swt.windowLocation;

public abstract class bMessageBox extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	protected Shell shell;
	StyledText text;
	TextViewer tv;
	static boolean editable;
	boolean textChanged;
	ModifyListener ml = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent arg0) {
			// TODO Auto-generated method stub
			textChanged = true;
		}
	};

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public bMessageBox(Shell parent, int style, boolean editable) {
		super(parent, style);
		this.editable = editable;
		createContents();
	}
	
	public void readOnly(boolean readOnly){
		text.setEditable(readOnly);
//		if(readOnly){
//			text.setIME(null);
//		}
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM|SWT.MIN){
			public void dispose(){
				if(editable){
					if(textChanged)
						saveAction();
					
					super.dispose();
				}
				else super.dispose();
			}
			public void checkSubclass(){
				
			}
		};
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(450, 510);
		windowLocation.showWindowOnScreenCenter(shell);
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 431, 444, 50);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setBounds(354, 10, 80, 27);
		btnNewButton.setText("\u786E\u5B9A");
		btnNewButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		if(!editable){
			tv = new TextViewer(shell, SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			tv.getTextWidget().setIME(null);
		}
			
		else {
			tv = new TextViewer(shell, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			TextViewerUndoManager tvm = new TextViewerUndoManager(100);
			tv.setUndoManager(tvm);
			tv.activatePlugins();
			tv.setDocument(new Document());
			tv.getTextWidget().addKeyListener(new KeyListener() {
				
				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					if((arg0.keyCode|arg0.stateMask) == (SWT.CONTROL|'z')){
						tv.doOperation(TextViewer.UNDO);
					}else if((arg0.keyCode|arg0.stateMask) == (SWT.ALT|'z')){
						tv.doOperation(TextViewer.REDO);
					}
			
				}
				
				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		text = tv.getTextWidget();
		if(((black)getParent()) != null)
			text.setFont(((black)getParent()).msyh);
		else text.setFont(((black)getParent()).text.getFont());
		text.setLeftMargin(20);
		Menu m = new Menu(text);
		MenuItem mi = ((black)getParent()).ba.getMenuItem(m, "¸´ÖÆ	Ctrl+C", SWT.NONE);
		mi.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				text.copy();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		text.setMenu(m);
		text.setLineSpacing(5);
		text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text.setBounds(0, 0, 444, 431);
		text.setKeyBinding(SWT.CONTROL|'a', ST.SELECT_ALL);
		
	}
	public abstract void saveAction();

	public void setText(String str){
		text.removeModifyListener(ml);
		text.setText(str);
		text.addModifyListener(ml);
	}
	public void setTextFontinfo(int fontsize,int fontstyle){
		Font font = SWTResourceManager.getFont(text.getFont().getFontData()[0].getName(),fontsize,fontstyle);
		text.setFont(font);
	}
	public void setTitle(String str){
		shell.setText(str);
	}
	public String getText(){
		return text.getText();
	}
}
