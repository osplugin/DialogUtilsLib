package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilslib.databinding.DialogUtilsLibCustomViewBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 自定义弹窗提示工具类
 */
public class DialogLibCustomUtils {

    private Dialog dialog;
    private final boolean registerEvenBus;

    /**
     * 创建对象并注册成为观察者
     */
    public static DialogLibCustomUtils create(Context context) {
        return create(context, true);
    }

    /**
     * 创建对象并根据第二参数决定是否注册成为观察者
     */
    public static DialogLibCustomUtils create(Context context, boolean registerEvenBus) {
        DialogLibCustomUtils dialogCommonUtils = new DialogLibCustomUtils(registerEvenBus);
        dialogCommonUtils.setContext(context);
        return dialogCommonUtils;
    }

    /**
     * 发送关闭事件，仅针对成为观察者的窗体有效
     */
    public static void sendCloseEvent(Object obj) {
        EventBus.getDefault().post(new Event(obj));
    }

    private DialogLibCustomUtils(boolean registerEvenBus) {
        this.registerEvenBus = registerEvenBus;
        if (registerEvenBus) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(Event event) {
        Log.d(getClass().getSimpleName(), "触发者：" + (null == event ? "自身手动关闭" : event.getClassName()));
        closeDialog();
    }

    private Context context;
    private String title;
    private String okDesc;
    private String cancelDesc;
    private boolean noShowOk;
    private boolean noShowCancel;

    private OnCustomBtnOk onCustomBtnOk;
    private OnBtnCancel onBtnCancel;
    private OnBtn onBtn;

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

    private boolean isNoShowOk() {
        return noShowOk;
    }

    private boolean isNoShowCancel() {
        return noShowCancel;
    }

    private OnBtnCancel getOnBtnCancel() {
        if (null == onBtnCancel) {
            onBtnCancel = () -> {
            };
        }
        return onBtnCancel;
    }

    private OnBtn getOnBtn() {
        if (null == onBtn) {
            onBtn = () -> {
            };
        }
        return onBtn;
    }

    private OnCustomBtnOk getOnCustomBtnOk() {
        if (null == onCustomBtnOk) {
            onCustomBtnOk = () -> true;
        }
        return onCustomBtnOk;
    }

    /**
     * 设置自定义视图触发ok按钮时的响应
     * 接口返回true关闭对话框，反之不关闭
     */
    public DialogLibCustomUtils setOnCustomBtnOk(OnCustomBtnOk onCustomBtnOk) {
        this.onCustomBtnOk = onCustomBtnOk;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibCustomUtils setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibCustomUtils setTitle(@StringRes int strId) {
        this.title = getContext().getString(strId);
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibCustomUtils setOkDesc(String okDesc) {
        this.okDesc = okDesc;
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibCustomUtils setOkDesc(@StringRes int strId) {
        this.okDesc = getContext().getString(strId);
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibCustomUtils setCancelDesc(String cancelDesc) {
        this.cancelDesc = cancelDesc;
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibCustomUtils setCancelDesc(@StringRes int strId) {
        this.cancelDesc = getContext().getString(strId);
        return this;
    }


    /**
     * 设置是否不显示确认按钮位置，对应确认按钮事件也无法触发
     * 与{@link DialogLibCustomUtils#noShowCancel()} 同时设置时，只能通过点击提示框外的其他区域关闭（存在按钮时无法点击提示框外的其他区域关闭），且无法触发任何事件回调
     */
    public DialogLibCustomUtils noShowOk() {
        this.noShowOk = true;
        return this;
    }

    /**
     * 设置是否不显示取消按钮位置，对应取消按钮事件也无法触发
     * 与{@link DialogLibCustomUtils#noShowOk()} 同时设置时，只能通过点击提示框外的其他区域关闭（存在按钮时无法点击提示框外的其他区域关闭），且无法触发任何事件回调
     */
    public DialogLibCustomUtils noShowCancel() {
        this.noShowCancel = true;
        return this;
    }

    /**
     * 设置取消按钮的事件触发
     */
    public DialogLibCustomUtils setOnBtnCancel(OnBtnCancel onBtnCancel) {
        this.onBtnCancel = onBtnCancel;
        return this;
    }

    /**
     * 设置确认和取消按钮的事件触发时均会触发的事件
     */
    public DialogLibCustomUtils setOnBtn(OnBtn onBtn) {
        this.onBtn = onBtn;
        return this;
    }

    /**
     * 显示自定义视图的提示框
     * 需要手动控制dialog关闭
     */
    public DialogLibCustomUtils show(View view) {
        dialog = new Dialog(context, R.style.DialogLibUtilsDialogStyle);
        //ContentView
        DialogUtilsLibCustomViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_utils_lib_custom_view, null, false);
        binding.contentGroup.removeAllViews();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        binding.contentGroup.addView(view);
        binding.setOkDesc(getOkDesc());
        binding.setCancelDesc(getCancelDesc());
        binding.setNoShowOk(isNoShowOk());
        binding.setNoShowCancel(isNoShowCancel());
        binding.setClick(v -> {
            try {
                //任何按钮都会触发
                getOnBtn().btn();
                if (v.getId() == R.id.btnOk) {
                    //ok按钮位置触发
                    if (getOnCustomBtnOk().ok()) {
                        closeDialog();
                    }
                } else if (v.getId() == R.id.btnCancel) {
                    closeDialog();
                    //cancel按钮位置触发
                    getOnBtnCancel().cancel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.setTitle(getTitle());
        dialog.setContentView(binding.getRoot());
        //2个按钮都不显示时，则允许点击其他位置关闭
        dialog.setCancelable((isNoShowOk() && isNoShowCancel()));
        dialog.show();
        setDialogWidth(dialog);
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
        p.width = (int) (size.x * 0.85);
        dialog.getWindow().setAttributes(p);
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
    }

    public interface OnBtnOk {
        void ok();
    }

    public interface OnCustomBtnOk {
        boolean ok();
    }

    public interface OnBtnCancel {
        void cancel();
    }

    public interface OnBtn {
        void btn();
    }

    /**
     * 触发此事件可以关闭所有已成为观察者的未关闭的窗体
     */
    public static class Event {
        private String className;

        public Event(Object className) {
            this.className = className.getClass().getName();
        }

        public String getClassName() {
            return className;
        }
    }
}
