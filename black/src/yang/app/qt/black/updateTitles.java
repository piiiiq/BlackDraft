package yang.app.qt.black;

import io.qt.core.QPoint;
import io.qt.widgets.QApplication;
import io.qt.gui.QFont;
import io.qt.gui.QTextCursor;
import io.qt.widgets.QTreeWidgetItem;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.widgets.QTextEdit;

public class updateTitles {
//	keyWords titlesPanel = 
	static black black;
private static QTextCursor tc;
private static QFont font;
private static boolean italic;
private static int i;
static QTreeWidgetItem ti = null;
private static int index;
private static int lastipos;
private static int a;
private static int current;
private static boolean moved;


	public static void update(boolean update,keyWords titlesPanel,QTextEdit text,black b) {
		black = b;
		b.uiRun(b,new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(titlesPanel == null)return;
				if(update && titlesPanel.isHidden())return;

				tc = text.textCursor();
				if(!update) {
					titlesPanel.tree.clear();
				}
				
				QTextCursor cursorForPosition = text.cursorForPosition(new QPoint(0,0));
				
				current = cursorForPosition.position();
				a = 0;
				lastipos = -1;
				index = 0;
				moved = false;
				tc.setPosition(0);
			}
		});
		
		do {
			b.uiRun(b,new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					font = tc.charFormat().font();
					italic = font.italic();
					i = tc.position();
				}
			});
			if(italic) {
				tc.movePosition(MoveOperation.StartOfBlock);
				b.uiRun(b,new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!update) {
							tc.setPosition(i);
							String title = tc.block().text();
							ti = black.getTreeItem(titlesPanel.tree);
							ti.setText(0, title);
							ti.setData(4, 4, i);
							titlesPanel.tree.addTopLevelItem(ti);
						}else {
							ti = titlesPanel.tree.topLevelItem(index);
						}
					}
				});
				
				
				if(a != -1) {
					if(lastipos != -1 && current >= lastipos && current < i) {
						b.uiRun(b,new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								QTreeWidgetItem topLevelItem = titlesPanel.tree.topLevelItem(index-1);
								titlesPanel.tree.setCurrentItem(topLevelItem);
							}
						});
						
						if(!update)
						a = -1;
						else break;
					}else if(current >= i && index == black.infoOfCurrentEditing.titles.size()-1) {
						b.uiRun(b,new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								titlesPanel.tree.setCurrentItem(ti);
							}
						});
						if(!update)
							a = -1;
						else break;
					}else {
						b.uiRun(b,new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								titlesPanel.tree.setCurrentItem(null);
							}
						});
					}
				}
				index++;
				lastipos = i;
			}
			QApplication.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					moved = tc.movePosition(MoveOperation.NextBlock);
				}
			});
		}while(moved);
	}
}
