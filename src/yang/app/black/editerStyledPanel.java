package yang.app.black;

import java.io.Serializable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;

import yang.demo.allPurpose.debug;
import yang.demo.swt.windowLocation;

public class editerStyledPanel extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;
	protected Object result;
	protected Shell shell;
	black black;
	private Combo combo;
	int fontsize,fontstyle,alignment;
	String fontname;
	Color foreground,background;
	boolean strikeout,underline;
	textInfo info;
	private Label label_2;
	private Label label_3;
	private Label label_4;
	Point mouselocation;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public editerStyledPanel(Shell parent,Point mouselocation) {
		super(parent, SWT.NO_TRIM);
		this.black = (black)parent;
		this.mouselocation = mouselocation;
		info = black.ba.getCurrentSelectTextFontStyle();
		
		if(info.fontName != null) this.fontname = info.fontName;
		else this.fontname = black.ba.getEditerDefaultFontName();
		
		if(info.fontSize != -1) this.fontsize = info.fontSize;
		else this.fontsize = black.ba.getEditerDefaultFontSize();
		
		if(info.fontStyle != -1) this.fontstyle = info.fontStyle;
		else this.fontstyle = SWT.None;
		
		if(info.alignment != -1) this.alignment = info.alignment;
		else this.alignment = black.getEditer().getAlignment();
		
		this.strikeout = info.scrikeout;
		this.underline = info.underline;
		createContents();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
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
		
		shell = new Shell(getParent(), SWT.TITLE);
		shell.setText("\u683C\u5F0F\u8BBE\u7F6E");
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(197, 91);

		if(mouselocation != null){
			Point p = black.getDisplay().map(black.text, null, mouselocation.x-shell.getSize().x-10,mouselocation.y+
					black.text.getCaret().getSize().y);
			black.text.getCaret().setData(((mycaret)black.text.getCaret()).getLineHeightAtCaretOffset());
			windowLocation.windowAlwaysInScreen(p,black, shell,black.text.getCaret());
		}else windowLocation.dialogLocation(black, shell);
			

		shell.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				shell.setActive();
			}
		});
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellDeactivated(ShellEvent e) {
				shell.dispose();
			}
		});
				
		combo = new Combo(shell, SWT.NONE);
		String[] fonts = black.ba.getALLFonts();
		combo.setItems(fonts);
		//String font = black.getEditorDefaultFontFromCFG();
		if(info.fontName != null){
			for(int i=0;i<fonts.length;i++){
				if(fonts[i].equals(info.fontName)) {
					combo.select(i);
					break;
				}
			}
		}
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!combo.getItem(combo.getSelectionIndex()).equals("")){
					fontname = combo.getItem(combo.getSelectionIndex());
					black.ba.setTextFontStyle(fontname, -1, -1,-1,-1);
				}
			}
		});
		combo.setBounds(5, 6, 124, 25);
		
		final Combo combo_1 = new Combo(shell, SWT.NONE);
		for(int i=5; i<=70; i++){
			combo_1.add(String.valueOf(i));
		}
		for(int i=0; i<combo_1.getItemCount(); i++){
			if(info.fontSize == Integer.valueOf(combo_1.getItem(i))) combo_1.select(i);
		}
		if(info.fontSize == -1){
			combo_1.add("", 0);
			combo_1.select(0);
		}
		combo_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//if(combo_1.getSelectionIndex()>=0 && combo_1.getSelectionIndex()<combo_1.getItemCount())
				if(!combo_1.getItem(combo_1.getSelectionIndex()).equals("")){
					fontsize = Integer.valueOf(combo_1.getItem(combo_1.getSelectionIndex()));
					black.ba.setTextFontStyle(null, fontsize, -1,-1,-1);
				}
			}
		});
		combo_1.setBounds(135, 6, 49, 25);
		
		final Label label = new Label(shell, SWT.NONE);
		label.setToolTipText("\u4E0B\u5212\u7EBF");
		if(underline == true)
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		else label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				underline = !underline;
				if(underline)
				black.ba.setTextFontStyle(null, -1, -1, -1, 1);
				else black.ba.setTextFontStyle(null, -1, -1, -1, 0);
				if(underline) label.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
				else label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		});
		label.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				if(!underline)
				label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		});
		label.setImage(SWTResourceManager.getImage(editerStyledPanel.class, "/yang/app/black/icons/font_underline_icon&16.png"));
		label.setBounds(5, 37, 16, 16);
		
		final Label label_1 = new Label(shell, SWT.NONE);
		label_1.setToolTipText("\u5220\u9664\u7EBF");
		label_1.setImage(SWTResourceManager.getImage(editerStyledPanel.class, "/yang/app/black/icons/font_strokethrough_icon&16.png"));
		if(strikeout == true)
			label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		else label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				strikeout = !strikeout;
				if(strikeout) black.ba.setTextFontStyle(null, -1, -1, 1, -1);
				else black.ba.setTextFontStyle(null, -1, -1, 0, -1);
				
				if(strikeout) label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
				else label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		});
		label_1.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				if(!strikeout)
				label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		});
		label_1.setBounds(27, 37, 16, 16);
		
		label_2 = new Label(shell, SWT.NONE);
		label_2.setToolTipText("\u5DE6\u5BF9\u9F50");
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				alignment = SWT.LEFT;
				black.ba.setCurrentSelectedTextAlignment(alignment);
				if(alignment == SWT.LEFT) {
					label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				}
				else label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		});
		if(alignment == SWT.LEFT)
			label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			else label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_2.setImage(SWTResourceManager.getImage(editerStyledPanel.class, "/yang/app/black/icons/align_left_icon&16.png"));
		label_2.setBounds(123, 37, 16, 16);
		label_2.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				if(alignment != SWT.LEFT)
				label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		});
		
		label_3 = new Label(shell, SWT.NONE);
		label_3.setToolTipText("\u5C45\u4E2D\u5BF9\u9F50");
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				alignment = SWT.CENTER;
				black.ba.setCurrentSelectedTextAlignment(alignment);
				if(alignment == SWT.CENTER) {
					label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

				}
				else label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		});
		if(alignment == SWT.CENTER)
			label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			else label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_3.setImage(SWTResourceManager.getImage(editerStyledPanel.class, "/yang/app/black/icons/align_center_icon&16.png"));
		label_3.setBounds(145, 37, 16, 16);
		label_3.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				if(alignment != SWT.CENTER)
				label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		});
		
		label_4 = new Label(shell, SWT.NONE);
		label_4.setToolTipText("\u53F3\u5BF9\u9F50");
		label_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				alignment = SWT.RIGHT;
				black.ba.setCurrentSelectedTextAlignment(alignment);
				if(alignment == SWT.RIGHT) {
					label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
					label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

				}
				else label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		});
		if(alignment == SWT.RIGHT)
			label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			else label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_4.setImage(SWTResourceManager.getImage(editerStyledPanel.class, "/yang/app/black/icons/align_right_icon&16.png"));
		label_4.setBounds(168, 37, 16, 16);
		label_4.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				if(alignment != SWT.RIGHT)
				label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		});
		
		final Label label_5 = new Label(shell, SWT.NONE);
		label_5.setToolTipText("\u7C97\u4F53");
		if(fontstyle == SWT.BOLD || fontstyle == (SWT.BOLD|SWT.ITALIC))
			label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			else label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				switch(fontstyle){
				case SWT.None:
					fontstyle = SWT.BOLD;
					break;
				case SWT.ITALIC:
					fontstyle = SWT.BOLD|SWT.ITALIC;
					break;
				case SWT.BOLD|SWT.ITALIC:
					fontstyle = SWT.ITALIC;
					break;
				case SWT.BOLD: 
					fontstyle = SWT.None;
					//break;
				}
				black.ba.setTextFontStyle(null, -1, fontstyle,-1,-1);
				if(fontstyle == SWT.BOLD || fontstyle == (SWT.BOLD|SWT.ITALIC)) label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
				else label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		});
		label_5.setImage(SWTResourceManager.getImage(editerStyledPanel.class, "/yang/app/black/icons/font_bold_icon&16.png"));
		label_5.setBounds(67, 37, 16, 16);
		label_5.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				if(fontstyle != SWT.BOLD && fontstyle != (SWT.BOLD|SWT.ITALIC))
				label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		});
		
		final Label label_6 = new Label(shell, SWT.NONE);
		label_6.setToolTipText("\u659C\u4F53");
		if(fontstyle == SWT.ITALIC || fontstyle == (SWT.BOLD|SWT.ITALIC))
			label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			else label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				switch(fontstyle){
				case SWT.None:
					fontstyle = SWT.ITALIC;
					break;
				case SWT.ITALIC:
					fontstyle = SWT.None;
					break;
				case SWT.BOLD|SWT.ITALIC:
					fontstyle = SWT.BOLD;
					break;
				case SWT.BOLD: 
					fontstyle = SWT.BOLD|SWT.ITALIC;
				}
				black.ba.setTextFontStyle(null, -1, fontstyle,-1,-1);
				if(fontstyle == SWT.ITALIC || fontstyle == (SWT.BOLD|SWT.ITALIC)) label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
				else label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
		});
		label_6.setImage(SWTResourceManager.getImage(editerStyledPanel.class, "/yang/app/black/icons/font_italic_icon&16.png"));
		label_6.setBounds(89, 37, 16, 16);
		label_6.addMouseTrackListener(new MouseTrackListener() {
			
			@Override
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				// TODO Auto-generated method stub
				if(fontstyle != SWT.ITALIC && fontstyle != (SWT.BOLD|SWT.ITALIC))
				label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				// TODO Auto-generated method stub
				label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
			}
		});
	}
	
}
