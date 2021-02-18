package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilslib.databinding.DialogUtilsLibTipInputBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 输入型弹窗提示工具类
 */
public class DialogLibInputUtils {
    private final static String TAG = DialogLibInputUtils.class.getSimpleName();
    private final static Map<String, DialogLibInputUtils> MAP = new HashMap<>();

    private Dialog dialog;
    private final boolean registerEvenBus;
    //标签
    @NonNull
    private final String tag;

    /**
     * 创建对象并注册成为观察者
     */
    public static DialogLibInputUtils create(Context context) {
        return create(context, true);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibInputUtils create(Context context, boolean registerEvenBus) {
        return create(context, "", registerEvenBus);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibInputUtils create(Context context, @NonNull String tag) {
        return create(context, tag, true);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibInputUtils create(Context context, @NonNull String tag, boolean registerEvenBus) {
        DialogLibInputUtils utils = new DialogLibInputUtils(tag, registerEvenBus);
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

    private DialogLibInputUtils(@NonNull String tag, boolean registerEvenBus) {
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
    private String title;
    private String message;
    private String okDesc;
    private String cancelDesc;
    private KeyListener keyListener;
    private Integer inputType;
    private Integer length;

    private OnBtnOk onBtnOk;
    private OnBtnCancel onBtnCancel;

    private Context getContext() {
        return context;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    private String getTitle() {
        if (TextUtils.isEmpty(title)) {
            return getContext().getResources().getString(R.string.dialog_utils_lib_default_title);
        }
        return title;
    }

    private String getMessage() {
        if (TextUtils.isEmpty(message)) {
            return "";
        }
        return message;
    }

    private String getOkDesc() {
        if (TextUtils.isEmpty(okDesc)) {
            return getContext().getResources().getString(R.string.dialog_utils_lib_ok);
        }
        return okDesc;
    }

    private String getCancelDesc() {
        if (TextUtils.isEmpty(cancelDesc)) {
            return getContext().getResources().getString(R.string.dialog_utils_lib_cancel);
        }
        return cancelDesc;
    }

    private OnBtnOk getOnBtnOk() {
        if (null == onBtnOk) {
            onBtnOk = (text) -> true;
        }
        return onBtnOk;
    }

    private OnBtnCancel getOnBtnCancel() {
        if (null == onBtnCancel) {
            onBtnCancel = () -> {
            };
        }
        return onBtnCancel;
    }

    private KeyListener getKeyListener() {
        return keyListener;
    }

    private Integer getInputType() {
        return inputType;
    }

    private Integer getLength() {
        return length;
    }

    private String getAlias() {
        return alias;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibInputUtils setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 设置字符长度
     */
    public DialogLibInputUtils setLength(Integer length) {
        if (null != length && length > 0) {
            this.length = length;
        }
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibInputUtils setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibInputUtils setTitle(@StringRes int strId) {
        this.title = getContext().getString(strId);
        return this;
    }

    /**
     * 设置提示信息的内容部分，默认为“”(空字符串)
     */
    public DialogLibInputUtils setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置提示信息的内容部分，默认为“”(空字符串)
     */
    public DialogLibInputUtils setMessage(@StringRes int strId) {
        this.message = getContext().getString(strId);
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibInputUtils setOkDesc(String okDesc) {
        this.okDesc = okDesc;
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibInputUtils setOkDesc(@StringRes int strId) {
        this.okDesc = getContext().getString(strId);
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibInputUtils setCancelDesc(String cancelDesc) {
        this.cancelDesc = cancelDesc;
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibInputUtils setCancelDesc(@StringRes int strId) {
        this.cancelDesc = getContext().getString(strId);
        return this;
    }

    /**
     * 设置确认按钮的事件触发
     * 此接口返回 true，则关闭对话框，反之持续显示不关闭
     */
    public DialogLibInputUtils setOnBtnOk(OnBtnOk onBtnOk) {
        this.onBtnOk = onBtnOk;
        return this;
    }

    /**
     * 设置取消按钮的事件触发
     */
    public DialogLibInputUtils setOnBtnCancel(OnBtnCancel onBtnCancel) {
        this.onBtnCancel = onBtnCancel;
        return this;
    }

    /**
     * 设置限定输入字符
     *
     * @param digits 限定字符，null或空字符则设置无效
     */
    public DialogLibInputUtils setKeyListener(String digits) {
        if (null != digits && digits.length() != 0) {
            this.keyListener = DigitsKeyListener.getInstance(digits);
        }
        return this;
    }

    /**
     * 设置限定输入字符
     *
     * @param strId 限定字符，string资源
     */
    public DialogLibInputUtils setKeyListener(@StringRes int strId) {
        this.keyListener = DigitsKeyListener.getInstance(getContext().getString(strId));
        return this;
    }

    /**
     * 设置输入的类型
     *
     * @param inputType {@link EditorInfo#TYPE_TEXT_FLAG_MULTI_LINE}
     */
    public DialogLibInputUtils setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    /**
     * 显示提示信息的对话框，根据链式写法传递参数决定显示
     */
    public DialogLibInputUtils show() {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibInputUtils obj = MAP.get(getAlias());
            if (null != obj) {
                //此时关闭自己，并移除注册，但不解除MAP缓存
                this.closeDialog(false);
                Log.w(TAG, String.format("别名('%s')限制，仅能同时显示一个同别名对话框", getAlias()));
                return obj;
            }
        }

        try {
            dialog = new Dialog(context, R.style.DialogLibUtilsDialogStyle);

            DialogUtilsLibTipInputBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_utils_lib_tip_input, null, false);
            binding.setClick(v -> {
                try {
                    if (v.getId() == R.id.btnOk) {
                        //ok按钮位置触发
                        if (getOnBtnOk().ok(binding.getMessage())) {
                            //关闭对话框
                            closeDialog();
                        }
                    } else if (v.getId() == R.id.btnCancel) {
                        //关闭对话框
                        closeDialog();
                        //cancel按钮位置触发
                        getOnBtnCancel().cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            binding.setTitle(getTitle());
            binding.setMessage(getMessage());
            binding.setOkDesc(getOkDesc());
            binding.setCancelDesc(getCancelDesc());

            if (null != getInputType()) {
                binding.etContent.setInputType(getInputType());
            }
            if (null != getKeyListener()) {
                binding.etContent.setKeyListener(getKeyListener());
            }
            if (null != getLength()) {
                binding.etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(getLength())});
            }

            //ContentView
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(false);

            dialog.show();
            setDialogWidth(dialog);
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
            return context.getResources().getDimension(R.dimen.dialog_utils_lib_landscape_width_factor);
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            return context.getResources().getDimension(R.dimen.dialog_utils_lib_portrait_width_factor);
        } else {
            return context.getResources().getDimension(R.dimen.dialog_utils_lib_portrait_width_factor);
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
        } catch (Exception e) {
        }

        if (!TextUtils.isEmpty(getAlias()) && remove) {
            MAP.remove(getAlias());
        }
    }

    public interface OnBtnOk {
        boolean ok(String text);
    }

    public interface OnBtnCancel {
        void cancel();
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
