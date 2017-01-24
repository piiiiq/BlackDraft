package yang.app.black;


import java.io.Serializable;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.allPurpose.debug;
import yang.demo.swt.windowLocation;

public class findinfo extends Shell implements Serializable{
	static final long serialVersionUID = 42L;
	boolean findInMark;
	black b;
	Tree tree;
	static int text_oldKeyArrowUpAction,text_oldKeyArrowDownAction,text_oldKeyEnterAction,text_oldAltEnterAction,text_oldAltAction
	,oldkeyone,oldkeytwo;
	checkKey keyUp, keyDown, keyEnter, keyAltEnter,keyone,keytwo,del;
	MouseListener ml;
	KeyListener kl;
	CaretListener cl;
	StyleRange sr;
	final static int replace = 0,
			readonly = 1,
			none = 3;
	int insertAction;
	private logShell shell;
	private Composite composite;
	String drawstr;
	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public findinfo(Shell shell, final black b, int style) {
		super(shell, SWT.RESIZE);
		this.b = b;
		createContents();
		drawstrAction(insertAction);
		
		if(b.text != null){
			text_oldKeyArrowUpAction = b.text.getKeyBinding(SWT.ARROW_UP);
			text_oldKeyArrowDownAction = b.text.getKeyBinding(SWT.ARROW_DOWN);
			text_oldKeyEnterAction = b.text.getKeyBinding(13);
			text_oldAltEnterAction = b.text.getKeyBinding(SWT.ALT|13);
			text_oldAltAction = b.text.getKeyBinding(SWT.ALT);
			oldkeyone = b.text.getKeyBinding(49);
			oldkeytwo = b.text.getKeyBinding(50);
			
			b.text.setKeyBinding(SWT.ARROW_UP, -1);
			b.text.setKeyBinding(SWT.ARROW_DOWN, -1);
			b.blackTextArea.removeKeyAction(b.blackTextArea.ck_enter);
			b.text.setKeyBinding(SWT.ALT|13, -1);
			b.text.setKeyBinding(SWT.ALT, -1);
			b.blackTextArea.removeKeyAction(b.blackTextArea.keyenter);
			b.text.setKeyBinding(49, -1);
			b.text.setKeyBinding(50, -1);
			
			del = new checkKey(127) {
				
				@Override
				public void action() {
					// TODO Auto-generated method stub
					if(tree.getSelectionCount() == 1){
						TreeItem[] selection = tree.getSelection();
						TextRegion tr = (TextRegion)selection[0].getData("textregion");
						String text = tr.text;
						b.ba.deleteFromMarkStatData(text);
						selection[0].setText(selection[0].getText()+"[已移除]");
					}
				}
			};
			b.blackTextArea.addKeyAction(del);
			
			keyUp = new checkKey(SWT.ARROW_UP) {
				
				@Override
				public void action() {
					// TODO Auto-generated method stub
					if(tree.getSelectionCount() > 0){
						int index = (int)tree.getSelection()[0].getData("index");
						if(index-1 >= 0){
							tree.setSelection(tree.getItem(index-1));
						}else{
							tree.setSelection(tree.getItem(tree.getItemCount()-1));
						}
					}
				}
			};
			b.blackTextArea.addKeyAction(keyUp);
			keyDown = new checkKey(SWT.ARROW_DOWN) {
				@Override
				public void action() {
					// TODO Auto-generated method stub
					if(tree.getSelectionCount() > 0){
						int index = (int)tree.getSelection()[0].getData("index");
						if(index+1 < tree.getItemCount()){
							tree.setSelection(tree.getItem(index+1));
						}else {
							tree.setSelection(tree.getItem(0));
						}
					}
				}
			};
			
			b.blackTextArea.addKeyAction(keyDown);
			keyEnter = new checkKey(13) {
				
				@Override
				public void action() {
					// TODO Auto-generated method stub
					insert();
				}
			};
			b.blackTextArea.addKeyAction(keyEnter);
			keyAltEnter = new checkKey(SWT.ALT|13) {
				
				@Override
				public void action() {
					// TODO Auto-generated method stub
					insert();
				}
			};
			b.blackTextArea.addKeyAction(keyAltEnter);
			keyone = new checkKey(49) {
				
				@Override
				public void action() {
					// TODO Auto-generated method stub
					String text = ((TextRegion)tree.getSelection()[0].getData("textregion")).text;
					int index = 0;
					if((index = text.indexOf("·")) != -1){
						String lastname = text.substring(0, index);
						tree.getSelection()[0].setData("textOfinsert", lastname);
						insert();
					}
				}
			};
			b.blackTextArea.addKeyAction(keyone);
			keytwo = new checkKey(50) {
				
				@Override
				public void action() {
					// TODO Auto-generated method stub
					String text = ((TextRegion)tree.getSelection()[0].getData("textregion")).text;
					int index = 0;
					if((index = text.lastIndexOf("·")) != -1){
						if(index+1 < text.length()){
							String firstname = text.substring(index+1, text.length());
							tree.getSelection()[0].setData("textOfinsert", firstname);
							insert();
						}else return;
						
					}
				}
			};
			b.blackTextArea.addKeyAction(keytwo);
			ml = new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					// TODO Auto-generated method stub
					dispose();
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			};
			b.text.addMouseListener(ml);
			cl = new CaretListener() {
				
				@Override
				public void caretMoved(CaretEvent event) {
					// TODO Auto-generated method stub
					b.ba.closeFindInfoDialog();	
				}
			};
			b.text.addCaretListener(cl);
			kl = new KeyListener() {
				
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					if(e.keyCode == SWT.ALT){
						//changeInsertAction();
						insertAction = none;
						drawstrAction(insertAction);
					}
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					if(e.keyCode == SWT.ALT){
						//changeInsertAction();
						insertAction = replace;
						drawstrAction(insertAction);
					}
				}
			};
			b.text.addKeyListener(kl);
		}
		
	}
	public void setVisible(boolean visible){
		if(tree.getItemCount() > 0){
			super.setVisible(true);
			tree.setSelection(tree.getItem(0));
			tree.select(tree.getItem(0));
		}
	}
	public void dispose(){
		if(!isDisposed()){
			if(b.text != null){
				b.text.setKeyBinding(SWT.ARROW_UP, text_oldKeyArrowUpAction);
				b.text.setKeyBinding(SWT.ARROW_DOWN, text_oldKeyArrowDownAction);
				b.text.setKeyBinding(SWT.SHIFT|13, text_oldAltEnterAction);
				b.text.setKeyBinding(SWT.SHIFT, text_oldAltAction);
				b.text.setKeyBinding(49, oldkeyone);
				b.text.setKeyBinding(50, oldkeytwo);
				b.blackTextArea.addKeyAction(b.blackTextArea.ck_enter);
				b.blackTextArea.removeKeyAction(keyUp);
				b.blackTextArea.removeKeyAction(keyDown);
				b.blackTextArea.removeKeyAction(keyEnter);
				b.blackTextArea.removeKeyAction(keyAltEnter);
				b.blackTextArea.removeKeyAction(keyone);
				b.blackTextArea.removeKeyAction(keytwo);
				b.blackTextArea.removeKeyAction(del);
				b.text.removeMouseListener(ml);
				b.text.removeCaretListener(cl);
				b.text.removeKeyListener(kl);
				b.blackTextArea.addKeyAction(b.blackTextArea.keyenter);
			}
			b.ba.saveWindowSizeToAppProperties(this);
			super.dispose();
			b.ba.findi = null;
		}
	}
	
	private void createContents() {
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		b.ba.readWindowSizeFromAppProperties(this, new Point(450, 143));
		addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {
				// TODO Auto-generated method stub
				composite.setBounds(-10, getSize().y-17-13, getSize().x,17);
				tree.setBounds(-15, 0, getSize().x, getSize().y-composite.getSize().y-14);
				
			}
			
			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		Point p = b.getDisplay().map(b.text, null, b.text.getCaret().getLocation());
		b.text.getCaret().setData(((mycaret)b.text.getCaret()).getLineHeightAtCaretOffset());
		windowLocation.windowAlwaysInScreen(p, (Shell)getParent(),this,b.text.getCaret());
		
		tree = new Tree(this, SWT.NONE);
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(0, 229);
		fd_tree.right = new FormAttachment(0, 436);
		fd_tree.top = new FormAttachment(0);
		fd_tree.left = new FormAttachment(0);
		tree.setLayoutData(fd_tree);
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tree.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		composite = new Composite(this, SWT.BORDER);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setBounds(0, 212, 436, 17);
		composite.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				changeInsertAction();
				drawstrAction(insertAction);
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		composite.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				if(drawstr != null){
					TextLayout tl = new TextLayout(getDisplay());
					Font font = SWTResourceManager.getFont("微软雅黑", 8, SWT.NONE);
					if(font != null)
						tl.setFont(font);
					
					tl.setText(drawstr);
					tl.draw(e.gc, composite.getSize().x-tl.getLineBounds(0).width-10, -2);
					tl.dispose();
				}
			}
		});
		
		tree.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
				switch(e.keyCode){
				case 13:
					insert();
				}
			}
		});
		tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				showMore();
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				insert();
			}
		});
		
	}
	public void changeInsertAction(){
		if(insertAction == replace) insertAction = none;
		else if(insertAction == none) insertAction = replace;
	}
	public void showMore(){
		if(tree.getSelectionCount() == 1 && tree.getHorizontalBar().isVisible()){
			TextRegion tr = ((TextRegion)tree.getSelection()[0].getData("textregion"));
			if(shell == null)
				shell = new logShell(this);
			if(sr == null) {
				sr = new StyleRange();
				sr.fontStyle = SWT.BOLD;
			}
			shell.setLocation(getLocation().x+getSize().x,getLocation().y);
			shell.setSize(200, getSize().y);
			shell.styledText.setText(tr.text);
			sr.start = 0;
			sr.length = tr.text.length();
			shell.styledText.setStyleRange(sr);
			shell.styledText.append("\n\n");
			shell.styledText.append("\n来源："+tr.name);
			shell.styledText.append("\n起始位置："+tr.start);
			shell.styledText.append("\n结束位置："+tr.end);
			//shell.pack();
			shell.setVisible(true);
		}else{
			if(shell != null && !shell.isDisposed()) shell.setVisible(false);
		}
	}
	public void insert(){
		if(tree.getSelectionCount() == 1){
			if(insertAction == none){
				if(findInMark) addtomarkstat(((TextRegion)tree.getSelection()[0].getData("textregion")).text);
				if(tree.getSelection()[0].getData("textOfinsert") == null)
					b.ba.insertText(((TextRegion)tree.getSelection()[0].getData("textregion")).text,b.text);
				else b.ba.insertText((String)tree.getSelection()[0].getData("textOfinsert"), b.text);
			}else if(insertAction == replace){
				if(findInMark) addtomarkstat(((TextRegion)tree.getSelection()[0].getData("textregion")).text);
				
				TextRegion tr = (TextRegion)tree.getSelection()[0].getData("textregion");
				if(b.text.getCaretOffset() > 0){
					int index = tr.text.indexOf(b.text.getText(b.text.getCaretOffset()-1,b.text.getCaretOffset()-1));
					if(index != -1){
						String newtext = tr.text.substring(index+1, tr.text.length());
						b.ba.insertText(newtext,b.text);
					}else 
						b.ba.insertText(((TextRegion)tree.getSelection()[0].getData("textregion")).text,b.text);
				}else b.ba.insertText(((TextRegion)tree.getSelection()[0].getData("textregion")).text,b.text);
				
			}else if(insertAction == readonly){
				
			}
			dispose();
		}
	}
	public void addtomarkstat(String text){
		Iterator<markstat> it = b.ba.markstat.iterator();
		hasinfo info = b.ba.markstatIshas(text);
		if(info.ishas){
			b.ba.markstat.get(info.index).count++;
			b.ba.setindexOfMarkstat();
			b.ba.setTopOnMarkstat(text);
		}else{
			b.ba.markstat.add(new markstat(text, 1));
			b.ba.setindexOfMarkstat();
		}
	}
	public void drawstrAction(int action){
		switch(action){
		case none:
			drawstr = "插入模式，按住Alt键不放切换至替换模式";
			composite.redraw();
			break;
		case replace:
			drawstr = "替换模式，松开Alt键切换至插入模式";
			composite.redraw();
		}
	}
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
