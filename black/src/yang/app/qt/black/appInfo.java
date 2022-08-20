package yang.app.qt.black;

public class appInfo {
	//月份应该比实际的小1，因为java日历获取到的月份是从0月开始的
	//要使测试日期失效，将年份和月份置为-1即可
	static int outYear = 2020,outMonth = 9;
	public static String appName = "BlackDraft";
	public static String appVersion = "0.6951";
	public static String buildDate = "2022-04-23";
	public static String appWebsite = "https://github.com/piiiiq/BlackDraft";
	/**
	 * 一个临时计数，用于测试
	 */
	public static int test = 0;
	/**
	 * 用于指示更新来自哪个邮箱
	 */
	public static String updateFrom = "";
	/**
	 * 0公用
	 * 1自用
	 */
	public static int mode = 0;
	public static String loaderVer = "不可用";
	static String updateInfo =""
			+ getAppVersionText("0.6951","修正退出写作视图不重新设置编辑器边距")
			+ getAppVersionText("0.695","启用自动滚屏时按动上下方向键会滚动页面，而不是移动光标")
			+ getAppVersionText("0.693","修改页面阴影为线段阴影；视情况添加页首空白")
			+ getAppVersionText("0.692","增加自动滚屏功能，并可以为每个文档保存自动滚屏数据；能够自定义字符间的距离")
			+ getAppVersionText("0.691","增加聊天机器人功能")
			+ getAppVersionText("0.69","修复bug，添加随机图片功能")
			+ getAppVersionText("0.681","升级底层库为最新版（Java14与Qt5.15)，迁移至64位平台，不再提供32位版本")
			+ getAppVersionText("0.6782","拼音检索时不会每次输入拼音都会重新对文档内容进行分词")
			+ getAppVersionText("0.6781","修复使用全拼输入法不能延时检索关键词的bug")
			+ getAppVersionText("0.678","改进自动完成功能，引入自动分词模块，支持在不定义关键词的情况下对文档内容进行模糊分词，并提供非精准的自动完成功能；"
					+ "在用嵌入式拼音输入法辅助自动完成功能时，可以输入大写字母来快速定位关键词条目；在文档内容发生改变时，会基于插入符前一个字符自动查找关键词;增加简繁转换;修复一些bug")
			+ getAppVersionText("0.6758","修改嵌入式输入法编码框的外观")
			+ getAppVersionText("0.6757","修复插入符在编辑器最底端时会被遮挡的问题")
			+ getAppVersionText("0.6755","修复模拟打字机移动页面模式问题；修复页面右边距问题；改进写作视图载入文档速度")
			+ getAppVersionText("0.6754","修复已经打开项目时退出打开项目对话框后自动跳转到默认项目的问题")
			+ getAppVersionText("0.6753","修复Bing故事界面右下角提示信息位置错误；将Bing故事呈现组件改为全动态布局")
			+ getAppVersionText("0.675","修复界面元素位置错误")
			+ getAppVersionText("0.674","重构一些代码；添加菜单；修复bug；增加程序健壮性")
			+ getAppVersionText("0.673","使用Bing每日图片作为背景，修复bug")
			+ getAppVersionText("0.672","修复更新模块字符编码错误；添加一些菜单项；可自动更新部署模块")
			+ getAppVersionText("0.668","自动下载&部署更新")
			+ getAppVersionText("0.667","添加增量更新，缩小更新包体积")
			+ getAppVersionText("0.665","添加高分屏模式；重写文本对话框布局代码；修复检查更新bug；用page键滚屏时保留一些像素")
			+ getAppVersionText("0.664","将背景图片更改为jpg格式，缩小图片体积以便加快载入图片速度")
			+ getAppVersionText("0.663","修复几处新发现的问题(例如加载文件缓慢的问题)；增加一些菜单项")
			+ getAppVersionText("0.662","改进项目检索功能，可跳回检索前的位置，修复检索框的一些问题")
			+ getAppVersionText("0.661","统一写作视图模式；可以修改更多界面元素的颜色，可以针对不同颜色模式设置是否启用图片背景")
			+ getAppVersionText("0.660","修正从搜索替换框中选择替换全部会出现替换错误的问题")
			+ getAppVersionText("0.659","白天和夜间模式支持高性能写作视图；修正从最大化窗口进入写作视图在退出写作视图后不还原成最大化窗口的问题")
			+ getAppVersionText("0.658")+"高性能写作视图下可以更改窗口透明度（全屏视图）\n"
			+ getAppVersionText("0.657")+"添加高性能写作视图模式\n"
			+ getAppVersionText("0.654")+"添加打字机模式，可以模拟打字机动态移动页面\n"
			+ getAppVersionText("0.652")+"重新加入隐藏编码框命令；可以更改插入符的颜色\n"
			+ getAppVersionText("0.643")+"禁用锚点和实验性撤销还原，修复窗口复位问题；添加文件时自动修复文件指针错误等\n"
			+ getAppVersionText("0.631")+"增加Linux系统支持\n"
			+ getAppVersionText("0.628")+"内建插入中文双引号快捷键\n"
			+ getAppVersionText("0.627")+"页面添加角标（写作视图）\n"
			+ getAppVersionText("0.625")+"修复嵌入式输入bug;写作视图下添加页面背景，可以更改写作视图下的页面和文字颜色\n"
			+ getAppVersionText("0.611")+"启用内嵌编辑\n"
			+ getAppVersionText("0.610")+"启用程序自建撤销&重做框架\n"
			+ getAppVersionText("0.607")+ "Git信息已加密储存\n"
			+ getAppVersionText("0.604")+"修复锚点列表所存在的问题\n"
			+ getAppVersionText("0.602")+"修复项目加载时的错误\n"
			+ getAppVersionText("0.601")+"可以用快捷键遍历RC目录下的背景图片\n"
			+ getAppVersionText("0.598")+"添加打字机卷动动画，增加了几个命令行参数\n"
			+ getAppVersionText("0.595")+"写作视图下显示任务栏；修改写作视图编辑器上下边距命令\n"
			+ getAppVersionText("0.591")+"重新加载背景命令；可提高（白天）或降低（晚上）背景图片亮度；编码框背景透明\n"
			+ getAppVersionText("0.590")+"添加字符间距，便于长时间查看\n"
			;
	public static String bugList = "<ul>"
			+ "<li>通过输入法输入文本，或者通过关键词列表插入文本时，在与锚点面板同步数据之后，redo/undo列表存在多余的步骤，应该获取Qtextcursor一次性编辑"
			+ "<li>撤销/重做同步锚点数据时存在性能问题"
			+ "<li>通过搜索框检索文本时，结果面板不应该隐藏，按下ESC键应该删除检索结果，以便重新检索"
			+ "</ul>";
	public static String addsInfo = "要使备份目录可被程序自行搜索，"
			+ "务必将备份目录建立在驱动器根目录下，并命名为BlackBackups<br>"
			+ "在文档开头添加“$bc 加密”文档将会加密保存"
			+ "<br>要使标题样式生效，首行缩进值至少要大于0";
	public static String aboutApp = "<b>"+appName+"</b><i>"
			+ " - 撰写故事底稿；快速定位、修改和编辑无格式文本；支持编辑点跳转和中文自动补全的沉浸式文学类草稿写作环境"
			+ ""
			+ "</i>";
	public static String authorInfo = "<br>版权© 2018-2020归段晓阳(beihuiguixian@hotmail.com)所有，保留最终解释权。"
			+ "<br>Copyright© 2018-2020 Young D. Shaw. All Rights Reserved.";
	public static String argsHelpInfo = "\n用法：Black.exe -参数" + "\n有效参数：" + "\n-help/-?/-h 输出帮助信息" + "\n-show 强制显示窗口"
			+ "\n-clone 从本地仓库克隆项目"
			+ "\n-debug 启用调试模式"
			+ "\n-full_writingView 启用全屏幕写作视图"
			+ "\n-noPageLine 禁止绘制分页线"
			+ "\n-noMarkChar 禁止绘制附加标记（例如段落标记等）";
	public static  String host0 = "pop.163.com",
			username0 = "yangisboy@163.com",
			port0 = "-1",
			password0 = "dxy13633528994",
			host1 = "pop.yandex.com",
			port1 = "995",
			username1 = "blackdraft@yandex.com",
			password1 = "527332367";
		
