package yang.app.qt.black;

import io.qt.core.QDir;
import io.qt.core.QDir.Filter;
import io.qt.core.QDir.Filters;
import io.qt.core.QObject;
import io.qt.widgets.QApplication;
import io.qt.widgets.QDirModel;
import io.qt.gui.QFont;
import io.qt.widgets.QHeaderView;
import io.qt.gui.QIcon;
import io.qt.widgets.QTreeView;
import io.qt.widgets.QWidget;
import java.util.List;

public class TreeModel extends QTreeView {
	private static class Model extends QTreeModel {
		private QDir lastDirectory;
		private List<String> lastEntryList;

		public Model(QObject parent) {
			super();
		}

		public int childCount(Object parent) {
			return entryList(dir(parent)).size();
		}

		public Object child(Object parent, int row) {
			QDir d = dir(parent);
			return new QDir(d.absoluteFilePath((String) entryList(d).get(row)));
		}

		public String text(Object value) {
			return ((QDir) value).dirName();
		}

		private List<String> entryList(QDir dir) {
			if (dir == this.lastDirectory) {
				return this.lastEntryList;
			}
			this.lastEntryList = dir.entryList(this.entryListFlags);
			this.lastDirectory = dir;
			return this.lastEntryList;
		}

		private QDir dir(Object value) {
			return value != null ? (QDir) value : QDir.root();
		}

		private QDir.Filters entryListFlags = new QDir.Filters(
				new QDir.Filter[] { QDir.Filter.NoDotAndDotDot, QDir.Filter.Dirs, QDir.Filter.Files });
	}

	public TreeModel()
  {
    this(null);
  }

	public TreeModel(QWidget parent)
  {
    header().hide();
    
    QDirModel model = new QDirModel();
    setModel(model);
    setColumnWidth(0, 1000);
    setFont(new QFont("微软雅黑"));
    setRootIndex(model.index("d:\\"));
    show();
    
    setWindowTitle("A Qt Jambi Tree Model");
    setWindowIcon(new QIcon("classpath:com/trolltech/images/qt-logo.png"));
  }

	public static void main(String[] args) {
		QApplication.initialize(args);

		TreeModel view = new TreeModel();
		view.show();

		QApplication.exec();
	}
}
