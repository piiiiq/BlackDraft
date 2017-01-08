package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;

import yang.demo.allPurpose.time;
import yang.demo.swt.windowLocation;

import org.eclipse.wb.swt.SWTResourceManager;

public class logShell extends Shell implements Serializable{
	static final long serialVersionUID = 42L;
	StyledText styledText;
	Shell parent;
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public logShell(Shell parent) {
		super(parent, SWT.DIALOG_TRIM);
		this.parent = parent;
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("log");
		setSize(349, 293);
		setImage(parent.getImage());
		addControlListener(new ControlListener() {
			
			@Override
			public void controlResized(ControlEvent e) {
				// TODO Auto-generated method stub
				styledText.setBounds(0, 0, getSize().x-14, getSize().y-14);
			}
			
			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		styledText = new StyledText(this, SWT.READ_ONLY | SWT.WRAP|SWT.V_SCROLL);
		styledText.setBounds(0, 0, 335, 279);
		styledText.setLeftMargin(10);
		styledText.getCaret().dispose();
		

	}
	public void dispose(){
		if(parent.isDisposed()) super.dispose();
		else hideWindow();
	}
	
	
	
	
	public void appendLog(String text,Point location, boolean clearlog){
		if(clearlog)
			styledText.setText(text);
		else {
			if(!text.equals(""))
				styledText.append("\n"+time.getCurrentDate("-")+" "+time.getCurrentTime()+" "+text);
			else styledText.append(text);
		}
		styledText.invokeAction(ST.PAGE_DOWN);
		if(location != null)
			setLocation(location);
		else windowLocation.onLeftBottomInScreen(this);
	}
	public void hideWindow(){
		setVisible(false);
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
