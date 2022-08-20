package yang.app.qt.black;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import io.qt.gui.QTextCursor;
import io.qt.gui.QTextCursor.MoveOperation;
import io.qt.widgets.QMessageBox;
import yang.demo.allPurpose.DesUtils;
import yang.demo.allPurpose.MD5;
import yang.demo.allPurpose.time;

public class addCommands {
	static bRunnable br00;
	static bRunnable autoScroll;

	public static void addCommands(final black b) {
		b.addCommand(new blackCommand("预测优先", "设置优先显示关键词列表中的预测条目", true) {
			public void action(String[] args) {
				b.LineAndDocFirst_keywords = (!b.LineAndDocFirst_keywords);
				if (b.LineAndDocFirst_keywords) {
					b.getMessageBox("关键词列表", "已开启优先显示预测条目");
				} else {
					b.getMessageBox("关键词列表", "已关闭优先显示预测条目");
				}
			}
		});
		b.addCommand(new blackCommand("快捷键", "显示快捷键概览", true) {
			public void action(String[] args) {
				b.getBMessageBox("快捷键(不完全)", appInfo.textToHtml(appInfo.keyInfo));
			}
		});
		b.addCommand(new blackCommand("计时器", "开始一个计时器，到时会弹出一个置顶的消息框及提示音提醒\n参数详情：\n参数1：时长(秒)\n参数2：计时器名称 ") {
			public void action(String[] args) {
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				if (((Integer) right.value).intValue() <= 0) {
					b.getMessageBox("参数错误", "时间不能为负值");
					return;
				}
				int value = ((Integer) right.value).intValue();
				String message = "计时器 ";
				String timerName = "";
				if (args.length > 1) {
					timerName = args[1];
				} else {
					timerName = time.msToTime(value * 1000);
				}
				message = message + "(" + timerName + "|" + right.value + "秒)";

				QMessageBox messageBox = b.getMessageBox("设定计时器", message + " 已设定！", false, b.ico_timer);
				String message_ = message;
				addCommands.addTimerAction(b, value, timerName, messageBox, message_);
			}
		});
		b.addCommand(new blackCommand("动画时间", "设定程序动画帧率时间\n参数1：时间间隔(单位毫秒)，必须是大于0的整数") {
			public void action(String[] args) {
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				if (((Integer) right.value).intValue() < 0) {
					b.getMessageBox("参数错误", "时间间隔不能小于0");
					return;
				}
				int value = ((Integer) right.value).intValue();
				b.setAnimationTime(value);
				b.getMessageBox("更改动画帧率间隔", "已将动画帧率间隔时间改为" + value + "毫秒");
			}
		});
		b.addCommand(new blackCommand("固定编辑行", "设定固定当前编辑的行的位置，例如将当前行固定到屏幕的中央，将值设定为2即可，值为1时则会关闭固定行。值越大，编辑的行越接近顶部") {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				if (((Integer) right.value).intValue() <= 0) {
					b.getMessageBox("参数错误", "必须大于0");
					return;
				}
				int value = ((Integer) right.value).intValue();
				b.btext.typeModeValue = value;
				b.settings.setValue(appInfo.typeModeValue, value + "");
			}
		});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("语音速率", "设定朗读速度，必须大于0，一般到十就非常快了") {

				@Override
				public void action(String[] args) {
					// TODO Auto-generated method stub
					isRight right = b.isRightIntValue(args[0]);
					if (!right.isright) {
						return;
					}
					if (((Integer) right.value).intValue() < 0) {
						b.getMessageBox("参数错误", "不能小于0");
						return;
					}
					int value = ((Integer) right.value).intValue();
					b.settings.setValue(appInfo.voiceRate, value + "");
					if (b.speech != null)
						b.speech.setRate(value);
					;
				}
			});
// 	b.addCommand(new blackCommand("可用语音","显示系统可用的语音库",true) {
//
//		@Override
//		public void action(String[] args) {
//			// TODO Auto-generated method stub
//			MSTTSSpeech speech = new MSTTSSpeech();
//			String[] voices = speech.getVoices();
//			StringBuilder sb = new StringBuilder();
//			for(int i=0;i<voices.length;i++) {
//				sb.append(i+" "+voices[i]+"\n");
//			}
//			sb.append("\n<b>当前语音："+voices[b.getIntValueFromSettings(appInfo.voice, "0")]+"</b>");
//			b.getBMessageBox("可用语音库", sb.toString());
//		}
// 	
// });
//    if(appInfo.mode == 1)

		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("setAddsInfo", "") {

				@Override
				public void action(String[] args) {
					// TODO Auto-generated method stub
					if (args[0] != null) {
						if (!b.isAdmin()) {
							b.getMessageBox("需要提升权限",
									"此命令需要以管理员身份运行！\n请以管理员身份执行" + appInfo.appName + "程序，或者按下alt+3组合键，键入管理员密码提升权限");
						}
						DesUtils des = new DesUtils("black"); // 自定义密钥
						String value = (String) b.settings.value(appInfo.additionalInfo);
						String mess = null;
						if (value != null) {
							String info = null;
							try {
								info = des.decrypt(value);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (info.equals(args[0]))
								mess = "检测到已经储存了信息，并且储存的信息与你正要储存的信息<b>相同</b>，是否覆盖？";
							else
								mess = "检测到已经储存了信息，但储存的信息与你正要储存的信息<b>不同</b>，是否覆盖？";

							int yesOrNo = b.getMessageBoxWithYesNO("重复储存信息", mess, "覆盖已储存的信息", "取消",
									QMessageBox.Icon.Question, 0);
							if (yesOrNo == 0) {
								return;
							}
						} else {
							mess = "储存的信息必须在<b>每周日凌晨2点</b>，且Black能够<b>联网</b>时才能读取！<br>确定要储存吗？";
							int yesOrNo = b.getMessageBoxWithYesNO("储存信息", mess, "储存", "取消", QMessageBox.Icon.Warning,
									0);
							if (yesOrNo == 0) {
								return;
							}
						}
						String encrypt = null;
						try {
							encrypt = des.encrypt(args[0]);
						} catch (Exception e) {
							b.getMessageBox("加密错误", "加密信息时产生错误");
						}
						b.settings.setValue(appInfo.additionalInfo, encrypt);
						if (b.adminPasswd != null)
							b.adminPasswd = args[0];
						b.getMessageBox("addsInfo", "储存成功!\n-------\n原始信息：" + args[0] + "\n加密后为：" + encrypt);
					}
				}
			});

