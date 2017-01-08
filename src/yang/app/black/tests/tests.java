package yang.app.black.tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class tests extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			tests shell = new tests(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public tests(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	//	RichTextEditor rte;
		//org.eclipse.nebula.widgets.richtext.feature.feature.group a;
		  //Nebula Rich Text Feature	1.0.0.201608240832	org.eclipse.nebula.widgets.richtext.feature.feature.group	Eclipse Nebula
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
