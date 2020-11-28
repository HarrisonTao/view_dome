package com.dykj.youfeng.mine.group;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.GroupBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的团队
 */
public class MeGroupActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    @BindView(R.id.tv_teamCount)
    TextView tvTeamCount;
    @BindView(R.id.tv_direct_count)
    TextView tvDirectCount;
    @BindView(R.id.tv_month_teamCount)
    TextView tvMonthTeamCount;
    @BindView(R.id.tv_direct_count_money)
    TextView tvDirectCountMoney;
    @BindView(R.id.zhitui)
    TextView zhitui;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.jianjie)
    TextView jianjie;
    @BindView(R.id.text2)
    TextView text2;

    private boolean isOpen=true;

    private int mPage = 1;
    private MeGroupAdapter mAdapter = new MeGroupAdapter(R.layout.item_group);

    @Override
    public int intiLayout() {
        return R.layout.activity_group;
    }

    @Override
    public void initData() {
        mSmartRefresh.setEnableRefresh(false);
        requestTeamList(true);
        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestTeamList(false));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                callTel(position);

            }

            private void callTel(int position) {
                GroupBean.ListBean listBean = mAdapter.getData().get(position);
                String tel = listBean.tel;
                // 拨打直推电话
                BaseToolsUtil.askPermissions(MeGroupActivity.this, new BaseToolsUtil.PermissionInterface() {
                    @Override
                    public void ok() {
                        if (!TextUtils.isEmpty(tel)) {
                            showCallDialog(listBean);
                        }
                    }

                    @Override
                    public void error() {
                        DyToast.getInstance().warning("您已拒绝拨打电话相关权限，无法使用该功能");
                    }
                }, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });
    }


    private void showCallDialog(GroupBean.ListBean bean) {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_public)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTel = view.findViewById(R.id.tv_content);
                    String name = TextUtils.isEmpty(bean.realname) ? "未实名" : bean.realname;
                    String lastPhone = bean.phone.substring(bean.phone.length() - 4);
                    tvTel.setText(name + "( " + bean.levelName + " ) 手机尾号: " + lastPhone);
                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_safety_quit).setOnClickListener(v -> {
                        BaseToolsUtil.call(MeGroupActivity.this, bean.tel);
                        DialogUtils.dismiss();
                    });
                })
                .show();
    }


    @Override
    public void doBusiness() {

    }


    private void requestTeamList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }
        Map<String, Object> parms = new HashMap<>();
        //  "97829b403ca3c99996a5f01dc56b2eab"
        parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        parms.put("page", mPage);
        parms.put("limit", "10");
        HttpFactory.getInstance().meTeamList(parms)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(MeGroupActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<GroupBean>>() {
                    @Override
                    public void success(HttpResponseData<GroupBean> data) {
                        Log.d("wtf", new Gson().toJson(data));
                        if ("9999".equals(data.status)) {
                            GroupBean data1 = data.getData();
                            tvTeamCount.setText(data1.jtteamCount + "");
                            tvDirectCount.setText(data1.directCount + "");
                            tvMonthTeamCount.setText(data1.teamMonthTotalSum + "");
                            tvDirectCountMoney.setText(data1.teamTotalSum + "");
                            if(isOpen) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, data1.page, data1.list, mSmartRefresh);
                            }else{
                                SmartRefreshUtils.loadMore(mAdapter, mPage, data1.page, data1.jtlist, mSmartRefresh);
                            }
                        } else {
                            DyToast.getInstance().error(data.message);
                            if (isRefresh) {
                                mSmartRefresh.finishRefresh();
                            } else {
                                mSmartRefresh.finishLoadMore();
                            }
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    @OnClick({R.id.zhitui, R.id.jianjie})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zhitui:
                isOpen=true;
                requestTeamList(true);
                if(text2.getVisibility()==View.VISIBLE){
                    text2.setVisibility(View.INVISIBLE);
                }
                if(text1.getVisibility()==View.INVISIBLE){
                    text1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.jianjie:

                isOpen=false;
                requestTeamList(true);
                if(text1.getVisibility()==View.VISIBLE){
                    text1.setVisibility(View.INVISIBLE);
                }
                if(text2.getVisibility()==View.INVISIBLE){
                    text2.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
