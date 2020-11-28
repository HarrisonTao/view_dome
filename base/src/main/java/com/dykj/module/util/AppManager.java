package com.dykj.module.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Stack;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @date 2019/4/5.
 * description：
 */
public class AppManager {
    private static final String TAG_INTENT_DATA = "Data";
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }

        return instance;
    }

    public void finishAllActivity() {
        int i = 0;

        for (int size = activityStack.size(); i < size; ++i) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }

        activityStack.clear();
    }

    public void startActivity(Activity act, Class cls, Object obj, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(act, cls);
        if (obj != null) {
            if (obj instanceof Serializable) {
                intent.putExtra("Data", (Serializable) obj);
            }else {
                intent.putExtra("Data", (Parcelable) obj);
            }

        }

        if (requestCode != -1) {
            act.startActivityForResult(intent, requestCode);
        } else {
            act.startActivity(intent);
        }

    }

    public void startActivity2(Activity act, Class cls, Object obj, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(act, cls);
        if (obj != null) {
            intent.putExtra("Data", (Serializable) obj);
        }

        if (requestCode != -1) {
            act.startActivityForResult(intent, requestCode);
        } else {
            act.startActivity(intent);
        }

    }

    public void startActivityForFrg(Fragment act, Class cls, Object obj, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(act.getActivity(), cls);
        if (obj != null) {
            intent.putExtra("Data", (Serializable) obj);
        }

        if (requestCode != -1) {
            act.startActivityForResult(intent, requestCode);
        } else {
            act.startActivity(intent);
        }

    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack();
        }

        activityStack.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        this.finishActivity(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }

    }

    public void finishOtherActivity(Class<?> cls) {
        Stack<Activity> stack = new Stack<>();
        stack.clear();
        stack.addAll(activityStack);
        for (Activity activity : stack) {
            if (!activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishActivity(Class<?> cls) {

        Iterator var2 = activityStack.iterator();

        while (var2.hasNext()) {
            Activity activity = (Activity) var2.next();
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }

    }

    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception var3) {
        }

    }
}
