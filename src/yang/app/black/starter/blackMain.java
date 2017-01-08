package yang.app.black.starter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;
import yang.app.black.black;
import yang.demo.allPurpose.autoDO;

public class blackMain {
	public static black b;
	public static void main(String args[]) {
		b = new black();
		while (!b.isDisposed()) {
			if (!Display.getDefault().readAndDispatch()) {
				Display.getDefault().sleep();
			}
		}
	}
	public static black getBlack(){
		return b;
	}
	public static void restart(){
		if(b != null && !b.isDisposed()){
			b.dispose();
		}
		main(null);
	}
}
