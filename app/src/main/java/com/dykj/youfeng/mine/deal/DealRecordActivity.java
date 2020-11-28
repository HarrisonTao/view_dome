package com.dykj.youfeng.mine.deal;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mine.deal.adpter.DealRecordAdapter;
import com.dykj.youfeng.mine.deal.popup.CreditPopupView;
import com.dykj.youfeng.mine.deal.popup.OnClickPopupItemTypeListener;
import com.dykj.youfeng.mine.deal.popup.TypePopupView;
import com.dykj.youfeng.mode.CreditcardListBean;
import com.dykj.youfeng.mode.IndexBillList;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交易记录
 */
public class DealRecordActivity extends BaseActivity implements OnClickPopupItemTypeListener {
    @BindView(R.id.tv_all_type)
    TextView tvAllType;
    @BindView(R.id.tv_all_card)
    TextView tvAllCard;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.refresh)
    SmartRefreshLayout mSmartRefresh;
    private DealRecordAdapter mAdapter = new DealRecordAdapter(R.layout.item_recharge_record);

    private int mPage = 1;
    private String mStatus;
    private String mCreditid;
    private List<CreditcardListBean.ListBean> mCreditcardList;

    @Override
    public int intiLayout() {
        return R.layout.activity_deal_record;
    }

    @Override
    public void initData() {
        initTitle("交易记录");
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
        mRecycler.setAdapter(mAdapter);
        requestCreditList();
    }

    @OnClick({R.id.tv_all_type, R.id.tv_all_card})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_all_type:
                tvAllType.setTextColor(getResources().getColor(R.color.main_color));
                tvAllCard.setTextColor(getResources().getColor(R.color.font_ff66));

                new XPopup.Builder(this)
                        .atView(view)
                        .asCustom(new TypePopupView(this, this))
                        .show();
                break;

            case R.id.tv_all_card:
                tvAllCard.setTextColor(getResources().getColor(R.color.main_color));
                tvAllType.setTextColor(getResources().getColor(R.color.font_ff66));
                if (null != mCreditcardList)
                    new XPopup.Builder(this)
                            .atView(view)
                            .asCustom(new CreditPopupView(this, mCreditcardList, this))
                            .show();
                break;
        }
    }


    @Override
    public void doBusiness() {
        requestList(true);
        mSmartRefresh.setOnRefreshListener(refreshLayout -> requestList(true));

        mSmartRefresh.setOnLoadMoreListener(refreshLayout -> requestList(false));
    }


    private void requestList(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        } else {
            mPage++;
        }

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();
        map.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
        map.put("page", mPage);
        if (!TextUtils.isEmpty(mStatus)) {
            map.put("status", mStatus);
        }
        if (!TextUtils.isEmpty(mCreditid)) {
            map.put("creditid", mCreditid);
        }

        HttpFactory.getInstance().getIndexBillList(map)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<IndexBillList>>() {
                    @Override
                    public void success(HttpResponseData<IndexBillList> data) {
                        if ("9999".equals(data.status)) {
                            IndexBillList indexBillList = data.getData();
                            if (null != indexBillList) {
                                SmartRefreshUtils.loadMore(mAdapter, mPage, indexBillList.page, indexBillList.list, mSmartRefresh);
                            } else {
                                mSmartRefresh.finishRefresh().finishLoadMore();
                            }
                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    /**
     * 请求信用卡列表
     */
    private void requestCreditList() {
        HttpFactory.getInstance().creditcardList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), "2", "1", "20")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<CreditcardListBean>>() {
                    @Override
                    public void success(HttpResponseData<CreditcardListBean> data) {
                        if ("9999".equals(data.status)) {
                            CreditcardListBean data1 = data.getData();
                            if (null != data1) {
                                mCreditcardList = new ArrayList<>();
                                mCreditcardList.add(new CreditcardListBean.ListBean("全部信用卡"));
                                mCreditcardList.addAll(data1.list);
                            }
                        } else {
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));
    }


    @Override
    public void checkType(int position, String type) {
        if (0 == position) {
            mStatus = null;
        } else {
            mStatus = position + "";
        }
        tvAllType.setText(type);
        tvAllType.setTextColor(getResources().getColor(R.color.main_color));
        tvAllCard.setTextColor(getResources().getColor(R.color.font_ff66));
        requestList(true);
    }

    @Override
    public void checkCradItem(int position, String card) {
        int id = mCreditcardList.get(position).id;
        if (-1 == id) {
            mCreditid = null;
        } else {
            mCreditid = id + "";
        }
        tvAllCard.setText(card);
        tvAllCard.setTextColor(getResources().getColor(R.color.main_color));
        tvAllType.setTextColor(getResources().getColor(R.color.font_ff66));
        requestList(true);


    }
}
