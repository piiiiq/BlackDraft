package yang.app.qt.black;

import io.qt.widgets.QApplication;
import java.io.File;

class loadFile {
	String text;
	String showName;
	int part;
	int start;
	black b;
	int plusValue;
	File file;
	boolean isSaved;
	boolean opened;
	long a;

	public loadFile(black b, String str, String showname, int part, File file, boolean isSaved, long a,
			boolean opened) {
		this.b = b;
		this.text = str;
		this.showName = showname;
		this.part = part;
		this.file = file;
		this.isSaved = isSaved;
		this.a = a;
		this.opened = opened;
		if (part == 0) {
			this.plusValue = 50000;
		} else {
			this.plusValue = part;
		}
		run();
	}

	public void run() {
		new Thread(new Runnable() {
			public void run() {
//				for (int i = loadFile.this.part; i >= 0; i += loadFile.this.plusValue) {
//					//此次载入之后还有文本没有被载入
//					if (i < loadFile.this.text.length()) {
//						final int i_ = i;
//						b.uiRun(b, new Runnable() {
//							public void run() {
//								loadFile.this.b.setProgressBarValue(i_, loadFile.this.text.length(),
//										loadFile.this.showName + "\n");
//								loadFile.this.b.btext.text.append(loadFile.this.text
//										.substring(loadFile.this.start, i_));
//								loadFile.this.start = i_;
//							}
//						});
//					} else {
//						//载入最后一段文本
//						if (loadFile.this.start < loadFile.this.text.length()) {
//							b.uiRun(b, new Runnable() {
//								public void run() {
////									loadFile.this.b.btext.text.insertPlainText(loadFile.this.text
////											.substring(loadFile.this.start, loadFile.this.text.length()));
//									loadFile.this.b.btext.text.append(loadFile.this.text
//											.substring(loadFile.this.start, loadFile.this.text.length()));
//									loadFile.this.b.setProgressBarValue(loadFile.this.text.length(),
//											loadFile.this.text.length(), loadFile.this.showName + "\n");
//									loadFile.this.b.whenLoadFileDone(loadFile.this.file, loadFile.this.isSaved,
//											loadFile.this.a, loadFile.this.showName, loadFile.this.opened);
//								}
//							});
//							break;
//						}
//						//已经没有文本可载入，已经载入了全部的文本
//						b.uiRun(b, new Runnable() {
//							public void run() {
//								loadFile.this.b.whenLoadFileDone(loadFile.this.file, loadFile.this.isSaved,
//										loadFile.this.a, loadFile.this.showName, loadFile.this.opened);
//							}
//						});
//						break;
//					}
//					try {
//						Thread.sleep(1L);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					System.out.println("载入文本增加值"+i);
//				}
				

				b.uiRun(b, new Runnable() {
					public void run() {
						loadFile.this.b.btext.text.setPlainText(text);
						loadFile.this.b.whenLoadFileDone(loadFile.this.file, loadFile.this.isSaved,
								loadFile.this.a, loadFile.this.showName, loadFile.this.opened);
						b.btext.vScrollBarMaxValue = b.text.verticalScrollBar().maximum();
					}
				});
			}
		}).start();
	}
}
