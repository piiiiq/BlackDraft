package yang.app.black;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.swt.windowLocation;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;

public class gitBranchInfo extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table table;
	black b;
	Menu menu;
	private Button button;
	private Group group;
	Collection<Ref> allBranchFromRemote = null;
	private Table table_1;
	private TableColumn tableColumn;
	private TableColumn tableColumn_2;
	private TableColumn tableColumn_1;


	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public gitBranchInfo(Shell parent, int style) {
		super(parent, style);
		b = (black)parent;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		setTable();
		setTableMenu();
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
		shell = new Shell(getParent(), getStyle());
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(693, 481);
		shell.setText("\u64CD\u4F5C\u4ED3\u5E93\u5206\u652F");
		windowLocation.dialogLocation(getParent(), shell);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton.setBounds(602, 417, 75, 25);
		btnNewButton.setText("\u786E\u5B9A");
		
		group = new Group(shell, SWT.NONE);
		group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group.setText("\u4ED3\u5E93\u5206\u652F");
		group.setBounds(10, 10, 201, 387);
		
		table = new Table(group, SWT.FULL_SELECTION|SWT.MULTI);
		table.setBounds(10, 28, 181, 349);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		menu = new Menu(table);
		table.setMenu(menu);
		
		button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reLoadData();
			}
		});
		button.setBounds(495, 417, 101, 25);
		button.setText("\u66F4\u65B0\u6570\u636E");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.setEnabled(false);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				b.ba.getCommitLogsFormLocalRespository();
			}
		});
		button_1.setBounds(358, 417, 131, 25);
		button_1.setText("\u67E5\u770B\u4ED3\u5E93\u63D0\u4EA4\u4FE1\u606F");
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group_1.setText("\u4ED3\u5E93\u6700\u8FD1\u63D0\u4EA4\u8BB0\u5F55");
		group_1.setBounds(217, 10, 460, 387);
		
		table_1 = new Table(group_1, SWT.MULTI);
		table_1.setBounds(10, 23, 440, 354);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		tableColumn_1 = new TableColumn(table_1, SWT.CENTER);
		tableColumn_1.setText("提交备注");
		tableColumn_1.pack();
		tableColumn_2 = new TableColumn(table_1, SWT.CANCEL);
		tableColumn_2.setText("提交者");
		tableColumn_2.pack();
		tableColumn = new TableColumn(table_1, SWT.CANCEL);
		tableColumn.setText("提交时间");
		tableColumn.pack();


	}
	public void setTable(){
		b.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showProgress showProgress = new showProgress(b) {
					
					@Override
					void actionWhenOKButtonSelected() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					void actionInOtherThread() {
						// TODO Auto-generated method stub
						if(allBranchFromRemote == null)return;
						table.removeAll();
						group.setText("仓库分支("+allBranchFromRemote.size()+")");
						for(Ref r:allBranchFromRemote){
							TableItem tableItem= new TableItem(table, SWT.NONE);
							tableItem.setText(0, r.getName());
							//tableItem.set
						}
						Iterable<RevCommit> logs = b.ba.getCommitLogsFormLocalRespository();
						for(RevCommit r:logs){
							String[] texts = new String[]{r.getFullMessage(),
									r.getAuthorIdent().getName(),
									DateFormat.getInstance().format(r.getCommitterIdent().getWhen())};
							table_1.removeAll();
							TableItem tableItem = new TableItem(table_1, SWT.NONE);
							tableItem.setText(texts);
							tableColumn.pack();
							tableColumn_1.pack();
							tableColumn_2.pack();
						}

					}
				};
				showProgress.setTitle("从远程仓库获取数据");
				showProgress.open();
			}
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				b.ba.setProgressInfo("从远程仓库获取数据...", 20);
				allBranchFromRemote = b.ba.getAllBranchFromRemote();
				b.ba.setProgressInfo("完成！", 100);
			}
		}).start();
		
	}
	public void setTableMenu(){
		MenuItem remove = b.ba.getMenuItem(menu, "从远程仓库中删除", SWT.NONE);
		remove.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() == 0) return;
				
				TableItem[] selection = table.getSelection();
				ArrayList<String> al = new ArrayList<>();
				for(TableItem ti:selection){
					al.add(ti.getText(0));
				}
				
				
				if(selection != null){
					b.getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress showProgress = new showProgress(b) {
								@Override
								void actionInOtherThread() {
									// TODO Auto-generated method stub
									reLoadData();

								}

								@Override
								void actionWhenOKButtonSelected() {
									// TODO Auto-generated method stub
									
								}
							};
							showProgress.setTitle("从远程仓库删除");
							showProgress.open();
						}
					});
					
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int progr = 0;
							if(al.size() > 0)
								progr = 100/al.size();
							for(String str:al){
								b.ba.deleteBranchFromRemote(str,false);
								b.ba.setProgressInfo("正在从远程仓库删除", b.ba.progressValue+progr);
								b.ba.addlogs("删除"+str,null);
							}
							b.ba.setProgressInfo("完成！", 100);
						}
					}).start();
					
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem remove_ = b.ba.getMenuItem(menu, "从远程仓库和本地仓库中删除", SWT.NONE);
		remove_.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() == 0) return;
				
				TableItem[] selection = table.getSelection();
				ArrayList<String> al = new ArrayList<>();
				for(TableItem ti:selection){
					al.add(ti.getText(0));
				}
				
				
				if(selection != null){
					b.getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress showProgress = new showProgress(b) {
								@Override
								void actionInOtherThread() {
									// TODO Auto-generated method stub
									reLoadData();

								}

								@Override
								void actionWhenOKButtonSelected() {
									// TODO Auto-generated method stub
									
								}
							};
							showProgress.setTitle("从远程和本地仓库中删除");
							showProgress.open();
						}
					});
					
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int progr = 0;
							if(al.size() > 0)
								progr = 100/al.size();
							for(String str:al){
								b.ba.deleteBranchFromRemote(str,true);
								b.ba.setProgressInfo("正在从远程仓库删除", b.ba.progressValue+progr);
								b.ba.addlogs("删除"+str,null);
							}
							b.ba.setProgressInfo("完成！", 100);
						}
					}).start();
					
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem copy = b.ba.getMenuItem(menu, "下载所选的分支到本地", SWT.NONE);
		copy.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getSelectionCount() == 0)return;
				DirectoryDialog dd = new DirectoryDialog(b);
				String dir = dd.open();
				if(dir == null) return;
				ArrayList<String> coll = new ArrayList<>();
				TableItem[] selection = table.getSelection();
				for(TableItem t:selection){
					coll.add(t.getText(0));
				}
				b.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress showProgress = new showProgress(b) {
							
							@Override
							void actionWhenOKButtonSelected() {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							void actionInOtherThread() {
								// TODO Auto-generated method stub
								
							}
						};
						showProgress.setTitle("从远程仓库下载");
						showProgress.open();
					}
				});
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						b.ba.setProgressInfo("正在从远程仓库下载", 20);
						b.ba.copyBranchFromeRemote(dir, null, coll, false);
						b.ba.setProgressInfo("下载完成", 100);
					}
				}).start();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem copyall = b.ba.getMenuItem(menu, "下载所有的分支到本地", SWT.NONE);
		copyall.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if(table.getItemCount() == 0)return;
				DirectoryDialog dd = new DirectoryDialog(b);
				String dir = dd.open();
				if(dir == null) return;
				
				b.getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showProgress showProgress = new showProgress(b) {
							
							@Override
							void actionWhenOKButtonSelected() {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							void actionInOtherThread() {
								// TODO Auto-generated method stub
								
							}
						};
						showProgress.setTitle("从远程仓库下载");
						showProgress.open();
					}
				});
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						b.ba.setProgressInfo("开始从远程仓库下载", 20);
						b.ba.copyBranchFromeRemote(dir, null, null, true);
						b.ba.setProgressInfo("完成！", 100);
					}
				}).start();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		MenuItem saveto = b.ba.getMenuItem(menu, "将当前项目上载到所选分支", SWT.NONE);
		saveto.setEnabled(false);
		MenuItem crate = b.ba.getMenuItem(menu, "创建分支", SWT.NONE);
		crate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				rename re = new rename(b, SWT.NONE);
				re.setTitle("命名分支");
				String name = re.open();
				if(name != null){
					b.getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showProgress showProgress = new showProgress(b) {
								
								@Override
								void actionWhenOKButtonSelected() {
									// TODO Auto-generated method stub
									reLoadData();
								}
								
								@Override
								void actionInOtherThread() {
									// TODO Auto-generated method stub
									
								}
							};
							showProgress.setTitle("新建分支并保存到远程仓库");
							showProgress.open();
						}
					});
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							b.ba.setProgressInfo("正在将更改上传至远程仓库", 20);
							b.ba.createBranch(name);
							b.ba.setProgressInfo("完成！", 100);
						}
					}).start();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		

	}
	public void reLoadData(){
		setTable();
	}
}
