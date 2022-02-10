# DialogUtilsApp
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![](https://jitpack.io/v/com.gitee.osard/DialogUtilsLib.svg)](https://jitpack.io/#com.gitee.osard/DialogUtilsLib)

### 更新记录

#### 1.2.4版本
- DialogLibCommon、DialogLibCustom、DialogLibInput支持单次更改按钮翻转，设置后无视全局设置，以当前翻转设置为准

#### 1.2.3版本
- 对比1.2.2版本修改设置反转按钮时需要传递boolean变量，适配某些需要动态左右手切换的项目，设置后的下次对话框创建时生效。

#### 1.2.2版本
- 对话框设置增加单独设置横竖屏宽度比，具体看示例。
- 设置按钮反转方法移除传递boolean变量。
- gradle升级到7.0.4。
- 重新优化继承关系，接口回调未做调整，对比先前版本并无调用时的代码变化。
- DialogLibCommon、DialogLibCustom、DialogLibInput这3种对话框的统一布局设定新增设置反转按钮位置，原按钮位置为“左确定，右取消”（默认），设置反转后为“左取消，右确定”。 
- 反转后操作与原操作完全一致，无需额外代码设置。
- 支持设置翻转屏幕不重建activity时，dialog跟随设置的横竖屏宽度比自动改变宽度大小（除SnackBarLib外全部）。
- 若未设置翻转屏幕不重建activity且注册了全局activity生命周期，则翻转屏幕将自动关闭已打开的dialog（除SnackBarLib外全部）。
- 支持设置因activity生命周期结束而关闭对话框时，触发回调监听（除SnackBarLib外全部）。
- 其他错误修正。

### 一、介绍
替换系统dialog风格后的通用提示框工具类，可以覆盖lib下的定义资源，改变现有的颜色风格，需要改变布局风格，可参考文档覆盖属性

### 二、工程引入工具包准备
**com.android.tools.build:gradle:4.2.2及以下版本，在工程的 build.gradle 文件添加** 

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

**com.android.tools.build:gradle:7.0.0及以上版本，在工程的 settings.gradle 文件添加**

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        //jitpack 仓库
        maven {
            url 'https://jitpack.io'
        }
    }
}
```

**APP的build.gradle文件添加** 
```
dependencies {
    ...
    
    def dialog_version = 'x.x.x' //使用最新版本
    implementation "com.gitee.osard:DialogUtilsLib:$dialog_version"
    implementation 'com.google.android.material:material:1.3.0'
}
```
### 三、使用

注意下方只做了基础展示，dialog的都会返回对应的utils对象，registerActivityLifecycleCallbacks方法设置后，activity销毁时会自动把显示在此activity上的dialog一起关闭。

-  **application初始化设置** 

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化dialog工具类设置
        DialogLibInitSetting.getInstance()
                //设置debug
                .setDebug(BuildConfig.DEBUG)
                //设置是否反转确定和取消按钮位置，默认：左确定，右取消；设置后：左取消，右确定
                .setReverseButton(true)
                //注册全局activity生命周期监听
                .registerActivityLifecycleCallbacks(this);

    }

    // **application** 下的此方法进行注册，且 **activity** 设置 **android:configChanges="orientation|keyboardHidden|screenSize"** 时，
    // 屏幕翻转不会销毁重建 **activity** ，注册的此方法，将会根据设置的横竖屏宽度比自动改变dialog的宽度大小。
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        DialogLibInitSetting.getInstance().onScreenRotation(newConfig);

    }
}
```

-  **普通dialog** 

```java
    DialogLibCommon.create(this)
        .setMessage("普通对话框1")
        .setAlias("text1")
        .setPortraitWidthFactor(0.85F)
        .setLandscapeWidthFactor(0.5F)
        .setOnBtnMessage(() -> {
        //描述区域点击时触发
            Toast.makeText(MainActivity.this, "点击了消息区域", Toast.LENGTH_SHORT).show();
        })
        .setOnBtnOk(() -> {
            Toast.makeText(MainActivity.this, "点击了确定按钮", Toast.LENGTH_SHORT).show();
        })
        .setOnBtnCancel(() -> {
            Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
        })
        .setOnActivityLifecycleClose(() -> {
            Toast.makeText(MainActivity.this, "activity销毁而关闭", Toast.LENGTH_SHORT).show();
        })
        .show();
```

-  **自定义dialog** 

