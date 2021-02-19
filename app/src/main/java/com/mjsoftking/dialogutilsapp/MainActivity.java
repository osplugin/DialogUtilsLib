package com.mjsoftking.dialogutilsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilsapp.databinding.ActivityMainBinding;
import com.mjsoftking.dialogutilslib.DialogLibAllCustomUtils;
import com.mjsoftking.dialogutilslib.DialogLibCommonUtils;
import com.mjsoftking.dialogutilslib.DialogLibCustomUtils;
import com.mjsoftking.dialogutilslib.DialogLibInputUtils;
import com.mjsoftking.dialogutilslib.DialogLibLoadingUtils;
import com.mjsoftking.dialogutilslib.DialogLibParam;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    //todo colors、strings、dimens 文件分别展示了覆盖属性，变更对话框的颜色风格，具体属性请前往对应lib下查看
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo 无论使用那个版本的aar资源，使用此方法均会强制改变是否是debug模式，此模式仅仅会控制是否打印日志。
        DialogLibParam.getInstance().setDebug(true);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setClick(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //todo 因系统字体、翻转等导致的销毁，使用下面的方法可以关闭全部使用create(this)方式注册的窗体，而不引发泄漏异常
        //todo 使用create(this, false)方式注册的需要手动处理返回的对象，进行关闭
        DialogLibCommonUtils.sendCloseEvent(this);
        DialogLibCustomUtils.sendCloseEvent(this);
        DialogLibInputUtils.sendCloseEvent(this);
        DialogLibLoadingUtils.sendCloseEvent(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(binding.text1)) {
            DialogLibCommonUtils.create(this)
                    .setMessage("普通对话框")
                    .setAlias("text1")
                    .noShowCancel()
                    .show();
            //故意产生2次
            DialogLibCommonUtils.create(this)
                    .setMessage("普通对话框")
                    .setAlias("text1")
                    .noShowCancel()
                    .show();
        } else if (v.equals(binding.text2)) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
            DialogLibCustomUtils.create(this)
                    .noShowCancel()
                    .setAlias("text2")
                    .show(imageView);
        } else if (v.equals(binding.text3)) {
            DialogLibInputUtils.create(this)
                    .setMessage("输入信息")
                    .setAlias("text3")
                    .setOnBtnOk(str -> {
                        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                        return true;
                    })
                    .show();
        } else if (v.equals(binding.text4)) {
            DialogLibLoadingUtils.create(this)
                    .setTimeoutClose(2000)
                    .setAlias("text4")
                    .setOnLoading(() -> {
                        Toast.makeText(MainActivity.this, "我是显示对话框前触发的", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        } else if (v.equals(binding.text5)) {
            final DialogLibAllCustomUtils dialog = DialogLibAllCustomUtils.create(this)
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