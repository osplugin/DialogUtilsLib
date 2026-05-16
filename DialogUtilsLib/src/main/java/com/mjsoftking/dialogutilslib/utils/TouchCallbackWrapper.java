package com.mjsoftking.dialogutilslib.utils;

import android.os.Build;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TouchCallbackWrapper implements Window.Callback {
    private final Window.Callback origin;
    private final Runnable onTouch;

    public TouchCallbackWrapper(Window.Callback origin, Runnable onTouch) {
        this.origin = origin;
        this.onTouch = onTouch;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (null != onTouch && event.getAction() == KeyEvent.ACTION_DOWN) {
            onTouch.run();
        }
        return origin.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return origin.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null != onTouch && event.getAction() == MotionEvent.ACTION_DOWN) {
            onTouch.run();
        }
        return origin.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return origin.dispatchTrackballEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return origin.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return origin.dispatchPopulateAccessibilityEvent(event);
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        return origin.onCreatePanelView(featureId);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        return origin.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, @Nullable View view, @NonNull Menu menu) {
        return origin.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, @NonNull Menu menu) {
        return origin.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        return origin.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        origin.onWindowAttributesChanged(attrs);
    }

    @Override
    public void onContentChanged() {
        origin.onContentChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        origin.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onAttachedToWindow() {
        origin.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        origin.onDetachedFromWindow();
    }

    @Override
    public void onPanelClosed(int featureId, @NonNull Menu menu) {
        origin.onPanelClosed(featureId, menu);
    }

    @Override
    public boolean onSearchRequested() {
        return origin.onSearchRequested();
    }

    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return origin.onSearchRequested(searchEvent);
        }
        return false;
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return origin.onWindowStartingActionMode(callback);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return origin.onWindowStartingActionMode(callback, type);
        }
        return null;
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        origin.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        origin.onActionModeFinished(mode);
    }
}
