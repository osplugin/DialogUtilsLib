package com.mjsoftking.dialogutilslib.callback;

import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.mjsoftking.dialogutilslib.bean.DialogLibBean;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

import java.util.Arrays;

public class ScreenRotationCallback {

    public static void run(@NonNull Configuration newConfig) {
        int mCurrentOrientation = newConfig.orientation;
        if (Arrays.asList(Configuration.ORIENTATION_PORTRAIT,
                Configuration.ORIENTATION_LANDSCAPE).contains(mCurrentOrientation)) {

            if (DialogLibCacheList.getInstance().isEmpty()) return;

            for (DialogLibBean obj : DialogLibCacheList.getInstance()) {
                //匹配activity类名，关闭被销毁的activity下的所有对话框
                obj.getDialogLibUtils().setDialogWidth(newConfig);
            }

        }
    }

}
