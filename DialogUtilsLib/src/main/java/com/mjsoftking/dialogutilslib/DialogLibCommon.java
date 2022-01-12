package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilslib.databinding.DialogUtilsLibTipBinding;
import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

import java.util.HashMap;
import java.util.Map;

/**
 * 常规弹窗提示工具类
 */
public class DialogLibCommon extends BaseDialogLibUtils {
    private final static String TAG = DialogLibCommon.class.getSimpleName();
    private final static Map<String, DialogLibCommon> MAP = new HashMap<>();

    private Dialog dialog;
    //别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
    private String alias;
    private String title;
    private String message;
    private String okDesc;
    private String cancelDesc;
    private boolean noShowOk;
    private boolean noShowCancel;
    private OnBtnOk onBtnOk;
    private OnBtnCancel onBtnCancel;
    private OnBtn onBtn;
    private OnBtnMessage onBtnMessage;
    private OnActivityLifecycleClose onActivityLifecycleClose;
    private Integer messageGravity;

    private DialogLibCommon() {
    }

    /**
     * 创建对象
     */
    public static DialogLibCommon create(Context context) {
        DialogLibCommon utils = new DialogLibCommon();
        utils.setContext(context);
        return utils;
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

    /**
     * 横屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibCommon setLandscapeWidthFactor(float landscapeWidthFactor) {
        this.landscapeWidthFactor = landscapeWidthFactor;
        return this;
    }

    /**
     * 竖屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibCommon setPortraitWidthFactor(float portraitWidthFactor) {
        this.portraitWidthFactor = portraitWidthFactor;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibCommon setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibCommon setTitle(@StringRes int strId) {
        this.title = getContext().getString(strId);
        return this;
    }

    private String getMessage() {
        if (TextUtils.isEmpty(message)) {
            return "";
        }
        return message;
    }

    /**
     * 设置提示信息的内容部分，默认为“”(空字符串)
     */
    public DialogLibCommon setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置提示信息的内容部分，默认为“”(空字符串)
     */
    public DialogLibCommon setMessage(@StringRes int strId) {
        this.message = getContext().getString(strId);
        return this;
    }

    private String getOkDesc() {
        if (TextUtils.isEmpty(okDesc)) {
            return getContext().getResources().getString(R.string.dialog_utils_lib_ok);
        }
        return okDesc;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibCommon setOkDesc(String okDesc) {
        this.okDesc = okDesc;
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibCommon setOkDesc(@StringRes int strId) {
        this.okDesc = getContext().getString(strId);
        return this;
    }

    private String getCancelDesc() {
        if (TextUtils.isEmpty(cancelDesc)) {
            return getContext().getResources().getString(R.string.dialog_utils_lib_cancel);
        }
        return cancelDesc;
    }

    public Integer getMessageGravity() {
        if (null == messageGravity) {
            messageGravity = Gravity.CENTER;
        }
        return messageGravity;
    }

    /**
     * 设置消息区域的对齐方式，默认居中显示
     * 参考 {@link android.view.Gravity}
     */
    public DialogLibCommon setMessageGravity(Integer messageGravity) {
        this.messageGravity = messageGravity;
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibCommon setCancelDesc(String cancelDesc) {
        this.cancelDesc = cancelDesc;
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibCommon setCancelDesc(@StringRes int strId) {
        this.cancelDesc = getContext().getString(strId);
        return this;
    }

    private boolean isNoShowOk() {
        return noShowOk;
    }

    private boolean isNoShowCancel() {
        return noShowCancel;
    }

    private OnBtnOk getOnBtnOk() {
        if (null == onBtnOk) {
            onBtnOk = () -> {
            };
        }
        return onBtnOk;
    }

    /**
     * 设置确认按钮的事件触发
     * 只能用在默认dialog的OK按钮触发返回
     */
    public DialogLibCommon setOnBtnOk(OnBtnOk onBtnOk) {
        this.onBtnOk = onBtnOk;
        return this;
    }

    private OnBtnCancel getOnBtnCancel() {
        if (null == onBtnCancel) {
            onBtnCancel = () -> {
            };
        }
        return onBtnCancel;
    }

    /**
     * 设置取消按钮的事件触发
     */
    public DialogLibCommon setOnBtnCancel(OnBtnCancel onBtnCancel) {
        this.onBtnCancel = onBtnCancel;
        return this;
    }

    private OnBtn getOnBtn() {
        if (null == onBtn) {
            onBtn = () -> {
            };
        }
        return onBtn;
    }

    /**
     * 设置确认和取消按钮的事件触发时均会触发的事件
     */
    public DialogLibCommon setOnBtn(OnBtn onBtn) {
        this.onBtn = onBtn;
        return this;
    }

    private String getAlias() {
        return alias;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibCommon setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 设置内容提示区域的点击事件，不设置或为null则内容提示区域无点击效果
     */
    public DialogLibCommon setOnBtnMessage(OnBtnMessage onBtnMessage) {
        this.onBtnMessage = onBtnMessage;
        return this;
    }

    /**
     * 设置是否不显示确认按钮位置，对应确认按钮事件也无法触发
     * 与{@link DialogLibCommon#noShowCancel()} 同时设置时，只能通过点击提示框外的其他区域关闭（存在按钮时无法点击提示框外的其他区域关闭），且无法触发任何事件回调
     */
    public DialogLibCommon noShowOk() {
        this.noShowOk = true;
        return this;
    }

    /**
     * 设置是否不显示取消按钮位置，对应取消按钮事件也无法触发
     * 与{@link DialogLibCommon#noShowOk()} 同时设置时，只能通过点击提示框外的其他区域关闭（存在按钮时无法点击提示框外的其他区域关闭），且无法触发任何事件回调
     */
    public DialogLibCommon noShowCancel() {
        this.noShowCancel = true;
        return this;
    }

    /**
     * 设置因activity生命周期结束而关闭对话框时，触发的回调
     */
    public DialogLibCommon setOnActivityLifecycleClose(OnActivityLifecycleClose onActivityLifecycleClose) {
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
     * 显示提示信息的对话框，根据链式写法传递参数决定显示
     */
    public DialogLibCommon show() {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibCommon obj = MAP.get(getAlias());
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

            DialogUtilsLibTipBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_utils_lib_tip, null, false);
            binding.setReverseButton(DialogLibInitSetting.getInstance().isReverseButton());
            binding.setClick(v -> {
                try {
                    //关闭对话框
                    closeDialog();
                    //任何按钮都会触发
                    getOnBtn().btn();
                    if (v.equals(binding.btnOk1) || v.equals(binding.btnOk2)) {
                        //ok按钮位置触发
                        getOnBtnOk().ok();
                    } else if (v.equals(binding.btnCancel1) || v.equals(binding.btnCancel2)) {
                        //cancel按钮位置触发
                        getOnBtnCancel().cancel();
                    }
                } catch (Exception e) {
                    if (DialogLibInitSetting.getInstance().isDebug()) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            });
            binding.setTitle(getTitle());

            //消息区域的对齐方式
            binding.messText.setGravity(getMessageGravity());

            binding.messText.setMovementMethod(ScrollingMovementMethod.getInstance());
            binding.setMessage(getMessage());
            binding.setOkDesc(getOkDesc());
            binding.setCancelDesc(getCancelDesc());
            binding.setNoShowOk(isNoShowOk());
            binding.setNoShowCancel(isNoShowCancel());

            if (null != onBtnMessage) {
                binding.messText.setOnClickListener(v -> onBtnMessage.btn());
            }

            //ContentView
            dialog.setContentView(binding.getRoot());
            //2个按钮都不显示时，则允许点击其他位置关闭
            dialog.setCancelable((isNoShowOk() && isNoShowCancel()));
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

    public interface OnBtnOk {
        void ok();
    }

    public interface OnBtnCancel {
        void cancel();
    }

    public interface OnBtn {
        void btn();
    }

    public interface OnBtnMessage {
        void btn();
    }

    public interface OnActivityLifecycleClose {
        void close();
    }
}
