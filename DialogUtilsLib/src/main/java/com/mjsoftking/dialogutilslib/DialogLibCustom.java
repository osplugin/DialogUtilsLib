package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilslib.databinding.DialogUtilsLibCustomViewBinding;
import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义弹窗提示工具类
 */
public class DialogLibCustom extends BaseDialogLibUtils {
    private final static String TAG = DialogLibCustom.class.getSimpleName();
    private final static Map<String, DialogLibCustom> MAP = new HashMap<>();

    private Dialog dialog;

    /**
     * 创建对象
     */
    public static DialogLibCustom create(Context context) {
        DialogLibCustom utils = new DialogLibCustom();
        utils.setContext(context);
        return utils;
    }

    private DialogLibCustom() {
    }

    //别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
    private String alias;
    private String title;
    private String okDesc;
    private String cancelDesc;
    private boolean noShowOk;
    private boolean noShowCancel;

    private OnCustomBtnOk onCustomBtnOk;
    private OnBtnCancel onBtnCancel;
    private OnBtn onBtn;
    private OnActivityLifecycleClose onActivityLifecycleClose;

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

    private String getAlias() {
        return alias;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibCustom setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 设置自定义视图触发ok按钮时的响应
     * 接口返回true关闭对话框，反之不关闭
     */
    public DialogLibCustom setOnCustomBtnOk(OnCustomBtnOk onCustomBtnOk) {
        this.onCustomBtnOk = onCustomBtnOk;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibCustom setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibCustom setTitle(@StringRes int strId) {
        this.title = getContext().getString(strId);
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibCustom setOkDesc(String okDesc) {
        this.okDesc = okDesc;
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibCustom setOkDesc(@StringRes int strId) {
        this.okDesc = getContext().getString(strId);
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibCustom setCancelDesc(String cancelDesc) {
        this.cancelDesc = cancelDesc;
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibCustom setCancelDesc(@StringRes int strId) {
        this.cancelDesc = getContext().getString(strId);
        return this;
    }


    /**
     * 设置是否不显示确认按钮位置，对应确认按钮事件也无法触发
     * 与{@link DialogLibCustom#noShowCancel()} 同时设置时，只能通过点击提示框外的其他区域关闭（存在按钮时无法点击提示框外的其他区域关闭），且无法触发任何事件回调
     */
    public DialogLibCustom noShowOk() {
        this.noShowOk = true;
        return this;
    }

    /**
     * 设置是否不显示取消按钮位置，对应取消按钮事件也无法触发
     * 与{@link DialogLibCustom#noShowOk()} 同时设置时，只能通过点击提示框外的其他区域关闭（存在按钮时无法点击提示框外的其他区域关闭），且无法触发任何事件回调
     */
    public DialogLibCustom noShowCancel() {
        this.noShowCancel = true;
        return this;
    }

    /**
     * 设置取消按钮的事件触发
     */
    public DialogLibCustom setOnBtnCancel(OnBtnCancel onBtnCancel) {
        this.onBtnCancel = onBtnCancel;
        return this;
    }

    /**
     * 设置是否反转确定和取消按钮位置
     * <p>
     * false：左确定，右取消，全局设置无效
     * true：左取消，右确定，全局设置无效
     * null：使用全局设置
     */
    public DialogLibCustom setReverseButton(Boolean reverseButton) {
        this.reverseButton = reverseButton;
        return this;
    }

    /**
     * 设置确认和取消按钮的事件触发时均会触发的事件
     */
    public DialogLibCustom setOnBtn(OnBtn onBtn) {
        this.onBtn = onBtn;
        return this;
    }

    /**
     * 横屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibCustom setLandscapeWidthFactor(float landscapeWidthFactor) {
        this.landscapeWidthFactor = landscapeWidthFactor;
        return this;
    }

    /**
     * 竖屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibCustom setPortraitWidthFactor(float portraitWidthFactor) {
        this.portraitWidthFactor = portraitWidthFactor;
        return this;
    }

    /**
     * 设置因activity生命周期结束而关闭对话框时，触发的回调
     */
    public DialogLibCustom setOnActivityLifecycleClose(OnActivityLifecycleClose onActivityLifecycleClose) {
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
    public DialogLibCustom show(View view) {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibCustom obj = MAP.get(getAlias());
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
            //ContentView
            DialogUtilsLibCustomViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_utils_lib_custom_view, null, false);
            binding.setReverseButton(this.reverseButton == null ?
                    DialogLibInitSetting.getInstance().isReverseButton() :
                    this.reverseButton);
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
                    if (v.equals(binding.btnOk1) || v.equals(binding.btnOk2)) {
                        //ok按钮位置触发
                        if (getOnCustomBtnOk().ok()) {
                            closeDialog();
                        }
                    } else if (v.equals(binding.btnCancel1) || v.equals(binding.btnCancel2)) {
                        closeDialog();
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

    public interface OnCustomBtnOk {
        boolean ok();
    }

    public interface OnBtnCancel {
        void cancel();
    }

    public interface OnBtn {
        void btn();
    }

    public interface OnActivityLifecycleClose {
        void close();
    }
}
