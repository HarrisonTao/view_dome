package com.dykj.youfeng.mine.deal.popup;

import android.content.Context;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mine.deal.adpter.IndexTypeAdapter;
import com.lxj.xpopup.impl.PartShadowPopupView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TypePopupView extends PartShadowPopupView {

    private List<String> typeList = new ArrayList<>();
    private IndexTypeAdapter mAdapter = new IndexTypeAdapter(R.layout.item_dialog_type);

    private OnClickPopupItemTypeListener mListener;

    public TypePopupView(@NonNull Context context, OnClickPopupItemTypeListener onClickPopupItemTypeListener) {
        super(context);
        this.mListener = onClickPopupItemTypeListener;
        initList();
    }

    private void initList() {
        typeList.clear();
        typeList.add("全部类型");
        typeList.add("消费");
        typeList.add("还款");
        typeList.add("收款");
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_recycler;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        RecyclerView mRecycler = findViewById(R.id.mRecycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecycler.setLayoutManager(gridLayoutManager);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setNewData(typeList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mListener.checkType(position,typeList.get(position));
            dismiss();
        });
    }

}
