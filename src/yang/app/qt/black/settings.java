package yang.app.qt.black;

import io.qt.core.QEvent;
import io.qt.core.QObject;
import io.qt.core.QRect;
import io.qt.widgets.QApplication;
import io.qt.widgets.QDesktopWidget;
import io.qt.widgets.QDialog;
import io.qt.gui.QFont;
import io.qt.widgets.QWidget;

public class settings extends QDialog {
	Ui_settings ui = new Ui_settings();
	black b;
	private QRect screenRect;

	public static void main(String[] args) {
		QApplication.initialize(args);

		settings testsettings = new settings();
		testsettings.show();

		QApplication.exec();
	}

	public settings() {
		this.ui.setupUi(this);
		setFont(new QFont("微软雅黑", 9));
		for (int i = 100; i <= 400; i += 10) {
			this.ui.zoom.addItem(String.valueOf(i));
		}
		int index = this.ui.fontComboBox.findText("宋体");
		this.ui.fontComboBox.setCurrentIndex(index);

		this.ui.fontComboBox.currentFontChanged.connect(this, "fontchanged(QFont)");
//		this.ui.zoom.currentIndexChanged.connect(this, "zoomchanged(int)");
		QApplication.connect(ui.zoom, "currentIndexChanged(int)", this,"zoomchanged(int)");
//		this.ui.linespace.valueChanged.connect(this, "linespace(int)");
		QApplication.connect(ui.linespace, "currentIndexChanged(int)", this,"linespace(int)");

//		this.ui.paragraphspace.valueChanged.connect(this, "paragraphspace(int)");
		QApplication.connect(ui.paragraphspace, "currentIndexChanged(int)", this,"paragraphspace(int)");

//		this.ui.firstline.valueChanged.connect(this, "firstline(int)");
		QApplication.connect(ui.firstline, "currentIndexChanged(int)", this,"firstline(int)");

//		this.ui.textCursorWidth.valueChanged.connect(this, "caretwidth(int)");
		QApplication.connect(ui.textCursorWidth, "currentIndexChanged(int)", this,"caretwidth(int)");


//		this.ui.taps.setCurrentIndex(3);
		ui.taps.removeTab(1);
		ui.taps.removeTab(1);
	}