```java
    ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        DialogLibCustom.create(this)
        .setPortraitWidthFactor(0.85F)
        .setLandscapeWidthFactor(0.5F)
        .setOnCustomBtnOk(() -> {
            Toast.makeText(MainActivity.this, "点击了确定按钮", Toast.LENGTH_SHORT).show();
            return true;
        })
        .setOnBtnCancel(() -> {
            Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
        })
        .setOnActivityLifecycleClose(() -> {
            Toast.makeText(MainActivity.this, "activity销毁而关闭", Toast.LENGTH_SHORT).show();
        })
        .setAlias("text2")
        .show(imageView);
```

-  **输入型dialog** 

```java
    DialogLibInput.create(this)
        .setMessage("输入信息")
        .setAlias("text3")
        .setPortraitWidthFactor(0.85F)
        .setLandscapeWidthFactor(0.5F)
        //todo 设置显示密码隐藏/显示图片，由于输入类型限制不是密码，此处设置无效
        .setShowLookPassword()
        //自动弹出键盘
        .setPopupKeyboard()
        .setOnBtnCancel(() -> {
            Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
        })
        .setOnBtnOk(str -> {
            Toast.makeText(MainActivity.this, "输入消息为：" + str, Toast.LENGTH_SHORT).show();
            return true;
        })
        .setOnActivityLifecycleClose(() -> {
            Toast.makeText(MainActivity.this, "activity销毁而关闭", Toast.LENGTH_SHORT).show();
        })
        .show();
```

-  **等待型dialog** 

```java
    DialogLibLoading.create(this)
        //自动关闭时间，如需手动关闭，则保存此对象，在需要时调用 **dialogLibLoading.closeDialog()** 
        .setTimeoutClose(2000)
        .setAlias("text4")
        .setOnLoading(() -> {
            Toast.makeText(MainActivity.this, "我是显示对话框前触发的", Toast.LENGTH_SHORT).show();
        })
        .setOnActivityLifecycleClose(() -> {
            Toast.makeText(MainActivity.this, "activity销毁而关闭", Toast.LENGTH_SHORT).show();
        })
        .show();
```

-  **完全自定义型dialog** 
```java
    final DialogLibAllCustom dialog = DialogLibAllCustom.create(this)
        .setCancelable(true)
        .setPortraitWidthFactor(0.85F)
        .setLandscapeWidthFactor(0.5F)
        .setAlias("text5");

        TextView view = new TextView(this);
        view.setBackgroundResource(R.color.purple_500);
        view.setTextColor(getResources().getColor(R.color.white));
        view.setText("这是一个完全自定义布局的对话框，对话框显示后需要手动关闭");
        view.setOnClickListener(v2 -> {
            dialog.closeDialog();
        });
        dialog.setOnActivityLifecycleClose(() -> {
            Toast.makeText(MainActivity.this, "activity销毁而关闭", Toast.LENGTH_SHORT).show();
        });
        dialog.show(view);
        
        
```
-  **密码输入型dialog** 

```java
    DialogLibInput.create(this)
        .setMessage("123")
        .setLength(6)
        .setPortraitWidthFactor(0.85F)
        .setLandscapeWidthFactor(0.5F)
        .setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
        .setAlias("text6")
        //设置显示密码隐藏/显示图片
        .setShowLookPassword()
        //自动弹出键盘
        .setPopupKeyboard()
        .setOnBtnCancel(() -> {
            Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
        })
        .setOnBtnOk(str -> {
            Toast.makeText(MainActivity.this, "输入密码为：" + str, Toast.LENGTH_SHORT).show();
            return true;
        })
        .setOnActivityLifecycleClose(() -> {
            Toast.makeText(MainActivity.this, "activity销毁而关闭", Toast.LENGTH_SHORT).show();
        })
        .show();
```
-  **Snackbar提示框**

```java
    SnackBarLib.make(binding.coordinator,
        "Snackbar提示框Snackbar提示框Snackbar提示框Snackbar提示框Snackbar提示框Snackbar提示框Snackbar提示框Snackbar提示框Snackbar提示框Snackbar提示框",
        10 * 1000)
        .setContentColor(R.color.white)
        .setAction("action", v1 ->
            Toast.makeText(getApplicationContext(), "action的点击事件", Toast.LENGTH_SHORT).show())
        .setActionClickCallback(tag -> {
            Toast.makeText(getApplicationContext(), "由点击action触发关闭", Toast.LENGTH_SHORT).show();
        })
        .showSuccess();

    //或者使用默认风格，与系统默认Snackbar的属性书写习惯基本一致
    SnackBarLib.makeShort(binding.text7,"Snackbar提示框").show();

    //关闭已显示的SnackBar
    SnackBarLib.dismiss();
```

### 四、资源覆盖，改变颜色、字体大小、默认文字


- **drawable下可覆盖资源及注释**

