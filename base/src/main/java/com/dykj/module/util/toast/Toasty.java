package com.dykj.module.util.toast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;

import com.blankj.utilcode.util.Utils;
import com.dykj.module.R;
import com.dykj.module.base.BaseApplication;

import java.lang.reflect.Field;

/**
 * @author LewChich
 * @date 2019/8/20
 * description：Toasty工具类，即使关闭通知权限也可以使用
 */
@SuppressLint("InflateParams")
public class Toasty {

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    private static final Typeface LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static IToast iToast;
    private static Typeface currentTypeface = LOADED_TOAST_TYPEFACE;
    private static int textSize = 16; // in SP
    private static boolean tintIcon = true;
    private static boolean allowQueue = true;
    private static Toast lastToast = null;

    private Toasty() {
        // avoiding instantiation
    }

    public static void normal(@NonNull Context context, @StringRes int message) {
        normal(context, context.getString(message), Toast.LENGTH_SHORT, null, false);
    }

    public static void normal(@NonNull Context context, @NonNull CharSequence message) {
        normal(context, message, Toast.LENGTH_SHORT, null, false);
    }

    public static void normal(@NonNull Context context, @StringRes int message, Drawable icon) {
        normal(context, context.getString(message), Toast.LENGTH_SHORT, icon, true);
    }

    public static void normal(@NonNull Context context, @NonNull CharSequence message, Drawable icon) {
        normal(context, message, Toast.LENGTH_SHORT, icon, true);
    }

    public static void normal(@NonNull Context context, @StringRes int message, int duration) {
        normal(context, context.getString(message), duration, null, false);
    }

    public static void normal(@NonNull Context context, @NonNull CharSequence message, int duration) {
        normal(context, message, duration, null, false);
    }

    public static void normal(@NonNull Context context, @StringRes int message, int duration,
                              Drawable icon) {
        normal(context, context.getString(message), duration, icon, true);
    }

    public static void normal(@NonNull Context context, @NonNull CharSequence message, int duration,
                              Drawable icon) {
        normal(context, message, duration, icon, true);
    }

    public static void normal(@NonNull Context context, @StringRes int message, int duration,
                              Drawable icon, boolean withIcon) {
        custom(context, context.getString(message), icon, ToastyUtils.getColor(context, R.color.normalColor),
                ToastyUtils.getColor(context, R.color.white), duration, withIcon, true);
    }

    public static void normal(@NonNull Context context, @NonNull CharSequence message, int duration,
                              Drawable icon, boolean withIcon) {
        custom(context, message, icon, ToastyUtils.getColor(context, R.color.normalColor),
                ToastyUtils.getColor(context, R.color.white), duration, withIcon, true);
    }

