package com.osard.dialogfragmentutilslib.init;

import com.osard.dialogfragmentutilslib.BuildConfig;
import com.osard.dialogfragmentutilslib.DialogLibCommon;
import com.osard.dialogfragmentutilslib.DialogLibCustom;
import com.osard.dialogfragmentutilslib.DialogLibInput;


/**
 * 对话框工具初始化设置
 */
public class DialogLibInitSetting {

    private static final DialogLibInitSetting INSTANCE = new DialogLibInitSetting();

    public static DialogLibInitSetting getInstance() {
        return INSTANCE;
    }

    DialogLibInitSetting() {
        this.debug = BuildConfig.DEBUG;
    }

    private boolean debug;
    private boolean reverseButton;

    public boolean isDebug() {
        return debug;
    }

    public boolean isReverseButton() {
        return reverseButton;
    }

    /**
     * 设置debug模式，设置后强制改变模式，debug模式下控制台会打印一些日志
     * <p>
     * 默认情况下
     * 1. 使用debug版本aar时默认为true。
     * 2. 使用release版本aar时默认为false。
     */
    public DialogLibInitSetting setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    /**
     * 设置是否反转确定和取消按钮位置
     * <p>
     * 适用于 {@link DialogLibCommon}、{@link DialogLibCustom}、{@link DialogLibInput} 这3种对话框的统一布局设定
     * <p>
     * 默认为false
     * <p>
     * false：左确定，右取消
     * true：左取消，右确定
     */
    public DialogLibInitSetting setReverseButton(boolean reverseButton) {
        this.reverseButton = reverseButton;
        return this;
    }

}
