基于SWT库的Black软件已不再更新，后续的Black软件将基于QT库开发，新版Black预览图：
--------
![image](https://github.com/piiiiq/Black/blob/master/images/blackQT.PNG)
新版特性：<br>
=======
* 完善的项目树管理功能，支持以可视化的方式插入子文档、关键词列表等
* 比旧版更完善的打字机卷动功能
* 支持段落间距
* 改进的关键词列表预测算法
* 改进的写作视图设置


关于旧版程序：<br>
--------------
软件是自己用来撰写小说草稿的，只支持windows系统，不具备排版，插入图片、表格、公式等功能。受限于SWT-StyledText类的功能，软件甚至不支持段落间距，如果您需要上述这些功能，就不必在这个软件上浪费时间了。<br>

本来是给自己用的，以前用word写东西，在多个文件间查找/替换不太方便，而且对于经常输入的特殊词汇和短语也无法自动补全，于是动手做了这个软件。发现bug请点击“帮助-发送反馈”将bug告诉我，或发邮件至beihuiguixian@hotmail.com 。提交bug的同时请同时奉上你的大名或者网名，我会将亲的名字列入软件测试者名单。<br>
版本更新：<br>
-------------
* 2017.01.15 版本更新至1.361，新增git远程仓库备份功能，改进关键词列表上下文预测算法和动态调频算法（此为基于SWT库的Black的最终版，基于SWT库的Black将不再更新，新版Black将基于QT界面库开发）
* 2017.01.09 版本更新至1.25，改进预定义列表排序算法，支持动态调频
* 2017.01.05 版本更新至1.20，补全一些菜单，修复了所有发现的bug，增加检查更新功能（程序启动时会自动检查是否存在新版本）
* 2017.01.04 版本更新至1.14，增加调试模式，用以记录程序出错信息
* 2017.01.03 版本更新至1.12，增加中文人名、英文人名、意大利人名产生功能
* 2017.01.02 版本更新至1.01，修复一些可致程序退出的bug，增加全部字体列表，改进程序健壮性，重构某些代码
* 2016.12.25 版本更新至0.9.90 增加全屏写作视图，修复bug，性能改进，增加标题列表，中文分词引擎

软件特性：<br>
=======
* 支持导入/导出doc/docx/纯文本文件
* 支持对项目里的所有文件进行查找和替换，查找和替换支持正则表达式<br>
* 支持中文预定义列表，可对人名、短语以及专有词汇进行预定义，在编辑器中按下Alt+/快捷键可唤出预定义词汇选择框进行选择并插入正文，使用预定义功能时需先在项目里新建一个名为“预定义”的文件<br>
* 支持自动滚屏，可将正在编辑的行固定在屏幕中央（类似Scrivener程序中的打字机滚动），使用自动滚屏时需按下ctrl+shift+/组合键在文档末尾插入一些空行<br>
* 支持以Alt+J插入中文左双引号，Alt+K插入中文右双引号<br>
* 支持自动保存<br>
* 支持项目备份<br>
* 支持编辑文件及位置记忆，重新启动程序时会自动打开上次所编辑的文件，并将光标定位至上次编辑的位置<br>
* 支持全屏写作视图，专注写作不分心。全屏写作时将鼠标指针移至窗口右上角会直接退出程序，下次启动程序时会直接打开写作视图<br>
* 支持产生中文、英文以及意大利人名<br><br>


下载地址：<br>
------------------------
* github： https://github.com/piiiiq/Black/releases/download/v1.20/Black-1.20.zip
* 百度网盘: http://pan.baidu.com/s/1qY3zB5q 密码:teyj<br>

安装运行：<br>
--------------------
1. 下载压缩包
2. 解压缩
3. 运行程序根目录下的Black.exe或Blackc.exe启动程序

进阶使用：<br>
------------------
* 使用预定义列表：按下alt+/或tab键可打开预定义列表，选择预定义词汇插入文档，前提是项目中包含一个名为“预定义”的文档，如果没有请在项目中新建文件，命名为“预定义”，并在文件中输入需要预先定义的词汇（以换行符切分），每行定义一个词汇；另外，在编辑器中选中文本，按下回车键可将所选文本加入预定义列表（如果预定义列表中不存在该词汇），如果预定义列表中存在该词汇，按下回车键则会将该词汇从预定义列表中删除
* 使用快速替换列表：快速替换列表可对文档内的词汇进行快速替换。用法是先在项目中为所要执行替换操作的文档新建一个替换文件，替换文件命名为“文档名称\_替换列表”（“文档名称”为所要执行替换操作的文档名），在替换文件中以“被替换词汇#替换词汇”格式输入替换内容，输入完成后保存替换文件，按下ctrl+9执行快速替换
* 使用自动滚屏，自动滚屏可将正在编辑的行固定在屏幕中央，便于作者集中注意力写作。用法是按下ctrl+shift+/键在文档末尾插入一些空行（插入的空行数量视你的屏幕高度而定），再次编辑内容时会自动滚屏

（更多进阶操作以后补全）

已知问题：<br>
-------------
* 如果出现输入法选词框不跟随光标的现象，请退出程序，运行程序根目录下的Blackc.exe程序再次启动程序并做尝试<br>
* 用QQ拼音输入法在程序中输入时可能会使程序崩溃，这是QQ拼音的问题<br>

出现崩溃时不要立刻重启程序，请先将程序根目录下的logs目录里的log.txt文件发给我，方便我排错。如果logs目录里的log.txt内容为空，请检查程序根目录下是否存在包含log字样的文本文件，如果存在请将其发给我。<br>
<br>
---------------软件截图--------------<br>
普通视图：
![image](https://github.com/piiiiq/Black/blob/master/images/new.PNG)
窗口写作视图：
![image](https://github.com/piiiiq/Black/blob/master/images/11.png)
全屏写作视图：
![image](https://github.com/piiiiq/Black/blob/master/images/1.PNG)
预定义列表：
![image](https://github.com/piiiiq/Black/blob/master/images/2.PNG)
快键菜单：
![image](https://github.com/piiiiq/Black/blob/master/images/3.PNG)

![image](https://github.com/piiiiq/Black/blob/master/images/4.jpg)![image](https://github.com/piiiiq/Black/blob/master/images/5.jpg)
