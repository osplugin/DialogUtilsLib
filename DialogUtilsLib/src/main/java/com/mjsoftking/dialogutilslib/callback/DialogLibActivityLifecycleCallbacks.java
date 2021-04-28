package com.mjsoftking.dialogutilslib.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.mjsoftking.dialogutilslib.bean.DialogLibBean;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

/**
 * 用途：当检测到activity生命周期变化时执行
 * <p>
 * 当销毁的activity下使用含有dialog，则销毁所有依附于此activity的dialog，避免发生内存泄漏
 * <p>
 * 作者：mjSoftKing
 * 时间：2021/04/28
 */
public class DialogLibActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        checkCacheAndDialogClose(activity);
    }

    /**
     * 检查产生生命周期的activity缓存内是否存在dialog，如果有则全部关闭。
     */
    public void checkCacheAndDialogClose(@NonNull Activity activity) {
        if (DialogLibCacheList.getInstance().isEmpty()) return;

        String activityClassName = activity.getComponentName().getClassName();
        for (DialogLibBean obj : DialogLibCacheList.getInstance()) {
            //匹配activity类名，关闭被销毁的activity下的所有对话框
            if (activityClassName.equals(obj.getActivityName())) {
                obj.getDialogLibUtils().closeDialog();
            }
        }
    }
}
