package com.dykj.module.util.marquee;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.dykj.module.R;

import java.util.List;

/**
 * Created by lj on 2017/5/15.
 * 上下翻滚的广播栏
 */

public class TextMarqueeVertical extends LinearLayout {
    private Context mContext;
    private ViewFlipper viewFlipper;
    private View marqueeTextView;
    private String[] textArrays;
    private List<TextMarqueeBean> listBean;
    private TextMarqueeListener clickListener;

    public TextMarqueeVertical(Context context) {
        super(context);
        mContext = context;
        initBasicView();
    }


    public TextMarqueeVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBasicView();
    }

    /**
     * 1.设置数据源；2.设置监听回调（将textView点击事件传递到目标界面进行操作）
     */
    public void setTextArrays(String[] textArrays, boolean flip, TextMarqueeListener listener) {
        this.textArrays = textArrays;
        this.clickListener = listener;
        initMarqueeTextView(textArrays, flip, listener);
    }

    /**
     * 1.设置数据源；2.设置监听回调（将textView点击事件传递到目标界面进行操作）
     *
     * @param listBe   设置数据源
     * @param flip     sa
     * @param listener 设置监听回调
     */
    public void setTextArrays(List<TextMarqueeBean> listBe, boolean flip, TextMarqueeListener listener) {
        this.listBean = listBe;
        this.clickListener = listener;
        initMarqueeTextView(listBean, flip, listener);
    }

    /**
     * 加载布局，初始化ViewFlipper组件及效果
     */
    public void initBasicView() {
        marqueeTextView = LayoutInflater.from(mContext).inflate(R.layout.z_marquee_view_flipper, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(marqueeTextView, layoutParams);
        viewFlipper = (ViewFlipper) marqueeTextView.findViewById(R.id.marqueeFlipper);
        //设置上下的动画效果（自定义动画，所以改左右也很简单）
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.z_marquee_slide_in_bottom));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.z_marquee_slide_out_top));
        viewFlipper.startFlipping();
    }

    public void initMarqueeTextView(String[] textArrays, boolean flip, TextMarqueeListener clickListener) {
        if (textArrays == null || textArrays.length == 0) {
            return;
        }
        int i = 0;
        viewFlipper.removeAllViews();
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        while (i < textArrays.length) {
            TextView textView = new TextView(mContext);
            textView.setText(textArrays[i]);
            textView.setTag(textArrays[i]);
            textView.setTextColor(Color.parseColor("#FF8534"));
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setOnClickListener(clickListener);
            viewFlipper.addView(textView, lp);
            i++;
        }
        if (flip) {
            viewFlipper.startFlipping();
        } else {
            if (textArrays.length == 1) {
                viewFlipper.stopFlipping();
            } else {
                viewFlipper.startFlipping();
            }
        }

    }

    public void initMarqueeTextView(List<TextMarqueeBean> listBe, boolean flip, TextMarqueeListener clickListener) {
        if (listBe != null && listBe.size() >= 0) {
            int i = 0;
            viewFlipper.removeAllViews();
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            while (i < listBe.size()) {

                View vItem = LayoutInflater.from(mContext).inflate(R.layout.z_marquee_icon_tv, null);
                ImageView imageView = vItem.findViewById(R.id.item_iv_icon);
                if (TextUtils.isEmpty(listBe.get(i).getIconUrl())) {
                    imageView.setImageResource(listBe.get(i).getIconDraw());
                } else {
                    Glide.with(getContext()).load(listBe.get(i).getIconUrl()).into(imageView);
                }
                TextView textView = vItem.findViewById(R.id.item_tv_desc);
                textView.setText(listBe.get(i).getTitle());
                textView.setTag(i);
                textView.setMaxLines(1);
                textView.setOnClickListener(clickListener);
                viewFlipper.addView(vItem, lp);
//                TextView textView = new TextView(mContext);
//                textView.setText(listBe.get(i).getTitle());
//                textView.setTag(listBe.get(i).getId());
//                textView.setMaxLines(1);
//                textView.setEllipsize(TextUtils.TruncateAt.END);
//                textView.setOnClickListener(clickListener);
//                viewFlipper.addView(textView, lp);
                i++;
            }
            if (flip) {
                viewFlipper.startFlipping();
            } else {
                if (listBe.size() == 1) {
                    viewFlipper.stopFlipping();
                } else {
                    viewFlipper.startFlipping();
                }
            }
        }
    }

    public void releaseResources() {
        if (marqueeTextView != null) {
            if (viewFlipper != null) {
                viewFlipper.stopFlipping();
                viewFlipper.removeAllViews();
                viewFlipper = null;
            }
            marqueeTextView = null;
        }
    }
}
