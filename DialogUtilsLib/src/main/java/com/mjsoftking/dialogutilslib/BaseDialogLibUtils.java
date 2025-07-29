package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;
import com.mjsoftking.dialogutilslib.utils.DensityUtils;

/**
 * 加载等待框工具类
 */
public abstract class BaseDialogLibUtils implements DialogLibUtils {

    protected Context context;

    protected Dialog dialog;

    protected float landscapeWidthFactor = -1;
    protected float portraitWidthFactor = -1;

    //临时翻转确定与取消按钮位置
    protected Boolean reverseButton;

    protected Context getContext() {
        return context;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialogWidth(String TAG, Dialog dialog) {
        setDialogWidth(TAG, dialog, null);
    }

    public void setDialogFullScreen(String TAG, Dialog dialog) {
        setDialogFullScreen(TAG, dialog, null);
    }

    /**
     * 宽度
     * <p>
     * 设置dialog的宽度
     * 需要在show之后调用
     */
    public void setDialogWidth(String TAG, Dialog dialog, Configuration configuration) {
        try {
            if (null == dialog || !dialog.isShowing()) {
                return;
            }
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
            if (null == configuration) {
                p.width = (int) (size.x * dialogWidthFactor(configuration));
            } else {
                p.width = (int) (DensityUtils.dipToPX(getContext(), configuration.screenWidthDp) * dialogWidthFactor(configuration));
            }
            dialog.getWindow().setAttributes(p);
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.w(TAG, "设置对话框宽度异常", e);
            }
        }
    }

    /**
     * 全屏
     * <p>
     * 设置dialog的宽度
     * 需要在show之后调用
     */
    public void setDialogFullScreen(String TAG, Dialog dialog, Configuration configuration) {
        try {
            if (null == dialog || !dialog.isShowing()) {
                return;
            }
            Window window = dialog.getWindow();
            if (null == window) {
                if (DialogLibInitSetting.getInstance().isDebug()) {
                    Log.w(TAG, "由于window为空，设置对话框属性失败！");
                }
                return;
            }
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            Display d = window.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            d.getSize(size);
            //全屏
            if (null == configuration) {
                layoutParams.width = size.x;
                layoutParams.height = size.y;
            } else {
                layoutParams.width = DensityUtils.dipToPX(getContext(), configuration.screenWidthDp);
                layoutParams.height = DensityUtils.dipToPX(getContext(), configuration.screenHeightDp);
            }
            window.setAttributes(layoutParams);
            window.setDimAmount(0f);
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.w(TAG, "设置对话框宽度异常", e);
            }
        }
    }

    protected float dialogWidthFactor(Configuration mConfiguration) {
        if (null == mConfiguration) {
            mConfiguration = context.getResources().getConfiguration();
        }
        //获取屏幕方向
        int ori = mConfiguration.orientation;
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

    protected float getLandscapeWidthFactor() {
        if (landscapeWidthFactor <= 0 || landscapeWidthFactor >= 1) {
            TypedValue outValue = new TypedValue();
            context.getResources().getValue(R.dimen.dialog_utils_lib_landscape_width_factor, outValue, true);
            float lwf = outValue.getFloat();
            if (lwf <= 0 || lwf >= 1) {
                return 0.5F;
            }
            return lwf;
        }
        return landscapeWidthFactor;
    }

    protected float getPortraitWidthFactor() {
        if (portraitWidthFactor <= 0 || portraitWidthFactor >= 1) {
            TypedValue outValue = new TypedValue();
            context.getResources().getValue(R.dimen.dialog_utils_lib_portrait_width_factor, outValue, true);
            float pwf = outValue.getFloat();
            if (pwf <= 0 || pwf >= 1) {
                return 0.85F;
            }
            return pwf;
        }
        return portraitWidthFactor;
    }


}
