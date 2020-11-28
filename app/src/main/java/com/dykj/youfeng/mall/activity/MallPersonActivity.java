package com.dykj.youfeng.mall.activity;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.MallUserInfo;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.youfeng.tools.ArithUtil;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.GlideUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lcjing
 */
public class MallPersonActivity extends BaseActivity {


    @BindView(R.id.iv_user_header)
    RoundedImageView ivUserHeader;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_collect_num)
    TextView tvCollectNum;
    @BindView(R.id.tv_point_num)
    TextView tvPointNum;

    @Override
    public int intiLayout() {
        return R.layout.activity_mall_person;
    }

    @Override
    public void initData() {
        initTitle("个人中心");
    }

    @Override
    public void doBusiness() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().userIndex(mToken)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<MallUserInfo>>() {
                    @Override
                    public void success(HttpResponseData<MallUserInfo> data) {
                        if ("9999".equals(data.status)) {
                            tvUserName.setText(data.getData().nickname);
                            tvCollectNum.setText(data.getData().kcount);
                            tvPointNum.setText(data.getData().points);
                            tvPhone.setText(ArithUtil.hintPhone(data.getData().phone));
                            GlideUtils.setMallImage(ivUserHeader, R.mipmap.moren_tx, data.getData().avatar);
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


    @OnClick({R.id.ll_collect, R.id.ll_point, R.id.tv_order, R.id.tv_dfk, R.id.tv_dsh, R.id.tv_dpj, R.id.tv_sh, R.id.tv_my_car, R.id.tv_address, R.id.tv_my_evaluate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_collect:
                startAct(GoodsCollectActivity.class,0,1);
                break;
            case R.id.ll_point:
                break;
            case R.id.tv_order:
                startAct(OrderListActivity.class, 0);
                break;
            case R.id.tv_dfk:
                startAct(OrderListActivity.class, 1);
                break;
            case R.id.tv_dsh:
                startAct(OrderListActivity.class, 3);
                break;
            case R.id.tv_dpj:
                startAct(OrderListActivity.class, 4);
                break;
            case R.id.tv_sh:
                startAct(RefundListActivity.class);
                break;
            case R.id.tv_my_car:
                startAct(CarActivity.class);
                break;
            case R.id.tv_address:
                startAct(AddressListActivity.class);
                break;
            case R.id.tv_my_evaluate:
                startAct(MyEvaActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doBusiness();
    }
}
