package yang.app.qt.black;

import io.qt.core.QCoreApplication;
import io.qt.core.QEvent;
import io.qt.core.QEvent.Type;
import io.qt.core.QObject;
import io.qt.core.QPoint;
import io.qt.core.QRect;
import io.qt.core.QSize;
import io.qt.core.Qt;
import io.qt.core.Qt.GlobalColor;
import io.qt.core.Qt.Key;
import io.qt.core.Qt.KeyboardModifier;
import io.qt.core.Qt.KeyboardModifiers;
import io.qt.core.Qt.ScrollBarPolicy;
import io.qt.core.Qt.WidgetAttribute;
import io.qt.core.Qt.WindowFlags;
import io.qt.core.Qt.WindowType;
import io.qt.widgets.QAbstractItemView;
import io.qt.widgets.QApplication;
import io.qt.gui.QCloseEvent;
import io.qt.widgets.QAbstractItemView.SelectionMode;
import io.qt.widgets.QAction;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QFont.SpacingType;
import io.qt.gui.QFontMetrics;
import io.qt.widgets.QFrame;
import io.qt.widgets.QFrame.Shape;
import io.qt.widgets.QHBoxLayout;
import io.qt.gui.QKeyEvent;
import io.qt.widgets.QLabel;
import io.qt.widgets.QLineEdit;
import io.qt.widgets.QMainWindow;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPalette;
import io.qt.gui.QPalette.ColorRole;
import io.qt.widgets.QPlainTextEdit;
import io.qt.gui.QResizeEvent;
import io.qt.widgets.QScrollBar;
import io.qt.widgets.QTreeWidgetItem;
import io.qt.widgets.QWidget;
import java.util.List;