```html
snackbar_lib_bg.xml   SnackBarLib默认使用的背景
```
 **snackbar_lib_bg.xml，如果颜色替换了@color/snackbar_lib_bg，则colors下的snackbar_lib_bg属性覆盖将会失效** 

```html
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/snackbar_lib_bg" />
    <corners android:radius="5dp" />
</shape>
```

 
- **colors下可覆盖资源及注释**

```html
<resources>

    <!-- dialog的统一风格，适用于DialogLibCommon、DialogLibCustom、DialogLibInput -->
    <!-- dialog的统一风格，背景色 -->
    <color name="dialog_all_default_bg">#FFFFFF</color>
    <!-- dialog的统一风格，标题字体颜色 -->
    <color name="dialog_all_default_title_fg">#000000</color>
    <!-- dialog的统一风格，内容区域字体颜色 -->
    <color name="dialog_all_default_content_fg">#000000</color>
    <!-- dialog的统一风格，确认按钮字体颜色 -->
    <color name="dialog_all_default_ok_fg">#2C9BF3</color>
    <!-- dialog的统一风格，取消按钮字体颜色 -->
    <color name="dialog_all_default_cancel_fg">#000000</color>
    <!-- dialog的统一风格，确认按钮背景色 -->
    <color name="dialog_all_default_ok_bg">#FFFFFF</color>
    <!-- dialog的统一风格，取消按钮背景色 -->
    <color name="dialog_all_default_cancel_bg">#FFFFFF</color>
    <!-- dialog的统一风格，风格线的颜色 -->
    <color name="dialog_all_default_split_line_bg">#EAEAEA</color>
    <!-- dialog的统一风格，按钮间分割线的颜色 -->
    <color name="dialog_all_default_button_split_line_bg">#EAEAEA</color>


    <!--dialog的整体背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_bg">@color/dialog_all_default_bg</color>
    <!--dialog的内容文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_content_fg">@color/dialog_all_default_content_fg</color>
    <!--dialog的标题文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_title_fg">@color/dialog_all_default_title_fg</color>
    <!--dialog的 确认 按钮文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_ok_fg">@color/dialog_all_default_ok_fg</color>
    <!--dialog的 取消 按钮文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_cancel_fg">@color/dialog_all_default_cancel_fg</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_ok_bg">@color/dialog_all_default_ok_bg</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_cancel_bg">@color/dialog_all_default_cancel_bg</color>
    <!--dialog的内容文字的文字颜色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_split_line">@color/dialog_all_default_split_line_bg</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibCommon-->
    <color name="dialog_utils_lib_common_button_split_bg">@color/dialog_all_default_button_split_line_bg</color>


    <!--dialog的整体背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_bg">@color/dialog_all_default_bg</color>
    <!--dialog的标题文字的文字颜色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_title_fg">@color/dialog_all_default_title_fg</color>
    <!--dialog的 确认 按钮文字的文字颜色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_ok_fg">@color/dialog_all_default_ok_fg</color>
    <!--dialog的 取消 按钮文字的文字颜色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_cancel_fg">@color/dialog_all_default_cancel_fg</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_ok_bg">@color/dialog_all_default_ok_bg</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_cancel_bg">@color/dialog_all_default_cancel_bg</color>
    <!--dialog的输入框标题与内容和内容与按钮分割线的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_split_line">@color/dialog_all_default_split_line_bg</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibCustom-->
    <color name="dialog_utils_lib_custom_button_split_bg">@color/dialog_all_default_button_split_line_bg</color>


    <!--dialog的整体背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_bg">@color/dialog_all_default_bg</color>
    <!--dialog的标题文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_title_fg">@color/dialog_all_default_title_fg</color>
    <!--dialog的 确认 按钮文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_ok_fg">@color/dialog_all_default_ok_fg</color>
    <!--dialog的 取消 按钮文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_cancel_fg">@color/dialog_all_default_cancel_fg</color>
    <!--dialog的 确认 按钮文字的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_ok_bg">@color/dialog_all_default_ok_bg</color>
    <!--dialog的 取消 按钮文字的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_cancel_bg">@color/dialog_all_default_cancel_bg</color>
    <!--dialog的输入框文字的文字颜色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_fg">@color/dialog_all_default_content_fg</color>
    <!--dialog的输入框下方分割线的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_split_line">@color/dialog_all_default_split_line_bg</color>
    <!--dialog的输入框下方显示2个按钮时，中间分隔的背景色，适用于 DialogLibInput-->
    <color name="dialog_utils_lib_input_button_split_bg">@color/dialog_all_default_button_split_line_bg</color>


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

```html
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

```html
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

```html
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

