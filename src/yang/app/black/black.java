package yang.app.black;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
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
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.app.mud.mud;
import yang.demo.allPurpose.autoDO;
import yang.demo.allPurpose.cfg_read_write;
import yang.demo.allPurpose.debug;
import yang.demo.allPurpose.safe;
import yang.demo.allPurpose.time;

public class black extends mud {
	public StyledText text;
	public TextViewer tv;
	public Properties projectProperties, fileInfo, recycle;
	public File projectFile;
	public blackTextArea blackTextArea;
	public blackAction ba = new blackAction(this);
	public logShell log = new logShell(this);
	public Properties appProperties = super.getAppProperties();
	public String backtext;
	public nameCreator namecreator;

	public io io = new io(this);
	Color color_white = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	Color color_black = SWTResourceManager.getColor(SWT.COLOR_BLACK);
	Color color_textBackground = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	Color color_textForeground = SWTResourceManager.getColor(SWT.COLOR_BLACK);
	Color color_labelDown = SWTResourceManager.getColor(SWT.COLOR_GREEN);
	Color color_down = SWTResourceManager.getColor(SWT.COLOR_BLUE);
	Color color_eye = SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND);
	Color color_label_exit = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	Font msyh = new SWTResourceManager().getFont("微软雅黑", 11, SWT.NONE);
	int fileIsSave = 1; // 1为已保存，0为未保存
	File currentEditFile;
	autoSaveTimer autoSave;
	final static String appVersion = allInfo.appVersion;
	final static String buildDate = allInfo.buildDate;
	final static String codeName = allInfo.codeName;
	Composite composite;
	Label label_3;
	MenuItem mntmCtrlz;
	MenuItem mntmCtrlx;
	private FormData fd_composite;
	Tree tree;
	private Menu menu_6;
	private MenuItem mntmCtrlshift;
	int FileCountOfProject = 0;
	public TreeItem draft;
	private MenuItem mntmCtrlshift_1;
	TreeItem treeItem_1, findresult;
	private MenuItem mntmCtrlr;
	boolean mouseLeftButton, mouseDrag;
	// Cursor normal = SWTResourceManager.getCursor(SWT.CURSOR_ARROW);
	// Cursor resizew = SWTResourceManager.getCursor(SWT.CURSOR_SIZEE);
	private Text text_1;
	boolean forwardSearch = true, caseSensitive, wholeWord, showAll, regExpression, currentFile = true;
	private MenuItem mntmNewCheckbox;
	private MenuItem menuItem_39;
	private MenuItem menuItem_40;
	private MenuItem menuItem_41;
	private MenuItem menuItem_42;
	private MenuItem menuItem_43;
	private MenuItem menuItem_44;
	private MenuItem menuItem_45;
	ArrayList<String> fileindex;
	Label label_4;
	public writingView wv;
	TextViewerUndoManager tvum;
	private MenuItem mntmF_2;
	File lastOpenedFile;
	String updateinfo;
	boolean donotChangeTitleBar;
	public  MenuItem menuItem_git;

	public static String getAppVersion() {
		return appVersion;
	}

	public static String getBuildDate() {
		return buildDate;
	}

	public Display getDisplay() {
		return super.getDisplay();
	}

	public void disposeTextArea() {
		if (text != null) {
			text.dispose();
			text = null; // 销毁text并清空
			setFileIsSave(1); // 在销毁文本域的同时要重置文件保存状态
			if (wv != null && !wv.isDisposed()) {
				wv.slider.dispose();
				wv.slider = null;
			}
		}

	}

	/**
	 * 设置自动保存的时间
	 * 
	 * @param i
	 *            秒数，如果为-1则停止自动保存
	 */
	public void setAutoSave(int i) {
		if (i != -1) {
			if (autoSave != null)
				autoSave.stop();
			autoSave = new autoSaveTimer(this);
			autoSave.setTimeForAutoSave(i);
			autoSave.start();
		} else if (autoSave != null)
			autoSave.stop();

		appProperties.setProperty("AutoSave", String.valueOf(i));
	}

	public Properties getAppProperties() {
		return appProperties;
	}

	/**
	 * 设置编辑器字体
	 * 
	 */
	public void setEditorFont(String fontname) {
		if (text != null) {
			Font f = SWTResourceManager.getFont(fontname, getZoomedFontSize(ba.getEditerDefaultFontSize()), SWT.None);
			text.setFont(f);
		}
		appProperties.setProperty("EditorDefaultFont", String.valueOf(fontname));

	}

	/**
	 * 从配置文件中获取编辑器字体
	 * 
	 * @return 字体的名称
	 */
	public String getEditorDefaultFontFromCFG() {
		return appProperties.getProperty("EditorDefaultFont", "宋体");
	}

	/**
	 * 设置编辑器缩放值
	 * 
	 * @param zoomvalue
	 */
	public void setEditorZoom(int zoomvalue) {
		if (text != null) {
			if (zoomvalue >= 100 && zoomvalue <= 400) {
				text.setRedraw(false);
				text.setFont(SWTResourceManager.getFont(getEditorDefaultFontFromCFG(), getZoomedFontSize(10, zoomvalue),
						SWT.NORMAL));
				StyleRange[] sr = text.getStyleRanges();
				for (StyleRange s : sr) {
					if (s.font != null) {
						int size = s.font.getFontData()[0].getHeight();
						int i = getZoomedFontSize(getFontRealSize(size), zoomvalue);
						Font font = SWTResourceManager.getFont(s.font.getFontData()[0].getName(), i, s.fontStyle);
						TextStyle ts = new TextStyle(font, s.foreground, s.background);
						ts.strikeout = s.strikeout;
						ts.underline = s.underline;
						ts.strikeoutColor = s.strikeoutColor;
						ts.underlineColor = s.underlineColor;
						ts.underlineStyle = s.underlineStyle;
						StyleRange fontStyle = new StyleRange(ts);
						fontStyle.start = s.start;
						fontStyle.length = s.length;
						text.setStyleRange(fontStyle);
					}

				}
				text.setRedraw(true);
				appProperties.setProperty("EditerZoom", String.valueOf(zoomvalue));
			}
		}

	}

	/**
	 * 获取缩放后的字体大小
	 * 
	 * @param oldfontsize
	 * @return
	 */
	public int getZoomedFontSize(int oldfontsize) {
		int i = getEditorZoomFromCFG() / 10 - 10 + oldfontsize;
		return i;
	}

	public int getFontRealSize(int zoomedfontsize) {
		int i = zoomedfontsize - (getEditorZoomFromCFG() / 10 - 10);
		return i;
	}

	public int getZoomedFontSize(int oldfontsize, int zoomvalue) {
		int i = zoomvalue / 10 - 10 + oldfontsize;
		return i;
	}

	public int getFontRealSize(int zoomedfontsize, int zoomvalue) {
		int i = zoomedfontsize - (zoomvalue / 10 - 10);
		return i;
	}

	/**
	 * 从配置文件中获取编辑器字体的字号
	 * 
	 * @param fontSize
	 *            编辑器字体的字号
	 */
	public int getEditorZoomFromCFG() {
		return Integer.valueOf(appProperties.getProperty("EditerZoom", "100"));
	}

	public void changeSaveLabelState() {
		if (getCurrentEditFile() != null) {
			// 编辑的是项目内的文件
			if (currentEditFile.getParent().equals(projectFile.getParent() + "\\Files")) {
				String title = ba.getShowNameByRealName(getCurrentEditFile().getName());
				String str = title + " - Black";
				String str_ = title + "* - Black";
				String strWv = title + " - 写作视图";
				String strWv_ = title + "* - 写作视图";

				if (fileIsSave == 0) {
					setText(str_);
					if (wv != null && !wv.isDisposed()) {
						wv.setText(strWv_);
					}
				} else {

					if (wv != null && !wv.isDisposed()) {
						wv.setText(strWv);
					}
				}
			} else {
				String title = currentEditFile.getName();
				String str = title + " - Black";
				String str_ = title + "* - Black";
				String strWv = title + " - 写作视图";
				String strWv_ = title + "* - 写作视图";
				if (fileIsSave == 0) {
					setText(str_);
					if (wv != null && !wv.isDisposed()) {
						wv.setText(strWv_);
					}
				} else {

					if (wv != null && !wv.isDisposed()) {
						wv.setText(strWv);
					}
				}
			}

		}
	}

	/**
	 * 设置文件是否保存
	 * 
	 * @param isSave
	 *            1为已保存，0为未保存
	 */
	public void setFileIsSave(int isSave) {
		fileIsSave = isSave;
		applySetting();
	}

	public int getFileIsSave() {
		return fileIsSave;
	}

	public void stopAutoSaveTimer() {
		if (autoSave != null)
			autoSave.stop();
	}

	public String getText() {
		return super.getText();
	}

	public boolean isDisposed() {
		return super.isDisposed();
	}

	/**
	 * 默认构造器
	 * 
	 * @wbp.parser.constructor
	 * 
	 */
	public black() {
		super(Display.getDefault(), SWT.SHELL_TRIM);
		log.setLayoutData(new FormData());
		// if (safe.isSafe())
		// create();
		// else
		// ba.getMessageBox("", "此程序已过评估期限，如要继续使用，请联系开发者获得授权");
		/*
		 * fortest test = new fortest(this, SWT.DIALOG_TRIM); boolean isok =
		 * test.open(); if(isok) create(); else System.exit(0);
		 */

		autoDO doing = new autoDO(1) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				if (updateinfo != null) {
					autoDO doingthis = this;
					getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							doingthis.stop();
							ba.showUpdateinof();
							updateinfo = null;
						}
					});
				} else {
					this.stop();
				}
			}
		};
		doing.start();
		Thread t = new Thread(new Runnable() {
			public void run() {
				ba.getNew();
			}
		});
		t.start();
		create();
		ba.debugMode(ba.isDebugMode(), true);
		// createMenuBar();
		// createVscrollBar(text,this);
	}

	public void create() {
		createContents();
		//applySetting();
		setAutoSave(Integer.valueOf(appProperties.getProperty("AutoSave", "300")));

		if (ba.IsReOpenLastProjectOnStarted()) {
			reOpenLastUsedProject();
		}

		if (ba.isWritingView()) {
			ba.restoreWritingView();
		} else {
			open();
		}
	}

	/**
	 * 销毁程序
	 */
	@Override
	public void dispose() {
		if (getCurrentEditFile() != null) {
			projectProperties.setProperty("lastUsedFile", getCurrentEditFile().toString());
			if (lastOpenedFile != null)
				setAppProperties("LastOpenedFile", lastOpenedFile.toString());
		}
		donotChangeTitleBar = true;
		closeCurrentFile(true);
		saveAppData();
		if (projectProperties != null && projectFile != null) {
			ba.saveProjectCFG();
			ba.saveFileindex();
			ba.writeToMarkFile();
		}
		super.saveAppData();
		ba.whenAppClose();
		System.exit(0);
	}

	/**
	 * 保存文件数据
	 */
	public void saveAppData() {
		if (projectFile != null) {
			appProperties.setProperty("LastUsedProject", projectFile.getAbsolutePath());
		}
	}

	/**
	 * 从配置文件中获取编辑器行距
	 * 
	 * @return
	 */
	public int getEditerLineSpace() {
		return Integer.valueOf(appProperties.getProperty("EditerLineSpace", "30"));
	}

	/**
	 * 设置编辑器行距
	 * 
	 * @param size
	 */
	public void setEditerLineSpace(int size) {
		appProperties.setProperty("EditerLineSpace", String.valueOf(size));
		if (getEditer() != null)
			getEditer().setLineSpacing(size);
	}

	/**
	 * 从配置文件中获取编辑器首行缩进值
	 * 
	 * @return
	 */
	public int getEditerIndent() {
		return Integer.valueOf(appProperties.getProperty("EditerIndent", "50"));
	}

	/**
	 * 设置编辑器的首行缩进值
	 * 
	 * @param value
	 */
	public void setEditerIndent(int value) {
		appProperties.setProperty("EditerIndent", String.valueOf(value));
		if (getEditer() != null)
			getEditer().setIndent(value);
	}

	/**
	 * Create contents of the shell.
	 */
	/**
	 * 
	 */
	protected void createContents() {
		setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/app.ico"));
		setBackground(color_white);
		setLayout(new FormLayout());
		setMinimumSize(500, 400);
		setText("Black");
		composite = new Composite(this, SWT.NONE);

		fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100);
		fd_composite.left = new FormAttachment(0);
		fd_composite.bottom = new FormAttachment(100);
		composite.setLayoutData(fd_composite);
		composite.setBackgroundImage(null);
		composite.setLayout(new FormLayout());
		composite.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		tree = new Tree(this, SWT.MULTI);

		fd_composite.top = new FormAttachment(tree, 1);
		FormData fd_text_11 = new FormData();
		fd_text_11.left = new FormAttachment(48, -1);

		DragSource ds = new DragSource(tree, DND.FEEDBACK_NONE);
		final TreeDragSourceEffect tdse = new TreeDragSourceEffect(tree);
		ds.setDragSourceEffect(tdse);

		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new DragSourceListener() {

			@Override
			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				mouseDrag = true;
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				event.data = tree.getSelection()[0].getText();

			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				mouseDrag = false;
			}
		});

		DropTarget dt = new DropTarget(tree, DND.FEEDBACK_NONE);
		final TreeDropTargetEffect tdte = new TreeDropTargetEffect(tree);
		dt.setDropTargetEffect(tdte);

		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new DropTargetListener() {

			@Override
			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void drop(DropTargetEvent event) {
				TreeItem ti = (TreeItem) tdte.getItem(event.x, event.y);// 目标
				if (ti != null) {
					if (ti.equals(treeItem_1)
							|| (ti.getParentItem() != null && ti.getParentItem().equals(treeItem_1))) {
						if (tree.getSelection()[0].getParentItem() != null
								&& tree.getSelection()[0].getParentItem().equals(draft)) {
							ba.moveFileToRecycle();
						}
					} else if (ti.equals(draft) || (ti.getParentItem() != null && ti.getParentItem().equals(draft))) {
						if (tree.getSelection()[0].getParentItem() != null
								&& tree.getSelection()[0].getParentItem().equals(treeItem_1)) {
							ba.restoreFile();
						} else if (tree.getSelection()[0].getParentItem() != null
								&& tree.getSelection()[0].getParentItem().equals(draft)) {
							if (!ti.equals(draft)) {
								String newindex_realname = (String) ti.getData("realname");
								String realname = (String) tree.getSelection()[0].getData("realname");
								fileindex.remove(realname);
								int newindex = fileindex.indexOf(newindex_realname);
								fileindex.add(newindex, (String) tree.getSelection()[0].getData("realname"));
								listProjectFile();
							}
						}
					}
				}
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}
		});

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setLayout(null);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.left = new FormAttachment(100, -230);
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.right = new FormAttachment(100);
		fd_composite_1.bottom = new FormAttachment(0, 25);
		composite_1.setLayoutData(fd_composite_1);

		text_1 = new Text(composite_1, SWT.CENTER);
		text_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		text_1.setBounds(31, 4, 135, 17);
		text_1.setMessage("\u952E\u5165\u6587\u672C\u4EE5\u68C0\u7D22\u6574\u4E2A\u9879\u76EE");
		text_1.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == 13) {
					if (getEditer() != null) {
						ba.hideFindResult();
						if (currentFile)
							blackTextArea.findWord(tv.getDocument(), text_1.getText(), forwardSearch, caseSensitive,
									wholeWord, showAll, regExpression);
						else
							ba.findInAllFiles(text_1.getText(), forwardSearch, caseSensitive, wholeWord, showAll,
									regExpression);
					}
				}
			}
		});

		Menu menu = new Menu(text_1);
		text_1.setMenu(menu);

		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.copy();
			}
		});
		mntmNewItem.setText("\u590D\u5236");

		MenuItem menuItem_1 = new MenuItem(menu, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.paste();
			}
		});
		menuItem_1.setText("\u7C98\u8D34");

		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem menuItem_2 = new MenuItem(menu, SWT.NONE);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.cut();
			}
		});
		menuItem_2.setText("\u526A\u5207");

		new MenuItem(menu, SWT.SEPARATOR);

		MenuItem menuItem_3 = new MenuItem(menu, SWT.NONE);
		menuItem_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.selectAll();
			}
		});
		menuItem_3.setText("\u5168\u9009");

		Label label = new Label(composite_1, SWT.NONE);
		ba.setColorForLabel(label);
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setToolTipText("\u641C\u7D22");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (getEditer() != null) {
					ba.hideFindResult();
					if (currentFile)
						blackTextArea.findWord(tv.getDocument(), text_1.getText(), forwardSearch, caseSensitive,
								wholeWord, showAll, regExpression);
					else
						ba.findInAllFiles(text_1.getText(), forwardSearch, caseSensitive, wholeWord, showAll,
								regExpression);
				}
			}
		});
		label.setBounds(202, 0, 28, 25);
		label.setAlignment(SWT.CENTER);
		label.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/zoom_icon&16.png"));

		final Label label_1 = new Label(composite_1, SWT.NONE);
		ba.setColorForLabel(label_1);
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setToolTipText("\u641C\u7D22\u9009\u9879");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
			}

			public void mouseUp(MouseEvent e) {
				label_1.getMenu().setVisible(true);
			}
		});
		label_1.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/br_up_icon&16.png"));
		label_1.setAlignment(SWT.CENTER);
		label_1.setBounds(0, 0, 25, 25);

		Menu menu_1 = new Menu(label_1);
		label_1.setMenu(menu_1);

		mntmNewCheckbox = new MenuItem(menu_1, SWT.CHECK);
		mntmNewCheckbox.setSelection(true);
		mntmNewCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentFile = mntmNewCheckbox.getSelection();
				menuItem_39.setSelection(false);
			}
		});
		mntmNewCheckbox.setText("\u4EC5\u641C\u7D22\u5F53\u524D\u6587\u4EF6");

		menuItem_39 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_39.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mntmNewCheckbox.setSelection(false);
				currentFile = !menuItem_39.getSelection();
			}
		});
		menuItem_39.setText("\u641C\u7D22\u6574\u4E2A\u9879\u76EE");

		new MenuItem(menu_1, SWT.SEPARATOR);

		menuItem_40 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_40.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				caseSensitive = menuItem_40.getSelection();
			}
		});
		menuItem_40.setText("\u533A\u5206\u5927\u5C0F\u5199");

		menuItem_41 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_41.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wholeWord = menuItem_41.getSelection();
			}
		});
		menuItem_41.setText("\u5168\u5B57\u5339\u914D");

		final MenuItem menuItem = new MenuItem(menu_1, SWT.CHECK);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				regExpression = menuItem.getSelection();
			}
		});
		menuItem.setText("\u5C06\u5173\u952E\u5B57\u4F5C\u4E3A\u6B63\u5219\u8868\u8FBE\u5F0F\u89E3\u6790");

		new MenuItem(menu_1, SWT.SEPARATOR);

		menuItem_42 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_42.setSelection(true);
		menuItem_42.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showAll = !menuItem_42.getSelection();
				menuItem_43.setSelection(false);
			}
		});
		menuItem_42.setText("\u4EC5\u663E\u793A\u5355\u9879");

		menuItem_43 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_43.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showAll = menuItem_42.getSelection();
				menuItem_42.setSelection(false);
			}
		});
		menuItem_43.setText("\u663E\u793A\u6240\u6709");

		new MenuItem(menu_1, SWT.SEPARATOR);

		menuItem_44 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_44.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				forwardSearch = !menuItem_44.getSelection();
				menuItem_45.setSelection(false);
			}
		});
		menuItem_44.setText("\u5411\u4E0A\u641C\u7D22");

		menuItem_45 = new MenuItem(menu_1, SWT.CHECK);
		menuItem_45.setSelection(true);
		menuItem_45.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				forwardSearch = menuItem_45.getSelection();
				menuItem_44.setSelection(false);
			}
		});
		menuItem_45.setText("\u5411\u4E0B\u641C\u7D22");

		Label label_2 = new Label(composite_1, SWT.NONE);
		ba.setColorForLabel(label_2);
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				blackTextArea.clearSelection();
				text_1.setText("");
				if (findresult != null) {
					findresult.dispose();
				}
			}
		});
		label_2.setToolTipText("\u9000\u51FA\u641C\u7D22");
		label_2.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/delete_icon&16.png"));
		label_2.setAlignment(SWT.CENTER);
		label_2.setBounds(172, 0, 27, 25);

		label_4 = new Label(composite, SWT.DOUBLE_BUFFERED);
		label_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ba.wordCountStat(black.this);
			}
		});
		label_4.setToolTipText("\u5355\u51FB\u6B64\u5904\u53EF\u67E5\u770B\u5B57\u6570\u7EDF\u8BA1\u4FE1\u606F");
		label_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_4.setAlignment(SWT.RIGHT);
		FormData fd_label_4 = new FormData();
		fd_label_4.left = new FormAttachment(100, -362);
		fd_label_4.right = new FormAttachment(100, -240);
		fd_label_4.bottom = new FormAttachment(100, -4);
		label_4.setLayoutData(fd_label_4);

		Label label_5 = new Label(composite, SWT.NONE);
		ba.setColorForLabel(label_5);
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ba.addFileToProject(null);
			}
		});
		label_5.setToolTipText("\u5411\u9879\u76EE\u91CC\u6DFB\u52A0\u6587\u4EF6");
		label_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_5.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/round_plus_icon&16.png"));
		label_5.setAlignment(SWT.CENTER);
		FormData fd_label_5 = new FormData();
		fd_label_5.bottom = new FormAttachment(100, -2);
		fd_label_5.top = new FormAttachment(0);
		fd_label_5.left = new FormAttachment(0);
		fd_label_5.right = new FormAttachment(0, 29);
		label_5.setLayoutData(fd_label_5);

		Label label_6 = new Label(composite, SWT.NONE);
		ba.setColorForLabel(label_6);

		label_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ba.moveFileToRecycle();
			}
		});
		label_6.setToolTipText("\u5220\u9664\u6240\u9009\u62E9\u7684\u6587\u4EF6");
		label_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_6.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/round_minus_icon&16.png"));
		label_6.setAlignment(SWT.CENTER);
		FormData fd_label_6 = new FormData();
		fd_label_6.bottom = new FormAttachment(100, -2);
		fd_label_6.top = new FormAttachment(0);
		fd_label_6.right = new FormAttachment(label_5, 35, SWT.RIGHT);
		fd_label_6.left = new FormAttachment(label_5, 6);
		label_6.setLayoutData(fd_label_6);

		Label label_8 = new Label(composite, SWT.NONE);
		ba.setColorForLabel(label_8);
		label_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ba.openWritingView();
			}
		});
		label_8.setToolTipText("\u5199\u4F5C\u89C6\u56FE");
		label_8.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/coffe_cup_icon&16.png"));
		label_8.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_8.setAlignment(SWT.CENTER);
		FormData fd_label_8 = new FormData();
		fd_label_8.bottom = new FormAttachment(100, -2);
		fd_label_8.top = new FormAttachment(0);
		fd_label_8.right = new FormAttachment(label_6, 35, SWT.RIGHT);
		fd_label_8.left = new FormAttachment(label_6, 6);
		label_8.setLayoutData(fd_label_8);

		tree.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				if (!mouseDrag && e.button == 1)
					ba.openFile();
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

				if (tree.getSelectionCount() == 1) {
					TreeItem[] tis = tree.getSelection();
					if (tis[0].equals(draft) || tis[0].equals(treeItem_1)) {
						tis[0].setExpanded(!tis[0].getExpanded());
					} else {
						Object result = new rename(black.this, ba.RENAME).open();
						if (result != null) {
							tis[0].setText((String) result);
							ba.rename((String) tis[0].getData("realname"), (String) result);
						}
					}
				}
			}
		});
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

		draft = new TreeItem(tree, SWT.None);
		draft.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/folder_icon&16.png"));
		draft.setText("草稿");

		draft.setExpanded(true);
		FormData fd_tree = new FormData();
		fd_tree.top = new FormAttachment(0);
		fd_tree.left = new FormAttachment(0);
		fd_tree.bottom = new FormAttachment(100, -28);
		fd_tree.right = new FormAttachment(0, 156);
		tree.setLayoutData(fd_tree);
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		menu_6 = new Menu(tree);
		tree.setMenu(menu_6);
		mntmCtrlshift = new MenuItem(menu_6, SWT.NONE);
		mntmCtrlshift.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/doc_plus_icon&16.png"));
		mntmCtrlshift.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.addFileToProject(null);
			}
		});
		mntmCtrlshift.setText("\u52A0\u5165\u65B0\u6587\u4EF6\tCtrl+Shift=");

		new MenuItem(menu_6, SWT.SEPARATOR);

		mntmCtrlshift_1 = new MenuItem(menu_6, SWT.NONE);
		mntmCtrlshift_1
				.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/doc_delete_icon&16.png"));
		mntmCtrlshift_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.moveFileToRecycle();
			}
		});
		mntmCtrlshift_1.setText("\u5220\u9664\u6240\u9009\u7684\u6587\u4EF6\tCtrl+Shift-");

		new MenuItem(menu_6, SWT.SEPARATOR);

		MenuItem mntmCtrlshiftr = new MenuItem(menu_6, SWT.NONE);
		mntmCtrlshiftr
				.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/round_and_up_icon&16.png"));
		mntmCtrlshiftr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.restoreFile();
			}
		});
		mntmCtrlshiftr.setText("\u8FD8\u539F\u6587\u4EF6\tCtrl+Shift+R");

		mntmCtrlr = new MenuItem(menu_6, SWT.NONE);
		mntmCtrlr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] tis = tree.getSelection();
				for (TreeItem ti : tis) {
					if (!ti.equals(draft) && !ti.equals(treeItem_1)) {
						if (!ti.getParentItem().equals(treeItem_1)) {
							Object result = new rename(black.this, ba.RENAME).open();
							if (result != null) {
								ti.setText((String) result);
								ba.rename((String) ti.getData("realname"), (String) result);
							}
						}
					}
				}
			}
		});
		mntmCtrlr.setText("\u91CD\u547D\u540D\u6240\u9009\u7684\u6587\u4EF6\tCtrl+R");

		new MenuItem(menu_6, SWT.SEPARATOR);

		MenuItem mntmCtrlshiftr_1 = new MenuItem(menu_6, SWT.NONE);
		mntmCtrlshiftr_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.clearRecycle();
			}
		});
		mntmCtrlshiftr_1.setText("\u6E05\u7A7A\u5783\u573E\u7BB1\tCtrl+Shift+C");

		treeItem_1 = new TreeItem(tree, 0);
		treeItem_1.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/trash_icon&16.png"));
		treeItem_1.setText("\u5783\u573E\u7BB1");
		treeItem_1.setExpanded(true);

		// createButtonPanel();
		createMenuBar();
