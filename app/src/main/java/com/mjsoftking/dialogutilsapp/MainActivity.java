package com.mjsoftking.dialogutilsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilsapp.databinding.ActivityMainBinding;
import com.mjsoftking.dialogutilslib.DialogLibAllCustom;
import com.mjsoftking.dialogutilslib.DialogLibCommon;
import com.mjsoftking.dialogutilslib.DialogLibCustom;
import com.mjsoftking.dialogutilslib.DialogLibInput;
import com.mjsoftking.dialogutilslib.DialogLibLoading;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    //todo colors、strings、dimens 文件分别展示了覆盖属性，变更对话框的颜色风格，具体属性请前往对应lib下查看
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setClick(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(binding.text1)) {
            DialogLibCommon.create(this)
                    .setMessage("普通对话框1")
                    .setAlias("text1")
                    .noShowCancel()
                    .show();
        } else if (v.equals(binding.text2)) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            DialogLibCustom.create(this)
                    .noShowCancel()
                    .setAlias("text2")
                    .show(imageView);
        } else if (v.equals(binding.text3)) {
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
        } else if (v.equals(binding.text4)) {
            DialogLibLoading.create(this)
                    .setTimeoutClose(2000)
                    .setAlias("text4")
                    .setOnLoading(() -> {
                        Toast.makeText(MainActivity.this, "我是显示对话框前触发的", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } else if (v.equals(binding.text5)) {
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
        }
    }
}