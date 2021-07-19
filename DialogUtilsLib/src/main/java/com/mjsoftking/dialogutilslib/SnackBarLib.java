package com.mjsoftking.dialogutilslib;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/**
 * 用途：{@link Snackbar} 的封装
 * <p>
 * 作者：MJSoftKing
 */
public class SnackBarLib {

    private Snackbar snackbar;
    private View view;
    private String content;
    @BaseTransientBottomBar.Duration
    private int duration;
//    private Context context;

    public SnackBarLib(View view, String content, @BaseTransientBottomBar.Duration int duration) {
        this.view = view;
//        this.context = view.getContext();
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
    public static SnackBarLib makeCustom(@NonNull View view, @NonNull String content,
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
    public static SnackBarLib makeCustom(@NonNull View view, @StringRes int content,
                                         @BaseTransientBottomBar.Duration int duration) {
        return makeCustom(view, view.getContext().getString(content), duration);
    }

    /**
     * 显示
     *
     * @return 返回原对象，可以控制直接关闭
     */
    public SnackBarLib show() {
        snackbar = Snackbar.make(view, content, duration);
        View view = snackbar.getView();
        view.setBackgroundResource(R.color.dialog_utils_lib_common_ok_fg);




        //事件
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
            }

            @Override
            public void onShown(Snackbar transientBottomBar) {
                super.onShown(transientBottomBar);
            }
        });

//        snackbar.setAction("点击查看", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "TODO 查看消息", Toast.LENGTH_SHORT).show();
//            }
//        });


        snackbar.show();
        return this;
    }

    /**
     * 如果正在显示则关闭。
     */
    public void dismiss() {
        if (null != snackbar && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

}
