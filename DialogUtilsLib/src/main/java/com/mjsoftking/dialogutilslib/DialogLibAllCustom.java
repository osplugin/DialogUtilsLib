package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

import java.util.HashMap;
import java.util.Map;

/**
 * 全部自定义弹窗提示工具类，需要完全定义dialog的布局
 */
public class DialogLibAllCustom implements DialogLibUtils {
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

    private Context context;
    //别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
    private String alias;
    private boolean cancelable;
    private float landscapeWidthFactor;
    private float portraitWidthFactor;

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private String getAlias() {
        return alias;
    }

    private boolean isCancelable() {
        return cancelable;
    }

    private float getLandscapeWidthFactor() {
        if (landscapeWidthFactor <= 0 || landscapeWidthFactor >= 1) {
            TypedValue outValue = new TypedValue();
            getContext().getResources().getValue(R.dimen.dialog_utils_lib_landscape_width_factor, outValue, true);
            float lwf = outValue.getFloat();
            if (lwf <= 0 || lwf >= 1) {
                return 0.5F;
            }
            return lwf;
        }
        return landscapeWidthFactor;
    }

    private float getPortraitWidthFactor() {
        if (portraitWidthFactor <= 0 || portraitWidthFactor >= 1) {
            TypedValue outValue = new TypedValue();
            getContext().getResources().getValue(R.dimen.dialog_utils_lib_portrait_width_factor, outValue, true);
            float pwf = outValue.getFloat();
            if (pwf <= 0 || pwf >= 1) {
                return 0.85F;
            }
            return pwf;
        }
        return portraitWidthFactor;
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
            setDialogWidth(dialog);
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

    /**
     * 设置dialog的宽度
     * 需要在show之后调用
     */
    private void setDialogWidth(Dialog dialog) {
        Window window = dialog.getWindow();
        if (null == window) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.w(TAG, "由于window为空，设置对话框属性失败！");
            }
            return;
        }
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * dialogWidthFactor());
        dialog.getWindow().setAttributes(p);
    }

    private float dialogWidthFactor() {
        Configuration mConfiguration = context.getResources().getConfiguration();
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            return getLandscapeWidthFactor();
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            return getPortraitWidthFactor();
        } else {
            return getPortraitWidthFactor();
        }
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

}
