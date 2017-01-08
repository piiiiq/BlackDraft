package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.swt.windowLocation;

public class startInfo extends Shell implements Runnable,Serializable{
	static final long serialVersionUID = 42L;
	String infotext = "";
	black b;
	

	/**
	 * Create the shell.
	 * @param display
	 */
	public startInfo(black b) {
		super(b.getDisplay(), SWT.NONE);
		setBackgroundImage(SWTResourceManager.getImage(startInfo.class, "/yang/app/black/res/intro.png"));
		this.b = b;
		createContents();
		open();
		
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(673, 173);
		windowLocation.showWindowOnScreenCenter(this);
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				e.gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				e.gc.drawString(infotext, 20, getSize().y-20,true);
				
			}
		});

	}
	public void sendMessage(final String text){
		
				infotext = text;
				redraw();
		
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void run() {
		while (!isDisposed()) {
			if (!Display.getDefault().readAndDispatch()) {
				Display.getDefault().sleep();
			}
		}
	}

}
