package yang.app.black;

import java.util.ArrayList;

import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

public class undoManager {
	ArrayList<undoPoint> al = new ArrayList<>();
	int level;
	StyledText text;
	public undoManager(StyledText text,int level){
		if(text != null) {
			this.text = text;
			text.addExtendedModifyListener(new ExtendedModifyListener() {
				
				@Override
				public void modifyText(ExtendedModifyEvent event) {
					// TODO Auto-generated method stub
//					if(event.st)
				}
			});
		}
		this.level = level;
	}
}
class undoPoint{
	int start,end;
	String text;
	StyleRange sr;
	static int undo = 1,redo = 2;
	public undoPoint(String text,int start,int end,StyleRange sr,int undoorredo){
		this.text = text;
		this.start = start;
		this.end = end;
		this.sr = sr;
	}
}
