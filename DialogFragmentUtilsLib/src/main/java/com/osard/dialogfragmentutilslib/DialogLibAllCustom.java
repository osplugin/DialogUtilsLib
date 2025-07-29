package com.osard.dialogfragmentutilslib;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.osard.dialogfragmentutilslib.init.DialogLibInitSetting;

import java.util.UUID;

/**
 * 全部自定义弹窗提示工具类，需要完全定义dialog的布局
 */
public class DialogLibAllCustom extends BaseDialogLibUtils {
    private final static String TAG = DialogLibAllCustom.class.getSimpleName();

    /**
     * 创建对象
     */
    public static DialogLibAllCustom create(Context context) {
        DialogLibAllCustom utils = new DialogLibAllCustom();
        utils.setContext(context);
        return utils;
    }

    private DialogLibAllCustom() {
    }

    //别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
    private String alias;

    private View customView;

    private void setContext(Context context) {
        this.context = context;
    }

    private String getAlias() {
        if (TextUtils.isEmpty(alias)) {
            alias = UUID.randomUUID().toString();
        }
        return alias;
    }


    /**
     * 横屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibAllCustom setLandscapeWidthFactor(float landscapeWidthFactor) {
        this.landscapeWidthFactor = landscapeWidthFactor;
        return this;
    }

    /**
     * 竖屏时dialog占屏幕宽度的百分比系数，0-1之间有效，不含边界
     */
    public DialogLibAllCustom setPortraitWidthFactor(float portraitWidthFactor) {
        this.portraitWidthFactor = portraitWidthFactor;
        return this;
    }

    /**
     * 是否允许点击其他位置关闭
     */
    public DialogLibAllCustom setCancelableDialog(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    /**
     * 别名，同一个别名的对话框同一时间只能弹出一个，在show时如果存在未关闭的对话框则直接返回原本对象
     * <p>
     * null、空字符串 无效
     */
    public DialogLibAllCustom setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return customView;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        closeDialog();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setDialogWidth(TAG, getDialog(), newConfig);
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogWidth(TAG, getDialog(), context.getResources().getConfiguration());
    }

    public DialogLibAllCustom show(View customView) {
        this.customView = customView;
        show(((FragmentActivity) context).getSupportFragmentManager(), getAlias());
        return this;
    }

    public boolean closeDialog() {
        try {
            dismiss();
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.w(TAG, "关闭对话框异常", e);
            }
        }
        return true;
    }

}
