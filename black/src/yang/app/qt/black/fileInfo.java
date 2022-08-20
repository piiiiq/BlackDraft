package yang.app.qt.black;

import io.qt.gui.QTextBlock;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextDocument;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class fileInfo implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	public String showName;
	public String fileName;
	public String charCountOfLastInput;

	class walkInfo implements Serializable, Cloneable {
		private static final long serialVersionUID = 1L;
		public int blockNumber;
		public int offestInLine;
		public String textOfLine;

		public walkInfo(int blockNumber, int offestInLine, String textOfLine) {
			this.blockNumber = blockNumber;
			this.offestInLine = offestInLine;
			this.textOfLine = textOfLine;
		}

		public String toString() {
			return "blockNumber: " + this.blockNumber + " offestInLine: " + this.offestInLine + " textOfLine: "
					+ this.textOfLine;
		}

		public walkInfo clone() {
			walkInfo clone = null;
			try {
				clone = (walkInfo) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return clone;
		}
	}

	public int isRoot = -1;
	public int cursorPos;
	public int scrollbarValue;
	public int selectionStart = -1;
	public int charCount;
	public boolean isFile;
	public boolean isDir;
	public boolean expaned;
	public boolean isFiles;
	public boolean isKeyWordsList;
	public boolean isNotes;
	public boolean setTitleStyle = false;
	public boolean showMark = false;
	public ArrayList<fileInfo> subfiles;
	public ArrayList<walkInfo> walkList;
	public int autoscrollvalue = 0;
	public int walkIndex;
	public ArrayList<Integer> titles = new ArrayList<>();
//	public HashSet<Integer> titles = new HashSet<>();
	public fileInfo(String showName, String fileName) {
		this.showName = showName;
		this.fileName = fileName;
	}

	public ArrayList<fileInfo> initFloder() {
		this.subfiles = new ArrayList();
		return this.subfiles;
	}

	public void addWalkInfo(int blockNumber, int offestInLine, String textOfLine) {
		if ((this.isFile) || (this.isFiles)) {
			if (this.walkList == null) {
				this.walkList = new ArrayList();
			}
			if (this.walkList.size() > 10) {
				this.walkList.remove(0);
			}
			this.walkList.add(new walkInfo(blockNumber, offestInLine, textOfLine));
			this.walkIndex = -1;
		}
	}

	public void updateLastWalkInfo(int blockNumber, int offestInLine, String textOfLine) {
		if ((this.walkList != null) && (this.walkList.size() > 0)) {
			walkInfo walkInfo = (walkInfo) this.walkList.get(this.walkList.size() - 1);
			walkInfo.blockNumber = blockNumber;
			walkInfo.offestInLine = offestInLine;
			walkInfo.textOfLine = textOfLine;
			this.walkIndex = -1;
		}
	}

	public walkInfo getLastWalkInfo() {
		if ((this.walkList != null) && (this.walkList.size() > 0)) {
			return (walkInfo) this.walkList.get(this.walkList.size() - 1);
		}
		return null;
	}

	public walkInfo getPreviousWalkInfo() {
		if ((this.walkList != null) && (this.walkList.size() > 0)) {
			if (this.walkIndex == -1) {
				walkInfo lastWalkInfo = getLastWalkInfo();
				QTextBlock findBlockByNumber = black.text.document_b().findBlockByNumber(lastWalkInfo.blockNumber);
				int pos = findBlockByNumber.position() + lastWalkInfo.offestInLine;
				QTextCursor tc = black.text.textCursor();
				if (pos == tc.position()) {
					this.walkIndex = (this.walkList.size() - 2);
				} else {
					this.walkIndex = (this.walkList.size() - 1);
				}
			} else if (this.walkIndex > 0) {
				this.walkIndex -= 1;
			} else {
				return null;
			}
			if (this.walkIndex >= 0) {
				return (walkInfo) this.walkList.get(this.walkIndex);
			}
		}
		return null;
	}

	public walkInfo getNextWalkInfo() {
		if ((this.walkList != null) && (this.walkList.size() > 0)) {
			if (this.walkIndex == -1) {
				return null;
			}
			if (this.walkIndex < this.walkList.size() - 1) {
				this.walkIndex += 1;
			} else {
				return null;
			}
			if (this.walkIndex < this.walkList.size()) {
				return (walkInfo) this.walkList.get(this.walkIndex);
			}
		}
		return null;
	}

	public void removeCurrentWalkInfo() {
		if ((this.walkIndex >= 0) && (this.walkIndex < this.walkList.size())) {
			this.walkList.remove(this.walkIndex);
			if (this.walkIndex == this.walkList.size()) {
				this.walkIndex = -1;
			}
		}
	}

	public fileInfo clone() {
		fileInfo clone = null;
		try {
			clone = (fileInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	public String toString() {
		String str = "------------------\n显示名称：" + this.showName + "\n" + "文件名称：" + this.fileName + "\n" + "是根目录："
				+ this.isRoot + "\n" + "是文件：" + this.isFile + "\n" + "是目录：" + this.isDir + "\n" + "是文件集：" + this.isFiles
				+ "\n" + "是关键词列表：" + this.isKeyWordsList + "\n" + "是笔记：" + this.isNotes + "\n" + "------------------";
		return str;
	}
}
