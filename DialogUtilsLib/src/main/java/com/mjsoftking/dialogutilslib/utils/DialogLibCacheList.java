package com.mjsoftking.dialogutilslib.utils;

import android.app.Activity;
import android.content.Context;

import com.mjsoftking.dialogutilslib.DialogLibUtils;
import com.mjsoftking.dialogutilslib.bean.DialogLibBean;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * activity下使用的dialog对应缓存列表
 * <p>
 * 1. 创建dialog时加入缓存；
 * 2. 关闭时移除；
 * 3. 依附的activity销毁时关闭所有依附此activity的dialog
 */
public class DialogLibCacheList extends CopyOnWriteArrayList<DialogLibBean> {

    private static final DialogLibCacheList INSTANCE = new DialogLibCacheList();

    public static DialogLibCacheList getInstance() {
        return INSTANCE;
    }

    DialogLibCacheList() {
    }

    public boolean add(Context context, DialogLibUtils lib) {
        if (context instanceof Activity) {
            DialogLibBean obj = new DialogLibBean(((Activity) context).getComponentName().getClassName(),
                    lib);
            return add(obj);
        }
        return false;
    }

    public boolean remove(Context context, DialogLibUtils lib) {
        if (context instanceof Activity) {
            DialogLibBean obj = new DialogLibBean(((Activity) context).getComponentName().getClassName(),
                    lib);
            return remove(obj);
        }
        return false;
    }

}