//    b.addCommand(new blackCommand("getAddsInfo","获取附加信息，一般为管理员账户密码，执行此操作还将提升权限，会立刻解除程序切换限制\n20日14点36分",true) {
//
//		@Override
//		public void action(String[] args) {
//			// TODO Auto-generated method stub
//			String timeServer = "http://www.ntsc.ac.cn";//中国科学院国家授时中心服务器地址
//			String DAY_OF_MONTH = time.getWebsiteDateDAY_OF_MONTH(timeServer);
//			if(DAY_OF_MONTH.equals("20")) {
//				String hourOfDay = time.getWebsiteDateHourOfDay(timeServer);
//				String min = time.getWebsiteDateMinOfHours(timeServer);
//
//				if(hourOfDay.equals("14") && min.equals("36")) {
//					String value = (String) b.settings.value(appInfo.additionalInfo);
//					if(value == null) {
//						b.getMessageBox("addsInfo", "什么也没储存！");
//						return;
//					}
//					DesUtils des = new DesUtils("black"); // 自定义密钥
//					String decrypt = null;
//					try {
//						decrypt = des.decrypt(value);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						b.getMessageBox("解密错误", "解密信息时产生错误");
//					}
//					b.userName = "Administrator";
//					InputDialog input = new InputDialog(b, "getAddsInfo", "存储的信息为：", false,true,decrypt) {
//
//						@Override
//						void whenOkButtonPressed() {
//							// TODO Auto-generated method stub
//							
//						}
//
//						@Override
//						void whenCannelButtonPressed() {
//							// TODO Auto-generated method stub
//							
//						}
//
//						@Override
//						void whenClose() {
//							// TODO Auto-generated method stub
//							
//						}};
//					input.setOkButtonName("复制到剪切板");
//					input.setCannelButtonName("取消");
//					int result = input.exec();
//					if(result == 1) {
//						input.copyLineText();
//					}
//					//b.getMessageBox("addsInfo", "储存的信息为："+decrypt+"\n-------\n已提升用户权限，并解除了程序切换限制！");
//					
//				}else if(hourOfDay.equals("@@")) {
//					b.getMessageBox("获取网络时间", "网络错误！");
//					return;
//				}
//				else b.getMessageBox("时间错误", "此操作只能在凌晨2点执行！");
//			}else if(DAY_OF_MONTH.equals("@@")) {
//				b.getMessageBox("获取网络时间", "网络错误！");
//				return;
//			}
//			else b.getMessageBox("日期错误", "此操作只能在星期日执行！");
//		}
//    	
//    });
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("设备管理器", "以管理员身份启动windows设备管理器", true) {
				private boolean openCMDWithAdmin = false;

				public void action(String[] args) {
					// TODO Auto-generated method stub
					Object value = b.settings.value(appInfo.additionalInfo);
					if (value == null) {
						b.getMessageBox("错误", "尚未设定管理员加密密码！");
						return;
					}
					if (b.userName.equals("Administrator")) {
						openCMDWithAdmin = true;
					} else {
//				String timeServer = "http://www.ntsc.ac.cn";//中国科学院国家授时中心服务器地址
//				String DAY_OF_MONTH = time.getWebsiteDateDAY_OF_MONTH(timeServer);
//				if(DAY_OF_MONTH.equals("@@")) {
//					b.getMessageBox("获取网络时间", "网络错误！");
//					return;
//				}
//				else if(DAY_OF_MONTH.equals("20")) {
//					String hourOfDay = time.getWebsiteDateHourOfDay(timeServer);
//					String min = time.getWebsiteDateMinOfHours(timeServer);
//
//					if(hourOfDay.equals("@@"))b.getMessageBox("错误", "网络错误！");
//					else if(hourOfDay.equals("14") && min.equals("36")) {
//						openCMDWithAdmin = true;
//					}else b.getMessageBox("错误", "错误的时间！");
//				}else b.getMessageBox("错误", "错误的日期！");
					}

					if (openCMDWithAdmin) {
						DesUtils des = new DesUtils("black"); // 自定义密钥
						String key = null;
						try {
							key = des.decrypt(value.toString());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:" + key
									+ " /domain: /command:\"mmc devmgmt.msc\" /runpath:d:");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("命令行", "", true) {
				public void action(String[] args) {
					// TODO Auto-generated method stub
					if (b.adminPasswd == null) {
						b.getMessageBox("错误", "尚未设定管理员加密密码！");
						return;
					}
					String username = null;
//			String timeServer = "http://www.ntsc.ac.cn";//中国科学院国家授时中心服务器地址
//			String DAY_OF_MONTH = time.getWebsiteDateDAY_OF_MONTH(timeServer);
//			String hourOfDay = time.getWebsiteDateHourOfDay(timeServer);
//			String min = time.getWebsiteDateMinOfHours(timeServer);
					if (b.userName.equals("Administrator")) {
						username = b.userName;
					} else {
//				if(DAY_OF_MONTH.equals("@@")) {
//					b.getMessageBox("获取网络时间", "网络错误！");
//					return;
//				}
//				else if (DAY_OF_MONTH.equals("20")) {
//					if(hourOfDay.equals("@@"))b.getMessageBox("错误", "网络错误！");
//					else if(hourOfDay.equals("14") && min.equals("36")) {
//						username="Administrator";
//					}else {
//						b.getMessageBox("错误", "错误的时间！");
//					}
//				}else {
//					b.getMessageBox("错误", "错误的日期！");
//				}
					}

					if (username == null)
						return;

					if (username.equals("Administrator") || username.equals("golden")) {
						if (!b.checkRunAsTool())
							return;

						try {
							Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:" + username + " /password:"
									+ b.adminPasswd + " /domain: /command:\"cmd.exe /C start\" /runpath:d:");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							b.getMessageBox("error", e.getMessage());
						}
					}

				}
			});

//    b.addCommand(new blackCommand("电影","",true) {
//
//		public void action(String[] args) {
//			// TODO Auto-generated method stub
//			if(!b.checkRunAsTool())return;
//	    	boolean openCMDWithAdmin = false;
//
//    		Object value = b.settings.value(appInfo.additionalInfo);
//			if(value == null) {
//				b.getMessageBox("错误", "尚未设定管理员加密密码！");
//				return;
//			}
//			if(b.isAdmin()) {
//				openCMDWithAdmin = true;
//			}else {
//				String timeServer = "http://www.ntsc.ac.cn";//中国科学院国家授时中心服务器地址
//				String dayOfWeek = time.getWebsiteDateDayOfWeek(timeServer);
//				if(dayOfWeek.equals("@@")) {
//					b.getMessageBox("获取网络时间", "网络错误！");
//					return;
//				}
//				else if(dayOfWeek.equals("星期四")) {
//					String hourOfDay = time.getWebsiteDateHourOfDay(timeServer);
//					String min = time.getWebsiteDateMinOfHours(timeServer);
//
//					if(hourOfDay.equals("@@"))b.getMessageBox("错误", "网络错误！");
//					else if(hourOfDay.equals("14") && min.equals("36")) {
//						openCMDWithAdmin = true;
//					}else b.getMessageBox("错误", "错误的时间！");
//				}else b.getMessageBox("错误", "错误的日期！");
//			}
//    		
//			
//			if(openCMDWithAdmin) {
//				String mpvPath = "d:\\Apps\\Unpack\\smplayer-portable-17.3.0.0\\mpv\\mpv.exe";
//				if(new File(mpvPath).exists())
//					try {
//						if(!MD5.getMD5Checksum(mpvPath).equals("2bf3aff0c254650926d93a1328c672fe")) {
//							return;
//						}
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				try {
//					Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:"+b.adminPasswd+" /domain: /command:\""+appInfo.javawPath+" -jar .\\bin_\\getMail.jar play\" /runpath:"+new File("").getAbsolutePath());
//
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}
//    });

		b.addCommand(new blackCommand("双拼转全拼", "使用算法强制将不支持展开为全拼的嵌入式双拼输入法编码串（预提交文本）转为全拼，目前仅支持微软双拼\n"
				+ "可以选择是否为转成全拼的拼音串添加间隔符号，通常是'" + "\n跟随参数0或1，0不添加间隔号，1添加间隔号。如果输入法已经在拼音串里添加了间隔符号，则应该禁止添加间隔号'", false) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				String v = (!Boolean.valueOf((String) b.settings.value(appInfo.conversionToFullPinyin, "false"))) + "";
				b.settings.setValue(appInfo.conversionToFullPinyin, v);
				if (v.equals("true")) {
					isRight r = b.isRightIntValue(args[0]);
					int value = (int) r.value;
					if (r.isright) {
						if (value == 0)
							b.settings.setValue(appInfo.useSpiltCharForPinyin, "false");
						else if (value == 1)
							b.settings.setValue(appInfo.useSpiltCharForPinyin, "true");
						else
							b.getMessageBox("参数错误", "参数错误！参数只能是0或1");
					}
				}
				b.setLoadFileMessage("双拼转全拼" + v);
			}
		});

		b.addCommand(new blackCommand("更多候选", "启用此选项后在利用拼音检索时将获取更多关键词候选项，否则只显示一个候选项", true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				String v = (!Boolean.valueOf((String) b.settings.value(appInfo.moreFindMarkWithPinyin, "false"))) + "";
				b.settings.setValue(appInfo.moreFindMarkWithPinyin, v);
				b.setLoadFileMessage("更多候选" + v);
			}

		});
		b.addCommand(new blackCommand("清理编辑点", "清理除当前文档之外的所有文档中的编辑点记录。储存太多的编辑点会使项目文件占用更多的资源，并拖慢项目文件的加载速度", true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				String clearEditPoint = b.clearEditPoint(null);
				b.getBMessageBox("清理除当前文件以外所有文件中的编辑点", "<b>清理了" + cheakDocument.getAllLine(clearEditPoint).size()
						+ "个文件中的编辑点记录：</b>\n<i>" + clearEditPoint + "</i>");
			}

		});
		b.addCommand(new blackCommand("清理加号",
				"清理所有文档名称前的加号（+）。在文档名称前面添加加号（+）可以将其纳入多文档字数统计范围，按下alt+shift+k键程序将会统计所有名称前面添加了加号的文档的字数，并计算这些字数的总和",
				true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				String clearPlusInShowName = b.clearPlusInShowName(null);
				List<String> allLine = cheakDocument.getAllLine(clearPlusInShowName);
				b.getBMessageBox("", "<b>删除了" + allLine.size() + "个文件名中的加号：</b>\n<i>" + clearPlusInShowName + "</i>");
			}

		});
		b.addCommand(new blackCommand("重设index",
				"当程序因断电或错误非正常关闭时，关闭前所编辑的项目的数据可能会受损，可能会出现文件Index错误，出现此错误时，请调用此命令尝试修复Index错误", true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				b.resetProjectFileIndex(true);
			}

		});
		b.addCommand(new blackCommand("隐藏编码框", "隐藏程序内置的针对嵌入式输入法设计的编码框", true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				boolean hide = !b.getBooleanValueFromSettings(appInfo.hideIMEPanel, "true");
				b.settings.setValue(appInfo.hideIMEPanel, hide + "");
				b.setLoadFileMessage("隐藏编码框");
			}

		});
		b.addCommand(new blackCommand("页面阴影", "启用或禁用页面阴影特效（仅写作视图下有效）\n" + "注意：当上下留白值为0时会启用高斯模糊阴影（不推荐。会影响绘图性能），否则只启用线段阴影（推荐）",
				true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				boolean pagesha = !b.getBooleanValueFromSettings(appInfo.usePageShadow, "false");
				b.settings.setValue(appInfo.usePageShadow, pagesha + "");
				b.setLoadFileMessage("启用页面阴影特效" + pagesha);
				b.repaint();
//				b.ba.shadow();
//				b.ba.setShandow(pagesha);
//				b.ba.setShandowAction(pagesha);
			}

		});

		b.addCommand(new blackCommand("设定备份时间", "单位为分钟数,为0时关闭自动备份") {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				int value = ((Integer) right.value).intValue();
				b.backupTime = value * 60000;
				b.settings.setValue(appInfo.backUpTime, b.backupTime);
				if (value > 0)
					b.getMessageBox("设定备份时间", "已将备份时间设定为" + time.msToTime(value * 60 * 1000));
				else
					b.getMessageBox("设定备份时间", "已关闭自动备份");
			}

		});
		b.addCommand(new blackCommand("设定备份位置", "将弹出一个目录框选择备份位置，建议将备份位置设定在外部储存器上，例如闪存盘", true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				if (!b.isAdmin()) {
					b.getMessageBox("设定备份目录", "请以管理员身份执行命令！");
					return;
				}
				b.backupDir = b.getDirDialog("选择备份位置");
				if (!b.backupDir.isEmpty() && new File(b.backupDir).isDirectory())
					b.projectInfo.setProperty(appInfo.backUpDir, b.backupDir);
			}

		});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("去蓝光", "为写作视图设定去蓝光值，值介于0和100之间") {

				@Override
				public void action(String[] args) {
					// TODO Auto-generated method stub
					isRight right = b.isRightIntValue(args[0]);
					if (!right.isright) {
						return;
					}
					int value = ((Integer) right.value).intValue();
					String message = "";
					if (b.writingView == 0)
						message = "请在写作视图中调用此命令";
					else if (value >= 0 && value <= 100) {
						b.withoutBuleValue = value;
						if (b.getIntValueFromSettings(appInfo.editorColor, "0") != 1) {
							b.settings.setValue(appInfo.editorColor, "1");
						}
						b.applyColorChange();
					} else
						message = "值必须介于0与100之间";
					if (!message.isEmpty())
						b.getMessageBox("去蓝光", message);
				}

			});
		b.addCommand(new blackCommand("重启程序", "重新启动" + appInfo.appName, true) {

			@Override
			public void action(String[] args) {
				// TODO Auto-generated method stub
				b.ba.reStartApp();
			}

		});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("收邮件", "仅供管理员或在每个星期六的时候才能调用", true) {

				@Override
				public void action(String[] args) {
					// TODO Auto-generated method stub
					int l = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
					if (!b.isAdmin() && l != 6)
						return;
					b.checkMail();
				}
			});