	public static String based = "QT";
	public static String lastUsedProject = "app/lastUsedProject";
	public static String editing = "editing";
	public static String zoomValue = "app/textzoom";
	public static String font = "app/font";
	public static String lineHeight = "app/lineheight";
	public static String paragraphSpace = "app/paragraphspace";
	public static String firstLine = "app/firstline";
	public static String caretWidth = "app/caretwidth";
	public static String fontSize = "app/fontsize";
	public static String textWidthForWritingView = "app/textwidthforwritingview";
	public static String writingViewMode = "app/writingviewmode";
	public static String shadowAlphaForWritingViewWindowMode = "app/backgroundalpha";
	public static String documentMargin = "app/documentmargin";
	public static String autoSave = "app/autoSave";
	public static String fileIndex = "fileindex";
	public static String projectName = "projectname";
	public static String gitHost = "githost";
	public static String gitUsername = "gitusername";
	public static String gitPassword = "gitpassword";
	public static String keywordsWidth = "app/keywordswidth";
	public static String keywordsHeight = "app/keywordsheight";
	public static String topKeywordsCount = "app/topkeywordscount";
	public static String onlyAPart = "app/onlyapart";
	public static String isWritingView = "app/iswritingview";
	public static String timeMode = "app/timemode";
	public static String editorColor = "app/editorcolor";
	public static String moveIndex = "moveindex";
	public static String keywordsSeparator = "@";
	public static String setAlphaByTime = "app/setalphabytime";
	public static String kiwixLastURL = "app/kiwixlasturl";
	public static String goldenDictPath = "app/goldendictpath";
	public static String mainClassNameOfExtendModule = "yang.app.qt.black.black";
	public static String findIn = "findIn";
	public static String FindHistory = "findhistory";
	public static String blackCommand = "$bc";
	public static String blackCommandAndQuiet = "$bcq";
	public static String animationTime = "app/beeptime";
	public static String blurRadiusWindowMode = "app/blurRadius";
	public static String blurRadiusFullScreen = "app/blurRadiusfullscreen";
	public static String shadowAlphaForWritingViewFullScreen = "app/alphafullscreen";
	public static String windowOpacity = "app/windowOpacity";
	public static String saveMode = "app/savemode";
	public static String yellowForceColor = "app/yellowForceColor";
	public static String noCaretShadow = "app/noCaretShadow";
	public static String alpha_windowBackground = "app/alpha_windowBackground";
	public static String whatStart = "app/whatStart";
	public static String quietMode = "app/quietMode";
	public static String whatTime = "app/whatTime";
	public static String lineAndDocFirst = "app/lineAndDocFirst";
	public static String noCaret = "app/noCaret";
	public static String noCaretS = "app/noCaretS";
	public static String boldFont = "app/boldFont";
	public static String italicFont = "app/italicFont";
	public static String simpleTopPanelMask = "app/simpleTopPanelMask";
	public static String noDock = "app/noDock";
	public static String smallTop = "app/smallTop";
	public static String noTop = "app/noTop";
	public static String fastKeyWords = "app/fastKeyWords";
	public static String focusMode = "app/focusMode";
	public static String lastEditFile = "lastEditFile", typeModeValue = "app/typeModeValue",
			textVScrollbarWidth = "app/textVScrollbarWidth", appVersionKey = "app/appVersion",
			IMECodePanelFont = "app/IMECodePanelFont", // 未使用
			editorHeight = "app/editorHeight", noBackgroundForWritingView = "app/noBackgroundForWritingView",
			additionalInfo = "app/additionalInfo", hideIMEPanel = "app/hideIMEPanel",
			conversionToFullPinyin = "app/conversionToFullPinyin",
			adminPasswordWithEncrypt = "app/adminPasswordWithEncrypt",
			moreFindMarkWithPinyin = "app/moreFindMarkWithPinyin", backUpDir = "backUpDir",
			lastBackUpTime = "lastBackUpTime", backUpTime = "app/backUpTime", withoutBlueValue = "app/withoutBlueValue",
			/**
			 * 通过键盘或者鼠标进行标点符号改写，为0时通过鼠标改写，为1时通过键盘改写
			 */
			insByMouseOrKey = "insByMouseOrKey", voiceRate = "app/voiceRate", mailDat = "./RC/mailDat",
			kiwixScrollBarValue = "app/kiwixScrollBarValue", blackUD = "BlackUD",
			wifiHWID = "PCI\\VEN_168C&DEV_002B&CC_0280", wikipeidaKeywordsList = "app/wikiKeywordsList",
			LightOrDark = "app/showPic", 
			showTitlePanel = "app/showTitlePanel",
			lastReadOfYW = "app/lastReadOfYW",
			lastRunGameTime = "app/lastGameTime",
			uiFont = "微软雅黑",
			/**
			 * 1将不可编辑和查看项目内容
			 */
			miniMode = "0",
			MD5OFGetMail = "app/MD5OFGetMail",
			javawPath = "./jre/bin/javaw.exe",
			charSpace = "app/charSpace",
			writingViewTextTopAndBottom = "app/writingViewTextTopAndBottom",
			lightWindowBgImg = "app/bgImg",
			payImgSUM = "a6e9c0c297516ead0853c76c1f1cb2b7",
			useBgColor = "app/useBgColor",
			colortext = "app/colortext",
			colortextbg = "app/colortextbg",
			textbgborder = "app/border",
			Antialiasing = "app/antialiasing",
			usePinyinFindKeywords = "app/usePinyinFindKeyword",
			pageHeght = "app/pageHeght",
			caretColor_normal = "app/caretColor_N",
			caretColor_writingview = "app/caretColor_W",
			typerMode = "app/typerMode",
			simpleWritingView = "app/simpleWritingView",
			writingViewAlpha = "app/writingViewAlpha",
			lightWindowColor = "app/lightWindowColor",
			darkWindowColor = "app/darkWindowColor",
			lightTextColor = "app/lightTextColor",
			darkTextColor = "app/darkTextColor",
			lightPageColor = "app/lightPageColor",
			darkPageColor = "app/darkPageColor",
			lightCaretColor = "app/lightCaretColor",
			darkCaretColor = "app/darkCaretColor",
			darkWindowBgImg = "app/darkWindowBgImg",
			useLightWindowBgImg = "app/useLightWindowBgImg",
			useDarkWindowBgImg = "app/useDarkWindowBgImg",
			lightLoadMessageColor = "app/lightLoadMesageColor",
			darkLoadMessageColor = "app/darkLoadMessageColor",
			lightTimeMessageColor = "app/lightTimeMessageColor",
			darkTimeMessageColor = "app/darkTimeMessageColor",
			UIfontSize = "app/UIfontSize",
			useBigUIFont = "app/useBigUIFont",
			bingPicInfo = "app/bingPicInfo",
			lightBingPic = "app/lightBingPic",
			darkBingPic = "app/darkBingPic",
			usePageShadow = "app/usePageShadow",
			useSpiltCharForPinyin = "app/useSpiltCharForPinyin",
			maxCharsAutoSegment = "app/maxCharsAutoSegment",
			pinyinStartSegmentMS = "app/pinyinStartSegmentMS",
			minLenghtOfAutoSegment = "app/minLenghtOfAutoSegment",
			infoOfAutoSegment = "app/infoOfAutoSegment",
			autoSaveInputOfAutoSegment = "app/autoSaveInputOfAutoSegment",
			autoShowKeywords = "app/autoShowKeywords",
			useInLineIME = "app/useInLineIME",
			fullScreen = "app/fullScreen",
			RoundShadow = "app/RoundShadow",
			randomImg = "app/randomImg",
			randomImgKeywords = "app/randomImgKeywords",
			fixedCaretPosWhenInput = "app/fixedCaretPosWhenInput",
			noCaretWhenInput = "app/noCaretWhenInput",
			rebotAPI = "app/rebotAPI",
			charSpaceValue = "app/charSpaceValue"
			;
	public static String keyInfo = "选中文本按下<b>tab</b>键快速检索当前文件\n"
			+ "选中文本按下<b>ctrl+tab</b>键快速检索父目录里的所有文件\n"
			+ "选中文本按下<b>enter</b>键可将其加入关键词列表\n"
			+ "未选中文本的状态下按下<b>alt+enter</b>键可打开标题列表\n"
			+ "未选中文本的状态下按下<b>ctrl+shift+enter</b>键可重新搜索关键词列表\n"
			+ "未选中文本的状态下按下<b>ctrl+tap</b>可在打开的文件之间切换\n"
			+ "<b>ctrl+0</b>开启/关闭静音模式\n"
			+ "<b>ctrl+`</b>快速将文本中的逗号替换为句号，或反之\n"
			+ "<b>ctrl+I</b>将当日输入的字符数统计清零\n"
			+ "<b>alt+`</b>产生一个中文人名并插入正文\n"
			+ "<b>F2</b>产生一个英文男名并插入正文\n"
			+ "<b>F3</b>产生一个英文女名并插入正文\n"
			+ "<b>F4</b>产生一个意大利男名并插入正文\n"
			+ "<b>F5</b>产生一个意大利女名并插入正文\n"
			+ "<b>F6</b>产生20个中文人名\n"
			+ "<b>alt+;(分号)</b>快速移动插入符至当前的编辑点、段首或段尾\n"
			+ "<b>alt+u</b>移动到前一次编辑的位置\n"
			+ "<b>alt+o</b>移动到后一次编辑的位置\n"
			+ "<b>alt+i</b>插入符上移\n"
			+ "<b>alt+k</b>插入符下移\n"
			+ "<b>alt+j</b>插入符左移\n"
			+ "<b>alt+l</b>插入符右移\n"
			+ "<b>alt+f</b>换行\n"
			+ "<b>alt+h</b>退格\n"
			+ "<b>alt+n/m</b>插入中文左右双引号\n"
			+ "<b>F9</b>开启或关闭编辑器动画效果\n"
			+ "<b>alt+pagedown</b>下一幅背景图片\n"
			+ "<b>ctrl/alt+2</b>在写作视图中切换颜色模式\n"
			+ "<b>ctrl/alt+1</b>手动备份项目\n"
			+ "<b>ctrl/alt+4</b>切换标点改写模式（通过鼠标或键盘改写标点）\n"
			+ "<b>ctrl/alt+5</b>开启/关闭拼音输入法检索关键词\n"
			+ addKeyInfo("Shift+ESC","退出程序")
			;
	
