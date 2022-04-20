package com.mjsoftking.dialogutilslib;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;

import com.mjsoftking.dialogutilslib.init.DialogLibInitSetting;

public class PopupWindowLib {

    private static final String TAG = PopupWindowLib.class.getSimpleName();

    private PopupWindow popupWindow;
    private View mContentView;
    private int mWidth;
    private int mHeight;
    private boolean mOutsideTouchable;
    private boolean mAttachedInDecor;
    private boolean mFocusable;
    private long mAutoCloseTime;

    //自动关闭的Handler和处理对象
    private Handler autoCloseHandler;
    private Runnable autoCloseRunnable = this::dismiss;

    /**
     * 创建对象
     */
    public static PopupWindowLib create() {
        return new PopupWindowLib();
    }

    private PopupWindowLib() {
    }

    /**
     * 设置内容布局视图
     *
     * @param contentView 目标视图，可修改视图内的内容达到改变显示，但不能随意更改设定的宽高
     * @param width       WindowManager.LayoutParams.WRAP_CONTENT 、 WindowManager.LayoutParams.MATCH_PARENT 或 具体大小
     * @param height      WindowManager.LayoutParams.WRAP_CONTENT 、 WindowManager.LayoutParams.MATCH_PARENT 或 具体大小
     */
    public PopupWindowLib setContentView(View contentView, int width, int height) {
        mContentView = contentView;
        mWidth = width;
        mHeight = height;
        return this;
    }

    /**
     * 触摸PopupWindow之外时是否关闭
     */
    public PopupWindowLib setOutsideTouchable(boolean touchable) {
        mOutsideTouchable = touchable;
        return this;
    }

    /**
     * 这将把弹出窗口附加到父窗口的装饰框架上，以避免与导航栏等屏幕装饰重叠。
     * API >= 19 后可用
     */
    public PopupWindowLib setAttachedInDecor(boolean enabled) {
        mAttachedInDecor = enabled;
        return this;
    }

    /**
     * PopupWindow 是否可以获取焦点
     */
    public PopupWindowLib setFocusable(boolean focusable) {
        mFocusable = focusable;
        return this;
    }

    /**
     * PopupWindow 弹出后的自动关闭时间
     *
     * @param millisecond 单位：毫秒，<=0时不自动关闭
     */
    public PopupWindowLib setAutoCloseTime(long millisecond) {
        mAutoCloseTime = millisecond;
        return this;
    }

    /**
     * 在锚点视图左下角的弹出窗口中显示内容视图。
     *
     * @param anchor 锚点视图
     */
    public PopupWindowLib showAsDropDown(View anchor) {
        createPopupWindow();
        popupWindow.showAsDropDown(anchor);
        autoClose();
        return this;
    }

    /**
     * 在锚点视图左下角的弹出窗口中显示内容视图。
     *
     * @param anchor 锚点视图
     * @param xoff   X轴偏移
     * @param yoff   Y轴偏移
     */
    public PopupWindowLib showAsDropDown(View anchor, int xoff, int yoff) {
        createPopupWindow();
        popupWindow.showAsDropDown(anchor, xoff, yoff);
        autoClose();
        return this;
    }

    /**
     * 在锚点视图左下角的弹出窗口中显示内容视图。
     *
     * @param anchor  锚点视图
     * @param xoff    X轴偏移
     * @param yoff    Y轴偏移
     * @param gravity 相对于锚点对齐弹出
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PopupWindowLib showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        createPopupWindow();
        popupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
        autoClose();
        return this;
    }

    /**
     * 在指定位置的弹出窗口中显示内容视图。
     *
     * @param parent  父视图
     * @param gravity 对齐方式
     * @param x       X轴偏移
     * @param y       Y轴偏移
     */
    public PopupWindowLib showAtLocation(View parent, int gravity, int x, int y) {
        createPopupWindow();
        popupWindow.showAtLocation(parent, gravity, x, y);
        autoClose();
        return this;
    }

    void createPopupWindow() {
        try {
            if (null == popupWindow) {
                popupWindow = new PopupWindow();
            } else {
                popupWindow.dismiss();
            }
            popupWindow.setContentView(mContentView);
            popupWindow.setWidth(mWidth < -2 ? WindowManager.LayoutParams.WRAP_CONTENT : mWidth);
            popupWindow.setHeight(mHeight < -2 ? WindowManager.LayoutParams.WRAP_CONTENT : mHeight);
            popupWindow.setOutsideTouchable(mOutsideTouchable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                popupWindow.setAttachedInDecor(mAttachedInDecor);
            }
            popupWindow.setFocusable(mFocusable);
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.e(TAG, "创建PopupWindow时出现错误", e);
            }
        }
    }

    synchronized void autoClose() {
        try {
            if (autoCloseHandler == null) {
                autoCloseHandler = new Handler(Looper.getMainLooper());
            }
            if (mAutoCloseTime > 0) {
                autoCloseHandler.removeCallbacks(autoCloseRunnable);
                autoCloseHandler.postDelayed(autoCloseRunnable, mAutoCloseTime);
            }
        } catch (Exception e) {
            if (DialogLibInitSetting.getInstance().isDebug()) {
                Log.e(TAG, "自动关闭PopupWindow时出现错误", e);
            }
        }
    }

    /**
     * 是否正在显示
     */
    public boolean isShowing() {
        if (null != popupWindow) {
            return popupWindow.isShowing();
        }
        return false;
    }

    /**
     * 关闭
     */
    public void dismiss() {
        if (null != popupWindow) {
            popupWindow.dismiss();
        }
    }

    public void update() {
        if (null != popupWindow) {
            popupWindow.update();
        }
    }

    public void update(int width, int height) {
        if (null != popupWindow) {
            popupWindow.update(width, height);
        }
    }

    public void update(int x, int y, int width, int height) {
        if (null != popupWindow) {
            popupWindow.update(x, y, width, height);
        }
    }

    public void update(int x, int y, int width, int height, boolean force) {
        if (null != popupWindow) {
            popupWindow.update(x, y, width, height, force);
        }
    }

    public void update(View anchor, int width, int height) {
        if (null != popupWindow) {
            popupWindow.update(anchor, width, height);
        }
    }

    public void update(View anchor, int xoff, int yoff, int width, int height) {
        if (null != popupWindow) {
            popupWindow.update(anchor, xoff, yoff, width, height);
        }
    }

}
