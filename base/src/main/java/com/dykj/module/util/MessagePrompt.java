package com.dykj.module.util;

import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.dykj.module.R;
import com.dykj.module.util.toast.DyToast;

/**
 * 作   者:  DLQ
 * 创建时间： 2018/12/27
 * 功能说明： 信息提示弹窗 用于信息提示
 */

public class MessagePrompt {
    private static final MessagePrompt ourInstance = new MessagePrompt();
    private Context mContext;
    private String mTitle;
    private String mContent;
    private Spanned mSpanned;
    private String mSureText = "确定";
    private String mCancelText = "取消";
    private int mImgResId;
    private int mSize = 0;
    private int mTextColor = 0;
    private onClickListener mListener;
    private boolean mIsClean = true;

    public static MessagePrompt getInstance() {
        return ourInstance;
    }

    /**
     * 传入context
     *
     * @param context
     * @return
     */
    public final MessagePrompt with(Context context) {
        mContext = context;
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public final MessagePrompt setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param size
     * @return
     */
    public final MessagePrompt setTitleSize(int size) {
        mSize = size;
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param color
     * @return
     */
    public final MessagePrompt setTitleColor(int color) {
        mTextColor = color;
        return this;
    }


    /**
     * 设置内容
     *
     * @param content
     * @return
     */
    public final MessagePrompt setContent(String content) {
        mContent = content;
        return this;
    }

    public final MessagePrompt setSpanned(Spanned spanned) {
        mSpanned = spanned;
        return this;
    }

    /**
     * 设置 确定按钮文字
     *
     * @param sureText
     * @return
     */
    public final MessagePrompt setSureText(String sureText) {
        if (!TextUtils.isEmpty(sureText)) {
            mSureText = sureText;
        }
        return this;
    }

    /**
     * 设置 取消按钮文字
     *
     * @param cancelText
     * @return
     */
    public final MessagePrompt setCancelText(String cancelText) {
        if (!TextUtils.isEmpty(cancelText)) {
            mCancelText = cancelText;
        }
        return this;
    }

    /**
     * 设置 取消按钮文字显示与隐藏
     *
     * @return
     */
    public final MessagePrompt setCancelTextV(boolean isClean) {
        mIsClean = isClean;
        return this;
    }

    /**
     * 设置  显示图片
     *
     * @param imgResId
     * @return
     */
    public final MessagePrompt setImageResId(int imgResId) {
        mImgResId = imgResId;
        return this;
    }

    /**
     * 确定和取消按钮回调
     *
     * @param listener
     * @return
     */
    public final MessagePrompt setClickListener(onClickListener listener) {
        mListener = listener;
        return this;
    }


    public void show() {

        if (mContext == null) {
            DyToast.getInstance().warning("请先使用初始化方法 with()");
            return;
        }

        DialogUtils.getInstance().with(mContext)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(80, 0, 80, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_prompt)
                .setOnChildViewclickListener(new DialogUtils.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        TextView content = view.findViewById(R.id.prompt_content);
                        TextView title = view.findViewById(R.id.prompt_title);
                        TextView sure = view.findViewById(R.id.prompt_sure);
                        TextView cancel = view.findViewById(R.id.prompt_cancel);
                        ImageView img = view.findViewById(R.id.prompt_img);
                        ImageView view_1 = view.findViewById(R.id.view_1);
                        if (!mIsClean) {
                            cancel.setVisibility(View.GONE);
                            view_1.setVisibility(View.GONE);
                        }


                        if (!TextUtils.isEmpty(mContent)) {
                            content.setText(mContent);
                            content.setVisibility(View.VISIBLE);
                        } else {
                            content.setVisibility(View.GONE);
                        }

                        if (mSpanned != null) {
                            content.setText(mSpanned);
                        }

                        if (!TextUtils.isEmpty(mTitle)) {
                            title.setText(mTitle);
                            if (mSize != 0) {
                                title.setTextSize(mSize);
                            }

                            if (mTextColor != 0) {
                                title.setTextColor(mTextColor);
                            }

                            title.setVisibility(View.VISIBLE);
                        } else {
                            title.setVisibility(View.GONE);
                        }

                        if (!TextUtils.isEmpty(mSureText)) {
                            sure.setText(mSureText);
                        }

                        if (!TextUtils.isEmpty(mCancelText)) {
                            cancel.setText(mCancelText);
                        }

                        if (mImgResId > 0) {
                            img.setImageResource(mImgResId);
                        }

                        sure.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtils.dismiss();
                                if (mListener != null) {
                                    mListener.sureClick();
                                }
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtils.dismiss();
                                if (mListener != null) {
                                    mListener.cancelClick();
                                }
                            }
                        });

                    }
                })
                .show();

    }

    public interface onClickListener {
        void sureClick();

        void cancelClick();
    }


}
