package yang.app.qt.shuchong;

import java.io.File;

import io.qt.core.Qt.GlobalColor;
import io.qt.gui.QColor;
import io.qt.gui.QFont;
import io.qt.gui.QMouseEvent;
import io.qt.gui.QPaintEvent;
import io.qt.gui.QPainter;
import io.qt.gui.QPen;
import io.qt.gui.QTextBlockFormat;
import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveMode;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.gui.QTextCursor.SelectionType;
import io.qt.widgets.QLabel;
import io.qt.widgets.QTextBrowser;
import io.qt.widgets.QTextEdit;
import io.qt.widgets.QWidget;
import yang.app.qt.black.cheakDocument;
import yang.app.qt.black.io;

public class shuchong extends QTextEdit{
	String con;
	String textByMove = "";
	public shuchong() {
		con = io.readTextFile(new File("d:\\test\\te.txt"), "utf-8");
//		System.out.println(con);
		setPlainText(con);
		QTextCursor tc = textCursor();
		tc.select(SelectionType.Document);
		QTextBlockFormat tbf = new QTextBlockFormat();
		
		tbf.setLineHeight(50, QTextBlockFormat.LineHeightTypes.FixedHeight.value());
		tc.setBlockFormat(tbf);
		this.setFont(new QFont("宋体",13));
		
		setGeometry(0,0, 400, 800);
		show();
		
		tc.movePosition(MoveOperation.Start);
		tc.select(SelectionType.BlockUnderCursor);
		String selectedText = tc.selectedText();
		char charAt = selectedText.charAt(selectedText.length()-1);
		System.out.println(charAt);
	}
//	@Override
	protected void mouseMoveEvent(QMouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseMoveEvent(e);
		String anchorAt = this.anchorAt(e.pos());
		QTextCursor tc = this.cursorForPosition(e.pos());
		tc.movePosition(MoveOperation.WordLeft);
		tc.movePosition(MoveOperation.WordRight, MoveMode.KeepAnchor);
		if(tc.selectedText().length()>2 &&!textByMove.equals(tc.selectedText())) {
//			System.out.println(tc.selectedText());
			String find = find(tc.selectedText(),tc.position()+1);
			if(!find.isEmpty())
			System.out.println(find);
			textByMove = tc.selectedText();
		}
	}
	String find(String text,int fromIndex) {
		boolean doing = true;
		do {
		int indexOf = con.indexOf(text, fromIndex);
		if(indexOf == -1) {
			return "";
		}else {
			fromIndex = indexOf+1;
			
			QTextCursor tc = textCursor();
			tc.setPosition(indexOf);
			tc.select(SelectionType.BlockUnderCursor);
			String stext = tc.selectedText();
			char charAt = stext.charAt(stext.length()-1);
			if(cheakDocument.isAsia(charAt)) {
				tc.movePosition(MoveOperation.StartOfBlock);
				tc.movePosition(MoveOperation.WordRight, MoveMode.KeepAnchor);
				System.out.println(tc.selectedText());
				if(tc.selectedText().equals(text))
					return stext;
			}
			
//			tc.movePosition(MoveOperation.EndOfBlock);
//			tc.movePosition(MoveOperation.PreviousWord);
//			tc.movePosition(MoveOperation.WordRight, MoveMode.KeepAnchor);
		}
		}while(doing);
		return "";
	}
	@Override
	protected void paintEvent(QPaintEvent e) {
		// TODO Auto-generated method stub
		super.paintEvent(e);
		QPainter p = new QPainter(this);
		p.begin(this);
		p.setPen(new QPen(new QColor(GlobalColor.black),3));
		p.drawRect(100, 100, 100, 100);
		p.end();

	}
}
