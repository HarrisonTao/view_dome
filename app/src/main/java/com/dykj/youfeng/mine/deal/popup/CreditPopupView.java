package com.dykj.youfeng.mine.deal.popup;

import android.content.Context;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mine.deal.adpter.IndexCreeditAdapter;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.lxj.xpopup.impl.PartShadowPopupView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreditPopupView extends PartShadowPopupView {

    private IndexCreeditAdapter mAdapter = new IndexCreeditAdapter(R.layout.item_dialog_type);

    private OnClickPopupItemTypeListener mListener;
    private List<CreditcardListBean.ListBean> mCreditcardList;

    public CreditPopupView(@NonNull Context context, List<CreditcardListBean.ListBean> mList, OnClickPopupItemTypeListener onClickPopupItemTypeListener) {
        super(context);
        this.mCreditcardList = mList;
        this.mListener = onClickPopupItemTypeListener;
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_recycler;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        RecyclerView mRecycler = findViewById(R.id.mRecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(mCreditcardList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mListener.checkCradItem(position,mCreditcardList.get(position).bank_name);
            dismiss();
        });
    }

}
