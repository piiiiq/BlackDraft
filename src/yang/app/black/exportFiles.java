package yang.app.black;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TreeDragSourceEffect;
import org.eclipse.swt.dnd.TreeDropTargetEffect;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import yang.demo.swt.windowLocation;

import org.eclipse.swt.widgets.Tree;

public class exportFiles extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	protected Shell shell;
	black b;
	ArrayList<String> fileindex = new ArrayList<String>();
	private Tree tree;
	private Button btnWord;
	private Button btnWorddocx;
	String outputFileExtendsions = "*.docx";
	File outputfile;
	private Label label_1;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public exportFiles(Shell parent, int style) {
		super(parent, style);
		b = (black)parent;
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(450, 604);
		shell.setText("\u5BFC\u51FA\u6587\u4EF6");
		windowLocation.dialogLocation((black)shell.getParent(), shell);
		
		Group group = new Group(shell, SWT.NONE);
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group.setText("\u5BFC\u51FA\u9009\u9879");
		group.setBounds(10, 362, 424, 80);
		
		Button button = new Button(group, SWT.CHECK);
		button.setEnabled(false);
		button.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		button.setBounds(10, 25, 249, 17);
		button.setText("\u5728\u6240\u9009\u62E9\u7684\u6BCF\u4E2A\u6587\u4EF6\u7684\u5185\u5BB9\u540E\u9762\u63D2\u5165\u5206\u9875\u7B26");
		
		Button button_3 = new Button(group, SWT.CHECK);
		button_3.setEnabled(false);
		button_3.setText("\u5C06\u9879\u76EE\u6811\u4E0A\u7684\u6587\u4EF6\u540D\u4F5C\u4E3A\u7AE0\u8282\u540D\u63D2\u5165\u76EE\u6807\u6587\u4EF6");
		button_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		button_3.setBounds(10, 48, 261, 17);
		
		Label label = new Label(shell, SWT.WRAP);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setBounds(10, 10, 424, 54);
		label.setText("\u9009\u62E9\u51C6\u5907\u5BFC\u51FA\u7684\u6587\u4EF6\uFF0C\u6240\u9009\u62E9\u7684\u591A\u4E2A\u6587\u4EF6\u4F1A\u5BFC\u51FA\u4E3A\u4E00\u4E2A\u6587\u4EF6\uFF0C\u6240\u9009\u62E9\u7684\u6587\u4EF6\u7684\u5185\u5BB9\u4F1A\u4F9D\u7167\u5176\u5728\u9879\u76EE\u6811\u4E2D\u7684\u6392\u5217\u987A\u5E8F\u63D2\u5165\u540C\u4E00\u4E2A\u76EE\u6807\u6587\u4EF6\uFF0C\u53EF\u4EE5\u901A\u8FC7\u4E0A\u4E0B\u62D6\u52A8\u9879\u76EE\u6811\u4E2D\u7684\u6587\u4EF6\u540D\u6765\u6539\u53D8\u6240\u9009\u62E9\u7684\u6587\u4EF6\u7684\u5185\u5BB9\u5728\u8F93\u51FA\u6587\u4EF6\u4E2D\u7684\u4F4D\u7F6E");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		button_1.setBounds(354, 538, 80, 27);
		button_1.setText("\u53D6\u6D88");
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(outputfile != null)
					b.ba.exportFiles(getchecked(), outputfile);
				else b.ba.getMessageBox("", "请选择输出位置及名称");
				
			}
		});
		button_2.setBounds(268, 538, 80, 27);
		button_2.setText("\u5F00\u59CB\u5BFC\u51FA");
		
		tree = new Tree(shell, SWT.MULTI|SWT.CHECK);
		tree.setBounds(61, 70, 373, 286);
		DragSource ds = new DragSource(tree, DND.FEEDBACK_NONE);
		final TreeDragSourceEffect tdse = new TreeDragSourceEffect(tree);
		tree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		ds.setDragSourceEffect(tdse);
		
		ds.setTransfer(new Transfer[] {TextTransfer.getInstance()});
		ds.addDragListener(new DragSourceListener() {
			
			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				event.data = tree.getSelection()[0].getText();
			}
			
			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
			}
		});
		
		DropTarget dt = new DropTarget(tree, DND.FEEDBACK_NONE);
		final TreeDropTargetEffect tdte = new TreeDropTargetEffect(tree);
		dt.setDropTargetEffect(tdte);
		
		dt.setTransfer(new Transfer[]{TextTransfer.getInstance()});
		dt.addDropListener(new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void drop(DropTargetEvent event) {
				TreeItem ti = (TreeItem)tdte.getItem(event.x, event.y);//目标
				if(ti != null){
					String newindex_realname = (String)ti.getData("realname");
					String realname = (String)tree.getSelection()[0].getData("realname");
					fileindex.remove(realname);
					int newindex = fileindex.indexOf(newindex_realname);
					fileindex.add(newindex, (String)tree.getSelection()[0].getData("realname"));
					listProjectFile();
				}
				
			
		}

			@Override
			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}});
		Iterator<String> it = ((black)shell.getParent()).fileindex.iterator();
		
		while(it.hasNext()){
			String filename = it.next();
			if(b.recycle.getProperty(filename) == null){
				TreeItem ti = new TreeItem(tree,SWT.CHECK);
				ti.setChecked(true);
				ti.setText(b.fileInfo.getProperty(filename, filename));
				ti.setData("realname", filename);
				fileindex.add(filename);
			}
		}
		
		final Button button_4 = new Button(shell, SWT.CHECK);
		button_4.setSelection(true);
		button_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAll(button_4.getSelection());
			}
		});
		button_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		button_4.setBounds(10, 70, 45, 17);
		button_4.setText("\u5168\u9009");
		
		final Button button_5 = new Button(shell, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell,SWT.SAVE);
				fd.setText("选择输出位置及名称");
				fd.setFilterExtensions(new String[]{outputFileExtendsions});
				String filename = fd.open();
				if(filename != null){
					outputfile = new File(filename);
					label_1.setText(filename);
				}
				
			}
		});
		button_5.setBounds(129, 538, 134, 27);
		button_5.setText("\u9009\u62E9\u8F93\u51FA\u4F4D\u7F6E\u53CA\u540D\u79F0");
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_1.setText("\u5BFC\u51FA\u4E3A");
		group_1.setBounds(10, 448, 424, 43);
		
		btnWord = new Button(group_1, SWT.RADIO);
		btnWord.setEnabled(false);
		btnWord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnWorddocx.setSelection(!btnWord.getSelection());
				outputFileExtendsions = "*.doc";
			}
		});
		btnWord.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnWord.setBounds(38, 16, 151, 17);
		btnWord.setText("Word 97-2003\uFF08.doc\uFF09");
		
		btnWorddocx = new Button(group_1, SWT.RADIO);
		btnWorddocx.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnWord.setSelection(!btnWorddocx.getSelection());
				outputFileExtendsions = "*.docx";
			}
		});
		btnWorddocx.setSelection(true);
		btnWorddocx.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		btnWorddocx.setText("Word 2007-2013\uFF08.docx\uFF09");
		btnWorddocx.setBounds(195, 16, 171, 17);
		
		label_1 = new Label(shell, SWT.WRAP);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setBounds(10, 497, 424, 35);

	}
	public void selectAll(boolean is){
		TreeItem[] tis = tree.getItems();
		for(TreeItem ti:tis){
			ti.setChecked(is);
		}
	}
	public ArrayList<String> getchecked(){
		ArrayList<String> al = new ArrayList<String>();
		TreeItem[] tis = tree.getItems();
		for(TreeItem ti:tis){
			if(ti.getChecked()){
				al.add((String)ti.getData("realname"));
			}
		}
		return al;
	}
	public void listProjectFile(){
		tree.clearAll(true);
		tree.setItemCount(0);
		Iterator<String> it = fileindex.iterator();
		while(it.hasNext()){
			String filename = it.next();
			TreeItem ti = new TreeItem(tree,SWT.CHECK);
			ti.setText(b.fileInfo.getProperty(filename, filename));
			ti.setData("realname", filename);
		}
	}
}
