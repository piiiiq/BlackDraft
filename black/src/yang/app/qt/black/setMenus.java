package yang.app.qt.black;

import java.util.ArrayList;
import java.util.List;

import io.qt.widgets.QAction;

public class setMenus {
	black b;
	public setMenus(black b) {
		this.b = b;
		setMenus();
	}
	void setMenus() {
//		ArrayList<QAction> al = new ArrayList<>();
//		b.ui.gitMenu.action
		if(b.projectFile == null) {
			setMenus_IsHasProject(false);
		}else {
			setMenus_IsHasProject(true);
			
			if(b.text == null) {
				setMenus_IsHasEditor(false);
			}else {
				setMenus_IsHasEditor(true);
			}
		}
	}
	void setMenus_IsHasEditor(boolean enable) {
		b.ui.editMenu.setEnabled(enable);
		b.ui.viewMenu.setEnabled(enable);
		b.ui.toolsMenu.setEnabled(enable);
		List<QAction> list = b.toolbar.actions();
		for(QAction a:list) {
			if(a.text().indexOf("写作视图") != -1) {
				a.setEnabled(enable);
			}
		}
	}
	void setMenus_IsHasProject(boolean enable) {
		b.ui.editMenu.setEnabled(enable);
		b.ui.gitMenu.setEnabled(enable);
		b.ui.projectMenu.setEnabled(enable);
		b.ui.viewMenu.setEnabled(enable);
		b.ui.toolsMenu.setEnabled(enable);
		List<QAction> actions = b.ui.fileMenu.actions();
		for(QAction a:actions) {
			if(a.text().indexOf("切换") != -1 || a.text().indexOf("导出") != -1) {
				a.setEnabled(enable);
			}
		}
		
	}
}
