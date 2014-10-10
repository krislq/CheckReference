CheckReference
==============

检查Android项目中多余的，重复的一些引用

#背景：
* 随着项目的发展，界面、文件、图片等信息不停的进行着增、删、改等操作。随着时间的推移，很多索引已经没有被引用了，但是如果人肉做的话，工作量蛮大的。
所以，写了这样一个小工具。

#功能：
* 找出项目中没有被引用的layout文件。
* 找出项目中复杂的String的索引（在windows的eclipse中，如果有两个相同的String申明，会自动最后一个，而不会报错）。
* 找出项目中没有被引用的String的索引。
* 找出项目中没有被引用的drawable的过引。

#使用
* 克隆项目
* 导入项目
* 修改PROJECT_PATH变量为你想检查的Android项目路径
* 运行即可

#说明

* [Layout - NOREFER] 没有被引用的布局文件
* [String - NOREFER] 没有被引用的String
* [String - REPEAT] 重复的String
* [Drawable - NOREFER] 没有被引用的素材文件
* [Drawable - REPEAT] 重复的的素材文件