//		if (ba.isShowMenuBar())
//			createMenuBar();
	}

	public void reOpenLastUsedProject() {

		if (appProperties.getProperty("LastUsedProject") != null) {
			ba.openProject(new File(appProperties.getProperty("LastUsedProject")));
		} else {
			ba.openNewProjectDialog();
		}
	}

	@SuppressWarnings("unchecked")
	public void setProject(Properties projectprojerties, File file) {
		projectFile = file;
		this.projectProperties = projectprojerties;
		File fileinfofile = new File(file.getParent() + "\\Settings\\fileinfo");
		File recyclefile = new File(projectFile.getParent() + "\\Settings\\recycle");
		File fileindex_file = new File(projectFile.getParent() + "\\Settings\\fileindex.blaobj");
		if (recyclefile.exists())
			recycle = cfg_read_write.cfg_read(recyclefile);
		if (fileinfofile.exists())
			fileInfo = cfg_read_write.cfg_read(fileinfofile);
		FileCountOfProject = Integer.valueOf(projectProperties.getProperty("fileCount"));
		ioThread io = new ioThread(this, null, fileindex_file, 0);
		getDisplay().syncExec(io);
		if (io.o != null)
			fileindex = (ArrayList<String>) io.o;
		else
			fileindex = new ArrayList<String>();

		if (getCurrentEditFile() != null && text != null) {
			closeCurrentFile(true);
		}
		listProjectFile();
		String lastusedfile = projectprojerties.getProperty("lastUsedFile");
		// 重新加载文档切换信息
		String lastOpenedfilePath = getAppProperties("LastOpenedFile");
		if (lastOpenedfilePath != null) {
			lastOpenedFile = new File(lastOpenedfilePath);
		}

		if (lastusedfile != null) {
			File lastused = new File(lastusedfile);
			if (lastused.exists())
				if (getIO().open(lastused)) {
					resetStyledTextTopPixelAndCaretOffset(lastused.getName(), text);
				}
		}
	}

	public void listProjectFile() {
		draft.clearAll(true);
		treeItem_1.clearAll(true);
		draft.setItemCount(0);
		treeItem_1.setItemCount(0);
		if (fileindex != null) {
			Iterator<String> it = fileindex.iterator();
			while (it.hasNext()) {
				String file = it.next();
				File f = new File(projectFile.getParent() + "\\Files\\" + file);
				if (f.exists()) {
					TreeItem treeItem;
					if (recycle != null) {
						String inrecycle = recycle.getProperty(file);
						if (inrecycle != null) {
							if (inrecycle.equals("true")) {
								treeItem = getDocTreeItem(treeItem_1, "");
							} else
								treeItem = getDocTreeItem(draft, "");
						} else
							treeItem = getDocTreeItem(draft, "");
					} else
						treeItem = getDocTreeItem(draft, "");

					treeItem.setData("realname", file);
					treeItem.setText(fileInfo.getProperty(file, file));
				} else {
					it.remove();
				}
			}
		}

		draft.setExpanded(true);
	}

	/**
	 * 重设StyledText的内容位置
	 * 
	 * @param filename
	 *            不包含路径名的文件名，如1.black
	 */
	public void resetStyledTextTopPixelAndCaretOffset(String filename, StyledText text) {
		text.setTopPixel(Integer.valueOf(fileInfo.getProperty(filename + "TopPixel", "0")));
		text.setCaretOffset(Integer.valueOf(fileInfo.getProperty(filename + "CaretOffset", "0")));
		backtext = text.getText();
		// tv.setRedraw(true);
	}

	public void setCurrentTextArea(StyledText textArea) {
		if (text != null)
			text.dispose();
		this.text = textArea;
		resetLayoutData();

	}

	public void setCurrentEditFile(File file) {
		// 如果文件不为空就将文件记录在历史信息里，一遍快速切换文件
		if (currentEditFile != null) {
			lastOpenedFile = currentEditFile;
		}
		currentEditFile = file;
	}

	public File getCurrentEditFile() {
		return currentEditFile;

	}

	public void resetLayoutData() {
		if (text != null) {
			FormData fd_styledText = new FormData();
			if (composite.getVisible())
				fd_styledText.bottom = new FormAttachment(composite, 1);
			else
				fd_styledText.bottom = new FormAttachment(100);
			fd_styledText.right = new FormAttachment(100);
			fd_styledText.top = new FormAttachment(0);
			if (tree.getVisible())
				fd_styledText.left = new FormAttachment(tree, 1);
			else
				fd_styledText.left = new FormAttachment(0);
			text.setLayoutData(fd_styledText);
			layout();
		}
	}

	// public void toWritingView() {
	// // setFullScreen(true);
	// setMaximized(true);
	// setLayoutDeferred(true);
	// System.out.println(new Point(getDisplay().getClientArea().width,
	// getDisplay().getClientArea().height));
	// //
	// setSize(getDisplay().getClientArea().width,getDisplay().getClientArea().height);
	// tree.setVisible(false);
	// composite.setVisible(false);
	// getMenuBar().dispose();
	// setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
	// }

	/**
	 * 创建编辑器
	 * 
	 * @return
	 */
	public void createTextArea(TextViewerUndoManager undo) {
		if (wv == null || wv.isDisposed()) {

			tv = new TextViewer(this, SWT.WRAP | SWT.V_SCROLL);
			StyledText styledText = tv.getTextWidget();
			if (undo != null) {
				undo.disconnect();
				undo.connect(tv);
			} else
				tvum = new TextViewerUndoManager(100);
			tv.setUndoManager(tvum);
			tv.activatePlugins();
			setCurrentTextArea(styledText);
			blackTextArea = new blackTextArea(styledText, this);
		} else {
			// wv.disposeTextArea();
			wv.createTextArea(null);
		}
	}

	public void createMenuBar() {
		Menu menu = new Menu(this, SWT.BAR);
		menu.setLocation(new Point(0, 0));
		setMenuBar(menu);

		MenuItem mntmF_4 = new MenuItem(menu, SWT.CASCADE);
		mntmF_4.setAccelerator(100);
		mntmF_4.setText("\u6587\u4EF6");

		Menu menu_1 = new Menu(mntmF_4);
		mntmF_4.setMenu(menu_1);

		MenuItem mntmCtrln = new MenuItem(menu_1, SWT.NONE);

		mntmCtrln.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.openNewProjectDialog();

			}
		});
		mntmCtrln.setText("\u65B0\u5EFA/\u6253\u5F00\u9879\u76EE\tCtrl+N");

		MenuItem saveas = ba.getMenuItem(menu_1, "以纯文本格式转存文件(UTF-8编码)", SWT.CASCADE);
		Menu menusaveas = new Menu(saveas);
		saveas.setMenu(menusaveas);
		MenuItem saveallas = ba.getMenuItem(menusaveas, "转存项目中的所有文件至指定的目录", SWT.NONE);
		saveallas.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.saveAllAsText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		MenuItem saveastxt = ba.getMenuItem(menusaveas, "转存当前所编辑的文件", SWT.None);
		saveastxt.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.saveAsText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem saveastxttoone = ba.getMenuItem(menusaveas, "转存项目中的所有文件到一个文件", SWT.NONE);
		saveastxttoone.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.saveAllAsTextToOneFile();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem open = ba.getMenuItem(menu_1, "打开文件", SWT.None);
		open.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.openFileWithoutProject();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmCtrlshifti = new MenuItem(menu_1, SWT.NONE);
		mntmCtrlshifti.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/import_icon&16.png"));
		mntmCtrlshifti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new importFiles(black.this, SWT.None).open();
			}
		});
		mntmCtrlshifti.setText("\u5BFC\u5165\tCtrl+I");

		MenuItem mntmCtrlshifte = new MenuItem(menu_1, SWT.NONE);
		mntmCtrlshifte.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/export_icon&16.png"));
		mntmCtrlshifte.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new exportFiles(black.this, SWT.None).open();
			}
		});
		mntmCtrlshifte.setText("\u5BFC\u51FA\tCtrl+E");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem backup = ba.getMenuItem(menu_1, "备份", SWT.CASCADE);
		Menu backupmenu = new Menu(backup);
		backup.setMenu(backupmenu);
		MenuItem startbackup = ba.getMenuItem(backupmenu, "开始备份", SWT.NONE);
		startbackup.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.startBackup();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem setbackupdir = ba.getMenuItem(backupmenu, "设置备份目录并开始备份", SWT.NONE);
		setbackupdir.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (ba.setBackupDir())
					ba.startBackup();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem openbackupdir = ba.getMenuItem(backupmenu, "打开备份目录", SWT.None);
		openbackupdir.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (ba.getBackupDir() != null)
					ba.showinExplorer(ba.getBackupDir() + "\\", false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem menuItem_11 = new MenuItem(menu_1, SWT.NONE);
		menuItem_11.setImage(null);
		menuItem_11.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.exitProgram();
			}
		});
		menuItem_11.setText("\u9000\u51FA\u7A0B\u5E8F");

		MenuItem menuItem_5 = new MenuItem(menu, SWT.CASCADE);
		menuItem_5.setAccelerator(SWT.ALT | 'e');
		menuItem_5.addArmListener(new ArmListener() {
			@Override
			public void widgetArmed(ArmEvent e) {
				if (tv != null) {
					mntmCtrlz.setEnabled(tv.canDoOperation(ITextOperationTarget.UNDO));
					mntmCtrlx.setEnabled(tv.canDoOperation(ITextOperationTarget.REDO));
				}
			}
		});

		menuItem_5.setText("\u7F16\u8F91");

		Menu menu_2 = new Menu(menuItem_5);
		menuItem_5.setMenu(menu_2);

		mntmCtrlz = new MenuItem(menu_2, SWT.NONE);
		mntmCtrlz.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/undo_icon&16.png"));

		mntmCtrlz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.Undo();
			}
		});
		mntmCtrlz.setText("\u64A4\u9500\tCtrl+Z");

		mntmCtrlx = new MenuItem(menu_2, SWT.NONE);
		mntmCtrlx.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/redo_icon&16.png"));
		mntmCtrlx.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.Redo();
			}
		});
		mntmCtrlx.setText("\u91CD\u505A\tAlt+Z");

		new MenuItem(menu_2, SWT.SEPARATOR);

		MenuItem mntmCtrlc = new MenuItem(menu_2, SWT.NONE);
		mntmCtrlc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.copyText(text);
			}
		});
		mntmCtrlc.setText("\u590D\u5236\tCtrl+C");

		MenuItem mntmCtrlv = new MenuItem(menu_2, SWT.NONE);
		mntmCtrlv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.pasteText(text);
			}
		});
		mntmCtrlv.setText("\u7C98\u8D34\tCtrl+V");

		MenuItem mntmCtrlx_1 = new MenuItem(menu_2, SWT.NONE);
		mntmCtrlx_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.cutText(text);
			}
		});
		mntmCtrlx_1.setText("\u526A\u5207\tCtrl+X");

		new MenuItem(menu_2, SWT.SEPARATOR);

		MenuItem mntmCtrls = new MenuItem(menu_2, SWT.NONE);
		mntmCtrls.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ba.saveFile(false, false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		mntmCtrls.setText("\u4FDD\u5B58\u5F53\u524D\u7F16\u8F91\u7684\u6587\u4EF6\tCtrl+S");

		MenuItem showinExplorer = ba.getMenuItem(menu_2, "在资源管理器中显示当前所编辑的文件", SWT.None);
		showinExplorer.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.showFileInExplorer();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		new MenuItem(menu_2, SWT.SEPARATOR);

		MenuItem mntmCtrlshiftf = new MenuItem(menu_2, SWT.NONE);
		mntmCtrlshiftf.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/res/icon/magnify16.png"));
		mntmCtrlshiftf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.findReplaceWord();
			}
		});
		mntmCtrlshiftf.setText("\u67E5\u627E/\u66FF\u6362\tCtrl+F");

		MenuItem mntmCtrla = new MenuItem(menu_2, SWT.NONE);
		mntmCtrla.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/3x3_grid_icon&16.png"));
		mntmCtrla.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.selectAllText(text);
			}
		});
		mntmCtrla.setText("\u5168\u9009\tCtrl+A");

		MenuItem menuItem_3 = new MenuItem(menu, SWT.CASCADE);
		menuItem_3.setText("\u9879\u76EE");

		Menu menu_7 = new Menu(menuItem_3);
		menuItem_3.setMenu(menu_7);

		MenuItem mntmShift = new MenuItem(menu_7, SWT.NONE);
		mntmShift.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.addFileToProject(null);
			}
		});
		mntmShift.setText("\u5411\u9879\u76EE\u4E2D\u6DFB\u52A0\u65B0\u6587\u4EF6\tCtrl+Shift+=");

		MenuItem mntmShift_1 = new MenuItem(menu_7, SWT.NONE);
		mntmShift_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.moveFileToRecycle();
			}
		});
		mntmShift_1.setText(
				"\u5220\u9664\u9879\u76EE\u4E2D\u5F53\u524D\u6240\u9009\u62E9\u7684\u6587\u4EF6\tCtrl+Shift+-");

		new MenuItem(menu_7, SWT.SEPARATOR);

		MenuItem mntmCtrlshiftc_1 = new MenuItem(menu_7, SWT.NONE);
		mntmCtrlshiftc_1.setText("\u6E05\u7A7A\u5783\u573E\u7BB1\tCtrl+Shift+C");

		new MenuItem(menu_7, SWT.SEPARATOR);

		MenuItem mntmShifti = new MenuItem(menu_7, SWT.NONE);
		mntmShifti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new projectInfo(black.this, SWT.None).open();
			}
		});
		mntmShifti.setText("\u9879\u76EE\u5C5E\u6027\tCtrl+Shift+P");

		MenuItem finderror = ba.getMenuItem(menu_7, "校验项目里的所有文件", SWT.None);
		finderror.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.getFileInfo();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		MenuItem menuItem_31 = new MenuItem(menu, SWT.CASCADE);
		menuItem_31.setText("\u683C\u5F0F");

		Menu menu_8 = new Menu(menuItem_31);
		menuItem_31.setMenu(menu_8);

		MenuItem mntmCtrlf = new MenuItem(menu_8, SWT.NONE);
		mntmCtrlf.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/fill_icon&16.png"));
		mntmCtrlf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (text != null)
					new editerStyledPanel(black.this, null).open();
			}
		});
		mntmCtrlf.setText("\u663E\u793A\u683C\u5F0F\u677F\tCtrl+G");

		ba.getMenuItem(menu_8, SWT.SEPARATOR);
		MenuItem removeline = ba.getMenuItem(menu_8, "删除当前文档内的空行", SWT.NONE);
		removeline.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentEditFile != null && text != null) {
					String str = text.getText();
					while (str.indexOf("\n\n") != -1) {
						str = str.replace("\n\n", "\n");
					}
					text.setText(str);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem addline = ba.getMenuItem(menu_8, "在当前文档内的每个段落间添加空行", SWT.NONE);
		addline.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentEditFile != null && text != null) {
					String str = text.getText().replace("\n", "\n\n");
					text.setText(str);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		MenuItem deleteSpaceAtLineEnd = ba.getMenuItem(menu_8, "删除当前文档所有段落后的空格", SWT.NONE);
		deleteSpaceAtLineEnd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentEditFile != null && text != null)
					ba.deleteSpaceAtLineEnd();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		MenuItem menuItem_33 = new MenuItem(menu, SWT.CASCADE);
		menuItem_33.setText("\u63D2\u5165");

		Menu menu_9 = new Menu(menuItem_33);
		menuItem_33.setMenu(menu_9);

		MenuItem mntmctrlt = new MenuItem(menu_9, SWT.NONE);
		menuItem_43.setAccelerator(SWT.CONTROL | 't');
		mntmctrlt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.insertText(time.getCurrentDate("-"), text);
			}
		});
		mntmctrlt.setText("\u5F53\u524D\u65E5\u671F\tAlt+D");

		MenuItem mntmctrlt_1 = new MenuItem(menu_9, SWT.NONE);
		mntmctrlt_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.insertText(time.getCurrentTime(), text);
			}
		});
		mntmctrlt_1.setText("\u5F53\u524D\u65F6\u95F4\tAlt+T");

		menuItem_git = new MenuItem(menu, SWT.CASCADE);
		menuItem_git.setText("Git");

		Menu menu_git = new Menu(menuItem_git);
		menu_git.addMenuListener(new MenuListener() {
			
			@Override
			public void menuShown(MenuEvent arg0) {
				// TODO Auto-generated method stub
				if(!ba.gitSetUp()){
					MenuItem[] items = menu_git.getItems();
					for(int i=0;i<items.length;i++){
						if(i>0)items[i].setEnabled(false);
					}
				}else{
					MenuItem[] items = menu_git.getItems();
					for(int i=0;i<items.length;i++){
						if(i>0)items[i].setEnabled(true);
					}
				}
			}
			
			@Override
			public void menuHidden(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		menuItem_git.setMenu(menu_git);
		
		MenuItem mntmgit = new MenuItem(menu_git, SWT.NONE);
		mntmgit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				gitInfo gitinfo = new gitInfo(black.this, SWT.DIALOG_TRIM);
				gitinfo.open();
			}
		});
		mntmgit.setText("\u4E3A\u6B64\u9879\u76EE\u914D\u7F6EGit\u8FDC\u7A0B\u4ED3\u5E93");
		
		new MenuItem(menu_git, SWT.SEPARATOR);
		
		MenuItem menuItem_10 = new MenuItem(menu_git, SWT.NONE);
		menuItem_10.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(ba.gitSetUp())
				ba.commit(null,false);
			}
		});
		menuItem_10.setText("\u5C06\u9879\u76EE\u66F4\u6539\u63D0\u4EA4\u5230\u672C\u5730\u4ED3\u5E93");
		
		MenuItem menuItem_9 = new MenuItem(menu_git, SWT.NONE);
		menuItem_9.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(ba.gitSetUp()){
					getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							new showProgress(black.this,"上传至远程仓库") {
								
								@Override
								void actionWhenOKButtonSelected() {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								void actionInOtherThread() {
									// TODO Auto-generated method stub
									
								}
							};
						}
					});
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ba.setProgressInfo("连接远程仓库...", 20);
							String[] info = ba.getGitInfo();
							Iterable<PushResult> push = ba.push(info[0],info[1],info[2],true);
							//if(push != null)ba.getBMessageBox(title, text);
							ba.setProgressInfo("上传完成！", 100);
						}
					}).start();
				}
			}
		});
		menuItem_9.setText("\u5C06\u672C\u5730\u4ED3\u5E93\u7684\u66F4\u6539\u4E0A\u63A8\u81F3\u8FDC\u7A0B\u4ED3\u5E93");
		
		MenuItem menuItem_12 = new MenuItem(menu_git, SWT.NONE);
		menuItem_12.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(ba.gitSetUp()){
					String[] gitInfo = ba.getGitInfo();
					if(gitInfo != null){
						ba.gitWorking(gitInfo[0],gitInfo[1],gitInfo[2]);
					}
				}
			}
		});
		menuItem_12.setText("\u63D0\u4EA4\u9879\u76EE\u66F4\u6539\u5E76\u4E0A\u63A8\u81F3\u8FDC\u7A0B\u4ED3\u5E93");
		
		new MenuItem(menu_git, SWT.SEPARATOR);
		
		MenuItem menuItem_13 = new MenuItem(menu_git, SWT.NONE);
		menuItem_13.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(ba.gitSetUp())
					new gitBranchInfo(black.this, SWT.DIALOG_TRIM).open();
			}
		});
		menuItem_13.setText("\u5206\u652F\u64CD\u4F5C");
		
		
		MenuItem menuItem_8 = new MenuItem(menu, SWT.CASCADE);
		menuItem_8.setText("\u5DE5\u5177");

		Menu menu_5 = new Menu(menuItem_8);
		menuItem_8.setMenu(menu_5);

		MenuItem mntmCtrlshiftc = new MenuItem(menu_5, SWT.NONE);
		mntmCtrlshiftc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.wordCountStat(black.this);
			}
		});
		mntmCtrlshiftc.setText("\u5B57\u6570\u7EDF\u8BA1\tCtrl+K");

		MenuItem menuItem_14 = new MenuItem(menu_5, SWT.CASCADE);
		menuItem_14.setText("\u5185\u5BB9\u68C0\u67E5");

		Menu menu_10 = new Menu(menuItem_14);
		menuItem_14.setMenu(menu_10);

		MenuItem menuItem_20 = new MenuItem(menu_10, SWT.NONE);
		menuItem_20.setText("列出当前文档内的所有标题");
		menuItem_20.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.getTitleList();
			}
		});
		MenuItem mark = ba.getMenuItem(menu_5, "预定义文件", SWT.CASCADE);
		Menu markmenu = new Menu(mark);
		mark.setMenu(markmenu);
		MenuItem editmark = ba.getMenuItem(markmenu, "编辑预定义文件	Ctrl+M", SWT.NONE);
		editmark.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.editMarkFile();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem addtomark = ba.getMenuItem(markmenu, "将选择的词汇加入预定义文件	(选中词汇按下Enter键)", SWT.None);
		addtomark.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentEditFile != null && text != null)
					if (!getCurrentEditFile().equals(ba.getRealFile("预定义")))
						ba.addTextToMarkFile(text.getSelectionText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem removefrommark = ba.getMenuItem(markmenu, "将选择的词汇从预定义文件中移除	(选中词汇按下Enter键)", SWT.None);
		removefrommark.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentEditFile != null && text != null)
					if (!getCurrentEditFile().equals(ba.getRealFile("预定义")))
						ba.addTextToMarkFile(text.getSelectionText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		ba.getMenuItem(markmenu, SWT.SEPARATOR);
		MenuItem rereadmark = ba.getMenuItem(markmenu, "重新读取预定义文件并建立索引", SWT.None);
		removefrommark.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.readMarkFile();
				ba.getMarkFileText();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem clearmarkstat = ba.getMenuItem(markmenu, "清空预定义文件调频数据", SWT.NONE);
		clearmarkstat.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.markstat.removeAll(ba.markstat);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		MenuItem writemark = ba.getMenuItem(markmenu, "保存预定义文件中的改动", SWT.None);
		writemark.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.writeToMarkFile();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		MenuItem name = ba.getMenuItem(menu_5, "人名产生", SWT.CASCADE);
		Menu menuname = new Menu(name);
		name.setMenu(menuname);
		MenuItem chname = ba.getMenuItem(menuname, "产生一个中文人名	Ctrl+~", SWT.NONE);
		chname.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.getChineseName();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem chnames = ba.getMenuItem(menuname, "产生20个中文人名", SWT.NONE);
		chnames.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.getChineseNames(20);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		MenuItem itname = ba.getMenuItem(menuname, "产生一个意大利男名	F2", SWT.NONE);
		itname.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.getItalinaName('m');
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem itnamef = ba.getMenuItem(menuname, "产生一个意大利女名	F3", SWT.NONE);
		itnamef.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.getItalinaName('f');
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem enname = ba.getMenuItem(menuname, "产生一个英语男名	F4", SWT.NONE);
		enname.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.getEnglishName('m');
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		MenuItem ennamef = ba.getMenuItem(menuname, "产生一个英语女名	F5", SWT.NONE);
		ennamef.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.getEnglishName('f');
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		ba.getMenuItem(menuname, SWT.SEPARATOR);
		MenuItem readname = ba.getMenuItem(menuname, "重新读取人名数据", SWT.NONE);
		readname.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (namecreator == null) {
					namecreator = new nameCreator(black.this, new File("./nameCreator"));
				}
				namecreator.getallfiles();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new MenuItem(menu_5, SWT.SEPARATOR);
		ba.getMenuItem(menu_5, "Debug模式（默认启用）", SWT.None).setEnabled(false);
		;

		MenuItem mntmF = new MenuItem(menu_5, SWT.NONE);
		mntmF.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.showSettings();
			}
		});
		mntmF.setText("\u9996\u9009\u9879\tF1");

		MenuItem menuItem_6 = new MenuItem(menu, SWT.CASCADE);
		menuItem_6.addArmListener(new ArmListener() {
			@Override
			public void widgetArmed(ArmEvent e) {
				if (getCurrentEditFile() == null || getEditer() == null) {
					mntmF_2.setEnabled(false);

				} else {
					mntmF_2.setEnabled(true);
				}

			}
		});
		menuItem_6.setText("\u89C6\u56FE");

		Menu menu_3 = new Menu(menuItem_6);
		menuItem_6.setMenu(menu_3);

		MenuItem mntmCtrl = new MenuItem(menu_3, SWT.NONE);
		mntmCtrl.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/res/icon/zoom-in16.png"));
		mntmCtrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.zoomOut();
			}
		});
		mntmCtrl.setText("\u653E\u5927\tCtrl+=");

		MenuItem mntmCtrl_1 = new MenuItem(menu_3, SWT.NONE);
		mntmCtrl_1.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/res/icon/zoom-out16.png"));
		mntmCtrl_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.zoomIn();
			}
		});
		mntmCtrl_1.setText("\u7F29\u5C0F\tCtrl+-");

		new MenuItem(menu_3, SWT.SEPARATOR);

		mntmF_2 = new MenuItem(menu_3, SWT.NONE);
		mntmF_2.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/coffe_cup_icon&16.png"));
		mntmF_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.openWritingView();
			}
		});
		mntmF_2.setText("\u5199\u4F5C\u89C6\u56FE\tF11");

		MenuItem menuItem_7 = new MenuItem(menu, SWT.CASCADE);
		menuItem_7.setText("\u5E2E\u52A9");

		Menu menu_4 = new Menu(menuItem_7);
		menuItem_7.setMenu(menu_4);

		MenuItem menuItem = new MenuItem(menu_4, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.getSystemInfo();
			}
		});
		menuItem.setText("\u7CFB\u7EDF\u4FE1\u606F");

		MenuItem menuItem_1 = new MenuItem(menu_4, SWT.NONE);
		menuItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				autoDO doing = new autoDO(1) {

					@Override
					public void action() {
						// TODO Auto-generated method stub
						if (updateinfo != null) {
							autoDO doingthis = this;
							getDisplay().asyncExec(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									doingthis.stop();
									ba.showUpdateinof();
									updateinfo = null;
								}
							});
						} else
							this.stop();
					}
				};
				doing.start();
				Thread t = new Thread(new Runnable() {
					public void run() {
						ba.getNew();
					}
				});
				t.start();
			}
		});
		menuItem_1.setText("检查新版本");

		new MenuItem(menu_4, SWT.SEPARATOR);

		MenuItem menuItem_2 = new MenuItem(menu_4, SWT.NONE);
		// menuItem_2.setEnabled(false);
		menuItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.getBMessageBox("键盘快捷键", ba.keyinfo());
			}
		});
		menuItem_2.setText("\u5FEB\u6377\u952E\u6982\u89C8");

		MenuItem menuItem_4 = new MenuItem(menu_4, SWT.NONE);
		menuItem_4.setText("发送反馈");
		menuItem_4.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ba.reportInfo();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		new MenuItem(menu_4, SWT.SEPARATOR);

		MenuItem mntmAtla = new MenuItem(menu_4, SWT.NONE);
		mntmAtla.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/res/icon/about16.png"));
		mntmAtla.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ba.aboutBlack();
			}
		});
		mntmAtla.setText("\u5173\u4E8E\tCtrl+Shift+A");

		// Menu menu_6 = new Menu(tree);
		// tree.setMenu(menu_6);
		//
		// MenuItem menuItem_25 = new MenuItem(menu_6, SWT.NONE);
		// menuItem_25.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// TreeItem ti = new TreeItem(tree,SWT.None);
		// ti.setText(time.getCurrentTime());
		// }
		// });
		// menuItem_25.setText("\u52A0\u5165\u65B0\u6587\u4EF6");
	}

	public StyledText getEditer() {
		return text;
	}

	public io getIO() {
		return io;
	}

	public void closeThis() {
		stopAutoSaveTimer();
		// time.closeTask();
		dispose();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * 负责应用程序的设置，通常在程序中的某些设置更改后调用此方法，以应用更改
	 */
	public void applySetting() {
		if (getEditer() != null && currentEditFile != null) {
			String showname = "";
			if (currentEditFile.getParent().equals(projectFile.getParent() + "\\Files")) {
				showname = ba.getShowNameByRealName(currentEditFile.getName());
			} else {
				showname = currentEditFile.getName();
			}
			setText(showname + " - Black");
			if (wv != null && !wv.isDisposed())
				wv.setText(showname + " - 写作视图");
		}else {
			if(!donotChangeTitleBar)
				setText("Black");
		}
		changeSaveLabelState();
	}

	public void closeCurrentFile(boolean saveFileView) {
		if (getCurrentEditFile() != null) {
			if (currentEditFile.getParent().equals(projectFile.getParent() + "\\Files")) {
				saveCurrentFile(saveFileView, true);
			} else
				saveCurrentFile(saveFileView, false);
			blackTextArea.clearSelection();
			setCurrentEditFile(null);
			disposeTextArea();
			ba.closeFindInfoDialog();
			applySetting();
		}
	}

	/**
	 * 保存当前编辑的文件 附加的两个参数用来确定是否保存该文件最后浏览的视图位置和字数
	 * 
	 * @param saveFileView
	 * @param saveCharCount
	 */
	public void saveCurrentFile(boolean saveFileView, boolean saveCharCount) {
		if (fileIsSave == 0) {
			getIO().save(getCurrentEditFile());
			backtext = text.getText();
		}
		if (saveCharCount) {

			fileInfo.setProperty(getCurrentEditFile().getName() + "CharCount", String.valueOf(text.getCharCount()));
			fileInfo.setProperty(getCurrentEditFile().getName() + "ChineseCharCount",
					String.valueOf(wordCountStat.chineseWordCount(text)));
		}
		if (saveFileView) {
			fileInfo.setProperty(getCurrentEditFile().getName() + "TopPixel", String.valueOf(text.getTopPixel()));
			fileInfo.setProperty(getCurrentEditFile().getName() + "CaretOffset", String.valueOf(text.getCaretOffset()));
		}
		if (saveFileView || saveCharCount) {
			ba.saveFileInfoCFG();
		}
	}

	public TreeItem getDocTreeItem(TreeItem parent, String treeitemtext) {
		TreeItem trtmNewTreeitem = new TreeItem(parent, SWT.NONE);
		if (draft != null && parent.equals(draft))
			trtmNewTreeitem
					.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/document_icon&16.png"));
		if (treeItem_1 != null && parent.equals(treeItem_1))
			trtmNewTreeitem
					.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/doc_delete_icon&16.png"));
		trtmNewTreeitem.setText(treeitemtext);

		return trtmNewTreeitem;
	}

	public void showResultTreeItem() {
		if (findresult == null || findresult.isDisposed()) {
			findresult = new TreeItem(tree, SWT.None);
			findresult.setText("搜索结果");
			findresult.setImage(SWTResourceManager.getImage(black.class, "/yang/app/black/icons/zoom_icon&16.png"));
		}
	}

	public TreeItem addFindResultToTree(String showtext) {
		showResultTreeItem();
		TreeItem ti = new TreeItem(findresult, SWT.None);
		ti.setText(showtext);
		findresult.setExpanded(true);
		return ti;
	}
}
