这是开发者自己用来写小说的软件，放上来供有相同需求的人使用。

 <h4>这个软件包含如下的功能：</h4>
<li> 建立一个项目以便管理小说所有草稿文件
<li> 对所有文稿进行统一的排版，可以自定义行距、段落距离、字体、字号、缩进等等
<li> 提供中文关键词自动补全功能，可以自定义关键词，或者使用分词模块分词识别
<li> 打字机卷动，可将编辑的行固定在程序中央
<li> 启动后可以还原所有文档上一次的编辑查看位置
<li> 多种写作界面可供选择，写作视图界面可以自定义背景、颜色，环境光影效果等，可以使用bing每日图片、随机动态网络图片作为背景等
<li> 除了方向键还可利用alt+j/k/l/i/h/f移动光标、删除或换行，不用移动右手也可以快速编辑文档
<li> 可以调整文字间距，便于阅读
<li> 基本的人名产生
<li> 多文档字数统计
<li> 可以对软件设定访问密码
<li> ....
 <br><br>
![屏幕截图](./Screenshot/1.PNG)
 
 <h4>使用方法</h4>
 从release页面下载压缩包，解压缩后运行BlackDraft.exe即可。从0.6951版本开始只支持64位windows系统。程序在windows 8上开发与测试，在win10及以上系统可能存在bug，理论上基本兼容win7及更高版本的系统。0.6951以下的版本可在32/64位的windows XP到windows 11中运行。
<h4>开发说明</h4>
用word写小说总有些不如意的地方，找了一圈软件，没一个软件用着顺手，包括那些付费的，所以决定自己写一个。断断续续写了很多年，边写边用，发现忍不了的bug才会修一修。
 <br><br>
此软件使用Java语言编写，基于Qt（一个C++跨平台框架）构建，因为使用Java+Qt编写，发布软件时需携带Java与Qt两者的执行库才能正常运行，Java库压缩后约60MB大小，Qt库压缩后约20MB大小。
<br><br>
<h4>一些常用但没有提供用户界面调用的功能</h4>
<li> alt+1备份到本地储存器，例如U盘。更多本地备份功能查看备份命令
<li> alt/ctrl+4切换使用鼠标或键盘改写标点方式，此功能可快速在中文逗号与句号之间互改
<li> alt+5启用或停用拼音检索关键词功能，此功能开启时可利用支持嵌入式（也成为内联）编辑的拼音输入法在输入中进行中文关键词自动补全，当前支持全拼和微软双拼键入时检索
<li> alt+；（分号键）快速在当前编辑位置、段首、段尾之间跳转
<li> alt+2切换白天或夜间配色(写作视图下有效)
<li> ctrl+enter显示历史编辑文件列表，点选了跳转到对应的文档
 <li> alt+enter显示当前文档内的所有标题。程序将结尾没有标点的段落视为一个标题，点选可跳转到对应的段落
  <li> ctrl+q可在上一个和当前编辑的文件之间来回切换
  <li> ctrl+Tab可在已经打开的文档间切换
   <li> 写作视图中鼠标移动到屏幕最左边可显示文件娄
    <li> 写作视图中鼠标移动到屏幕最右边可显示滚动条
     <li> ctrl+k统计当前文档字符数
    <li> 任何视图中按下shift+esc便可关闭程序，如果从写作视图中直接关闭程序，下次启动时将直接进入写作视图
  
<li> 在搜索框中输入“$bc 设置颜色 h”（不含引号）可查看设置颜色这个命令的用法
<li> 在搜索框中输入“$bc 随机图片 0”（不含引号）可关闭随机图片背景
<li> 暂时只想到上面这些，以后再补充……
<br>命令通常在搜索框中输入，在普通视图右上角的搜索框或按下Ctrl+f弹出的搜索框都可。输入“$bc help”（不含引号）命令可查看程序当前版本支持的全部命令及其用法
 <br><br>
<h4>此软件使用了如下的开源模块/库</h4>
QTJAMBI5 - 许可证不详(github.com/OmixVisualization/qtjambi)<br>
QTJAMBI4 - LGPL-3许可证(sourceforge.net/projects/qtjambi)<br>
HanLP - Apache-2许可证(github.com/hankcs/HanLP/)<br>
JGIT - EDL许可证(www.eclipse.org/jgit/)<br>
PINYIN4J - BSD许可证(pinyin4j.sourceforge.net)<br>
WINRUN4J - CPL许可证(github.com/poidasmith/winrun4j)<br>
感谢这些软件库的开发者，没有这些软件就没有BlackDraft。