//    if(appInfo.mode == 1)
//    b.addCommand(new blackCommand("看邮件","先通过-1参数显示邮件列表，然后通过邮件标题前面的序号显示邮件内容\n\n参数：\n-1 检查邮件（不包含"+appInfo.appName+"更新邮件）\n邮件对应的序号 用于显示邮件内容") {
//		
//		@Override
//		public void action(String[] args) {
//			// TODO Auto-generated method stub
//			timerInfo ti = b.getTimerInfo(b.checkMailTimerID);
////			if(ti == null) {
////				b.getMessageBox("看邮件", "好像没有收到邮件！");
////				return;
////			}
//			
//			isRight right = b.isRightIntValue(args[0]);
//	        if (!right.isright) {
//	          return;
//	        }
//	        int value = ((Integer)right.value).intValue();
//	        if(value >= 0 || value == -999) b.clearFindHistoryOnlyCurrent();
//	        File f = new File(appInfo.mailDat);
//	        if(!f.exists())return;
//			StringBuilder sb = new StringBuilder();
//
//	        if(value == -1) {
//	        	for(int i=0;i<b.mailData.size();i++) {
//	        		TextRegion tr = b.mailData.get(i);
//	        		sb.append("<b>"+i+"</b> "+"<i>"+tr.filename+"</i><br>");
//	        	}
//	        }else if(value == -99) {
//	        	for(int i=0;i<b.mailData.size();i++) {
//	        		TextRegion tr = b.mailData.get(i);
//	        		tr.end = -99;
//	        	}
//	        	b.removeAllTimer(-998);
//	        	b.mailDataIsChanged = true;
//	        	b.removeTimer(b.checkMailTimerID);
//	        	return;
//	        }
//	        else {
//	        	if(value >= b.mailData.size() || value < 0) {
//					b.clearFindHistoryOnlyCurrent();
//					return;
//				}
//				else {
//					TextRegion tr = b.mailData.get(value);
//					sb.append(tr.text);
//					tr.end = -99;
//					ArrayList<timerInfo> allTimer = b.getAllTimer(-998);
//					for(timerInfo in:allTimer) {
//						if(tr.start == (int)in.data) {
//							b.removeTimer(in.id);
//							break;
//						}
//					}
//					
//					int count = 0;
//					b.mailDataIsChanged = true;
//					for(TextRegion trr:b.mailData) {
//						if(trr.end != -99) count++;
//					}
//					if(count > 0) ti.timerName = "<font style=\"font-size:13pt;color:yellow\">有"+count+"封邮件</font>";
//					else {
//						b.removeTimer(b.checkMailTimerID);
//						b.checkMailTimerID = -1;
//					}
//					
//				}
//	        }
////	        QTextEdit te = new QTextEdit();
//			QWebView te = new QWebView();
//			if(value >= 0) te.setWindowTitle(b.mailData.get(value).filename);
//			else if(value == -1)
//				te.setWindowTitle("全部邮件（不包含"+appInfo.appName+"更新邮件）");
//			
//			
//			te.setWindowIcon(b.windowIcon());
//			te.setFont(new QFont("微软雅黑"));
//			te.setHtml(sb.toString());
//			te.setZoomFactor(1.5);
//			te.setGeometry(100, 100, 900, 600);
//			te.show();
//	        
////	        String host = "pop.163.com";
////			String username = "yangisboy@163.com";
////			String password = "dxy13633528994";
////			Message[] messages = null;
////			StringBuilder sb = new StringBuilder();
////			int count = 0;
////			try {
////				messages = mailTool.receive(username, password, host);
////				if(value == -1) {
////					for (int i = 0; i < messages.length; i++) {
////						showMail re = new showMail((MimeMessage) messages[i]);
////						String subject = re.getSubject();
////						if(subject.indexOf("black") == -1) {
////							count++;
////							sb.append("<b>"+i+"</b> <i>"+re.getSubject()+"</i><br>");
////						}
////					}
////				}else if(value == -2) {
////					for (int i = 0; i < messages.length; i++) {
////						showMail re = new showMail((MimeMessage) messages[i]);
////						String subject = re.getSubject();
////						if(subject.indexOf("black") == -1 && subject.indexOf("美国之音") == -1) {
////							count++;
////							sb.append("<b>"+i+"</b> <i>"+re.getSubject()+"</i><br>");
////						}
////					}
////					sb.append("(共"+messages.length+"封邮件，筛选后有"+count+"封邮件符合条件[不显示美国之音的邮件])");
////				}else if(value == -999) {
////					Folder folder_INBOX = mailTool.getFolder_INBOX(username, password, host);
////					Message[] messages2 = folder_INBOX.getMessages();
////					for (int i = 0; i < messages2.length; i++) {
////						showMail re = new showMail((MimeMessage) messages2[i]);
////						String subject = re.getSubject();
////						if(subject.indexOf("black") == -1) {
////							messages2[i].setFlag(Flags.Flag.DELETED, true);
////							count++;
////						}
////					}
////					folder_INBOX.close(true);
////					sb.append("删除了"+count+"封邮件");
////				}
////				else {
////					Folder folder_INBOX = mailTool.getFolder_INBOX(username, password, host);
////					Message[] messages2 = folder_INBOX.getMessages();
////					if(value >= messages2.length || value < 0) {
////						b.clearFindHistoryOnlyCurrent();
////						return;
////					}
////					else {
////						String contentType = messages2[value].getContentType();
////						if (contentType.startsWith("text/plain")) {
////							ReceiveMail.getMailTextContent(messages2[value], sb,true);
////						} else
////							ReceiveMail.getMailTextContent(messages2[value],sb, false);	
////						messages2[value].setFlag(Flags.Flag.DELETED, true);
////						folder_INBOX.close(true);
////					}
////				}
////				
////				QTextEdit te = new QTextEdit();
////				
////				if(value >= 0) te.setWindowTitle(messages[value].getSubject());
////				else if(value == -1)
////					te.setWindowTitle("全部邮件（不包含black更新邮件）");
////				else if(value  == -2)
////					te.setWindowTitle("全部邮件（不包含black更新邮件和美国之音邮件）");
////				else if(value  == -999)
////					te.setWindowTitle("邮件删除结果");
////				
////				te.setFrameShape(Shape.NoFrame);
////				te.setWindowIcon(b.windowIcon());
////				te.setReadOnly(true);
////				te.setFont(new QFont("微软雅黑"));
////				te.setHtml(sb.toString());
////				te.setGeometry(100, 100, 900, 600);
////				te.zoomIn(5);
////				te.show();
////			} catch (Exception e) {
////				b.getMessageBox("收邮件", e.getMessage());
////				
////			}
//		}
//	});
//    b.addCommand(new blackCommand("加密","加密当前文档\n参数1为密码") {
//
//		@Override
//		public void action(String[] args) {
//			// TODO Auto-generated method stub
//			if(args[0] != null) {
//				String plainText = b.text.toPlainText();
//				DesUtils desUtils = new DesUtils(args[0]);
//				try {
//					String encryptText = desUtils.encrypt(plainText);
//					QTextCursor tc = black.text.textCursor();
//					tc.select(SelectionType.Document);
//					tc.insertText(encryptText);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//				
//			
//		}

