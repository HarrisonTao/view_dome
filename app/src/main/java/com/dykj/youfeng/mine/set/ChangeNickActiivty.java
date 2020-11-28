package com.dykj.youfeng.mine.set;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.dykj.youfeng.R;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/***
 * 修改昵称
 */
public class ChangeNickActiivty extends BaseActivity {
    @BindView(R.id.et_new_psw)
    EditText etNewNk;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    public int intiLayout() {
        return R.layout.activity_change_nick;
    }

    @Override
    public void initData() {
        initTitle("昵称");


        tvSubmit.setOnClickListener(v -> {
            String newNick = etNewNk.getText().toString().trim();
            if (TextUtils.isEmpty(newNick)){
                DyToast.getInstance().warning("请输入新的昵称!");
                return;
            }


            Map<String, Object> parms = new HashMap<>();
            parms.put("token", MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""));
            parms.put("nickname", newNick);
            HttpFactory.getInstance().editNick(parms)
                    .compose(HttpObserver.schedulers(getAty()))
                    .as(HttpObserver.life(ChangeNickActiivty.this))
                    .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                        @Override
                        public void success(HttpResponseData data) {
                            if ("9999".equals(data.status)) {
                                MMKV.defaultMMKV().encode(AppCacheInfo.mNickName,newNick);
                                EventBus.getDefault().postSticky(AppCacheInfo.mRefreshUserInfo);
                                DyToast.getInstance().success(data.message);
                                finish();
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



        });
    }

    @Override
    public void doBusiness() {

    }


}