public abstract class keyWords
  extends QMainWindow
{
  Ui_keyWords ui = new Ui_keyWords();
  public black_tree tree;
  public statusbar statusbar;
  public showMoreText showMoreText;
  bTextEdit text;
  boolean onlyUserAction;
  boolean donotShowMoreText = true;
  boolean noDefaultSelection;
  boolean forFindLine;
  public QLabel message;
  protected boolean scrollBarOfTreeHideing;
  boolean hideStatusBar = false;
  boolean translucentBackgound;
  
  public static void main(String[] args) {
		QApplication.initialize(args);
		QFont defaultfont = new QFont("微软雅黑", 9);
		QApplication.setFont(defaultfont);
		QMainWindow m = new QMainWindow();
		m.setGeometry(100, 100, 100, 100);
		m.show();
		QLineEdit line = new QLineEdit(m);
		line.setGeometry(0, 0, 100, 100);
		line.show();
		keyWords k = new keyWords(m,false) {

			@Override
			public void whenSubmit(QTreeWidgetItem paramQTreeWidgetItem) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenNumberTwoPressed(QTreeWidgetItem paramQTreeWidgetItem) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenNumberThreePressed(QTreeWidgetItem paramQTreeWidgetItem) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void selectionChanged(QTreeWidgetItem paramQTreeWidgetItem) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenDelPressed(QTreeWidgetItem paramQTreeWidgetItem) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenWindowSizeChanged(QSize paramQSize) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenHide() {
				// TODO Auto-generated method stub
				
			}};
			k.translucentBackgound = false;

			k.setWindowFlags(Qt.WindowType.Tool,Qt.WindowType.CustomizeWindowHint);
			k.setStyleSheet("background:white;color:green;");
			k.setGeometry(400, 400, 200, 100);
			k.show();
			QApplication.exec();

  }
 
 
  public keyWords(QWidget parent,boolean showStatusbar)
  {
    super(parent);
    this.ui.setupUi(this);
    this.forFindLine = true;
//    Qt.WindowFlags windowFlags = new Qt.WindowFlags(new Qt.WindowType[] { Qt.WindowType.CustomizeWindowHint});
//    setWindowFlags(windowFlags);
//    setWindowFlags(Qt.WindowType.Dialog);
//    setWindowOpacity(0.9D);
    
    this.tree = new black_tree(this, null);
    this.ui.horizontalLayout.addWidget(this.tree);
    if(showStatusbar) {
        this.statusbar = new statusbar();
        this.statusbar.setFont(new QFont("微软雅黑", 8));
        this.statusbar.setSizeGripEnabled(false);
        this.message = new QLabel();
        this.statusbar.addPermanentWidget(this.message);
        this.statusbar.setMaximumHeight(15);
        this.setStatusBar(statusbar);
    }
    
    QPalette p = palette();
    p.setColor(QPalette.ColorRole.Window, new QColor(Qt.GlobalColor.white));
    setPalette(p);
//    setStatusBar(this.statusbar);
    this.ui.horizontalLayout.setMargin(0);
    this.tree.setHeaderHidden(true);
    this.tree.setFrameShape(QFrame.Shape.NoFrame);
//    this.tree.installEventFilter(this);
//    installEventFilter(this);
    this.tree.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
    this.tree.setFont(new QFont(appInfo.uiFont, 9));
    this.tree.setRootIsDecorated(false);
    this.tree.setIconSize(new QSize(16, 16));
    this.tree.verticalScrollBar().setMaximumWidth(17);
    this.tree.setStyleSheet("QTreeWidget::item:selected {background-color: rgb(150,197,1);color: rgb(255,255,255);}");
    tree.itemDoubleClicked.connect(this,"clicked()");
    QObject ef = new QObject() {
    	@Override
    	public boolean eventFilter(QObject o, QEvent e) {
    		// TODO Auto-generated method stub
//    		System.out.println(e.type());
//    		if(e.type() == QEvent.Type.KeyPress) {
//    			System.out.println(o.getClass().getName());
//    		}
    		if(o.equals(tree) && e.type() == QEvent.Type.KeyPress) {
    			QKeyEvent k =  (QKeyEvent) e;
//    			System.out.println(k.key());
    			if(k.key() == 16777220) {
    				clicked();
    				return true;
    			}else if(k.key() == Qt.Key.Key_Escape.value()) {
    				whenHide();
//    				close();
    				return true;
    			}
    		}else if ((o.equals(keyWords.this)) && (e.type() == QEvent.Type.HoverEnter)) {
				tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
			
			} else if ((o.equals(keyWords.this)) && (e.type() == QEvent.Type.HoverLeave)) {
				tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
			}else if(o.equals(keyWords.this) && e.type() == QEvent.Type.Hide) {
//				show();

//				return true;
				whenHide();
			}else if(o.equals(keyWords.this) && e.type() == QEvent.Type.Close) {
				return true;
			}else if(o.equals(keyWords.this) && e.type() == QEvent.Type.HideToParent) {
				return true;
			}
    		return super.eventFilter(o, e);
    	}
    };
    this.setWindowFlags(Qt.WindowType.Window);
    tree.installEventFilter(ef);
    this.installEventFilter(ef);
    this.tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
//    this.tree.currentItemChanged.connect(this, "currentItemChanged(QTreeWidgetItem,QTreeWidgetItem)");
//    this.tree.itemDoubleClicked.connect(this, "doubleClicked(QTreeWidgetItem,int)");
    this.tree.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
  }
  

  void clicked() {
	  QTreeWidgetItem currentItem = tree.currentItem();
	  whenSubmit(currentItem);
  }
  
  public keyWords(QWidget parent, bTextEdit text)
  {
    super(parent);
    this.ui.setupUi(this);
    this.text = text;
    Qt.WindowFlags windowFlags = new Qt.WindowFlags(new Qt.WindowType[] { Qt.WindowType.ToolTip});
    setWindowFlags(windowFlags);
	setAttribute(WidgetAttribute.WA_TranslucentBackground);

    setWindowOpacity(0.9D);
    setMinimumSize(10, 10);
    this.tree = new black_tree(this, null);
    this.ui.horizontalLayout.addWidget(this.tree);
    this.statusbar = new statusbar();
    this.statusbar.hide();
//    this.statusbar.setFont(new QFont("simsun", 8));
    this.statusbar.setSizeGripEnabled(false);
    this.message = new QLabel();
    this.statusbar.addPermanentWidget(this.message);
    this.statusbar.setMaximumHeight(15);
//    QPalette p = palette();
//    p.setColor(QPalette.ColorRole.Window, new QColor(Qt.GlobalColor.white));
//    setPalette(p);
    setStatusBar(this.statusbar);
    this.ui.horizontalLayout.setMargin(0);
    this.tree.setHeaderHidden(true);
    this.tree.setFrameShape(QFrame.Shape.StyledPanel);
    
    this.tree.installEventFilter(this);
    installEventFilter(this);
    this.tree.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);
//    this.tree.setFont(new QFont("simsun", 9));
    this.tree.setRootIsDecorated(false);
    this.tree.setIconSize(new QSize(16, 16));
    this.tree.verticalScrollBar().setMaximumWidth(17);
    tree.setStyleSheet("QTreeWidget::item:selected {background-color: rgb(150,197,1);color: rgb(255,255,255);}");
    
    this.tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
    this.tree.currentItemChanged.connect(this, "currentItemChanged(QTreeWidgetItem,QTreeWidgetItem)");
    this.tree.itemDoubleClicked.connect(this, "doubleClicked(QTreeWidgetItem,int)");
    this.tree.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
  }
  
  public void doubleClicked(QTreeWidgetItem item, int index)
  {
    whenSubmit(item);
  }
  public void paintEvent(QPaintEvent e) {
		super.paintEvent(e);
		QPainter p = new QPainter(this);
		int alpha = 255;
		if(translucentBackgound) alpha = 0;
		QColor color = palette().color(backgroundRole());
		color.setAlpha(alpha);
		p.fillRect(this.rect(), color);
//		QRect br = p.fontMetrics().boundingRect(windowTitle());
//		p.setPen(new QColor(Qt.GlobalColor.white));
//		p.drawText((width()-br.width())/2, 10, windowTitle());
//		p.end();
  }
  public void currentItemChanged(QTreeWidgetItem current, QTreeWidgetItem last) {}
  
	public void showMoreText(QTreeWidgetItem qt) {
		if (this.donotShowMoreText) {
			return;
		}
		QFont font = qt.font(0);
		String str = qt.text(0);
		TextRegion tr = (TextRegion) qt.data(1, 0);

		QFontMetrics qFontMetrics = new QFontMetrics(font);
		int iconWidth = 0;
		if (qt.icon(0) != null) {
			iconWidth = this.tree.iconSize().height();
		}
		int width = qFontMetrics.width(str) + iconWidth;
		int viewwidth = this.tree.viewport().width();
		if (this.tree.verticalScrollBar().isVisible()) {
			viewwidth -= this.tree.verticalScrollBar().width();
		}
		if ((width > viewwidth) || (tr.describe != null)) {
			if (this.showMoreText == null) {
				this.showMoreText = new showMoreText(parentWidget());
			}
			if (tr.describe == null) {
				this.showMoreText.plainTextEdit.setPlainText(tr.text);
			} else {
				this.showMoreText.plainTextEdit.setPlainText(tr.describe);
				this.showMoreText.mark(tr.text);
			}
			if (this.showMoreText.isVisible()) {
				return;
			}
			QRect r = geometry();
			int y = r.height() / 2;
			int Y = 0;
			if (str == null) {
				int newY = r.y() - y - 40;
				if (newY > 0) {
					Y = newY;
				} else {
					Y = r.y() + height() + 10;
				}
			} else if (!this.forFindLine) {
				QRect cursorRect = text.cursorRect();
				QPoint mapToGlobal = text.mapToGlobal(new QPoint(cursorRect.x(),cursorRect.y()));
				if (r.y() >= mapToGlobal.y()) {
					int newY = r.y() - y - this.text.cursorRect().height() - 20;
					if (newY > 0) {
						Y = newY;
					} else {
						Y = r.y() + height() + 10;
					}
				} else {
					Y = mapToGlobal.y()+cursorRect.height()+10;
				}
			} else {
				Y = r.y() + r.height() + 10;
			}
			this.showMoreText.setGeometry(r.x(), Y, r.width(), y);
			this.showMoreText.show();
			this.showMoreText.setWindowOpacity(0.9D);
		} else if (this.showMoreText != null) {
			this.showMoreText.hide();
		}
	}
  
  public void selectTop()
  {
    List<QTreeWidgetItem> selectedItems = this.tree.selectedItems();
    if (selectedItems.size() > 0)
    {
      QTreeWidgetItem qTreeWidgetItem = (QTreeWidgetItem)selectedItems.get(0);
      int indexOfTopLevelItem = this.tree.indexOfTopLevelItem(qTreeWidgetItem);
      if (indexOfTopLevelItem > 0)
      {
        QTreeWidgetItem topLevelItem = this.tree.topLevelItem(indexOfTopLevelItem - 1);
        topLevelItem.setSelected(true);
        this.tree.setCurrentItem(topLevelItem);
        selectionChanged(topLevelItem);
        showMoreText(topLevelItem);
      }
      else
      {
        QTreeWidgetItem topLevelItem = this.tree.topLevelItem(this.tree.topLevelItemCount() - 1);
        topLevelItem.setSelected(true);
        this.tree.setCurrentItem(topLevelItem);
        selectionChanged(topLevelItem);
        showMoreText(topLevelItem);
      }
      qTreeWidgetItem.setSelected(false);
    }
    else if (this.tree.topLevelItemCount() > 0)
    {
      this.tree.topLevelItem(this.tree.topLevelItemCount() - 1).setSelected(true);
    }
  }
  
  public void selectBottom()
  {
    List<QTreeWidgetItem> selectedItems = this.tree.selectedItems();
    if (selectedItems.size() > 0)
    {
      QTreeWidgetItem qTreeWidgetItem = (QTreeWidgetItem)selectedItems.get(0);
      int indexOfTopLevelItem = this.tree.indexOfTopLevelItem(qTreeWidgetItem);
      if (indexOfTopLevelItem + 1 < this.tree.topLevelItemCount())
      {
        QTreeWidgetItem topLevelItem = this.tree.topLevelItem(indexOfTopLevelItem + 1);
        topLevelItem.setSelected(true);
        this.tree.setCurrentItem(topLevelItem);
        selectionChanged(topLevelItem);
        showMoreText(topLevelItem);
      }
      else
      {
        QTreeWidgetItem topLevelItem = this.tree.topLevelItem(0);
        topLevelItem.setSelected(true);
        this.tree.setCurrentItem(topLevelItem);
        selectionChanged(topLevelItem);
        showMoreText(topLevelItem);
      }
      qTreeWidgetItem.setSelected(false);
    }
    else if (this.tree.topLevelItemCount() > 0)
    {
      this.tree.topLevelItem(0).setSelected(true);
    }
  }
  
  public QTreeWidgetItem getSelectionItem()
  {
    List<QTreeWidgetItem> selectedItems = this.tree.selectedItems();
    if (selectedItems.size() > 0) {
      return (QTreeWidgetItem)selectedItems.get(0);
    }
    return null;
  }
  
	public boolean eventFilter(QObject o, QEvent e) {
		if (e.type() == QEvent.Type.KeyPress) {
			QKeyEvent key = (QKeyEvent) e;
			if ((this.tree.UsedToProjectPanelOrKeywordsList == 1) && ((black.findview == 0) || (black.findview == 4) || (black.findview == 5))) {
				if ((!key.modifiers().isSet(new Qt.KeyboardModifier[] { Qt.KeyboardModifier.ControlModifier }))
						&& (key.key() >= 65) && (key.key() <= 90)) {
					if(!key.modifiers().isSet(new Qt.KeyboardModifier[] { Qt.KeyboardModifier.AltModifier }))return false;
					int numberByChar = black.getNumberByChar(key.text());
					if (numberByChar >= this.tree.topLevelItemCount()) {
						return true;
					}
					QTreeWidgetItem item = this.tree.topLevelItem(numberByChar);

					whenSubmit(item);
					return true;
				}
			}
			if (((!this.forFindLine) && (o.equals(this.text)) && (key.key() == Qt.Key.Key_PageUp.value()))
					|| (key.key() == 44)) {
				Qt.KeyboardModifiers no = new Qt.KeyboardModifiers(new Qt.KeyboardModifier[0]);
				no.set(new Qt.KeyboardModifier[] { Qt.KeyboardModifier.NoModifier });
				QKeyEvent key_alt = new QKeyEvent(QEvent.Type.KeyPress, Qt.Key.Key_PageUp.value(), no);
				QCoreApplication.postEvent(this.tree, key_alt);
				return true;
			}
			if (((!this.forFindLine) && (o.equals(this.text)) && (key.key() == Qt.Key.Key_PageDown.value()))
					|| (key.key() == 46)) {
				Qt.KeyboardModifiers no = new Qt.KeyboardModifiers(new Qt.KeyboardModifier[0]);
				no.set(new Qt.KeyboardModifier[] { Qt.KeyboardModifier.NoModifier });
				QKeyEvent key_alt = new QKeyEvent(QEvent.Type.KeyPress, Qt.Key.Key_PageDown.value(), no);
				QCoreApplication.postEvent(this.tree, key_alt);
				return true;
			}
			if (key.key() == Qt.Key.Key_Escape.value()) {
				close();
				return true;
			}
			if ((!o.equals(this)) && (key.key() == Qt.Key.Key_Up.value())) {
				selectTop();
				return true;
			}
			if ((!o.equals(this)) && (key.key() == Qt.Key.Key_Down.value())) {
				selectBottom();
				return true;
			}
			if ((!o.equals(this)) && (key.key() == Qt.Key.Key_Return.value())) {
				List<QTreeWidgetItem> selectedItems = this.tree.selectedItems();
				if (selectedItems.size() > 0) {
					whenSubmit((QTreeWidgetItem) selectedItems.get(0));
				}
				return true;
			}
			if ((o.equals(this)) || (key.key() != 16777249)) {
				if ((!o.equals(this)) && (key.key() == Qt.Key.Key_Space.value())) {
					List<QTreeWidgetItem> selectedItems = this.tree.selectedItems();
					if (selectedItems.size() > 0) {
						close();
						boolean is = false;
						if (black.findview == 0) {
							String text = ((TextRegion) ((QTreeWidgetItem) selectedItems.get(0)).data(1, 0)).text;
							String[] str = cheakDocument.subString(text, appInfo.keywordsSeparator);
							if (str[0].indexOf("·") != -1) {
								is = true;
							} else if (str[0].length() == 3) {
								is = true;
							}
						}
						if (!is) {
							whenSubmit((QTreeWidgetItem) selectedItems.get(0));
						} else {
							whenNumberThreePressed((QTreeWidgetItem) selectedItems.get(0));
						}
					}
					return true;
				}
				if (key.key() == Qt.Key.Key_1.value()) {
					this.donotShowMoreText = true;
					selectTop();
					this.donotShowMoreText = false;
					return true;
				}
				if (key.key() == Qt.Key.Key_Tab.value()) {
					this.donotShowMoreText = true;
					selectBottom();
					this.donotShowMoreText = false;

					return true;
				}
				if (key.key() == 50) {
					QTreeWidgetItem selectionItem = getSelectionItem();
					whenNumberTwoPressed(selectionItem);
					return true;
				}
				if (key.key() == 51) {
					QTreeWidgetItem selectionItem = getSelectionItem();
					whenNumberThreePressed(selectionItem);
					return true;
				}
				if (key.key() == 16777223) {
					QTreeWidgetItem selectionItem = getSelectionItem();
					whenDelPressed(selectionItem);
					return true;
				}
				if (key.key() == 16777217) {
					return true;
				}
			}
		} else if ((o.equals(this.text)) && (e.type() == QEvent.Type.KeyRelease)) {
			QKeyEvent k = (QKeyEvent) e;
			if (!o.equals(this)) {
				k.key();
			}
		} else if ((!o.equals(this)) || (e.type() != QEvent.Type.WindowActivate)) {
			if ((o.equals(this)) && (e.type() == QEvent.Type.WindowDeactivate)) {
				if (!this.forFindLine) {
					close();
				}
			} else {
				if ((o.equals(this)) && (e.type() == QEvent.Type.Hide)) {
					setStyleSheet("");
				    tree.setStyleSheet("QTreeWidget::item:selected {background-color: rgb(150,197,1);color: rgb(255,255,255);}");
					translucentBackgound = false;
					
				    this.hideStatusBar = false;
					if ((this.showMoreText != null) && (this.showMoreText.isVisible())) {
						this.showMoreText.hide();
					}
					whenHide();
					return true;
				}
				if ((o.equals(this)) && (e.type() == QEvent.Type.Show)) {
					if (this.hideStatusBar) {
						this.statusbar.setFixedHeight(0);
					} else {
						this.statusbar.setFixedHeight(15);
					}
					if (this.text != null) {
						QFont font = text.font();
						QFont f = QApplication.font();
						f.setPointSize(font.pointSize());						
						this.tree.setFont(f);
					}
					if (this.tree.topLevelItemCount() > 0) {
						this.donotShowMoreText = true;
						QTreeWidgetItem topLevelItem = this.tree.topLevelItem(0);
						if (!this.noDefaultSelection) {
							this.tree.setCurrentItem(topLevelItem);
							showMoreText(topLevelItem);
						} else if (this.tree.currentItem() != null) {
							showMoreText(this.tree.currentItem());
						}
						if (!this.onlyUserAction) {
							selectionChanged(topLevelItem);
						}
						this.donotShowMoreText = false;
						setWindowOpacity(0.0D);
						show();
						new bRunnable(10, true, false, true, true) {
							public void run() {
								keyWords.this.setWindowOpacity(0.9D);
							}
						};
					}
				} else if ((o.equals(this)) && (e.type() == QEvent.Type.Resize)) {
					QResizeEvent resize = (QResizeEvent) e;
					whenWindowSizeChanged(resize.size());
					if ((this.showMoreText != null) && (this.showMoreText.isVisible())) {
						this.showMoreText.hide();
					}
				} else if ((o.equals(this)) && (e.type() == QEvent.Type.HoverEnter)) {
					setWindowOpacity(1.0D);
					this.tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
					if (!this.hideStatusBar) {
						this.statusbar.show();
					}
				} else if ((o.equals(this)) && (e.type() == QEvent.Type.HoverLeave)) {
					setWindowOpacity(0.9D);
					this.tree.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
					if (!this.hideStatusBar) {
						this.statusbar.hide();
					}
				}
			}
		}
		return false;
	}
  
  public abstract void whenSubmit(QTreeWidgetItem paramQTreeWidgetItem);
  
  public abstract void whenNumberTwoPressed(QTreeWidgetItem paramQTreeWidgetItem);
  
  public abstract void whenNumberThreePressed(QTreeWidgetItem paramQTreeWidgetItem);
  
  public abstract void selectionChanged(QTreeWidgetItem paramQTreeWidgetItem);
  
  public abstract void whenDelPressed(QTreeWidgetItem paramQTreeWidgetItem);
  
  public abstract void whenWindowSizeChanged(QSize paramQSize);
  
  public abstract void whenHide();
}