//    });
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("wifi", "开启或关闭笔记本内置wifi卡", true) {

				@Override
				public void action(String[] args) {
					// TODO Auto-generated method stub
					if (!b.isAdmin() && b.hourOfDay < 20)
						return;

					if (!b.checkRunAsTool())
						return;
					try {
						Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:"
								+ b.adminPasswd + " /domain: /command:\"" + appInfo.javawPath
								+ " -jar .\\bin_\\getMail.jar auto\" /runpath:" + new File("").getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (!b.isAdmin())
						bAction.firefoxRuning = true;
				}

			});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("百科白名单", "", true) {

				@Override
				public void action(String[] args) {
					// TODO Auto-generated method stub
					String text = b.getStringValueFromSettings(appInfo.wikipeidaKeywordsList, "");
					DesUtils des = new DesUtils("black");
					try {
						String list = des.decrypt(text);
						String listtext = "";
						if (list != null)
							listtext = list;
						boolean readonly = false;
						String title = "编辑百科白名单";
						String timeServer = "http://www.ntsc.ac.cn";// 中国科学院国家授时中心服务器地址
						String hourOfDay = time.getWebsiteDateHourOfDay(timeServer);
						if (!hourOfDay.equals("12") && !b.isAdmin()) {
							readonly = true;
							title = title + "(只读)";
						}
						new bmessageBox(b, title, "保存", listtext, readonly) {

							@Override
							public void buttonPressedAction(String paramString) {
								// TODO Auto-generated method stub
								if (this.ui.textEdit.isReadOnly()) {
									this.close();
									return;
								}
								int[] stat = cheakDocument.wordStat(paramString);
								if (stat[2] > 20) {
									b.getMessageBox("", "词条数量不能大于20个");
									return;
								}
								try {
									String encrypt = des.encrypt(paramString);
									b.settings.setValue(appInfo.wikipeidaKeywordsList, encrypt);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								this.close();
								if (b.k != null) {
									b.k.arts = b.k.getKeywordsList();
								}
							}
						};
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("run",
					"0 Firefox\n" + "1 surfblock\n" + "2 QQ\n" + "3 LibreOffice writer\n" + "4 office word\n"
							+ "5 office outlook\n" + "6 office onenote\n" + "7 taskmgr\n" + "8 foobar2000\n"
							+ "9 re game\n" + "10 goldendict\n") {

				@Override
				public void action(String[] args) {
					isRight right = b.isRightIntValue(args[0]);
					if (!right.isright) {
						return;
					}
					int value = Integer.valueOf(args[0]);
					if (!b.isAdmin()) {
						if (value == 8)
							;
						else if (value == 4)
							;
						else if (value == 6)
							;
						else if (value == 9)
							;
						else if (value == 10)
							;
						else if (b.hourOfDay < 20)
							return;
					}

					String path = null;
					String system = System.getenv("SystemDrive");
					if (value == 0) {
						path = "Apps\\Unpack\\FirefoxPortable\\FirefoxPortable.exe";
						if (!b.isAdmin())
							bAction.firefoxRuning = true;
					} else if (value == 1) {
						path = "Program Files\\Surfblocker\\surfblock.exe";
					} else if (value == 2) {
						path = "Program Files\\Tencent\\QQ\\Bin\\QQ.exe";
					} else if (value == 3) {
						path = "Apps\\Unpack\\LibreOfficePortablePrevious\\LibreOfficeWriterPortable.exe";
					} else if (value == 4) {
						path = system + "\\Program Files\\Microsoft Office\\Office16\\WINWORD.exe";
					} else if (value == 5) {
						path = system + "\\Program Files\\Microsoft Office\\Office16\\OUTLOOK.exe";
					} else if (value == 6) {
						path = system + "\\Program Files\\Microsoft Office\\Office16\\ONENOTE.exe";
					} else if (value == 7) {
						path = system + "\\Windows\\System32\\taskmgr.exe";
					} else if (value == 8) {
						path = "Apps\\Unpack\\Foobar2000\\Foobar2000.exe";
					} else if (value == 9) {
						long time = Long.valueOf((String) black.settings.value(appInfo.lastRunGameTime, "0"));
						long cur = System.currentTimeMillis();
						if (cur - time < 86400000) {
							b.getMessageBox("玩游戏", "距离上次玩游戏的时间还不到一天的时间，你不能再次玩游戏！");
							return;
						}

						path = "Apps\\Unpack\\game\\Resident Evil 4\\game.exe";
						timerInfo ti = new timerInfo(-11223344, 1800000, "关闭游戏", new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								bAction.closeGame(b);
							}
						}, false);
						b.addTimer(ti);
						black.settings.setValue(appInfo.lastRunGameTime, cur + "");
					} else if (value == 10) {
						path = "Apps\\Unpack\\GoldenDict\\GoldenDict.exe";
					} else {
						return;
					}

					if (path != null) {
						File file = null;
						File p = new File(path);
						if (p.exists()) {
							file = p;
						} else {
							File[] r = File.listRoots();
							for (File f : r) {
								File fi = new File(f.getAbsolutePath() + path);
								if (fi.exists()) {
									file = fi;
									break;
								}
							}
						}

						if (file != null)
							try {
								if (file.canRead() && file.canExecute())
									Runtime.getRuntime().exec(file.getAbsolutePath());
								else {
									if (!b.isAdmin())
										return;
									if (b.adminPasswd == null)
										return;
									if (!b.checkRunAsTool())
										return;
									try {
										Runtime.getRuntime()
												.exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:"
														+ b.adminPasswd + " /domain: /command:\""
														+ file.getAbsolutePath() + "\" /runpath:"
														+ new File("").getAbsolutePath());
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					}
				}
			});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("音量", "设定系统音量，最大值为65535，最小值为0\n-1 最大音量\n-2最小音量\n-3中等音量") {

				@Override
				public void action(String[] args) {
					isRight right = b.isRightIntValue(args[0]);
					if (!right.isright) {
						return;
					}
					int value = Integer.valueOf(args[0]);
					bAction.changedSysVolume(value);
				}
			});
//  b.addCommand(new blackCommand("插入列表","",true) {
//
//		@Override
//		public void action(String[] args) {
//			bAction.insertList();
//	}});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("kms", "", true) {

				@Override
				public void action(String[] args) {
					if (!b.checkRunAsTool())
						return;
					try {
						Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:"
								+ b.adminPasswd + " /domain: /command:\"" + appInfo.javawPath
								+ " -jar .\\bin_\\getMail.jar kms\" /runpath:" + new File("").getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		if (b.dbm)
			if (appInfo.mode == 1)
				b.addCommand(new blackCommand("test", "", true) {

					@Override
					public void action(String[] args) {
//			bAction.conversionDocuments(b);
						String host = b.projectInfo.getProperty(appInfo.gitHost);
						String username = b.projectInfo.getProperty(appInfo.gitUsername);
						String password = b.projectInfo.getProperty(appInfo.gitPassword);
						try {
							b.projectInfo.setProperty(appInfo.gitHost, b.des.encrypt(host));
							b.projectInfo.setProperty(appInfo.gitUsername, b.des.encrypt(username));
							b.projectInfo.setProperty(appInfo.gitPassword, b.des.encrypt(password));

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("chkdsk", "", true) {

				@Override
				public void action(String[] args) {
					if (!b.checkRunAsTool())
						return;
					try {
						Runtime.getRuntime().exec("./Tools/RunAs/lsrunas.exe /user:administrator /password:"
								+ b.adminPasswd + " /domain: /command:\"" + appInfo.javawPath
								+ " -jar .\\bin_\\getMail.jar chkdsk\" /runpath:" + new File("").getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		if (appInfo.mode == 1)
			b.addCommand(new blackCommand("问题列表", "", true) {

				@Override
				public void action(String[] args) {
					b.getBMessageBox("需要修复和改进的问题", appInfo.bugList);
				}
			});
		b.addCommand(new blackCommand("字符间距", "开/关字符间距", true) {

			@Override
			public void action(String[] args) {
				b.ba.charSpace();

			}
		});
		b.addCommand(new blackCommand("加密信息", "使用特定的密钥加密信息", true) {

			@Override
			public void action(String[] args) {
				b.ba.desInfo(b);

			}
		});
		b.addCommand(new blackCommand("上下留白值", "修改写作视图编辑器的上下留白的值", true) {

			@Override
			public void action(String[] arg) {
				String topAndBottom = b.getStringValueFromSettings(appInfo.writingViewTextTopAndBottom, "0 0");
				String[] args = cheakDocument.checkCommandArgs(topAndBottom);
				int top = 0, bottom = 0;
				isRight isTop = b.isRightIntValue(args[0]);
				if (isTop.isright) {
					top = (int) isTop.value;
				}
				isRight isBottom = b.isRightIntValue(args[1]);
				if (isBottom.isright) {
					bottom = (int) isBottom.value;
				}
				new InputDialog(b, "修改编辑器上下留白", "输入上下留白数值（用空格隔开）：", false, false, top + " " + bottom) {

					@Override
					void whenOkButtonPressed() {
						// TODO Auto-generated method stub
						b.settings.setValue(appInfo.writingViewTextTopAndBottom, value());
						if (b.writingView > 0) {
							int width = b.getWritingViewTextWitdh() / 2;
							b.setTextLocationForWritingView(width);
						}

					}

					@Override
					void whenClose() {
						// TODO Auto-generated method stub

					}

					@Override
					void whenCannelButtonPressed() {
						// TODO Auto-generated method stub

					}
				}.exec();

			}
		});
		b.addCommand(new blackCommand("提取电子邮件地址", "提取文档中的所有电子邮件地址", true) {

			@Override
			public void action(String[] args) {
				String mailString = bAction.getMailString(black.text.toPlainText());
				b.getBMessageBox("提取电子邮件地址", mailString);
			}
		});
		b.addCommand(new blackCommand("替换换行符为逗号", "用英文逗号替换当前文档内的所有换行符，将结果用一个新的文本对话框返回", true) {

			@Override
			public void action(String[] args) {
				String stringWith = bAction.getStringWith(black.text.toPlainText());
				b.getBMessageBox("提取电子邮件地址", stringWith);
			}
		});
		b.addCommand(new blackCommand("获取MD5校验值", "获取文档内容的MD5校验值", true) {

			@Override
			public void action(String[] args) {
				try {
					String md5 = MD5.getMD5ChecksumOfString(black.text.toPlainText());
					b.getBMessageBox("文档内容的MD5校验值", md5);
				} catch (Exception e) {
				}
				;
			}
		});
		b.addCommand(new blackCommand("设置颜色",
				appInfo.textToHtml("设置与数字序号对应的界面元素的颜色\n\n<i>" + "0  页面背景颜色（写作视图下有效，支持透明度）\n"
						+ "1  页面文字颜色（写作视图下有效，不支持透明度）\n" + "2  插入符颜色（不支持透明度）\n" + "3 窗口背景颜色（写作视图下有效，支持透明度）\n"
						+ "4 屏幕提示颜色（写作视图下有效，不支持透明度）\n" + "5 右下角提示信息颜色（写作视图下有效，不支持透明度）\n" + "</i>\n" + "颜色序号后面可以追加透明度值，"
						+ "透明度的值的取值范围是0到255，0为全透明，255为完全不透明。注意：如果将某个界面元素的透明度设定为0，该界面元素中完全透明的部分将不会接受鼠标事件（移动和点击）。"
						+ "在不同的环境下执行此命令会设定不同环境中对应界面元素的颜色\n" + "\n例如命令：<i>$bc 设置颜色 3 234</i>\n\n"
						+ "如果在写作视图的亮色模式中执行上面的命令，将会弹出一个颜色选择框为写作视图的亮色模式的窗口背景设定一个颜色，该颜色的透明度为234，此颜色只在进入写作视图亮色模式时有效。"
						+ "对于不支持设置透明度的元素，追加的透明度值将被忽略\n")) {

			@Override
			public void action(String[] args) {
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					b.getMessageBox("参数错误", "参数似乎不是整数");
					return;
				}
				int alpha = 255;
				if (args.length > 1) {
					isRight right1 = b.isRightIntValue(args[1]);
					alpha = (int) right1.value;
				}
				b.ba.setColors((int) right.value, alpha);

			}
		});
//    b.addCommand(new blackCommand("页面边线","启用或禁用页面边线（写作视图）",true) {
//
//		@Override
//		public void action(String[] args) {
//			b.ba.useTextBorder();
//	}});
//    b.addCommand(new blackCommand("页面背景","启用或禁用页面背景（写作视图）",true) {
//
//		@Override
//		public void action(String[] args) {
//			b.ba.useTextBg();
//	}});
//    b.addCommand(new blackCommand("反锯齿","启用或禁用针对页面背景的反锯齿渲染（写作视图）",true) {
//
//		@Override
//		public void action(String[] args) {
//			b.ba.setAntialiasing();
//	}});
		b.addCommand(new blackCommand("更新信息", "显示所有历史更新信息", true) {

			@Override
			public void action(String[] args) {
				b.ba.getUpdateInfoAll(false);
			}
		});
//    b.addCommand(new blackCommand("页面高度","设置页面高度（写作视图下有效），不能小于50。默认为1619") {
//
//		@Override
//		public void action(String[] args) {
//			isRight right = b.isRightIntValue(args[0]);
//	        if (!right.isright) {
//	          return;
//	        }
//	        int value = (int) right.value;
//			if(value >= 50) {
//				b.settings.setValue(appInfo.pageHeght, right.value+"");
//				b.repaint();
//			}
//	}});
		b.addCommand(new blackCommand("打字机模式",
				"模拟打字机移动页面(此功能可能会与打字机卷动功能发生冲突，开启此功能之前请先关闭打字机卷动功能)\n参数：\n0 仅纵向\n1 仅横向\n2 全向\n3 禁用") {

			@Override
			public void action(String[] args) {
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				int value = (int) right.value;
				if (value >= 0 && value <= 3) {
					b.settings.setValue(appInfo.typerMode, value + "");
				}
			}
		});
		b.addCommand(new blackCommand("高分屏模式", "启用或禁用高分屏和平板模式。\n高分屏模式下将增大界面字体字号，便于在高分屏和平板下阅读。非高分屏和平板模式下界面的字号是9号。"
				+ "高分屏和平板模式下界面字号默认为12号，可以为高分屏和平板模式自定义界面字号\n\n" + "参数： 高分屏&平板模式的界面字号值，介于9和15之间", false) {

			@Override
			public void action(String[] args) {
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				int value = (int) right.value;
				if (value >= 9 && value <= 15) {
					b.settings.setValue(appInfo.UIfontSize, value + "");
				} else
					b.getMessageBox("参数错误", "参数只能介于9和15之间（含）");
				b.ba.setUIFontSize();

			}
		});
		b.addCommand(new blackCommand("窗口背景图片", "启用或禁用窗口背景图片（仅支持写作视图）\n"
				+ "请将窗口背景图片放在根目录下的RC文件夹内，图片支持JPG(JPEG)和png(PNG)格式，在写作视图中按下Alt+PageDown组合键循环显示RC文件夹内的图片（作为背景图片）\n"
				+ "可以为亮色和暗色模式分别启用/关闭/设置不同的背景图片", true) {

			@Override
			public void action(String[] args) {
				b.ba.setUsePic();
			}
		});
		b.addCommand(new blackCommand("Bing图片",
				"使用Bing每日图片作为窗口背景\n" + "如果要开启此选项必须先启用窗口背景图片功能，此选项启用后程序会在启动时通过网络获取Bing（国际版）每日图片信息", true) {

			@Override
			public void action(String[] args) {
				b.ba.setUseBingPic();
			}
		});
		b.addCommand(new blackCommand("分词字数", "为字数超出设定值的文档停止执行自动分词，默认对超出2万字的文档停止自动分词\n"
				+ "文档的字数越多，分出的词数也就越多，与用户输入比对耗费的时间也就越多，自动完成面板的弹出延时也就越长" + "\n参数：停止自动分词的文档字数阈值，整数", false) {

			@Override
			public void action(String[] args) {
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				int value = (int) right.value;
				if (value >= 0) {
					b.settings.setValue(appInfo.maxCharsAutoSegment, value + "");
				}

			}
		});
		b.addCommand(new blackCommand("分词词长过滤",
				"设定由自动分词得到的词汇里可被视为关键词显示在自动完成对话框内的词的最小的词长（默认可被接纳的词长是一个字以上的词汇）\n" + "参数：词长，整数", false) {

			@Override
			public void action(String[] args) {
				isRight right = b.isRightIntValue(args[0]);
				if (!right.isright) {
					return;
				}
				int value = (int) right.value;
				if (value >= 0) {
					b.settings.setValue(appInfo.minLenghtOfAutoSegment, value + "");
				}

			}
		});
		b.addCommand(new blackCommand("分词词性信息", "显示自动分词词性信息，需要对自动分词的词汇类型过滤时请参阅此命令的输出信息", true) {

			@Override
			public void action(String[] args) {
				b.ba.showSegmentHelp();

			}
		});
		b.addCommand(
				new blackCommand("分词词性过滤", "设定由自动分词得到的词汇里可被视为关键词显示在自动完成对话框内的词的词性，默认不过滤词性（所有词性的词汇都会接纳为关键词来自动完成）", true) {

					@Override
					public void action(String[] args) {
						b.ba.setSegmentInfo();

					}
				});
		b.addCommand(
				new blackCommand("自动完成延时", "输入法文本或文档内容改变后触发自动完成对话框弹出的延迟时间，单位是毫秒，默认值是500毫秒\n" + "参数：延迟时间，整数", false) {

					@Override
					public void action(String[] args) {
						isRight right = b.isRightIntValue(args[0]);
						if (!right.isright) {
							return;
						}
						int value = (int) right.value;
						if (value >= 100) {
							b.settings.setValue(appInfo.pinyinStartSegmentMS, value + "");
						}

					}
				});
		b.addCommand(new blackCommand("自动添加分词词汇", "自动将选择并插入到文档内的、由自动分词产生的词汇添加到关键词列表内，以便在空文件内继续自动完成这些词汇", true) {

			@Override
			public void action(String[] args) {
				boolean v = !b.getBooleanValueFromSettings(appInfo.autoSaveInputOfAutoSegment, "true");
				b.settings.setValue(appInfo.autoSaveInputOfAutoSegment,
						v);

			}
		});
		b.addCommand(
				new blackCommand("自动完成",
						"启用或禁用在文档内容更改时显示自动完成对话框\n"
								+ "此设置只更改文档更改后是否自动执行自动完成，要禁用或启用使用嵌入编辑式拼音输入法自动辅助自动完成功能，请使用“工具”菜单里提供的设置项，或按下Alt+5组合键",
						true) {

					@Override
					public void action(String[] args) {
						b.ba.showKeywordsWhenTextChanaged();

					}
				});
		b.addCommand(new blackCommand("内联编辑", "通过内联编辑显示编码串", true) {

			@Override
			public void action(String[] args) {
				boolean v = !b.getBooleanValueFromSettings(appInfo.useInLineIME, "false");
				b.settings.setValue(appInfo.useInLineIME,
						v);

			}
		});
		b.addCommand(new blackCommand("显示任务栏", "设定在写作视图中显示或隐藏任务栏", true) {

			@Override
			public void action(String[] args) {
				boolean v = !b.getBooleanValueFromSettings(appInfo.fullScreen, "true");
				b.settings.setValue(appInfo.fullScreen,
						v);
				if(b.writingView == 1) {
					if (v) {
						b.setGeometry(b.ba.desktopGeometry(false));
					} else {
						b.setGeometry(b.ba.desktopGeometry(true));
					}
				}
			}
		});
		b.addCommand(new blackCommand("环境阴影", "启用环境阴影\n"
				+ "参数：\n"
				+ "0 禁用\n1 仅线性\n2 仅半径\n3 线性和半径", false) {

			@Override
			public void action(String[] args) {
				isRight r = b.isRightIntValue(args[0]);
				if(!r.isright)return;
				b.settings.setValue(appInfo.RoundShadow,
						r.value+"");
				b.ba.shadow();
			}
		});
		b.addCommand(new blackCommand("固定插入符位置", "在用输入法输入文本时，未提交之前，禁止插入符移动位置。启用此选项可以避免某些输入法输入时选词面板随插入符一起移动", true) {

			@Override
			public void action(String[] args) {
				boolean v = !b.getBooleanValueFromSettings(appInfo.fixedCaretPosWhenInput, "false");
				b.settings.setValue(appInfo.fixedCaretPosWhenInput,
						v);
				
			}
		});
		b.addCommand(new blackCommand("随机图片", "通过网络获取随机图片作为窗口背景\n"
				+ "启用此功能后程序会在指定的间隔时间后重复获取图片作为窗口背景，显示效果类似于放映幻灯片。鉴于网络畅通性不同，实际的间隔时间可能会大于设定的值\n"
				+ "参数：\n"
				+ "0 通过网络请求新图片的时间间隔（单位秒），默认为1秒，为0时会关闭此功能\n"
				+ "1 关键词，只获取与给定的关键词相关的图片，如果存在多个关键词请用英文逗号隔开。关键词不能包含空格，且为英文", false) {

			@Override
			public void action(String[] args) {
				isRight r = b.isRightIntValue(args[0]);
				if(!r.isright)return;
				int v = (int) r.value;
				if(v<0)return;
				Thread t = (Thread) b.ba.tempData.get("randomImgThread");
				if(t != null) {
					b.ba.tempData.remove("randomImgThread");
					if(v == 0) {
						b.ba.tempData.remove("randomImg");
						b.ba.tempData.remove("randomImgOld");
					}
					b.debugPrint("开始中断线程");
//					t.interrupt();
//					t.stop();
					while(!t.isInterrupted()) {
						t.interrupt();
						b.debugPrint("线程是否被中断："+t.isInterrupted());
					}
				}
				b.settings.setValue(appInfo.randomImg,
						v+"");
				if(args.length > 1) {
					b.settings.setValue(appInfo.randomImgKeywords, args[1]);
				}else {
					b.settings.remove(appInfo.randomImgKeywords);
				}
				b.applyColorChange();

			}
		});
		b.addCommand(new blackCommand("随机图片窗口", "通过网络获取随机图片，并通过新窗口显示\n"
				+ "参数：\n"
				+ "0 重复获取图片的时间间隔（单位秒）不能小于1秒\n"
				+ "1  图片链接，为0时从默认url获取图片\n"
				+ "2 关键词，获取与关键词相关的图片。存在多个关键词时，用英文逗号将每个关键词隔开。要使用此参数，参数1（图片链接）必须为0，"
				, false) {

			private isRight right;

			@Override
			public void action(String[] args) {
				if(args.length < 1)return;
				
				right = b.isRightIntValue(args[0]);
				if(!right.isright) {
					return;
				}
				showRandomImg random = (showRandomImg) b.ba.tempData.get("randomImgWindow");
				if(random != null) {
					random.close();
					b.ba.tempData.remove("randomImgWindow");
				}
				
				int v = (int) right.value;
				if(v < 1) v = 1;
				String url = "0";
				String keywords = null;
				if(args.length > 1) url = args[1]; 
				if(args.length > 2) keywords = args[2];
				showRandomImg showRandomImg = new showRandomImg(v,url,keywords);
				b.ba.tempData.put("randomImgWindow", showRandomImg);
			}
		});
		b.addCommand(new blackCommand("调试", "临时禁用或启用调试模式", true) {

			@Override
			public void action(String[] args) {
				String message = null;
				if(b.dbm) message = "已经禁用调试模式";
				else message = "已经启用调试模式";
				
				b.dbm = !b.dbm;
				
				b.getMessageBox("禁用调试模式", message);
			}
		});
		b.addCommand(new blackCommand("隐藏插入符", "用输入法输入文本时隐藏插入符", true) {

			@Override
			public void action(String[] args) {
				boolean v = !b.getBooleanValueFromSettings(appInfo.noCaretWhenInput, "false");
				b.settings.setValue(appInfo.noCaretWhenInput, v);
			}
		});
		b.addCommand(new blackCommand("机器人接口", "更改聊天机器人接口\n参数（一个字符串） 包含接口链接、返回结果的起始分隔符和结束分隔符，将三个参数以‘隔’字间隔连在一起的字符串", false) {

			@Override
			public void action(String[] args) {
				if(args.length == 1)
				b.settings.setValue(appInfo.rebotAPI, args[0]);
				else b.getMessageBox("参数错误", "参数数量错误");
			}
		});
		b.addCommand(new blackCommand("自言自语", "让机器人和自己说话", false) {


			@Override
			public void action(String[] args) {
				if(br00 == null) {
					br00 = new bRunnable(10,true,true,false,false) {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							String d = (String) this.Data("0");
							if(d.isEmpty())
								d = args[0];
							d = bAction.rebotTalk(d);
							if(d == null)return;
							setData("0", d);
							String rt = d;
							b.uiRun(b, new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									QTextCursor tc = b.text.textCursor();
									tc.movePosition(MoveOperation.End);
									tc.insertText("\n"+rt);
									b.text.setTextCursor(tc);
								}
							});
							
						}
					};
					br00.setData("0", "");
					br00.start();
				}
				else {
					br00.stop();
					br00 = null;
				}
			}
		});
		b.addCommand(new blackCommand("自动滚屏", "自动滚屏，参数表示滚动一个像素的间隔时间，单位毫秒", false) {



			@Override
			public void action(String[] args) {
				if(args.length < 1)return;
				
				isRight right = b.isRightIntValue(args[0]);
				if(!right.isright) {
					return;
				}
				b.infoOfCurrentEditing.autoscrollvalue = (int) right.value;

				if(autoScroll != null) {
					autoScroll.stop();
					autoScroll = null;
				}
				if((int)right.value > 0) {
					autoScroll = new bRunnable((int)right.value) {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(b.isActiveWindow())
							b.text.verticalScrollBar().setValue(b.text.verticalScrollBar().value()+1);
							
						}
					};
				}
			}
		});
		b.addCommand(new blackCommand("字符间距值", "设定字符间的距离，单位是像素，必须大于0且小于等于100", false) {



			@Override
			public void action(String[] args) {
				if(args.length < 1)return;
				
				isRight right = b.isRightIntValue(args[0]);
				if(!right.isright && (int)right.value > 0 && (int)right.value <= 100) {
					return;
				}
				b.settings.setValue(appInfo.charSpaceValue, (100+(int)right.value)+"");
				if(b.getBooleanValueFromSettings(appInfo.charSpace, "false"))
					b.setStyleForCurrentDocument(false);
				else {
					b.getMessageBox("提示", "字符间距尚未启用，必须先启用字符间距才能看到此设置的效果");
				}
			}
		});
		if (b.dbm)
			b.addCommand(new blackCommand("test", "", true) {

				@Override
				public void action(String[] args) {
					b.ba.test();
				}
			});

	}

	static void addTimerAction(final black b, final int value, final String timerName, QMessageBox messageBox,
			final String message_) {
		final String messageEnd = "----------\n开始于" + time.getCurrentTimeHasSecond();
		b.addTimer(new timerInfo(-1, value * 1000, timerName, new Runnable() {
			public void run() {
				b.uiRun(b, new Runnable() {
					public void run() {
						if ((messageBox != null) && (messageBox.isVisible())) {
							messageBox.hide();
						}
						black.startBeepSound(-1);
						int mb = b.getMessageBoxWithYesNO("时间到",
								message_ + " 时间到！\n" + messageEnd + "\n结束于" + time.getCurrentTimeHasSecond(), "重新计时",
								"退出", b.ico_timer, 0, true);
						black.stopBeepSound();
						if (mb == 1) {
							addCommands.addTimerAction(b, value, timerName, messageBox, message_);
						}
					}
				});
			}
		}, true));
	}
}
