package com.dykj.youfeng.home.kf;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mode.ChatBean;
import com.dykj.youfeng.mode.KfBean;
import com.dykj.youfeng.mode.SmsBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.BaseToolsUtil;
import com.dykj.module.util.DialogUtils;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.date.DateFormatUtil;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 客服
 */
public class KfActivity extends BaseActivity {

    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.iv_send)
    TextView ivSend;
    @BindView(R.id.wxkfText)
    TextView wxkfText;


    private KfAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private KfBean kfBean;
    private List<ChatBean> list = new ArrayList<>();


    @Override
    public int intiLayout() {
        return R.layout.activity_kf;
    }

    @Override
    public void initData() {
        initToolBar(false);
    }

    @Override
    public void doBusiness() {
        mAdapter = new KfAdapter(R.layout.item_chat, list);
        mRecycler.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        getIndex();
        kfService();
    }


    @OnClick({R.id.iv_send,R.id.wxkfText})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_send:
                String msg = etMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    ToastUtils.showShort("请输入问题编号");
                    return;
                }
                getMsg(msg);
                break;
            case R.id.wxkfText:


                // 获取系统剪贴板
                ClipboardManager clipboard = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);
                String ssd =MMKV.defaultMMKV().decodeString(AppCacheInfo.mKefuwechat);
                // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
                ClipData clipData = ClipData.newPlainText("Label", MMKV.defaultMMKV().decodeString(AppCacheInfo.mKefuwechat));
                // 把数据集设置（复制）到剪贴板
                clipboard.setPrimaryClip(clipData);
                DyToast.getInstance().info("客服微信号已复制，请微信添加客服好友");
                break;

        /*    case R.id.tv_call:
                BaseToolsUtil.askPermissions(this, new BaseToolsUtil.PermissionInterface() {
                    @Override
                    public void ok() {
                        showCallDialog();
                    }

                    @Override
                    public void error() {
                        DyToast.getInstance().warning("您已拒绝拨打电话相关权限，无法使用该功能");
                        finish();
                    }
                }, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);


                break;
                */

        }

    }

    /**
     * 请求问题 编号
     *
     * @param msg
     */
    private void getMsg(String msg) {
        if ((Integer.valueOf(msg) - 1 > kfBean.list.size()) || ((Integer.valueOf(msg) - 1 == -1) || (Integer.valueOf(msg) - 1 == kfBean.list.size()))) {
            DyToast.getInstance().warning("请输入正确的序号");
            return;
        }
        list.add(new ChatBean(false, msg));
        mAdapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(list.size() - 1);
        etMsg.setText("");
        etMsg.clearComposingText();
        int id = kfBean.list.get(Integer.valueOf(msg) - 1).id;

        HttpFactory.getInstance().kfInfo(MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, ""), id + "")
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(KfActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<String>>() {
                    @Override
                    public void success(HttpResponseData<String> data) {
                        if ("9999".equals(data.status)) {
                            list.add(new ChatBean(true, data.getData(), DateFormatUtil.getDateTime()));
                            mAdapter.notifyDataSetChanged();
                            layoutManager.scrollToPosition(list.size() - 1);
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
     * 拨打客服电话
     */
   /* private void showCallDialog() {
        DialogUtils.getInstance()
                .with(this)
                .setlayoutPosition(Gravity.CENTER)
                .setlayoutPading(140, 0, 140, 0)
                .setlayoutAnimaType(DialogUtils.ANIM_FADE_IN)
                .setlayoutId(R.layout.dialog_public)
                .setOnChildViewclickListener((view, layoutResId) -> {
                    TextView tvTel = view.findViewById(R.id.tv_content);
                    tvTel.setText(tvCallTel.getText().toString());

                    view.findViewById(R.id.tv_cancel).setOnClickListener(v -> DialogUtils.dismiss());
                    view.findViewById(R.id.tv_safety_quit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String sTel = tvCallTel.getText().toString();
                            if (!TextUtils.isEmpty(sTel)) {
                                BaseToolsUtil.call(KfActivity.this, sTel);
                            } else {
                                DyToast.getInstance().warning("客服电话为空!");
                            }
                            DialogUtils.dismiss();
                        }
                    });
                })
                .show();
    }*/


    /**
     * 客服列表
     */
    private void getIndex() {
        HttpFactory.getInstance().kfList()
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(KfActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<KfBean>>() {
                    @Override
                    public void success(HttpResponseData<KfBean> data) {
                        if ("9999".equals(data.status)) {
                            kfBean = data.getData();
                            if (kfBean.list.size() > 0) {
                                String t = "尊敬的客户，您好！我是您的智能管家。请输入数字" + kfBean.start + "到"
                                        + kfBean.end + "我们将光速为您解决问题。";
                                for (int i = 0; i < kfBean.list.size(); i++) {
                                    t = t + "\n" + (i + 1) + "、" + kfBean.list.get(i).question;
                                }
                                list.add(new ChatBean(true, t, DateFormatUtil.getDateTime()));
                                mAdapter.notifyDataSetChanged();
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
     * 客服中心
     */
    private void kfService() {
        HttpFactory.getInstance().kfService()
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(KfActivity.this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<SmsBean>>() {
                    @Override
                    public void success(HttpResponseData<SmsBean> data) {
                        if ("9999".equals(data.status)) {
                            String serviceTel = data.getData().serviceTel;
                            if (!TextUtils.isEmpty(serviceTel)) {
                               // tvCallTel.setText(serviceTel);
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

}
