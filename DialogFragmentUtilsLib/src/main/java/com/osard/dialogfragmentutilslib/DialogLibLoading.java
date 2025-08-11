package com.osard.dialogfragmentutilslib;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.osard.dialogfragmentutilslib.databinding.DialogUtilsLibLoadingDataBinding;
import com.osard.dialogfragmentutilslib.init.DialogLibInitSetting;

/**
 * 加载等待框工具类
 */
public class DialogLibLoading extends BaseDialogLibUtils {
    private final static String TAG = DialogLibLoading.class.getSimpleName();

    private DialogUtilsLibLoadingDataBinding binding;
    private final Handler handler;
    private final Runnable timeoutRunnable = this::closeDialog;

    /**
     * 创建对象
     */
    public static DialogLibLoading create(Context context) {
        DialogLibLoading utils = new DialogLibLoading();
        utils.setContext(context);
        return utils;
    }

    private DialogLibLoading() {
        handler = new Handler(Looper.getMainLooper());
    }

    private String message;
    private Integer timeout;
    private OnDismissListener onDismissListener;

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

    /**
     * 设置dialog关闭时触发的回调
     */
    public DialogLibLoading setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_utils_lib_loading_data, null, false);
        binding.setMessage(getMessage());
        setCancelable(false);
        return binding.getRoot();
    }

    /**
     * 设置对话框是否可取消，不建议直接使用，会破坏链式结构
     */
    @Deprecated
    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        closeDialog();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setDialogFullScreen(TAG, getDialog(), newConfig);
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogFullScreen(TAG, getDialog(), context.getResources().getConfiguration());
    }

    @Override
    protected void onDismissDialog() {
        if (null != onDismissListener) {
            onDismissListener.dismiss();
        }
    }

    /**
     * 显示提示信息的对话框，根据链式写法传递参数决定显示
     */
    public DialogLibLoading show() {
        try {
            if (MAP.containsKey(getAlias())) {
                MAP.get(getAlias()).closeDuplicateAliasDialog();
            }
            MAP.put(getAlias(), this);

            handler.removeCallbacks(timeoutRunnable);
            handler.post(showDialogRunnable);
            //如果设置了超时关闭，则时间到时关闭，反之需要手动关闭
            if (null != getTimeout()) {
                handler.postDelayed(timeoutRunnable, getTimeout());
            }
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        return this;
    }

    public boolean closeDialog() {
        closeDialogWithRetry(TAG, 0);
        handler.removeCallbacks(timeoutRunnable);
        return true;
    }

    public interface OnDismissListener {
        void dismiss();
    }
}
