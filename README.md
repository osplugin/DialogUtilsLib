# DialogUtilsApp
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![](https://jitpack.io/v/com.gitee.mjsoftking/DialogUtilsLib.svg)](https://jitpack.io/#com.gitee.mjsoftking/DialogUtilsLib)

### 一、介绍
替换系统dialog风格后的通用提示框工具类，可以覆盖lib下的定义资源，改变现有的颜色风格，需要改变布局风格，可参考文档覆盖属性

### 二、工程引入工具包准备
**工程的build.gradle文件添加** 

```
allprojects {
    repositories {
        google()
        mavenCentral()

        //jitpack 仓库
        maven { url 'https://jitpack.io' }
    }
}
```

**APP的build.gradle文件添加** 
```
dependencies {
    ...
    implementation 'com.gitee.mjsoftking:DialogUtilsLib:1.0.6'
    implementation 'com.google.android.material:material:1.2.1'
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
            .setOnBtnMessage(()->{
                //描述区域点击时触发
            })
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
-  **密码输入型dialog** 

```
    DialogLibInput.create(this)
        .setMessage("123")
        .setLength(6)
        .setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
        .setAlias("text6")
        //设置显示密码隐藏/显示图片
        .setShowLookPassword()
        //自动弹出键盘
        .setPopupKeyboard()
        .setOnBtnOk(str -> {
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            return true;
        })
        .show();
```
-  **Snackbar提示框**

```
    SnackBarLib.makeCustom(binding.text7,
        "Snackbar提示框", 10 * 1000)
        .setContentColor(R.color.white)
        .setAction("试试", v1 ->
              Toast.makeText(getApplicationContext(), "action的点击事件", Toast.LENGTH_SHORT).show())
        .setActionClickCallback(tag -> {
          Toast.makeText(getApplicationContext(), "由点击action触发关闭", Toast.LENGTH_SHORT).show();
        })
        .showSuccess();
```

### 四、资源覆盖，改变颜色、字体大小、默认文字
 
- **colors下可覆盖资源及注释**

```
<resources>

    <!--dialog的整体背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_bg">#FFFFFF</color>
    <!--dialog的内容文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_content_fg">#000000</color>
    <!--dialog的标题文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_title_fg">#000000</color>
    <!--dialog的 确认 按钮文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_ok_fg">#2C9BF3</color>
    <!--dialog的 取消 按钮文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_cancel_fg">#000000</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_ok_bg">#FFFFFF</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_cancel_bg">#FFFFFF</color>
    <!--dialog的内容文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_split_line">#EAEAEA</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_button_split_bg">#EAEAEA</color>


    <!--dialog的整体背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_bg">#FFFFFF</color>
    <!--dialog的标题文字的文字颜色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_title_fg">#000000</color>
    <!--dialog的 确认 按钮文字的文字颜色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_ok_fg">#2C9BF3</color>
    <!--dialog的 取消 按钮文字的文字颜色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_cancel_fg">#000000</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_ok_bg">#FFFFFF</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_cancel_bg">#FFFFFF</color>
    <!--dialog的输入框标题与内容和内容与按钮分割线的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_split_line">#EAEAEA</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_button_split_bg">#EAEAEA</color>


    <!--dialog的整体背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_bg">#FFFFFF</color>
    <!--dialog的标题文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_title_fg">#000000</color>
    <!--dialog的 确认 按钮文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_ok_fg">#2C9BF3</color>
    <!--dialog的 取消 按钮文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_cancel_fg">#000000</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_ok_bg">#FFFFFF</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_cancel_bg">#FFFFFF</color>
    <!--dialog的输入框文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_fg">#000000</color>
    <!--dialog的输入框下方分割线的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_split_line">#EAEAEA</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_button_split_bg">#EAEAEA</color>


    <!--dialog的加载框加载等待区域的背景色，适用于 DialogLibLoading-->
    <color name="dialog_utils_lib_loading_content_bg">#FFC4C4C4</color>
    <!--dialog的加载框加载等待区域文字提示的文字颜色，适用于 DialogLibLoading-->
    <color name="dialog_utils_lib_loading_content_text_fg">#FFFFFF</color>


    <!--snackbar的背景色，适用于 SnackBarLib-->
    <color name="snackbar_lib_bg">#C4C4C4</color>
    <!--snackbar的文本显示区域前文字颜色，适用于 SnackBarLib-->
    <color name="snackbar_lib_content_fg">#FFFFFF</color>
    <!--snackbar的action显示区域文字颜色，适用于 SnackBarLib-->
    <color name="snackbar_lib_action_fg">#2C9BF3</color>

</resources>
```
-  **dimens下字体大小资源** 

```
<resources>

    <!--标题字体大小，适用于 DialogLibCommon-->
    <dimen name="dialog_utils_lib_common_title_text_size">14sp</dimen>
    <!--确定 字体大小，适用于 DialogLibCommon-->
    <dimen name="dialog_utils_lib_common_ok_text_size">14sp</dimen>
    <!--取消 字体大小，，适用于 DialogLibCommon-->
    <dimen name="dialog_utils_lib_common_cancel_text_size">14sp</dimen>
    <!--内容 字体大小，适用于 DialogLibCommon-->
    <dimen name="dialog_utils_lib_common_content_text_size">14sp</dimen>


    <!--标题字体大小，适用于 DialogLibInput-->
    <dimen name="dialog_utils_lib_input_title_text_size">14sp</dimen>
    <!--确定 字体大小，适用于 DialogLibInput-->
    <dimen name="dialog_utils_lib_input_ok_text_size">14sp</dimen>
    <!--取消 字体大小，适用于 DialogLibInput-->
    <dimen name="dialog_utils_lib_input_cancel_text_size">14sp</dimen>
    <!--输入框 字体大小，适用于 DialogLibInput-->
    <dimen name="dialog_utils_lib_input_text_size">14sp</dimen>


    <!--标题字体大小，适用于 DialogLibCustom-->
    <dimen name="dialog_utils_lib_custom_title_text_size">14sp</dimen>
    <!--确定 字体大小，适用于 DialogLibCustom-->
    <dimen name="dialog_utils_lib_custom_ok_text_size">14sp</dimen>
    <!--取消 字体大小，适用于 DialogLibCustom-->
    <dimen name="dialog_utils_lib_custom_cancel_text_size">14sp</dimen>


    <!--加载框 字体大小，适用于 DialogLibLoading 提示内容区域-->
    <dimen name="dialog_utils_lib_loading_text_size">14sp</dimen>


    <!--snackbar的文本 字体大小，适用于 SnackBarLib-->
    <dimen name="snackbar_lib_content_size">14sp</dimen>
    <!--snackbar的 action 字体大小，适用于 SnackBarLib-->
    <dimen name="snackbar_lib_action_size">14sp</dimen>
    <!--snackbar的 image 的宽高，正方形，适用于 SnackBarLib-->
    <dimen name="snackbar_lib_img_w_h">30dp</dimen>
    <!--snackbar的 内容单行时 的 top和bottom的内边距，修改系统默认的超大边距，适用于 SnackBarLib-->
    <dimen name="design_snackbar_padding_vertical">10dp</dimen>
    <!--snackbar的 内容多行时 的 top和bottom的内边距，修改系统默认的超大边距，适用于 SnackBarLib-->
    <dimen name="design_snackbar_padding_vertical_2lines">10dp</dimen>


    <!--dialog 宽度占屏幕宽度的百分比，取值0-1之间，不包含边界，竖屏时的系数，所有类型统一设定-->
    <item name="dialog_utils_lib_portrait_width_factor" format="float" type="dimen">0.85</item>
    <!--dialog 宽度占屏幕宽度的百分比，取值0-1之间，不包含边界，横屏时的系数，所有类型统一设定-->
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
- **mipmap下资源定义，png格式，可覆盖**

```
dialog_utils_lib_password_hide 隐藏图片命名
dialog_utils_lib_password_show 显示图片命名
```
```
snackbar_lib_default_success snackbar通过的默认图标
snackbar_lib_default_error snackbar错误的默认图标
snackbar_lib_default_info snackbar信息的默认图标
snackbar_lib_default_warn snackbar警告的默认图标
```

- **integers下资源定义，可覆盖**

```
<resources>

    <!--snackbar的文本显示区域的最大行数，超出时末尾自动显示...，适用于 SnackBarLib-->
    <integer name="design_snackbar_text_max_lines">2</integer>
</resources>
```

### 五、预览
-  **普通对话框** 
![普通对话框](https://images.gitee.com/uploads/images/2021/0629/164213_9646f711_1021361.png "普通.png")
-  **自定义对话框** 
![自定义对话框](https://images.gitee.com/uploads/images/2021/0629/164235_a87a557f_1021361.png "自定义.png")
-  **完全自定义对话框** 
![完全自定义对话框](https://images.gitee.com/uploads/images/2021/0629/164251_3d768863_1021361.png "完全自定义.png")
-  **输入对话框** 
![输入对话框](https://images.gitee.com/uploads/images/2021/0629/164307_101e5302_1021361.png "输入.png")
-  **等待对话框** 
![等待对话框](https://images.gitee.com/uploads/images/2021/0629/164333_93867347_1021361.png "等待.png")
-  **密码输入显示密码时对话框** 
![密码输入显示密码时对话框](https://images.gitee.com/uploads/images/2021/0629/164348_abd94487_1021361.png "密码显示.png")
-  **密码输入隐藏密码时对话框** 
![密码输入隐藏密码时对话框](https://images.gitee.com/uploads/images/2021/0629/164405_96b990fb_1021361.png "密码隐藏.png")
-  **Snackbar提示框**
![Snackbar提示框](https://images.gitee.com/uploads/images/2021/0719/150802_8b4318b8_1021361.png "Snackbar提示框.png")


License
-------

    Copyright 2021 mjsoftking

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

