package com.osard.dialogfragmentutilslib;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/**
 * 用途：{@link Snackbar} 的封装，增加了文字前的图片显示
 * <p>
 * 作者：MJSoftKing
 */
public class SnackBarLib {

    private static final String TAG = SnackBarLib.class.getSimpleName();

    private static Snackbar snackbar;
    private View view;
    private String content;
    @BaseTransientBottomBar.Duration
    private int duration;
    private Context context;
    @DrawableRes
    private int viewBackground;
    @DrawableRes
    private int imgRes;
    @ColorRes
    private int contentColor;
    private String actionStr;
    private View.OnClickListener listener;
    @ColorRes
    private int actionTextColor;

    private int imageWH;

    private int contentMaxLine;

    private Object tag;

    private IShow showCallback;

    private ISwipe swipeCallback;
    private IActionClick actionClickCallback;
    private ITimeout timeoutCallback;
    private IManual manualCallback;
    private IConsecutive consecutiveCallback;

    //默认的监听方法
    private final BaseTransientBottomBar.BaseCallback<Snackbar> DEFAULT_CALLBACK =
            new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    //移除监听
                    if (null != transientBottomBar) {
                        transientBottomBar.removeCallback(DEFAULT_CALLBACK);
                    }
                    //在此处主要负责清除static的引用关系
                    snackbar = null;

                    super.onDismissed(transientBottomBar, event);

