package com.dykj.youfeng.mine.yue.cash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.CashApplyBean;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class TakeOutAdapter extends PagerAdapter {

    private int currentView = 0;
    private List<CashApplyBean.DebitCardBean> mDebitList;
    private Context mCt;

    public TakeOutAdapter(List<CashApplyBean.DebitCardBean> debit_card, Context context) {
        this.mDebitList = debit_card;
        this.mCt = context;
    }


    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        currentView = position;
    }

    @Override
    public int getCount() {
        return mDebitList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mCt).inflate(R.layout.item_cash_deposit, container, false);
        TextView tvCardName = view.findViewById(R.id.tv_card_name);
        TextView tvCardNumber = view.findViewById(R.id.tv_card_number);
        CashApplyBean.DebitCardBean debitCardBean = mDebitList.get(position);
        tvCardName.setText(debitCardBean.bank_name);
        tvCardNumber.setText(debitCardBean.bank_num);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int getCurrentItemPotision() {
        return currentView;
    }
}
