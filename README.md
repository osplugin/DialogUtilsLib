# DialogUtilsApp

### 一、介绍
替换系统dialog风格后的通用提示框工具类，可以覆盖lib下的定义资源，改变现有的颜色风格，需要改变布局风格，可下载项目后自行调整
- APP 使用示例项目，libs下含有已编译最新的aar资源。
- dialogutilslib arr资源项目，需要引入的资源包项目。
- aar文件生成，在工具栏直接Gradle - (项目名) - dialogutilslib - Tasks - build - assemble，直到编译完成
- aar文件位置，打开项目所在文件夹，找到 dialogutilslib\build\outputs\aar 下。

### 二、工程引入工具包准备
下载项目，可以在APP项目的libs文件下找到DialogUtilsLib.aar文件（已编译为最新版），引入自己的工程
引用时需要在android标签下加入，此处设置libs路径需要根据项目结构确定位置。

```
android {
     repositories {
        flatDir {
            dirs 'libs'
        }
    }
    ...
}
```
引入aar

```
dependencies {
   implementation(name: 'DialogUtilsLib', ext: 'aar')
   ...
}
```
### 三、使用

注意下方只做了基础展示，dialog的都会返回对应的utils对象，registerActivityLifecycleCallbacks方法设置后，activity销毁时会自动把显示在此activity上的dialog一起关闭。

-  **application初始化设置** 

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化dialog工具类设置
        DialogLibInitSetting.getInstance()
                //设置debug
                .setDebug(BuildConfig.DEBUG)
                //注册全局activity生命周期监听
                .registerActivityLifecycleCallbacks(this);

    }
}
```


-  **普通dialog** 

```
            DialogLibCommon.create(this)
                    .setMessage("普通对话框1")
                    .setAlias("text1")
                    .noShowCancel()
                    .show();
```

-  **自定义dialog** 

```
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            DialogLibCustom.create(this)
                    .noShowCancel()
                    .setAlias("text2")
                    .show(imageView);
```

-  **输入型dialog** 

```
            DialogLibInput.create(this)
                    .setMessage("输入信息")
                    .setAlias("text3")
                    //自动弹出键盘
                    .setPopupKeyboard()
                    .setOnBtnOk(str -> {
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        return true;
                    })
                    .show();
```

-  **等待型dialog** 

```
            DialogLibLoading.create(this)
                    .setTimeoutClose(2000)
                    .setAlias("text4")
                    .setOnLoading(() -> {
                        Toast.makeText(MainActivity.this, "我是显示对话框前触发的", Toast.LENGTH_SHORT).show();
                    })
                    .show();
```

-  **完全自定义型dialog** 
```
            final DialogLibAllCustom dialog = DialogLibAllCustom.create(this)
                    .setCancelable(true)
                    .setAlias("text5");

            TextView view = new TextView(this);
            view.setBackgroundResource(R.color.design_default_color_secondary);
            view.setText("这是一个完全自定义布局的对话框，对话框显示后需要手动关闭");
            view.setOnClickListener(v2 -> {
                dialog.closeDialog();
            });

            dialog.show(view);