	public settings(QWidget parent) {
		super(parent);
		this.b = ((black) parent);
		this.ui.setupUi(this);
		setWindowTitle("首选项");
		

		
		setFixedSize(width(), height());
		for (int i = 100; i <= 400; i += 10) {
			this.ui.zoom.addItem(String.valueOf(i) + "%");
		}
		int fontindex = this.ui.fontComboBox.findText(this.b.getEditorFontName());
		this.ui.fontComboBox.setCurrentIndex(fontindex);
		int zoomindex = this.ui.zoom.findText(String.valueOf(this.b.btext.zoomvalue * 10 + 100) + "%");
		this.ui.zoom.setCurrentIndex(zoomindex);
		this.ui.linespace.setValue(this.b.getEditorLineSpaceValue());
		this.ui.paragraphspace.setValue(this.b.getEditorParagraphSpaceValue());
		this.ui.firstline.setValue(this.b.getEditorFirstLineValue());
		this.ui.textCursorWidth.setValue(this.b.getEditorCaretWidthValue());
		this.ui.textCursorWidth.setMinimum(0);
		this.ui.fontsize.setValue(this.b.getEditorFontSize());
		QDesktopWidget desk = new QDesktopWidget();
		this.screenRect = desk.rect();
		this.ui.textWidth.setMinimum(100);
		this.ui.textWidth.setMaximum(this.screenRect.width());
		this.ui.textWidth.setValue(this.screenRect.width() - this.b.getWritingViewTextWitdh());

		this.ui.documentmargin.setMaximum(200);
		this.ui.documentmargin.setValue(this.b.getDocumentMargin());
		int autoSave = this.b.getAutoSave();
		if (autoSave > 0) {
			this.ui.savelable.setEnabled(true);
			this.ui.savetime.setEnabled(true);
			this.ui.savecheckBox.setChecked(true);
			this.ui.savetime.setValue(autoSave);
		} else {
			this.ui.savelable.setEnabled(false);
			this.ui.savetime.setEnabled(false);
			this.ui.savecheckBox.setChecked(false);
		}
		int topKeywordsCount = this.b.getTopKeywordsCount();
		if (topKeywordsCount >= 0) {
			this.ui.topKeywordsCount.setEnabled(true);
			this.ui.topKeywords.setChecked(true);
			this.ui.topKeywordsCount.setValue(topKeywordsCount);
		} else {
			this.ui.topKeywordsCount.setEnabled(false);
			this.ui.topKeywords.setChecked(false);
		}
		String[] gitInfo = this.b.getGitInfo();
		if (gitInfo != null) {
			this.ui.githost.setText(gitInfo[0]);
			this.ui.gitusername.setText(gitInfo[1]);
			this.ui.gitpassword.setText(gitInfo[2]);
			this.ui.savegitinfo.setChecked(true);
		}
		ui.shadowSize.setEnabled(false);
		ui.alpha.setEnabled(false);
		
		ui.shadowSize.setValue(b.getEditorHeight());
		ui.shadowSize.setMinimum(5);
		ui.shadowSize.setMaximum(this.screenRect.height() / 3);
		ui.shadowSize.valueChanged.connect(this, "textHeight(int)");
		this.ui.alpha.setEnabled(false);
		this.ui.fullscreen.setEnabled(false);
		this.ui.window.setEnabled(false);

		this.ui.textWidth.sliderPressed.connect(this, "textWidthPressed()");
		this.ui.textWidth.sliderReleased.connect(this, "textWidthReleased()");
		this.ui.fontComboBox.currentFontChanged.connect(this, "fontchanged(QFont)");
//		this.ui.zoom.currentIndexChanged.connect(this, "zoomchanged(int)");
		QApplication.connect(ui.zoom, "currentIndexChanged(int)", this,"zoomchanged(int)");

//		this.ui.linespace.valueChanged.connect(this, "linespace(int)");
		QApplication.connect(ui.linespace, "valueChanged(int)", this,"linespace(int)");

//		this.ui.paragraphspace.valueChanged.connect(this, "paragraphspace(int)");
		QApplication.connect(ui.paragraphspace, "valueChanged(int)", this,"paragraphspace(int)");

//		this.ui.firstline.valueChanged.connect(this, "firstline(int)");
		QApplication.connect(ui.firstline, "valueChanged(int)", this,"firstline(int)");

//		this.ui.textCursorWidth.valueChanged.connect(this, "caretwidth(int)");
		QApplication.connect(ui.textCursorWidth, "valueChanged(int)", this,"caretwidth(int)");

//		this.ui.fontsize.valueChanged.connect(this, "fontsize(int)");
		QApplication.connect(ui.fontsize, "valueChanged(int)", this,"fontsize(int)");

		this.ui.textWidth.valueChanged.connect(this, "textWidth(int)");
//		this.ui.documentmargin.valueChanged.connect(this, "margin(int)");
		QApplication.connect(ui.documentmargin, "valueChanged(int)", this,"margin(int)");

		this.ui.savecheckBox.clicked.connect(this, "checkAutoSaveBox(boolean)");
//		this.ui.savetime.valueChanged.connect(this, "setSaveTime(int)");
		QApplication.connect(ui.savetime, "valueChanged(int)", this,"setSaveTime(int)");
		this.ui.gitstartwork.pressed.connect(this, "gitwork()");
		this.ui.topKeywords.clicked.connect(this, "checkTopKeywordsCountBox(boolean)");
//		this.ui.topKeywordsCount.valueChanged.connect(this, "topKeywordsCount(int)");
		QApplication.connect(ui.topKeywordsCount, "valueChanged(int)", this,"topKeywordsCount(int)");

		int onlyAPart = this.b.onlyPart();
		if (onlyAPart == 0) {
			this.ui.onlyAPart.setChecked(false);
		} else {
			this.ui.onlyAPart.setChecked(true);
		}
		this.ui.onlyAPart.clicked.connect(this, "onlyAPart(boolean)");
		if(!b.isAdmin()) {
//			ui.taps.removeTab(1);
//			ui.taps.removeTab(1);
		}
		else if (black.writingView == 0) {
			this.ui.taps.setCurrentIndex(1);
		} else {
			this.ui.taps.setCurrentIndex(2);
		}
		this.installEventFilter(this);
	}

