package yang.app.black.tests;

import org.eclipse.swt.SWT;  
import org.eclipse.swt.events.ControlAdapter;  
import org.eclipse.swt.events.ControlEvent;  
import org.eclipse.swt.events.DisposeEvent;  
import org.eclipse.swt.events.DisposeListener;  
import org.eclipse.swt.graphics.Rectangle;  
import org.eclipse.swt.internal.win32.OS;  
import org.eclipse.swt.internal.win32.RECT;  
import org.eclipse.swt.internal.win32.SHELLEXECUTEINFO;  
import org.eclipse.swt.internal.win32.TCHAR;  
import org.eclipse.swt.layout.FillLayout;  
import org.eclipse.swt.widgets.Composite;  
import org.eclipse.swt.widgets.Display;  
import org.eclipse.swt.widgets.Shell;  
public class EmbedEXEIntoSWT {  
    private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="10,10"  
    private Composite composite = null;  
    int notepadHwnd = 0;  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        Display display = Display.getDefault();  
        EmbedEXEIntoSWT thisClass = new EmbedEXEIntoSWT();  
        thisClass.createSShell();  
        thisClass.sShell.open();  
        while (!thisClass.sShell.isDisposed()) {  
            if (!display.readAndDispatch())  
                display.sleep();  
        }  
        display.dispose();  
    }  
    /** 
     * This method initializes sShell 
     */  
    private void createSShell() {  
        sShell = new Shell();  
        sShell.setText("Shell");  
        composite = new Composite(sShell, SWT.NONE);  
        sShell.setLayout(new FillLayout());  
       // composite.setBackground(sShell.getDisplay().getSystemColor(SWT.COLOR_BLACK));  
        composite.setBounds(0,0,800,600);  
        composite.setLayout(new FillLayout());  
        composite.addControlListener(new ControlAdapter() {  
            public void controlResized(ControlEvent e) {  
                if (notepadHwnd != 0) {  
                    Rectangle cRect = ((Composite) (e.widget)).getClientArea();  
                    // 全屏  
                    OS.SetWindowPos(notepadHwnd, 0, 0,0,cRect.width, cRect.height,OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_ASYNCWINDOWPOS);  
//                  // 居中  
//                  RECT sRect = new RECT();  
//                  OS.GetClientRect(notepadHwnd, sRect);  
//                  int w = sRect.right - sRect.left;  
//                  int h = sRect.bottom - sRect.top;  
//                  OS.SetWindowPos(notepadHwnd, 0, (cRect.width - w)/2, (cRect.height - h)/2, w, h - 20, OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_ASYNCWINDOWPOS);  
                }  
            }  
        });  
        sShell.addDisposeListener(new DisposeListener() {  
            public void widgetDisposed(DisposeEvent arg0) {  
                if (notepadHwnd != 0) {  
                    OS.SendMessage(notepadHwnd, OS.WM_CLOSE, 0, 0);  
                }  
            }  
        });  
        try {  
            startNotePad(); 
        	//executeProg("notepad.exe");
        } catch (Exception e) {  
        }  
    }  
    private boolean executeProg(String fileName) throws Exception  
    {  
        int hHeap = OS.GetProcessHeap ();  
        TCHAR buffer = new TCHAR (0, fileName, true);  
        int byteCount = buffer.length () * TCHAR.sizeof;  
        int lpFile = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);  
        OS.MoveMemory (lpFile, buffer, byteCount);  
        SHELLEXECUTEINFO info = new SHELLEXECUTEINFO ();  
        info.cbSize = SHELLEXECUTEINFO.sizeof;  
        info.lpFile = lpFile;  
        //隐藏启动  
        info.nShow = OS.SW_HIDE;  
        boolean result = OS.ShellExecuteEx (info);  
        if (lpFile != 0) OS.HeapFree (hHeap, 0, lpFile);  
        return result;  
    }  
      
    protected void startNotePad() throws Exception {  
        //"notepad.exe"为待启动的程序名  
        executeProg("notepad.exe");  
          
        //"Notepad"为被嵌套程序窗口的ClassName(Win32级别)，可以使用Spy++等工具查看  
        while (notepadHwnd == 0) {  
            notepadHwnd = OS.FindWindow(new TCHAR(0,"Notepad",true),null);  // Notepad,rsedc_wclass  
            Thread.sleep(10);  
        }  
          
        //&~WS_BORDER去掉内嵌程序边框，这样看起来更像一个内嵌的程序。如果需要显示边框，则将这两行代码删除  
        int oldStyle = OS.GetWindowLong(notepadHwnd, OS.GWL_STYLE);          
        OS.SetWindowLong(notepadHwnd, OS.GWL_STYLE, oldStyle&~OS.WS_BORDER);  
          
        //composite为承载被启动程序的控件  
        OS.SetParent(notepadHwnd, composite.handle);          
        //窗口最大化  
        OS.SendMessage(notepadHwnd, OS.WM_SYSCOMMAND, OS.SC_MAXIMIZE, 0);  
    }  
}  