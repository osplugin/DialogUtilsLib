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

import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilslib.databinding.DialogUtilsLibLoadingDataBinding;
import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

import java.util.HashMap;
import java.util.Map;

/**
 * 加载等待框工具类
 */
public class DialogLibLoading implements DialogLibUtils {
    private final static String TAG = DialogLibLoading.class.getSimpleName();
    private final static Map<String, DialogLibLoading> MAP = new HashMap<>();

    private Dialog dialog;
    private DialogUtilsLibLoadingDataBinding binding;

    /**
     * 创建对象
     */
    public static DialogLibLoading create(Context context) {
        DialogLibLoading utils = new DialogLibLoading();
        utils.setContext(context);
        return utils;
    }

    private DialogLibLoading() {
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
    public DialogLibLoading setOnLoading(OnLoading onLoading) {
        this.onLoading = onLoading;
        return this;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibLoading setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 设置提示信息的内容部分，
     * dialog未显示时，默认为“数据处理中...”
     * dialog显示时，则刷新显示的内容
     */
    public DialogLibLoading setMessage(String message) {
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
    public DialogLibLoading setMessage(@StringRes int strId) {
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
    public DialogLibLoading setTimeoutClose(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 显示提示信息的对话框，根据链式写法传递参数决定显示
     */
    public DialogLibLoading show() {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibLoading obj = MAP.get(getAlias());
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
            //show前先触发，可以在此事件开始任务，配合别名，可以避免快速点击时触发2次任务的问题
            getOnLoading().loading();

            dialog = new Dialog(context, R.style.DialogLibUtilsDialogLoadingStyle);
            binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_utils_lib_loading_data, null, false);
            binding.setMessage(getMessage());
            //ContentView
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(false);
            dialog.setOnCancelListener(dialog -> closeDialog());

            dialog.show();
            setDialogWidth(dialog);

            //如果设置了超时关闭，则时间到时关闭，反之需要手动关闭
            if (null != getTimeout()) {
                new Handler().postDelayed(this::closeDialog, getTimeout());
            }
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
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        Display d = window.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        //全屏
        layoutParams.width = (int) (size.x);
        layoutParams.height = (int) (size.y);
        window.setAttributes(layoutParams);
        window.setDimAmount(0f);
        dialog.setCanceledOnTouchOutside(false);
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

    public interface OnLoading {
        void loading();
    }

}