	public boolean eventFilter(QObject o, QEvent e) {
		if (e.type() == QEvent.Type.Show) {
			b.noEditorEffect = true;
		} else if (e.type() == QEvent.Type.Hide) {
			b.noEditorEffect = false;
		}
		return super.eventFilter(o, e);
	}

	void textHeight(int v) {
		b.setEditorHeight(v);
	}

	void textWidthReleased() {

	}

	void textWidthPressed() {
	}

	void onlyAPart(boolean ischeck) {
		this.b.setOnlyAPart(ischeck);
	}

	void gitwork() {
		final String host = this.ui.githost.text();
		final String username = this.ui.gitusername.text();
		final String password = this.ui.gitpassword.text();
		if ((!host.equals("")) && (!username.equals("")) && (!password.equals(""))) {
			new showProgress(this, this.b, "连接远程仓库", false) {
				public void whenOkButtonPressed() {
					if ((settings.this.ui.savegitinfo.isChecked())
							&& (this.ui.progress.value() == this.ui.progress.maximum())) {
						this.b.saveGitInfo(host, username, password);
					}
					hide();
				}

				public void whenAutoClose() {
				}

				@Override
				public void whenDialogClosed() {
					// TODO Auto-generated method stub
					
				}
			};
			new Thread(new Runnable() {
				public void run() {
					settings.this.b.setProgressInfo("连接远程仓库...", 20);
					settings.this.b.testConn(host, username, password);
					settings.this.b.setProgressInfo("上传成功！", 100);
				}
			})

					.start();
		}
	}

	void checkTopKeywordsCountBox(boolean ischeck) {
		if (ischeck) {
			topKeywordsCount(20);
			this.ui.topKeywordsCount.setEnabled(true);
			this.ui.topKeywordsCount.setValue(20);
		} else {
			topKeywordsCount(-1);
			this.ui.topKeywordsCount.setEnabled(false);
		}
	}

	void topKeywordsCount(int value) {
		this.b.setTopKeywordsCount(value);
	}

	void setSaveTime(int value) {
		this.b.setAutoSave(value);
	}

	void checkAutoSaveBox(boolean ischeck) {
		if (ischeck) {
			this.ui.savelable.setEnabled(true);
			this.ui.savetime.setEnabled(true);
			this.ui.savecheckBox.setChecked(true);
			this.ui.savetime.setValue(300);
			this.b.setAutoSave(300);
		} else {
			this.ui.savelable.setEnabled(false);
			this.ui.savetime.setEnabled(false);
			this.ui.savecheckBox.setChecked(false);
			this.b.setAutoSave(-1);
		}
	}

	void margin(int value) {
		this.b.setDocumentMargin(value);
	}

	void textWidth(int value) {
		this.b.setWritingViewTextWidth(this.screenRect.width() - value);
	}

	void fontchanged(QFont font) {
		this.b.setEditorFont(font);
	}

	void zoomchanged(int index) {
		this.b.setEditorZoomValue(index);
	}

	void linespace(int value) {
		this.b.setEditorLineSpace(value);
	}

	void paragraphspace(int value) {
		this.b.setEditorParagraphSpace(value);
	}

	void firstline(int value) {
		this.b.setEditorFirstLine(value);
	}

	void caretwidth(int value) {
		this.b.setCaretWidth(value);
	}

	void fontsize(int value) {
		this.b.setEditorFontSize(value);
	}
}
