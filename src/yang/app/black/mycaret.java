package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;

public class mycaret extends Caret implements Serializable{
	static final long serialVersionUID = 42L;
	
	StyledText st;
	int lineHeightAtCaretOffset;
	public mycaret(Canvas parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		this.st = (StyledText)parent;
	}
	@Override
	protected void checkSubclass() {
		// TODO Auto-generated method stub
		//super.checkSubclass();
	}
	public int getLineHeightAtCaretOffset(){
		return lineHeightAtCaretOffset;
	}
	public void setLocation(Point p){
		lineHeightAtCaretOffset = st.getLineHeight(st.getCaretOffset());
		//lineHeightAtCaretOffset = st.getText(st.getCaretOffset(), st.getCaretOffset());	
		if(getStyle() == SWT.HORIZONTAL){
			super.setSize(lineHeightAtCaretOffset/2, 2);
			super.setLocation(p.x, p.y+lineHeightAtCaretOffset-3);
		}else{
			super.setSize(1, lineHeightAtCaretOffset);
			super.setLocation(p.x, p.y);
		}
	}
}
