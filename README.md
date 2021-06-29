# DialogUtilsApp
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
    implementation 'com.gitee.mjsoftking:DialogUtilsLib:1.0.2'
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


### 四、资源覆盖，改变颜色、字体大小、默认文字
 
- **colors下可覆盖资源及注释，默认黑色和白色不建议覆盖，前景色：字体的颜色，背景色：布局的背景色** 

```
<resources>
    <!--dialog的整体背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_bg">#FFFFFF</color>
    <!--dialog的内容文字的前景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_content_fg">#000000</color>
    <!--dialog的标题文字的前景色，适用于所有带标题的dialog，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_title_fg">#000000</color>
    <!--dialog的 确认 按钮文字的前景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_ok_fg">#2C9BF3</color>
    <!--dialog的 取消 按钮文字的前景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_cancel_fg">#000000</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_ok_bg">#FFFFFF</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_cancel_bg">#FFFFFF</color>
    <!--dialog的内容文字的前景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_split_line">#EAEAEA</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_button_split_bg">#EAEAEA</color>

    <!--dialog的整体背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_bg">#FFFFFF</color>
    <!--dialog的标题文字的前景色，适用于所有带标题的dialog，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_title_fg">#000000</color>
    <!--dialog的 确认 按钮文字的前景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_ok_fg">#2C9BF3</color>
    <!--dialog的 取消 按钮文字的前景色，适用于 DialogLibCustom-->
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
    <!--dialog的标题文字的前景色，适用于所有带标题的dialog，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_title_fg">#000000</color>
    <!--dialog的 确认 按钮文字的前景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_ok_fg">#2C9BF3</color>
    <!--dialog的 取消 按钮文字的前景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_cancel_fg">#000000</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_ok_bg">#FFFFFF</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_cancel_bg">#FFFFFF</color>
    <!--dialog的输入框文字的前景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_fg">#000000</color>
    <!--dialog的输入框下方分割线的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_split_line">#EAEAEA</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_button_split_bg">#EAEAEA</color>

    <!--dialog的加载框加载等待区域的背景色，适用于 DialogLibLoading-->
    <color name="dialog_utils_lib_loading_content_bg">#FFC4C4C4</color>
    <!--dialog的加载框加载等待区域文字提示的前景色，适用于 DialogLibLoading-->
    <color name="dialog_utils_lib_loading_content_text_fg">#FFFFFF</color>
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


    <!--dialog 宽度占屏幕宽度的百分比，取值0-1之间，不包含边界，竖屏时的系数，统一设定-->
    <item name="dialog_utils_lib_portrait_width_factor" format="float" type="dimen">0.85</item>
    <!--dialog 宽度占屏幕宽度的百分比，取值0-1之间，不包含边界，横屏时的系数，统一设定-->
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
- **mipmap下资源定义，注意：此2张图片为密码输入时显示/隐藏按钮的图片，png格式** 

![输入图片说明](https://images.gitee.com/uploads/images/2021/0429/144351_bd3a5ff2_1021361.png "屏幕截图.png")
```
dialog_utils_lib_password_hide 隐藏图片命名
dialog_utils_lib_password_show 显示图片命名
```

### 五、预览
-  **普通对话框** 
![普通对话框](https://images.gitee.com/uploads/images/2021/0421/154236_fa7889fe_1021361.png "1.png")
-  **自定义对话框** 
![自定义对话框](https://images.gitee.com/uploads/images/2021/0421/154252_0da16cb5_1021361.png "2.png")
-  **完全自定义对话框** 
![完全自定义对话框](https://images.gitee.com/uploads/images/2021/0421/154300_1d122592_1021361.png "3.png")
-  **输入对话框** 
![输入对话框](https://images.gitee.com/uploads/images/2021/0421/154309_67c5cf0f_1021361.png "4.png")
-  **等待对话框** 
![等待对话框](https://images.gitee.com/uploads/images/2021/0421/154318_cb3811ea_1021361.png "5.png"))
-  **密码输入显示密码时对话框** 
![输入图片说明](https://images.gitee.com/uploads/images/2021/0429/144540_599285ea_1021361.png "显示.png")
-  **密码输入隐藏密码时对话框** 
![输入图片说明](https://images.gitee.com/uploads/images/2021/0429/144608_874ecbca_1021361.png "隐藏.png")


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

