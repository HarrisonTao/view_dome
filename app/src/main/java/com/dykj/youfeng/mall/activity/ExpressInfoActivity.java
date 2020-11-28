package com.dykj.youfeng.mall.activity;


import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.ExpressAdapter;
import com.dykj.youfeng.mode.ExpInfoBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author lcjing
 * 查看物流
 */
public class ExpressInfoActivity extends BaseActivity {


    @BindView(R.id.iv_goods_img)
    ImageView ivGoodsImg;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_exp)
    TextView tvExp;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.rv_express)
    RecyclerView rvExpress;

    private List<ExpInfoBean.DataBean> list;
    private ExpressAdapter adapter;

    @Override
    public int intiLayout() {
        return R.layout.activity_wu_liu_info;
    }

    @Override
    public void initData() {
        initTitle("查看物流");
    }


    @Override
    public void doBusiness() {
        list = new ArrayList<>();

        adapter = new ExpressAdapter(R.layout.item_express, list);
        rvExpress.setAdapter(adapter);
        Map<String ,String> map=(Map<String ,String>)getIntentData();
        GlideUtils.setMallImage(ivGoodsImg,map.get("goodsImg"));
        getData(map.get("orderId"));
        kfService();
    }

    private void getData(String orderId) {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().getExpress(mToken, orderId)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ExpInfoBean>>() {
                    @Override
                    public void success(HttpResponseData<ExpInfoBean> data) {
                        if ("9999".equals(data.status)) {
                            switch (data.getData().state) {
                                case "0":
                                    tvStatus.setText("在途");
                                    break;
                                case "1":
                                    tvStatus.setText("揽收");
                                    break;
                                case "2":
                                    tvStatus.setText("疑难");
                                    break;
                                case "3":
                                    tvStatus.setText("签收");
                                    break;
                                case "4":
                                    tvStatus.setText("退签");
                                    break;
                                case "5":
                                    tvStatus.setText("派件");
                                    break;
                                case "6":
                                    tvStatus.setText("退回");
                                    break;
                                default:
                                    break;
                            }
                            tvExp.setText(String.format("%s：%s", data.getData().kuaidi_name, data.getData().nu));
                            list.clear();
                            list.addAll(data.getData().data);
                            adapter.notifyDataSetChanged();
                            tvService.setText(String.format("客服电话：%s", servicePhone));
                        } else {
                            finish();
                            DyToast.getInstance().error(data.message);
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }else {
                            DyToast.getInstance().error("获取物流信息失败");
                        }
                        finish();
                    }
                }));
    }



    private String servicePhone="";

    /**
     * 客服中心
     */
    private void kfService() {
        HttpFactory.getInstance().kfService()
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {

                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if ("9999".equals(data.status)) {
                            servicePhone=data.getData().serviceTel;
                            if (list.size()>0) {
                                tvService.setText(String.format("客服电话：%s", data.getData().serviceTel));
                            }

                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                    }
                }));
    }
}
