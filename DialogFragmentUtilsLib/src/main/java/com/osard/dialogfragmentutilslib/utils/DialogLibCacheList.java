package com.osard.dialogfragmentutilslib.utils;

import android.app.Activity;
import android.content.Context;

import com.osard.dialogfragmentutilslib.DialogLibUtils;
import com.osard.dialogfragmentutilslib.bean.DialogLibBean;
import com.osard.dialogfragmentutilslib.init.DialogLibInitSetting;

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
        //没有注册生命周期时，无需缓存，以避免内存无法释放
        if (!DialogLibInitSetting.getInstance().isRegisterActivityLifecycle()) {
            return false;
        }

        if (context instanceof Activity) {
            DialogLibBean obj = new DialogLibBean(((Activity) context).getComponentName().getClassName(),
                    lib);
            return add(obj);
        }
        return false;
    }

    public boolean remove(Context context, DialogLibUtils lib) {
        //没有注册生命周期时，无需缓存，以避免内存无法释放，因此移除缓存也无需设置
        if (!DialogLibInitSetting.getInstance().isRegisterActivityLifecycle()) {
            return false;
        }

        if (context instanceof Activity) {
            DialogLibBean obj = new DialogLibBean(((Activity) context).getComponentName().getClassName(),
                    lib);
            return remove(obj);
        }
        return false;
    }

}