    public static void warning(@NonNull Context context, @StringRes int message) {
        warning(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    public static void warning(@NonNull Context context, @NonNull CharSequence message) {
        warning(context, message, Toast.LENGTH_SHORT, true);
    }

    public static void warning(@NonNull Context context, @StringRes int message, int duration) {
        warning(context, context.getString(message), duration, true);
    }


    public static void warning(@NonNull Context context, @NonNull CharSequence message, int duration) {
        warning(context, message, duration, true);
    }

    public static void warning(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        custom(context, context.getString(message), ToastyUtils.getDrawable(context, R.drawable.ic_error_outline_white_24dp),
                ToastyUtils.getColor(context, R.color.warningColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void warning(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        custom(context, message, ToastyUtils.getDrawable(context, R.drawable.ic_error_outline_white_24dp),
                ToastyUtils.getColor(context, R.color.warningColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void info(@NonNull Context context, @StringRes int message) {
        info(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    public static void info(@NonNull Context context, @NonNull CharSequence message) {
        info(context, message, Toast.LENGTH_SHORT, true);
    }

    public static void info(@NonNull Context context, @StringRes int message, int duration) {
        info(context, context.getString(message), duration, true);
    }

    public static void info(@NonNull Context context, @NonNull CharSequence message, int duration) {
        info(context, message, duration, true);
    }

    public static void info(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        custom(context, context.getString(message), ToastyUtils.getDrawable(context, R.drawable.ic_info_outline_white_24dp),
                ToastyUtils.getColor(context, R.color.infoColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void info(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        custom(context, message, ToastyUtils.getDrawable(context, R.drawable.ic_info_outline_white_24dp),
                ToastyUtils.getColor(context, R.color.infoColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void success(@NonNull Context context, @StringRes int message) {
        success(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    public static void success(@NonNull Context context, @NonNull CharSequence message) {
        success(context, message, Toast.LENGTH_SHORT, true);
    }

    public static void success(@NonNull Context context, @StringRes int message, int duration) {
        success(context, context.getString(message), duration, true);
    }

    public static void success(@NonNull Context context, @NonNull CharSequence message, int duration) {
        success(context, message, duration, true);
    }

    public static void success(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        custom(context, context.getString(message), ToastyUtils.getDrawable(context, R.drawable.ic_check_white_24dp),
                ToastyUtils.getColor(context, R.color.successColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void success(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        custom(context, message, ToastyUtils.getDrawable(context, R.drawable.ic_check_white_24dp),
                ToastyUtils.getColor(context, R.color.successColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void error(@NonNull Context context, @StringRes int message) {
        error(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    public static void error(@NonNull Context context, @NonNull CharSequence message) {
        error(context, message, Toast.LENGTH_SHORT, true);
    }

    public static void error(@NonNull Context context, @StringRes int message, int duration) {
        error(context, context.getString(message), duration, true);
    }

    public static void error(@NonNull Context context, @NonNull CharSequence message, int duration) {
        error(context, message, duration, true);
    }

    public static void error(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        custom(context, context.getString(message), ToastyUtils.getDrawable(context, R.drawable.ic_clear_white_24dp),
                ToastyUtils.getColor(context, R.color.errorColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void error(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        custom(context, message, ToastyUtils.getDrawable(context, R.drawable.ic_clear_white_24dp),
                ToastyUtils.getColor(context, R.color.errorColor), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, true);
    }

    public static void custom(@NonNull Context context, @StringRes int message, Drawable icon,
                              int duration, boolean withIcon) {
        custom(context, context.getString(message), icon, -1, ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, false);
    }

    public static void custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon,
                              int duration, boolean withIcon) {
        custom(context, message, icon, -1, ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, false);
    }

    public static void custom(@NonNull Context context, @StringRes int message, @DrawableRes int iconRes,
                              @ColorRes int tintColorRes, int duration,
                              boolean withIcon, boolean shouldTint) {
        custom(context, context.getString(message), ToastyUtils.getDrawable(context, iconRes),
                ToastyUtils.getColor(context, tintColorRes), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, shouldTint);
    }

    public static void custom(@NonNull Context context, @NonNull CharSequence message, @DrawableRes int iconRes,
                              @ColorRes int tintColorRes, int duration,
                              boolean withIcon, boolean shouldTint) {
        custom(context, message, ToastyUtils.getDrawable(context, iconRes),
                ToastyUtils.getColor(context, tintColorRes), ToastyUtils.getColor(context, R.color.white),
                duration, withIcon, shouldTint);
    }

    public static void custom(@NonNull Context context, @StringRes int message, Drawable icon,
                              @ColorRes int tintColorRes, int duration,
                              boolean withIcon, boolean shouldTint) {
        custom(context, context.getString(message), icon, ToastyUtils.getColor(context, tintColorRes),
                ToastyUtils.getColor(context, R.color.white), duration, withIcon, shouldTint);
    }

    public static void custom(@NonNull Context context, @StringRes int message, Drawable icon,
                              @ColorRes int tintColorRes, @ColorRes int textColorRes, int duration,
                              boolean withIcon, boolean shouldTint) {
        custom(context, context.getString(message), icon, ToastyUtils.getColor(context, tintColorRes),
                ToastyUtils.getColor(context, textColorRes), duration, withIcon, shouldTint);
    }

    @SuppressLint("ShowToast")
    public static void custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon,
                              @ColorInt int tintColor, @ColorInt int textColor, int duration,
                              boolean withIcon, boolean shouldTint) {
        final Toast currentToast = Toast.makeText(context, "", duration);
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.toast_layout, null);
        final ImageView toastIcon = toastLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame;

        if (shouldTint) {
            drawableFrame = ToastyUtils.tint9PatchDrawableFrame(context, tintColor);
        } else {
            drawableFrame = ToastyUtils.getDrawable(context, R.drawable.toast_frame);
        }
        ToastyUtils.setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null) {
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            }
            ToastyUtils.setBackground(toastIcon, tintIcon ? ToastyUtils.tintIcon(icon, textColor) : icon);
        } else {
            toastIcon.setVisibility(View.GONE);
        }

        toastTextView.setText(message);
        toastTextView.setTextColor(textColor);
        toastTextView.setTypeface(currentTypeface);
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        currentToast.setView(toastLayout);

        if (!allowQueue) {
            if (lastToast != null) {
                lastToast.cancel();
            }
            lastToast = currentToast;
        }
        show(toastLayout, Toast.LENGTH_SHORT);
    }

    private static void show(final View view, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (iToast != null) {
                    iToast.cancel();
                }
                iToast = ToastFactory.newToast(Utils.getApp());
                iToast.setView(view);
                iToast.setDuration(duration);
//                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
//                    iToast.setGravity(sGravity, sXOffset, sYOffset);
//                }
//                setBg();
                iToast.show();
            }
        });
    }

    interface IToast {

        void show();

        void cancel();

        View getView();

        void setView(View view);

        void setDuration(int duration);

        void setGravity(int gravity, int xOffset, int yOffset);

        void setText(@StringRes int resId);

        void setText(CharSequence s);
    }

    static class ToastFactory {

        static IToast makeToast(Context context, CharSequence text, int duration) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                return new SystemToast(makeNormalToast(context, text, duration));
            }
            return new ToastWithoutNotification(makeNormalToast(context, text, duration));
        }

        static IToast newToast(Context context) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                return new SystemToast(new Toast(context));
            }
            return new ToastWithoutNotification(new Toast(context));
        }

        private static Toast makeNormalToast(Context context, CharSequence text, int duration) {
            @SuppressLint("ShowToast")
            Toast toast = Toast.makeText(context, "", duration);
            toast.setText(text);
            return toast;
        }
    }

    static class ToastWithoutNotification extends AbsToast {

        private static final Utils.OnActivityDestroyedListener LISTENER =
                new Utils.OnActivityDestroyedListener() {
                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        if (iToast == null) {
                            return;
                        }
                        iToast.cancel();
                    }
                };
        private View mView;
        private WindowManager mWM;
        private WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        ToastWithoutNotification(Toast toast) {
            super(toast);
        }

        @Override
        public void show() {
            mView = mToast.getView();
            if (mView == null) {
                return;
            }
            final Context context = mToast.getView().getContext();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                Context topActivityOrApp = Utils.getApp();
                if (!(topActivityOrApp instanceof Activity)) {
                    Log.e("ToastUtils", "Couldn't get top Activity.");
                    return;
                }
                Activity topActivity = (Activity) topActivityOrApp;
                if (topActivity.isFinishing() || topActivity.isDestroyed()) {
                    Log.e("ToastUtils", topActivity + " is useless");
                    return;
                }
                mWM = topActivity.getWindowManager();
                mParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
            } else {
                mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                mParams.type = WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW + 37;
            }

            final Configuration config = context.getResources().getConfiguration();
            final int gravity = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                    ? Gravity.getAbsoluteGravity(mToast.getGravity(), config.getLayoutDirection())
                    : mToast.getGravity();

            mParams.y = mToast.getYOffset();
            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.windowAnimations = android.R.style.Animation_Toast;

            mParams.setTitle("ToastWithoutNotification");
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mParams.gravity = gravity;
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f;
            }
            mParams.x = mToast.getXOffset();
            mParams.packageName = context.getPackageName();

            try {
                if (mWM != null) {
                    mWM.addView(mView, mParams);
                }
            } catch (Exception ignored) { /**/ }

            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, mToast.getDuration() == Toast.LENGTH_SHORT ? 2000 : 3500);
        }

        @Override
        public void cancel() {
            try {
                if (mWM != null) {
                    mWM.removeViewImmediate(mView);
                }
            } catch (Exception ignored) { /**/ }
            mView = null;
            mWM = null;
            mToast = null;
        }
    }

    static abstract class AbsToast implements IToast {

        Toast mToast;

        AbsToast(Toast toast) {
            mToast = toast;
        }

        @Override
        public View getView() {
            return mToast.getView();
        }

        @Override
        public void setView(View view) {
            mToast.setView(view);
        }

        @Override
        public void setDuration(int duration) {
            mToast.setDuration(duration);
        }

        @Override
        public void setGravity(int gravity, int xOffset, int yOffset) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        @Override
        public void setText(int resId) {
            mToast.setText(resId);
        }

        @Override
        public void setText(CharSequence s) {
            mToast.setText(s);
        }
    }

    static class SystemToast extends AbsToast {

        private static Field sField_mTN;
        private static Field sField_TN_Handler;

        SystemToast(Toast toast) {
            super(toast);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    //noinspection JavaReflectionMemberAccess
                    sField_mTN = Toast.class.getDeclaredField("mTN");
                    sField_mTN.setAccessible(true);
                    Object mTN = sField_mTN.get(toast);
                    sField_TN_Handler = sField_mTN.getType().getDeclaredField("mHandler");
                    sField_TN_Handler.setAccessible(true);
                    Handler tnHandler = (Handler) sField_TN_Handler.get(mTN);
                    sField_TN_Handler.set(mTN, new SafeHandler(tnHandler));
                } catch (Exception ignored) { /**/ }
            }
        }

        @Override
        public void show() {
            mToast.show();
        }

        @Override
        public void cancel() {
            mToast.cancel();
        }

        static class SafeHandler extends Handler {
            private Handler impl;

            SafeHandler(Handler impl) {
                this.impl = impl;
            }

            @Override
            public void handleMessage(Message msg) {
                impl.handleMessage(msg);
            }

            @Override
            public void dispatchMessage(Message msg) {
                try {
                    impl.dispatchMessage(msg);
                } catch (Exception e) {
                    Log.e("ToastUtils", e.toString());
                }
            }
        }
    }

    public static class Config {
        private Typeface typeface = Toasty.currentTypeface;
        private int textSize = Toasty.textSize;

        private boolean tintIcon = Toasty.tintIcon;
        private boolean allowQueue = true;

        private Config() {
            // avoiding instantiation
        }

        @CheckResult
        public static Toasty.Config getInstance() {
            return new Toasty.Config();
        }

        public static void reset() {
            Toasty.currentTypeface = LOADED_TOAST_TYPEFACE;
            Toasty.textSize = 16;
            Toasty.tintIcon = true;
            Toasty.allowQueue = true;
        }

        @CheckResult
        public Toasty.Config setToastTypeface(@NonNull Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        @CheckResult
        public Toasty.Config setTextSize(int sizeInSp) {
            this.textSize = sizeInSp;
            return this;
        }

        @CheckResult
        public Config tintIcon(boolean tintIcon) {
            this.tintIcon = tintIcon;
            return this;
        }

        @CheckResult
        public Config allowQueue(boolean allowQueue) {
            this.allowQueue = allowQueue;
            return this;
        }

        public void apply() {
            Toasty.currentTypeface = typeface;
            Toasty.textSize = textSize;
            Toasty.tintIcon = tintIcon;
            Toasty.allowQueue = allowQueue;
        }
    }
}
