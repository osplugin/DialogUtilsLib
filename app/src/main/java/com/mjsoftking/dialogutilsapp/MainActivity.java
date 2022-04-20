package com.mjsoftking.dialogutilsapp;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilsapp.databinding.ActivityMainBinding;
import com.mjsoftking.dialogutilsapp.databinding.PopLayoutBinding;
import com.mjsoftking.dialogutilslib.DialogLibAllCustom;
import com.mjsoftking.dialogutilslib.DialogLibCommon;
import com.mjsoftking.dialogutilslib.DialogLibCustom;
import com.mjsoftking.dialogutilslib.DialogLibInput;
import com.mjsoftking.dialogutilslib.DialogLibLoading;
import com.mjsoftking.dialogutilslib.PopupWindowLib;
import com.mjsoftking.dialogutilslib.SnackBarLib;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private Handler handler = new Handler(Looper.myLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            SnackBarLib.dismiss();
        }
    };

    //todo colors、strings、dimens 文件分别展示了覆盖属性，变更对话框的颜色风格，具体属性请前往对应lib下查看
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setClick(this);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.e("MainActivity屏幕", "旋转");

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View v) {
        if (v.equals(binding.text1)) {
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
        } else if (v.equals(binding.text2)) {
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
        } else if (v.equals(binding.text3)) {
            DialogLibInput.create(this)
                    .setPortraitWidthFactor(0.85F)
                    .setLandscapeWidthFactor(0.5F)
                    .setMessage("输入信息")
                    .setAlias("text3")
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
        } else if (v.equals(binding.text4)) {
            DialogLibLoading.create(this)
                    .setTimeoutClose(10 * 1000)
                    .setAlias("text4")
                    .setOnLoading(() -> {
                        Toast.makeText(MainActivity.this, "我是显示对话框前触发的，10秒后自动关闭", Toast.LENGTH_SHORT).show();
                    })
                    .setOnActivityLifecycleClose(() -> {
                        Toast.makeText(MainActivity.this, "activity销毁而关闭", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } else if (v.equals(binding.text5)) {
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
        } else if (v.equals(binding.text6)) {
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
        } else if (v.equals(binding.text7)) {
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

            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 1000 * 3);
        } else if (v.equals(binding.text8)) {
            PopLayoutBinding plb = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_layout, null, false);

            PopupWindowLib popupWindowLib = PopupWindowLib.create()
                    .setOutsideTouchable(true)
                    .setAttachedInDecor(true)
                    .setFocusable(true)
//                    .setContentView(plb.getRoot(), WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setContentView(plb.getRoot(), binding.text8.getWidth(), 280)
                    .setAutoCloseTime(8 * 1000)
                    .showAsDropDown(binding.text8, 0, 20);
        }
    }
}