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
import android.view.WindowManager;

import androidx.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 全部自定义弹窗提示工具类，需要完全定义dialog的布局
 */
public class DialogLibAllCustomUtils {
    private final static String TAG = DialogLibAllCustomUtils.class.getSimpleName();
    private final static Map<String, DialogLibAllCustomUtils> MAP = new HashMap<>();

    private Dialog dialog;
    private final boolean registerEvenBus;
    //标签
    @NonNull
    private final String tag;

    /**
     * 创建对象并注册成为观察者
     */
    public static DialogLibAllCustomUtils create(Context context) {
        return create(context, true);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibAllCustomUtils create(Context context, boolean registerEvenBus) {
        return create(context, "", registerEvenBus);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibAllCustomUtils create(Context context, @NonNull String tag) {
        return create(context, tag, true);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibAllCustomUtils create(Context context, @NonNull String tag, boolean registerEvenBus) {
        DialogLibAllCustomUtils utils = new DialogLibAllCustomUtils(tag, registerEvenBus);
        utils.setContext(context);
        return utils;
    }

    /**
     * 发送关闭事件，仅针对成为观察者的窗体有效
     */
    public static void sendCloseEvent(Object obj) {
        sendCloseEvent(obj, null);
    }

    /**
     * 发送关闭事件，仅针对成为观察者的窗体有效
     */
    public static void sendCloseEvent(Object obj, String tag) {
        EventBus.getDefault().post(new DialogLibCommonUtils.Event(obj, tag));
    }

    private DialogLibAllCustomUtils(@NonNull String tag, boolean registerEvenBus) {
        this.registerEvenBus = registerEvenBus;
        this.tag = tag;
        if (registerEvenBus) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Event event) {
        if (null != event) {
            if (null == event.getTag() || event.getTag().equals(tag)) {
                if (DialogLibParam.getInstance().isDebug()) {
                    Log.w(getClass().getSimpleName(), "触发关闭者：" + event.getClassName());
                }
            } else {
                //tag 校验不符合，不关闭此窗口
                return;
            }
        }
        closeDialog();
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
    public DialogLibAllCustomUtils setLandscapeWidthFactor(float landscapeWidthFactor) {
        this.landscapeWidthFactor = landscapeWidthFactor;
        return this;
    }

    /**
     * 竖屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibAllCustomUtils setPortraitWidthFactor(float portraitWidthFactor) {
        this.portraitWidthFactor = portraitWidthFactor;
        return this;
    }

    /**
     * 是否允许点击其他位置关闭
     */
    public DialogLibAllCustomUtils setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibAllCustomUtils setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 显示自定义视图的提示框
     * 需要手动控制dialog关闭
     */
    public DialogLibAllCustomUtils show(View view) {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibAllCustomUtils obj = MAP.get(getAlias());
            if (null != obj) {
                //此时关闭自己，并移除注册，但不解除MAP缓存
                this.closeDialog(false);
                if (DialogLibParam.getInstance().isDebug()) {
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
            if (DialogLibParam.getInstance().isDebug()) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        if (!TextUtils.isEmpty(getAlias())) {
            MAP.put(getAlias(), this);
        }

        return this;
    }

    /**
     * 设置dialog的宽度
     * 需要在show之后调用
     */
    private void setDialogWidth(Dialog dialog) {
        WindowManager m = dialog.getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
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

    public void closeDialog() {
        closeDialog(true);
    }

    private void closeDialog(boolean remove) {
        if (registerEvenBus) {
            EventBus.getDefault().unregister(this);
        }
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception ignore) {
        }

        if (!TextUtils.isEmpty(getAlias()) && remove) {
            MAP.remove(getAlias());
        }
    }

    /**
     * 触发此事件可以关闭所有已成为观察者的未关闭的窗体
     */
    public static class Event {
        private String className;
        private String tag;

        public Event(Object className, String tag) {
            this.className = className.getClass().getName();
            this.tag = tag;
        }

        public String getClassName() {
            return className;
        }

        public String getTag() {
            return tag;
        }
    }
}