                    switch (event) {
                        case DISMISS_EVENT_SWIPE:
                            if (null != swipeCallback) {
                                swipeCallback.fun(tag);
                            }
                            break;
                        case DISMISS_EVENT_ACTION:
                            if (null != actionClickCallback) {
                                actionClickCallback.fun(tag);
                            }
                            break;
                        case DISMISS_EVENT_TIMEOUT:
                            if (null != timeoutCallback) {
                                timeoutCallback.fun(tag);
                            }
                            break;
                        case DISMISS_EVENT_MANUAL:
                            if (null != manualCallback) {
                                manualCallback.fun(tag);
                            }
                            break;
                        case DISMISS_EVENT_CONSECUTIVE:
                            if (null != consecutiveCallback) {
                                consecutiveCallback.fun(tag);
                            }
                            break;
                    }
                }

                @Override
                public void onShown(Snackbar transientBottomBar) {
                    super.onShown(transientBottomBar);
                    //在此处赋值，确保static的对象记录留存最后显示的Snackbar
                    snackbar = transientBottomBar;

                    if (null != showCallback) {
                        showCallback.fun(tag);
                    }
                }
            };


    public SnackBarLib(View view, String content, @BaseTransientBottomBar.Duration int duration) {
        this.view = view;
        this.context = view.getContext();
        this.content = content;
        this.duration = duration;
    }

    /**
     * 一直显示，只有右滑或者点击事件以后，可以移除
     *
     * @param view    父布局
     * @param content 内容
     */
    public static SnackBarLib makeIndefinite(@NonNull View view, @NonNull String content) {
        return new SnackBarLib(view, content, Snackbar.LENGTH_INDEFINITE);
    }

    /**
     * 一直显示，只有右滑或者点击事件以后，可以移除
     *
     * @param view    父布局
     * @param content 内容
     */
    public static SnackBarLib makeIndefinite(@NonNull View view, @StringRes int content) {
        return makeIndefinite(view, view.getContext().getString(content));
    }

    /**
     * 短时间显示
     *
     * @param view    父布局
     * @param content 内容
     */
    public static SnackBarLib makeShort(@NonNull View view, @NonNull String content) {
        return new SnackBarLib(view, content, Snackbar.LENGTH_SHORT);
    }

    /**
     * 短时间显示
     *
     * @param view    父布局
     * @param content 内容
     */
    public static SnackBarLib makeShort(@NonNull View view, @StringRes int content) {
        return makeShort(view, view.getContext().getString(content));
    }

    /**
     * 长时间显示
     *
     * @param view    父布局
     * @param content 内容
     */
    public static SnackBarLib makeLong(@NonNull View view, @NonNull String content) {
        return new SnackBarLib(view, content, Snackbar.LENGTH_LONG);
    }

    /**
     * 长时间显示
     *
     * @param view    父布局
     * @param content 内容
     */
    public static SnackBarLib makeLong(@NonNull View view, @StringRes int content) {
        return makeLong(view, view.getContext().getString(content));
    }

    /**
     * 自定义显示时间，从1开始
     *
     * @param view     父布局
     * @param content  内容
     * @param duration 时间，单位：毫秒
     */
    public static SnackBarLib make(@NonNull View view, @NonNull String content,
                                         @BaseTransientBottomBar.Duration int duration) {
        return new SnackBarLib(view, content, duration);
    }

    /**
     * 自定义显示时间，从1开始
     *
     * @param view     父布局
     * @param content  内容
     * @param duration 时间，单位：毫秒
     */
    public static SnackBarLib make(@NonNull View view, @StringRes int content,
                                         @BaseTransientBottomBar.Duration int duration) {
        return make(view, view.getContext().getString(content), duration);
    }

    /**
     * 如果正在显示则关闭。
     */
    public static void dismiss() {
        if (null != snackbar && snackbar.isShown()) {
            snackbar.dismiss();
        }
        snackbar = null;
    }

    private int getViewBackground() {
        if (viewBackground == 0) {
            viewBackground = R.drawable.snackbar_lib_bg;
        }
        return viewBackground;
    }

    /**
     * 设置 SnackBar 的背景
     */
    public SnackBarLib setViewBackground(@DrawableRes int viewBackground) {
        this.viewBackground = viewBackground;
        return this;
    }

    private int getActionTextColor() {
        if (actionTextColor == 0) {
            actionTextColor = R.color.snackbar_lib_action_fg;
        }
        return actionTextColor;
    }

    /**
     * 设置按钮文本颜色
     */
    public SnackBarLib setActionTextColor(int actionTextColor) {
        this.actionTextColor = actionTextColor;
        return this;
    }

    private int getContentColor() {
        if (contentColor == 0) {
            contentColor = R.color.snackbar_lib_content_fg;
        }
        return contentColor;
    }

    /**
     * 设置内容文本颜色
     */
    public SnackBarLib setContentColor(@ColorRes int contentColor) {
        this.contentColor = contentColor;
        return this;
    }

    /**
     * 设置 SnackBar 的图标内容
     */
    public SnackBarLib setImgRes(@DrawableRes int imgRes) {
        this.imgRes = imgRes;
        return this;
    }

    /**
     * 设置按钮文本及点击事件
     */
    @NonNull
    public SnackBarLib setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(context.getResources().getString(resId), listener);
    }

    /**
     * 设置按钮文本及点击事件
     */
    @NonNull
    public SnackBarLib setAction(
            @Nullable String text, @Nullable View.OnClickListener listener) {
        this.actionStr = text;
        this.listener = listener;
        return this;
    }

    /**
     * 设置扩展参数，在事件触发时返回，传入什么，返回什么
     */
    public SnackBarLib setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 设置图标的宽高，正方形，单位：dp
     */
    public SnackBarLib setImageWH(int imageWH) {
        this.imageWH = imageWH;
        return this;
    }

    /**
     * 设置内容区域的最大显示行数
     */
    public SnackBarLib setContentMaxLine(int contentMaxLine) {
        this.contentMaxLine = contentMaxLine;
        return this;
    }

    /**
     * 设置Snackbar显示时的回调
     */
    public SnackBarLib setShowCallback(IShow showCallback) {
        this.showCallback = showCallback;
        return this;
    }

    /**
     * 设置Snackbar滑动关闭时的回调
     */
    public SnackBarLib setSwipeCallback(ISwipe swipeCallback) {
        this.swipeCallback = swipeCallback;
        return this;
    }

    /**
     * 设置Snackbar action点击关闭时的回调
     */
    public SnackBarLib setActionClickCallback(IActionClick actionClickCallback) {
        this.actionClickCallback = actionClickCallback;
        return this;
    }

    /**
     * 设置Snackbar 超时正常关闭时的回调
     */
    public SnackBarLib setTimeoutCallback(ITimeout timeoutCallback) {
        this.timeoutCallback = timeoutCallback;
        return this;
    }

    /**
     * 设置Snackbar 手动关闭时的回调
     */
    public SnackBarLib setManualCallback(IManual manualCallback) {
        this.manualCallback = manualCallback;
        return this;
    }

    /**
     * 设置Snackbar连续弹出前一个关闭时的回调
     */
    public SnackBarLib setConsecutiveCallback(IConsecutive consecutiveCallback) {
        this.consecutiveCallback = consecutiveCallback;
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        Snackbar snackbar = Snackbar.make(view, content, duration);
        View view = snackbar.getView();
        ImageView imageView = view.findViewById(R.id.snackbar_img);
        TextView textView = view.findViewById(R.id.snackbar_text);

        //设置背景风格
        view.setBackgroundResource(getViewBackground());
        //设置小图标
        if (imgRes != 0) {
            imageView.setImageDrawable(context.getResources().getDrawable(imgRes));
            imageView.setVisibility(View.VISIBLE);

            //设置小图标后，设置的图标大小才会有效
            if (imageWH > 0) {
                int wh = dipToPX(imageWH);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = wh;
                params.height = wh;
                imageView.setLayoutParams(params);
            }
        }
        //设置内容文本颜色
        snackbar.setTextColor(context.getResources().getColor(getContentColor()));

        //设置按钮的文本及点击事件
        if (!TextUtils.isEmpty(actionStr) && null != listener) {
            snackbar.setAction(actionStr, listener);

            //设置按钮文本颜色
            snackbar.setActionTextColor(context.getResources().getColor(getActionTextColor()));
        }

        if (contentMaxLine > 0) {
            textView.setMaxLines(contentMaxLine);
        }

        //添加监听
        snackbar.addCallback(DEFAULT_CALLBACK);

        snackbar.show();
    }

    /**
     * 显示带 success 图标的模式
     * <p>
     * 使用此方法覆盖之前{@link SnackBarLib#setImgRes(int)}的设置，图标资源可覆盖替换
     */
    public void showSuccess() {
        setImgRes(R.mipmap.snackbar_lib_default_success).show();
    }

    /**
     * 显示带 Error 图标的模式
     * <p>
     * 使用此方法覆盖之前{@link SnackBarLib#setImgRes(int)}的设置，图标资源可覆盖替换
     */
    public void showError() {
        setImgRes(R.mipmap.snackbar_lib_default_error).show();
    }

    /**
     * 显示带 Info 图标的模式
     * <p>
     * 使用此方法覆盖之前{@link SnackBarLib#setImgRes(int)}的设置，图标资源可覆盖替换
     */
    public void showInfo() {
        setImgRes(R.mipmap.snackbar_lib_default_info).show();
    }

    /**
     * 显示带 Warning 图标的模式
     * <p>
     * 使用此方法覆盖之前{@link SnackBarLib#setImgRes(int)}的设置，图标资源可覆盖替换
     */
    public void showWarning() {
        setImgRes(R.mipmap.snackbar_lib_default_warn).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dipToPX(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 显示时
     */
    public interface IShow {
        void fun(Object tag);
    }

    /**
     * 滑动关闭触发
     */
    public interface ISwipe {
        void fun(Object tag);
    }

    /**
     * 点击action后触发
     */
    public interface IActionClick {
        void fun(Object tag);
    }

    /**
     * 时间到了触发
     */
    public interface ITimeout {
        void fun(Object tag);
    }

    /**
     * 手动关闭触发
     */
    public interface IManual {
        void fun(Object tag);
    }

    /**
     * 连续弹出时关闭前一个触发
     */
    public interface IConsecutive {
        void fun(Object tag);
    }

}
