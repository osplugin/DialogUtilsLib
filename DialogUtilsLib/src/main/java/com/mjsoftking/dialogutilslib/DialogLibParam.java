package com.mjsoftking.dialogutilslib;

/**
 * 常规弹窗提示工具类的常量类
 */
public class DialogLibParam {

    private static final DialogLibParam INSTANCE = new DialogLibParam();

    public static DialogLibParam getInstance() {
        return INSTANCE;
    }

    DialogLibParam() {
        this.debug = BuildConfig.DEBUG;
    }

    private boolean debug;

    public boolean isDebug() {
        return debug;
    }

    /**
     * 设置debug模式
     * <p>
     * 1. 使用debug版本aar时默认为true。
     * 2. 使用release版本aar时默认为false。
     */
    public DialogLibParam setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }
}
