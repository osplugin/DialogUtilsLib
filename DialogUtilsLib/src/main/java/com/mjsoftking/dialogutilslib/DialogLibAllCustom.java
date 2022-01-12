package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

import java.util.HashMap;
import java.util.Map;

/**
 * 全部自定义弹窗提示工具类，需要完全定义dialog的布局
 */
public class DialogLibAllCustom extends BaseDialogLibUtils {
    private final static String TAG = DialogLibAllCustom.class.getSimpleName();
    private final static Map<String, DialogLibAllCustom> MAP = new HashMap<>();

    private Dialog dialog;

    /**
     * 创建对象
     */
    public static DialogLibAllCustom create(Context context) {
        DialogLibAllCustom utils = new DialogLibAllCustom();
        utils.setContext(context);
        return utils;
    }

    private DialogLibAllCustom() {
    }

    //别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
    private String alias;
    private boolean cancelable;

    private OnActivityLifecycleClose onActivityLifecycleClose;

    private void setContext(Context context) {
        this.context = context;
    }

    private String getAlias() {
        return alias;
    }

    private boolean isCancelable() {
        return cancelable;
    }

    /**
     * 横屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibAllCustom setLandscapeWidthFactor(float landscapeWidthFactor) {
        this.landscapeWidthFactor = landscapeWidthFactor;
        return this;
    }

    /**
     * 竖屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibAllCustom setPortraitWidthFactor(float portraitWidthFactor) {
        this.portraitWidthFactor = portraitWidthFactor;
        return this;
    }

    /**
     * 是否允许点击其他位置关闭
     */
    public DialogLibAllCustom setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibAllCustom setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 设置因activity生命周期结束而关闭对话框时，触发的回调
     */
    public DialogLibAllCustom setOnActivityLifecycleClose(OnActivityLifecycleClose onActivityLifecycleClose) {
        this.onActivityLifecycleClose = onActivityLifecycleClose;
        return this;
    }

    /**
     * activity生命周期结束时调用此方法触发相关回调
     */
    public void activityLifecycleClose() {
        if (null != onActivityLifecycleClose) {
            onActivityLifecycleClose.close();
        }
    }

    /**
     * 显示自定义视图的提示框
     * 需要手动控制dialog关闭
     */
    public DialogLibAllCustom show(View view) {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibAllCustom obj = MAP.get(getAlias());
            if (null != obj) {
                //此时关闭自己，并移除注册，但不解除MAP缓存
                this.closeDialog(false);
                if (DialogLibInitSetting.getInstance().isDebug()) {
                    Log.w(TAG, String.format("别名('%s')限制，仅能同时显示一个同别名对话框", getAlias()));
                }
                return obj;
            }
        }

        try {
            dialog = new Dialog(context, R.style.DialogLibUtilsDialogStyle);
            dialog.setContentView(view);
            dialog.setCancelable(isCancelable());
            dialog.setOnCancelListener(dialog -> closeDialog());
            dialog.show();
            setDialogWidth(TAG, dialog);
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        if (!TextUtils.isEmpty(getAlias())) {
            MAP.put(getAlias(), this);
        }

        DialogLibCacheList.getInstance().add(getContext(), this);
        return this;
    }

    public void setDialogWidth(Configuration configuration) {
        setDialogWidth(TAG, dialog, configuration);
    }

    public boolean closeDialog() {
        return closeDialog(true);
    }

    private boolean closeDialog(boolean remove) {
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.w(TAG, "关闭对话框异常", e);
            }
        }

        if (!TextUtils.isEmpty(getAlias()) && remove) {
            MAP.remove(getAlias());
        }

        if (remove) {
            DialogLibCacheList.getInstance().remove(getContext(), this);
        }

        return true;
    }

    public interface OnActivityLifecycleClose {
        void close();
    }
}