```

### 四、资源覆盖，改变颜色、字体大小、默认文字
 
- **colors下可覆盖资源及注释，默认黑色和白色不建议覆盖，前景色：字体的颜色，背景色：布局的背景色** 

```
<resources>
    <!--黑色-->
    <color name="dialog_utils_lib_black">#FF000000</color>
    <!--白色-->
    <color name="dialog_utils_lib_white">#FFFFFFFF</color>

    <!--dialog的标题文字的前景色，适用于所有带标题的dialog-->
    <color name="dialog_utils_lib_title_fg">@color/dialog_utils_lib_black</color>
    <!--dialog的 确认 按钮文字的前景色-->
    <color name="dialog_utils_lib_ok_fg">@color/dialog_utils_lib_white</color>
    <!--dialog的 取消 按钮文字的前景色-->
    <color name="dialog_utils_lib_cancel_fg">@color/dialog_utils_lib_white</color>
    <!--dialog的 确认 按钮文字的背景色-->
    <color name="dialog_utils_lib_ok_bg">#22C5A3</color>
    <!--dialog的 取消 按钮文字的背景色-->
    <color name="dialog_utils_lib_cancel_bg">#F8A01A</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色-->
    <color name="dialog_utils_lib_button_split_bg">@color/dialog_utils_lib_white</color>

    <!--dialog的内容文字的前景色，适用于 DialogLibCommonUtils-->
    <color name="dialog_utils_lib_content_fg">@color/dialog_utils_lib_black</color>

    <!--dialog的输入框文字的前景色，适用于 DialogLibInputUtils-->
    <color name="dialog_utils_lib_input_fg">@color/dialog_utils_lib_black</color>
    <!--dialog的输入框下方分割线的背景色，适用于 DialogLibInputUtils-->
    <color name="dialog_utils_lib_input_split_line">@color/dialog_utils_lib_ok_bg</color>

    <!--dialog的加载框加载等待区域的背景色-->
    <color name="dialog_utils_lib_loading_content_bg">#FFc4c4c4</color>
    <!--dialog的加载框加载等待区域文字提示的前景色-->
    <color name="dialog_utils_lib_loading_content_text_fg">@color/dialog_utils_lib_white</color>
</resources>
```
-  **dimens下字体大小资源** 

```
<resources>
    <dimen name="dialog_utils_lib_text_size_normal">14sp</dimen>

    <!--标题字体大小，统一设定-->
    <dimen name="dialog_utils_lib_title_text_size">@dimen/dialog_utils_lib_text_size_normal</dimen>
    <!--确定 字体大小，统一设定-->
    <dimen name="dialog_utils_lib_ok_text_size">@dimen/dialog_utils_lib_text_size_normal</dimen>
    <!--取消 字体大小，统一设定-->
    <dimen name="dialog_utils_lib_cancel_text_size">@dimen/dialog_utils_lib_text_size_normal</dimen>
    <!--内容 字体大小，适用于 DialogLibCommonUtils的提示内容区域-->
    <dimen name="dialog_utils_lib_content_text_size">@dimen/dialog_utils_lib_text_size_normal</dimen>
    <!--输入框 字体大小，适用于 DialogLibInputUtils 输入区域-->
    <dimen name="dialog_utils_lib_input_text_size">@dimen/dialog_utils_lib_text_size_normal</dimen>
    <!--加载框 字体大小，适用于 DialogLibLoadingUtils 提示内容区域-->
    <dimen name="dialog_utils_lib_loading_text_size">@dimen/dialog_utils_lib_text_size_normal</dimen>

    <!--dialog 宽度占屏幕宽度的百分比，取值0-1之间，不包含边界，竖屏时的系数-->
    <item name="dialog_utils_lib_portrait_width_factor" format="float" type="dimen">0.85</item>
    <!--dialog 宽度占屏幕宽度的百分比，取值0-1之间，不包含边界，横屏时的系数-->
    <item name="dialog_utils_lib_landscape_width_factor" format="float" type="dimen">0.5</item>
</resources>
```
- **strings下资源定义，注意：如果你的项目存在多语言，则必须覆盖** 

```
<resources>
    <string name="dialog_utils_lib_ok">确定</string>
    <string name="dialog_utils_lib_cancel">取消</string>
    <string name="dialog_utils_lib_default_title">提示</string>
    <string name="dialog_utils_lib_data_processing">数据处理中…</string>
</resources>
```

### 五、预览
- 普通对话框
![普通对话框](https://images.gitee.com/uploads/images/2021/0421/154236_fa7889fe_1021361.png "1.png")
- 自定义对话框
![自定义对话框](https://images.gitee.com/uploads/images/2021/0421/154252_0da16cb5_1021361.png "2.png")
- 完全自定义对话框
![完全自定义对话框](https://images.gitee.com/uploads/images/2021/0421/154300_1d122592_1021361.png "3.png")
- 输入对话框
![输入对话框](https://images.gitee.com/uploads/images/2021/0421/154309_67c5cf0f_1021361.png "4.png")
- 等待对话框
![等待对话框](https://images.gitee.com/uploads/images/2021/0421/154318_cb3811ea_1021361.png "5.png"))
