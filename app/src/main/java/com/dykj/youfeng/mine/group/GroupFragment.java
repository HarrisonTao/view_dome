package com.dykj.youfeng.mine.group;

import android.os.Bundle;
import android.view.View;

import com.dykj.module.base.BaseFragment;
import com.dykj.youfeng.R;

/**
 * 团队明细
 * @author zhaoyanhui
 */
public class GroupFragment extends BaseFragment {


    private static final String TAG_ID = "tag_id";

    public static GroupFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putSerializable(TAG_ID,type);
        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int intiLayout() {
        return R.layout.activity_recycler;
    }

    @Override
    protected void onViewReallyCreated(View view) {

    }

    @Override
    public void doBusiness() {

    }
}
