package com.osard.dialogfragmentutilslib;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.osard.dialogfragmentutilslib.init.DialogLibInitSetting;
import com.osard.dialogfragmentutilslib.utils.DialogLibCacheList;

import java.util.HashMap;
import java.util.Map;

/**
 * 全部自定义弹窗提示工具类，需要完全定义dialog的布局
 */
public class DialogLibAllCustom extends BaseDialogLibUtils {
    private final static String TAG = DialogLibAllCustom.class.getSimpleName();
    private final static Map<String, DialogLibAllCustom> MAP = new HashMap<>();

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

    private OnActivityLifecycleClose onActivityLifecycleClose;

    private View customView;

    private void setContext(Context context) {
        this.context = context;
    }

    private String getAlias() {
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

    /**
     * 设置因activity生命周期结束而关闭对话框时，触发的回调
     */
    public DialogLibAllCustom setOnActivityLifecycleClose(OnActivityLifecycleClose onActivityLifecycleClose) {
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
     * 设置自定义视图
     */
    public DialogLibAllCustom setCustomView(View customView) {
        this.customView = customView;
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

    public void setDialogWidth(Configuration configuration) {
//        setDialogWidth(TAG, dialog, configuration);
    }

    public boolean closeDialog() {
        return closeDialog(true);
    }

    private boolean closeDialog(boolean remove) {
        try {
            if (isVisible()) {
                dismiss();
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

    public interface OnActivityLifecycleClose {
        void close();
    }
}
