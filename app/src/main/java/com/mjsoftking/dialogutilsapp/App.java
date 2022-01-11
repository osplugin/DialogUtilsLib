package com.mjsoftking.dialogutilsapp;

import android.app.Application;

import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;

/**
 * 用途：
 * <p>
 * 作者：mjSoftKing
 * 时间：2021/04/28
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化dialog工具类设置
        DialogLibInitSetting.getInstance()
                //设置debug
                .setDebug(BuildConfig.DEBUG)
                //设置是否反转确定和取消按钮位置，false：左确定，右取消；true：左取消，右确定。默认：false
                .setReverseButton(true)
                //注册全局activity生命周期监听
                .registerActivityLifecycleCallbacks(this);

    }
}
