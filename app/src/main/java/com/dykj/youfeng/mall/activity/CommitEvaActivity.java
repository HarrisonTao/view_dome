package com.dykj.youfeng.mall.activity;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.dykj.youfeng.R;
import com.dykj.youfeng.mall.adapter.EvaInfoAdapter;
import com.dykj.youfeng.mode.EvaInfoBean;
import com.dykj.youfeng.network.card_api.HttpFactory;
import com.dykj.youfeng.tools.AppCacheInfo;
import com.dykj.module.AppConstant;
import com.dykj.module.base.BaseActivity;
import com.dykj.module.net.HttpListener;
import com.dykj.module.net.HttpObserver;
import com.dykj.module.net.HttpResponseData;
import com.dykj.module.util.MyLogger;
import com.dykj.module.util.toast.DyToast;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lcjing
 */
public class CommitEvaActivity extends BaseActivity {
    @BindView(R.id.rv_eva)
    RecyclerView rvEva;
    private EvaInfoAdapter adapter;
    private List<EvaInfoBean> list;

//    @BindView(R.id.iv_goods)
//    ImageView ivGoods;
//    @BindView(R.id.tv_goods_name)
//    TextView tvGoodsName;
//    @BindView(R.id.tv_goods_type)
//    TextView tvGoodsType;
//    @BindView(R.id.tv_goods_count)
//    TextView tvGoodsCount;
//    @BindView(R.id.et_content)
//    EditText etContent;
//    @BindView(R.id.rv_img)
//    RecyclerView rvImg;
//    private List<String> imgs;
//    private EvaImgAdapter imgAdapter;


    @Override
    public int intiLayout() {
        return R.layout.activity_commit_eva;
    }

    @Override
    public void initData() {
        initTitle("发布评价");
    }

    @Override
    public void doBusiness() {

        list = new ArrayList<>();
        adapter = new EvaInfoAdapter(this, R.layout.item_eva_info, list);
        rvEva.setAdapter(adapter);

        orderId = getIntentData().toString();
        getOrderInfo();

    }


    private String orderId;


    private void getOrderInfo() {
        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().evaluation(mToken, orderId)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<List<EvaInfoBean>>>() {
                    @Override
                    public void success(HttpResponseData<List<EvaInfoBean>> data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            //设置数据
                            list.clear();
                            list.addAll(data.getData());
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).imgs = new ArrayList<>();
                                list.get(i).imgs.add("");
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());

                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));

    }


    @OnClick(R.id.tv_commit)
    public void onViewClicked() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String content = list.get(i).getEtContent().getText().toString();
            if (StringUtils.isEmpty(content)) {
                DyToast.getInstance().warning("请输入评价内容");
                return;
            }
            sb.append(list.get(i).goods_id).append(",").append(content).append(",");
            for (String s : list.get(i).imgs) {
                if (StringUtils.isEmpty(s)) {
                    break;
                }
                sb.append(s).append(";");
            }
            sb.append("|");
        }
//        if (sb.length()<2) {
//            sb.append("  ");
//        }

        String str = sb.toString();

        String mToken = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
        HttpFactory.getMallInstance().doEvaluation(mToken, orderId, str)
                .compose(HttpObserver.schedulers(this))
                .as(HttpObserver.life(this))
                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData>() {
                    @Override
                    public void success(HttpResponseData data) {
                        if (data.status.equals(AppConstant.SUCCESS)) {
                            //设置数据
                            setResult(1);
                            finish();
                        } else {
                            DyToast.getInstance().warning(data.getMessage());
                        }
                    }

                    @Override
                    public void error(Throwable data) {
                        MyLogger.dLog().e(data);
                        if (!"数据解析异常".equals(data.getMessage())) {
                            DyToast.getInstance().error(data.getMessage());
                        }
                    }
                }));

    }


//    private void upImg(String mPhotoPath) {
//        String token = MMKV.defaultMMKV().decodeString(AppCacheInfo.mTokent, "");
//        File file = new File(mPhotoPath);
//        MultipartBody build = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
//                .addFormDataPart("token", token)
//                .build();
//        HttpFactory.getMallInstance().upImg(build)
//                .compose(HttpObserver.schedulers(getAty()))
//                .as(HttpObserver.life(this))
//                .subscribe(new HttpObserver<>(new HttpListener<HttpResponseData<ImageInfoBean>>() {
//                    @Override
//                    public void success(HttpResponseData<ImageInfoBean> data) {
//                        if ("9999".equals(data.status)) {
//                            if (imgs.size()>0&& StringUtils.isEmpty( imgs.get(imgs.size() - 1))) {
//                                imgs.remove(imgs.size() - 1);
//                            }
//                            imgs.add(data.getData().images);
//                            if (imgs.size() < 3) {
//                                imgs.add("");
//                            }
//                            imgAdapter.refreshData();
//                        } else {
//                            DyToast.getInstance().error(data.message);
//                        }
//                    }
//
//                    @Override
//                    public void error(Throwable data) {
//                        MyLogger.dLog().e(data);
//                        DyToast.getInstance().error(data.getMessage());
//                    }
//                }));
//    }

}
