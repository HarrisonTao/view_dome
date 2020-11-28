package com.dykj.youfeng.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dykj.youfeng.R;

/**
 * Created by lcjingon 2019/10/28.
 * description:
 * @author lcjing
 */
public class EmptyView {
    public static final int NULL=-1;
    public static final int NORM=0;
    public static final int NEWS=1;
    public static final int CREDIT_CARD=2;
    public static final int HK_PLAN=3;
    public static final int DEBIT_CARD=4;
    public static final int CASH=5;
    public static final int RECHARGE=6;
    public static final int REPAYMENT=7;
    public static final int SEARCH=8;
    public static View getEmptyView(int tag, Context context){
        View view= LayoutInflater.from(context).inflate(R.layout.base_empty,null);
        if (tag==NORM) {
            return view;
        }
        if(tag==NULL){
            view.setVisibility(View.GONE);
            return view;
        }
        return getEmptyView(tag,view);
    }

    public static View getEmptyView(int tag, Context context,int backgroundColor){
        View view= LayoutInflater.from(context).inflate(R.layout.base_empty,null);
        if (tag==NORM) {
            view.setBackgroundColor(backgroundColor);
            return view;
        }
        return getEmptyView(tag,view);
    }

    public static View getEmptyView(int tag,View view){
        view.findViewById(R.id.ll_norm).setVisibility(View.GONE);
        view.findViewById(R.id.ll_news).setVisibility(View.VISIBLE);
        ImageView iv=view.findViewById(R.id.iv_wu);
        TextView tv=view.findViewById(R.id.tv_wu);
        switch (tag){
            case NEWS:
                iv.setImageResource(R.mipmap.ic_zero);
                tv.setText("您还没有收到消息哦~");
                break;
            case CREDIT_CARD:
                iv.setImageResource(R.mipmap.ic_zero);
                tv.setText("您还没有添加信卡，快去添加吧");
                break;
            case DEBIT_CARD:
                iv.setImageResource(R.mipmap.ic_zero);
                tv.setText("您还没有添加储蓄卡，快去添加吧");
                break;
            case HK_PLAN:
                iv.setImageResource(R.mipmap.ic_zero);
                tv.setText("您还没有制定的还款计划哦~");
                break;
            case CASH:
                iv.setImageResource(R.mipmap.ic_zero);
                tv.setText("您还没有提现记录哦~");
                break;
            case RECHARGE:
                iv.setImageResource(R.mipmap.ic_zero);
                tv.setText("您还没有充值记录哦~");
                break;
            case REPAYMENT:
                iv.setImageResource(R.mipmap.ic_zero);
                tv.setText("您暂时没有需要制定计划的信用卡");
                break;
            case SEARCH:
                iv.setImageResource(R.mipmap.ic_wujieg);
                tv.setText("暂无搜索结果");
                break;
        }
        return view;
    }
}
