package com.mjsoftking.dialogutilslib.bean;

import androidx.annotation.NonNull;

import com.mjsoftking.dialogutilslib.DialogLibUtils;


/**
 * 用途：用来缓存activity下存活的dialog
 * <p>
 * 作者：mjSoftKing
 * 时间：2021/04/28
 */
public class DialogLibBean {

    private final String activityName;
    private final DialogLibUtils dialogLibUtils;

    public DialogLibBean(@NonNull String activityName, @NonNull DialogLibUtils dialogLibUtils) {
        this.activityName = activityName;
        this.dialogLibUtils = dialogLibUtils;
    }

    @NonNull
    public String getActivityName() {
        return activityName;
    }

    @NonNull
    public DialogLibUtils getDialogLibUtils() {
        return dialogLibUtils;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DialogLibBean)) return false;
        DialogLibBean that = (DialogLibBean) o;
        return dialogLibUtils.hashCode() == that.getDialogLibUtils().hashCode();
    }

    @Override
    public int hashCode() {
        return dialogLibUtils.hashCode();
    }
}
