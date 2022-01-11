package com.mjsoftking.dialogutilsapp;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

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

    // **application** 下的此方法进行注册，且 **activity** 设置 **android:configChanges="orientation|keyboardHidden|screenSize"** 时，
    // 屏幕翻转不会销毁重建 **activity** ，注册的此方法，将会根据设置的横竖屏宽度比自动改变dialog的宽度大小。
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        DialogLibInitSetting.getInstance().onScreenRotation(newConfig);

    }

}
