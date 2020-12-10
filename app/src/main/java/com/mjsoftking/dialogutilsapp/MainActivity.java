package com.mjsoftking.dialogutilsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilsapp.databinding.ActivityMainBinding;
import com.mjsoftking.dialogutilslib.DialogLibCommonUtils;
import com.mjsoftking.dialogutilslib.DialogLibCustomUtils;
import com.mjsoftking.dialogutilslib.DialogLibInputUtils;
import com.mjsoftking.dialogutilslib.DialogLibLoadingUtils;

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
            DialogLibCommonUtils.create(this)
                    .setMessage("普通对话框")
                    .noShowCancel()
                    .show();
        } else if (v.equals(binding.text2)) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            DialogLibCustomUtils.create(this)
                    .noShowCancel()
                    .show(imageView);
        } else if (v.equals(binding.text3)) {
            DialogLibInputUtils.create(this)
                    .setMessage("输入信息")
                    .setOnBtnOk(str -> {
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        return true;
                    })
                    .show();
        } else if (v.equals(binding.text4)) {
            DialogLibLoadingUtils.create(this)
                    .setTimeoutClose(2000)
                    .show();
        }
    }
}