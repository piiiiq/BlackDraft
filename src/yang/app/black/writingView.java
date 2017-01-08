package yang.app.black;

import java.io.Serializable;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.app.mud.mud;
import yang.demo.allPurpose.autoDO;
import yang.demo.swt.windowLocation;

public class writingView extends mud implements Serializable{
	static final long serialVersionUID = 42L;
	static black b;
	int x;
	Slider slider;
	boolean isMax;
	Shell fullScreenShell;
	autoDO doing;

	/**
	 * Create the shell.
	 * @param display
	 */
	public writingView(Shell parent,final black b,int style) {
		super(parent,SWT.NO_TRIM|SWT.CLOSE,"writingView");
		fullScreenShell = parent;
		this.b = b;
		init();
		setBounds(getBounds().x, 0, getBounds().width, windowLocation.returnScreenHeight());
		//layout();
	}
	public writingView(black b, int style){
		super(style,"writingView");
		this.b = b;
		init();
		layout();
		setVisible(true);
		//b.text.setFocus();
		setFocus();

	}
	public void init(){
		b.setVisible(false);
		//windowLocation.reMoveWindow(this);
		addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				// TODO Auto-generated method stub
				if(slider != null && !slider.isDisposed()){
					if(e.x >= getClientArea().width-slider.getSize().x-30){
						slider.setVisible(true);
					}else slider.setVisible(false);
				}
			}
		});
		
		setImage(b.getImage());
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setStyledTextWidth(Integer.valueOf(b.appProperties.getProperty("WritingView_TextX", String.valueOf(windowLocation.returnScreenWidth()-
				windowLocation.returnScreenWidth()/4))));
		createContents();
		//setMaximized(true);
		if(b.tv != null){
			b.tv.getTextWidget().setParent(this);
			
			//b.tv.getTextWidget().setMargins(130, 10, 110, 0);
			b.blackTextArea.writingView();
			setStyledTextLayout(x);
		}
		addSlider();

	}
	public void addSlider(){
		slider = new Slider(this,SWT.V_SCROLL|SWT.TRANSPARENT);
		slider.setVisible(false);
		//slider.setBackground(SWTResourceManager.getColor(106,106,106));
		FormData fd_slider = new FormData();
		fd_slider.top = new FormAttachment(0);
		fd_slider.bottom = new FormAttachment(100);
		fd_slider.right = new FormAttachment(100);
		slider.setLayoutData(fd_slider);
		//layout();
		b.ba.setSlider(slider);
		slider.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				b.text.setTopPixel(slider.getSelection());
			}
		});
		slider.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(doing == null)initDoing();
				else {
					doing.stop();
					doing = null;
					initDoing();
				}
			}
			
			@Override
			public void mouseEnter(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void initDoing(){
		doing = new autoDO(5) {
			@Override
			public void action() {
				// TODO Auto-generated method stub
				if(slider != null && !slider.isDisposed())
				getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						slider.setVisible(false);						
					}
				});
				
				doing.stop();
				doing = null;
			}
		};
		doing.start();
	}
	public void disposeTextArea(){
		if(b.tv.getTextWidget() != null && !b.tv.getTextWidget().isDisposed()){
			b.closeCurrentFile(true);			
		}
	}
	public Rectangle getSizeAndLocation(){
		return getBounds();
	}
	public boolean isMax(){
		return getMaximized();
	}
	public void setStyledTextWidth(int width){
		x = (windowLocation.returnScreenWidth()-width)/2;
		if(b.text != null && !b.text.isDisposed()){
			setStyledTextLayout(x);
			//System.out.println("oy0");
			layout();
			//System.out.println("oy1");
		}
	}
	public void setStyledTextLayout(int x){
		FormData fd_styledText = new FormData();
		fd_styledText.bottom = new FormAttachment(100);
		fd_styledText.right = new FormAttachment(100, 0-x);
		fd_styledText.top = new FormAttachment(0);
		fd_styledText.left = new FormAttachment(0, x);
		b.text.setLayoutData(fd_styledText);
		//layout();
	}
	public void createTextArea(IDocument iDocument){
	
		if(b.text != null && !b.text.isDisposed()){
			disposeTextArea();
		}
		b.tv = new TextViewer(b, SWT.V_SCROLL|SWT.WRAP);
		b.text = b.tv.getTextWidget();
		b.text.setVisible(false);
		b.text.setParent(this);
		setStyledTextLayout(x);
		b.blackTextArea = new blackTextArea(b.text,b);
		b.blackTextArea.writingView();
		b.tv.setUndoManager(new TextViewerUndoManager(100));
		b.tv.activatePlugins();
		if(b.ba.isFullScreenWritingView())
			b.ba.changeColor(b.ba.lastForeColor, b.ba.lastBackColor);
		if(iDocument != null) {
			b.tv.setDocument(iDocument);
		}
		addSlider();
		layout();
		b.text.setFocus();
		b.text.setVisible(true);
	}
	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		String name = b.ba.getShowNameByRealName(b.getCurrentEditFile().getName());
		if(b.fileIsSave == 0)
			setText(name+"* - 写作视图");
		else setText(name+" - 写作视图");
		setLayout(new FormLayout());
	}
	public void exit(){
		if(!isDisposed()){
			if(b.text != null && !b.text.isDisposed()){
				//b.text.setMargins(10, 10, 10, 0);
				b.text.getVerticalBar().setVisible(true);
				b.text.setParent(b);
				super.dispose();
				
				if(fullScreenShell != null && !fullScreenShell.isDisposed())
					fullScreenShell.dispose();
				
				b.resetLayoutData();
				b.blackTextArea.st.getMenu().dispose();
				b.blackTextArea.addMenu(b.blackTextArea.st);
				b.text.setFocus();
				b.text.setForeground(b.color_black);
				b.text.setBackground(b.color_white);
				b.setVisible(true);
				//b.open();
				b.setActive();
				b.setAlpha(255);
				b.setAppProperties("isWritingView", "false");
				
			}
		}
	}
	public void resetView(){
		b.resetStyledTextTopPixelAndCaretOffset(b.getCurrentEditFile().getName(), b.text);
		b.text.redraw();
//		ScrollBar vb = b.text.getVerticalBar();
//		slider.setValues(vb.getSelection(), vb.getMinimum(), vb.getMaximum(), vb.getThumb(), vb.getIncrement(), vb.getPageIncrement());
	}
	public void dispose(){
		b.setAppProperties("isWritingView", "true");
		super.saveAppData();
		b.dispose();
//		exit();
//		shell.dispose();
	}
	public void close(){
		exit();
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
