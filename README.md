# DialogUtilsApp

#### 介绍
替换系统dialog风格后的通用提示框工具类，可以覆盖lib下的定义资源，改变现有的颜色风格，需要改变布局风格，可下载项目后自行调整

#### 属性覆盖，改变颜色风格
在lib下可以找到 colors、strings、dimens 定义的资源，在引用aar的主项目下在对应位置覆盖这些资源便可以改变部分风格

#### 标签设置
提供的dialog工具类均已支持标签设置，只要标签不为null或空字符串时，当一个activity关闭时，可以使用DialogLibCommonUtils.sendCloseEvent(this, "tag");关闭所有同类型的同标签的对话框而不引发窗体泄漏

#### 别名设置
提供的dialog工具类均已支持别名设置，只要别名不为null或空字符串时，可以使同一类型对话框同一别名的在同一时间只显示一个，同时第二次以后的show方法均会返回正在显示的对话框对象而不是新的对象。

#### 等待框支持显示前回调
DialogLibLoadingUtils 增加show前执行的回调方法，现在可以先创建DialogLibLoadingUtils的对话框后依据回调开始操作了，同时配合别名设置可以保证任务只会调用一次

