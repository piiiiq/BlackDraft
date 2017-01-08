package yang.app.black;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class pictureViewer extends Canvas {

	public final static int 
		HSCROLLUP = 1,
		HSCROLLDOWN = 2,
		VSCROLLUP = 3,
		VSCROLLDOWN = 4,
		NONE = 0,
		RESET = 5;
		
	private int scrollMode = RESET;
	private int scrollValue = 10;
	private int destX,destY,srcX,srcY,srcWidth,srcHeigth,destWidth,destHeigth,scrollIndex,lastScrollMode;
	private Image image = SWTResourceManager.getImage("C:\\Users\\C\\Desktop\\\u672A\u547D\u540D\u56FE\u7247.jpg");
	private boolean button1Down = false;
	private int oldXForButton1Down;
	
	public pictureViewer(Composite parent, int style) {
		super(parent, style|SWT.DOUBLE_BUFFERED);
		// TODO Auto-generated constructor stub
		createContext();
	}
	public void createContext()
	{
		addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				paint(e.gc, image);
			}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				switch(e.button)
				{
				case 1:
					button1Down = false;
					break;
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				

				//redraw();
				switch(e.button)
				{
				case 1:
					button1Down = true;
					oldXForButton1Down = e.x;
					break;
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				// TODO Auto-generated method stub
				if(button1Down)
				{
					if(e.x > oldXForButton1Down)
					{
						scrollMode = HSCROLLUP;
						scroll(scrollValue,0,0,0,getSize().x-scrollValue,getSize().y,false);
					}
					else
					{
						scrollMode = HSCROLLDOWN;
						scroll(0,0,scrollValue,0,getSize().x-scrollValue,getSize().y,false);
					}
				}
			}
		});
	}
	public void paint(GC gc, Image image)
	{
		switch(scrollMode)
		{
			case RESET:
				reSet(gc,image);
				break;
			case NONE:
				none(gc,image);
				break;
			case HSCROLLUP:
				hScrollUp(gc, image);
				break;
			case HSCROLLDOWN:
				hScrollDown(gc, image);
				break;
			case VSCROLLUP:
				break;
			case VSCROLLDOWN:
				break;
		}
		lastScrollMode = scrollMode;
	}
	public void reSet(GC gc, Image image)
	{
		gc.drawImage(
				image,
				0,
				0,
				image.getImageData().width,
				image.getImageData().height,
				0,
				0,
				image.getImageData().width, 
				image.getImageData().height
				);
	}
	public void none(GC gc, Image image)
	{
		gc.drawImage(
				image,
				srcX,
				srcY,
				srcWidth,
				srcHeigth,
				destX,
				destY,
				destWidth, 
				destHeigth
				);
	}
	public void hScrollUp(GC gc, Image image)
	{
		switch (lastScrollMode) {
		case RESET:
			break;
		case HSCROLLDOWN:
			srcX = srcX - getSize().x;
			break;
		case HSCROLLUP:
			srcX = srcX - scrollValue;
			
			break;
		default:
			break;
		}
		if(srcX < 0)
			srcX = 0;
		srcY = 0;
		destX = 0;
		destY = 0;
		srcWidth = scrollValue;
		srcHeigth = image.getImageData().height;
		destWidth = srcWidth;
		destHeigth = srcHeigth;
		System.out.println(srcX+" " +" "+srcY+" "+srcWidth+" "+srcHeigth+" "+destX+" "+destY+" "+destWidth+" "+destHeigth);
		gc.drawImage(
				image,
				srcX,
				srcY,
				srcWidth,
				srcHeigth,
				destX,
				destY,
				destWidth, 
				destHeigth
				);
		scrollIndex++;
	}
	public void hScrollDown(GC gc, Image image)
	{

		switch (lastScrollMode) {
		case RESET:
			srcX = getSize().x;
			break;
		case HSCROLLDOWN:
			srcX = srcX + scrollValue;
			break;
		case HSCROLLUP:
			srcX = srcX + getSize().x;
			break;
		default:
			break;
		}
		if(srcX > image.getImageData().width)
			srcX = image.getImageData().width;
		srcY = 0;
		destX = getSize().x - scrollValue;
		destY = 0;
		srcWidth = image.getImageData().width - srcX;
		srcHeigth = image.getImageData().height;
		destWidth = srcWidth;
		destHeigth = srcHeigth;
		
		gc.drawImage(
				image,
				srcX,
				srcY,
				srcWidth,
				srcHeigth,
				destX,
				destY,
				destWidth, 
				destHeigth
				);
		scrollIndex++;

		//scrollMode = NONE;
	}

}
