package com.mjsoftking.dialogutilslib;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.dialogutilslib.databinding.DialogUtilsLibTipInputBinding;
import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;
import com.mjsoftking.dialogutilslib.utils.DialogLibCacheList;

import java.util.HashMap;
import java.util.Map;

/**
 * 输入型弹窗提示工具类
 */
public class DialogLibInput implements DialogLibUtils {
    private final static String TAG = DialogLibInput.class.getSimpleName();
    private final static Map<String, DialogLibInput> MAP = new HashMap<>();

    private Dialog dialog;

    /**
     * 创建对象
     */
    public static DialogLibInput create(Context context) {
        DialogLibInput utils = new DialogLibInput();
        utils.setContext(context);
        return utils;
    }

    private DialogLibInput() {
    }

    private Context context;
    //别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
    private String alias;
    private String title;
    private String message;
    private String hint;
    @ColorRes
    private Integer hintTextColor;
    private String okDesc;
    private String cancelDesc;
    private KeyListener keyListener;
    private Integer inputType;
    private Integer length;
    private boolean popupKeyboard;
    private boolean showLookPassword;

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

    private boolean isPopupKeyboard() {
        return popupKeyboard;
    }

    private String getHint() {
        if (TextUtils.isEmpty(hint)) {
            return "";
        }
        return hint;
    }

    private boolean isShowLookPassword() {
        return showLookPassword;
    }

    /**
     * 显示查看/隐藏密码图标组件
     * <p>
     * 建议在使用密码输入时启用
     */
    public DialogLibInput setShowLookPassword() {
        this.showLookPassword = true;
        return this;
    }

    //    private Integer getHintTextColor() {
//        return hintTextColor;
//    }
//
//    public DialogLibInput setHintTextColor(int hintTextColor) {
//        this.hintTextColor = hintTextColor;
//        return this;
//    }

    /**
     * 设置提示信息的hint部分，默认为“”(空字符串)
     */
    public DialogLibInput setHint(@StringRes int hintId) {
        this.hint = getContext().getString(hintId);
        return this;
    }

    /**
     * 设置提示信息的hint部分，默认为“”(空字符串)
     */
    public DialogLibInput setHint(String hint) {
        this.hint = hint;
        return this;
    }

