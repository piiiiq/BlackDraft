package yang.app.black;

import java.io.Serializable;
import java.util.Iterator;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableCursor;

import yang.demo.swt.windowLocation;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;

public class projectInfo extends Dialog implements Serializable{
	static final long serialVersionUID = 42L;

	protected Object result;
	protected Shell shell;
	private Table table;
	black b;
	private Table table_1;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public projectInfo(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		this.b = (black)parent;
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
		shell.setSize(678, 475);
		shell.setText("\u9879\u76EE\u5C5E\u6027");
		windowLocation.dialogLocation(b, shell);
		Group group = new Group(shell, SWT.NONE);
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group.setText("\u57FA\u672C\u4FE1\u606F");
		group.setBounds(10, 10, 277, 377);
		
		table_1 = new Table(group, SWT.FULL_SELECTION);
		table_1.setBounds(10, 24, 257, 343);
		table_1.setHeaderVisible(true);

		
		TableColumn tableColumn_3 = new TableColumn(table_1, SWT.NONE);
		tableColumn_3.setWidth(100);
		
		TableColumn tableColumn_4 = new TableColumn(table_1, SWT.NONE);
		tableColumn_4.setWidth(147);
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_1.setText("\u6587\u4EF6\u5B57\u6570\u7EDF\u8BA1");
		group_1.setBounds(293, 10, 369, 377);
		
		table = new Table(group_1, SWT.MULTI|SWT.CHECK);
		table.setHeaderVisible(true);
		table.setBounds(10, 26, 349, 341);
		
		table.addSelectionListener(new SelectionListener() {

			private TableItem tableItem;
			private TableItem tableItem_;
			private TableItem tableItem__;

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				int all = 0;
				int all_ = 0;
				TableItem[] tiss = table.getItems();
				for(int i=0; i< tiss.length; i++){
					if(table.getItem(i).getChecked()){						
						all += Integer.valueOf(table.getItem(i).getText(1));
						all_ += Integer.valueOf(table.getItem(i).getText(2));
					}
					
					
				}
				
				if(tableItem__ == null)
					tableItem__ = new TableItem(table_1, SWT.NONE);
				if(tableItem == null)
					tableItem = new TableItem(table_1, SWT.NONE);
				if(tableItem_ == null)
					tableItem_ = new TableItem(table_1, SWT.NONE);
				
				
				tableItem.setText(0, "中日韩(选中)");
				tableItem.setText(1, String.valueOf(all));
				tableItem_.setText(0, "所有(选中)");
				tableItem_.setText(1, String.valueOf(all_));
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		tableColumn.setWidth(147);
		tableColumn.setText("\u6587\u4EF6\u540D");
		
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.CENTER);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("\u4E2D\u65E5\u97E9\u5B57\u7B26\u6570");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.CENTER);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("\u6240\u6709\u5B57\u7B26\u6570");
		if(b.projectFile != null){
			int chineseall = 0,charall = 0,fileCountOfDraft = 0,fileCountOfRecycle = 0;
			Iterator<String> it = b.fileindex.iterator();
			while(it.hasNext()){
				String name = it.next();
				if(b.recycle.getProperty(name) == null){
					fileCountOfDraft++;
				}else fileCountOfRecycle++;
				String charcount = b.fileInfo.getProperty(name+"CharCount","0");
				String chinesecharcount = b.fileInfo.getProperty(name+"ChineseCharCount","0");
				chineseall += Integer.valueOf(chinesecharcount);
				charall += Integer.valueOf(charcount);
				String filename = b.fileInfo.getProperty(name,name);
				TableItem ti = new TableItem(table, SWT.None);
				ti.setText(new String[]{filename,chinesecharcount,charcount});
			}
			TableItem t_ = new TableItem(table_1,SWT.None);
			t_.setText(new String[]{"项目名称",b.projectProperties.getProperty("projectName")});
			
			TableItem t_0 = new TableItem(table_1,SWT.None);
			t_0.setText(new String[]{"创建时间",b.projectProperties.getProperty("createDate")});
			
			TableItem t_1 = new TableItem(table_1,SWT.None);
			t_1.setText(new String[]{"草稿箱文件数",String.valueOf(fileCountOfDraft)});
			
			TableItem t_2 = new TableItem(table_1,SWT.None);
			t_2.setText(new String[]{"垃圾箱文件数",String.valueOf(fileCountOfRecycle)});
			
			TableItem t_3 = new TableItem(table_1,SWT.None);
			t_3.setText(new String[]{"总字符数",String.valueOf(charall)});
			
			TableItem t_4 = new TableItem(table_1,SWT.None);
			t_4.setText(new String[]{"总中日韩字符数",String.valueOf(chineseall)});
			
//			text.setText("项目名称: "+b.projectProperties.getProperty("projectName")+
//					"创建时间: "+b.projectProperties.getProperty("createDate")+
//					"草稿箱文件数: "+fileCountOfDraft+
//					"垃圾箱文件数: "+fileCountOfRecycle+
//					"总字符数: "+charall+
//					"总中日韩字符数: "+chineseall
//					);
		}
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		button.setBounds(582, 409, 80, 27);
		button.setText("\u786E\u5B9A");

	}
}
