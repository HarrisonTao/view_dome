package com.dykj.youfeng.home.repayment.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dykj.youfeng.R;
import com.dykj.youfeng.home.repayment.adapter.RepaymentChannelAdapter;
import com.dykj.youfeng.mode.ChannelList;
import com.dykj.youfeng.mode.SelectCreditBean;
import com.dykj.youfeng.mode.ShopInputBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.SmartRefreshUtils;
import com.dykj.module.util.toast.DyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 还款选择通道
 */
public class RepaymentChannelActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mSmartRefresh)
    SmartRefreshLayout mSmartRefresh;
    private RepaymentChannelAdapter mAdapter = new RepaymentChannelAdapter(R.layout.item_channel);
    
    @Override
    public int intiLayout() {
        return R.layout.activity_recycler;
    }
    
    @Override
    public void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initTitle("选择通道","一键还款");
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty, mRecycler);
        mAdapter.setOnItemClickListener(this);
        
        mSmartRefresh.setOnRefreshListener(refreshLayout -> requestChannelList());
        mSmartRefresh.setEnableLoadMore(false);
    }
    
    @Override
    public void doBusiness() {
        requestChannelList();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    
    /**
     * 请求渠道列表
     */
    private void requestChannelList() {
        Log.d("wtf","Tokent"+MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "") );
        HttpFactory.getInstance().getChannelList(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""))
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RepaymentChannelActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<ChannelList>>>() {
                    @Override
                    public void success(HttpResponseData<List<ChannelList>> data) {
                        if ("9999".equals(data.status)) {
                            List<ChannelList> data1 = data.getData();
                            if (null != data1) {
                                List<ChannelList> channelLists = new ArrayList<>();
                                for (int i = 0; i < data1.size(); i++) {
                                    ChannelList channelBean = data1.get(i);
                                    if ("1".equals(channelBean.status)) {
                                        channelLists.add(channelBean);
                                    }
                                }
                                
                                SmartRefreshUtils.loadMore(mAdapter, channelLists, mSmartRefresh);
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
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ChannelList channelBean = mAdapter.getData().get(position);
        String channelType = channelBean.type;
        // 判断是否是畅捷~
        if (channelType.contains("Cj") || channelType.contains("cj")) {  // 畅捷大额 & 畅捷小额
            String code = MMKV.defaultMMKV().decodeString(AppCacheInfo.merchantCode, "");
            if ("0".equals(code)) {   //   未进件~    畅捷进件 0.未进件 其他为已进件
                ShopInputBean bean = new ShopInputBean();
                bean.type = "replaceRepay";
                bean.channle = channelType;
                startAct(ShopInputActivity.class, bean);
            } else {  // 已进件
                SelectCreditBean bean = new SelectCreditBean();
                bean.type = "replaceRepay";
                bean.channelType = channelType;
                startAct(CreditCardMsgActivity.class, bean);
            }
        } else if (channelType.contains("jft") || channelType.contains("Jft")) {
           String code = channelType.replace("jft", "");
            code = code.replace("Jft", "");
            /* jftIsJoin(code,"手动还款");*/
            //用户已进件   判断卡的状态
            SelectCreditBean bean = new SelectCreditBean();
            bean.type = "jft";
            bean.channelType = "Jft" + code;
            bean.repaymentMode = "手动";
            startAct(CreditCardMsgActivity.class, bean);
        } else if ("Ds".equalsIgnoreCase(channelType)) {
            SelectCreditBean bean = new SelectCreditBean();
            bean.type = "Ds";
            bean.channelType="Ds";
            startAct(CreditCardMsgActivity.class, bean);
        }  else if ("Kjt".equalsIgnoreCase(channelType)) {
            SelectCreditBean bean = new SelectCreditBean();
            bean.type = "Kjt";
            bean.channelType="Kjt";
            startAct(CreditCardMsgActivity.class, bean);
        } else {
            // 其他通道
            SelectCreditBean bean = new SelectCreditBean();
            bean.type = "OtherRepay";
            bean.channelType = channelType;
            startAct(CreditCardMsgActivity.class, bean);
        }
    }
    
    
    //*****************************jft********************************start*************************************
    
    /***
     * @param code
     */
    private void jftIsJoin(final String code,String repaymentMode) {
        /*HttpFactory.getInstance()
                .jftIsJoin(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), code)
                .compose(HttpObserver.schedulers(getAty()))
                .as(HttpObserver.life(RepaymentChannelActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if ("8888".equals(data.status)) {
                            //用户未进件
                            startAct(getAty(), ShopInputJftActivity.class, code);
                        } else if ("9999".equals(data.getStatus())) {
                            //用户已进件   判断卡的状态
                            SelectCreditBean bean = new SelectCreditBean();
                            bean.type = "jft";
                            bean.channelType = "Jft" + code;
                            bean.repaymentMode = repaymentMode;
                            startAct(CreditCardMsgActivity.class, bean);
                        } else {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                    
                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        DyToast.getInstance().error(data.getMessage());
                    }
                }));*/
    }

    //*****************************jft********************************end*************************************
    
    @Subscribe(sticky = true)
    public void onEvent(String onEvent) {
        if (TextUtils.isEmpty(onEvent)) {
            return;
        }
        if (onEvent.equals(AppCacheInfo.mRefreshUserInfo)) {
            // 通道进件成功之后发送的这个event,
            requestChannelList();
        }
    }


    @Override
    public void rightClick() {
        // 一键还款
        jftIsJoin("3004","一键");
    }
}