    /**
     * 是否弹出键盘，默认不弹出，弹出键盘时默认光标定位到输入框位置
     */
    public DialogLibInput setPopupKeyboard() {
        this.popupKeyboard = true;
        return this;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibInput setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 设置字符长度
     */
    public DialogLibInput setLength(Integer length) {
        if (null != length && length > 0) {
            this.length = length;
        }
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibInput setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置标题显示信息，默认为“提示”
     */
    public DialogLibInput setTitle(@StringRes int strId) {
        this.title = getContext().getString(strId);
        return this;
    }

    /**
     * 设置提示信息的内容部分，默认为“”(空字符串)
     */
    public DialogLibInput setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 设置提示信息的内容部分，默认为“”(空字符串)
     */
    public DialogLibInput setMessage(@StringRes int strId) {
        this.message = getContext().getString(strId);
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibInput setOkDesc(String okDesc) {
        this.okDesc = okDesc;
        return this;
    }

    /**
     * 设置确认按钮位置的提示文字，默认为“确认”
     */
    public DialogLibInput setOkDesc(@StringRes int strId) {
        this.okDesc = getContext().getString(strId);
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibInput setCancelDesc(String cancelDesc) {
        this.cancelDesc = cancelDesc;
        return this;
    }

    /**
     * 设置取消按钮位置的提示文字，默认为“取消”
     */
    public DialogLibInput setCancelDesc(@StringRes int strId) {
        this.cancelDesc = getContext().getString(strId);
        return this;
    }

    /**
     * 设置确认按钮的事件触发
     * 此接口返回 true，则关闭对话框，反之持续显示不关闭
     */
    public DialogLibInput setOnBtnOk(OnBtnOk onBtnOk) {
        this.onBtnOk = onBtnOk;
        return this;
    }

    /**
     * 设置取消按钮的事件触发
     */
    public DialogLibInput setOnBtnCancel(OnBtnCancel onBtnCancel) {
        this.onBtnCancel = onBtnCancel;
        return this;
    }

    /**
     * 设置限定输入字符
     *
     * @param digits 限定字符，null或空字符则设置无效
     */
    public DialogLibInput setKeyListener(String digits) {
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
    public DialogLibInput setKeyListener(@StringRes int strId) {
        this.keyListener = DigitsKeyListener.getInstance(getContext().getString(strId));
        return this;
    }

    /**
     * 设置输入的类型
     *
     * @param inputType {@link EditorInfo#TYPE_TEXT_FLAG_MULTI_LINE}
     */
    public DialogLibInput setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    /**
     * 显示提示信息的对话框，根据链式写法传递参数决定显示
     */
    public DialogLibInput show() {
        if (!TextUtils.isEmpty(getAlias())) {
            DialogLibInput obj = MAP.get(getAlias());
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
                    //显示/隐藏密码
                    else if (v.equals(binding.lookPassword)) {
                        int index = binding.etContent.getSelectionStart();
                        Object obj = binding.lookPassword.getTag(R.integer.dialog_utils_lib_password_tag_key);
                        boolean status = true;
                        if (obj instanceof Boolean) {
                            status = (Boolean) obj;
                        }
                        if (status) {
                            //显示文本
                            binding.etContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.lookPassword.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.dialog_utils_lib_password_show));
                        } else {
                            //隐藏文本
                            binding.etContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.lookPassword.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.dialog_utils_lib_password_hide));
                        }
                        binding.lookPassword.setTag(R.integer.dialog_utils_lib_password_tag_key, !status);
                        if (index >= 0) {
                            binding.etContent.setSelection(index);
                        }
                    }
                } catch (Exception e) {
                    if (DialogLibInitSetting.getInstance().isDebug()) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            });
            binding.setTitle(getTitle());
            binding.setMessage(getMessage());
            binding.setHint(getHint());
            binding.setOkDesc(getOkDesc());
            binding.setCancelDesc(getCancelDesc());

            if (isPopupKeyboard()) {
                binding.etContent.post(() -> {
                    binding.etContent.setSelection(binding.etContent.length());
                    showSoftInput(binding.etContent);
                });
            } else {
                binding.etContent.post(() -> {
                    binding.etContent.setSelection(binding.etContent.length());
                });
            }
            if (null != getInputType()) {
                binding.etContent.setInputType(getInputType());

                //是密码输入类型，设置显示/隐藏密码图标才会有效
                if (isPasswordInputType()) {
                    binding.setShowLookPassword(isShowLookPassword());
                }
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

    private boolean isPasswordInputType() {
        final int variation =
                getInputType() & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        return variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
                || variation
                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
    }

    private void showSoftInput(@NonNull final View view) {
        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, 0, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                        || resultCode == InputMethodManager.RESULT_HIDDEN) {
                    imm.toggleSoftInput(0, 0);
                }
            }
        });
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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

    private float getLandscapeWidthFactor() {
        TypedValue outValue = new TypedValue();
        getContext().getResources().getValue(R.dimen.dialog_utils_lib_landscape_width_factor, outValue, true);
        float lwf = outValue.getFloat();
        if (lwf <= 0 || lwf >= 1) {
            return 0.5F;
        }
        return lwf;
    }

    private float getPortraitWidthFactor() {
        TypedValue outValue = new TypedValue();
        getContext().getResources().getValue(R.dimen.dialog_utils_lib_portrait_width_factor, outValue, true);
        float pwf = outValue.getFloat();
        if (pwf <= 0 || pwf >= 1) {
            return 0.85F;
        }
        return pwf;
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

    public interface OnBtnOk {
        boolean ok(String text);
    }

    public interface OnBtnCancel {
        void cancel();
    }

}
