 这是开发者自己用来写小说的软件，放上来供有相同需求的人使用。<br><br>
 此软件使用Java语言，基于Qt（一个C++跨平台组建库）构建，使用Qtjambi库作为桥，通过Java调用Qt库。<br>
 因为旧版本所使用的Qtjambi4存在很多bug，且开发者已经停止维护，所以此次升级底层库到了Qtjambi5，对应的Qt版本为Qt5.15，并以64位版本构建。新版本修复了一些bug，添加了一些无聊的功能。
<br> <br>
用word写小说总有些不如意的地方，找了一圈软件，没一个软件用着顺手，包括那些付费的，所以决定自己写一个。断断续续写了很多年，边写边用，发现忍不了的bug才会修一修。
<br>这个软件有很多小bug，所谓的小bug是那些我认为基本不会丢失写作数据的bug。一个功能做出来我只是大体测试一下，精力所限小bug懒得修理，我经常使用的功能的bug率很低，有些bug我已经发现很久了，但懒得修，能凑合用就不修。
 <br><br>
 大体上这个软件包含如下的功能：<br>
 管理小说草稿；<br>
 对所有文稿进行统一的排版，可以自定义行距、段落距离、字体、字号、缩进等等；<br>
 提供中文关键词自动补全功能，可以自定义关键词，或者使用分词模块分词识别；<br>
 打字机卷动，可将编辑的行固定在程序中央；<br>
 启动后可以还原所有文档上一次的编辑查看位置；<br>
 多种写作界面可供选择，写作视图界面可以自定义背景颜色，可以使用动态网络图片作为背景等；<br>
 除了方向键还可利用alt+jklihf移动光标、删除或换行，不用移动右手也可以快速编辑文档；<br>
 可以调整文字间距，便于阅读；<br>
 基本的人名产生;<br>
 多文档字数统计；<br>
 一些我无聊时写出来的界面特效,如环境光影、动态背景图片等等；<br>
 <br>
 其他功能懒得写了，自己玩去吧。<br>
 
 <br>
 基于新底层库的版本删去了旧版本中的某些功能，例如光标动画、长光标等，原因是程序中基于Qt4实现的一些功能在Qt5上不能正常工作。
 
 
 <br><br>
此软件使用了如下的开源模块/库：<br>
QTJAMBI5 - 许可证不详(https://github.com/OmixVisualization/qtjambi)<br>
QTJAMBI4 - LGPL-3许可证(sourceforge.net/projects/qtjambi)<br>
HanLP - Apache-2许可证(github.com/hankcs/HanLP/)<br>
JGIT - EDL许可证(www.eclipse.org/jgit/)<br>
PINYIN4J - BSD许可证(pinyin4j.sourceforge.net)<br>
WINRUN4J - CPL许可证(github.com/poidasmith/winrun4j)<br>
感谢这些软件库的开发者，没有这些软件就没有BlackDraft。
