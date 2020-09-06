### 此程序是开发者自己用来写小说草稿的软件，放上来供有相同需求的写作者使用。
#### 软件专注文学类草稿写作，提供了一些有针对性的功能：<br>
* 新建一个项目可以管理文件、文件集、文件夹和关键词列表等；
* 可以自动完成和补全经常输入的中文词汇；
* 为项目内所有的文档提供统一的排版，不支持为特定的字符更改格式（因为软件是用来写文学类草稿的，为文本设定格式没有意义），排版支持自定义字体、字号、行距、段距、边距等；
* 可以备份项目到本地储存器或网络位置（Git仓库）；
* 打字机卷动，把正在编辑行固定在屏幕中央；
* 字数统计（单文档和多文档）；
* 可以在文档中插入锚点，锚点可以理解为是不分级别的标题，用来快速移动到文档的不同部分；
* Alt+字母按键控制插入符移动，不需要移动右手到方向键区就能移动插入符；
* 记录编辑点，能够记忆文档编辑的位置，并快速跳转到历史编辑位置；
* 重启程序能够还原所有文档的最后的查看和编辑位置；
* 多种视图，有普通视图（显示所有界面元素），精简视图（显示少量界面元素），和写作视图（隐藏绝大部分界面元素）；
* 动画效果，编辑器有一些动画；
* 对中文输入法进行优化，删除某些嵌入式输入法提交的无意义预编辑字符串，为了提高性能，程序内建了编码框，可以在嵌入式输入方式和传统输入方式之间进行选择；
* 人名产生，自用、实验性功能，除了开发者自己不推荐其他人使用；
* 文字间距，可以一键开关文字间距，长时间编辑文档缓解眼睛疲劳；
* 对称类标点高亮，对括号、书名号、中文引号等对称类标点进行高亮，便于审阅内容；
*	更多功能参阅使用指南，或自行探索。

#### 关于自动完成
新版本的BlackDraft（v0.678）引入了精简的中文自动分词模块，基于神经网络训练而成的HanPL中文语言处理工具包。鉴于完整版的HanPL磁盘占用和内存占用都比较大（磁盘占用400MB+，仅用作分词用途加载后内存占用也超过了500MB，不适合打包到BlackDraft内，所以BlackDraft只内置了精简版的。完整版拥有全功能的神经语言程序学（NLP）命名实体识别模式和CRF新词识别模式，相比之下精简版的没了上面两项识别模式，对新词的识别能力有些不足，基本上只能依赖字典和算法来识别，不过聊胜于无，作为定义关键词以外的辅助自动完成的手段包含进BlackDraft也未尝不可。<br><br>
现在没有关键词列表或没有定义关键词也可以使用基于自动分词产生的关键词来自动完成一些词汇，当然精准度肯定比不上自己定义的关键词。<br><br>
虽然现在不必添加关键词列表也可以使用自动完成功能，但还是建议添加一个关键词列表（即便你并不打算定义关键词），因为如果存在与所编辑的文档相关联的关键词列表，每次选择由自动分词产生的关键词并插入到文档中后，BlackDraft会自动将插入文档的词汇保存到关键词列表内，以方便以后在同样与该关键词列表相关联的其他文档内（主要是空文档，因为空文档没有内容，所以无法使用自动分词来自动生成关键词）插入这些关键词，这样可以在编辑其他文档的时候还能方便的插入前面使用过的、由自动分词模块产生的关键词。<br><br>
###### 要想最好的使用BlackDraft的自动完成功能，必须使用支持嵌入式编辑（也被成为内联编辑）的拼音输入法输入文字，BlackDraft能够在用户使用嵌入式拼音输入法输入时自动获取用户输入的拼音，并与当前所有已经识别的关键词进行对比，将匹配度（模糊匹配，不必输入所有的拼音即可快速匹配）最高的关键词显示在屏幕的左下角，用户可以在使用输入法输入的过程中（未按下空格键或选词键提交输入法候选词之前），通过按下Shift+与右下角显示的关键词之前的英文字母序号，来选中对应的关键词，然后按下Enter键即可将所选的关键词插入到文中。BlackDraft目前支持使用全拼或微软双拼的嵌入式拼音输入法检索和自动完成词汇。<br><br>
###### 很多现代拼音输入法都支持嵌入式编辑模式，例如：微软拼音输入法；小狼毫Rime输入法；QQ输入法；谷歌输入法等。<br>

#### 软件更新
从0.668版本开始支持自动下载与部署更新，也支持增量更新，程序启动后会自动连接github检查和下载更新。<br>
国内的原因，访问github可能很慢，下载有时会失败，失败后点击“帮助”菜单->“检查更新”可重复尝试下载，更新下载支持断点续传，会基于已经下载的部分继续下载，下载失败多试几次即可。<br>
实在无法下载请自行从[Releases](https://github.com/piiiiq/BlackDraft/releases "下载页面") 页面下载增量更新包（更新包的大小目前在1MB以内），将更新包放到程序根目录下的update文件夹内（不要解压缩和重命名，更新包名字是更新的版本号加.zip，如果下载的zip文件名有错请自行更名，错误的名字会导致无法更新），重启程序即可自动升级，前提是你的程序已经是0.668或更高版本。
##### 推荐使用自动更新和增量更新来升级软件，以便缩小下载数据量和保留自定义配置。


#### 下载链接：
见[Releases](https://github.com/piiiiq/BlackDraft/releases "下载页面") 页面。
##### 下面是软件运行截图
###### <center>普通视图</center>
![image](https://s2.ax1x.com/2020/02/15/1z6l6A.md.png)<br>
###### <center>写作视图</center><br>
![image](https://s2.ax1x.com/2020/02/15/1z68mt.md.png)<br>
###### <center>精简视图</center><br>
![image](https://s2.ax1x.com/2020/02/15/1z6mFO.md.png)<br>