	public static final String[] syls = { ".", "。", "!", "！", "?", "？", "——", "……", "”", "\"", "】", "：", ":" };
	
	public static String textToHtml(String text) {
		StringBuilder sb = new StringBuilder();
		sb.append(text.replaceAll("\n", "<br>"));
		return sb.toString();
	}
	/**
	 * 获取测试版本的信息，此方法会计算软件过期时间
	 * 如果过期日期或月份为-1，则返回不含有关软件评估的信息
	 * @return
	 */
	static String getInfo() {
		String testInfo = "<ul><li>此版本的"+appName+"软件仅作为评估之用，评估截止日期为"
				+ outYear+"年"+(outMonth-1)+"月"
				+ "。评估期内您可以自由复制、分发和使用此软件。"
				+ "<li>本软件可能存在某些尚未被发现和修复的错误，这些错误可能导致使用者的数据遭受损失。"
				+ "<li>如您使用本软件，即视您已经了解这些信息，并愿意承担使用本软件所产生的任何后果。"
						+ "<b>对于您因使用本软件而产生的损失，开发者将不承担任何责任。</b>"
				+ "<li>开发者保留此软件后续版本授权的重新定义权。 "
				+ "</ul>"
				+ "\n-----------\n";
		if(outYear == -1 || outMonth == -1)
			testInfo = "";
		String info = testInfo
				+ "<b><i>本软件链接和使用了下列开放源代码模块：</i></b>"
				+ "<i><ul>"
				+ "<li>QTJAMBI - LGPL-3许可证(sourceforge.net/projects/qtjambi)"
//				+ "<li>OSHI - BSD许可证(pinyin4j.sourceforge.net)"
				+ "<li>HanLP - Apache-2许可证(github.com/hankcs/HanLP/)"
				+ "<li>JGIT - EDL许可证(www.eclipse.org/jgit/)"
				+ "<li>PINYIN4J - BSD许可证(pinyin4j.sourceforge.net)"
				+ "<li>WINRUN4J - CPL许可证(github.com/poidasmith/winrun4j)"
				+ "</ul>"
				+ "Apache-2许可证的定义，见(www.apache.org/licenses/LICENSE-2.0.html)网站。<br>"
				+ "LGPL-3许可证的定义，见(www.gnu.org)网站。<br>"
//				+ "MIT许可证的定义，见(www.mit-license.org)网站。<br>" 
				+ "CPL许可证的定义，见(www.ibm.com/developerworks/library/os-cpl.html)网站。<br>"
				+ "BSD许可证的定义，见(www.linfo.org/bsdlicense.html)网站。<br>"
				+ "EDL许可证的定义，见(www.eclipse.org/org/documents/edl-v10.php)网站。<br>"
				+ "</i>";
		return info;
	}
	/**
	 * 返回应用版本的文本描述，如果需要在更新信息中插入版本信息，使用此方法可以得到规范格式的版本信息
	 * @param ver
	 * @return
	 */
	static String getAppVersionText(String ver) {
		return "<i>"+ver+"</i> ";
	}
	static String getAppVersionText(String ver,String UpdateInfo) {
		return getAppVersionText(ver)+UpdateInfo+"\n";
	}
	static String getAppVersionText(String ver,String date,String UpdateInfo) {
		return getAppVersionText(ver)+UpdateInfo+"("+date+")\n";
	}
	/**
	 * 添加快捷键描述信息
	 * @return
	 */
	static String addKeyInfo(String key,String info) {
		return "<b>"+key+"</b>"+info+"\n";
	}
	static String segmentInfo = "下面一行包含了所有名词词性，如果要让程序自动完成分词得到的全部名词，复制下面一行到[分词词性过滤]命令打开的词性编辑框即可：\n"
			+ "n nb nba nbc nbp nf ng nh nhd nhm ni nic nis nit nl nm nmc nn nnd nnt nr nr1 nr2 nrf nrj ns nsf nt ntc ntcb ntcf ntch nth nto nts ntu nx nz"
			+ "\n\n所有词性简写及其含义详见下面：\n"
			+ "a 	形容词\r\n" + 
			"ad 	副形词\r\n" + 
			"ag 	形容词性语素\r\n" + 
			"al 	形容词性惯用语\r\n" + 
			"an 	名形词\r\n" + 
			"b 	区别词\r\n" + 
			"	begin\r\n" + 
			"bg 	区别语素\r\n" + 
			"bl 	区别词性惯用语\r\n" + 
			"c 	连词\r\n" + 
			"cc 	并列连词\r\n" + 
			"d 	副词\r\n" + 
			"dg 	辄,俱,复之类的副词\r\n" + 
			"dl 	连语\r\n" + 
			"e 	叹词\r\n" + 
			"end 	仅用于终##终\r\n" + 
			"f 	方位词\r\n" + 
			"g 	学术词汇\r\n" + 
			"gb 	生物相关词汇\r\n" + 
			"gbc 	生物类别\r\n" + 
			"gc 	化学相关词汇\r\n" + 
			"gg 	地理地质相关词汇\r\n" + 
			"gi 	计算机相关词汇\r\n" + 
			"gm 	数学相关词汇\r\n" + 
			"gp 	物理相关词汇\r\n" + 
			"h 	前缀\r\n" + 
			"i 	成语\r\n" + 
			"j 	简称略语\r\n" + 
			"k 	后缀\r\n" + 
			"l 	习用语\r\n" + 
			"m 	数词\r\n" + 
			"mg 	数语素\r\n" + 
			"Mg 	甲乙丙丁之类的数词\r\n" + 
			"mq 	数量词\r\n" + 
			"n 	名词\r\n" + 
			"nb 	生物名\r\n" + 
			"nba 	动物名\r\n" + 
			"nbc 	动物纲目\r\n" + 
			"nbp 	植物名\r\n" + 
			"nf 	食品，比如“薯片”\r\n" + 
			"ng 	名词性语素\r\n" + 
			"nh 	医药疾病等健康相关名词\r\n" + 
			"nhd 	疾病\r\n" + 
			"nhm 	药品\r\n" + 
			"ni 	机构相关（不是独立机构名）\r\n" + 
			"nic 	下属机构\r\n" + 
			"nis 	机构后缀\r\n" + 
			"nit 	教育相关机构\r\n" + 
			"nl 	名词性惯用语\r\n" + 
			"nm 	物品名\r\n" + 
			"nmc 	化学品名\r\n" + 
			"nn 	工作相关名词\r\n" + 
			"nnd 	职业\r\n" + 
			"nnt 	职务职称\r\n" + 
			"nr 	人名\r\n" + 
			"nr1 	复姓\r\n" + 
			"nr2 	蒙古姓名\r\n" + 
			"nrf 	音译人名\r\n" + 
			"nrj 	日语人名\r\n" + 
			"ns 	地名\r\n" + 
			"nsf 	音译地名\r\n" + 
			"nt 	机构团体名\r\n" + 
			"ntc 	公司名\r\n" + 
			"ntcb 	银行\r\n" + 
			"ntcf 	工厂\r\n" + 
			"ntch 	酒店宾馆\r\n" + 
			"nth 	医院\r\n" + 
			"nto 	政府机构\r\n" + 
			"nts 	中小学\r\n" + 
			"ntu 	大学\r\n" + 
			"nx 	字母专名\r\n" + 
			"nz 	其他专名\r\n" + 
			"o 	拟声词\r\n" + 
			"p 	介词\r\n" + 
			"pba 	介词“把”\r\n" + 
			"pbei 	介词“被”\r\n" + 
			"q 	量词\r\n" + 
			"qg 	量词语素\r\n" + 
			"qt 	时量词\r\n" + 
			"qv 	动量词\r\n" + 
			"r 	代词\r\n" + 
			"rg 	代词性语素\r\n" + 
			"Rg 	古汉语代词性语素\r\n" + 
			"rr 	人称代词\r\n" + 
			"ry 	疑问代词\r\n" + 
			"rys 	处所疑问代词\r\n" + 
			"ryt 	时间疑问代词\r\n" + 
			"ryv 	谓词性疑问代词\r\n" + 
			"rz 	指示代词\r\n" + 
			"rzs 	处所指示代词\r\n" + 
			"rzt 	时间指示代词\r\n" + 
			"rzv 	谓词性指示代词\r\n" + 
			"s 	处所词\r\n" + 
			"t 	时间词\r\n" + 
			"tg 	时间词性语素\r\n" + 
			"u 	助词\r\n" + 
			"ud 	助词\r\n" + 
			"ude1 	的 底\r\n" + 
			"ude2 	地\r\n" + 
			"ude3 	得\r\n" + 
			"udeng 	等 等等 云云\r\n" + 
			"udh 	的话\r\n" + 
			"ug 	过\r\n" + 
			"uguo 	过\r\n" + 
			"uj 	助词\r\n" + 
			"ul 	连词\r\n" + 
			"ule 	了 喽\r\n" + 
			"ulian 	连 （“连小学生都会”）\r\n" + 
			"uls 	来讲 来说 而言 说来\r\n" + 
			"usuo 	所\r\n" + 
			"uv 	连词\r\n" + 
			"uyy 	一样 一般 似的 般\r\n" + 
			"uz 	着\r\n" + 
			"uzhe 	着\r\n" + 
			"uzhi 	之\r\n" + 
			"v 	动词\r\n" + 
			"vd 	副动词\r\n" + 
			"vf 	趋向动词\r\n" + 
			"vg 	动词性语素\r\n" + 
			"vi 	不及物动词（内动词）\r\n" + 
			"vl 	动词性惯用语\r\n" + 
			"vn 	名动词\r\n" + 
			"vshi 	动词“是”\r\n" + 
			"vx 	形式动词\r\n" + 
			"vyou 	动词“有”\r\n" + 
			"w 	标点符号\r\n" + 
			"wb 	百分号千分号，全角：％ ‰ 半角：%\r\n" + 
			"wd 	逗号，全角：， 半角：,\r\n" + 
			"wf 	分号，全角：； 半角： ;\r\n" + 
			"wh 	单位符号，全角：￥ ＄ ￡ ° ℃ 半角：$\r\n" + 
			"wj 	句号，全角：。\r\n" + 
			"wky 	右括号，全角：） 〕 ］ ｝ 》 】 〗 〉 半角： ) ] { >\r\n" + 
			"wkz 	左括号，全角：（ 〔 ［ ｛ 《 【 〖 〈 半角：( [ { <\r\n" + 
			"wm 	冒号，全角：： 半角： :\r\n" + 
			"wn 	顿号，全角：、\r\n" + 
			"wp 	破折号，全角：—— －－ ——－ 半角：— —-\r\n" + 
			"ws 	省略号，全角：…… …\r\n" + 
			"wt 	叹号，全角：！\r\n" + 
			"ww 	问号，全角：？\r\n" + 
			"wyy 	右引号，全角：” ’ 』\r\n" + 
			"wyz 	左引号，全角：“ ‘ 『\r\n" + 
			"x 	字符串\r\n" + 
			"xu 	网址URL\r\n" + 
			"xx 	非语素字\r\n" + 
			"y 	语气词(delete yg)\r\n" + 
			"yg 	语气语素\r\n" + 
			"z 	状态词\r\n" + 
			"zg 	状态词";
}
