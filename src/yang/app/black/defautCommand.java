package yang.app.black;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import javax.lang.model.element.PackageElement;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import yang.demo.allPurpose.debug;
import yang.demo.allPurpose.time;

public class defautCommand implements Serializable {
	static final long serialVersionUID = 42L;

	static String NOONCURRENT = "该命令在当前环境不可用";

	public static void throwNo(black black, StyledText st) {
		black.ba.insertText(NOONCURRENT, st);
	}

	public static void defaultCommand(final black black, final StyledText st, blackTextArea bta) {
		bta.addCommand(new command("时间") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.insertText(time.getCurrentTime(), st);
			}
		});
		bta.addCommand(new command("日期") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.insertText(time.getCurrentDate("-"), st);
			}
		});
		bta.addCommand(new command("横向光标") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.resetCaret(SWT.HORIZONTAL);
			}
		});
		bta.addCommand(new command("纵向光标") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.resetCaret(SWT.VERTICAL);
			}
		});
		bta.addCommand(new command("搜索", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (command_tr.text != null)
					black.ba.findinAllText(command_tr.text);
			}
		});
		bta.addCommand(new command("行距", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (command_tr.text.matches("[0-9]{2}"))
					black.setEditerLineSpace(Integer.valueOf(command_tr.text));
			}
		});
		bta.addCommand(new command("这个怎么用？") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.getMessageBox("快捷命令帮助",
						"可在编辑器内插入快键命令\n（程序的某些设置只能通过快键命令更改）\n快键命令的格式有两类:\n1. 无值类，只需输入“@”+“命令”，如使用护眼色只需输入“@护眼色”"
								+ "\n2. 有值类，需输入“@”+“命令”+“值”+“空格”，如要改变编辑器的行距为20，需输入“@行距20 ”");
			}
		});
		bta.addCommand(new command("显示或隐藏项目面板") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (black.wv == null || black.wv.isDisposed()) {
					black.tree.setVisible(!black.tree.getVisible());
					black.resetLayoutData();

				} else
					throwNo(black, st);
			}
		});
		bta.addCommand(new command("显示或隐藏底边栏", "用于临时显示或隐藏底边栏，此设置将在程序退出时失效") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (black.wv == null || black.wv.isDisposed()) {
					black.composite.setVisible(!black.composite.getVisible());
					black.resetLayoutData();
				} else
					throwNo(black, st);
			}
		});

		bta.addCommand(new command("写作视图") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.openWritingView();
			}
		});

		bta.addCommand(new command("退出程序") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.dispose();
			}
		});

		 bta.addCommand(new command("以PDF格式输出当前所编辑的文件") {
		
		 @Override
		 public void action(TextRegion command_tr) {
		 // TODO Auto-generated method stub
		 black.ba.saveCurrentFileAsPDF();
		 }
		 });
		bta.addCommand(new command("以DOCX格式输出当前所编辑的文件") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				FileDialog fd;
				if (black.wv == null || black.wv.isDisposed())
					fd = black.ba.getFileDialog("保存为docx文件","", black, SWT.SAVE, new String[] { "*.docx" });
				else
					fd = black.ba.getFileDialog("保存为docx文件","", black.wv, SWT.SAVE, new String[] { "*.docx" });
				if (fd.getFileNames().length == 1) {
					File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
					ioThread io = new ioThread(black);
					io.writeDocxFile(f, black.text, false);
					black.ba.getMessageBox("", "保存成功");
				}
			}
		});
		bta.addCommand(new command("以TXT格式输出当前所编辑的文件", "可以追加字符编码，如不追加将使用平台的默认编码", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub

				FileDialog fd;
				if (black.wv == null || black.wv.isDisposed())
					fd = black.ba.getFileDialog("保存为TXT文件","", black, SWT.SAVE, new String[] { "*.txt" });
				else
					fd = black.ba.getFileDialog("保存为TXT文件", "",black.wv, SWT.SAVE, new String[] { "*.txt" });
				if (fd.getFileNames().length == 1) {
					File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
					if (black.ba.saveCurrentFileAsTXT(f, command_tr.text))
						black.ba.getMessageBox("", "保存成功");
					else
						black.ba.getMessageBox("", "保存失败");
				}

			}
		});
		bta.addCommand(new command("以TXT格式输出全部文件", "可以追加字符编码", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				FileDialog fd = null;
				if (black.wv == null || black.wv.isDisposed())
					fd = black.ba.getFileDialog("保存为TXT文件", "",black, SWT.SAVE, new String[] { "*.txt" });
				else
					fd = black.ba.getFileDialog("保存为TXT文件", "",black.wv, SWT.SAVE, new String[] { "*.txt" });
				if (fd.getFileNames().length == 1) {
					File f = new File(fd.getFilterPath() + System.getProperty("file.separator") + fd.getFileName());
					black.saveCurrentFile(false, false);
					findinfo_[] text = black.ba.getAllTextFromProject(true, false);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < text.length; i++) {
						if (text[i].stringbuilder != null)
							sb.append(text[i].stringbuilder.toString());
					}
					ioThread io = new ioThread(black);
					if (!command_tr.text.equals("")) {
						if (Charset.isSupported(command_tr.text)) {
							if (io.writeTextFile(f, sb.toString(), command_tr.text))
								black.ba.getMessageBox("", "保存成功");
							else
								black.ba.getMessageBox("", "保存失败");
						} else
							black.ba.getMessageBox("", "不被支持的字符编码");
					} else {
						if (io.writeTextFile(f, sb.toString(), Charset.defaultCharset().displayName()))
							black.ba.getMessageBox("", "保存成功");
						else
							black.ba.getMessageBox("", "保存失败");
					}
				}

			}
		});
		bta.addCommand(new command("垃圾箱文件数") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.getMessageBox("", "垃圾箱内有" + black.ba.getRecycleFileCount() + "个文件");
			}
		});
		bta.addCommand(new command("重新启动程序") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.restartBlack();
			}
		});
		bta.addCommand(new command("导出文件") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				new exportFiles(black, SWT.None).open();
			}
		});
		bta.addCommand(new command("导入文件") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				new importFiles(black, SWT.None).open();
			}
		});
		bta.addCommand(new command("项目属性") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				new projectInfo(black, SWT.None).open();
			}
		});

		bta.addCommand(new command("快速检索时同时检索垃圾箱内的文件", "重复调用可关闭或开启") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.includeRecycle = !black.ba.includeRecycle;
			}
		});
		bta.addCommand(new command("快速检索时是否同时检索垃圾箱内的文件？") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.insertText(String.valueOf(black.ba.includeRecycle), st);
			}
		});
		bta.addCommand(new command("设置透明度", "设置主窗口的透明度，值介于0和255之间", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (command_tr.text.matches("[0-9]{3}")) {
					int alpha = Integer.valueOf(command_tr.text);
					if (alpha <= 255) {
						if (black.wv == null || black.wv.isDisposed())
							black.setAlpha(alpha);
						else
							black.wv.setAlpha(alpha);
					}
				}
			}
		});

		bta.addCommand(new command("删除当前文档内每个文本行开头以及末尾标点后面的空格") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < black.text.getLineCount(); i++) {
					String line = black.text.getLine(i);
					if (line.length() > 1) {
						// 删除行开头的空格
						if (line.charAt(0) == ' ') {
							line = line.substring(1, line.length());
						}
						// 删除行末尾的空格
						char lastSecChar = line.charAt(line.length() - 2);
						if (cheakDocument.cheak(lastSecChar)) {
							char lastChar = line.charAt(line.length() - 1);
							if (lastChar == ' ') {
								line = line.substring(0, line.length() - 1);
							}
						}
					}
					sb.append(line + "\n");

				}
				black.text.setText(sb.toString());
			}

		});

		bta.addCommand(new command("列出当前文档内的所有标题（末尾缺乏标点的文本行即视为标题）") {
			public void action(TextRegion command_tr) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < black.text.getLineCount(); i++) {
					String line = black.text.getLine(i);
					if (cheakDocument.cheakString(line))
						sb.append(line + "\n");
				}
				black.ba.getBMessageBox("标题列表", sb.toString());
			}
		});

		bta.addCommand(new command("在系统资源管理器中显示当前所编辑的文件") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.showFileInExplorer();
			}

		});
		bta.addCommand(new command("设置备份目录") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.setBackupDir();
			}
		});
		bta.addCommand(new command("打开备份目录") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				String dir = black.ba.getBackupDir();
				if (dir != null)
					black.ba.showinExplorer(dir, false);
				else
					black.ba.getMessageBox("备份信息", "尚未设置备份目录！");
			}
		});
		bta.addCommand(new command("开始备份") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.startBackup();
			}
		});
		bta.addCommand(new command("编辑预定义文件") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.editMarkFile();
			}
		});
		// bta.addCommand(new command("聚焦文本行") {
		//
		// @Override
		// public void action(TextRegion command_tr) {
		// // TODO Auto-generated method stub
		// black.blackTextArea.test();
		// }
		// });
		bta.addCommand(new command("校验项目内的文件") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.getFileInfo();
			}
		});
		bta.addCommand(new command("将预定义文件内容改动写入磁盘") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				black.ba.writeToMarkFile();
			}
		});
		bta.addCommand(new command("重新读取预定义文件并建立索引") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.readMarkFile();
				black.ba.getMarkFileText();
			}
		});
		bta.addCommand(new command("产生一个意大利人名并插入正文","命令后面输入人名性别（“男”或“女”）", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.getItalinaName(command_tr.text.charAt(0));
			}
		});
		bta.addCommand(new command("产生一个英语人名并插入正文","命令后面输入人名性别（“男”或“女”）", true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.getEnglishName(command_tr.text.charAt(0));
			}
		});
		bta.addCommand(new command("产生给定数量的中文人名", "命令后面输入需要的人名数量",true) {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				boolean haserror = false;
				int count = 0;
				try {
					count = Integer.valueOf(command_tr.text);
				} catch (NumberFormatException e) {
					haserror = true;
				}
				if (!haserror) {
					bta.black.ba.getChineseNames(count);
				}

			}
		});
		bta.addCommand(new command("产生20个中文人名") {

			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (bta.black.namecreator == null) {
					bta.black.namecreator = new nameCreator(bta.black, new File("./nameCreator"));
				}
				if(bta.black.namecreator.isHasChineseNameData()){
					String[] names = bta.black.namecreator.getChineseNames(20);
					StringBuilder sb = new StringBuilder();
					for (String s : names) {
						sb.append(s + "\n");
					}
					bta.black.ba.getBMessageBox("产生了20个中文人名", sb.toString());
				}else bta.black.ba.getMessageBox("人名产生错误", "不存在中文人名数据！");
			}
		});
		bta.addCommand(new command("重新读取人名数据") {
			
			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				if (bta.black.namecreator == null) {
					bta.black.namecreator = new nameCreator(bta.black, new File("./nameCreator"));
				}
				bta.black.namecreator.getallfiles();
			}
		});
		bta.addCommand(new command("开关调试模式") {
			
			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.debugMode(!bta.black.ba.isDebugMode(),false);
			}
		});
		bta.addCommand(new command("是否为调试模式") {
			
			@Override
			public void action(TextRegion command_tr) {
				// TODO Auto-generated method stub
				bta.black.ba.getMessageBox("调试信息", bta.black.ba.isDebugMode()+"");
			}
		});
//		bta.addCommand(new command("test") {
//			
//			@Override
//			public void action(TextRegion command_tr) {
//				// TODO Auto-generated method stub
//				bta.black.ba.saveAllAsText();
//			}
//		});
	}
}
