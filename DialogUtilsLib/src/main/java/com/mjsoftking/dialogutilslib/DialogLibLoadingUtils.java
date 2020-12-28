package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilslib.databinding.DialogUtilsLibLoadingDataBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载等待框工具类
 */
public class DialogLibLoadingUtils {
    private final static String TAG = DialogLibLoadingUtils.class.getSimpleName();
    private final static Map<String, DialogLibLoadingUtils> MAP = new HashMap<>();

    private Dialog dialog;
    private final boolean registerEvenBus;
    private DialogUtilsLibLoadingDataBinding binding;
    //标签
    @NonNull
    private final String tag;

    /**
     * 创建对象并注册成为观察者
     */
    public static DialogLibLoadingUtils create(Context context) {
        return create(context, true);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibLoadingUtils create(Context context, boolean registerEvenBus) {
        return create(context, "", registerEvenBus);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibLoadingUtils create(Context context, @NonNull String tag) {
        return create(context, tag, true);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibLoadingUtils create(Context context, @NonNull String tag, boolean registerEvenBus) {
        DialogLibLoadingUtils utils = new DialogLibLoadingUtils(tag, registerEvenBus);
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

    private DialogLibLoadingUtils(@NonNull String tag, boolean registerEvenBus) {
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
                Log.w(getClass().getSimpleName(), "触发关闭者：" + event.getClassName());
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
    private String message;
    private Integer timeout;
    //显示前触发，可以先调用show显示，根据此事件去做事情
    private OnLoading onLoading;

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private String getMessage() {
        if (TextUtils.isEmpty(message)) {
            return context.getString(R.string.dialog_utils_lib_data_processing);
        }
        return message;
    }

    //获取超时时间，最小 500 毫秒
    private Integer getTimeout() {
        if (null != timeout && timeout <= 500) {
            timeout = 500;
        }
        return timeout;
    }

    private String getAlias() {
        return alias;
    }

    private OnLoading getOnLoading() {
        if (null == onLoading) {
            onLoading = () -> {
            };
        }
        return onLoading;
    }

    /**
     * 显示前触发，可以先调用show显示，根据此事件去做事情
     */
    public DialogLibLoadingUtils setOnLoading(OnLoading onLoading) {
        this.onLoading = onLoading;
        return this;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibLoadingUtils setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 设置提示信息的内容部分，
     * dialog未显示时，默认为“数据处理中...”
     * dialog显示时，则刷新显示的内容
     */
    public DialogLibLoadingUtils setMessage(String message) {
        if (null != binding) {
            binding.setMessage(message);
        } else {
            this.message = message;
        }
        return this;
    }

    /**
     * 设置提示信息的内容部分，
     * dialog未显示时，默认为“数据处理中...”
     * dialog显示时，则刷新显示的内容
     */
    public DialogLibLoadingUtils setMessage(@StringRes int strId) {
        if (null != binding) {
            binding.setMessage(getContext().getString(strId));
        } else {
            this.message = getContext().getString(strId);
        }
        return this;
    }

    /**
     * 提示框的显示时间，自显示开始计时，达到此时间时会自动关闭，不设置则需要手动关闭
     *
     * @param timeout 超时时间，单位毫秒（最小500毫秒，低于500均按照500毫米计算），超过此时间后自动关闭，不设置不关闭
     */
    public DialogLibLoadingUtils setTimeoutClose(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 显示提示信息的对话框，根据链式写法传递参数决定显示
     */
    public DialogLibLoadingUtils show() {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibLoadingUtils obj = MAP.get(getAlias());
            if (null != obj) {
                Log.w(TAG, String.format("别名('%s')限制，仅能同时显示一个同别名对话框", getAlias()));
                return obj;
            }
        }

        try {
            //show前先触发，可以在此事件开始任务，配合别名，可以避免快速点击时触发2次任务的问题
            getOnLoading().loading();

            dialog = new Dialog(context, R.style.DialogLibUtilsDialogLoadingStyle);
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_utils_lib_loading_data, null, false);
            binding.setMessage(getMessage());
            //ContentView
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(false);

            dialog.show();
            setDialogWidth(dialog);

            //如果设置了超时关闭，则时间到时关闭，反之需要手动关闭
            if (null != getTimeout()) {
                new Handler().postDelayed(this::closeDialog, getTimeout());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        Display d = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        //全屏
        layoutParams.width = (int) (size.x * 1);
        layoutParams.height = (int) (size.y * 1);
        window.setAttributes(layoutParams);
        window.setDimAmount(0f);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void closeDialog() {
        if (registerEvenBus) {
            EventBus.getDefault().unregister(this);
        }
        try {
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
        }

        if (!TextUtils.isEmpty(getAlias())) {
            MAP.remove(getAlias());
        }
    }

    public interface OnLoading {
        void loading();
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
